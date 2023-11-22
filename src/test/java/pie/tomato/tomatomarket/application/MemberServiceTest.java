package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.member.MemberService;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.MemberTown;
import pie.tomato.tomatomarket.domain.Region;
import pie.tomato.tomatomarket.presentation.member.response.MemberRegionResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class MemberServiceTest {

	@Autowired
	private MemberService memberService;
	@Autowired
	private SupportRepository supportRepository;

	@DisplayName("사용자의 닉네임 변경 요청에 성공한다.")
	@Test
	void editNickname() {
		// given
		Member member = setupMember("파이", "123@123", "profile");

		// when
		memberService.modifyNickname(member.getId(), "파이에오");

		// then
		Member editMember = supportRepository.findById(member.getId(), Member.class);
		assertThat(editMember.getNickname()).isEqualTo("파이에오");
	}

	@DisplayName("사용자의 프로필 이미지 변경에 성공한다.")
	@Test
	void editThumbnail() {
		// given
		Member member = setupMember("파이", "123@123", "profile");
		String original = member.getProfile();
		MockMultipartFile thumbnail = createMockMultipartFile("test-image");

		// when
		memberService.modifyProfile(member.getId(), thumbnail);
		Member findMember = supportRepository.findById(member.getId(), Member.class);

		// then
		assertThat(findMember.getProfile()).isNotEqualTo(original);
	}

	@DisplayName("사용자의 동네 정보를 불러온다.")
	@Test
	void memberRegions() {
		// given
		Member member1 = setupMember("파이", "123@123", "profile");
		Member member2 = setupMember("유저", "email@email", "image");

		Principal principal = setPrincipal(member1);
		Principal principal2 = setPrincipal(member2);

		Region region1 = setupRegion("강서구 우장산동", "우장산동");
		Region region2 = setupRegion("강서구 마곡동", "마곡동");
		Region region3 = setupRegion("강서구 내발산동", "내발산동");

		setupMemberTown(region1, member1, true);
		setupMemberTown(region2, member1, false);

		setupMemberTown(region3, member2, true);

		// when
		List<MemberRegionResponse> member1Regions = memberService.findAllRegions(principal);
		List<MemberRegionResponse> member2Regions = memberService.findAllRegions(principal2);

		//then
		assertAll(
			() -> assertThat(member1Regions.size()).isEqualTo(2),
			() -> assertThat(member2Regions.size()).isEqualTo(1)
		);
	}

	Principal setPrincipal(Member member) {
		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
		return principal;
	}

	Member setupMember(String nickname, String email, String profile) {
		return supportRepository.save(new Member(nickname, email, profile));
	}

	Region setupRegion(String fullName, String shortName) {
		return supportRepository.save(new Region(fullName, shortName));
	}

	MemberTown setupMemberTown(Region region, Member member, boolean isSelected) {
		return supportRepository.save(MemberTown.of(region, member, isSelected));
	}

	private MockMultipartFile createMockMultipartFile(String filename) {
		return new MockMultipartFile(
			filename, "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));
	}
}

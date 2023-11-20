package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.member.MemberService;
import pie.tomato.tomatomarket.domain.Member;
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
		Member member = setupMember();

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
		Member member = setupMember();
		String original = member.getProfile();
		MockMultipartFile thumbnail = createMockMultipartFile("test-image");

		// when
		memberService.modifyProfile(member.getId(), thumbnail);
		Member findMember = supportRepository.findById(member.getId(), Member.class);

		// then
		assertThat(findMember.getProfile()).isNotEqualTo(original);
	}

	Member setupMember() {
		return supportRepository.save(new Member("파이", "123@123", "profile"));
	}

	private MockMultipartFile createMockMultipartFile(String filename) {
		return new MockMultipartFile(
			filename, "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));
	}
}

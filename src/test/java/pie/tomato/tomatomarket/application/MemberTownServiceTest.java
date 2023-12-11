package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.memberTown.MemberTownService;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.MemberTown;
import pie.tomato.tomatomarket.domain.Region;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.memberTown.request.AddMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.DeleteMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.SelectMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.response.MemberTownListResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@SpringBootTest
@Transactional
class MemberTownServiceTest {

	@Autowired
	private SupportRepository supportRepository;
	@Autowired
	private MemberTownService memberTownService;

	@DisplayName("지역(동네) 추가에 성공한다.")
	@Test
	void addMemberTown_ver1() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region1 = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		setupRegion("서울특별시 강남구 신사동", "신사동");

		// when
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region1.getId()));

		// then
		List<MemberTown> towns = supportRepository.findAll(MemberTown.class);
		assertThat(towns.size()).isEqualTo(1);
	}

	@DisplayName("지역(동네) 추가에 실패한다. : 이미 2개의 동네 선택을 완료했을 시")
	@Test
	void addMemberTown_ver2() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region1 = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		Region region2 = setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		Region region3 = setupRegion("서울특별시 강남구 신사동", "신사동");

		// when
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region1.getId()));
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region2.getId()));

		// then
		assertThatThrownBy(
			() -> memberTownService.addMemberTown(principal, new AddMemberTownRequest(region3.getId())))
			.isInstanceOf(BadRequestException.class)
			.extracting("ErrorCode")
			.isEqualTo(ErrorCode.MAXIMUM_MEMBER_TOWN_SIZE);
	}

	@DisplayName("지역(동네) 추가에 실패한다. : 이미 추가된 동네일 시")
	@Test
	void addMemberTown_ver3() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");

		// when
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region.getId()));

		// then
		assertThatThrownBy(
			() -> memberTownService.addMemberTown(principal, new AddMemberTownRequest(region.getId())))
			.isInstanceOf(BadRequestException.class)
			.extracting("ErrorCode")
			.isEqualTo(ErrorCode.ALREADY_ADDRESS);
	}

	@DisplayName("지역(동네) 선택에 성공한다.")
	@Test
	void selectMemberTown() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region1 = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		Region region2 = setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		setupRegion("서울특별시 강남구 신사동", "신사동");
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region1.getId()));
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region2.getId()));

		// when
		memberTownService.selectMemberTown(principal, new SelectMemberTownRequest(region1.getId()));

		// then
		MemberTown SelectMemberTown = supportRepository.findById(region1.getId(), MemberTown.class);
		MemberTown nonSelectMemberTown = supportRepository.findById(region2.getId(), MemberTown.class);
		assertAll(
			() -> assertThat(SelectMemberTown.isSelected()).isTrue(),
			() -> assertThat(nonSelectMemberTown.isSelected()).isFalse()
		);
	}

	@DisplayName("주어진 지역 이름으로 지역(동네) 조회에 성공한다.")
	@Test
	void findAllMemberTown() {
		// given
		setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		setupRegion("서울특별시 강남구 신사동", "신사동");
		setupRegion("서울특별시 종로구 사직동", "사직동");
		setupRegion("서울특별시 종로구 삼청동", "삼청동");

		// when
		CustomSlice<MemberTownListResponse> response1 =
			memberTownService.findAll("강남구", 10, null);

		// then
		assertThat(response1.getContents().size()).isEqualTo(3);
	}

	@DisplayName("지역(동네) 삭제에 성공한다. : 선택된 동네가 2개 일 시")
	@Test
	void deleteMemberTown_ver1() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region1 = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		Region region2 = setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		setupRegion("서울특별시 강남구 신사동", "신사동");
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region1.getId()));
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region2.getId()));
		memberTownService.selectMemberTown(principal, new SelectMemberTownRequest(region1.getId()));

		// when
		memberTownService.deleteMemberTown(principal, new DeleteMemberTownRequest(region1.getId()));
		List<MemberTown> memberTowns = supportRepository.findAll(MemberTown.class);

		// then
		assertAll(
			() -> assertThat(memberTowns.get(0).isSelected()).isTrue(),
			() -> assertThat(memberTowns.size()).isEqualTo(1)
		);
	}

	@DisplayName("지역(동네) 삭제에 실패한다. : 선택된 동네가 1개 일 시")
	@Test
	void deleteMemberTown_ver2() {
		// given
		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Region region1 = setupRegion("서울특별시 강남구 역삼1동", "역삼1동");
		setupRegion("서울특별시 강남구 역삼2동", "역삼2동");
		setupRegion("서울특별시 강남구 신사동", "신사동");
		memberTownService.addMemberTown(principal, new AddMemberTownRequest(region1.getId()));

		// when & then
		assertThatThrownBy(
			() -> memberTownService.deleteMemberTown(principal, new DeleteMemberTownRequest(region1.getId())))
			.isInstanceOf(BadRequestException.class)
			.extracting("ErrorCode")
			.isEqualTo(ErrorCode.MINIMUM_MEMBER_TOWN_SIZE);
	}

	Region setupRegion(String fullName, String shortName) {
		return supportRepository.save(new Region(fullName, shortName));
	}

	Member setupMember() {
		return supportRepository.save(new Member("파이", "123@123", "profile"));
	}

	Principal setPrincipal(Member member) {
		return Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
	}
}

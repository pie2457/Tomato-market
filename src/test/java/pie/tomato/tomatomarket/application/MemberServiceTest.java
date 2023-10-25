package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
		Member member = supportRepository.save(
			new Member("123piepiepie123", "pie_choco@pie.com", "pie/image.jpg"));

		// when
		memberService.modifyNickname(member.getId(), "파이에오");

		// then
		Member editMember = supportRepository.findById(member.getId(), Member.class);
		assertThat(editMember.getNickname()).isEqualTo("파이에오");
	}
}

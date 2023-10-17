package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.oauth.KakaoClient;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;

@SpringBootTest
@Transactional
class AuthServiceTest {

	@Autowired
	private AuthService authService;
	@Autowired
	private MemberRepository memberRepository;
	@MockBean
	private KakaoClient kakaoClient;

	@DisplayName("회원가입에 성공한다.")
	@Test
	void signup() {
		// given
		accessTokenAndUserInfo(new Member("pie123", "pie-choco@pie.com", "pie/image.jpg"));

		// when
		authService.signup("code");

		// then
		assertThat(memberRepository.existsMemberByEmail("pie-choco@pie.com")).isTrue();
	}

	@DisplayName("중복된 email로 회원가입 요청 시 실패한다.")
	@Test
	void DuplicateEmailSignupFail() {
		// given
		Member saveMember = memberRepository.save(new Member("pie123", "pie-choco@pie.com", "pie/image.jpg"));
		accessTokenAndUserInfo(saveMember);

		// when & then
		assertThatThrownBy(
			() -> authService.signup("code")).isInstanceOf(BadRequestException.class)
			.extracting("errorCode").isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);
	}

	@DisplayName("로그인에 성공한다.")
	@Test
	void login() {
		// given
		Member saveMember =
			memberRepository.save(new Member("pie123", "pie-choco@pie.com", "pie/image.jpg"));
		accessTokenAndUserInfo(saveMember);

		// when & then
		assertThat(authService.login("code")).isNotBlank();

	}

	@DisplayName("회원가입을 하지 않은 상태에서 로그인 시도 시 실패한다.")
	@Test
	void loginFail() {
		// given
		accessTokenAndUserInfo(new Member("pie123", "pie-choco@pie.com", "pie/image.jpg"));

		// when & then
		assertThatThrownBy(
			() -> authService.login("code")).isInstanceOf(BadRequestException.class)
			.extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND_MEMBER);
	}

	void accessTokenAndUserInfo(Member member) {
		given(kakaoClient.getAccessToken(anyString())).willReturn("abc.abc.abc");
		given(kakaoClient.getUserInfo(anyString())).
			willReturn(member);
	}
}

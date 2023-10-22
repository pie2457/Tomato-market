package pie.tomato.tomatomarket.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.oauth.KakaoClient;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.OAuthUser;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.config.jwt.JwtProvider;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

	private final KakaoClient kakaoClient;
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	public void signup(String code) {
		String accessToken = kakaoClient.getAccessToken(code);
		OAuthUser oAuthUser = kakaoClient.getUserInfo(accessToken);
		validateDuplicateEmail(oAuthUser.getEmail());
		memberRepository.save(
			new Member(oAuthUser.getNickname(), oAuthUser.getEmail(), oAuthUser.getProfileUrl()));
	}

	public String login(String code) {
		String accessToken = kakaoClient.getAccessToken(code);
		OAuthUser oAuthUser = kakaoClient.getUserInfo(accessToken);
		Member member = memberRepository.findByEmail(oAuthUser.getEmail())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		return jwtProvider.createAccessToken(member.getId());
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsMemberByEmail(email)) {
			throw new BadRequestException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
	}
}

package pie.tomato.tomatomarket.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.oauth.KakaoClient;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.infrastructure.config.jwt.JwtProvider;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

	private final KakaoClient kakaoClient;
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	public void signup(String code) {
		String accessToken = kakaoClient.getAccessToken(code);
		Member member = kakaoClient.getUserInfo(accessToken);
		validateDuplicateEmail(member.getEmail());
		memberRepository.save(member);
	}

	public String login(String code) {
		String accessToken = kakaoClient.getAccessToken(code);
		Member member = kakaoClient.getUserInfo(accessToken);
		validateExistEmail(member.getEmail());
		return jwtProvider.createAccessToken(member.getId());
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsMemberByEmail(email)) {
			throw new BadRequestException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
	}

	private void validateExistEmail(String email) {
		if (!memberRepository.existsMemberByEmail(email)) {
			throw new BadRequestException(ErrorCode.NOT_FOUND_MEMBER);
		}
	}
}

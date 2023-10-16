package pie.tomato.tomatomarket.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.oauth.KakaoClient;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final KakaoClient kakaoClient;
	private final MemberRepository memberRepository;

	public void signup(String code) {
		String accessToken = kakaoClient.getAccessToken(code);
		Member member = kakaoClient.getUserInfo(accessToken);
		validateDuplicateEmail(member.getEmail());
		memberRepository.save(member);
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsMemberByEmail(email)) {
			throw new BadRequestException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
	}
}

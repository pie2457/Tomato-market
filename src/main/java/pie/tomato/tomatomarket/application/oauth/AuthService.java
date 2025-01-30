package pie.tomato.tomatomarket.application.oauth;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.OAuthProvider;
import pie.tomato.tomatomarket.domain.OAuthUser;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.config.jwt.JwtProvider;
import pie.tomato.tomatomarket.infrastructure.persistence.member.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final List<OAuthClient> oAuthClients;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(String code, OAuthProvider oAuthProvider) {
        OAuthClient oAuthClient = oAuthClients.stream().filter(authClient -> authClient.isSupport(oAuthProvider))
            .findFirst().orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        String accessToken = oAuthClient.getAccessToken(code);
        OAuthUser oAuthUser = oAuthClient.getUserInfo(accessToken);
        validateDuplicateEmail(oAuthUser.getEmail());
        memberRepository.save(
            new Member(oAuthUser.getNickname(), oAuthUser.getEmail(), oAuthUser.getProfileUrl()));
    }

    public String login(String code, OAuthProvider oAuthProvider) {
        OAuthClient oAuthClient = oAuthClients.stream().filter(authClient -> authClient.isSupport(oAuthProvider))
            .findFirst().orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        String accessToken = oAuthClient.getAccessToken(code);
        OAuthUser oAuthUser = oAuthClient.getUserInfo(accessToken);
        Member member = memberRepository.findByEmail(oAuthUser.getEmail())
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        return jwtProvider.createAccessToken(member.getId(), member.getEmail(), member.getNickname());
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
    }
}

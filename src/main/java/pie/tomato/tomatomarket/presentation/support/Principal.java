package pie.tomato.tomatomarket.presentation.support;

import java.util.Optional;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.UnAuthorizedException;

@Getter
@Builder
public class Principal {

	private Long memberId;
	private String nickname;
	private String email;

	public static Principal from(Claims claims) {
		PrincipalBuilder principal = Principal.builder();
		Optional.ofNullable(claims.get("memberId"))
			.ifPresentOrElse(memberId -> principal.memberId(Long.valueOf(memberId.toString())),
				() -> {
					throw new UnAuthorizedException(ErrorCode.INVALID_TOKEN);
				});
		Optional.ofNullable(claims.get("email"))
			.ifPresentOrElse(email -> principal.email((String)email),
				() -> {
					throw new UnAuthorizedException(ErrorCode.INVALID_TOKEN);
				});
		Optional.ofNullable(claims.get("nickname"))
			.ifPresentOrElse(nickname -> principal.nickname((String)nickname),
				() -> {
					throw new UnAuthorizedException(ErrorCode.INVALID_TOKEN);
				});
		return principal.build();
	}
}

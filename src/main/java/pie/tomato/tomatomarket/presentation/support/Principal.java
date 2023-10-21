package pie.tomato.tomatomarket.presentation.support;

import java.util.Optional;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Principal {

	private Long memberId;
	private String nickname;
	private String email;
	private String profile;

	public static Principal from(Claims claims) {
		PrincipalBuilder principal = Principal.builder();
		Optional.ofNullable(claims.get("memberId"))
			.ifPresent(memberId -> principal.memberId(Long.valueOf(memberId.toString())));
		Optional.ofNullable(claims.get("email"))
			.ifPresent(email -> principal.email((String)email));
		Optional.ofNullable(claims.get("nickname"))
			.ifPresent(nickname -> principal.nickname((String)nickname));
		Optional.ofNullable(claims.get("profile"))
			.ifPresent(profile -> principal.profile((String)profile));
		return principal.build();
	}
}

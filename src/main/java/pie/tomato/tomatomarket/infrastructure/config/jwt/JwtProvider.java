package pie.tomato.tomatomarket.infrastructure.config.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.UnAuthorizedException;
import pie.tomato.tomatomarket.infrastructure.config.properties.JwtProperties;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Component
public class JwtProvider {

	private final SecretKey secretKey;
	private final long accessTokenExpirationTime;

	public JwtProvider(JwtProperties jwtProperties) {
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpirationTime = jwtProperties.getAccessTokenExpirationTime();
	}

	public String createAccessToken(Long memberId) {
		Date now = new Date();
		Date accessTokenExpiration = new Date(now.getTime() + accessTokenExpirationTime);

		return Jwts.builder()
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.setIssuedAt(now)
			.setExpiration(accessTokenExpiration)
			.addClaims(Map.of("memberId", memberId))
			.compact();
	}

	public void validateToken(final String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(ErrorCode.EXPIRED_TOKEN);
		} catch (JwtException e) {
			throw new UnAuthorizedException(ErrorCode.INVALID_TOKEN);
		}
	}

	public Principal extractPrincipal(final String token) {
		final Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Principal.from(claims);
	}
}

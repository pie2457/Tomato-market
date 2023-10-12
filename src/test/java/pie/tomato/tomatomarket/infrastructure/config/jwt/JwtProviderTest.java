package pie.tomato.tomatomarket.infrastructure.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.UnAuthorizedException;
import pie.tomato.tomatomarket.infrastructure.config.properties.JwtProperties;

class JwtProviderTest {

	private final String secretKey = "1e57a452a094728c291bc42bf2bc7eb8d9fd8844d1369da2bf728588b46c4e75";
	private final JwtProvider jwtProvider = new JwtProvider(
		new JwtProperties(secretKey, 100000, 100000));

	@Test
	@DisplayName("회원의 ID가 주어지면 토큰을 생성한다.")
	void createToken() {
		// given
		String accessToken = jwtProvider.createAccessToken(1L);

		// when & then
		assertThat(accessToken).isNotBlank();
	}

	@Test
	@DisplayName("잘못된 토큰이 들어오면 예외가 던져진다.")
	void invalidToken() {
		// given
		String invalidToken = "qwe.123.q12ew2";

		// when & then
		assertThatThrownBy(
			() -> jwtProvider.validateToken(invalidToken)).isInstanceOf(UnAuthorizedException.class)
			.extracting("ErrorCode").isEqualTo(ErrorCode.INVALID_TOKEN);
	}
}

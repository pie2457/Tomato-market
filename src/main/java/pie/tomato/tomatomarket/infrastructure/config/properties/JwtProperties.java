package pie.tomato.tomatomarket.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties("jwt")
public class JwtProperties {

	private final String secretKey;
	private final long accessTokenExpirationTime;

	@ConstructorBinding
	public JwtProperties(String secretKey, long accessTokenExpirationTime) {
		this.secretKey = secretKey;
		this.accessTokenExpirationTime = accessTokenExpirationTime;
	}
}

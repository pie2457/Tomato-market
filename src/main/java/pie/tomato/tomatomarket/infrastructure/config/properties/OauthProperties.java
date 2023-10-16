package pie.tomato.tomatomarket.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties("oauth")
public class OauthProperties {

	private final String clientId;
	private final String secretId;
	private final String oauthUrl;
	private final String tokenUrl;
	private final String resourceUrl;

	@ConstructorBinding
	public OauthProperties(String clientId, String secretId, String oauthUrl, String tokenUrl,
		String resourceUrl) {
		this.clientId = clientId;
		this.secretId = secretId;
		this.oauthUrl = oauthUrl;
		this.tokenUrl = tokenUrl;
		this.resourceUrl = resourceUrl;
	}
}

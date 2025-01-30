package pie.tomato.tomatomarket.application.oauth;

import pie.tomato.tomatomarket.domain.OAuthProvider;
import pie.tomato.tomatomarket.domain.OAuthUser;

public interface OAuthClient {
    boolean isSupport(OAuthProvider provider);

    String getAccessToken(String code);

    OAuthUser getUserInfo(String accessToken);
}

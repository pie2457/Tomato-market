package pie.tomato.tomatomarket.application.oauth;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.OAuthProvider;
import pie.tomato.tomatomarket.domain.OAuthUser;
import pie.tomato.tomatomarket.infrastructure.config.properties.OauthProperties;

@Component
@RequiredArgsConstructor
public class NaverClient implements OAuthClient {
    private final OauthProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public boolean isSupport(OAuthProvider provider) {
        return OAuthProvider.NAVER == provider;
    }

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity<>(createTokenRequestBody(code), headers);
        Map<String, Object> response = restTemplate.postForObject(properties.getNaver().getProvider().getTokenUrl(),
            request, Map.class);
        return response.get("access_token").toString();
    }

    private MultiValueMap<String, String> createTokenRequestBody(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.getNaver().getUser().getClientId());
        params.add("redirect_uri", "http://localhost:8080/api/auth/kakao/oauth");
        params.add("code", code);
        params.add("client_secret", properties.getNaver().getUser().getClientSecret());
        return params;
    }

    @Override
    public OAuthUser getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        HttpEntity request = new HttpEntity<>(headers);
        Map<String, Object> response = restTemplate.postForObject(properties.getNaver().getProvider().getUserInfoUrl(),
            request, Map.class);

        return OAuthUser.from(response);
    }
}

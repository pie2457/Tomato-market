package pie.tomato.tomatomarket.application.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.infrastructure.config.properties.OauthProperties;

@RequiredArgsConstructor
@Component
public class KakaoClient {

	private final OauthProperties kakao;

	public String getAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String url = kakao.getTokenUrl();
		HttpEntity request = new HttpEntity<>(createTokenRequestBody(code), headers);
		Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
		return response.get("access_token").toString();
	}

	private MultiValueMap<String, String> createTokenRequestBody(final String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakao.getClientId());
		params.add("redirect_uri", "http://localhost:8080/api/auth/kakao/oauth");
		params.add("code", code);
		params.add("client_secret", kakao.getSecretId());
		return params;
	}

	public Member getUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(accessToken);

		String resourceUrl = kakao.getResourceUrl();
		HttpEntity request = new HttpEntity<>(headers);
		Map<String, Object> response = restTemplate.postForObject(resourceUrl, request, Map.class);

		Map<String, Object> kakaoAccount = (Map<String, Object>)response.get("kakao_account");
		String email = kakaoAccount.get("email").toString();
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
		String uuid = UUID.randomUUID().toString();
		String nickname = profile.get("nickname").toString() + uuid;
		String profileImageUrl = profile.get("profile_image_url").toString();
		return new Member(nickname, email, profileImageUrl);
	}
}

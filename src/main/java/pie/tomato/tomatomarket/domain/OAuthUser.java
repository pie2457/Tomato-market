package pie.tomato.tomatomarket.domain;

import java.util.Map;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthUser {

	private final String email;
	private final String nickname;
	private final String profileUrl;

	public static OAuthUser from(Map<String, Object> response) {
		Map<String, Object> kakaoAccount = (Map<String, Object>)response.get("kakao_account");
		String email = kakaoAccount.get("email").toString();
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
		String uuid = UUID.randomUUID().toString();
		String nickname = profile.get("nickname").toString() + uuid;
		String profileImageUrl = profile.get("profile_image_url").toString();
		return new OAuthUser(email, nickname, profileImageUrl);
	}
}

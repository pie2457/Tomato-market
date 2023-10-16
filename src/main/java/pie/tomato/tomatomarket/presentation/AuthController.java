package pie.tomato.tomatomarket.presentation;

import static org.springframework.http.HttpStatus.*;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.AuthService;
import pie.tomato.tomatomarket.infrastructure.config.properties.OauthProperties;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final OauthProperties kakao;

	@GetMapping("/code")
	@ResponseStatus(FOUND)
	public ResponseEntity<String> signupCode() {
		HttpHeaders headers = new HttpHeaders();
		String redirectUrl =
			"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
				+ kakao.getClientId() +
				"&redirect_uri=http://localhost:8080/api/auth/kakao/signup";
		headers.setLocation(URI.create(redirectUrl));
		return new ResponseEntity<>(headers, MOVED_PERMANENTLY);
	}

	@GetMapping("/kakao/signup")
	public ResponseEntity<Void> signup(@RequestParam("code") String code) {
		authService.signup(code);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}

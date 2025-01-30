package pie.tomato.tomatomarket.presentation.auth;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.oauth.AuthService;
import pie.tomato.tomatomarket.domain.OAuthProvider;
import pie.tomato.tomatomarket.presentation.auth.response.LoginResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/{oAuthProvider}")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    public ResponseEntity<Void> signup(@RequestParam String code,
        @PathVariable OAuthProvider oAuthProvider) {
        authService.signup(code, oAuthProvider);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String code,
        @PathVariable OAuthProvider oAuthProvider) {
        return ResponseEntity.status(OK).body(new LoginResponse(authService.login(code, oAuthProvider)));
    }
}

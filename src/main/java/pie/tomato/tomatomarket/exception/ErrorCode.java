package pie.tomato.tomatomarket.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// JWT
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
	EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
	EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다."),

	// Member
	ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원을 찾지 못하였습니다."),
	ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}

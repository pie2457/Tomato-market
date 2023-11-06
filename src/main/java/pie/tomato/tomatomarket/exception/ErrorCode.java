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
	ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

	// Image
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 확장자입니다."),
	FILE_IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 입출력에 실패했습니다."),

	// Item
	INVALID_STATUS(HttpStatus.BAD_REQUEST, "상태는 판매중, 예약중, 판매완료만 들어올 수 있습니다."),
	NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	ITEM_FORBIDDEN(HttpStatus.FORBIDDEN, "상품에 대한 권한이 없습니다."),

	// Category
	NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

	// Sales
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

	// Auth
	NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인 상태가 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;
}

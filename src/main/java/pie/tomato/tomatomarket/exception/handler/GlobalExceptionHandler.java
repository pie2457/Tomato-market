package pie.tomato.tomatomarket.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorResponse;
import pie.tomato.tomatomarket.exception.UnAuthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.badRequest()
			.body(new ErrorResponse(400, e.getMessage()));
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(401, e.getMessage()));
	}
}

package pie.tomato.tomatomarket.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorResponse;
import pie.tomato.tomatomarket.exception.InternalServerException;
import pie.tomato.tomatomarket.exception.NotFoundException;
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

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponse(404, e.getMessage()));
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(500, e.getMessage()));
	}
}

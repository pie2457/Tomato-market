package pie.tomato.tomatomarket.exception;

public class ForbiddenException extends TomatoMarketException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ForbiddenException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}

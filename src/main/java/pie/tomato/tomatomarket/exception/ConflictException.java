package pie.tomato.tomatomarket.exception;

public class ConflictException extends TomatoMarketException {

	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ConflictException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}

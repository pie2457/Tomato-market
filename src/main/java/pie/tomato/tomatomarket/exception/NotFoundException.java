package pie.tomato.tomatomarket.exception;

public class NotFoundException extends TomatoMarketException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NotFoundException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}

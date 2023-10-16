package pie.tomato.tomatomarket.exception;

public class BadRequestException extends TomatoMarketException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}

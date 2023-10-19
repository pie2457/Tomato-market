package pie.tomato.tomatomarket.exception;

public class InternalServerException extends TomatoMarketException {

	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}

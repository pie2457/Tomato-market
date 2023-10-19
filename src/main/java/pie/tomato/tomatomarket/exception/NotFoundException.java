package pie.tomato.tomatomarket.exception;

public class NotFoundException extends TomatoMarketException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}

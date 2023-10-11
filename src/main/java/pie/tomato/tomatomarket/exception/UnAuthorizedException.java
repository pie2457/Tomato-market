package pie.tomato.tomatomarket.exception;

public class UnAuthorizedException extends TomatoMarketException {

	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}

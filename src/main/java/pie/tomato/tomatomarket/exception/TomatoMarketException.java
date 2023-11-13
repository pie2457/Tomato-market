package pie.tomato.tomatomarket.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TomatoMarketException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String message;

	public TomatoMarketException(ErrorCode errorCode) {
		this(errorCode, errorCode.getMessage());
	}
}

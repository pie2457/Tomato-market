package pie.tomato.tomatomarket.domain;

import java.util.Arrays;

import lombok.Getter;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;

@Getter
public enum WishStatus {

	YES,
	NO;

	public static WishStatus of(String status) {
		return Arrays.stream(WishStatus.values())
			.filter(wishStatus -> wishStatus.name().equals(status.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_PARAMETER));
	}
}

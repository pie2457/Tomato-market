package pie.tomato.tomatomarket.domain;

import java.util.Arrays;

import lombok.Getter;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;

@Getter
public enum ItemStatus {

	ON_SALE("판매중"),
	SOLD_OUT("판매완료"),
	RESERVED("예약중");

	private final String name;

	ItemStatus(String name) {
		this.name = name;
	}

	public static ItemStatus from(String status) {
		return Arrays.stream(ItemStatus.values())
			.filter(itemStatus -> itemStatus.getName().equals(status))
			.findFirst()
			.orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_STATUS));
	}
}
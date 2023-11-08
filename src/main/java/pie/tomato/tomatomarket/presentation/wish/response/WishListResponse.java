package pie.tomato.tomatomarket.presentation.wish.response;

import java.time.LocalDateTime;

import lombok.Getter;
import pie.tomato.tomatomarket.domain.ItemStatus;

@Getter
public class WishListResponse {

	private Long itemId;
	private String thumbnailUrl;
	private String title;
	private String tradingRegion;
	private LocalDateTime createdAt;
	private Long price;
	private ItemStatus status;
	private Long wishCount;
	private Long chatCount;
	private String sellerId;
}

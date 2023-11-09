package pie.tomato.tomatomarket.presentation.item.response;

import java.time.LocalDateTime;

import lombok.Getter;
import pie.tomato.tomatomarket.domain.ItemStatus;

@Getter
public class SalesItemDetailResponse {

	private Long itemId;
	private String thumbnailUrl;
	private String title;
	private String tradingRegion;
	private LocalDateTime createdAt;
	private Long price;
	private ItemStatus status;
}

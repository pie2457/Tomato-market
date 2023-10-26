package pie.tomato.tomatomarket.presentation.request.item;

import java.time.LocalDateTime;

import lombok.Getter;
import pie.tomato.tomatomarket.domain.ItemStatus;

@Getter
public class ItemResponse {

	private Long itemId;
	private String thumbnailUrl;
	private String title;
	private String tradingRegion;
	private LocalDateTime createdAt;
	private Long price;
	private ItemStatus status;
	private Long chatCount;
	private Long wishCount;
	private String isSeller;
}

package pie.tomato.tomatomarket.presentation.sales.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SalesItemDetailResponse {

	private Long itemId;
	private String thumbnailUrl;
	private String title;
	private String tradingRegion;
	private LocalDateTime createdAt;
	private Long price;
	private String status;
}

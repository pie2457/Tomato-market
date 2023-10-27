package pie.tomato.tomatomarket.presentation.request.item;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemModifyRequest {

	private String title;
	private Long price;
	private String content;
	@JsonProperty("region")
	private String tradingRegion;
	private String status;
	private Long categoryId;
	private List<String> deleteImageUrls;
	private String thumbnailImage;
}

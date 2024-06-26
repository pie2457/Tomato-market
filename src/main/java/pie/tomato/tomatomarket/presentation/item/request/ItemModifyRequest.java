package pie.tomato.tomatomarket.presentation.item.request;

import static lombok.AccessLevel.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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

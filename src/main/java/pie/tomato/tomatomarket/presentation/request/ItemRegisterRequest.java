package pie.tomato.tomatomarket.presentation.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRegisterRequest {

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	private Long price;
	private String status;
	private String thumbnail;
	private String content;
	private String region;
	private Long categoryId;

	@Builder
	public Item toEntity(Member member, String thumbnailUrl, ItemStatus itemStatus) {
		return Item.builder()
			.title(title)
			.content(content)
			.price(price)
			.status(itemStatus)
			.region(region)
			.createdAt(LocalDateTime.now())
			.thumbnail(thumbnailUrl)
			.wishCount(0L)
			.chatCount(0L)
			.viewCount(0L)
			.member(member)
			.category(new Category(categoryId))
			.build();
	}
}

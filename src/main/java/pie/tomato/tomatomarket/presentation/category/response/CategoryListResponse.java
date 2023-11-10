package pie.tomato.tomatomarket.presentation.category.response;

import lombok.Getter;
import pie.tomato.tomatomarket.domain.Category;

@Getter
public class CategoryListResponse {

	private Long categoryId;
	private String categoryImage;
	private String categoryName;

	public CategoryListResponse(Category category) {
		this.categoryId = category.getId();
		this.categoryImage = category.getCategoryImage();
		this.categoryName = category.getName();
	}
}

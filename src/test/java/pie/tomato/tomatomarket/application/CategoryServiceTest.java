package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.category.CategoryService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.presentation.category.response.CategoryListResponse;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class CategoryServiceTest {

	@Autowired
	private SupportRepository supportRepository;
	@Autowired
	private CategoryService categoryService;

	@DisplayName("카테고리 목록 조회에 성공한다.")
	@Test
	void findCategories() {
		// given
		setupCategory("인기매물");
		setupCategory("부동산");
		setupCategory("중고차");
		setupCategory("디지털기기");
		setupCategory("생활가전");
		setupCategory("가구/인테리어");
		setupCategory("유아동");
		setupCategory("유아도서");
		setupCategory("스포츠/레저");
		setupCategory("여성잡화");
		setupCategory("여성의류");
		setupCategory("남성패션/잡화");

		// when
		List<CategoryListResponse> categories = categoryService.findAll();

		// then
		assertThat(categories.size()).isEqualTo(12);
	}

	Category setupCategory(String name) {
		return supportRepository.save(new Category(name, "categoryImage"));
	}
}

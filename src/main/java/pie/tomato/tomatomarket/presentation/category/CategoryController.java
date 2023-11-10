package pie.tomato.tomatomarket.presentation.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.category.CategoryService;
import pie.tomato.tomatomarket.presentation.category.response.CategoryListResponse;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<CategoryListResponse>> findCategories() {
		return ResponseEntity.ok().body(categoryService.findAll());
	}
}

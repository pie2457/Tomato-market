package pie.tomato.tomatomarket.infrastructure.persistence.item;

import static pie.tomato.tomatomarket.domain.QItem.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.infrastructure.persistence.PaginationRepository;
import pie.tomato.tomatomarket.presentation.request.item.ItemResponse;

public interface ItemRepository extends JpaRepository<Item, Long>, PaginationRepository {

	Optional<Item> findItemByIdAndMemberId(Long itemId, Long memberId);

	boolean existsItemById(Long itemId);

	default BooleanExpression lessThanItemId(Long itemId) {
		if (itemId == null) {
			return null;
		}

		return item.id.lt(itemId);
	}

	default BooleanExpression equalCategoryId(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return item.category.id.eq(categoryId);
	}

	default BooleanExpression equalTradingRegion(String region) {
		if (region == null) {
			return null;
		}

		return item.region.like(region + "%");
	}

	default SliceImpl<ItemResponse> checkLastPage(int size, List<ItemResponse> results) {

		boolean hasNext = false;

		if (results.size() > size) {
			hasNext = true;
			results.remove(size);
		}

		return new SliceImpl<>(results, PageRequest.ofSize(size), hasNext);
	}
}

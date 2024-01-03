package pie.tomato.tomatomarket.infrastructure.persistence.item;

import static pie.tomato.tomatomarket.domain.QItem.*;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Optional<Item> findByIdAndMemberId(Long itemId, Long memberId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select item from Item item where item.id = :itemId")
	Optional<Item> findByIdWithPessimisticLock(@Param("itemId") Long itemId);

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

	default BooleanExpression equalMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return item.member.id.eq(memberId);
	}

	default BooleanExpression findStatus(ItemStatus status) {
		if (status == null) {
			return null;
		} else if (status.equals(ItemStatus.SOLD_OUT)) {
			return item.status.eq(status);
		} else {
			return item.status.ne(ItemStatus.SOLD_OUT);
		}
	}
}

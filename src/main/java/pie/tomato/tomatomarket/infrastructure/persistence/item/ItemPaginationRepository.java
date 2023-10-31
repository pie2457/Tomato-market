package pie.tomato.tomatomarket.infrastructure.persistence.item;

import static pie.tomato.tomatomarket.domain.QItem.*;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.persistence.util.PaginationUtil;
import pie.tomato.tomatomarket.presentation.request.item.ItemResponse;

@RequiredArgsConstructor
@Repository
public class ItemPaginationRepository {

	private final JPAQueryFactory queryFactory;
	private final ItemRepository itemRepository;

	public Slice<ItemResponse> findByIdAndRegion(Long itemId, String region, int size, Long categoryId) {
		List<ItemResponse> itemResponses = queryFactory.select(Projections.fields(ItemResponse.class,
				item.id.as("itemId"),
				item.thumbnail.as("thumbnailUrl"),
				item.title,
				item.region.as("tradingRegion"),
				item.createdAt,
				item.price,
				item.status,
				item.wishCount,
				item.chatCount,
				item.member.nickname.as("isSeller")))
			.from(item)
			.where(itemRepository.lessThanItemId(itemId),
				itemRepository.equalCategoryId(categoryId),
				itemRepository.equalTradingRegion(region)
			)
			.orderBy(item.createdAt.desc())
			.limit(size + 1)
			.fetch();
		return PaginationUtil.checkLastPage(size, itemResponses);
	}
}

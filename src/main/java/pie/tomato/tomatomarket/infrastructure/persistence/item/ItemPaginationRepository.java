package pie.tomato.tomatomarket.infrastructure.persistence.item;

import static pie.tomato.tomatomarket.domain.QItem.*;
import static pie.tomato.tomatomarket.domain.QWish.*;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.persistence.util.PaginationUtil;
import pie.tomato.tomatomarket.presentation.item.response.ItemResponse;
import pie.tomato.tomatomarket.presentation.wish.response.WishListResponse;

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

	public Slice<WishListResponse> findByMemberIdAndCategoryId(Long memberId, Long categoryId,
		int size, Long cursor) {
		List<WishListResponse> wishListResponse = queryFactory.select(Projections.fields(WishListResponse.class,
				item.id.as("cursor"),
				item.thumbnail.as("thumbnailUrl"),
				item.title,
				item.region.as("tradingRegion"),
				item.createdAt,
				item.price,
				item.status,
				item.wishCount,
				item.chatCount,
				item.member.nickname.as("sellerId")))
			.from(wish)
			.innerJoin(wish.item, item)
			.on(item.id.eq(wish.item.id))
			.where(
				itemRepository.lessThanItemId(cursor),
				itemRepository.equalMemberId(memberId),
				itemRepository.equalCategoryId(categoryId))
			.orderBy(item.createdAt.desc())
			.limit(size + 1)
			.fetch();
		return PaginationUtil.checkLastPage(size, wishListResponse);
	}
}

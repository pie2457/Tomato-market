package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.wish.WishItemService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.presentation.wish.response.CategoryWishListResponse;
import pie.tomato.tomatomarket.presentation.wish.response.WishListResponse;
import pie.tomato.tomatomarket.support.SupportRepository;

@SpringBootTest
@Transactional
class WishItemServiceTest {

	@Autowired
	private WishItemService wishItemService;
	@Autowired
	private SupportRepository supportRepository;

	@DisplayName("관심상품 등록에 성공한다.")
	@Test
	void wishRegister() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);

		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		wishItemService.changeWishStatus(item.getId(), "YES", principal);
		wishItemService.changeWishStatus(item.getId(), "YES", principal);
		wishItemService.changeWishStatus(item.getId(), "YES", principal);

		Item findItem = supportRepository.findById(item.getId(), Item.class);

		// then
		assertThat(findItem.getWishCount()).isEqualTo(3L);
	}

	@DisplayName("관심상품 해제에 성공한다.")
	@Test
	void wishCancel() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		wishItemService.changeWishStatus(item.getId(), "YES", principal);
		wishItemService.changeWishStatus(item.getId(), "YES", principal);
		wishItemService.changeWishStatus(item.getId(), "no", principal);

		Item findItem = supportRepository.findById(item.getId(), Item.class);

		// then
		assertThat(findItem.getWishCount()).isEqualTo(1);
	}

	@DisplayName("관심상품 목록 조회에 성공한다.")
	@Test
	void WishList() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		wishItemService.changeWishStatus(item.getId(), "yes", principal);
		wishItemService.changeWishStatus(item.getId(), "yes", principal);
		wishItemService.changeWishStatus(item.getId(), "yes", principal);

		// when
		CustomSlice<WishListResponse> wishList =
			wishItemService.findWishList(principal, category.getId(), 10, null);

		// then
		assertThat(wishList.getContents().size()).isEqualTo(3);
	}

	@DisplayName("관심상품으로 등록된 상품의 카테고리들을 조회한다.")
	@Test
	void categoryOfWishList() {
		// given
		Category category = setupCategory();
		Category category1 = supportRepository.save(new Category("가구", "categoryImage"));
		Category category2 = supportRepository.save(new Category("스포츠", "categoryImage"));
		Category category3 = supportRepository.save(new Category("전자", "categoryImage"));

		Member member = setupMember();
		Principal principal = setPrincipal(member);
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		Item item1 = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category1));
		wishItemService.changeWishStatus(item.getId(), "yes", principal);
		wishItemService.changeWishStatus(item1.getId(), "yes", principal);

		// when
		List<CategoryWishListResponse> categoryWishList = wishItemService.findCategoryWishList(principal);

		// then
		assertAll(
			() -> assertThat(categoryWishList.size()).isEqualTo(2),
			() -> assertThat(categoryWishList).anyMatch(
				categoryWish -> !categoryWish.getCategoryId().equals(category2.getId())),
			() -> assertThat(categoryWishList).anyMatch(
				categoryWish -> !categoryWish.getCategoryId().equals(category3.getId()))
		);
	}

	Member setupMember() {
		return supportRepository.save(new Member("파이", "123@123", "profile"));
	}

	Category setupCategory() {
		return supportRepository.save(new Category("잡화", "categoryImage"));
	}

	Principal setPrincipal(Member member) {
		return Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
	}
}

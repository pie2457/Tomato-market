package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.item.ItemService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.Image;
import pie.tomato.tomatomarket.domain.ImageFile;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.infrastructure.persistence.image.ImageRepository;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.item.request.ItemModifyRequest;
import pie.tomato.tomatomarket.presentation.item.request.ItemRegisterRequest;
import pie.tomato.tomatomarket.presentation.item.request.ItemStatusModifyRequest;
import pie.tomato.tomatomarket.presentation.item.response.ItemDetailResponse;
import pie.tomato.tomatomarket.presentation.item.response.ItemResponse;
import pie.tomato.tomatomarket.presentation.item.response.SalesItemDetailResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class ItemServiceTest {

	@Autowired
	private ItemService itemService;
	@Autowired
	private ImageRepository imageRepository;
	@MockBean
	private FakeImageUploader imageUploader;
	@Autowired
	private SupportRepository supportRepository;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@DisplayName("상품 등록 요청에 성공한다.")
	@Test
	void itemRegister() {
		// given
		given(imageUploader.uploadImageToS3(any(ImageFile.class))).willReturn("url");
		given(imageUploader.uploadImagesToS3(anyList())).willReturn(List.of("url"));

		Category category = setupCategory();

		ItemRegisterRequest request = createItemRegisterRequest(category);
		MockMultipartFile thumbnail = createMockMultipartFile("test-image");
		MockMultipartFile image = createMockMultipartFile("test-image");

		Member member = supportRepository.save(new Member("파이", "123@123", "profile"));
		Principal principal = setPrincipal(member);

		// when
		itemService.register(request, thumbnail, List.of(image), principal);
		Item findItem = supportRepository.findAll(Item.class).get(0);

		// then
		assertThat(findItem).hasFieldOrProperty("id")
			.hasFieldOrProperty("title")
			.hasFieldOrProperty("price")
			.hasFieldOrProperty("content")
			.hasFieldOrProperty("thumbnail")
			.hasFieldOrProperty("status")
			.hasFieldOrProperty("region")
			.hasFieldOrProperty("chatCount")
			.hasFieldOrProperty("viewCount")
			.hasFieldOrProperty("wishCount")
			.hasFieldOrProperty("createdAt");
	}

	@DisplayName("상품 상태 수정에 성공한다.")
	@Test
	void modifyItemStatus() {
		// given
		Member member = setupMember();
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, setupCategory()));

		Principal principal = setPrincipal(member);
		ItemStatusModifyRequest request = new ItemStatusModifyRequest("예약중");

		// when
		itemService.modifyStatus(item.getId(), principal, request);

		// then
		Item findItem = supportRepository.findById(item.getId(), Item.class);
		assertThat(findItem.getStatus()).isEqualTo(ItemStatus.RESERVED);
	}

	@DisplayName("상품 목록 조회에 성공한다.")
	@Test
	void findAllItem() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		for (int i = 0; i < 15; i++) {
			supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
				"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		}

		// when
		CustomSlice<ItemResponse> findItems = itemService.findAll(null, 10, null, null);

		// then
		assertAll(
			() -> assertThat(findItems.getContents().size()).isEqualTo(10),
			// 0번째로 나온 객체의 생성시간이 가장 마지막에 나온 객체의 생성시간보다 이후인지 확인하는 메서드
			() -> assertThat(findItems.getContents().get(0).getCreatedAt())
				.isAfter(findItems.getContents().get(findItems.getContents().size() - 1).getCreatedAt()),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("itemId"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("thumbnailUrl"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("title"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("tradingRegion"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("createdAt"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("price"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("status"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("chatCount"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("wishCount"),
			() -> assertThat(findItems.getContents().get(0)).hasFieldOrProperty("isSeller")
		);

	}

	@DisplayName("카테고리별 상품 목록 조회에 성공한다.")
	@Test
	void categoryByAllItem() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Category category2 = supportRepository.save(new Category("가구", "categotyImage"));
		for (int i = 0; i < 7; i++) {
			supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
				"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		}
		for (int i = 0; i < 4; i++) {
			supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
				"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category2));
		}

		// when
		CustomSlice<ItemResponse> items = itemService.findAll(null, 10, null, category.getId());

		// then
		assertThat(items.getContents().size()).isEqualTo(7);
	}

	@DisplayName("상품 수정에 성공한다. : 기존 썸네일 삭제, 새로운 썸네일 추가, 기존 이미지 삭제, 이미지 추가")
	@Test
	void modifyItemVer_1() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);

		ItemRegisterRequest request = createItemRegisterRequest(category);
		MockMultipartFile thumbnail = createMockMultipartFile("test-image");
		MockMultipartFile image = createMockMultipartFile("test-image");

		itemService.register(request, thumbnail, List.of(image), principal);

		Item originItem = supportRepository.findAll(Item.class).get(0);
		List<Image> images = imageRepository.findAll();
		List<String> imageUrls = images.stream()
			.map(Image::getImageUrl)
			.collect(Collectors.toList());
		String originItemThumbnail = originItem.getThumbnail();
		String originItemImages = imageUrls.get(0);
		ItemModifyRequest modifyRequest = createItemModifyRequest(category, originItem, imageUrls);
		MockMultipartFile updateThumbnail = createMockMultipartFile("changed-test-image");
		MockMultipartFile updateImage = createMockMultipartFile("changed-test-image");

		// when
		itemService.modifyItem(originItem.getId(), principal, modifyRequest, List.of(updateImage), updateThumbnail);

		// then
		Item updateItem = supportRepository.findAll(Item.class).get(0);
		List<Image> updateImages = imageRepository.findAll();
		List<String> updateImageUrls = updateImages.stream()
			.map(Image::getImageUrl)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(updateItem.getThumbnail()).isNotEqualTo(originItemThumbnail),
			() -> assertThat(updateImageUrls.get(0)).isNotEqualTo(originItemImages)
		);
	}

	@DisplayName("상품 수정에 성공한다. : 새로운 이미지만 추가")
	@Test
	void modifyItemVer_2() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);

		ItemRegisterRequest request = createItemRegisterRequest(category);
		MockMultipartFile thumbnail = createMockMultipartFile("test-image");
		MockMultipartFile image = createMockMultipartFile("test-image");

		itemService.register(request, thumbnail, List.of(image), principal);

		Item originItem = supportRepository.findAll(Item.class).get(0);
		List<Image> images = imageRepository.findAll();

		ItemModifyRequest modifyRequest = new ItemModifyRequest("사과", 5000L, "사과 팜", "역삼1동", "판매중",
			category.getId(), null, originItem.getThumbnail());
		MockMultipartFile updateImage = createMockMultipartFile("changed-test-image");

		// when
		itemService.modifyItem(originItem.getId(), principal, modifyRequest, List.of(updateImage), null);

		// then
		List<Image> updateImages = imageRepository.findAll();

		assertAll(
			() -> assertThat(images.size()).isEqualTo(1),
			() -> assertThat(updateImages.size()).isEqualTo(2)
		);
	}

	@DisplayName("상품 삭제에 성공한다.")
	@Test
	void deleteItem() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);

		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		itemService.deleteItem(item.getId(), principal);

		// then
		assertAll(
			() -> assertThat(supportRepository.findById(item.getId(), Item.class)).isNull(),
			() -> assertThat(imageRepository.findById(item.getId())).isEmpty()
		);
	}

	@DisplayName("상품 상세 조회에 성공한다.")
	@Test
	void itemDetails() {
		// given
		Member member = setupMember();
		Category category = setupCategory();
		Principal principal = setPrincipal(member);
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		ItemDetailResponse itemDetailResponse = itemService.itemDetails(principal, item.getId());

		// then
		assertAll(
			() -> assertThat(itemDetailResponse).hasFieldOrProperty("title")
				.hasFieldOrProperty("nickname")
				.hasFieldOrProperty("status")
				.hasFieldOrProperty("createdAt")
				.hasFieldOrProperty("content")
				.hasFieldOrProperty("chatCount")
				.hasFieldOrProperty("wishCount")
				.hasFieldOrProperty("viewCount")
				.hasFieldOrProperty("price")
				.hasFieldOrProperty("thumbnail")
				.hasFieldOrProperty("images"),
			() -> assertThat(itemDetailResponse.isInWishList()).isFalse()
		);
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@DisplayName("한 사람이 상품을 중복 조회해도 뷰카운트는 증가하지 않는다.")
	@Test
	void itemViewCount() {
		// given
		Member member = supportRepository.save(new Member("파이파이", "123@123", "profile"));
		Category category = setupCategory();
		Principal principal = setPrincipal(member);
		Item item = supportRepository.save(new Item("머리끈", "머리끈 100개 팝니다.", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		itemService.itemDetails(principal, item.getId());
		itemService.itemDetails(principal, item.getId());

		String key = "itemId: " + item.getId();
		String s = redisTemplate.opsForValue().get(key);

		// then
		assertThat(s).isEqualTo("1");
		redisTemplate.delete("파이파이1");
	}

	@DisplayName("판매내역 조회에 성공한다. : 전체조회")
	@Test
	void salesItemDetails_V1() {
		// given
		Category category = setupCategory();
		Member member = setupMember();
		Principal principal = setPrincipal(member);

		supportRepository.save(new Item("1번", "내용1", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		supportRepository.save(new Item("2번", "내용2", 3000L, "thumbnail", ItemStatus.RESERVED,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		supportRepository.save(new Item("3번", "내용3", 3000L, "thumbnail", ItemStatus.SOLD_OUT,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		CustomSlice<SalesItemDetailResponse> salesItemDetailResponse =
			itemService.salesItemDetails("all", principal, 10, null);

		// then
		assertThat(salesItemDetailResponse.getContents().size()).isEqualTo(3);
	}

	@DisplayName("판매내역 조회에 성공한다. : 판매중(+예약중)만 조회, 판매완료만 조회")
	@Test
	void salesItemDetails_V2() {
		// given
		Category category = setupCategory();
		Member member = setupMember();
		Principal principal = setPrincipal(member);

		supportRepository.save(new Item("1번", "내용1", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		supportRepository.save(new Item("2번", "내용2", 3000L, "thumbnail", ItemStatus.RESERVED,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
		supportRepository.save(new Item("3번", "내용3", 3000L, "thumbnail", ItemStatus.SOLD_OUT,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));

		// when
		CustomSlice<SalesItemDetailResponse> salesItemDetailResponse1 =
			itemService.salesItemDetails("on_sale", principal, 10, null);
		CustomSlice<SalesItemDetailResponse> salesItemDetailResponse2 = itemService.salesItemDetails(
			"sold_out", principal, 10, null);

		// then
		assertAll(
			() -> assertThat(salesItemDetailResponse1.getContents().size()).isEqualTo(2),
			() -> assertThat(salesItemDetailResponse2.getContents().size()).isEqualTo(1)
		);
	}

	private ItemRegisterRequest createItemRegisterRequest(Category category) {
		return new ItemRegisterRequest("수박",
			5000L,
			"판매중",
			"thumbnail",
			"content",
			"region",
			category.getId());
	}

	private ItemModifyRequest createItemModifyRequest(Category category, Item originItem,
		List<String> imageUrls) {
		return new ItemModifyRequest("사과", 5000L, "사과 팜", "역삼1동", "판매중",
			category.getId(), imageUrls, originItem.getThumbnail());
	}

	private MockMultipartFile createMockMultipartFile(String filename) {
		return new MockMultipartFile(
			filename, "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));
	}

	Principal setPrincipal(Member member) {
		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
		return principal;
	}

	Member setupMember() {
		return supportRepository.save(new Member("파이", "123@123", "profile"));
	}

	Category setupCategory() {
		return supportRepository.save(new Category("잡화", "categoryImage"));
	}
}

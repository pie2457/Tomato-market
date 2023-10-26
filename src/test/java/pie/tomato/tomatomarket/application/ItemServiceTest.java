package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.image.ImageUploader;
import pie.tomato.tomatomarket.application.item.ItemService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.ImageFile;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.request.item.ItemRegisterRequest;
import pie.tomato.tomatomarket.presentation.request.item.ItemResponse;
import pie.tomato.tomatomarket.presentation.request.item.ItemStatusModifyRequest;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class ItemServiceTest {

	@Autowired
	private ItemService itemService;
	@MockBean
	private ImageUploader imageUploader;
	@Autowired
	private SupportRepository supportRepository;

	@DisplayName("상품 등록 요청에 성공한다.")
	@Test
	void itemRegister() {
		// given
		given(imageUploader.uploadImageToS3(any(ImageFile.class))).willReturn("url");
		given(imageUploader.uploadImagesToS3(anyList())).willReturn(List.of("url"));

		Category category = setupCategory();

		ItemRegisterRequest request = new ItemRegisterRequest("수박",
			5000L,
			"판매중",
			"thumbnail",
			"content",
			"region",
			category.getId());
		MockMultipartFile thumbnail = new MockMultipartFile(
			"test-image", "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));
		MockMultipartFile image = new MockMultipartFile(
			"test-image", "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));

		Member member = supportRepository.save(new Member("파이", "123@123", "profile"));
		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();

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

		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
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
		assertThat(findItems.getContents().size()).isEqualTo(10);
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

	Member setupMember() {
		return supportRepository.save(new Member("파이", "123@123", "profile"));
	}

	Category setupCategory() {
		return supportRepository.save(new Category("잡화", "categoryImage"));
	}
}

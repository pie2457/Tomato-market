package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.ImageFile;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.request.ItemRegisterRequest;
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

		Category category = supportRepository.save(new Category("가구/잡화", "image"));

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
}

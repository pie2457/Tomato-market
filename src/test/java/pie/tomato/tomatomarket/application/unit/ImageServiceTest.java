package pie.tomato.tomatomarket.application.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.ImageService;
import pie.tomato.tomatomarket.application.ImageUploader;
import pie.tomato.tomatomarket.domain.ImageFile;

@Transactional
@SpringBootTest
class ImageServiceTest {

	@InjectMocks
	private ImageService imageService;
	@Mock
	private ImageUploader imageUploader;

	@DisplayName("이미지 파일이 주어지면 업로드에 성공한다.")
	@Test
	void imageUpload() throws IOException {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			"test-image", "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));

		given(imageUploader.uploadImageToS3(any(ImageFile.class))).willReturn("url");

		// when & then
		assertThatCode(() -> imageService.uploadImageToS3(mockMultipartFile))
			.doesNotThrowAnyException();
	}
}

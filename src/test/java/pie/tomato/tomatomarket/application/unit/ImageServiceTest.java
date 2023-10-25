package pie.tomato.tomatomarket.application.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.image.ImageService;
import pie.tomato.tomatomarket.application.image.ImageUploader;
import pie.tomato.tomatomarket.domain.ImageFile;

@Transactional
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

	@InjectMocks
	private ImageService imageService;
	@Mock
	private ImageUploader imageUploader;

	@DisplayName("이미지 파일이 주어지면 업로드에 성공한다.")
	@Test
	void imageUpload() {
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

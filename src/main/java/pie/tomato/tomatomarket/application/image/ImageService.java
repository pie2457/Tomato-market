package pie.tomato.tomatomarket.application.image;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.ImageFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

	private final ImageUploader imageUploader;

	public String uploadImageToS3(MultipartFile multipartFile) {
		ImageFile file = ImageFile.from(multipartFile);
		return imageUploader.uploadImageToS3(file);
	}

	public List<String> uploadImagesToS3(List<MultipartFile> multipartFiles) {
		List<ImageFile> imageFiles = ImageFile.from(multipartFiles);
		return imageUploader.uploadImagesToS3(imageFiles);
	}

	public void deleteImageFromS3(String fileName) {
		imageUploader.deleteImage(fileName);
	}

	public void deleteImagesFromS3(List<String> deleteUrls) {
		deleteUrls.forEach(imageUploader::deleteImage);
	}
}

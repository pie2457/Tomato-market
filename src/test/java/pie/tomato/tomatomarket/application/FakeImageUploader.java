package pie.tomato.tomatomarket.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;

import pie.tomato.tomatomarket.application.image.ImageUploader;
import pie.tomato.tomatomarket.domain.ImageFile;
import pie.tomato.tomatomarket.infrastructure.config.properties.S3Properties;

@Profile("test")
@Component
public class FakeImageUploader extends ImageUploader {

	public FakeImageUploader(AmazonS3Client amazonS3Client, S3Properties s3Properties) {
		super(amazonS3Client, s3Properties);
	}

	@Override
	public String uploadImageToS3(ImageFile imageFile) {
		return imageFile.getFileName();
	}

	@Override
	public List<String> uploadImagesToS3(List<ImageFile> imageFile) {
		return imageFile.stream()
			.map(ImageFile::getFileName)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteImage(String fileName) {
	}
}

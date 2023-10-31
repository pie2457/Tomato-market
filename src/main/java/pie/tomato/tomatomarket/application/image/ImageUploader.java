package pie.tomato.tomatomarket.application.image;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import pie.tomato.tomatomarket.domain.ImageFile;
import pie.tomato.tomatomarket.infrastructure.config.properties.S3Properties;

@Component
public class ImageUploader {

	private static final String UPLOADED_IMAGES_DIR = "public/";

	private final AmazonS3Client amazonS3Client;
	private final String bucket;

	public ImageUploader(AmazonS3Client amazonS3Client, S3Properties s3Properties) {
		this.amazonS3Client = amazonS3Client;
		this.bucket = s3Properties.getS3().getBucket();
	}

	public String uploadImageToS3(ImageFile imageFile) {
		final String fileName = putImage(imageFile);
		return getObjectUrl(fileName);
	}

	public List<String> uploadImagesToS3(List<ImageFile> imageFile) {
		List<String> urls = new ArrayList<>();
		for (ImageFile file : imageFile) {
			final String fileName = putImage(file);
			urls.add(getObjectUrl(fileName));
		}
		return urls;
	}

	private String putImage(ImageFile imageFile) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getFileSize());

		final String fileName = UPLOADED_IMAGES_DIR + imageFile.getFileName();
		amazonS3Client.putObject(bucket, fileName, imageFile.getImageInputStream(), metadata);
		return fileName;
	}

	private String getObjectUrl(final String fileName) {
		return URLDecoder.decode(amazonS3Client.getUrl(bucket, fileName).toString(), StandardCharsets.UTF_8);
	}

	public void deleteImage(String fileName) {
		amazonS3Client.deleteObject(bucket, fileName);
	}
}

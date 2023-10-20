package pie.tomato.tomatomarket.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.InternalServerException;

@Getter
@RequiredArgsConstructor
public class ImageFile {

	private final String fileName;
	private final String contentType;
	private final Long fileSize;
	private final InputStream imageInputStream;

	private ImageFile(MultipartFile multipartFile) {
		this.fileName = getFileName(multipartFile);
		this.contentType = getImageContentType(multipartFile);
		this.imageInputStream = getImageInputStream(multipartFile);
		this.fileSize = multipartFile.getSize();
	}

	public static ImageFile from(MultipartFile multipartFile) {
		return new ImageFile(multipartFile);
	}

	public static List<ImageFile> from(List<MultipartFile> multipartFiles) {
		List<ImageFile> imageFiles = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			imageFiles.add(new ImageFile(multipartFile));
		}
		return imageFiles;
	}

	public InputStream getImageInputStream(MultipartFile multipartFile) {
		try {
			return multipartFile.getInputStream();
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.FILE_IO_EXCEPTION);
		}
	}

	private String getImageContentType(MultipartFile multipartFile) {
		return ImageContentType.findEnum(StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()));
	}

	private String getFileName(MultipartFile multipartFile) {
		String ext = extractExt(multipartFile.getOriginalFilename());
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}

	@Getter
	@RequiredArgsConstructor
	enum ImageContentType {

		JPEG("jpeg"),
		JPG("jpg"),
		PNG("png"),
		SVG("svg");

		private final String contentType;

		public static String findEnum(String contentType) {
			for (ImageContentType imageContentType : ImageContentType.values()) {
				if (imageContentType.getContentType().equalsIgnoreCase(contentType)) {
					return imageContentType.getContentType();
				}
			}
			throw new BadRequestException(ErrorCode.INVALID_FILE_EXTENSION);
		}
	}
}

package pie.tomato.tomatomarket.infrastructure.persistence.image;

import java.util.List;

import pie.tomato.tomatomarket.domain.Image;

public interface ImageRepositoryCustom {

	void saveAllImages(List<Image> itemImages);
}

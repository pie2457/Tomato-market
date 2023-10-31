package pie.tomato.tomatomarket.infrastructure.persistence.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pie.tomato.tomatomarket.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {

	@Modifying
	@Query("delete from Image image where image.item.id = :itemId and image.imageUrl in :imageUrls")
	void deleteImageByItemIdAndImageUrls(@Param("itemId") Long itemId,
		@Param("imageUrls") List<String> imageUrls);

	@Modifying
	@Query("delete from Image image where image.item.id = :itemId and image.imageUrl = :imageUrl")
	void deleteImageByItemIdAndImageUrl(@Param("itemId") Long itemId,
		@Param("imageUrl") String imageUrl);

	void deleteByItemId(Long itemId);
}

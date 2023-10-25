package pie.tomato.tomatomarket.infrastructure.persistence.image;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}

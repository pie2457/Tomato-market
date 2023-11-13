package pie.tomato.tomatomarket.infrastructure.persistence.region;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}

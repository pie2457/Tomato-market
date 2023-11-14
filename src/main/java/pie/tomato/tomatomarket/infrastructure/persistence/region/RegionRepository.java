package pie.tomato.tomatomarket.infrastructure.persistence.region;

import static pie.tomato.tomatomarket.domain.QRegion.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

	default BooleanExpression lessThanAddressId(Long addressId) {
		if (addressId == null) {
			return null;
		}

		return region.id.lt(addressId);
	}

	default BooleanExpression equalsAddressName(String addressName) {
		if (addressName == null) {
			return null;
		}

		return region.fullAddressName.contains(addressName);
	}
}

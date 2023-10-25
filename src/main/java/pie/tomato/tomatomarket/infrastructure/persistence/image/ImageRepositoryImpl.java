package pie.tomato.tomatomarket.infrastructure.persistence.image;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Image;

@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void saveAllImages(List<Image> itemImages) {
		String sql = "INSERT INTO image "
			+ "(image_url, item_id) VALUES (:imageUrl, :itemId)";
		MapSqlParameterSource[] params = itemImages.stream()
			.map(itemImage -> new MapSqlParameterSource()
				.addValue("imageUrl", itemImage.getImageUrl())
				.addValue("itemId", itemImage.getItem().getId()))
			.collect(Collectors.toList())
			.toArray(MapSqlParameterSource[]::new);
		jdbcTemplate.batchUpdate(sql, params);
	}
}

package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Image {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String imageUrl;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private Image(String imageUrl, Item item) {
		this.imageUrl = imageUrl;
		this.item = item;
	}

	public static Image of(String imageUrl, Item item) {
		return new Image(imageUrl, item);
	}

	public static List<Image> createImage(List<String> imageUrls, Item item) {
		return imageUrls.stream()
			.map(url -> new Image(url, item))
			.collect(Collectors.toList());
	}
}

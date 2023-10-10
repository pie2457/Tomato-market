package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Image {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String imageUrl;
	private boolean thumbnail;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
}

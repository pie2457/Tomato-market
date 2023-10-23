package pie.tomato.tomatomarket.domain;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Category {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	private String categoryImage;

	public Category(Long id) {
		this.id = id;
	}

	public Category(String name, String categoryImage) {
		this.name = name;
		this.categoryImage = categoryImage;
	}
}

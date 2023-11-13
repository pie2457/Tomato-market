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
public class Region {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String fullAddressName;
	private String addressName;

	public Region(String fullAddressName, String addressName) {
		this.fullAddressName = fullAddressName;
		this.addressName = addressName;
	}
}

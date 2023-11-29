package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Chatroom {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private LocalDateTime createdAt;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "seller_id")
	private Member seller;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "buyer_id")
	private Member buyer;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	public Chatroom(Member seller, Member buyer, Item item) {
		this.createdAt = LocalDateTime.now();
		this.seller = seller;
		this.buyer = buyer;
		this.item = item;
	}

	public String getSenderName(Long memberId) {
		if (memberId == seller.getId()) {
			return seller.getNickname();
		}
		return buyer.getNickname();
	}
}

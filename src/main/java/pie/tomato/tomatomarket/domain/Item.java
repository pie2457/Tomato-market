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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Item {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String title;
	private String content;
	private Long price;
	private String thumbnail;
	private ItemStatus status;
	private String region;
	private Long chatCount;
	private Long wishCount;
	private Long viewCount;
	private LocalDateTime createdAt;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Builder
	public Item(String title, String content, Long price, String thumbnail, ItemStatus status, String region,
		Long chatCount,
		Long wishCount, Long viewCount, LocalDateTime createdAt, Member member, Category category) {
		this.title = title;
		this.content = content;
		this.price = price;
		this.thumbnail = thumbnail;
		this.status = status;
		this.region = region;
		this.chatCount = chatCount;
		this.wishCount = wishCount;
		this.viewCount = viewCount;
		this.createdAt = createdAt;
		this.member = member;
		this.category = category;
	}
}

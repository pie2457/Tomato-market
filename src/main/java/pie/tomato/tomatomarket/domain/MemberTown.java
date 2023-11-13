package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class MemberTown {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "region_id")
	private Region region;

	private MemberTown(String name, Member member, Region region) {
		this.name = name;
		this.member = member;
		this.region = region;
	}

	public static MemberTown of(Region region, Member member) {
		return new MemberTown(region.getAddressName(), member, region);
	}
}

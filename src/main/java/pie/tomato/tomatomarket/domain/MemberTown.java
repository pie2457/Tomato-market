package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
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

	@Column(name = "is_selected", nullable = false)
	private boolean isSelected;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "region_id")
	private Region region;

	private MemberTown(String name, Member member, Region region, boolean isSelected) {
		this.name = name;
		this.member = member;
		this.region = region;
		this.isSelected = isSelected;
	}

	public static MemberTown of(Region region, Member member, boolean isSelected) {
		return new MemberTown(region.getAddressName(), member, region, isSelected);
	}

	public boolean isSameRegionId(Long regionId) {
		return this.region.getId().equals(regionId);
	}

	public void changeIsSelectedTrue() {
		this.isSelected = true;
	}

	public void changeIsSelectedFalse() {
		this.isSelected = false;
	}
}

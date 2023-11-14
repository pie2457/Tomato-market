package pie.tomato.tomatomarket.infrastructure.persistence.memberTown;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pie.tomato.tomatomarket.domain.MemberTown;

public interface MemberTownRepository extends JpaRepository<MemberTown, Long> {

	Optional<List<MemberTown>> findAllByMemberId(Long memberId);

	@Query("delete from MemberTown memberTown where memberTown.member.id = :memberId and memberTown.region.id = :regionId")
	@Modifying
	void deleteByMemberIdAndRegionId(@Param("memberId") Long memberId, @Param("regionId") Long regionId);
}

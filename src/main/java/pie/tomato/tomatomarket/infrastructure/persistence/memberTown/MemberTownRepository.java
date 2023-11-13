package pie.tomato.tomatomarket.infrastructure.persistence.memberTown;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.MemberTown;

public interface MemberTownRepository extends JpaRepository<MemberTown, Long> {

	List<MemberTown> findAllByMemberId(Long memberId);
}

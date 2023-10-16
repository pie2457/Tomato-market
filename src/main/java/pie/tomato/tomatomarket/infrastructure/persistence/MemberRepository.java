package pie.tomato.tomatomarket.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsMemberByEmail(String email);
}

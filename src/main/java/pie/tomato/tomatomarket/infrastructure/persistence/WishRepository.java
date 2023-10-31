package pie.tomato.tomatomarket.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {

	void deleteByItemIdAndMemberId(Long itemId, Long memberId);
}

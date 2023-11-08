package pie.tomato.tomatomarket.infrastructure.persistence.wish;

import static pie.tomato.tomatomarket.domain.QWish.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {

	void deleteByItemIdAndMemberId(Long itemId, Long memberId);

	boolean existsByItemIdAndMemberId(Long itemId, Long memberId);

	default BooleanExpression equalMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return wish.member.id.eq(memberId);
	}
}

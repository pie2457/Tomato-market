package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QItem.*;
import static pie.tomato.tomatomarket.domain.QMember.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from Chatroom chatroom where chatroom.item.id = :itemId")
	void deleteAllByItemId(@Param("itemId") Long itemId);

	default BooleanExpression lessThanItemId(Long itemId) {
		if (itemId == null) {
			return null;
		}

		return item.id.lt(itemId);
	}

	default BooleanExpression equalMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return member.id.eq(memberId);
	}
}

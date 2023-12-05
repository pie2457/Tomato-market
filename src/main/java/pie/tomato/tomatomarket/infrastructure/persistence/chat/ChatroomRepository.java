package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatroom.*;

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

	@Query("SELECT COUNT(1) FROM ChatLog chatLog WHERE chatLog.chatroom.id = :chatroomId AND chatLog.newMessage = 1")
	Long getNewMessageCount(@Param("chatroomId") Long chatroomId);

	default BooleanExpression lessThanChatroomId(Long chatroomId) {
		if (chatroomId == null) {
			return null;
		}

		return chatroom.id.lt(chatroomId);
	}

	default BooleanExpression equalMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return chatroom.buyer.id.eq(memberId).or(chatroom.seller.id.eq(memberId));
	}
}

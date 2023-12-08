package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatLog.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.ChatLog;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

	@Modifying
	@Query("update ChatLog chatLog set chatLog.newMessage = 0 "
		+ "where chatLog.chatroom.id = :chatroomId and chatLog.id >= :messageIndex")
	void changeToReadState(@Param("chatroomId") Long chatroomId, @Param("messageIndex") Long messageIndex);

	ChatLog findTopByChatroomIdOrderByChatroomIdDesc(Long chatroomId);

	default BooleanExpression lessThanMessageIndex(Long messageIndex) {
		if (messageIndex == null) {
			return null;
		}

		return chatLog.id.lt(messageIndex);
	}

	default BooleanExpression greaterThanMessageIndex(Long messageIndex) {
		if (messageIndex == null) {
			return null;
		}

		return chatLog.id.gt(messageIndex);
	}

	default BooleanExpression equalChatroomId(Long chatroomId) {
		if (chatroomId == null) {
			return null;
		}
		return chatLog.chatroom.id.eq(chatroomId);
	}
}

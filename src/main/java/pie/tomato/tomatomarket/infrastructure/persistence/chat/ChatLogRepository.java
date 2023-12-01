package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatLog.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.core.types.dsl.BooleanExpression;

import pie.tomato.tomatomarket.domain.ChatLog;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

	default BooleanExpression lessThanMessageIndex(Long messageIndex) {
		if (messageIndex == null) {
			return null;
		}

		return chatLog.id.lt(messageIndex);
	}

	default BooleanExpression equalChatroomId(Long chatroomId) {
		if (chatroomId == null) {
			return null;
		}
		return chatLog.chatroom.id.eq(chatroomId);
	}
}

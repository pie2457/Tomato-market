package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatLog.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

	private final JPAQueryFactory queryFactory;
	private final ChatLogRepository chatLogRepository;

	public List<String> getMessage(Long messageIndex, Long chatroomId) {
		return queryFactory.select(chatLog.message)
			.from(chatLog)
			.where(
				chatLogRepository.lessThanMessageIndex(messageIndex),
				chatLogRepository.equalChatroomId(chatroomId)
			)
			.orderBy(chatLog.createdAt.asc())
			.fetch();
	}
}

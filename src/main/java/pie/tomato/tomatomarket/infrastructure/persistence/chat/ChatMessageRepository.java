package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatLog.*;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.persistence.util.PaginationUtil;
import pie.tomato.tomatomarket.presentation.chat.response.ChatMessageResponse;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

	private final JPAQueryFactory queryFactory;
	private final ChatLogRepository chatLogRepository;

	public List<String> getMessagesByChatroomId(Long messageIndex, Long chatroomId) {
		return queryFactory.select(chatLog.message)
			.from(chatLog)
			.where(
				chatLogRepository.lessThanMessageIndex(messageIndex),
				chatLogRepository.equalChatroomId(chatroomId)
			)
			.orderBy(chatLog.createdAt.asc())
			.fetch();
	}

	public Slice<ChatMessageResponse.Chat> readAll(String nickname, Long chatroomId, int size, Long messageIndex) {
		List<ChatMessageResponse.Chat> chatResponse = queryFactory.select(
				Projections.fields(ChatMessageResponse.Chat.class,
					chatLog.id.as("messageId"),
					chatLog.sender.eq(nickname).as("isMe"),
					chatLog.message
				))
			.from(chatLog)
			.where(
				chatLogRepository.greaterThanMessageIndex(messageIndex),
				chatLogRepository.equalChatroomId(chatroomId)
			)
			.orderBy(chatLog.createdAt.asc())
			.limit(size + 1)
			.fetch();
		return PaginationUtil.checkLastPage(size, chatResponse);
	}
}

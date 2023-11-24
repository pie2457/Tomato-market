package pie.tomato.tomatomarket.infrastructure.persistence.chatroom;

import static pie.tomato.tomatomarket.domain.QChatLog.*;
import static pie.tomato.tomatomarket.domain.QChatroom.*;
import static pie.tomato.tomatomarket.domain.QItem.*;
import static pie.tomato.tomatomarket.domain.QMember.*;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.persistence.util.PaginationUtil;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomListResponse;

@Repository
@RequiredArgsConstructor
public class ChatPaginationRepository {

	private final JPAQueryFactory queryFactory;
	private final ChatroomRepository chatroomRepository;

	public Slice<ChatroomListResponse> findAll(Long memberId, int size, Long itemId) {
		List<ChatroomListResponse> responses = queryFactory.select(Projections.fields(ChatroomListResponse.class,
				chatLog.id.as("chatroomId"),
				item.thumbnail.as("thumbnailUrl"),
				member.nickname.as("chatPartnerName"),
				member.profile.as("chatPartnerProfile"),
				chatLog.createdAt.as("lastSendTime"),
				chatLog.message.as("lastSendMessage"),
				chatLog.newMessage.as("newMessageCount")))
			.from(chatLog)
			.innerJoin(chatLog.chatroom, chatroom)
			.innerJoin(chatroom.item, item)
			.innerJoin(item.member, member)
			.where(
				chatroomRepository.lessThanItemId(itemId),
				chatroomRepository.equalMemberId(memberId)
			)
			.orderBy(chatLog.createdAt.desc())
			.limit(size + 1)
			.fetch();
		return PaginationUtil.checkLastPage(size, responses);
	}
}

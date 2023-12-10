package pie.tomato.tomatomarket.infrastructure.persistence.chat;

import static pie.tomato.tomatomarket.domain.QChatroom.*;
import static pie.tomato.tomatomarket.domain.QItem.*;

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

	public Slice<ChatroomListResponse> findAll(Long memberId, int size, Long chatroomId) {
		List<ChatroomListResponse> responses = queryFactory.select(Projections.fields(ChatroomListResponse.class,
				chatroom.id.as("chatroomId"),
				item.thumbnail.as("thumbnailUrl")))
			.from(chatroom)
			.innerJoin(chatroom.item, item)
			.where(
				chatroomRepository.lessThanChatroomId(chatroomId),
				chatroomRepository.equalMemberId(memberId)
			)
			.limit(size + 1)
			.fetch();
		return PaginationUtil.checkLastPage(size, responses);
	}
}

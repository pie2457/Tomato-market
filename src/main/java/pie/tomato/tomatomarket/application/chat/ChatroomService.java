package pie.tomato.tomatomarket.application.chat;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Chatroom;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.chat.ChatPaginationRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.chat.ChatroomRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.item.ItemRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.member.MemberRepository;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomIdResponse;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomListResponse;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

	private final ChatPaginationRepository chatPaginationRepository;
	private final ChatroomRepository chatroomRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	public CustomSlice<ChatroomListResponse> findAll(Principal principal, int size, Long cursor) {
		Slice<ChatroomListResponse> responses =
			chatPaginationRepository.findAll(principal.getMemberId(), size, cursor);

		List<ChatroomListResponse> content = responses.getContent();

		for (ChatroomListResponse chatroomListResponse : content) {
			Long messageCount = chatroomRepository.getNewMessageCount(chatroomListResponse.getChatroomId());
			chatroomListResponse.assignNewMessageCount(messageCount);
		}

		Long nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getChatroomId();

		return new CustomSlice<>(content, nextCursor, responses.hasNext());
	}

	@Transactional
	public ChatroomIdResponse register(Principal principal, Long itemId) {
		Member buyer = getMember(principal.getMemberId());
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ITEM));
		Member seller = getMember(item.getMember().getId());

		Chatroom savedChatroom = chatroomRepository.save(new Chatroom(seller, buyer, item));

		return new ChatroomIdResponse(savedChatroom.getId());
	}

	private Member getMember(Long principal) {
		return memberRepository.findById(principal)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
	}
}

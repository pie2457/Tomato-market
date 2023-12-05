package pie.tomato.tomatomarket.application.chat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.ChatLog;
import pie.tomato.tomatomarket.domain.Chatroom;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.chat.ChatLogRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.chat.ChatMessageRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.chat.ChatroomRepository;
import pie.tomato.tomatomarket.presentation.chat.request.PostMessageRequest;
import pie.tomato.tomatomarket.presentation.chat.response.ChatMessageResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatLogService {

	private final ChatLogRepository chatLogRepository;
	private final ChatroomRepository chatroomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final Map<DeferredResult<List<String>>, Long> chat = new ConcurrentHashMap<>();

	@Transactional
	public void postMessage(Principal principal, Long chatroomId, PostMessageRequest request) {
		Chatroom chatroom = chatroomRepository.findById(chatroomId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM));
		String receiver = chatroom.getReceiverName(principal.getMemberId());

		chatLogRepository.save(ChatLog.of(request, principal, receiver, chatroom));

		for (Entry<DeferredResult<List<String>>, Long> entry : chat.entrySet()) {
			List<String> messages = chatMessageRepository.getMessagesByChatroomId(entry.getValue(), chatroomId);
			entry.getKey().setResult(messages);
		}
	}

	@Transactional
	public ChatMessageResponse getMessages(Principal principal, Long chatroomId,
		int size, Long messageIndex) {
		final DeferredResult<List<String>> deferredResult =
			new DeferredResult<>(null, Collections.emptyList());
		chat.put(deferredResult, messageIndex);

		deferredResult.onCompletion(() -> chat.remove(deferredResult));

		Slice<ChatMessageResponse.Chat> chatMessages =
			chatMessageRepository.readAll(principal.getNickname(), chatroomId, size, messageIndex);

		List<ChatMessageResponse.Chat> responses = chatMessages.getContent();

		Long nextCursor = responses.isEmpty() ? null : responses.get(responses.size() - 1).getMessageId();

		Chatroom chatroom = chatroomRepository.findById(chatroomId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM));

		chatLogRepository.changeToReadState(chatroomId, messageIndex);

		return new ChatMessageResponse(
			getPartnerName(principal.getMemberId(), chatroom),
			ChatMessageResponse.ChatItem.of(chatroom.getItem()),
			responses, nextCursor);
	}

	private String getPartnerName(Long memberId, Chatroom chatroom) {
		if (chatroom.getSeller().getId() == memberId) {
			return chatroom.getBuyer().getNickname();
		}
		return chatroom.getSeller().getNickname();
	}
}

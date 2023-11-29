package pie.tomato.tomatomarket.application.chat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatLogService {

	private final ChatLogRepository chatLogRepository;
	private final ChatroomRepository chatroomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final Map<DeferredResult<List<String>>, Integer> chat = new ConcurrentHashMap<>();

	@Transactional
	public void postMessage(Principal principal, Long chatroomId, PostMessageRequest request) {
		Chatroom chatroom = chatroomRepository.findById(chatroomId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM));
		String receiver = chatroom.isSender(principal.getMemberId());

		chatLogRepository.save(ChatLog.of(request, principal, receiver, chatroom));

		for (Entry<DeferredResult<List<String>>, Integer> entry : chat.entrySet()) {
			List<String> messages = chatMessageRepository.getMessage(Long.valueOf(entry.getValue()), chatroomId);
			entry.getKey().setResult(messages);
		}
	}
}

package pie.tomato.tomatomarket.presentation.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.chat.ChatLogService;
import pie.tomato.tomatomarket.presentation.chat.request.PostMessageRequest;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatLogController {

	private final ChatLogService chatLogService;

	@PostMapping("/{chatroomId}")
	public ResponseEntity<Void> postMessage(@PathVariable Long chatroomId,
		@RequestBody PostMessageRequest message, @AuthPrincipal Principal principal) {
		chatLogService.postMessage(principal, chatroomId, message);
		return ResponseEntity.ok().build();
	}
}

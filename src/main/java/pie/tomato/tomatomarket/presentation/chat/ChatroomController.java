package pie.tomato.tomatomarket.presentation.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.chat.ChatroomService;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomIdResponse;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomListResponse;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatroomController {

	private final ChatroomService chatroomService;

	@GetMapping
	public ResponseEntity<CustomSlice<ChatroomListResponse>> findAll(@AuthPrincipal Principal principal,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor) {
		return ResponseEntity.ok(chatroomService.findAll(principal, size, cursor));
	}

	@PostMapping("/items/{itemId}")
	public ResponseEntity<ChatroomIdResponse> register(@AuthPrincipal Principal principal,
		@PathVariable Long itemId) {
		return ResponseEntity.ok(chatroomService.register(principal, itemId));
	}

	@GetMapping("/items/{itemId}")
	public ResponseEntity<CustomSlice<ChatroomListResponse>> findAllByItemId(@AuthPrincipal Principal principal,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor,
		@PathVariable Long itemId) {
		return ResponseEntity.ok(chatroomService.findAllByItemId(principal, size, cursor, itemId));
	}
}

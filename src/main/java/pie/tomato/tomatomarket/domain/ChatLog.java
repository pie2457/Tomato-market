package pie.tomato.tomatomarket.domain;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pie.tomato.tomatomarket.presentation.chat.request.PostMessageRequest;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatLog {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String message;
	private String sender;
	private String receiver;
	private LocalDateTime createdAt;
	private Long newMessage;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	private ChatLog(String message, String sender, String receiver, LocalDateTime createdAt, Long newMessage,
		Chatroom chatroom) {
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		this.createdAt = createdAt;
		this.newMessage = newMessage;
		this.chatroom = chatroom;
	}

	public static ChatLog of(PostMessageRequest request, Principal principal, String receiver, Chatroom chatroom) {
		return new ChatLog(
			request.getMessage(), principal.getNickname(), receiver, LocalDateTime.now(), 1L, chatroom);
	}
}

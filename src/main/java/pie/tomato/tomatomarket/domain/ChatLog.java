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

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatLog {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String message;
	private String seller;
	private String buyer;
	private LocalDateTime createdAt;
	private Long newMessage;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	public ChatLog(String message, String seller, String buyer, LocalDateTime createdAt, Long newMessage,
		Chatroom chatroom) {
		this.message = message;
		this.seller = seller;
		this.buyer = buyer;
		this.createdAt = createdAt;
		this.newMessage = newMessage;
		this.chatroom = chatroom;
	}
}

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
	private String to;
	private String from;
	private LocalDateTime createdAt;
	private Long newMessage;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

}

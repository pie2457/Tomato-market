package pie.tomato.tomatomarket.presentation.chat.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ChatroomListResponse {

	private Long chatroomId;
	private String thumbnailUrl;
	private String chatPartnerName;
	private String chatPartnerProfile;
	private LocalDateTime lastSendTime;
	private String lastSendMessage;
	private Long newMessageCount;

	public void assignNewMessageCount(Long newMessageCount) {
		this.newMessageCount = newMessageCount;
	}
}

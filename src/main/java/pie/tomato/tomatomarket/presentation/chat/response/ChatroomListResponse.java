package pie.tomato.tomatomarket.presentation.chat.response;

import java.time.LocalDateTime;

import lombok.Getter;
import pie.tomato.tomatomarket.domain.ChatLog;
import pie.tomato.tomatomarket.domain.Chatroom;
import pie.tomato.tomatomarket.presentation.support.Principal;

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

	public void assignLastMessageInfo(ChatLog chatLog) {
		this.lastSendTime = chatLog.getCreatedAt();
		this.lastSendMessage = chatLog.getMessage();
	}

	public void assignPartnerInfo(Chatroom chatroom, Principal principal) {
		if (chatroom.isSeller(principal.getMemberId())) {
			this.chatPartnerName = chatroom.getBuyer().getNickname();
			this.chatPartnerProfile = chatroom.getBuyer().getProfile();
		} else {
			this.chatPartnerName = chatroom.getSeller().getNickname();
			this.chatPartnerProfile = chatroom.getSeller().getProfile();
		}
	}
}

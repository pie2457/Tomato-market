package pie.tomato.tomatomarket.presentation.chat.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pie.tomato.tomatomarket.domain.Item;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

	private String chatPartnerName;
	private ChatItem item;
	private List<Chat> chat;
	private Long nextCursor;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Chat {

		private Long messageId;
		private boolean isMe;
		private String message;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChatItem {

		private String title;
		private String thumbnailUrl;
		private Long price;

		public static ChatItem of(Item item) {
			return new ChatItem(item.getTitle(), item.getThumbnail(), item.getPrice());
		}
	}
}



package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.chat.ChatLogService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.ChatLog;
import pie.tomato.tomatomarket.domain.Chatroom;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.chat.request.PostMessageRequest;
import pie.tomato.tomatomarket.presentation.chat.response.ChatMessageResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class ChatLogServiceTest {

	@Autowired
	private SupportRepository supportRepository;
	@Autowired
	private ChatLogService chatLogService;

	@DisplayName("채팅방 메세지 전송에 성공한다.")
	@Test
	void postMessage() {
		// given
		Member seller = setupMember("파이", "123@123", "profile");
		Member buyer = setupMember("브루니", "2121@1211", "profile2");
		Principal principalBuyer = setPrincipal(buyer);
		Category category = setupCategory();
		Item item = setupItem(seller, category);
		Chatroom chatroom = new Chatroom(seller, buyer, item);
		supportRepository.save(chatroom);
		ChatLog chatLog = ChatLog.of(new PostMessageRequest("안녕하세요"), principalBuyer, "파이", chatroom);
		supportRepository.save(chatLog);

		// when
		chatLogService.postMessage(principalBuyer, chatroom.getId(), new PostMessageRequest("안녕하세요?"));
		List<ChatLog> logs = supportRepository.findAll(ChatLog.class);

		// then
		assertAll(
			() -> assertThat(logs.size()).isEqualTo(2),
			() -> assertThat(logs.get(0).getMessage()).isEqualTo("안녕하세요"),
			() -> assertThat(logs.get(1).getMessage()).isEqualTo("안녕하세요?")
		);
	}

	@DisplayName("채팅방의 메세지를 가져오는데 성공한다.")
	@Test
	void getMessages() {
		// given
		Member seller = setupMember("파이", "123@123", "profile");
		Member buyer = setupMember("브루니", "2121@1211", "profile2");
		Principal principalSeller = setPrincipal(seller);
		Principal principalBuyer = setPrincipal(buyer);
		Category category = setupCategory();
		Item item = setupItem(seller, category);
		Chatroom chatroom = new Chatroom(seller, buyer, item);
		supportRepository.save(chatroom);
		ChatLog chat1 = ChatLog.of(new PostMessageRequest("안녕하세요"), principalBuyer, "파이", chatroom);
		ChatLog chat2 = ChatLog.of(new PostMessageRequest("안녕하세요1"), principalBuyer, "파이", chatroom);
		ChatLog chat3 = ChatLog.of(new PostMessageRequest("안녕하세요2"), principalBuyer, "파이", chatroom);
		ChatLog chat4 = ChatLog.of(new PostMessageRequest("안녕하세요3"), principalBuyer, "파이", chatroom);
		setupChatLog(chat1);
		setupChatLog(chat2);
		setupChatLog(chat3);
		setupChatLog(chat4);

		// when
		ChatMessageResponse messages = chatLogService.getMessages(principalSeller, chatroom.getId(), 20, 0L);

		// then
		assertAll(
			() -> assertThat(messages.getChatPartnerName()).isEqualTo(buyer.getNickname()),
			() -> assertThat(messages.getChat().size()).isEqualTo(4),
			() -> assertThat(messages.getItem().getTitle()).isEqualTo(item.getTitle())
		);
	}

	Principal setPrincipal(Member member) {
		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
		return principal;
	}

	Member setupMember(String nickname, String email, String profile) {
		return supportRepository.save(new Member(nickname, email, profile));
	}

	Category setupCategory() {
		return supportRepository.save(new Category("잡화", "categoryImage"));
	}

	Item setupItem(Member member, Category category) {
		return supportRepository.save(new Item("1번", "내용1", 3000L, "thumbnail", ItemStatus.ON_SALE,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
	}

	void setupChatLog(ChatLog chatLog) {
		supportRepository.save(chatLog);
	}
}

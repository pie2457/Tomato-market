package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pie.tomato.tomatomarket.application.chat.ChatroomService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.ChatLog;
import pie.tomato.tomatomarket.domain.Chatroom;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.chat.request.PostMessageRequest;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomIdResponse;
import pie.tomato.tomatomarket.presentation.chat.response.ChatroomListResponse;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@Transactional
@SpringBootTest
class ChatroomServiceTest {

	@Autowired
	private SupportRepository supportRepository;
	@Autowired
	private ChatroomService chatroomService;

	@DisplayName("채팅방 목록을 불러오는데 성공한다.")
	@Test
	void findAllChatroom() {
		// given
		Member seller = setupMember("파이", "123@123", "profile");
		Member buyer = setupMember("브루니", "2121@1211", "profile2");
		Principal principalSeller = setPrincipal(seller);
		Principal principalBuyer = setPrincipal(buyer);
		Category category = setupCategory();
		Item item = setupItem(seller, category);
		Chatroom chatroom = new Chatroom(seller, buyer, item);
		supportRepository.save(chatroom);

		supportRepository.save(ChatLog.of(new PostMessageRequest("얼마에요"), principalBuyer, "파이", chatroom));

		// when
		CustomSlice<ChatroomListResponse> response = chatroomService.findAll(principalSeller, 10, null);

		// then
		assertThat(response.getContents().get(0)).hasFieldOrProperty("chatroomId")
			.hasFieldOrProperty("thumbnailUrl")
			.hasFieldOrProperty("chatPartnerName")
			.hasFieldOrProperty("chatPartnerProfile")
			.hasFieldOrProperty("lastSendTime")
			.hasFieldOrProperty("lastSendMessage")
			.hasFieldOrProperty("newMessageCount");
	}

	@DisplayName("채팅방 생성에 성공한다.")
	@Test
	void registerChatroom() {
		// given
		Member seller = setupMember("파이", "123@123", "profile");
		Member buyer = setupMember("브루니", "2121@1211", "profile2");
		Principal principal = setPrincipal(buyer);
		Category category = setupCategory();
		Item item = setupItem(seller, category);

		// when
		ChatroomIdResponse response = chatroomService.register(principal, item.getId());
		Chatroom chatroom = supportRepository.findById(response.getChatroomId(), Chatroom.class);

		// then
		assertAll(
			() -> assertThat(chatroom.getId()).isNotNull(),
			() -> assertThat(chatroom).hasFieldOrProperty("buyer")
				.hasFieldOrProperty("seller")
				.hasFieldOrProperty("item")
				.hasFieldOrProperty("createdAt")
		);
	}

	@DisplayName("안읽은 채팅 메세지 개수를 확인한다.")
	@Test
	void newMessageCount() {
		// given
		Member seller = setupMember("파이", "123@123", "profile");
		Member buyer = setupMember("브루니", "2121@1211", "profile2");
		Principal principalSeller = setPrincipal(seller);
		Principal principalBuyer = setPrincipal(buyer);
		Category category = setupCategory();
		Item item = setupItem(seller, category);
		Chatroom chatroom = new Chatroom(seller, buyer, item);
		supportRepository.save(chatroom);

		supportRepository.save(ChatLog.of(new PostMessageRequest("안녕하세요"), principalBuyer, "파이", chatroom));
		supportRepository.save(ChatLog.of(new PostMessageRequest("물건"), principalBuyer, "파이", chatroom));
		supportRepository.save(ChatLog.of(new PostMessageRequest("팔렸나요"), principalBuyer, "파이", chatroom));
		supportRepository.save(ChatLog.of(new PostMessageRequest("얼마에요"), principalBuyer, "파이", chatroom));

		// when
		CustomSlice<ChatroomListResponse> all = chatroomService.findAll(principalSeller, 10, null);

		// then
		assertThat(all.getContents().get(0).getNewMessageCount()).isEqualTo(4);
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
}

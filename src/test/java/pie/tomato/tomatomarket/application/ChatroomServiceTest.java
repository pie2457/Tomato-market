package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;

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
		Member member = setupMember("파이", "123@123", "profile");
		Principal principal = setPrincipal(member);
		Category category = setupCategory();
		Item item = setupItem(member, category);
		Chatroom chatroom = new Chatroom(LocalDateTime.now().minusSeconds(5), member, item);
		supportRepository.save(chatroom);
		supportRepository.save(new ChatLog("얼마에요", "브루니", "파이", LocalDateTime.now(), 0L, chatroom));

		// when
		CustomSlice<ChatroomListResponse> response = chatroomService.findAll(principal, 10, null);

		// then
		assertThat(response.getContents().get(0)).hasFieldOrProperty("chatroomId")
			.hasFieldOrProperty("thumbnailUrl")
			.hasFieldOrProperty("chatPartnerName")
			.hasFieldOrProperty("chatPartnerProfile")
			.hasFieldOrProperty("lastSendTime")
			.hasFieldOrProperty("lastSendMessage")
			.hasFieldOrProperty("newMessageCount");
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
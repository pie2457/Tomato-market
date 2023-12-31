package pie.tomato.tomatomarket.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pie.tomato.tomatomarket.application.item.ItemService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.presentation.support.Principal;
import pie.tomato.tomatomarket.support.SupportRepository;

@SpringBootTest
public class ItemConcurrencyTest {

	@Autowired
	private ItemService itemService;

	@Autowired
	private SupportRepository supportRepository;

	@DisplayName("상품의 상세 페이지를 여러명이 동시에 조회한다.")
	@Test
	void itemDetails_concurrency() throws InterruptedException {
		// given
		List<Member> members = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Member member = supportRepository.save(new Member("파이" + i, "123@123" + i, "profile"));
			members.add(member);
		}
		Category category = setupCategory();

		Item item1 = setItem(members.get(0), category, "1번", "내용1", ItemStatus.ON_SALE);

		int threadCount = 8;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(100);

		// when
		for (int i = 0; i < 100; i++) {
			Member member = members.get(i);
			executorService.execute(() -> {
				try {
					itemService.itemDetails(setPrincipal(member), item1.getId());
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		// then
		Thread.sleep(1000 * 90);

		Item findItem = supportRepository.findById(item1.getId(), Item.class);
		assertThat(findItem.getViewCount()).isEqualTo(100);
	}

	private Principal setPrincipal(Member member) {
		Principal principal = Principal.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.memberId(member.getId())
			.build();
		return principal;
	}

	private Category setupCategory() {
		return supportRepository.save(new Category("잡화", "categoryImage"));
	}

	private Item setItem(Member member, Category category, String title, String content, ItemStatus status) {
		return supportRepository.save(new Item(title, content, 3000L, "thumbnail", status,
			"역삼1동", 0L, 0L, 0L, LocalDateTime.now(), member, category));
	}
}

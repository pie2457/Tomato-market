package pie.tomato.tomatomarket.application.wish;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.Wish;
import pie.tomato.tomatomarket.domain.WishStatus;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.WishRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.item.ItemRepository;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemService {

	private final ItemRepository itemRepository;
	private final WishRepository wishRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void changeWishStatus(Long itemId, String wish, Principal principal) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ITEM));

		if (WishStatus.of(wish) == WishStatus.YES) {
			register(item, principal);
		} else {
			cancel(item);
		}
	}

	private void register(Item item, Principal principal) {
		Member member = memberRepository.findById(principal.getMemberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));

		item.wishRegister();
		wishRepository.save(new Wish(member, item));
	}

	private void cancel(Item item) {
		item.wishCancel();
		wishRepository.deleteByItemId(item.getId());
	}
}

package pie.tomato.tomatomarket.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Image;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.ImageRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.ItemRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;
import pie.tomato.tomatomarket.presentation.request.ItemRegisterRequest;
import pie.tomato.tomatomarket.presentation.request.ItemStatusModifyRequest;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void register(ItemRegisterRequest itemRegisterRequest, MultipartFile thumbnail,
		List<MultipartFile> itemImages, Principal principal) {
		ItemStatus itemStatus = ItemStatus.from(itemRegisterRequest.getStatus());
		String thumbnailUrl = imageService.uploadImageToS3(thumbnail);

		Member member = new Member(principal.getMemberId());
		Item item = itemRepository.save(itemRegisterRequest.toEntity(member, thumbnailUrl, itemStatus));

		if (itemImages != null) {
			List<String> images = imageService.uploadImagesToS3(itemImages);
			imageRepository.saveAll(images.stream()
				.map(url -> Image.of(url, item))
				.collect(Collectors.toList()));
		}
	}

	@Transactional
	public void modifyStatus(Long itemId, Principal principal, ItemStatusModifyRequest request) {
		verifyExistsMember(principal.getMemberId());
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ITEM_NOT_FOUND));

		item.changeStatus(ItemStatus.from(request.getStatus()));
	}

	private void verifyExistsMember(Long memberId) {
		if (!memberRepository.existsMemberById(memberId)) {
			throw new NotFoundException(ErrorCode.NOT_FOUND_MEMBER);
		}
	}
}

package pie.tomato.tomatomarket.application.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.image.ImageService;
import pie.tomato.tomatomarket.domain.Category;
import pie.tomato.tomatomarket.domain.Image;
import pie.tomato.tomatomarket.domain.Item;
import pie.tomato.tomatomarket.domain.ItemStatus;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.ForbiddenException;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.CategoryRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.ChatroomRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.MemberRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.WishRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.image.ImageRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.item.ItemPaginationRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.item.ItemRepository;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.request.item.ItemModifyRequest;
import pie.tomato.tomatomarket.presentation.request.item.ItemRegisterRequest;
import pie.tomato.tomatomarket.presentation.request.item.ItemResponse;
import pie.tomato.tomatomarket.presentation.request.item.ItemStatusModifyRequest;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final MemberRepository memberRepository;
	private final ItemPaginationRepository itemPaginationRepository;
	private final CategoryRepository categoryRepository;
	private final WishRepository wishRepository;
	private final ChatroomRepository chatroomRepository;

	@Transactional
	public void register(ItemRegisterRequest itemRegisterRequest, MultipartFile thumbnail,
		List<MultipartFile> itemImages, Principal principal) {
		ItemStatus itemStatus = ItemStatus.from(itemRegisterRequest.getStatus());
		String thumbnailUrl = imageService.uploadImageToS3(thumbnail);

		Member member = memberRepository.getReferenceById(principal.getMemberId());
		Item item = itemRepository.save(itemRegisterRequest.toEntity(member, thumbnailUrl, itemStatus));

		if (itemImages != null) {
			List<String> images = imageService.uploadImagesToS3(itemImages);
			List<Image> imageList = images.stream()
				.map(url -> Image.toEntity(url, item))
				.collect(Collectors.toList());
			imageRepository.saveAllImages(imageList);
		}
	}

	@Transactional
	public void modifyStatus(Long itemId, Principal principal, ItemStatusModifyRequest request) {
		verifyExistsMember(principal.getMemberId());
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND_ITEM));

		item.changeStatus(ItemStatus.from(request.getStatus()));
	}

	public CustomSlice<ItemResponse> findAll(String region, int size, Long itemId, Long categoryId) {
		Slice<ItemResponse> response = itemPaginationRepository.findByIdAndRegion(itemId, region, size, categoryId);
		List<ItemResponse> content = response.getContent();

		Long nextCursor = setNextCursor(content);

		return new CustomSlice<>(content, nextCursor, response.hasNext());
	}

	private Long setNextCursor(List<ItemResponse> content) {
		Long nextCursor = null;
		if (!content.isEmpty()) {
			nextCursor = content.get(content.size() - 1).getItemId();
		}
		return nextCursor;
	}

	@Transactional
	public void modifyItem(Long itemId, Principal principal, ItemModifyRequest modifyRequest,
		List<MultipartFile> itemImages, MultipartFile thumbnail) {
		verifyExistsMember(principal.getMemberId());
		verifyExistsItem(itemId);

		Item findItem = itemRepository.findItemByIdAndMemberId(itemId, principal.getMemberId())
			.orElseThrow(() -> new ForbiddenException(ErrorCode.ITEM_FORBIDDEN));

		deleteImages(modifyRequest, findItem);
		updateImages(itemImages, findItem);

		String thumbnailUrl = updateThumbnail(findItem, modifyRequest.getThumbnailImage(), thumbnail);
		Category category = findCategory(modifyRequest);

		findItem.modify(modifyRequest, thumbnailUrl, category);
	}

	private void updateImages(List<MultipartFile> itemImages, Item findItem) {
		if (itemImages == null || itemImages.isEmpty()) {
			return;
		}
		if (!itemImages.isEmpty()) {
			List<String> updateImageUrls = imageService.uploadImagesToS3(itemImages);
			imageRepository.saveAllImages(Image.createImage(updateImageUrls, findItem));
		}
	}

	private String updateThumbnail(Item item, String thumbnailUrl, MultipartFile thumbnail) {
		if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
			return item.getThumbnail();
		}
		imageService.deleteImageFromS3(thumbnailUrl);
		imageRepository.deleteImageByItemIdAndImageUrl(item.getId(), thumbnailUrl);
		return imageService.uploadImageToS3(thumbnail);
	}

	private void deleteImages(ItemModifyRequest modifyRequest, Item item) {
		if (modifyRequest.getDeleteImageUrls() == null) {
			return;
		}
		if (!modifyRequest.getDeleteImageUrls().isEmpty()) {
			imageService.deleteImagesFromS3(modifyRequest.getDeleteImageUrls());
			imageRepository.deleteImageByItemIdAndImageUrls(item.getId(), modifyRequest.getDeleteImageUrls());
		}
	}

	private Category findCategory(ItemModifyRequest modifyRequest) {
		return categoryRepository.findById(modifyRequest.getCategoryId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CATEGORY));
	}

	@Transactional
	public void deleteItem(Long itemId, Principal principal) {
		verifyExistsMember(principal.getMemberId());
		verifyExistsItem(itemId);

		Item findItem = itemRepository.findItemByIdAndMemberId(itemId, principal.getMemberId())
			.orElseThrow(() -> new ForbiddenException(ErrorCode.ITEM_FORBIDDEN));

		imageService.deleteImageFromS3(findItem.getThumbnail());
		deleteAllRelatedItem(itemId, principal.getMemberId());
	}

	private void deleteAllRelatedItem(Long itemId, Long memberId) {
		imageRepository.deleteByItemId(itemId);
		wishRepository.deleteByItemIdAndMemberId(itemId, memberId);
		chatroomRepository.deleteAllByItemId(itemId);
		itemRepository.deleteById(itemId);
	}

	private void verifyExistsMember(Long memberId) {
		if (!memberRepository.existsMemberById(memberId)) {
			throw new NotFoundException(ErrorCode.NOT_FOUND_MEMBER);
		}
	}

	private void verifyExistsItem(Long itemId) {
		if (!itemRepository.existsItemById(itemId)) {
			throw new NotFoundException(ErrorCode.NOT_FOUND_ITEM);
		}
	}
}

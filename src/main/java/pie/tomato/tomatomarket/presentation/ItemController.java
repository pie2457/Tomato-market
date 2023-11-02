package pie.tomato.tomatomarket.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.item.ItemService;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.request.item.ItemModifyRequest;
import pie.tomato.tomatomarket.presentation.request.item.ItemRegisterRequest;
import pie.tomato.tomatomarket.presentation.request.item.ItemStatusModifyRequest;
import pie.tomato.tomatomarket.presentation.response.item.ItemDetailResponse;
import pie.tomato.tomatomarket.presentation.response.item.ItemResponse;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> register(@RequestPart("item") ItemRegisterRequest itemRegisterRequest,
		@RequestPart(value = "images", required = false) List<MultipartFile> itemImages,
		@RequestPart("thumbnailImage") MultipartFile thumbnail,
		@AuthPrincipal Principal principal) {
		itemService.register(itemRegisterRequest, thumbnail, itemImages, principal);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{itemId}/status")
	public ResponseEntity<Void> modifyStatus(@PathVariable Long itemId,
		@AuthPrincipal Principal principal,
		@RequestBody ItemStatusModifyRequest request) {
		itemService.modifyStatus(itemId, principal, request);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<CustomSlice<ItemResponse>> findAll(@RequestParam String region,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) Long categoryId) {
		return ResponseEntity.ok().body(itemService.findAll(region, size, cursor, categoryId));
	}

	@PatchMapping("/{itemId}")
	public ResponseEntity<Void> modifyItem(@PathVariable Long itemId,
		@AuthPrincipal Principal principal,
		@RequestPart("item") ItemModifyRequest modifyRequest,
		@RequestPart(value = "images", required = false) List<MultipartFile> itemImages,
		@RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnail) {
		itemService.modifyItem(itemId, principal, modifyRequest, itemImages, thumbnail);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{itemId}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long itemId, @AuthPrincipal Principal principal) {
		itemService.deleteItem(itemId, principal);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<ItemDetailResponse> itemDetails(@PathVariable Long itemId) {
		return ResponseEntity.ok().body(itemService.itemDetails(itemId));
	}
}

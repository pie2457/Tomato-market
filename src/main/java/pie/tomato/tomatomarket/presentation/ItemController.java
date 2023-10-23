package pie.tomato.tomatomarket.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.ItemService;
import pie.tomato.tomatomarket.presentation.request.ItemRegisterRequest;
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
}
package pie.tomato.tomatomarket.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.wish.WishItemService;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishItemController {

	private final WishItemService wishItemService;

	@PostMapping("/{itemId}")
	public ResponseEntity<Void> changeWishStatus(@PathVariable Long itemId, @RequestParam String wish,
		@AuthPrincipal Principal principal) {
		wishItemService.changeWishStatus(itemId, wish, principal);
		return ResponseEntity.ok().build();
	}
}

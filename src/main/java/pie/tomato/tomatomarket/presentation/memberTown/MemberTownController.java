package pie.tomato.tomatomarket.presentation.memberTown;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.memberTown.MemberTownService;
import pie.tomato.tomatomarket.presentation.memberTown.request.AddMemberTownRequest;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class MemberTownController {

	private final MemberTownService memberTownService;

	@PostMapping
	public ResponseEntity<Void> addMemberTown(@AuthPrincipal Principal principal,
		@RequestBody AddMemberTownRequest addMemberTownRequest) {
		memberTownService.addMemberTown(principal, addMemberTownRequest);
		return ResponseEntity.ok().build();
	}
}

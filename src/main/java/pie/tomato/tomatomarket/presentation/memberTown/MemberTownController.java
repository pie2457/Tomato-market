package pie.tomato.tomatomarket.presentation.memberTown;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.memberTown.MemberTownService;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.memberTown.request.AddMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.DeleteMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.SelectMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.response.MemberTownListResponse;
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

	@PutMapping
	public ResponseEntity<Void> selectMemberTown(@AuthPrincipal Principal principal,
		@RequestBody SelectMemberTownRequest selectMemberTownRequest) {
		memberTownService.selectMemberTown(principal, selectMemberTownRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<CustomSlice<MemberTownListResponse>> findAll(@RequestParam String region,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor) {
		return ResponseEntity.ok().body(memberTownService.findAll(region, size, cursor));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMemberTown(@AuthPrincipal Principal principal,
		@RequestBody DeleteMemberTownRequest deleteMemberTownRequest) {
		memberTownService.deleteMemberTown(principal, deleteMemberTownRequest);
		return ResponseEntity.ok().build();
	}
}

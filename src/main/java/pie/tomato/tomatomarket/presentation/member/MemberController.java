package pie.tomato.tomatomarket.presentation.member;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.member.MemberService;
import pie.tomato.tomatomarket.presentation.member.request.NicknameModifyRequest;
import pie.tomato.tomatomarket.presentation.member.response.MemberRegionResponse;
import pie.tomato.tomatomarket.presentation.support.AuthPrincipal;
import pie.tomato.tomatomarket.presentation.support.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	@PutMapping("/{memberId}")
	public ResponseEntity<Void> modifyNickname(@PathVariable Long memberId,
		@RequestBody NicknameModifyRequest request) {
		memberService.modifyNickname(memberId, request.getNickname());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{memberId}")
	public ResponseEntity<Void> modifyThumbnailImage(@PathVariable Long memberId,
		@RequestPart("updateImageFile") MultipartFile profile) {
		memberService.modifyProfile(memberId, profile);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/regions")
	public ResponseEntity<List<MemberRegionResponse>> findAllRegions(@AuthPrincipal Principal principal) {
		return ResponseEntity.ok(memberService.findAllRegions(principal));
	}
}

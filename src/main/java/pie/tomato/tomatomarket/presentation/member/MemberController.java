package pie.tomato.tomatomarket.presentation.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.member.MemberService;
import pie.tomato.tomatomarket.presentation.member.request.NicknameModifyRequest;

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
}

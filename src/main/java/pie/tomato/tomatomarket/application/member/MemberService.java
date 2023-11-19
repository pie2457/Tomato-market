package pie.tomato.tomatomarket.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.application.image.ImageService;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ImageService imageService;

	@Transactional
	public void modifyNickname(Long memberId, String nickname) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		existsNickname(nickname);
		member.changeNickname(nickname);
	}

	private void existsNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME);
		}
	}

	@Transactional
	public void modifyProfile(Long memberId, MultipartFile profile) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		imageService.deleteImageFromS3(member.getProfile());
		String updateThumbnail = imageService.uploadImageToS3(profile);
		member.changeThumbnail(updateThumbnail);
	}
}

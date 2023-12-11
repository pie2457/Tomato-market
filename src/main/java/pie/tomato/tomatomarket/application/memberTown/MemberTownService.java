package pie.tomato.tomatomarket.application.memberTown;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.domain.Member;
import pie.tomato.tomatomarket.domain.MemberTown;
import pie.tomato.tomatomarket.domain.Region;
import pie.tomato.tomatomarket.exception.BadRequestException;
import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.NotFoundException;
import pie.tomato.tomatomarket.infrastructure.persistence.member.MemberRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.memberTown.MemberTownPaginationRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.memberTown.MemberTownRepository;
import pie.tomato.tomatomarket.infrastructure.persistence.region.RegionRepository;
import pie.tomato.tomatomarket.presentation.dto.CustomSlice;
import pie.tomato.tomatomarket.presentation.memberTown.request.AddMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.DeleteMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.request.SelectMemberTownRequest;
import pie.tomato.tomatomarket.presentation.memberTown.response.MemberTownListResponse;
import pie.tomato.tomatomarket.presentation.support.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberTownService {

	public static final int MEMBER_TOWN_MINIMUM_SIZE = 1;

	private final MemberTownRepository memberTownRepository;
	private final RegionRepository regionRepository;
	private final MemberRepository memberRepository;
	private final MemberTownPaginationRepository memberTownPaginationRepository;

	@Transactional
	public void addMemberTown(Principal principal, AddMemberTownRequest memberTownRequest) {
		Member member = memberRepository.findById(principal.getMemberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
		Region region = regionRepository.findById(memberTownRequest.getAddressId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REGION));

		validateMemberTownSizeAndDuplicateRegion(principal, memberTownRequest);

		memberTownRepository.save(MemberTown.of(region, member, true));
	}

	private void validateMemberTownSizeAndDuplicateRegion(
		Principal principal, AddMemberTownRequest memberTownRequest) {
		List<MemberTown> memberTowns = getMemberTowns(principal);

		memberTowns.stream()
			.filter(memberTown -> memberTown.isSameRegionId(memberTownRequest.getAddressId()))
			.findAny()
			.ifPresent(memberTown -> {
				throw new BadRequestException(ErrorCode.ALREADY_ADDRESS);
			});

		if (memberTowns.size() == MEMBER_TOWN_MINIMUM_SIZE) {
			changeIsSelectedFalse(memberTowns, memberTownRequest.getAddressId());
			return;
		}

		if (memberTowns.size() > MEMBER_TOWN_MINIMUM_SIZE) {
			throw new BadRequestException(ErrorCode.MAXIMUM_MEMBER_TOWN_SIZE);
		}
	}

	private void changeIsSelectedFalse(List<MemberTown> memberTowns, Long addressId) {
		memberTowns.stream()
			.filter(memberTown -> !memberTown.isSameRegionId(addressId))
			.forEach(MemberTown::changeIsSelectedFalse);
	}

	private List<MemberTown> getMemberTowns(Principal principal) {
		return memberTownRepository.findAllByMemberId(principal.getMemberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER_TOWN));
	}

	@Transactional
	public void selectMemberTown(Principal principal, SelectMemberTownRequest selectMemberTownRequest) {
		List<MemberTown> memberTowns = getMemberTowns(principal);

		MemberTown selectMemberTown = getMemberTown(selectMemberTownRequest, memberTowns);
		selectMemberTown.changeIsSelectedTrue();

		changeIsSelectedFalse(memberTowns, selectMemberTown.getId());
	}

	private MemberTown getMemberTown(SelectMemberTownRequest selectMemberTownRequest,
		List<MemberTown> memberTowns) {
		return memberTowns.stream()
			.filter(memberTown -> memberTown.isSameRegionId(selectMemberTownRequest.getSelectedAddressId()))
			.findAny()
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER_TOWN));
	}

	public CustomSlice<MemberTownListResponse> findAll(String addressName, int size, Long cursor) {
		Slice<MemberTownListResponse> responses =
			memberTownPaginationRepository.findByAddressName(addressName, size, cursor);
		List<MemberTownListResponse> content = responses.getContent();

		Long nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getAddressId();

		return new CustomSlice<>(content, nextCursor, responses.hasNext());
	}

	@Transactional
	public void deleteMemberTown(Principal principal, DeleteMemberTownRequest deleteMemberTownRequest) {
		List<MemberTown> memberTowns = getMemberTowns(principal);

		if (memberTowns.size() <= MEMBER_TOWN_MINIMUM_SIZE) {
			throw new BadRequestException(ErrorCode.MINIMUM_MEMBER_TOWN_SIZE);
		}

		for (MemberTown memberTown : memberTowns) {
			deleteMemberTownAndChangeIsSelected(principal, deleteMemberTownRequest, memberTown);
		}
	}

	private void deleteMemberTownAndChangeIsSelected(Principal principal,
		DeleteMemberTownRequest deleteMemberTownRequest, MemberTown memberTown) {
		if (memberTown.isSameRegionId(deleteMemberTownRequest.getAddressId())) {
			memberTownRepository.deleteByMemberIdAndRegionId(
				principal.getMemberId(), deleteMemberTownRequest.getAddressId());
			return;
		}
		memberTown.changeIsSelectedTrue();
	}
}

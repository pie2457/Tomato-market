package pie.tomato.tomatomarket.presentation.member.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameModifyRequest {

	private String nickname;
}

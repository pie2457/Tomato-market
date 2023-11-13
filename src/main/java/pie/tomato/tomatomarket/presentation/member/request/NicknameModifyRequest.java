package pie.tomato.tomatomarket.presentation.member.request;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class NicknameModifyRequest {

	private String nickname;
}

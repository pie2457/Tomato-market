package pie.tomato.tomatomarket.presentation.memberTown.request;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class AddMemberTownRequest {

	private Long addressId;
}

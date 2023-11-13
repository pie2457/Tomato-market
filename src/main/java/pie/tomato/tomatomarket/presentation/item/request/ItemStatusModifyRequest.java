package pie.tomato.tomatomarket.presentation.item.request;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ItemStatusModifyRequest {

	private String status;
}

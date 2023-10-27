package pie.tomato.tomatomarket.infrastructure.persistence;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import pie.tomato.tomatomarket.presentation.request.item.ItemResponse;

public interface PaginationRepository {

	default SliceImpl<ItemResponse> checkLastPage(int size, List<ItemResponse> results) {

		boolean hasNext = false;

		if (results.size() > size) {
			hasNext = true;
			results.remove(size);
		}

		return new SliceImpl<>(results, PageRequest.ofSize(size), hasNext);
	}
}

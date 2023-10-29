package pie.tomato.tomatomarket.infrastructure.persistence;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

public interface PaginationRepository<T> {

	default SliceImpl<T> checkLastPage(int size, List<T> results) {

		boolean hasNext = false;

		if (results.size() > size) {
			hasNext = true;
			results.remove(size);
		}

		return new SliceImpl<>(results, PageRequest.ofSize(size), hasNext);
	}
}

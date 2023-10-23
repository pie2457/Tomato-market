package pie.tomato.tomatomarket.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

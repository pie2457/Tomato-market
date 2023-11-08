package pie.tomato.tomatomarket.infrastructure.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

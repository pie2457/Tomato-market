package pie.tomato.tomatomarket.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pie.tomato.tomatomarket.domain.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

	void deleteByItemId(Long itemId);
}

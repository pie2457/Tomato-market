package pie.tomato.tomatomarket.application.redis;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.persistence.item.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemViewCountRedisService {

	public static final String ITEM_ID_PREFIX = "itemId: ";
	private static final Pattern ITEM_ID_PATTERN = Pattern.compile("itemId:*");

	private final RedisTemplate<String, String> redisTemplate;
	private final ItemRepository itemRepository;

	public void addViewCount(String nickname, Long itemId) {
		String key = ITEM_ID_PREFIX + itemId;
		String duplicateKey = nickname;
		ValueOperations<String, String> value = redisTemplate.opsForValue();

		if (isNotFirstView(duplicateKey)) {
			return;
		}

		if (value.get(key) != null) {
			value.increment(key);
			return;
		}

		value.set(duplicateKey, String.valueOf(itemId));
		value.set(key, "1", Duration.ofMinutes(1));
	}

	private boolean isNotFirstView(String duplicateKey) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(duplicateKey));
	}

	@Transactional
	@Scheduled(cron = "0 0/1 * * * ?")
	public void deleteViewCountCache() {
		Set<String> keys = Optional.ofNullable(redisTemplate.keys(ITEM_ID_PATTERN.pattern()))
			.orElseGet(Collections::emptySet);

		for (String key : keys) {
			Long itemId = Long.parseLong(key.split(" ")[1]);
			Long viewCount = Long.valueOf(Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse("0"));
			Long originViewCount = itemRepository.findViewCountById(itemId);
			itemRepository.addViewCountFromRedis(itemId, originViewCount + viewCount);
			redisTemplate.delete(key);
			redisTemplate.delete("itemId: " + itemId);
		}
	}
}

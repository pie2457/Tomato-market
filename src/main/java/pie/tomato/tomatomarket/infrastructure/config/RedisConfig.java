package pie.tomato.tomatomarket.infrastructure.config;

import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        var nodes = redisProperties.getSentinel().getNodes().stream().map(node -> {
            String[] hostAndPort = node.split(":");
            return new RedisNode(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        }).collect(Collectors.toList());
        var sentinelConfiguration = new RedisSentinelConfiguration()
            .master(redisProperties.getSentinel().getMaster());
        sentinelConfiguration.setSentinels(nodes);

        return new LettuceConnectionFactory(sentinelConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}

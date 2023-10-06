package zerobase.ShowMeTheDividend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    /** host서버의 IP주소와 포트번호가 설정된 객체를 생성하는 매서드.
     * 이 객체를 통해 레디스와 연결할 수 있다.
     * */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // 싱글 인스턴스 전용
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(this.host);
        conf.setPort(Integer.parseInt(this.port));
        return new LettuceConnectionFactory(conf);
    }

    /** 매서드 redisConnectionFactory()로 만들어진 객체를 캐시에 적용.
     * 그리고 직렬화(Serialization)를 통해 자바의 객체를 레디스에서 사용할 수 있도록 함.
     * */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        // key와 value를 직렬화하기
        // 직렬화 : 값을 바이트 형태로 변환.
        // 자바에서 사용되던 데이터를 다른 프로그램(레디스)에서도 사용 가능하게 한다.
        // (역직렬화 : 바이트 형태의 데이터를 자바에서 사용가능한 값으로 변경)
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(conf)
                .build();
    }
}

package io.simpolor.redis2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /* @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    } */

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost",6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.none());

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        jedisClientConfigurationBuilder.connectTimeout(Duration.ofSeconds(3));

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfigurationBuilder.build());

        return  jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        // 데이터 형태에 따른 세팅
        // serializer를 명시하지 않으면 앞에 unicode가 붙는 문제가 발생
        template.setKeySerializer( new StringRedisSerializer() );

        template.setValueSerializer( new StringRedisSerializer() );
        template.setValueSerializer( new GenericToStringSerializer<>( Object.class ) );
        template.setValueSerializer( new GenericJackson2JsonRedisSerializer() );

        template.setHashValueSerializer( new GenericToStringSerializer<>( Object.class ) );

        return template;
    }

}

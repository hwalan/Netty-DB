package com.netty.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;
	
	@Value("${spring.data.redis.port}")
	private int port;
	
	@Value("${spring.data.redis.password}")
	private String password;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// RedisStandaloneConfiguration은 single node에 redis를 연결하기 위해 
		// redis 정보(host, port, password)를 설정하는 클래스
		RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
		rsc.setHostName(host);
		rsc.setPort(port);
		rsc.setPassword(password);
		
		LettuceConnectionFactory lcf = new LettuceConnectionFactory(rsc);
		return lcf;
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		// Redis 서버에서 명령어를 수행하기 위해 추상화를 제공하며
		// Serialize, Deserialize로 Redis 서버에 CRUD를 할 수 있는 operation interface도 제공
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		
		return template;
	}
	
}

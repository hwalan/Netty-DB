package com.netty.redis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRepository {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${spring.data.redis.key}")
	private String key;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	public boolean logInsert(String type, String message, LocalDateTime time) {
		
		String formatTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String ymd = formatTime.substring(0, 8);
		String hms = formatTime.substring(8, 14);
		
		type = type + "_" + hms;
		
		logger.info("RedisRepository logInsert :: " + key + ymd + " | " + type + " | " + message);
		// EX) key = log_table_20230110, field = Server_170000, message = Server Start
		
		try {
			HashOperations<String, String, String> hash = redisTemplate.opsForHash();
			hash.put(key + ymd, type, message);
			
			return true;
		} catch (Exception e) {
			logger.error("RedisRepository ERROR :: ", e);
			return false;
		}
	}

}
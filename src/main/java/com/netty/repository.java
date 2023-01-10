package com.netty;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netty.jpa.JpaRepository;
import com.netty.redis.RedisRepository;

@Component
public class Repository {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${service}")
	private String service;

	@Autowired
	JpaRepository jpa;
	
	@Autowired
	RedisRepository redis;
	
	public boolean logInsert(String type, String message, LocalDateTime time) {
		
		logger.info("Repository :: " + service);
		
		switch (service) {
			case "jpa": {
				return jpa.logInsert(type, message, time);
			}
			case "redis": {
				return redis.logInsert(type, message, time);
			}
		}
		
		return false;
	}
}

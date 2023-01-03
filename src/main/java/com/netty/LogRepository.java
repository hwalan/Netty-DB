package com.netty;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class LogRepository {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public boolean logInsert(String type, String message) {
		
		try {
			
			LogEntity entity = new LogEntity();
			
			entity.setType(type);
			entity.setMessage(message);
			entity.setTime(LocalDateTime.now());
			
			logger.info("LogRepository logInsert :: " + entity.toString());
			em.persist(entity);
			
		} catch (Exception e) {
			logger.error("LogRepository ERROR : ", e);
		}
		
		return true;
	}

}

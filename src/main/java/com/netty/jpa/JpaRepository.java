package com.netty.jpa;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class JpaRepository {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public boolean logInsert(String type, String message, LocalDateTime time) {
		
		try {
			
			LogEntity entity = new LogEntity();
			
			entity.setType(type);
			entity.setMessage(message);
			entity.setTime(time);
			
			logger.info("JpaRepository logInsert :: " + entity.toString());
			em.persist(entity);
			
			return true;
		} catch (Exception e) {
			logger.error("JpaRepository ERROR : ", e);
			return false;
		}
	}

}

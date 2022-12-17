package com.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NettyClientScheduler {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
	@Autowired
	NettyClient client;
	
	@Scheduled(fixedDelay = 5000, initialDelay = 5000)
	public void scheduler() throws Exception {
		logger.info("NettyClientScheduler : " + vo.isClientAlive());
		
		if (!vo.isClientAlive()) {
			client.service();
		}
		
	}
	
}

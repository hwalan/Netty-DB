package com.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*@Component*/
public class NettyServerScheduler {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
	@Autowired
	NettyServer server;
	
	@Scheduled(fixedDelay = 2000, initialDelay = 1000)
	public void schedler() throws Exception {
		logger.info("NettyServerScheduler : " + vo.isServerAlive());
		
		if (!vo.isServerAlive()) {
			server.service();
		}

	}
	
}

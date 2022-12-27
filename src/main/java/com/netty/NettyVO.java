package com.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
public class NettyVO {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean isServerAlive = false;
	private boolean isClientAlive = false;
	
	public synchronized long getTime(String type) {
		int num = (int) (Math.random() * 7) + 2;
		long time = num * 1000;
		
		logger.info(type + " NettyVO setTime() :: " + time);
		
		// 랜덤 숫자에 따라 시간을 다르게하여 반환
		return time;
	}
	
	public synchronized boolean isClose(String type) {
		int num = (int) (Math.random() * 9);

		logger.info(type + " NettyVO isClose num :: " + num);
		
		// 만약 랜덤 숫자가 짝수면 메시지 전달
		if (num % 2 == 0) return true;
		// 랜덤 숫자가 홀수일 경우 Netty 종료
		else return false;
	}
	
}

package com.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netty.LogRepository;
import com.netty.NettyVO;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
	@Autowired
	LogRepository repository;
	
	// channel이 EventLoop에 등록되었을때 호출
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("NettyClient channelRegistered");
		ctx.writeAndFlush("Hello World Client");
	}

	// 이벤트가 수신되었을때 호출
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info("NettyClient channelRead() : " + msg);
		
		// 랜덤 시간만큼 sleep 후 다음 작업 수행 (Out Of Memory 방지)
		Thread.sleep(vo.getTime("NettyClient"));
		
		if (vo.isClose("NettClient")) {
			String message = "Return Message :: " + System.currentTimeMillis();
			
			repository.logInsert("Client", message);
			ctx.writeAndFlush(message);
		} else {
			repository.logInsert("Client", "Server UnRegistered");
			ctx.close();
		}
	}

}

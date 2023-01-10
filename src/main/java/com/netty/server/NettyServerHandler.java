package com.netty.server;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netty.NettyVO;
import com.netty.Repository;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
	@Autowired
	Repository repository;

	// channel이 EventLoop에 등록되었을때 호출
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("NettyServer channelRegistered");
		ctx.writeAndFlush("Hello World Server");
	}

	// 이벤트가 수신되었을때 호출
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info("NettyServer channelRead() : " + msg);
		
		// 랜덤 시간만큼 sleep 후 다음 작업 수행 (Out Of Memory 방지)
		Thread.sleep(vo.getTime("NettyServer"));
		
		if (vo.isClose("NettyServer")) {
			String message = "Return Message :: " + System.currentTimeMillis();
			
			repository.logInsert("Server", message, LocalDateTime.now());
			ctx.writeAndFlush(message);
		} else {
			repository.logInsert("Server", "Client UnRegistered", LocalDateTime.now());
			ctx.close();
		}
	}

}

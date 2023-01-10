package com.netty.client;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netty.NettyVO;
import com.netty.Repository;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class NettyClient {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	NettyVO vo;
	
	@Autowired
	Repository repository;
	
	@Autowired
	NettyClientInitializer initializer;

	public void service() {
		logger.info("NettyClient");

		// NioEventLoopGroup : 멀티Thread 환경의 그룹
		// 기본 생성자 사용 시 CPU core * 2개의 EventLoop 생성
		EventLoopGroup group = new NioEventLoopGroup();

		try {

			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(initializer);

			ChannelFuture f = b.connect("127.0.0.1", 80).sync();
			
			repository.logInsert("Client", "Client Start", LocalDateTime.now());

			f.channel().closeFuture().sync();

		} catch (Exception e) {
			logger.error("NettyClient ERROR : ", e);
		} finally {
			logger.info("NettyClient close");
			repository.logInsert("Client", "Client Close", LocalDateTime.now());
			
			group.shutdownGracefully();
			vo.setClientAlive(false);
		}
	}

}

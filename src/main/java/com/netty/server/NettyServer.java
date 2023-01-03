package com.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netty.LogRepository;
import com.netty.NettyVO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
public class NettyServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
	@Autowired
	LogRepository repository;
	
	@Autowired
	NettyServerInitializer initializer;
	
	public void service() {
		logger.info("NettyServer");
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.childOption(ChannelOption.SO_LINGER, 0)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(initializer);
			
			ChannelFuture f = b.bind(80).sync();
			
			repository.logInsert("Server", "Server Start");

			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			logger.error("NettyServer ERROR : ", e);
		} finally {
			logger.info("NettyServer close");
			repository.logInsert("Server", "Server Close");
			
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			vo.setServerAlive(false);
		}
		
	}
	
}

package com.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
public class NettyServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyVO vo;
	
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
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					logger.info("NettyClient initChannel()");
					
					ChannelPipeline p = ch.pipeline();
					p.addLast(new StringDecoder());
					p.addLast(new StringEncoder());
					p.addLast(new SimpleChannelInboundHandler<String>() {
						
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
								ctx.writeAndFlush("Return Message :: " + System.currentTimeMillis());
							} else {
								ctx.close();
							}
						}
					});
				}
			});
			
			ChannelFuture f = b.bind(80).sync();

			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			logger.error("NettyServer ERROR : ", e);
		} finally {
			logger.info("NettyServer close");
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			vo.setServerAlive(false);
		}
		
	}
	
}

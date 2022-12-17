package com.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component
public class NettyClient {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	NettyVO vo;

	public void service() {
		logger.info("NettyClient");

		// NioEventLoopGroup : 멀티Thread 환경의 그룹
		// 기본 생성자 사용 시 CPU core * 2개의 EventLoop 생성
		EventLoopGroup group = new NioEventLoopGroup();

		try {

			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					logger.info("NettyClient initChannel()");
					// channel과 handler 사이에서 연결 통로 역할
					// StringDecoder와 StringEncoder를 통해 channel과 handler는 String 형식으로 이벤트 송수신
					ChannelPipeline p = ch.pipeline();
					p.addLast(new StringDecoder());
					p.addLast(new StringEncoder());
					p.addLast(new SimpleChannelInboundHandler<String>() {

						// channel이 EventLoop에 등록되었을때 호출
						@Override
						public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
							logger.info("NettyClient channelRegistered");
							ctx.writeAndFlush("Hello World");
						}

						// 이벤트가 수신되었을때 호출
						@Override
						protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
							logger.info("NettyClient channelRead() : " + msg);
							Thread.sleep(1000);
							ctx.close();
						}
					});
				}
			});

			ChannelFuture f = b.connect("127.0.0.1", 80).sync();

			f.channel().closeFuture().sync();

		} catch (Exception e) {
			logger.error("NettyClient ERROR : ", e);
		} finally {
			logger.info("NettyClient close");
			group.shutdownGracefully();
			vo.setClientAlive(false);
		}
	}

}

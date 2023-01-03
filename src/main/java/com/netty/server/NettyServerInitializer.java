package com.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyServerHandler handler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		logger.info("NettyClient initChannel()");
		
		ChannelPipeline p = ch.pipeline();
		p.addLast(new StringDecoder());
		p.addLast(new StringEncoder());
		p.addLast(handler);
	}

}

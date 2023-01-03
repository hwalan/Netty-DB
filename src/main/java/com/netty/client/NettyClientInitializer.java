package com.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netty.LogRepository;
import com.netty.NettyVO;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NettyClientHandler handler;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		logger.info("NettyClient initChannel()");
		// channel과 handler 사이에서 연결 통로 역할
		// StringDecoder와 StringEncoder를 통해 channel과 handler는 String 형식으로 이벤트 송수신
		ChannelPipeline p = ch.pipeline();
		p.addLast(new StringDecoder());
		p.addLast(new StringEncoder());
		p.addLast(handler);
	}


}

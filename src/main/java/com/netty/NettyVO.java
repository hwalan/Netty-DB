package com.netty;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
public class NettyVO {

	private boolean isServerAlive = false;
	private boolean isClientAlive = false;
}

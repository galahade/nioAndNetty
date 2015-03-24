package com.young.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class DiscardServerHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		((ByteBuf) msg).release();
	}
	
	public void exceptioCaught(ChannelHandlerContext ctx, Throwable cause)  {
		cause.printStackTrace();
		ctx.close();
	}

}

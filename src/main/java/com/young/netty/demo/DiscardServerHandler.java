package com.young.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf)msg;
		try{
			if (in.isReadable()) {
				/*2
				 System.out.print((char)in.readByte());
				System.out.flush();*/
				/*3
				System.out.println(in.toString(CharsetUtil.UTF_8));*/
				ctx.write(msg);
				ctx.flush();
			}
		} finally {
			/*2,3
			ReferenceCountUtil.release(msg);*/
		}
		/*1
		((ByteBuf) msg).release();*/
	}
	
	public void exceptioCaught(ChannelHandlerContext ctx, Throwable cause)  {
		cause.printStackTrace();
		ctx.close();
	}

}

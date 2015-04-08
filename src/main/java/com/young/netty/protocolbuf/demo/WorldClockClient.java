package com.young.netty.protocolbuf.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.Arrays;
import java.util.List;

public class WorldClockClient {

	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));
	static final List<String> CITIES = Arrays.asList(System.getProperty(
			"cities", "Asia/Seoul,Europe/Berlin,America/Los_Angeles").split(","));

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx ;
		if(SSL) {
			sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new WorldClockClientInitializer(sslCtx));
			
			Channel ch = b.connect(HOST, PORT).sync().channel();
			
			WorldClockClientHandler handler = ch.pipeline().get(WorldClockClientHandler.class);
			
			List<String> response = handler.getLocalTimes(CITIES);
			
			ch.close();
			
			for(int i = 0; i < CITIES.size(); i++) {
				System.out.format("%28s: %s%n", CITIES.get(i), response.get(i));
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}

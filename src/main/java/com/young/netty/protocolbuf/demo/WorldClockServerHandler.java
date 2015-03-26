package com.young.netty.protocolbuf.demo;

import java.util.TimeZone;

import com.young.netty.protocolbuf.demo.WorldClockProtocol.LocalTimes;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.Location;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.Locations;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WorldClockServerHandler extends SimpleChannelInboundHandler<Locations> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Locations locations)
			throws Exception {
		
		long currentTime = System.currentTimeMillis();
		
		LocalTimes.Builder builder = LocalTimes.newBuilder();
		
		for(Location l : locations.getLocationList()) {
			TimeZone tz = TimeZone.getTimeZone(ID)
		}
		// TODO Auto-generated method stub
		
	}

}

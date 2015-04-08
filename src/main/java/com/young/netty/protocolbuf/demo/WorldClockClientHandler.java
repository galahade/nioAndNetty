package com.young.netty.protocolbuf.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import com.young.netty.protocolbuf.demo.WorldClockProtocol.Continent;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.LocalTime;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.LocalTimes;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.Location;
import com.young.netty.protocolbuf.demo.WorldClockProtocol.Locations;

public class WorldClockClientHandler extends SimpleChannelInboundHandler<LocalTimes> {
	
	private static final Pattern DELIM = Pattern.compile("/");
	
	private volatile Channel channel;
	
	private final BlockingQueue<LocalTimes> answer = new LinkedBlockingQueue<LocalTimes>();

	public WorldClockClientHandler() {
		super(false);
	}
	
	public List<String> getLocalTimes(Collection<String> cities) {
        Locations.Builder builder = Locations.newBuilder();

        for (String c: cities) {
            String[] components = DELIM.split(c);
            builder.addLocation(Location.newBuilder().
                setContinent(Continent.valueOf(components[0].toUpperCase())).
                setCity(components[1]).build());
        }
        
        Locations locations = builder.build();
        
        channel.writeAndFlush(locations);

        LocalTimes localTimes;
        boolean interrupted = false;
        for (;;) {
            try {
                localTimes = answer.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        List<String> result = new ArrayList<String>();
        for (LocalTime lt: localTimes.getLocalTimeList()) {
            result.add(
                    new Formatter().format(
                            "%4d-%02d-%02d %02d:%02d:%02d %s",
                            lt.getYear(),
                            lt.getMonth(),
                            lt.getDayOfMonth(),
                            lt.getHour(),
                            lt.getMinute(),
                            lt.getSecond(),
                            lt.getDayOfWeek().name()).toString());
        }

        return result;
    }

	

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, LocalTimes times) {
        answer.add(times);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

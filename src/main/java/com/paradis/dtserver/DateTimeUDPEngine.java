package com.paradis.dtserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUDPEngine implements Runnable {
    private int port;

    public DateTimeUDPEngine(int port) {
        this.port = port;
    }

    public void run()  {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new UDPHandler());

            ChannelFuture f = b.bind(port).sync();

            System.out.println("UDP engine now listening to port " + port);

            f.channel().closeFuture().sync();
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        finally {
            group.shutdownGracefully();
        }
    }
}

class UDPHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        String response = null;
        String payload = packet.content().toString(CharsetUtil.UTF_8);
        switch (payload) {
            case "date":
                response = getIsoDate();
                break;
            case "time":
                response = getIsoTime();
                break;
            case "datetime":
                response =  getIsoDateTime();
                break;
            default:
                // intentionally blank
                break;
        }

        if(response != null) {
            ctx.write(new DatagramPacket(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private String getIsoDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private String getIsoTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("hh:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private String getIsoDateTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }


}
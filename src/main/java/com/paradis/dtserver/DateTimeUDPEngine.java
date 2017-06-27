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


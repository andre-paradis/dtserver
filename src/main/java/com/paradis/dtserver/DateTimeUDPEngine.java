package com.paradis.dtserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

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
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(DatagramChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //IdleStateHandler idleHandler = new IdleStateHandler(10,0,0);
                            p.addLast(new UDPHandler());
                            //p.addLast(idleHandler);
                            //p.addLast(new IdleEventHandler());
                        }
                    });


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


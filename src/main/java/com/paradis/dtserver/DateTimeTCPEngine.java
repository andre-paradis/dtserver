package com.paradis.dtserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Netty server configured to handle the challenge custom protocol over TCP.
 */
public class DateTimeTCPEngine implements Runnable {

    private int _port;
    private int _idleTimeoutSeconds;
    private EventLoopGroup  _bossGroup;
    private EventLoopGroup  _workerGroup;


    public DateTimeTCPEngine(int port, int idleTimeoutSeconds) {
        _port = port;
        _idleTimeoutSeconds = idleTimeoutSeconds;
    }

    public void run() {
        try {
            _bossGroup = new NioEventLoopGroup();
            _workerGroup = new NioEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap();

            b.group(_bossGroup, _workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("Accepting new connection");
                            // ensure packet are buffered until a ull line is received.
                            DelimiterBasedFrameDecoder frameDecoder = new DelimiterBasedFrameDecoder(80,true, Delimiters.lineDelimiter());
                            // disconnect client after a read idle timeout.
                            IdleStateHandler idleHandler = new IdleStateHandler(_idleTimeoutSeconds,0,0);
                            ch.pipeline().addLast(frameDecoder, new TCPHandler(), idleHandler, new IdleEventHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(_port).sync();
            System.out.println("TCP engine now listening to port " + _port);
            f.channel().closeFuture().sync();
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        finally {
            _workerGroup.shutdownGracefully();
            _bossGroup.shutdownGracefully();
        }
    }

}


package com.paradis.dtserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * Created by andre.paradis on 2017-06-27.
 */
class TCPHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Discard the received data silently.
         String command = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);

        CommandProcessor cmdProcessor = new CommandProcessor();
        String response = cmdProcessor.processCommand(command);

        if(response != null) {
            ctx.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
            ctx.flush();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

package com.paradis.dtserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * Handles channel operations for the TCP engine.
 */
class TCPHandler extends ChannelInboundHandlerAdapter {

    /**
     * channelRead is called when a line has been received.  This line is passed through the
     * command processor to get a response back. Incomplete line are avoided du to the use of
     * DelimiterBasedFrameDecoder that does the job of buffering packets until a full line  is received.
     * @param ctx
     * @param msg
     */
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

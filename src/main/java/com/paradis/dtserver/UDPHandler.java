package com.paradis.dtserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * Class responsible to handle channel operation for the UDP protocol.  It is assumed that a command
 * is always received in a single datagram packet.  The same command processor used in the TCP
 * implementation is reused.
 */
class UDPHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        String response = null;
        String command = packet.content().toString(CharsetUtil.UTF_8);

        CommandProcessor cmdProcessor = new CommandProcessor();
        response = cmdProcessor.processCommand(command);

        if(response != null) {
            ctx.write(new DatagramPacket(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
            ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}

package com.paradis.dtserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class IdleEventHandler extends ChannelDuplexHandler {

    public IdleEventHandler(){

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                System.out.println("Closing connection after timeout period");
                ctx.write(Unpooled.copiedBuffer("Timeout!\n", CharsetUtil.UTF_8));
                ctx.flush();
                ctx.close();
            }
        }
    }

}

package com.paradis.dtserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * Handler that receives event triggered by IdleStateHandler.  If event is idle
 * timeout on read operation, the channel is closed.
 */
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

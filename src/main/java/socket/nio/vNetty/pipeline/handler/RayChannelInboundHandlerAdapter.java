package socket.nio.vNetty.pipeline.handler;

import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;

public class RayChannelInboundHandlerAdapter implements RayChannelInboundHandler {

    @Override
    public void channelRead(RayChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(RayChannelHandlerContext ctx) {

    }
}

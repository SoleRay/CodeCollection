package socket.nio.vNetty.pipeline.handler;

import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;

public interface RayChannelInboundHandler extends RayChannelHandler {

    void channelRead(RayChannelHandlerContext ctx, Object msg);
}

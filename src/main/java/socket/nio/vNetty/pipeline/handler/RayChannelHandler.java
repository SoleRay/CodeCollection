package socket.nio.vNetty.pipeline.handler;

import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;

public interface RayChannelHandler {

    void channelRead(RayAbstractChannelHandlerContext ctx, Object msg);
}

package socket.nio.vNetty.pipeline;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

public interface RayChannelPipeline {

    void addLast(RayChannelHandler ...handler);

    void remove(RayChannelHandler handler);

    void fireChannelRead(Object msg);

    RayChannel channel();

    void invokeHandlerAddedIfNeeded();
}

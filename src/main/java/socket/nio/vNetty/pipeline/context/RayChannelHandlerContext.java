package socket.nio.vNetty.pipeline.context;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.pipeline.RayChannelPipeline;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

public interface RayChannelHandlerContext {

    void fireChannelRead(Object msg);

    RayChannel channel();

    RayChannelHandlerContext prev();

    RayChannelHandlerContext next();

    RayChannelPipeline pipeline();

    RayChannelHandler handler();
}

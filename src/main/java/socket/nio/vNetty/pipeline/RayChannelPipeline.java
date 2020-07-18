package socket.nio.vNetty.pipeline;

import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

public interface RayChannelPipeline {

    void addLast(RayChannelHandler handler);

    void fireChannelRead(Object msg);
}

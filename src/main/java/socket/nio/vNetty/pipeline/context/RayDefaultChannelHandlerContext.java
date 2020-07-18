package socket.nio.vNetty.pipeline.context;

import socket.nio.vNetty.pipeline.RayChannelPipeline;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

public class RayDefaultChannelHandlerContext extends RayAbstractChannelHandlerContext {

    public RayDefaultChannelHandlerContext(RayChannelPipeline pipeline, RayChannelHandler handler) {
        super(pipeline, handler);
    }
}

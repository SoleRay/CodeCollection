package socket.nio.vNetty.pipeline.context;

public interface RayChannelHandlerContext {

    void fireChannelRead(Object msg);
}

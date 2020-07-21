package socket.nio.vNetty.pipeline.handler;

import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;

public abstract class RayChannelInitializer implements RayChannelInboundHandler{

    @Override
    public void channelRead(RayChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(RayChannelHandlerContext ctx){
        try{
            initChannel(ctx);
        }finally {
            ctx.pipeline().remove(this);
        }
    }

    protected abstract void initChannel(RayChannelHandlerContext ctx);
}

package socket.nio.vNetty.customer;

import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandlerAdapter;

public class MyInBoundHandlerB extends RayChannelInboundHandlerAdapter {

    @Override
    public void channelRead(RayChannelHandlerContext ctx, Object msg) {
        System.out.println("handlerB——"+this+" get msg:"+msg);
        super.channelRead(ctx, msg);
    }
}

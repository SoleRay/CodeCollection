package socket.nio.vNetty.customer;

import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandlerAdapter;

public class MyInBoundHandlerA extends RayChannelInboundHandlerAdapter {

    private int a = 5;

    @Override
    public void channelRead(RayChannelHandlerContext ctx, Object msg) {
        System.out.println("handlerA——"+this+" get msg:"+msg);
        super.channelRead(ctx, msg);
        System.out.println("a="+(a++));
    }
}

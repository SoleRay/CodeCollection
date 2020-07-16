package socket.netty.mydemo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InBoundHandlerA extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerA read....");
        super.channelRead(ctx, msg);

        /**
         * 区别，通过ctx调用writeAndFlush，将直接调用当前Handler的前一个outBoundHandler
         * 而通过ctx.channel()调用writeAndFlush，将交给pipeline，从tail开始调用outBoundHandler
         */
        ctx.writeAndFlush(ctx.alloc().buffer(16).writeBytes("hello".getBytes()));
        ctx.channel().writeAndFlush(ctx.alloc().buffer(16).writeBytes("hello".getBytes()));
    }
}

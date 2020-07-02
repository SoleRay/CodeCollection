package socket.netty.demo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerChannelInboundHandler extends ChannelInboundHandlerAdapter {

        private void print(ChannelHandlerContext ctx){
            String methodName  = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.out.println( ctx.channel().getClass().getSimpleName()+":"+methodName+"——"+ctx.channel().localAddress());
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelActive(ctx);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelInactive(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            print(ctx);
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelReadComplete(ctx);
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelWritabilityChanged(ctx);
        }
    }
package socket.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;

public class ChildChannelInboundHandler extends ChannelInboundHandlerAdapter {

        private void print(ChannelHandlerContext ctx){
            String methodName  = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.out.println(ctx.channel().getClass().getSimpleName()+":"+ methodName+"——"+ctx.channel().localAddress());
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            print(ctx);
            super.channelActive(ctx);
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
            String returnMsg = "hello girl!!!";
            System.out.println("服务器端收到来自客户端的消息："+msg);
            System.out.println("服务器端将向客户端返回消息"+returnMsg);
            ByteBuf buf = Unpooled.directBuffer(100);
            buf.writeBytes(returnMsg.getBytes());
            ctx.channel().writeAndFlush(buf);
//            super.channelRead(ctx, msg);
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
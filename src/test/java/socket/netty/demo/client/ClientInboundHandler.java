package socket.netty.demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class ClientInboundHandler extends ChannelInboundHandlerAdapter {

        private void print(ChannelHandlerContext ctx){
            String methodName  = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.out.println( methodName+"——"+ctx.channel().localAddress());
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

//            CompletableFuture future = CompletableFuture.runAsync(()->{
                ByteBuf byteBuf = (ByteBuf) msg;
                byte[] bytes = new byte[byteBuf.writerIndex()];
                try{
                    byteBuf.readBytes(bytes,0,byteBuf.writerIndex());
                }catch (Exception e){
                    e.printStackTrace();
                }
                String content = new String(bytes);
                System.out.println("客户端收到服务器端的消息："+content);
//            }).thenRun(()->{
//                System.out.println("请输入");
//                Scanner scanner = new Scanner(System.in);
//                String s = scanner.nextLine();
//                ctx.channel().writeAndFlush(new Date() + ": hello world!");
//            });
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
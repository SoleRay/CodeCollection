package socket.netty.mydemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import socket.netty.mydemo.handler.InBoundHandlerA;
import socket.netty.mydemo.handler.InBoundHandlerB;
import socket.netty.mydemo.handler.OutBoundHandlerA;
import socket.netty.mydemo.handler.OutBoundHandlerB;

/**
 * Created by Administrator on 2016-10-28.
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new InBoundHandlerA());
                    ch.pipeline().addLast(new InBoundHandlerB());
                    ch.pipeline().addLast(new OutBoundHandlerA());
                    ch.pipeline().addLast(new OutBoundHandlerB());
                }
            });

            Channel channel = bootstrap.bind(8000).sync().channel();
            channel.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}

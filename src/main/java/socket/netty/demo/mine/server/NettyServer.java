package socket.netty.demo.mine.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import socket.netty.demo.mine.handler.*;

/**
 * Created by Administrator on 2016-10-28.
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ShareInBoundHandler shareInBoundHandler = new ShareInBoundHandler();
        try{
            bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(shareInBoundHandler);
//                    ch.pipeline().addLast(new InBoundHandlerA());
//                    ch.pipeline().addLast(new InBoundHandlerB());
//                    ch.pipeline().addLast(new OutBoundHandlerA());
//                    ch.pipeline().addLast(new OutBoundHandlerB());
                }
            });

            Channel channel = bootstrap.bind("192.168.0.187",9090).sync().channel();
            channel.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}

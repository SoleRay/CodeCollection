package socket.netty.mydemo.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import socket.netty.mydemo.handler.InBoundHandlerB;

import java.util.Date;

/**
 * Created by Administrator on 2016-10-30.
 */
public class NettyClient {

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new InBoundHandlerB());
                    }
                });
        Channel channel= bootstrap.connect("127.0.0.1", 8000).sync().channel();
        channel.writeAndFlush(new Date() + ": hello world!");
        channel.closeFuture().sync();

    }
}

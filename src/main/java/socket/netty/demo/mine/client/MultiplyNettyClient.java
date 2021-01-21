package socket.netty.demo.mine.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;

/**
 * Created by Administrator on 2016-10-30.
 */
public class MultiplyNettyClient {

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) {
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new InBoundHandlerB());
                    }
                });
        InetSocketAddress remoteAddress = new InetSocketAddress("127.0.0.1", 8000);

        int clientNum = 3;

        for (int i = 0; i < clientNum; i++) {
//            Channel channel = bootstrap.bind(10000+i).sync().channel().connect(remoteAddress).sync().channel();
//            channel.writeAndFlush("hello,"+i);
            Channel channel= bootstrap.connect(remoteAddress).sync().channel();
            System.out.println("xxxx");
        }


//        Channel channel= bootstrap.connect("192.168.0.13", 9090).sync().channel();

//        while(true){
//            if(!channel.isOpen()){
//                break;
//            }
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(8000);
//        }
//
//        if(!channel.isOpen()){
//            channel.close();
//            group.shutdownGracefully();
//        }


    }
}

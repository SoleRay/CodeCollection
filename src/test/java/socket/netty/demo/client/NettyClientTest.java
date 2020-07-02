package socket.netty.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyClientTest {

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ClientInboundHandler());
                        ch.pipeline().addLast(new ClientOutboundHandler());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).sync().channel();


        for(int i=0; i<3; i++){
//        for(;;){
//
//            String s = scanner.nextLine();
            channel.writeAndFlush(i+"");
//            channel.writeAndFlush(s);
        }
//        channel.closeFuture().sync();


    }

}

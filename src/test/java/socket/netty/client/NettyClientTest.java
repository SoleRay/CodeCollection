package socket.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
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

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

//        Thread.sleep(3000);
//        channel.writeAndFlush(new Date() + ": hello world!");

        for(;;){
            System.out.println("请输入");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            channel.writeAndFlush(s);
        }


    }

}

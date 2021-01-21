package socket.netty.demo.mine.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new InBoundHandlerB());
                    }
                });
        Channel channel= bootstrap.connect("127.0.0.1", 8000).sync().channel();

        while(true){
            if(!channel.isOpen()){
                break;
            }
            channel.writeAndFlush(new Date() + ": hello world!");
            Thread.sleep(8000);
        }

        if(!channel.isOpen()){
            channel.close();
            group.shutdownGracefully();
        }


    }
}

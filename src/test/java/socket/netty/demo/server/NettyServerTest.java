package socket.netty.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerTest {


    public static void main(String[] args) {

        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<NioServerSocketChannel>(){
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerChannelInboundHandler());
                        ch.pipeline().addLast(new ServerChannelOutboundHandler());
                    }
                })
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    protected void initChannel(NioSocketChannel ch) {

                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChildChannelInboundHandler());
                        ch.pipeline().addLast(new ChildChannelOutboundHandler());
                    }

                }).bind(8000);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }));

    }


}

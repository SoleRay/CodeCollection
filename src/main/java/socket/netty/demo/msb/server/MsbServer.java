package socket.netty.demo.msb.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class MsbServer {

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(1);

    private ChannelHandler childHandler;


    public void bind(int port){
        bind("127.0.0.1",port);
    }

    public void bind(String hostname,int port){
        try {
            NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();
            serverSocketChannel.pipeline().addLast(new AcceptHandler());
            bossGroup.next().register(serverSocketChannel);
            serverSocketChannel.bind(new InetSocketAddress(hostname,port)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void childHandler(ChannelHandler childHandler){
        this.childHandler = childHandler;
    }

    class AcceptHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            NioSocketChannel socketChannel = (NioSocketChannel) msg;

            socketChannel.pipeline().addLast(childHandler);

            workGroup.next().register(socketChannel);
            ctx.fireChannelRead(msg);
        }
    }
}

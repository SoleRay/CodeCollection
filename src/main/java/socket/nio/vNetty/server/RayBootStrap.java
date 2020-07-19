package socket.nio.vNetty.server;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.channel.RayServerSocketChannel;
import socket.nio.vNetty.group.RayEventLoopGroup;
import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class RayBootStrap {

    private SocketAddress localAddress;

    private RayEventLoopGroup bossGroup;

    private RayEventLoopGroup workGroup;



    public RayBootStrap(RayEventLoopGroup bossGroup, RayEventLoopGroup workGroup) {
        this.bossGroup = bossGroup;
        this.workGroup = workGroup;
    }

    public void bind(int port) throws IOException {
        bind(new InetSocketAddress("127.0.0.1",port));
    }

    public void bind(SocketAddress localAddress) throws IOException {
        this.localAddress = localAddress;
        initAndRegister();
    }

    private void initAndRegister() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(localAddress);
        RayServerSocketChannel rayServerSocketChannel = new RayServerSocketChannel(serverSocketChannel,SelectionKey.OP_ACCEPT);
        bossGroup.register(rayServerSocketChannel);

        rayServerSocketChannel.pipeline().addLast(new RayServerBootstrapAcceptor());
    }

    public class RayServerBootstrapAcceptor implements RayChannelHandler {

        @Override
        public void channelRead(RayAbstractChannelHandlerContext ctx, Object msg) {
            RayChannel channel = (RayChannel) msg;
            workGroup.register(channel);
        }
    }

    public static void main(String[] args) throws Exception {
        RayEventLoopGroup bossGroup = new RayEventLoopGroup(1,"AcceptorEventLoopGroup","AcceptorEventLoop");
        RayEventLoopGroup workGroup = new RayEventLoopGroup("DataEventLoopGroup","DataEventLoop");
        RayBootStrap bootStrap = new RayBootStrap(bossGroup,workGroup);
        bootStrap.bind(8000);
    }
}

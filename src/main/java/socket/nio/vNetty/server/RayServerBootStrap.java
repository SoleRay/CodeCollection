package socket.nio.vNetty.server;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.channel.RayServerSocketChannel;
import socket.nio.vNetty.customer.MyInBoundHandlerA;
import socket.nio.vNetty.customer.MyInBoundHandlerB;
import socket.nio.vNetty.customer.MyLogHandler;
import socket.nio.vNetty.group.RayEventLoopGroup;
import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandler;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandlerAdapter;
import socket.nio.vNetty.pipeline.handler.RayChannelInitializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class RayServerBootStrap {

    private SocketAddress localAddress;

    private RayEventLoopGroup bossGroup;

    private RayEventLoopGroup workGroup;

    private RayChannelHandler bossHandler;

    private RayChannelHandler workHandler;


    public RayServerBootStrap(RayEventLoopGroup bossGroup, RayEventLoopGroup workGroup) {
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
        rayServerSocketChannel.pipeline().addLast(bossHandler);

    }

    public class RayServerBootstrapAcceptor extends RayChannelInboundHandlerAdapter {

        @Override
        public void channelRead(RayChannelHandlerContext ctx, Object msg) {
            RayChannel channel = (RayChannel) msg;
            channel.pipeline().addLast(workHandler);
            workGroup.register(channel);
            ctx.fireChannelRead(msg);
        }
    }

    public void bossHandler(RayChannelHandler bossHandler) {
        this.bossHandler = bossHandler;
    }

    public void workHandler(RayChannelHandler workHandler) {
        this.workHandler = workHandler;
    }

    public static void main(String[] args) throws Exception {
        RayEventLoopGroup bossGroup = new RayEventLoopGroup(1,"AcceptorEventLoopGroup","AcceptorEventLoop");
        RayEventLoopGroup workGroup = new RayEventLoopGroup("DataEventLoopGroup","DataEventLoop");
        RayServerBootStrap bootStrap = new RayServerBootStrap(bossGroup,workGroup);

        bootStrap.bossHandler(new MyLogHandler());
        bootStrap.workHandler(new RayChannelInitializer() {

            @Override
            protected void initChannel(RayChannelHandlerContext ctx) {
                ctx.pipeline().addLast(new MyInBoundHandlerA(),new MyInBoundHandlerB());
            }
        });

        bootStrap.bind(8000);
    }


}

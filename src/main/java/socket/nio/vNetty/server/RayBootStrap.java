package socket.nio.vNetty.server;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.channel.RayServerSocketChannel;
import socket.nio.vNetty.group.RayEventLoopGroup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

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
    }

    public class RayServerBootstrapAcceptor{

        public void register(RayChannel channel){
            workGroup.register(channel);
        }
    }
}

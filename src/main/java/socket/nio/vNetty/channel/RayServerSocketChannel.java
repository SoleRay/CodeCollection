package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;
import socket.nio.vNetty.server.RayBootStrap;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class RayServerSocketChannel extends RayAbstractChannel {

    public RayServerSocketChannel(SelectableChannel channel, int ops) {
        super(channel,ops);
    }

    @Override
    public void read() throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) javaChannel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);
        RaySocketChannel raySocketChannel = new RaySocketChannel(socketChannel, SelectionKey.OP_READ);




//        RayBootStrap.RayServerBootstrapAcceptor acceptor = new RayBootStrap.RayServerBootstrapAcceptor();
    }

    @Override
    public void write() throws IOException {

    }
}

package socket.nio.v3;

import socket.nio.v3.handler.ReadableHandler;
import socket.nio.v3.reactor.AbstractReactor;
import socket.nio.v3.reactor.MainReactor;
import socket.nio.v3.reactor.SubReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServerV3 {

    public void start() throws Exception{
        ServerSocketChannel serverSocketChannel = initServer();
        MainReactor mainReactor = createMainReactor(serverSocketChannel);
        mainReactor.start();
    }

    private MainReactor createMainReactor(ServerSocketChannel serverSocketChannel) throws Exception {
        MainReactor mainReactor = new MainReactor();
        SelectionKey selectionKey = mainReactor.registerToSelector(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        mainReactor.initSubReactors();
        return mainReactor;
    }

    private ServerSocketChannel initServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8080));
        return serverSocketChannel;
    }

    public static void main(String[] args) throws Exception {
        NIOServerV3 v3 = new NIOServerV3();
        v3.start();
    }
}

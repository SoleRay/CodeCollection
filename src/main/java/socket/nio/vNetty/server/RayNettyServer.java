package socket.nio.vNetty.server;

import socket.nio.vNetty.channel.RaySocketChannel;
import socket.nio.vNetty.group.RayEventLoopGroup;
import socket.nio.vNetty.loop.RayEventLoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class RayNettyServer {

    public static void main(String[] args) throws Exception {

        Selector selector = SelectorProvider.provider().openSelector();

        initServer(selector);

        run(selector);
    }

    private static void run(Selector selector) throws Exception {
        RayEventLoopGroup eventLoopGroup = new RayEventLoopGroup();

        while(true){
            try {
                int connectNum = selector.select();
                System.out.println("系统接入的客户端数量："+connectNum);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(key.isAcceptable()){
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        RaySocketChannel raySocketChannel = new RaySocketChannel(socketChannel, SelectionKey.OP_READ);
                        eventLoopGroup.register(raySocketChannel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initServer(Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
}

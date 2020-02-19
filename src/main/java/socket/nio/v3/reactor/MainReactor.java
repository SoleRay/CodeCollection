package socket.nio.v3.reactor;

import socket.nio.v3.reactor.AbstractReactor;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainReactor extends AbstractReactor {

    private final AbstractReactor[] subReactors = new AbstractReactor[1];

    private final AtomicInteger count = new AtomicInteger(1);

    private final ExecutorService pool = Executors.newCachedThreadPool();

    public MainReactor() throws Exception {
        super();
    }

    public void initSubReactors() throws Exception {
        for(int i=0;i<subReactors.length;i++){
            SubReactor subReactor = new SubReactor();
            subReactor.registerPool(pool);
            subReactors[i] = subReactor;
        }
    }

    @Override
    protected void handle(SelectionKey key) throws Exception {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        new Acceptor(socketChannel).start();
    }

    class Acceptor {

        private SocketChannel socketChannel;

        public Acceptor(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void start() throws Exception {
            int index = count.getAndIncrement() % subReactors.length;
            AbstractReactor subReactor = subReactors[index];
            SelectionKey selectionKey = subReactor.registerToSelector(socketChannel);
            selectionKey.interestOps(SelectionKey.OP_READ);
            subReactor.start();
            System.out.println("收到新连接：" + socketChannel);
        }

    }
}

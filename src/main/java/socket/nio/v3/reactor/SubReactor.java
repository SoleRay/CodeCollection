package socket.nio.v3.reactor;

import socket.nio.v3.handler.ReadableHandler;
import socket.nio.v3.reactor.AbstractReactor;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SubReactor extends AbstractReactor {

    private ExecutorService pool;

    public SubReactor() throws Exception {
        super();
    }

    public void registerPool(ExecutorService pool){
        this.pool = pool;
    }

    @Override
    protected void handle(SelectionKey key) {
        /**必须设置一下，否则外层的循环能一直read，从而不断地调用handle方法*/
        key.interestOps(0);
        pool.execute(()->{
            new ReadableHandler().handle(key);
        });
    }
}

package socket.nio.v3.reactor;

import java.nio.channels.*;
import java.util.Iterator;

public abstract class AbstractReactor implements Runnable {

    private final Selector selector;

    private final Thread t;

    private volatile boolean isRunning;

    public AbstractReactor() throws Exception {
        t = new Thread(this);
        selector = Selector.open();
    }

    @Override
    public void run() {
        try{
            System.out.println("服务器已起动....");
            while(true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){

                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(key.isAcceptable() || key.isReadable()){
                        handle(key);

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start(){
        if(!isRunning){
            t.start();
            isRunning = true;
        }
    }

    public SelectionKey registerToSelector(SelectableChannel channel) throws Exception {
       return channel.register(selector,0);
    }

    protected abstract void handle(SelectionKey key) throws Exception;
}

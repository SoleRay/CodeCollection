package socket.nio.vNetty.loop;

import socket.nio.vNetty.channel.RayAbstractChannel;
import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.channel.RaySocketChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class RayEventLoop implements Runnable {

    private Selector selector;

    private Thread thread;

    private Queue<Runnable> queue;

    public RayEventLoop() throws Exception {
        selector = SelectorProvider.provider().openSelector();
        queue = new LinkedBlockingQueue<>();
        thread = new Thread(this);
        thread.start();
    }

    public void register(RayChannel channel)  {
        queue.add(()->{
            System.out.println("开始运行注册任务...");
            channel.register(this);

        });
        selector.wakeup();
    }

    @Override
    public void run() {
        while(!thread.isInterrupted()){
            try {
                System.out.println("正在查询IO事件....");
                int eventNum = selector.select();
                System.out.println("系统发生的IO事件数量："+eventNum);

                if(eventNum == 0){
                    runTasks();
                }else {
                    processSelectedKeys();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runTasks() {
        Runnable task;

        while ((task = queue.poll())!=null){
            task.run();
        }
    }

    private void processSelectedKeys() throws IOException {
        System.out.println("开始处理channel读写事件");

        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();

        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            RayAbstractChannel channel = (RayAbstractChannel) key.attachment();
            iterator.remove();

            if(key.isReadable()){
                channel.read();
                key.interestOps(SelectionKey.OP_WRITE);
            }

            if(key.isWritable()){
                channel.write();
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private int readData(SocketChannel channel, ByteBuffer buffer) throws IOException {
        int readNum ;
        do{
            readNum = channel.read(buffer);

            System.out.println("readNum:"+readNum);
            if(buffer.position()>0){
                break;
            }
        }while (channel.isOpen() && readNum!=-1);
        return readNum;
    }

    public Selector selector() {
        return selector;
    }
}

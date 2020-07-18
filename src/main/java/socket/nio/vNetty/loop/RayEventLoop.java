package socket.nio.vNetty.loop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import socket.nio.vNetty.channel.RayAbstractChannel;
import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.channel.RaySocketChannel;
import socket.nio.vNetty.group.RayEventLoopGroup;

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

    private RayEventLoopGroup eventLoopGroup;

    public RayEventLoop(RayEventLoopGroup eventLoopGroup) throws Exception {
        this.selector = SelectorProvider.provider().openSelector();
        this.eventLoopGroup = eventLoopGroup;
        this.queue = new LinkedBlockingQueue<>();
        this.thread = new Thread(this);
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
            RayChannel channel = (RayChannel) key.attachment();
            iterator.remove();

            if(key.isReadable() || key.isAcceptable()){
                channel.read();
            }

            if(key.isWritable()){
                channel.write();
            }
        }
    }

    public Selector selector() {
        return selector;
    }

    /**
     * 合并前（accept 和read write 分开之前的代码）
     * @throws IOException
     */
//    private void processSelectedKeys() throws IOException {
//        System.out.println("开始处理channel读写事件");
//
//        Set<SelectionKey> selectionKeys = selector.selectedKeys();
//        Iterator<SelectionKey> iterator = selectionKeys.iterator();
//
//        while (iterator.hasNext()){
//            SelectionKey key = iterator.next();
//            RayAbstractChannel channel = (RayAbstractChannel) key.attachment();
//            iterator.remove();
//
//            if(key.isReadable()){
//                channel.read();
//                key.interestOps(SelectionKey.OP_WRITE);
//            }
//
//            if(key.isWritable()){
//                channel.write();
//                key.interestOps(SelectionKey.OP_READ);
//            }
//        }
//    }


}

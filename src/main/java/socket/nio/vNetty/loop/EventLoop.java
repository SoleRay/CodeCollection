package socket.nio.vNetty.loop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop implements Runnable {

    private Selector selector;

    private Thread thread;

    private Queue<Runnable> queue;

    public EventLoop() throws Exception {
        selector = SelectorProvider.provider().openSelector();
        queue = new LinkedBlockingQueue<>();
        thread = new Thread(this);
        thread.start();
    }

    public void register(SocketChannel socketChannel,int ops)  {
        queue.add(()->{
            try {
                System.out.println("开始运行注册任务...");
                socketChannel.register(selector,ops);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
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
            SocketChannel channel = (SocketChannel)key.channel();

            iterator.remove();

            if(key.isReadable()){
                System.out.println("检测到key的可读事件....");

                ByteBuffer buffer = ByteBuffer.allocate(1024);

                int readNum = readData(channel, buffer);

                if(readNum==-1){
                    channel.close();
                }

                /**byteBuffer的position为0，说明写模式下指针从未移动过，说明没有读到数据，后续就不需要操作了*/
                if(buffer.position()==0){
                    return;
                }


                buffer.flip();
                byte[] bytes = new byte[readNum];
                buffer.get(bytes,0,readNum);
                System.out.println("收到消息：" + new String(bytes));

                key.attach(bytes);

                key.interestOps(SelectionKey.OP_WRITE);
            }

            if(key.isWritable()){
                System.out.println("检测到key的可写事件....");

                byte[] bytes = (byte[]) key.attachment();
                key.attach(null);

                System.out.println("可写事件发生，向客户端写入数据："+new String(bytes));

                if(bytes!=null){
                    channel.write(ByteBuffer.wrap(bytes));
                }


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
}

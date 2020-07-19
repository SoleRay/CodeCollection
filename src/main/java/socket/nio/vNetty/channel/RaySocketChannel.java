package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class RaySocketChannel extends RayAbstractChannel {

    public RaySocketChannel(SelectableChannel channel, int interestOp) {
        super(channel,interestOp);
    }

    @Override
    public void read() throws IOException{
        System.out.println(eventLoop.eventLoopName()+":检测到key的可读事件....");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) javaChannel();

        try {
            int readNum = readBytes(channel, buffer);

            if(readNum==-1){
                channel.close();
                return;
            }

            /**byteBuffer的position为0，说明写模式下指针从未移动过，说明没有读到数据，后续就不需要操作了*/
            if(buffer.position()==0){
                return;
            }
            /** 对消息进行处理 */
            pipeline().fireChannelRead(buffer);
//            processMsg(buffer);

            writeQueue.add(ByteBuffer.wrap("OK".getBytes()));

            selectionKey.interestOps(SelectionKey.OP_WRITE);

        }catch (IOException e){
            channel.close();
            selectionKey.cancel();
            throw e;
        }
    }

    private void processMsg(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes,0,buffer.limit());
        System.out.println(eventLoop.eventLoopName()+":收到消息：" + new String(bytes));
    }

    private int readBytes(SocketChannel channel, ByteBuffer buffer)  {
        int readNum ;

        do{
            try {
                readNum = channel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }

            System.out.println(eventLoop.eventLoopName()+":readNum:"+readNum);
            if(buffer.position()>0){
                break;
            }
        }while (channel.isOpen() && readNum!=-1);
        return readNum;
    }

    @Override
    public void write() throws IOException {
        System.out.println(eventLoop.eventLoopName()+":检测到key的可写事件....");

        SocketChannel channel = (SocketChannel) javaChannel();

        ByteBuffer buffer;

        while ((buffer=writeQueue.poll())!=null){
            try {
                channel.write(buffer);
            } catch (IOException e) {
                channel.close();
                throw e;
            }
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}

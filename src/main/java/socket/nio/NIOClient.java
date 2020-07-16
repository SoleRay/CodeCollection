package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class NIOClient {

    public void start() throws Exception{
        Selector selector = Selector.open();

        initClient(selector);

        run(selector);
    }

    private void initClient(Selector selector) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost",8000));
    }

    private void run(Selector selector) throws IOException {
        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                SocketChannel channel = (SocketChannel) key.channel();

                iterator.remove();

                if(key.isConnectable()){
                    handleConnectable(key, channel);
                }else if(key.isWritable()){
                    handleWriteable(key, channel);
                }else if(key.isReadable()){
                    handleReadable(channel);
                }
            }
        }
    }

    private void handleConnectable(SelectionKey key, SocketChannel channel) throws IOException {
        if(channel.finishConnect()){
            System.out.println("连接成功-" + channel);
            ByteBuffer byteBuffer = ByteBuffer.allocate(20480);
            key.attach(byteBuffer);
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void handleWriteable(SelectionKey key, SocketChannel channel) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.clear();
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("请输入:");
//        String msg = scanner.nextLine();
//        scanner.close();
        String msg = "aaaaa";
        byteBuffer.put(msg.getBytes("UTF-8"));
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            channel.write(byteBuffer);
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    private void handleReadable(SocketChannel channel) throws IOException {

        System.out.println("收到服务端响应:");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = 0;
        while(channel.isOpen() && (read = channel.read(byteBuffer))!=-1){

            if(byteBuffer.position()>0){
                break;
            }
        }
        if(read==-1){
            System.out.println("客户端尝试断开连接。。。。");
            channel.close();
        }

        if(byteBuffer.position()==0){
            return;
        }
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
    }

    public static void main(String[] args) throws Exception {
        NIOClient client = new NIOClient();
        client.start();
    }
}

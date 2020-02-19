package socket.nio.v1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServerV1 {

    public void start() throws Exception{
        Selector selector = Selector.open();

        initServer(selector);

        run(selector);
    }

    private void initServer(Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void run(Selector selector) throws IOException {
        System.out.println("服务器已起动....");
        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){

                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isAcceptable()){
                    handleAcceptable(selector, key);
                }else if(key.isReadable()){
                    handleReadable(key);
                }
            }
        }
    }

    private void handleAcceptable(Selector selector, SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        System.out.println("收到新连接：" + socketChannel);
    }

    private void handleReadable(SelectionKey key) {
        try{
//            Thread.sleep(30000);
            SocketChannel socketChannel = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            /**channel读取到的数据会写入到ByteBuffer中，所以ByteBuffer此时是写模式*/
            int read = readData(socketChannel, byteBuffer);

            if(read==-1){
                socketChannel.close();
            }
            /**byteBuffer的position为0，说明写模式下指针从未移动过，说明没有读到数据，后续就不需要操作了*/
            if(byteBuffer.position()==0){
                return;
            }
            /**从写模式转化为读模式*/
            byteBuffer.flip();

            /**对读取的数据进行业务处理*/
            doBussiness(socketChannel,byteBuffer);

            /**给客户端回应*/
            doResponse(socketChannel);
        }catch (Exception e){
            e.printStackTrace();
            key.channel();
        }
    }

    private int readData(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        int read=0;
        while(socketChannel.isOpen() && (read=socketChannel.read(byteBuffer))!=-1){

            if(byteBuffer.position()>0){
                break;
            }
        }
        return read;
    }

    private void doBussiness(SocketChannel socketChannel, ByteBuffer byteBuffer) throws Exception {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String content = new String(bytes,"UTF-8");
        System.out.println("读取到的数据:" + content);
        System.out.println("数据来自:"+socketChannel.getRemoteAddress());
    }

    private void doResponse(SocketChannel socketChannel) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 11\r\n\r\n" +
                "Hello World";
        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes("UTF-8"));
        while (responseBuffer.hasRemaining()){
            socketChannel.write(responseBuffer);
        }
    }


    public static void main(String[] args) throws Exception {
        NIOServerV1 v1 = new NIOServerV1();
        v1.start();
    }
}

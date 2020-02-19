package socket.nio.v2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServerV2 {

    SubReactor[] subReactors = new SubReactor[1];

    AtomicInteger count = new AtomicInteger(1);

    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void start() throws Exception{
        ServerSocketChannel serverSocketChannel = initServer();
        MainReactor mainReactor = initMainReactor(serverSocketChannel);
        initSubReactors();
        mainReactor.start();
    }

    private MainReactor initMainReactor(ServerSocketChannel serverSocketChannel) throws Exception {
        MainReactor mainReactor = new MainReactor();
        mainReactor.register(serverSocketChannel);
        return mainReactor;
    }

    private void initSubReactors() throws Exception {
        for(int i=0;i<subReactors.length;i++){
            subReactors[i] = new SubReactor();
        }
    }

    private ServerSocketChannel initServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8080));
        return serverSocketChannel;
    }

    class MainReactor implements Runnable {

        private final Selector selector;

        private final Thread t;

        public MainReactor() throws Exception{
            t = new Thread(this);
            selector = Selector.open();
        }

        public void start(){
            t.start();
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

                        if(key.isAcceptable()){
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            socketChannel.configureBlocking(false);
                            new Acceptor(socketChannel);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void register(ServerSocketChannel serverSocketChannel) throws Exception {
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        }
    }

    class Acceptor {

        public Acceptor(SocketChannel socketChannel) throws Exception{
            int index = count.getAndIncrement() % subReactors.length;
            SubReactor subReactor = subReactors[index];
            subReactor.register(socketChannel);
            subReactor.start();
            System.out.println("收到新连接：" + socketChannel);
        }

    }

    class SubReactor implements Runnable {

        private final Selector selector;

        private final Thread t;

        private volatile boolean isRunning;

        public SubReactor() throws Exception {
            this.selector = Selector.open();
            this.t = new Thread(this);
        }

        public void start(){
            if(!isRunning){
                t.start();
                isRunning = true;
            }
        }

        @Override
        public void run() {
            try{
                while(true){
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){

                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if(key.isReadable()){
                            key.interestOps(0);
                            pool.execute(()->{
                                handleReadable(key);
                            });

                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void register(SocketChannel socketChannel) throws Exception {
            socketChannel.register(selector,SelectionKey.OP_READ);
        }

        private void handleReadable(SelectionKey key) {
            try{

                SocketChannel socketChannel = (SocketChannel) key.channel();
                System.out.println("channel:"+socketChannel.getRemoteAddress()+" 开始写。。。");
                Thread.sleep(10000);
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
    }

    public static void main(String[] args) throws Exception {
        NIOServerV2 v1 = new NIOServerV2();
        v1.start();
    }
}

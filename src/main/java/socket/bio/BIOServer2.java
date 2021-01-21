package socket.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer2 {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    private List<Socket> socketList = new ArrayList<>();

    private ServerSocket serverSocket;

    public BIOServer2() {

    }

    public void bind() {
        bind(8080);
    }

    public void startServer() {
        new Thread(new Acceptor()).start();

        new Thread(new Poller()).start();
    }

    public void bind(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    socketList.add(socket);
                    System.out.println("接受到客户端："+socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class Poller implements Runnable {

        @Override
        public void run()  {
            while (true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("遍历socket....");
                for (Socket socket : socketList) {
                    threadPool.execute(()->process(socket));
                }
            }

        }

        private void process(Socket socket) {
            try {
                // 接收数据、打印
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String msg;
                while ((msg = reader.readLine()) != null) {
                    if (msg.length() == 0) {
                        break;
                    }
                    System.out.println(msg);
                }

                System.out.println("收到数据,来自："+ socket.toString());
                // 响应结果 200
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                outputStream.write("Content-Length: 11\r\n\r\n".getBytes());
                outputStream.write("Hello World".getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        BIOServer2 server2 = new BIOServer2();
        server2.bind();
        server2.startServer();
    }
}

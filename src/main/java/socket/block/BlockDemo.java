package socket.block;

import common.io.FileUtil;

import java.io.*;
import java.util.concurrent.locks.LockSupport;

public class BlockDemo {

    public static void main(String[] args) {


        String resource = "1.txt";

        Runnable runnable = () -> {
            InputStream is = null;
            try {
                is = FileUtil.getFileStreamUnderResources("1.txt");
//                LockSupport.park();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }
}

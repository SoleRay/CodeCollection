package pool;

import concurrent.pool.MyThreadPool;

import java.util.concurrent.*;

public class PoolTest {

    public static void main(String[] args) throws InterruptedException {

//        testPool2();
        testPool();
//        int COUNT_BITS = Integer.SIZE - 3;
//        int COUNT_MASK = (1 << COUNT_BITS) - 1;
//        System.out.println(Integer.toHexString(COUNT_MASK));
//        System.out.println(Integer.toHexString(~COUNT_MASK));
//
//
//        int RUNNING    = -1 << COUNT_BITS;
//        int SHUTDOWN   =  0 << COUNT_BITS;
//        int STOP       =  1 << COUNT_BITS;
//        int TIDYING    =  2 << COUNT_BITS;
//        int TERMINATED =  3 << COUNT_BITS;
//
//        System.out.println(RUNNING);
//        System.out.println(SHUTDOWN);
//        System.out.println(STOP);
//        System.out.println(TIDYING);
//        System.out.println(TERMINATED);
//
//        System.out.println(Integer.toHexString(RUNNING));
//        System.out.println(Integer.toHexString(SHUTDOWN));
//        System.out.println(Integer.toHexString(STOP));
//        System.out.println(Integer.toHexString(TIDYING));
//        System.out.println(Integer.toHexString(TERMINATED));
//        System.out.println(Integer.toHexString(RUNNING & COUNT_MASK));


    }

    private static void testPool2() throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,4,1000,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());

//        pool.allowCoreThreadTimeOut(true);
//        System.out.println(pool.allowsCoreThreadTimeOut());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hello");
            }
        };

        Runnable test = new Runnable() {
            @Override
            public void run() {
                pool.execute(r);
            }
        };

        for(int i=0;i<6;i++){
            Thread t = new Thread(test);
            t.start();
        }

        Thread.sleep(3000);
        pool.shutdown();


//        Thread.sleep(5000);
//        pool.shutdownNow();
    }

    private static void testPool() throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(2, 6);

        Runnable r = new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println(Thread.currentThread().getName()+ " hello");
            }
        };


        Runnable test = new Runnable() {
            @Override
            public void run() {
                pool.execute(r);
            }
        };

        for(int i=0;i<6;i++){
            Thread t = new Thread(test);
            t.start();
        }

        Thread.sleep(1000);
//        pool.shutDownNow();
        pool.shutDown();
    }
}

package concurrent.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchThreadTest {


    public static void main(String[] args)  {

        ExecutorService pool = Executors.newCachedThreadPool();

        int threadCount = 1000;
        int splitCount = 500;

        CountDownLatch beginLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        //启500个线程，做事情A
        for(int i=0;i<splitCount;i++){
            pool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        beginLatch.await();
                        System.out.println("aaaaa");
                        endLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //再启500个线程，做事情B
        for(int i=0;i<splitCount;i++){
            pool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        beginLatch.await();
                        System.out.println("bbbbb");
                        endLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //让1000个线程同时启动，确保测试的正确性
        beginLatch.countDown();

        try {

            //等1000个线程同时执行完，最后统计结果的正确性
            endLatch.await();

            System.out.println("aaaaaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(new MyRunnable());
        executorService.submit(new MyRunnable());
        executorService.submit(new MyRunnable());

        Thread.sleep(5000);
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            LockSupport.park();
            System.out.println(Thread.currentThread().getId());
        }
    }
}

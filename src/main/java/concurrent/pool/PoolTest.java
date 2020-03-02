package concurrent.pool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class PoolTest {

    @Test
    public void testCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(111);
            }
        });
    }

    @Test
    public void testkeepAliveTime(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 10,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5));

        pool.allowCoreThreadTimeOut(true);

        pool.execute(r);
        pool.execute(r);
        pool.execute(r);
        pool.execute(r);
        pool.execute(r);
    }
}

package timer;

import concurrent.pool.source.SourceThreadPoolExecutor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerTaskTest {

    public static void main(String[] args) {
        Timer timer = new Timer();

        SourceThreadPoolExecutor poolExecutor = new SourceThreadPoolExecutor(16, 32, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(32));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 43; i++) {
                    poolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {

                            }
                        }
                    });
                }
                System.out.println(poolExecutor);
            }
        }, 1000L,10000L);
    }
}

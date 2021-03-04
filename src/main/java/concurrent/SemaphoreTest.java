package concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) throws Exception {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            Thread t = new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hello");
            });
            t.start();


        }

        Thread.sleep(3000);
        semaphore.release();
    }
}

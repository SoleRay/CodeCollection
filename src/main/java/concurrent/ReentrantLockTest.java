package concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(() -> {
                reentrantLock.lock();
                System.out.println("hello");
            });
            t.start();


        }
    }
}

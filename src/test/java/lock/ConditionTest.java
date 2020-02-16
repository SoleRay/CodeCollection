package lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void test() throws Exception{
        Runnable r =()->{
            lock.lock();

            try {
                condition.await();
                System.out.println(Thread.currentThread().getId()+ " next...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        };

        Runnable r2 =()->{
            lock.lock();

            try {
                condition.signal();
                System.out.println(Thread.currentThread().getId()+ " next...");
            }  finally {
                lock.unlock();
            }
        };
        Thread t = new Thread(r);
        Thread t2 = new Thread(r2);
        t.start();
        Thread.sleep(5000);
        t2.start();


    }

    public static void main(String[] args) throws Exception {
        ConditionTest test = new ConditionTest();
        test.test();
    }
}

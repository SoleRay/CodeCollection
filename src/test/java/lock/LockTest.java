package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        testCondition(lock);

//        testLock(lock);

//        long nanos = c.awaitNanos(10000);
//        System.out.println(nanos);
//        lock.unlock();
    }

    private static void testCondition(ReentrantLock lock) throws InterruptedException {
        Condition c = lock.newCondition();
        Runnable r1 =()->{
            lock.lock();
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }


        };

        Runnable r2 =()->{
            lock.lock();
            c.signal();
            lock.unlock();
        };
        Thread t1 = new Thread(r1,"t1");
        Thread t2 = new Thread(r2,"t2");
        t1.start();

        Thread.sleep(1000);
        t2.start();

    }

    private static void testLock(ReentrantLock lock) {
        Runnable r3 =()->{
            LockSupport.park();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();

        };
        Thread t3 = new Thread(r3,"t3");


        Runnable r2 =()->{
            LockSupport.park();
            lock.lock();

        };
        Thread t2 = new Thread(r2,"t2");


        Runnable r = ()->{
            lock.lock();
            LockSupport.unpark(t2);
            LockSupport.unpark(t3);
//            lock.unlock();
        };
        Thread t1 = new Thread(r,"t1");


        t1.start();
        t2.start();
        t3.start();
    }
}

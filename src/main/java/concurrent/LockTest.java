package concurrent;

import org.apache.poi.ss.formula.functions.Count;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {

    private static ReentrantLock lock = new ReentrantLock(true);

    private static ReadWriteLock rwlock = new ReentrantReadWriteLock();

    private static Lock r = rwlock.readLock();

    private static Lock w = rwlock.writeLock();

    private static CountDownLatch c = new CountDownLatch(1);


    private static class ReadRun implements Runnable{

        @Override
        public void run() {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            r.lock();

            try {
                System.out.println(Thread.currentThread().getName()+"——"+Thread.currentThread().getId()+"——11111");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName()+"——"+Thread.currentThread().getId()+"——22222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                r.unlock();
            }
        }
    }

    private static class WriteRun implements Runnable{

        @Override
        public void run() {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            w.lock();

            try{
                System.out.println(Thread.currentThread().getName()+"——"+Thread.currentThread().getId()+"——33333");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName()+"——"+Thread.currentThread().getId()+"——44444");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                w.unlock();
            }
        }
    }

    public static void main(String[] args) {

//        Thread r1 = new Thread(new ReadRun());
//        Thread r2 = new Thread(new ReadRun());
//        Thread r3 = new Thread(new ReadRun());
////        Thread wt = new Thread(new WriteRun());
//
//        r1.start();
//        r2.start();
//        r3.start();
////        wt.start();
//
//        c.countDown();

        Runnable r = ()->{
            lock.lock();
            try{
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                lock.unlock();
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);

        t1.start();
        t2.start();
    }
}

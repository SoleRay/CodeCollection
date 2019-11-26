package pool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {

    public static void main(String[] args) {

//        testWaitAndSleep();
//        testLockSupport();

        testCondition();

//        LockSupport.parkNanos(1000000000);
//        System.out.println("box");
//        System.out.println(2|0);
//        System.out.println(2|1);
    }

    private static void testCondition() {
        Runnable r = new Runnable() {

            private ReentrantLock lock = new ReentrantLock();

            private Condition c = lock.newCondition();

            @Override
            public void run() {
                lock.lock();
                System.out.println("hello");
                try {
                    System.out.println("box");
                    try {
                        c.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }finally {
                    lock.unlock();
                }
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();

    }

    private static void testLockSupport() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
                LockSupport.park();
                System.out.println("nice to meet you!");
            }
        };
        Thread t = new Thread(r);
        t.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t);
        System.out.println("over");
    }


    public void hello(){
        this.upPark();

    }

    public void upPark(){
        LockSupport.unpark(Thread.currentThread());
    }




    /**
     * sleep 不会释放锁
     * wait  会释放锁
     */
    private static void testWaitAndSleep() {
        Object o = new Object();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    System.out.println("hello");
                    try {
                        o.wait();
                        //Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
    }
}

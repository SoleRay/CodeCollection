package concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ThreadPrint {

    public static void main(String[] args) {
//        AtomicInteger atomicInteger = new AtomicInteger(1);
//
//        ReentrantLock lock = new ReentrantLock();
//
//        Condition condition = lock.newCondition();
//
//        Runnable runnable =  ()->{
//            IntStream.rangeClosed(1,50).forEach(i->{
//                lock.lock();
//                try {
//                    System.out.println(Thread.currentThread().getId()+":"+atomicInteger.getAndIncrement());
//                    condition.signal();
//                    condition.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    lock.unlock();
//                }
//            });
//        };
//
//        Thread t1 = new Thread(runnable);
//        Thread t2 = new Thread(runnable);
//        t1.start();
//        t2.start();
//
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(isOdd(8));

    }


    public static boolean isOdd(int i) {
        if(i==0){
            throw new IllegalArgumentException("0 不是奇数，也不是偶数");
        }
        return (i&1)==0?false:true;
    }
}

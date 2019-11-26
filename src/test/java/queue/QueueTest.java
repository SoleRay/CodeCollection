package queue;

import concurrent.queue.DelayBean;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class QueueTest {


    public static void main(String[] args) throws InterruptedException {

//        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
//        queue.offer("str");
//        queue.offer("str");
//        queue.offer("str");
//        queue.poll();
//        queue.poll();
//        queue.offer("str");
//        queue.poll();

        testSynchronousQueue();

//        testDelayQueue();

    }

    private static void testDelayQueue() throws InterruptedException {


        DelayQueue<DelayBean<String>> queue = new DelayQueue<>();

        Runnable cr2 = () -> {

            LockSupport.park();

            try {
                Thread.sleep(2000);
                DelayBean<String> bean = queue.take();
                System.out.println("Thread C2:" + bean.getData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread c2 = new Thread(cr2);

        Runnable cr = () -> {

            LockSupport.park();
            long t1 = System.currentTimeMillis();

            try {
                DelayBean<String> bean = queue.take();
                System.out.println("Thread C1:" + bean.getData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long t2 = System.currentTimeMillis();

            long diff = t2 - t1;

            System.out.println("take cost " + TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS));

        };


        Thread c = new Thread(cr);


        Runnable pr = () -> {
            DelayBean<String> d1 = new DelayBean<>("1", "bob", "east", 5000);
            queue.put(d1);
            LockSupport.unpark(c);
            LockSupport.unpark(c2);
        };


        Thread p = new Thread(pr);


        p.start();
        c.start();
        c2.start();
    }


    private static void testSynchronousQueue() throws InterruptedException {
        SynchronousQueue queue = new SynchronousQueue<String>();

        Runnable pr1 = () -> {
            try {
                System.out.println("pr1...");
                queue.put("str");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Runnable pr2 = () -> {

            try {
                System.out.println("pr2...");
                queue.put("cbd");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Runnable cr = () -> {

            try {
                LockSupport.park();
                Object obj = queue.take();
                System.out.println(obj);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Thread p1 = new Thread(pr1);
        Thread p2 = new Thread(pr2);
        Thread c = new Thread(cr);
        p1.start();
        p2.start();
        c.start();

        Thread.sleep(5000);
        LockSupport.unpark(c);
//        LockSupport.unpark(p2);
//        Thread.sleep(5000);
//        LockSupport.unpark(p1);

    }
}

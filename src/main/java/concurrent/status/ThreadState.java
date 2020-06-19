package concurrent.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadState {

    public static void main(String[] args) throws InterruptedException {
        Thread current = Thread.currentThread();

        Object o = new Object();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //状态二
                System.out.println("线程状态二："+Thread.currentThread().getState());

                //状态三：被sleep阻塞：timed_waitting
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //状态四：被synchronized的锁阻塞：blocked
                synchronized (o){
                    //do nothing
                }

                //状态五：被LockSupport阻塞：waitting
                LockSupport.park();


            }
        });

        System.out.println("线程状态一："+thread.getState());
        thread.start();

        /** 主线程停顿1秒，确保让子线程开始跑， 子线程输出状态二，此时为子线程run时的状态 */
        Thread.sleep(1000);

        /** 1秒后，子线程停顿5秒，主线程继续输出状态三，此时为子线程sleep时的状态 */
        System.out.println("线程状态三："+thread.getState());


        /** 必须是synchronized，如果使用current的lock，其效果等同于LockSupport.park */
        synchronized (o){
            /** 主线程停顿5秒，子线程开始跑，主线程设置5秒，确保让子线程结束sleep，然后被锁阻塞 */
            Thread.sleep(5000);

            /** 主线程结束睡眠，主线程继续输出状态四，此时为子线程被锁阻塞时的状态 */
            System.out.println("线程状态四："+thread.getState());
        }

        /** 主线程解锁以后，子线程未必马上跑，所以主线程再次停顿1秒，让子线程开始跑 */
        Thread.sleep(1000);

        /** 主线程1秒的停顿时间，足以让子线程进入LockSupport.park()，此时回来，输出该状态 */
        System.out.println("线程状态五："+thread.getState());

        /** 主线程upark掉子线程，让子线程不再阻塞，停顿1秒，确保子线程继续运行直至结束生命周期。 */
        LockSupport.unpark(thread);
        Thread.sleep(1000);
        System.out.println("线程状态六："+thread.getState());
    }
}

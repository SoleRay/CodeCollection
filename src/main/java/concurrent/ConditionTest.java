package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(() -> {
                try{
                    reentrantLock.lock();
                    System.out.println("before await");
                    condition.await();
                    System.out.println("signaled!!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }


            });
            t.start();

            Thread.sleep(20000);
            reentrantLock.lock();
            condition.signal();
            reentrantLock.unlock();


        }
    }
}

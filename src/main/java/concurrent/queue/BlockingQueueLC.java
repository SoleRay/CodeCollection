package concurrent.queue;

import org.apache.poi.ss.formula.functions.T;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueLC<T> {

    private LinkedList<T> list = new LinkedList<>();

    private int limit;

    private Lock lock = new ReentrantLock();

    private Condition putCondition = lock.newCondition();

    private Condition takeCondition = lock.newCondition();

    public BlockingQueueLC(int limit) throws Exception {
        if (limit <= 0) {
            throw new Exception("min length must over zero!!!");
        }
        this.limit = limit;
    }

    public void put(T t) throws InterruptedException {
        lock.lock();

        try{
            while (list.size() >= limit) {
                System.out.println("queue reaches to max,wait....");
                putCondition.await();
            }

            if (list.size() == 0) {
                takeCondition.signal();
            }

            list.add(t);
            System.out.println("added one element, now queue has " + list.size() + "element..");

        }finally {
            lock.unlock();
        }



    }

    public T take() throws InterruptedException {

        lock.lock();

        try{
            while (list.size() == 0) {
                System.out.println("queue is empty,wait....");
                takeCondition.await();
            }

            if (list.size() == limit) {
                putCondition.signal();
            }

            T t = list.remove(0);
            System.out.println("remove one element, now queue has " + list.size() + "element..");
            return t;
        }finally {
            lock.unlock();
        }


    }
}

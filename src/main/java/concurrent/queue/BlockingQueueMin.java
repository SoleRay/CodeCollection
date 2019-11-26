package concurrent.queue;

import java.util.LinkedList;;

public class BlockingQueueMin<T> {

    private LinkedList<T> list = new LinkedList<>();

    private int limit;

    public BlockingQueueMin(int limit) throws Exception {
        if (limit <= 0) {
            throw new Exception("min length must over zero!!!");
        }
        this.limit = limit;
    }

    public synchronized void put(T t) throws InterruptedException {
        while (list.size() >= limit) {
            System.out.println("queue reaches to max,wait....");
            wait();
        }

        if (list.size() == 0) {
            notifyAll();
        }

        list.add(t);
        System.out.println("added one element, now queue has " + list.size() + "element..");


    }

    public synchronized T take() throws InterruptedException {
        while (list.size() == 0) {
            System.out.println("queue is empty,wait....");
            wait();
        }

        if (list.size() == limit) {
            notifyAll();
        }

        T t = list.remove(0);
        System.out.println("remove one element, now queue has " + list.size() + "element..");
        return t;
    }


}

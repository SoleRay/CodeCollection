package concurrent.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

public class DelayedQueueTest {

    public static void main(String[] args) throws Exception{
        DelayQueue<DelayBean> queue = new DelayQueue<>();

        DelayBean<String> bean1 = new DelayBean<>("1","mike","hello",4000);
        DelayBean<String> bean2 = new DelayBean<>("2","john","hello",3000);

        queue.put(bean1);
        queue.put(bean2);


        Thread.sleep(5000);
        DelayBean bean = queue.poll();
        System.out.println(bean.getId());
    }
}

package lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    private final CyclicBarrier barrier = new CyclicBarrier(3);


    public void test() throws Exception{
        Runnable r =()->{

            try {
                System.out.println(Thread.currentThread().getName()+" ready...");
                barrier.await();
                System.out.println(Thread.currentThread().getName()+" tripped...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        for(int i=0;i<6;i++){
            Thread t = new Thread(r);
            t.setName(String.valueOf(i));
            t.start();
        }



    }


    public static void main(String[] args) throws Exception {
        CyclicBarrierTest c = new CyclicBarrierTest();
        c.test();
    }
}

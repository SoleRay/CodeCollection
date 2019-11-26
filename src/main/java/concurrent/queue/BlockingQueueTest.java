package concurrent.queue;


import java.util.Random;

public class BlockingQueueTest {

    public static void main(String[] args) throws Exception {
//        BlockingQueueMin<String> queue = new BlockingQueueMin(5);

        BlockingQueueLC<String> queue = new BlockingQueueLC(5);

        Thread consumer = new Thread(new Runnable(){

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    String str = null;
                    try {
                        str = queue.take();
                    } catch (InterruptedException e) {
                        System.out.println("interrupted...");
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(str);
                }
            }
        });

        Thread producer = new Thread(new Runnable(){

            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    try {
                        queue.put(String.valueOf(new Random().nextInt()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                consumer.interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}

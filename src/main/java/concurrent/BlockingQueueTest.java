package concurrent;


import java.util.Random;

public class BlockingQueueTest {

    public static void main(String[] args) throws Exception {
        BlockingQueueMin<String> queue = new BlockingQueueMin(5);

        Thread consumer = new Thread(new Runnable(){

            @Override
            public void run() {
                while (true){
                    String str = queue.take();
                    System.out.println(str);
                }
            }
        });

        Thread producer = new Thread(new Runnable(){

            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    queue.put(String.valueOf(new Random().nextInt()));
                }
            }
        });

        producer.start();
        consumer.start();
    }
}

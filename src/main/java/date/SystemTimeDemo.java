package date;

import org.junit.jupiter.api.Test;

public class SystemTimeDemo {

    @Test
    public void run() throws Exception {

        long startTime = System.nanoTime();

        System.out.println(startTime);

        while (true){

            Thread.sleep(3000);

            long currentTime = System.nanoTime();

            System.out.println("after 3 second:"+currentTime);
            System.out.println(currentTime - startTime);
        }

    }
}

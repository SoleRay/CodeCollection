package timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskTest {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("aaaa");
            }
        }, 1000L);
    }
}

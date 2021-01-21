package sched;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new MyTask(),5000,10000);
        timer.schedule(new MyTask(),6000,2000);
        timer.schedule(new MyTask(),4000,500);
    }

    static class MyTask extends TimerTask{
        @Override
        public void run() {
            System.out.println("hello....");
        }
    }
}

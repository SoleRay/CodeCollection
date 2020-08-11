package timer;

public class RayTimerTaskTest {

    public static void main(String[] args) throws Exception {
        RayTimer timer = new RayTimer();
        timer.schedule(new MyTimerTask("one"),7000,10000);

        Thread.sleep(500);
        timer.schedule(new MyTimerTask("two"),5000,15000);

        Thread.sleep(500);
        timer.schedule(new MyTimerTask("three"),4000,15000);
//        timer.schedule(new MyTimerTask("three"),6000,20000);
    }

    static class MyTimerTask extends RayTimerTask{

        public MyTimerTask(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"——"+getName()+":hello");
        }
    }
}

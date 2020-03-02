package concurrent.base;

public class ThreadStop {

    public void doStop(){

        Thread thread = new Thread(){

            @Override
            public void run() {
                while(Thread.currentThread().isInterrupted()){

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //需要重新调用一次，因为发生InterruptedException异常时，isInterrupted=true的状态被擦除，重新变成了isInterrupted=false
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        };

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();

    }
}

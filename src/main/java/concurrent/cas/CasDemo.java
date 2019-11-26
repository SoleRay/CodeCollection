package concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class CasDemo {

    private final AtomicInteger ctl = new AtomicInteger(0);


    public void execute(){
        for(;;){
            int c = ctl.get();

            /**正常情况下，顺利break，并发冲突时，冲突的线程将再次进入循环*/
            if (ctl.compareAndSet(c,c+1)){
                break;
            }
        }
    }
}

package timer;

import date.LocalDateUtil;
import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RayTimer {

    private ExecutorService executorService;

    private RayTimeThread thread = new RayTimeThread();

    private RayTaskQueue taskQueue = new RayTaskQueue();

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public RayTimer() {
        this(Executors.newFixedThreadPool(4));
    }

    public RayTimer(ExecutorService executorService) {
        this.executorService = executorService;
        thread.start();
    }

    public void schedule(RayTimerTask task, long delay, long period) {
        try{
            lock.lock();
            task.nextExecuteTime = System.currentTimeMillis() + delay;
            task.period = period;
            taskQueue.add(task);

            //多么优雅的代码！！！
            if(taskQueue.getMin() == task){
                condition.signalAll();
            }
        }finally {
            lock.unlock();
        }

    }

    class RayTimeThread implements Runnable {

        private Thread thread;

        public RayTimeThread() {
            thread = new Thread(this);
        }

        @Override
        public void run() {
            while (true) {
                try{
                    lock.lock();
                    while (taskQueue.isEmpty()){
                        condition.await();
                    }
                    RayTimerTask runTask = taskQueue.getMin();

                    watchTime(runTask);

                    if(runTask.nextExecuteTime <= System.currentTimeMillis()){
                        System.out.println("execute");

                        if(runTask.period!=0){
                            long newTime =  runTask.nextExecuteTime + runTask.period;
                            taskQueue.reschedule(newTime);
                        }else {
                            taskQueue.removeMin();
                        }
                        executorService.execute(runTask);
                    }else {
                        System.out.println("wait");
                        condition.await(runTask.nextExecuteTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }


            }
        }

        public void start(){
            thread.start();
        }
    }

    private void watchTime(RayTimerTask runTask) {
        System.out.print(Thread.currentThread().getName()+"——"+runTask.getName()+",");
        LocalTime localTime = LocalDateUtil.convertToLocalTime(runTask.nextExecuteTime);
        LocalTime currentTime = LocalDateUtil.convertToLocalTime(System.currentTimeMillis());
        System.out.print("nextExecuteTime:"+localTime.getHour()+":"+localTime.getMinute()+":"+localTime.getSecond()+",");
        System.out.print("currentTime:"+currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond()+"——");
    }


}

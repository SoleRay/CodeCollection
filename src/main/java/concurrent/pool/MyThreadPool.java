package concurrent.pool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool {

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(4);

    private Set<Worker> workers = new HashSet<>();

    /**
     * ctl的前4位是runstate，后28位是workerCount
     *
     * 所以这个综合值的默认初始状态是：RUNNING,0个线程
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING,0));

    private static final int COUNT_BITS = Integer.SIZE - 3; //29
    private static final int COUNT_MASK = (1 << COUNT_BITS ) -1; //1fff,ffff

    private static final int RUNNING    = -1 << COUNT_BITS;//e000,0000
    private static final int SHUTDOWN   =  0 << COUNT_BITS;//0
    private static final int STOP       =  1 << COUNT_BITS;//2000,0000
    private static final int TIDYING    =  2 << COUNT_BITS;//4000,0000
    private static final int TERMINATED =  3 << COUNT_BITS;//6000,0000

    private final ReentrantLock mainLock = new ReentrantLock();


    private static int runStateOf(int c){
        return c & ~COUNT_MASK;
    }

    private static int workerCountOf(int c){
        return c & COUNT_MASK;
    }

    /**
     *
     * @param rs = runState
     * @param wc = workerCount
     * @return 返回综合值 ctl
     */
    private static int ctlOf(int rs ,int wc){
        return rs | wc;
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    private boolean remove(Runnable task){
        return queue.remove(task);
    }


    private int corePoolSize;

    private int maxPoolSize;

    public MyThreadPool(int corePoolSize, int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    private final class Worker implements Runnable {

        private Thread t;

        private Runnable task;

        public Worker(Runnable firstTask) {
            task = firstTask;
            t = new Thread(this);
        }

        @Override
        public void run() {
            try {
                while (task != null || (task = getTask()) != null) {

                    try {
                        task.run();
                    } finally {
                        task = null;
                    }
                }
            } finally {
                workers.remove(this);
            }

        }

        public void start() {
            t.start();
        }

        public boolean isAlive(){
            return t.isAlive();
        }
    }

    private Runnable getTask() {
        try {

            Runnable task = queue.poll(300, TimeUnit.MILLISECONDS);
            return task;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void execute(Runnable task) {
        if(task == null){
            throw new NullPointerException();
        }

        int c = ctl.get();

        if (workerCountOf(c) < corePoolSize) {
            addWorker(task,true);
            System.out.println("结束前："+workerCountOf(ctl.get()));
            return;
        }

        System.out.println("线程池中的正在运行的线程已经达到最大值，任务将进入队列。。。");

        //如果线程数目已经超过核心线程数，但任务还可以加入队列的话，那么什么都不做
        if (isRunning(c) && queue.offer(task)) {
            int recheck = ctl.get();
            if(!isRunning(recheck) && remove(task)){
                rejectTask();
            }else if(workerCountOf(recheck) == 0){
                //TODO
            }
        }else if(!addWorker(task,false)){
            rejectTask();
        }
    }

    private void rejectTask() {
        throw new RuntimeException("当前运行的线程数已经达到最大值，无法再接受新的任务");
    }

    //其实这个锁，真正想要锁的，是把work放入set这一步 
    private boolean addWorker(Runnable task, boolean core) {

        int c = ctl.get();

        while(true){

            //如果当前worker的数量超过了核心或者最大线程数（取决于core的值）
            if(workerCountOf(c) >= workerCountOf((core?corePoolSize:maxPoolSize))){
                System.out.println("超过了核心或者最大线程数");
                return false;
            }

            /**
             * 计算数量，就是通过这个方法来控制并发的
             * 比如说，核心线程数目是1，如果有两个人同时调用了execute方法来到这里，
             * 那么后执行的线程，这个compareAndIncrementWorkerCount就会返回false
             * 然后执行c = ctl.get()，重新刷新一下c的值，否则c不改变的话，会进入死循环
             */
            if(compareAndIncrementWorkerCount(c)){
                break;
            }

            c = ctl.get();
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        try{
            Worker worker = new Worker(task);
            try{
                mainLock.lock();

                if(isRunning(c)){
                    if(worker.isAlive()){
                        throw new IllegalThreadStateException();
                    }

                    workers.add(worker);
                    workerAdded = true;
                }
            }finally {
                mainLock.unlock();
            }
            if(workerAdded){
                worker.start();
                workerStarted = true;
            }
        }finally {
            if(!workerStarted){

            }
        }

        return workerStarted;
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        boolean b = ctl.compareAndSet(expect, expect + 1);
        System.out.println(b);
        return b;
    }

    private void addWorkerFailed(Worker w) {

    }
}

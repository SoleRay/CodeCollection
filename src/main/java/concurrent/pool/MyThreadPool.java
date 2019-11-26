package concurrent.pool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool {

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(4);

    private Set<Worker> workers = new HashSet<>();

    /**
     * ctl的前4位是runstate，后28位是workerCount
     * <p>
     * 所以这个综合值的默认初始状态是：RUNNING,0个线程
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static final int COUNT_BITS = Integer.SIZE - 3; //29
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1; //1fff,ffff



    private static final int RUNNING = -1 << COUNT_BITS;//e000,0000 //-536870912
    private static final int SHUTDOWN = 0 << COUNT_BITS;//0
    private static final int STOP = 1 << COUNT_BITS;//2000,0000 //536870912
    private static final int TIDYING = 2 << COUNT_BITS;//4000,0000 //1073741824
    private static final int TERMINATED = 3 << COUNT_BITS;//6000,0000 //1610612736

    private static final boolean ONLY_ONE = true;

    private final ReentrantLock mainLock = new ReentrantLock();

    private final Condition termination = mainLock.newCondition();

    private static int runStateOf(int c) {
        return c & ~COUNT_MASK;
    }

    private static int workerCountOf(int c) {
        return c & COUNT_MASK;
    }

    /**
     * @param rs = runState
     * @param wc = workerCount
     * @return 返回综合值 ctl
     */
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    private boolean remove(Runnable task) {
        return queue.remove(task);
    }


    private int corePoolSize;

    private int maxPoolSize;

    public MyThreadPool(int corePoolSize, int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        private Thread t;

        private Runnable task;

        public Worker(Runnable firstTask) {
            task = firstTask;
            t = new Thread(this);
        }

        @Override
        public void run() {
            runTask(this);
        }

        public void start() {
            t.start();
        }

        public boolean isAlive() {
            return t.isAlive();
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unlock() {
            release(1);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }

        public Runnable getTask() {
            return task;
        }

        public void setTask(Runnable task) {
            this.task = task;
        }

        public Thread getThread() {
            return t;
        }

        public void interrupt() {

            if (getState() >= 0 && t != null && !t.isInterrupted()) {
                System.out.println("开始中断线程："+t.getName());
                t.interrupt();
            }
        }
    }

    private void runTask(Worker worker) {

        Thread t = Thread.currentThread();

        Runnable task = worker.getTask();

        try {
            while (task != null || (task = getTask()) != null) {

                worker.lock();
                System.out.println(t.getName()+" 开始执行任务");
                if ((runStateAtLeast(ctl.get(), STOP) ||
                        (Thread.interrupted() &&
                                runStateAtLeast(ctl.get(), STOP))) &&
                        !t.isInterrupted()){
                    System.out.println(t.getName()+" 再次被中断");
                    t.interrupt();
                }


                try {
                    beforeExecute(t, task);
                    try {
                        task.run();
                        afterExecute(task, null);
                    } catch (Exception e) {
                        afterExecute(task, e);
                    }

                } finally {
                    task = null;
                    worker.unlock();
                }
            }
        } finally {
            processWorkerExit(worker);

        }
    }

    private void processWorkerExit(Worker worker) {

        mainLock.lock();
        try {
            workers.remove(this);
            System.out.println(worker.t.getName()+" 被移出Worker集合");
        } finally {
            mainLock.unlock();
        }

        tryTerminate();
    }

    private void tryTerminate() {
        System.out.println("try terminate...");

        for(;;) {
            int c = ctl.get();

            if (isRunning(c)
                    || runStateAtLeast(c, TIDYING)
                    || (runStateLessThan(c, STOP) && !queue.isEmpty())) {
                return;
            }

            if (workerCountOf(c) != 0) {
                interruptIdleWorkers(ONLY_ONE);
            }

            mainLock.lock();

            try {
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        terminated();
                    } finally {
                        ctl.set(ctlOf(TERMINATED, 0));
                        termination.signalAll();
                    }
                    return;
                }
            } finally {
                mainLock.unlock();
            }

        }
    }

    private void terminated() {
    }

    private void interruptIdleWorkers(boolean onlyOne) {

        try {
            mainLock.lock();

            for (Worker w : workers) {

                Thread t = w.getThread();
                System.out.println(t.getName());
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        System.out.println("开始中断线程："+t.getName());
                        t.interrupt();
                    } catch (Exception e) {

                    } finally {
                        w.unlock();
                    }
                }else {
                    System.out.println(t.getName()+" 正在运行，不能被中断");
                }

                if (onlyOne) {
                    break;
                }
            }
        } finally {
            mainLock.unlock();
        }

    }

    private Runnable getTask() {

        boolean timedOut = false;

        for(;;) {

            int c = ctl.get();

            //1.如果状态是shutdown,那必须队列是空的，才可以返回null
            //2.如果是stop或者更高的状态，那可以直接返回null
            //3.这个判断很关键，shutDown和shutDownNow的本质区别就在这一步
            //shutdown的情况下，只要queue不为空，这里就不会返回null，所以程序会把所有的任务执行完，才退出
            //而shutDownNow的情况下，这里就算queue不为空，也会直接返回null，所以runTask方法就直接结束了
            if((runStateAtLeast(c, SHUTDOWN) && queue.isEmpty())
                    || runStateAtLeast(c,STOP)){
                decrementWorkerCount();
                return null;
            }

            int wc = workerCountOf(c);

            boolean timed = wc > corePoolSize;

            try {

                if ((wc > maxPoolSize || (timed && timedOut))
                        && (wc > 1 || queue.isEmpty())) {
                    if (compareAndDecrementWorkerCount(c)) {
                        return null;
                    }

                    continue;
                }

                Runnable task = timed ?
                        queue.poll(300, TimeUnit.MILLISECONDS) :
                        queue.take();

                if (task != null) {
                    return task;
                }
                timedOut = true;
            } catch (InterruptedException e) {
                timedOut = false;
            }
        }
    }



    public void shutDown() {
        mainLock.lock();
        try {
            advanceRunState(SHUTDOWN);
            interruptIdleWorkers(false);
            onShutdown();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
    }

    public void shutDownNow() {
        mainLock.lock();
        try {
            advanceRunState(STOP);
            interruptWorkers();
            onShutdown();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
    }

    private void onShutdown() {
    }

    private void advanceRunState(int targetState) {
        int c = ctl.get();
        while (true) {
            if (runStateAtLeast(c, targetState) || ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c)))) {
                break;
            }
        }
    }

    private void interruptWorkers() {

        for (Worker w : workers) {
            w.interrupt();
        }
    }


    protected void beforeExecute(Thread t, Runnable r) {
    }

    protected void afterExecute(Runnable r, Throwable t) {
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }

        int c = ctl.get();

        if (workerCountOf(c) < corePoolSize) {
            addWorker(task, true);
            System.out.println("结束前：" + workerCountOf(ctl.get()));
            return;
        }

        System.out.println("线程池中的正在运行的线程已经达到最大值，任务将进入队列。。。");

        //如果线程数目已经超过核心线程数，但任务还可以加入队列的话，那么什么都不做
        if (isRunning(c) && queue.offer(task)) {
            int recheck = ctl.get();
            if (!isRunning(recheck) && remove(task)) {
                rejectTask();
            } else if (workerCountOf(recheck) == 0) {
                //TODO
            }
        } else if (!addWorker(task, false)) {
            rejectTask();
        }
    }

    private void rejectTask() {
        throw new RuntimeException("当前运行的线程数已经达到最大值，无法再接受新的任务");
    }

    //其实这个锁，真正想要锁的，是把work放入set这一步
    private boolean addWorker(Runnable task, boolean core) {



        for(;;) {

            int c = ctl.get();

            if(runStateAtLeast(c,STOP)
                    || (runStateAtLeast(c,SHUTDOWN) && (task!=null||queue.isEmpty()))){
                return false;
            }


            //如果当前worker的数量超过了核心或者最大线程数（取决于core的值）
            if (workerCountOf(c) >= workerCountOf((core ? corePoolSize : maxPoolSize))) {
                System.out.println("超过了核心或者最大线程数");
                return false;
            }

            /**
             * 计算数量，就是通过这个方法来控制并发的
             * 比如说，核心线程数目是1，如果有两个人同时调用了execute方法来到这里，
             * 那么后执行的线程，这个compareAndIncrementWorkerCount就会返回false
             * 然后执行c = ctl.get()，重新刷新一下c的值，否则c不改变的话，会进入死循环
             */
            if (compareAndIncrementWorkerCount(c)) {
                break;
            }

        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker worker = null;
        try {
            worker = new Worker(task);
            try {
                mainLock.lock();

                int c = ctl.get();

                if (isRunning(c)) {
                    if (worker.isAlive()) {
                        throw new IllegalThreadStateException();
                    }

                    workers.add(worker);
                    workerAdded = true;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
                worker.start();
                workerStarted = true;
            }
        } finally {
            if (!workerStarted) {
                addWorkerFailed(worker);
            }
        }

        return workerStarted;
    }

    private void decrementWorkerCount() {
        ctl.addAndGet(-1);
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    private void addWorkerFailed(Worker w) {
        mainLock.lock();
        try {
            if (w != null)
                workers.remove(w);
            decrementWorkerCount();
            tryTerminate();
        } finally {
            mainLock.unlock();
        }
    }

    public void aa(){

        for (;;) {
            int c = ctl.get();

            if(ctl.compareAndSet(c, c + 1)) {
                break;
            }
        }
    }
}

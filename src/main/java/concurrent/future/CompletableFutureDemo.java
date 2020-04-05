package concurrent.future;

import math.Sum;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CompletableFutureDemo {


    /**
     * runAsync 异步执行一个Runnable，相当于通过Thread运行一个Runnable
     * Runnable r = () -> {};
     * Thread t = new Thread(r)
     * t.start();
     */
    @Test
    public void runAsync(){
        CompletableFuture.runAsync(()-> System.out.println("hello"));
    }

    /**
     * supplyAsync 异步执行一个Supplier，相当于通过Thread运行一个含有Callable的FutureTask
     * Callable c = () -> {return xxx;};
     * FutureTask f = new FutureTask(c);
     * Thread t = new Thread(f);
     * t.start();
     * System.out.println(f.get());
     */
    @Test
    public void supplyAsync() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Sum::sumRange);
        System.out.println(future.get());
    }

    /**
     * thenApply 相当于注册了一个回调，即：当任务完成后，做些什么。这就解决了普通Future需要手工get阻塞，拿到结果以后才能做事情的痛点。
     */
    @Test
    public void thenApply() throws Exception {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(Sum::sumRange).thenApply(result->result +"，"+ 2);
        System.out.println(future.get());
    }

    /**
     * complete 用于手工完成某个任务。其核心是set Result的值
     * 应用场景：两个线程，一个线程用future.get()阻塞。另外一个线程，做完一些事情后，手动调用complete()进行通知
     */
    @Test
    public void complete() throws Exception {
        CompletableFuture<String> future = new CompletableFuture();
        //第一个线程
        CompletableFuture.runAsync(()->{
            String result = null;
            try {
                Thread.sleep(3000);//模拟运行任务。。。
                result = "find the boy...";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete(result);
        });

        //第二个线程
        CompletableFuture.runAsync(()->{
            try {
                //TODO sth....
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * whenComplete 当任务完成时触发事件
     *
     *
     */
    @Test
    public void whenComplete() throws Exception {

        CompletableFuture<String> future = new CompletableFuture();

        future.whenComplete((result,throwable)->{
            if(throwable instanceof RuntimeException){
                throwable.printStackTrace();
            }

            System.out.println(result);
        });

//        future.complete("box");
//        future.completeExceptionally(new RuntimeException("....."));
        future.cancel(true);
    }

    /**
     * thenCompose 多个CompletableFuture 串联执行，可以把前一个任务的结果传递给下一个任务，最终拿到最后的future
     * 注意，是串联执行。
     */
    @Test
    public void thenCompose() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(Sum::sumRange).thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " dogs cry..."));
        System.out.println(future.get());
    }

    /**
     * 执行两个独立的任务，并对其结果执行某些操作，但不需要返回值
     */
    @Test
    public void thenAcceptBoth() throws Exception {
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(Sum::sumRange);
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(Sum::factorial);

        futureA.thenAcceptBoth(futureB, (resultA, resultB) -> System.out.println(resultA +"，"+ resultB));
    }

    /**
     * 执行两个独立的任务，并对其结果执行某些操作，需要返回值
     */
    @Test
    public void thenCombine() throws Exception {
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(Sum::sumRange);
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(Sum::factorial);

        CompletableFuture<String> resultFuture = futureA.thenCombine(futureB, (resultA, resultB) -> resultA +"，"+ resultB);
        System.out.println(resultFuture.get());
    }

    @Test
    public void allOf() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(new SleepTask(5000));
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(new SleepTask(3000));
        CompletableFuture<Integer> futureC = CompletableFuture.supplyAsync(new SleepTask(1000));

        CompletableFuture<Void> resultFuture = CompletableFuture.allOf(futureA, futureB, futureC);
        resultFuture.get();
        Integer resultA = futureA.get();
        Integer resultB = futureB.get();
        Integer resultC = futureC.get();
        Integer sum = resultA+resultB+resultC;
        System.out.println("Sum futureA="+ resultA +",futureB="+resultB+",futureC="+resultC+" is "+sum);
        stopWatch.stop();
        System.out.println("end in " + stopWatch.getTotalTimeSeconds());

    }

    @Test
    public void anyOf() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Supplier<Long> s = new TicketTask();
        CompletableFuture<Long> futureA = CompletableFuture.supplyAsync(s);
        CompletableFuture<Long> futureB = CompletableFuture.supplyAsync(s);
        CompletableFuture<Long> futureC = CompletableFuture.supplyAsync(s);

        CompletableFuture<Object> resultFuture = CompletableFuture.anyOf(futureA, futureB, futureC);
        System.out.println("Thread "+ resultFuture.get()+" get the ticket");
        stopWatch.stop();
        System.out.println("end in " + stopWatch.getTotalTimeSeconds());
    }


    class TicketTask implements Supplier<Long> {

        private final AtomicBoolean ticket = new AtomicBoolean();

        @Override
        public Long get() {

            if(ticket.compareAndSet(false,true)){
                return Thread.currentThread().getId();
            }

            System.out.println("Thread " + Thread.currentThread().getId() + " didn't get the ticket!!!!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thread " + Thread.currentThread().getId() + "end...");
            return null;
        }
    }

    class SleepTask implements Supplier<Integer>{

        private long sleepTime;

        public SleepTask(long sleepTime){
            this.sleepTime = sleepTime;
        }

        @Override
        public Integer get() {
            try {
                Thread.sleep(sleepTime);
                System.out.println("Thread " + Thread.currentThread().getId() + " wake up!!!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Random().nextInt(100);
        }
    }


}

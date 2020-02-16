package concurrent.future;

import math.Sum;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.time.Clock;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    @Test
    public void runAsync(){
        CompletableFuture.runAsync(()-> System.out.println("hello"));
    }

    @Test
    public void supplyAsync() throws Exception {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Sum::sumRange);
        System.out.println(future.get());
    }

    @Test
    public void thenApply() throws Exception {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(Sum::sumRange).thenApply(result->result +"，"+ 2);
        System.out.println(future.get());
    }

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

        Runnable r = new SleepTask();
        CompletableFuture<Void> futureA = CompletableFuture.runAsync(r);
        CompletableFuture<Void> futureB = CompletableFuture.runAsync(r);
        CompletableFuture<Void> futureC = CompletableFuture.runAsync(r);

        CompletableFuture<Void> resultFuture = CompletableFuture.allOf(futureA, futureB, futureC);
        resultFuture.get();
        stopWatch.stop();
        System.out.println("end in " + stopWatch.getTotalTimeSeconds());

    }

    class SleepTask implements Runnable{

        @Override
        public void run() {
            try {
                System.out.println("Thread is " + Thread.currentThread().getId());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

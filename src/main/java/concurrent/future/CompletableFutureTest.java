package concurrent.future;

import org.junit.jupiter.api.Test;

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
        Clock clock = Clock.tickMillis(ZoneId.systemDefault());
        CompletableFuture<Void> futureA = CompletableFuture.runAsync(SleepTask::new);
        CompletableFuture<Void> futureB = CompletableFuture.runAsync(SleepTask::new);
        CompletableFuture<Void> futureC = CompletableFuture.runAsync(SleepTask::new);

        CompletableFuture<Void> resultFuture = CompletableFuture.allOf(futureA, futureB, futureC);
        resultFuture.get();
        long millis = clock.millis();
        System.out.println("end in " + millis);

    }

    class SleepTask implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Sum {

        static int sumRange(int start,int end){
            int result = 0;
            for(int i=start;i<=end;i++){
                result = result + i;
            }
            return result;
        }

        static int factorial(int start,int end){
            int result = 1;
            for(int i=start;i<=end;i++){
                result = result * i;
            }
            return result;
        }

        static int sumPow(int start,int end){
            int result = 0;
            for(int i=start;i<=end;i++){
                result = result + i*i;
            }
            return result;
        }

        static int sumRange(){
            return sumRange(1,100);
        }

        static int factorial(){
            return factorial(1,10);
        }

        static int sumPow(){
            return factorial(1,100);
        }

    }
}

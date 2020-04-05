package socket.netty.promise;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

    public static final String EXIT = "exit";

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        while(true){

            String s = scanner.nextLine();

            CompletableFuture<String> future = new CompletableFuture();

            future.whenComplete((result, throwable) -> {
                invokeTaskB(future, throwable);
                System.out.println("请输入：");
            });

            executor.execute(()->{
                invokeTaskA(s, future);
            });

            if(exit(executor,scanner,s)){
                break;
            }
        }
    }

    private static boolean exit(ExecutorService executor, Scanner scanner,String inputStr) {

        if(EXIT.equalsIgnoreCase(inputStr)){
            executor.shutdown();
            scanner.close();
            return true;
        }

        return false;
    }

    private static void invokeTaskA(String s, CompletableFuture<String> future) {
        try {
            System.out.println("start to execute task A");
//                Thread.sleep(3000);//模拟做些事情

            if("dog".equals(s)){
                future.cancel(true);
            }else if("apple".equals(s)){
                future.completeExceptionally(new RuntimeException("we can't eat apple!!!!"));
            }else {
                System.out.println("task A is done!!!");
                future.complete(null);
            }
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }

    private static void invokeTaskB(CompletableFuture<String> future, Throwable throwable) {
        if(future.isCancelled()){
            System.out.println("task is cancel...");
        }else if(future.isCompletedExceptionally()){
            if(throwable!=null){
                System.out.println("expection occured:"+throwable.getMessage());
            }
        }else {
            System.out.println("task B done !!!!");
        }

    }
}

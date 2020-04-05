package socket.netty.promise;

import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PromiseTest {

    public static void main(String[] args) throws Exception {

        System.out.println("请输入：");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        DefaultPromise promise = new DefaultPromise(eventLoopGroup.next());

        executor.execute(()->{
            invokeTaskA(s, promise);
        });

        //让任务A先完成
//        Thread.sleep(1000);

        if(promise.isDone()){
            invokeTaskB(executor, eventLoopGroup, promise);
        }else {
            promise.addListener(future -> {

                //看看实际走了哪个流程
                System.out.println("use listener....");

                invokeTaskB(executor, eventLoopGroup, promise);
            });
        }
    }

    private static void invokeTaskA(String s, DefaultPromise promise) {
        try {
            System.out.println("start to execute task A");
//                Thread.sleep(3000);//模拟做些事情

            if("dog".equals(s)){
                promise.cancel(true);
                return;
            }

            if("apple".equals(s)){
                throw new RuntimeException("we can't eat apple!!!!");
            }

            System.out.println("task A is done!!!");
            promise.trySuccess(null);
        } catch (Exception e) {
            promise.tryFailure(e);
//            e.printStackTrace();
        }
    }

    private static void invokeTaskB(ExecutorService executor, NioEventLoopGroup eventLoopGroup, Future future) {
        if(future.isSuccess()){
            System.out.println("task B done !!!!");
        }else if(future.isCancelled()){
            System.out.println("task is cancel...");
        }else {
            Throwable cause = future.cause();
            if(cause!=null){
                System.out.println("expection occured:"+cause.getMessage());
            }
        }
        executor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }
}

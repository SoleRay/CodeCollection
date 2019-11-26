package concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MyCompute extends RecursiveTask<Integer> {

    private final static int THRESHOLD = 10;
    private int[] src;
    private int fromIndex;
    private int toIndex;

    public MyCompute(int[] src, int fromIndex, int toIndex) {
        this.src = src;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @Override
    protected Integer compute() {
        if(toIndex - fromIndex <= THRESHOLD){
            System.out.println("fromIndex="+fromIndex+",toIndex="+toIndex);
            return sum(src,fromIndex,toIndex);
        }else {
            int midIndex = getMiddleIndex(fromIndex,toIndex);

            MyCompute left = new MyCompute(src,fromIndex,midIndex);
            MyCompute right = new MyCompute(src,midIndex+1,toIndex);
            invokeAll(left,right);
            return left.join()+right.join();
        }

    }

    private int getMiddleIndex(int fromIndex, int toIndex) {
        return (toIndex + fromIndex)/2;
    }

    private int sum(int[] src, int fromIndex, int toIndex) {

        int sum = 0;
        for(int i = fromIndex;i<=toIndex;i++){
            sum = sum + src[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        int len = 10000000;

        int[] src = new int[len];

        for(int i=0;i<len;i++){
            src[i] = i;
        }

        int sum = 0;
        for(int i=0;i<src.length;i++){
            sum = sum + src[i];
        }
        System.out.println(sum);


        ForkJoinPool pool = new ForkJoinPool();

        MyCompute m = new MyCompute(src,0,len-1);
        pool.invoke(m);
        pool.shutdown();
        System.out.println(m.join());
        System.out.printf("**********************\n");

        System.out.printf("线程池的worker线程们的数量:%d\n",
                pool.getPoolSize());
        System.out.printf("当前执行任务的线程的数量:%d\n",
                pool.getActiveThreadCount());
        System.out.printf("没有被阻塞的正在工作的线程:%d\n",
                pool.getRunningThreadCount());
        System.out.printf("已经提交给池还没有开始执行的任务数:%d\n",
                pool.getQueuedSubmissionCount());
        System.out.printf("已经提交给池已经开始执行的任务数:%d\n",
                pool.getQueuedTaskCount());
        System.out.printf("线程偷取任务数:%d\n",
                pool.getStealCount());
        System.out.printf("池是否已经终止 :%s\n",
                pool.isTerminated());
        System.out.printf("**********************\n");
    }
}

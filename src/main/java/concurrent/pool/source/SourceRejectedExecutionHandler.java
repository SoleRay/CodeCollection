package concurrent.pool.source;

public interface SourceRejectedExecutionHandler {


    void rejectedExecution(Runnable r, SourceThreadPoolExecutor executor);
}
package cold.com.message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {

    private Thread thread;
    private LinkedBlockingQueue<Runnable> runnables;
    private ExecutorService executor;
    private Boolean isStop = false;


    @Override
    public Thread newThread(@NonNull Runnable r) {

        thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("WorkTread");
        thread.setUncaughtExceptionHandler(this);
        return thread;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!isStop)
            createThreadPool(1, 1, 0L, this, runnables);
    }

    public static WorkThreadFactory createThreadPool(int corePoolSize,
                                                     int maximumPoolSize,
                                                     long keepAliveTime, @NonNull WorkThreadFactory workThreadFactory, LinkedBlockingQueue<Runnable> runnables) {
        workThreadFactory.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.NANOSECONDS, runnables, workThreadFactory);
        return workThreadFactory;
    }

    public static WorkThreadFactory createThreadPool() {
        return createThreadPool(1);
    }

    public static WorkThreadFactory createThreadPool(int size) {
        WorkThreadFactory workThreadFactory = new WorkThreadFactory();
        return createThreadPool(size, 1, 0L, workThreadFactory, workThreadFactory.runnables = new LinkedBlockingQueue<>());
    }


    public void shutdownNow() {
        isStop = true;
        if (executor != null)
            executor.shutdownNow();
    }


    public void execute(@NonNull final Run command) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                command.run(WorkThreadFactory.this.isStop);
            }
        };
        executor.execute(runnable);

    }
}

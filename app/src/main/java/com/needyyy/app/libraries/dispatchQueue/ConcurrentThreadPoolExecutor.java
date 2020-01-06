package com.needyyy.app.libraries.dispatchQueue;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by akamahesh on 19/11/16.
 */

final class ConcurrentThreadPoolExecutor extends ThreadPoolExecutor {
    private Runnable activeRunnable = null;

    private ConcurrentThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ConcurrentThreadPoolExecutor build (String identifier, DispatchQueue.QoS qoS)   {
        ThreadGroup threadGroup = new ThreadGroup(identifier);

        LinkedBlockingQueue<Runnable> workQueue =  new LinkedBlockingQueue<Runnable>();
        PriorityThreadFactory threadFactory = new PriorityThreadFactory(qoS.getValue(), threadGroup);

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        switch (qoS) {
            case userInteractive:
                return new ConcurrentThreadPoolExecutor(2, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>(), threadFactory, new CallerRunsPolicy());
            case userInitiated:
                return new ConcurrentThreadPoolExecutor(2, NUMBER_OF_CORES*2, 30L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), threadFactory, new CallerRunsPolicy());
            case background:
                return new ConcurrentThreadPoolExecutor(2, NUMBER_OF_CORES*2, 30L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), threadFactory, new DiscardOldestPolicy());
            case utility:
                return new ConcurrentThreadPoolExecutor(0, 2, 30L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), threadFactory, new DiscardOldestPolicy());
            default:        //Same as background
                return new ConcurrentThreadPoolExecutor(1, NUMBER_OF_CORES*2, 30L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), threadFactory, new DiscardOldestPolicy());
        }
    }
}
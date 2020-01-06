package com.needyyy.app.libraries.dispatchQueue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by akamahesh on 19/11/16.
 */

final class SerialThreadPoolExecutor extends ThreadPoolExecutor {
    private Runnable activeRunnable = null;

    private SerialThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, LinkedBlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public static SerialThreadPoolExecutor build(String identifier, DispatchQueue.QoS qoS) {
        ThreadGroup threadGroup = new ThreadGroup(identifier);

        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        PriorityThreadFactory threadFactory = new PriorityThreadFactory(qoS.getValue(), threadGroup);

        SerialThreadPoolExecutor serialThreadPoolExecutor = new SerialThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, workQueue, threadFactory);
        serialThreadPoolExecutor.prestartCoreThread();
        return serialThreadPoolExecutor;
    }

    public synchronized void execute(final Runnable r) {
        this.getQueue().offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });

        if (activeRunnable == null) {
            scheduleNext();
        }
    }


    protected synchronized void scheduleNext() {
        if ((activeRunnable = this.getQueue().poll()) != null) {
            this.execute(activeRunnable);
        }
    }
}

package com.needyyy.app.libraries.dispatchQueue;

import android.os.Process;

import java.util.concurrent.ThreadFactory;

/**
 * Created by akamahesh on 12/11/16.
 */

class PriorityThreadFactory implements ThreadFactory {
    private final int mThreadPriority;
    private final ThreadGroup threadGroup;

    public PriorityThreadFactory(int threadPriority, ThreadGroup threadGroup) {
        this.mThreadPriority    = threadPriority;
        this.threadGroup        = threadGroup;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable=new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                }catch (Exception e){
                    e.printStackTrace();
                }
                runnable.run();
            }
        };
        return new Thread(threadGroup ,wrapperRunnable);
    }
}

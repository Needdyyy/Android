package com.needyyy.app.libraries.dispatchQueue;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by akamahesh on 19/11/16.
 */

class MainThreadExecutor implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public synchronized void execute(Runnable runnable) {
        handler.post(runnable);
    }

}

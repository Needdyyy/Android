package com.needyyy.app.libraries.dispatchQueue;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by akamahesh on 18/11/16.
 */

public class DispatchQueue  {
    /**************************************Defining Enums******************************************/
    //Queue Type
    public enum Type   {
        concurrent                 (1),
        serial                     (2);             //Default Queue Type

        private int value;
        Type (int type) {
            this.value = type;
        }

        /**Convert int to DispatchQueue Type*/
        public static Type getType (int value)  {
            for (Type type: Type.values()) {
                if(type.value == value) {
                    return type;
                }
            }
            return serial;                          // return Default value
        }

        /**To get Integer value of corresponding emum*/
        public Integer getValue()    {
            return this.value;
        }
    }

    // Define the Quality of service in DispatchQueue.
    public enum QoS   {
        userInteractive                 (1),            //ProcessPriority: THREAD_PRIORITY_LESS_FAVORABLE; Highest for app
        userInitiated                   (3),
        background                      (5),            //Default Queue Type
        utility                         (10);           //ProcessPriority: THREAD_PRIORITY_BACKGROUND;  Lowest for app

        private int value;
        QoS (int qos) {
            this.value = qos;
        }

        /**Convert int to DispatchQueue Qos*/
        public static QoS getQoS (int value)  {
            for (QoS qos: QoS.values()) {
                if(qos.value == value) {
                    return qos;
                }
            }
            return background;                          // return Default value
        }

        /**To get Integer value of corresponding emum*/
        public Integer getValue()    {
            return this.value;
        }
    }

    /**************************************Defining Enums******************************************/

    private static final String kIdUserInteractive                                    = "com.queue.global.userInteractive";
    private static final String kIdUserInitiated                                      = "com.queue.global.userInitiated";
    private static final String kIdBackground                                         = "com.queue.global.background";
    private static final String kIdUtility                                            = "com.queue.global.utility";

    private static final AtomicReference<DispatchQueue> _UserInteractiveDispatchQueue = new AtomicReference<DispatchQueue>();
    private static final AtomicReference<DispatchQueue> _UserInitiatedDispatchQueue   = new AtomicReference<DispatchQueue>();
    private static final AtomicReference<DispatchQueue> _BackgroundDispatchQueue      = new AtomicReference<DispatchQueue>();
    private static final AtomicReference<DispatchQueue> _UtilityDispatchQueue         = new AtomicReference<DispatchQueue>();
    private static final AtomicReference<MainThreadExecutor> _MainDispatchQueue       = new AtomicReference<MainThreadExecutor>();

    private final ThreadPoolExecutor threadPoolExecutor;

    private ThreadPoolExecutor getThreadPoolExecutor(@NonNull String identifier, @NonNull Type type,
                                                     @NonNull QoS qos)  {
        if(type == Type.concurrent) {
            return ConcurrentThreadPoolExecutor.build(identifier, qos);
        }
        else    {   //serial default
            return SerialThreadPoolExecutor.build(identifier, qos);
        }
    }

    public DispatchQueue (@NonNull String identifier)    {
        threadPoolExecutor = getThreadPoolExecutor(identifier, Type.serial, QoS.background);
    }

    public DispatchQueue(@NonNull String identifier, @NonNull Type type)    {
        threadPoolExecutor = getThreadPoolExecutor(identifier, type, QoS.background);
    }

    public DispatchQueue(@NonNull String identifier, @NonNull QoS qos)    {
        threadPoolExecutor = getThreadPoolExecutor(identifier, Type.serial, qos);
    }

    public DispatchQueue(@NonNull String identifier, @NonNull Type type, @NonNull QoS qos) {
        threadPoolExecutor = getThreadPoolExecutor(identifier, type, qos);
    }

    private static DispatchQueue globalQueue(String identifier , @NonNull AtomicReference<DispatchQueue> atomicDispatchQueue,
                                             QoS qos)  {
        for (;;) {
            DispatchQueue currentDispatchQueue = atomicDispatchQueue.get();
            if (currentDispatchQueue != null) {
                return currentDispatchQueue;
            }
            currentDispatchQueue = new DispatchQueue(identifier, Type.concurrent, qos);
            if (atomicDispatchQueue.compareAndSet(null, currentDispatchQueue)) {
                return currentDispatchQueue;
            } else {
                currentDispatchQueue.shutdownQueue();
            }
        }
    }

    public static DispatchQueue global(@NonNull QoS qos) {
        switch (qos) {
            case userInteractive:
                return globalQueue(kIdUserInteractive, _UserInteractiveDispatchQueue, qos);
            case userInitiated:
                return globalQueue(kIdUserInteractive, _UserInitiatedDispatchQueue, qos);
            case background:
                return globalQueue(kIdUserInteractive, _BackgroundDispatchQueue, qos);  //Default value
            case utility:
                return globalQueue(kIdUserInteractive, _UtilityDispatchQueue, qos);
            default:
                return globalQueue(kIdUserInteractive, _BackgroundDispatchQueue, qos);  //Default value
        }
    }

    private static MainThreadExecutor mainQueue () {
        for (;;) {
            MainThreadExecutor mainExecutor = _MainDispatchQueue.get();
            if (mainExecutor != null) {
                return mainExecutor;
            }
            mainExecutor = new MainThreadExecutor();
            if (_MainDispatchQueue.compareAndSet(null, mainExecutor)) {
                return mainExecutor;
            }
        }
    }

    private void shutdownQueue ()  {
        this.threadPoolExecutor.shutdown();
    }

    public synchronized static void main (@NonNull Runnable task)  {
        mainQueue().execute(task);
    }

    public synchronized void async (Runnable task) {
        this.threadPoolExecutor.execute(task);
    }

    public synchronized void sync (Runnable task) {
        try {
            Future future = this.threadPoolExecutor.submit(task);
            future.get();
        }
        catch (Exception error) {
            error.printStackTrace();
            Log.v("DISPATCHQUEUE", error.getMessage());
        }
    }

    protected void finalize( ) throws Throwable {
        this.threadPoolExecutor.shutdown();
        super.finalize( );
    }
}
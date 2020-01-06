package com.needyyy.app.Base;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.needyyy.app.utils.PreferencesManager;
import com.needyyy.app.webutils.WebConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by akamahesh on 21/4/17.
 */
public class BaseApplication extends MultiDexApplication {


    public static PreferencesManager manager;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static BaseApplication controller;
    public static Retrofit retrofit;
    private static final String TAG = "BaseApplication";
    //Static Properties
    private static Context _Context;

    public static Context getContext() {
        return _Context;
    }

    public static BaseApplication getInstance() {
        return (BaseApplication) _Context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _Context = getApplicationContext();
        MultiDex.install(this);
        controller = this;


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        /**
         * Order of initialization :>ReachabilityManager ->
         *LocationServiceManager ->
          * DatabaseManager ->
          * ModelManager ->
          * MainRealmDBManager ->
          * ContactsSyncManager
        */

//        ReachabilityManager.manager();
//        LocationServiceManager.manager().initializeManager(this);
//        DatabaseManager.manager().initializeDatabaseManager(this);
//        ModelManager.modelManager().initializeModelManager();
//        MainRealmDBManager.manager().initializeManager();
//        ContactsSyncManager.manager().initializeManager(this);

        //to initial Leak canary **not be removed

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            //this process is dedicated to LeakCanary for heap analysis.
//            //you should not init your app in this process
//            return;
//        }

//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            Timber.plant(new CrashReportingTree());
//        }

        //Disable Crashlytics for debug mode
        configureCrashReporting();


//        LeakCanary.install(this);
        enableStrictMode();
//        AndroidUtils.getInstance().printKeyHash(getContext());
    }

    /**
     * detects things you might be doing by accident and brings them to your attention so you can fix them
     * @link https://developer.android.com/reference/android/os/StrictMode.html
     */
    private void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
    }


    private void configureCrashReporting() {
//        CrashlyticsCore core = new CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
//                .build();
//        Fabric.with(this, core);
    }

    public static PreferencesManager getManager() {

        if (manager == null)
            manager = new PreferencesManager(controller);

        return manager;
    }


    public static Retrofit getRetrofitInstance() {

        if (retrofit != null)
            return retrofit;

//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Accept", "application/json")
//                            .header("x-api-key", "$2y$10$wWcYRnJiq3aDK1VavfCEOud7qSS5f2kal5df43f43545gfg56oKCQ4U43MHNumiTBAOS")
//                            .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = httpClient.addInterceptor(interceptor).connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES) .writeTimeout(1, TimeUnit.MINUTES).build();

        final Gson gson0 = new GsonBuilder()

                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(WebConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson0))
                .client(client)
                .build();

        return retrofit;
    }

}
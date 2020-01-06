package com.needyyy.app.Chat.groupchatwebrtc.view;

import android.app.Application;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.needyyy.app.Chat.groupchatwebrtc.models.QbConfigs;
import com.needyyy.app.Chat.groupchatwebrtc.util.QBResRequestExecutor;
import com.needyyy.app.Chat.groupchatwebrtc.utils.ConfigUtils;
import com.needyyy.app.webutils.WebConstants;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import co.paystack.android.PaystackSdk;
import java.util.concurrent.TimeUnit;
import com.needyyy.app.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends MultiDexApplication {
    private static App instance, controller;
    public static Retrofit retrofit;

    private QBResRequestExecutor qbResRequestExecutor;

    public static final String TAG = App.class.getSimpleName();

    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config_template.json";
    private QbConfigs qbConfigs;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initQBSessionManager();
        initQbConfigs();
        initCredentials();
        initApplication();
        MultiDex.install(this);
        controller = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        PaystackSdk.initialize(getApplicationContext());
    }

    public static Retrofit getRetrofitInstance() {

        if (retrofit != null)
            return retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = httpClient.addInterceptor(interceptor).connectTimeout(25, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS) .writeTimeout(30, TimeUnit.SECONDS).build();

        final Gson gson = new GsonBuilder()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(WebConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }


    private void initApplication(){
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    //////////////////////////

    private void initQbConfigs() {
        Log.e(TAG, "QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = ConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }


    public void initCredentials(){
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());

            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public QbConfigs getQbConfigs(){
        return qbConfigs;
    }

    protected String getQbConfigFileName(){
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }

    private void initQBSessionManager() {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession qbSession) {
                Log.d(TAG, "Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                Log.d(TAG, "Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                Log.d(TAG, "Session Deleted");
            }

            @Override
            public void onSessionRestored(QBSession qbSession) {
                Log.d(TAG, "Session Restored");
            }

            @Override
            public void onSessionExpired() {
                Log.d(TAG, "Session Expired");
            }

            @Override
            public void onProviderSessionExpired(String provider) {
                Log.d(TAG, "Session Expired for provider:" + provider);
            }
        });
    }

}

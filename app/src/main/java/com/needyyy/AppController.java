package com.needyyy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.needyyy.app.Chat.groupchatwebrtc.models.QbConfigs;
import com.needyyy.app.Chat.groupchatwebrtc.util.QBResRequestExecutor;
import com.needyyy.app.Chat.groupchatwebrtc.utils.ConfigUtils;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.NetworkChangeReceiver;
import com.needyyy.app.utils.PreferencesManager;
import com.needyyy.app.webutils.WebConstants;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.needyyy.app.constants.Constants.kCurrentUser;


public class AppController extends MultiDexApplication {

    public static PreferencesManager manager;
    public final static int FRAGMENT_CODE = 1001;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static AppController controller;
    public static Retrofit retrofit;
    public static Retrofit retrofitnode;
    private static final String TAG = "AppController";

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    UserDataResult userData ;
    private QBResRequestExecutor qbResRequestExecutor;

    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config_template.json";
    private QbConfigs qbConfigs;


    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        controller = this;
        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        if (userData!=null)
            offlineFeature();
        initQBSessionManager();
        initQbConfigs();
        initCredentials();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    private void offlineFeature() {


        //---WHENEVER USER IS NOT LOGGED IN THIS FEATURE WILL NOT WORK---
        //---DO IT WHENEVER YOU GET TIME---
        if(userData!=null){

            //---FIREBASE OFFLINE FEATURE---
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            // ---PIACSSO OFFLINE FEATURE--
            mAuth=FirebaseAuth.getInstance();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(true);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);

            //String user_id = mAuth.getCurrentUser().getUid();
            // Log.e("Current user inside : ",user_id);

            //---SEETING TIME_STAMP ON DISCONNECT-----
            mUserDatabase = FirebaseDatabase.getInstance().
                    getReference().child("needyyy/users").child(userData.getId());
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){

                        mUserDatabase.child("is_online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    public static PreferencesManager getManager() {

        if (manager == null)
            manager = new PreferencesManager(controller);

        return manager;
    }

    public static Context getContext() {
        return controller ;
    }
    public static Retrofit getRetrofitInstance(boolean i) {

        if (retrofit != null)
            return retrofit;



        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Double randnumber= CommonUtil.getRandomNumber();
                String userId = "",jwt;


                UserDataResult userData =(BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                if (userData!=null){
                    userId = userData.getId();
                    jwt=userData.getJwt();
                }else{
                    userId = "";
                    jwt="jwt";
                }

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Userid", userId)
                        .header("Devicetoken", AppController.getManager().getTocken())
                        .header(Constant.Jwt, jwt)
//                        .header("enc_security",CommonUtil.MD5_Hash(CommonUtil.encodeBase64(randnumber+"Surya_Mohit")))
//                        .header("user_id", AppController.getManager().getId())
//                        .header("language", AppController.getManager().getCurrentLanguage())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

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

    public static Retrofit getRetrofitInstanceNode() {

        if (retrofitnode != null)
            return retrofitnode;



        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Double randnumber= CommonUtil.getRandomNumber();
                String userId = "",jwt;


                UserDataResult userData =(BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                if (userData!=null){
                    userId = userData.getId();
                    jwt=userData.getJwt();
                }else{
                    userId = "";
                    jwt="jwt";
                }

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Userid", userId)
                        .header("Devicetoken", AppController.getManager().getTocken())
                        .header(Constant.Jwt, jwt)
//                        .header("enc_security",CommonUtil.MD5_Hash(CommonUtil.encodeBase64(randnumber+"Surya_Mohit")))
//                        .header("user_id", AppController.getManager().getId())
//                        .header("language", AppController.getManager().getCurrentLanguage())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = httpClient.addInterceptor(interceptor).connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES) .writeTimeout(1, TimeUnit.MINUTES).build();

        final Gson gson0 = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofitnode = new Retrofit.Builder()
                .baseUrl(WebConstants.BASE_URL_NODE)
                .addConverterFactory(GsonConverterFactory.create(gson0))
                .client(client)
                .build();

        return retrofitnode;
    }


    public static synchronized AppController getInstance() {
        return controller;
    }


    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }


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

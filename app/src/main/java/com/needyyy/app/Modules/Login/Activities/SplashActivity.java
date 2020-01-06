package com.needyyy.app.Modules.Login.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.needyyy.AppController;
import com.needyyy.app.Chat.groupchatwebrtc.services.CallService;
import com.needyyy.app.Chat.groupchatwebrtc.utils.Consts;
import com.needyyy.app.Chat.groupchatwebrtc.utils.SharedPrefsHelper;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.mypage.model.masterindex.masterindex.MasterIndex;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.Kmasterhit;
import static com.needyyy.app.constants.Constants.kCurrentUser;


public class SplashActivity extends AppCompatActivity {
    //    private static final String APP_ID = "78030";
//    private static final String AUTH_KEY = "af72FtfUNJAkyL9";
//    private static final String AUTH_SECRET = "B4xkMzhbc8MVLt7";
//    private static final String ACCOUNT_KEY = "7yvNe17TnjNUqDoPwfqp";
    MasterIndex masterIndex;
    AlertDialog.Builder builder;
    int verCode;
    String[] permissionRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    Boolean status=true;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 404;
    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  sharedPrefsHelper = SharedPrefsHelper.getInstance();
        // registersession();
        // intializeframework();

        // login();
        builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_splash);
        generateFirebaseTocken();

//        sharedPrefsHelper = SharedPrefsHelper.getInstance();
//
//
//        if (sharedPrefsHelper.hasQbUser()) {
//            startLoginService(sharedPrefsHelper.getQbUser());
//            Log.d("CHAT", "Chat Session Available");
//        }else{
//            Log.d("CHAT", "Chat Session Not Available");
//        }
    }

    public void startLoginService(QBUser qbUser) {
        CallService.start(SplashActivity.this, qbUser);
    }

    private void  hitapi() {

        WebInterface Service = AppController.getRetrofitInstanceNode().create(WebInterface.class);
        Call<JsonObject> call = Service.MasterIndex(1,1,10);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String message=String.valueOf(response.body().get("message"));
                Boolean status=response.body().get("status").getAsBoolean();
                if(message.equals("110110")) {
//                    JsonArray data = response.body().getAsJsonArray("data");
                    logout();
                }
                else
                {
                    MasterIndex masterIndex=new MasterIndex();
                    masterIndex.setMessage(message);
                    masterIndex.setStatus(status);

                    Data data=new Gson().fromJson(response.body().getAsJsonObject("data"), Data.class);
                    masterIndex.setData(data);
                    BaseManager.saveMasterHitData(data, Kmasterhit);
                    try {
                        PackageInfo pInfo = getBaseContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                        verCode = pInfo.versionCode;
                        UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                        if (!AppController.getManager().getEmail().isEmpty()) {
                            if (userData != null) {
                                if (verCode >=Integer.parseInt(masterIndex.getData().getAndroidV())) {
                                    if (!userData.equals("")) {
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        i.putExtra("fromsplash", "1");
                                        i.putExtra("userdata", userData.getIs_fingerprint_enable());
                                        startActivity(i);
                                        finish();
                                        // startHomeActivity();
                                    }

                                    //startHomeActivity();
                                } else {
                                    builder.setMessage("Update your app").setTitle("alert");

                                    //Setting message manually and performing action on button click
                                    builder.setMessage("Your app version in less than current version")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                                            Uri.parse("https://play.google.com/store/apps/details?id=com.needyyy.app"));
                                                    startActivity(intent);

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                   finish();
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    //Setting the title manually
                                    alert.setTitle("Update your app");
                                    alert.show();
                                }

                            } else {
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                i.putExtra("fromsplash","2");
                                i.putExtra("userdata","");
                                startActivity(i);
                                finish();
                            }
                        }else {
                            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                            i.putExtra("fromsplash","2");
                            i.putExtra("userdata","");
                            startActivity(i);
                            finish();
                        }
//                            }
//                        }, 2000);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }
//                if (response.body().get("status").equals("false")) {
//
//                    BaseManager.saveMasterHitData(masterIndex.getData(), Kmasterhit);
//                    try {
//                        PackageInfo pInfo = getBaseContext().getPackageManager().getPackageInfo(getPackageName(), 0);
//                         verCode = pInfo.versionCode;
//                                UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
//                                if (!AppController.getManager().getEmail().isEmpty()) {
//                                    if (userData != null) {
//                                        if (verCode >=Integer.parseInt(masterIndex.getData().getAndroidV())) {
//                                            startHomeActivity();
//                                        } else {
//                                            builder.setMessage("Update your app").setTitle("alert");
//
//                                            //Setting message manually and performing action on button click
//                                            builder.setMessage("Your app version in less than current version")
//                                                    .setCancelable(false)
//                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            finish();
//                                                            Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
//                                                                    Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    })
//                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            //  Action for 'NO' Button
//                                                            dialog.cancel();
//                                                            Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
//                                                                    Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                            //Creating dialog box
//                                            AlertDialog alert = builder.create();
//                                            //Setting the title manually
//                                            alert.setTitle("Update your app");
//                                            alert.show();
//                                        }
//
//                                    } else {
//                                        startActivity(LoginActivity.getIntent(SplashActivity.this));
//                                        finish();
//                                    }
//                                }else {
//                                    startActivity(LoginActivity.getIntent(SplashActivity.this));
//                                    finish();
//                                }
////                            }
////                        }, 2000);
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                else
//                {
//                    if(masterIndex.getMessage().equals("110110"))
//                    {
//                        startActivity(LoginActivity.getIntent(SplashActivity.this));
//                        finish();
//                    }
//                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


            }
        });
    }

        private void generateFirebaseTocken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("SplashActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("tocken","====="+token);
                        AppController.getManager().setTocken(token);

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                ArrayList<String> _arPermission = new ArrayList<String>();
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != 0) {
                            _arPermission.add("" + grantResults[i]);
                        }
                    }
//                    if (_arPermission.size() == 0) {
                    delay();
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setTitle(getResources().getString(R.string.alert));
//                        builder.setCancelable(false);
//                        builder.setMessage(getResources().getString(R.string.location_permission));
//                        builder.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//
//                        builder.create().show();
//
//
//                          }
                }
            }
        }

    }

    public void delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
//                if (!AppController.getManager().getEmail().isEmpty()) {
//                    if (userData != null) {
//                        hitapi();
//                       // startHomeActivity();
//                    } else {
//                        startActivity(LoginActivity.getIntent(SplashActivity.this));
//                        finish();
//                    }
//                }else {
//                    startActivity(LoginActivity.getIntent(SplashActivity.this));
//                    finish();
//                }
                hitapi();
            }
        }, 2000);

    }

    private void startHomeActivity() {
        startActivity(HomeActivity.getIntent(SplashActivity.this));
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(status) {
            final LocationManager manager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                buildAlertMessageNoGps();
            else {
                status = false;
                checkPermission();
            }
        }
    }
    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions(this, permissionRequired)) {
                delay();
            }
        } else {
            delay();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.gpsnotenablemsg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        status=true;
                    }
                })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean checkPermissions(Context context, String[] permissions) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
//            }else {
//                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                    buildAlertMessageNoGps();
//                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void logout() {
        AppController.getManager().clearPreference();
        BaseManager.clearPreference();
        generateFirebaseTocken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fromsplash","2");
        intent.putExtra("userdata","");
        startActivity(intent);
        finish();

    }
//    private void login() {
//        QBUser qbUsers=new QBUser("isha2","12345678");
//
//        qbUsers.setFullName("isha2");
//        qbUsers.setLogin("isha2");
//        qbUsers.setPassword("12345678");
//
//        QBUsers.signIn(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
//            @Override
//            public void onSuccess(QBUser qbUser, Bundle bundle) {
//
//                //  saveUserData(qbUser);
//                Toast.makeText(getBaseContext(),"login sucess",Toast.LENGTH_SHORT).show();
//
////                Intent intent=new Intent(MainActivity.this,chatDialogActivity.class);
////                intent.putExtra("user","kaka");
////                intent.putExtra("password","12345678");
////                startActivity(intent);
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//
//
////                signup();
//                Toast.makeText(getBaseContext(),"login failed",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void saveUserData(QBUser qbUser) {
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
    }
//    private void intializeframework() {
//        QBSettings.getInstance().init(getBaseContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
//        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
//    }

    private void registersession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                //signup();
                Toast.makeText(SplashActivity.this,"session sucess",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SplashActivity.this,"session falied",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup()
    {
        QBUser qbUsers=new QBUser("isha2","12345678");
        qbUsers.setFullName("isha2");
        qbUsers.setLogin("isha2");
        qbUsers.setPassword("12345678");

        QBUsers.signUp(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                // saveUserData(qbUser);
                Toast.makeText(SplashActivity.this,"signup sucess",Toast.LENGTH_SHORT).show();
                // login();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SplashActivity.this,"signup failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Chat.groupchatwebrtc.util.QBResRequestExecutor;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;

import com.needyyy.app.Modules.Login.model.register.OtpMain;
import com.needyyy.app.Modules.Login.model.register.UserDataMain;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.mypage.model.masterindex.masterindex.MasterIndex;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WIFI_SERVICE;
import static com.needyyy.app.constants.Constants.KPostdata;
import static com.needyyy.app.constants.Constants.Kmasterhit;
import static com.needyyy.app.constants.Constants.kCurrentUser;
import static org.webrtc.ContextUtils.getApplicationContext;


public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private static final String APP_ID = "78030";
    private static final String AUTH_KEY = "af72FtfUNJAkyL9";
    private static final String AUTH_SECRET = "B4xkMzhbc8MVLt7";
    private static final String ACCOUNT_KEY = "7yvNe17TnjNUqDoPwfqp";
    private TextView tvSignup, tvSignin, tvForgetPassword;
    private String ip="12345678";
    private TextInputEditText edtEmail, edtPassword;
    ImageView pincode,fingerprint;
    protected QBResRequestExecutor requestExecutor;
    private QBUser userForSave;
    UserDataMain userDataMain;
    public SignInFragment() {

    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_sign_in);
    }

    @Override
    protected void initView(View mView) {
        tvSignup = mView.findViewById(R.id.tv_signup);
        tvSignin = mView.findViewById(R.id.tv_signin);
        edtEmail = mView.findViewById(R.id.edt_email);
       // pincode = mView.findViewById(R.id.pincode);
       // fingerprint = mView.findViewById(R.id.fingerPrint);
        edtPassword = mView.findViewById(R.id.edt_password);
        tvForgetPassword = mView.findViewById(R.id.tv_forgotpassword);

        requestExecutor = AppController.getInstance().getQbResRequestExecutor();
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).setTitle("Sign In");
        intializeframework();
        tvSignup.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
      //  pincode.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signup:
                ((LoginActivity) getActivity()).replaceFragment(SignUpFragment.newInstance());
                break;
            case R.id.tv_signin:
                checkValidation();
                break;
            case R.id.tv_forgotpassword:
                showforgetPasswordDialog();
                break;
           // case R.id.pincode:
               // ((LoginActivity) getActivity()).replaceFragment(CreatePinFragment.newInstance(3));
             //   break;
        }
    }

    private void checkValidation() {
        if (!edtEmail.getText().toString().isEmpty()) {
            if (CommonUtil.checkEmail(edtEmail.getText().toString())) {
                if ((edtPassword.getText().length() != 0)) {
                    if (edtPassword.getText().length() >= 8) {
                        loginApi();
                    } else {
                        snackBar(getContext().getResources().getString(R.string.entervalidpassword));
                    }
                } else {
                    snackBar(getContext().getResources().getString(R.string.enterpassword));
                }
            } else {
                snackBar(getContext().getResources().getString(R.string.entervalidemail));
            }
        } else {
            snackBar(getContext().getResources().getString(R.string.enteremail));
        }
    }
    String device_type = "1";

    private void loginApi() {
        if (CommonUtil.isConnectingToInternet(getContext())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<UserDataMain> call = Service.loginApi(AppController.getManager().getId(), 0,device_type, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(),Build.MANUFACTURER,Build.MODEL,Build.VERSION.RELEASE,ip);
            call.enqueue(new Callback<UserDataMain>() {
                @Override
                public void onResponse(Call<UserDataMain> call, Response<UserDataMain> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    userDataMain = response.body();
                    if (userDataMain.getStatus()) {
                        //  login();
                      //  signInCreatedUser(createUserWithEnteredData(), false);
                        // startSignUpNewUser(createUserWithEnteredData());
                        // loginToChat(result);
                        AppController.getManager().setId(userDataMain.getData().getId());
                        BaseManager.saveDataIntoPreferences(userDataMain.getData(), kCurrentUser);
                        AppController.getManager().setEmail(edtEmail.getText().toString().trim());
                        hit_api_for_masterhit();
                    } else {
                        snackBar(userDataMain.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<UserDataMain> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        } else {
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }

    private void hit_api_for_masterhit() {

            WebInterface Service = AppController.getRetrofitInstanceNode().create(WebInterface.class);
            Call<JsonObject> call = Service.MasterIndex(1,1,10);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    String message=String.valueOf(response.body().get("message"));
                    Boolean status=response.body().get("status").getAsBoolean();
                    if(message.equals("110110")) {
//                    JsonArray data = response.body().getAsJsonArray("data");

                    }
                    else
                    {
                        MasterIndex masterIndex=new MasterIndex();
                        masterIndex.setMessage(message);
                        masterIndex.setStatus(status);

                        Data data=new Gson().fromJson(response.body().getAsJsonObject("data"), Data.class);
                        masterIndex.setData(data);
                        BaseManager.saveMasterHitData(data, Kmasterhit);
                        startActivity( new Intent( getActivity(), HomeActivity.class));
//                            }
//                        }, 2000);

                    }
//
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {


                }
            });
        }



    private void showforgetPasswordDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_forgot_password);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);
        TextView tvSend = dialog.findViewById(R.id.tv_send);
        TextInputEditText etEmailPhone = dialog.findViewById(R.id.et_email_phone);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String emailText = etEmailPhone.getText().toString().trim();
                boolean validornot = isEmailValid(emailText);

                if (validornot) {
                    AppController.getManager().setEmail(emailText);
                    forgotPass(emailText);
//                } else {
//                    if (!AppController.getManager().getMobile().isEmpty()) {
//                        if (AppController.getManager().getMobile().equals(emailText)) {
//                            forgotPass(emailText);
//                        }
                } else {
                    if (isValidPhone(emailText)) {
                        AppController.getManager().setMobile(emailText);
                        forgotPass(emailText);
                    } else{
                        hideKeyboard(getActivity());
                        Toast.makeText(getActivity(), "Please enter valid email or mobile", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch(which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        String emailText = etEmailPhone.getText().toString().trim();
//                        boolean validornot = isEmailValid(emailText);
//
//                        if(validornot){
//                            AppController.getManager().setEmail(emailText);
//                            forgotPass(emailText);
//                        }else{
//                            if(!AppController.getManager().getMobile().isEmpty()){
//                                if(AppController.getManager().getMobile().equals(emailText)){
//                                    forgotPass(emailText);
//                                }
//                            }else{
//                                if(isValidPhone(emailText)){
//                                    AppController.getManager().setMobile(emailText);
//                                    forgotPass(emailText);
//                                }
//                            }
//                        }
//
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        break;
//                }
//            }
//        };

        dialog.show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }

    private void forgotPass(String emailText) {
        if (CommonUtil.isConnectingToInternet(getContext())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<OtpMain> call = Service.forgotPassword(emailText);
            call.enqueue(new Callback<OtpMain>() {
                @Override
                public void onResponse(Call<OtpMain> call, Response<OtpMain> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    OtpMain forgotPassword = response.body();
                    if (forgotPassword.getStatus()) {
                        AppController.getManager().setOtp(forgotPassword.getData().getOtp().toString());
                        Toast.makeText(getActivity(), "otp is : " + AppController.getManager().getOtp(), Toast.LENGTH_SHORT).show();
                        snackBar(forgotPassword.getMessage());
                        ((LoginActivity) getActivity()).replaceFragment(OTPFragment.newInstance(2, AppController.getManager().getOtp(), emailText));
                    } else {
//                        if (forgotPassword.getMessage().equals(110110)){
//                            ((HomeActivity)getActivity()).logout();
//                        }else{
                        snackBar(forgotPassword.getMessage());
//                        }
                    }
                }
                @Override
                public void onFailure(Call<OtpMain> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        } else {
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }



//    private void startSignUpNewUser(final QBUser newUser) {
//        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
//                    @Override
//                    public void onSuccess(QBUser result, Bundle params) {
//
//                        loginToChat(result);
//                    }
//
//                    @Override
//                    public void onError(QBResponseException e) {
//                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
//                            signInCreatedUser(newUser, false);
//                        } else {
//
//                            Toast.makeText(getContext(),R.string.sign_up_error, Toast.LENGTH_LONG);
//                        }
//                    }
//                }
//        );
//    }
//    private void loginToChat(final QBUser qbUser) {
//        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
//        startLoginService(qbUser);
//        saveUserData(qbUser);
//        userForSave = qbUser;
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        getActivity().finish();
//        cancelProgressDialog();
//    }
//    private void startLoginService(QBUser qbUser) {
//        Intent tempIntent = new Intent(getContext(), CallService.class);
//        PendingIntent pendingIntent = getActivity().createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
//        CallService.start(getContext(), qbUser, pendingIntent);
//    }

//    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
//        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
//            @Override
//            public void onSuccess(QBUser result, Bundle params) {
//                if (deleteCurrentUser) {
//                    removeAllUserData(result);
//                } else {
//                    //start your home activity here
//                    startActivity(new Intent(getActivity(), HomeActivity.class));
//                   // startOpponentsActivity();
//                }
//            }
//
//            @Override
//            public void onError(QBResponseException responseException) {
//
//                Toast.makeText(getContext(),R.string.sign_up_error, Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    private void removeAllUserData(final QBUser user) {
//        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid, Bundle bundle) {
//                UsersUtils.removeUserData(getActivity());
//                startSignUpNewUser(createUserWithEnteredData());
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//
//                Toast.makeText(getActivity(),R.string.sign_up_error, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//    private void startOpponentsActivity() {
//        startActivity( new Intent( getActivity(), HomeActivity.class));
//        //OpponentsActivity.start(getActivity(), false);
//
//    }
//    private QBUser createUserWithEnteredData() {
//        return createQBUserWithCurrentData(String.valueOf(userDataMain.getData().getName()),
//                String.valueOf("ashu"));
//    }
//    private void login() {
//        QBUser qbUsers=new QBUser("kaka4","12345678");
//        StringifyArrayList<String> userTags = new StringifyArrayList<>();
//        userTags.add("needy");
//        qbUsers.setFullName("kaka4");
//        qbUsers.setLogin("kaka4");
//        qbUsers.setPassword("12345678");
//        qbUsers.setTags(userTags);
//
//        QBUsers.signIn(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
//            @Override
//            public void onSuccess(QBUser qbUser, Bundle bundle) {
//                saveUserData(qbUser);
//                Toast.makeText(getActivity(),"login sucess",Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getActivity(),"login failed",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//   // private String getCurrentDeviceId() {
//        return Utils.generateDeviceId(getActivity());
//    }


//    private void saveUserData(QBUser qbUser) {
//        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
//        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
//        sharedPrefsHelper.saveQbUser(qbUser);
//    }


//    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
//        QBUser qbUser = null;
//        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
//            StringifyArrayList<String> userTags = new StringifyArrayList<>();
//            userTags.add(chatRoomName);
//
//            qbUser = new QBUser();
//            qbUser.setFullName(userName);
//            qbUser.setLogin(getCurrentDeviceId());
//            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
//            qbUser.setTags(userTags);
//        }
//
//        return qbUser;
//    }
    private void intializeframework() {
        QBSettings.getInstance().init(getActivity(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }
}

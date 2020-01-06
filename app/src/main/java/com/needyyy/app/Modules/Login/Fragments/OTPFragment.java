package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Chat.groupchatwebrtc.activities.OpponentsActivity;
import com.needyyy.app.Chat.groupchatwebrtc.services.CallService;
import com.needyyy.app.Chat.groupchatwebrtc.util.QBResRequestExecutor;
import com.needyyy.app.Chat.groupchatwebrtc.utils.Consts;
import com.needyyy.app.Chat.groupchatwebrtc.utils.QBEntityCallbackImpl;
import com.needyyy.app.Chat.groupchatwebrtc.utils.SharedPrefsHelper;
import com.needyyy.app.Chat.groupchatwebrtc.utils.UsersUtils;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.register.OtpMain;
import com.needyyy.app.Modules.Login.model.register.UserDataMain;
import com.needyyy.app.R;
import com.needyyy.app.fingerprint.FingureprintFragment;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.mypage.model.masterindex.masterindex.MasterIndex;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.views.AppEditText;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.model.QBUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.KPostdata;
import static com.needyyy.app.constants.Constants.Kmasterhit;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class OTPFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private static final String APP_ID = "78030";
    private static final String AUTH_KEY = "af72FtfUNJAkyL9";
    private static final String AUTH_SECRET = "B4xkMzhbc8MVLt7";
    private static final String ACCOUNT_KEY = "7yvNe17TnjNUqDoPwfqp";

    private TextView tvLogin,tvSignup;
    private AppEditText mPinFirstDigitEditText;
    private AppEditText mPinSecondDigitEditText;
    private AppEditText mPinThirdDigitEditText;
    private AppEditText mPinForthDigitEditText;
    private AppEditText mPinHiddenEditText;
    Button btnDone;
    UserDataMain userDataMain;
    private GPSTracker gpsTracker ;
    String username,email,mobile,password,dob,gender,location,ccode,isSocial,deviceTocken,deviceType,otp,otptext,latitude,longitute;
    int TYPE,index;
    private TextView resendotp;
    String ismobile;
    protected QBResRequestExecutor requestExecutor;
    private QBUser userForSave;
    private String quickbloxId="";
    public OTPFragment() {
        // Required empty public constructor

    }

    public static OTPFragment newInstance(int index,int TYPE , String username,
                                          String email,
                                          String mobile,
                                          String password,
                                          String dob,
                                          String gender,
                                          String location,
                                          String ccode,
                                          String isSocial,
                                          String deviceTocken,
                                          String deviceType,String otp,String latitute,String lonitude) {
        OTPFragment fragment = new OTPFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.INDEX, index);
        args.putString(Constant.USERNAME, username);
        args.putInt(Constant.Type, TYPE);
        args.putString(Constant.EMAIL, email);
        args.putString(Constant.Mobile, mobile);
        args.putString(Constant.Password, password);
        args.putString(Constant.Dob, dob);
        args.putString(Constant.GENDER, gender);
        args.putString(Constant.LOCATION, location);
        args.putString(Constant.CCODE, ccode);
        args.putString(Constant.ISSOCIAL, isSocial);
        args.putString(Constant.Device_Token, deviceTocken);
        args.putString(Constant.Device_Type, deviceType);
        args.putString(Constant.OTP, otp);
        args.putString("latitute",latitute);
        args.putString("longitude",lonitude);
        fragment.setArguments(args);
        return fragment;
    }

    public static OTPFragment newInstance(int TYPE , String otp , String etmail) {
        OTPFragment fragment = new OTPFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Type, TYPE);
//        args.putString(Constant.EMAIL, email);
//        args.putString(Constant.Mobile, mobile);
        args.putString(Constant.OTP, otp);
        args.putString(Constant.ISMOBILE, etmail);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_otp);
        requestExecutor = AppController.getInstance().getQbResRequestExecutor();
        if (getArguments() != null) {
            TYPE = getArguments().getInt(Constant.Type);
            if(TYPE == 1) {
                otp = getArguments().getString(Constant.OTP);
                index = getArguments().getInt(Constant.INDEX);
                username = getArguments().getString(Constant.USERNAME);
                email = getArguments().getString(Constant.EMAIL);
                mobile = getArguments().getString(Constant.Mobile);
                password = getArguments().getString(Constant.Password);
                dob = getArguments().getString(Constant.Dob);
                gender = getArguments().getString(Constant.GENDER);
                location = getArguments().getString(Constant.LOCATION);
                ccode = getArguments().getString(Constant.CCODE);
                isSocial = getArguments().getString(Constant.ISSOCIAL);
                deviceTocken = getArguments().getString(Constant.Device_Token);
                deviceType = getArguments().getString(Constant.Device_Type);
                latitude=getArguments().getString("latitute");
                longitute=getArguments().getString("longitude");
            }
            else if(TYPE == 2){
                otp = getArguments().getString(Constant.OTP);
//                email = getArguments().getString(Constant.EMAIL);
//                mobile = getArguments().getString(Constant.Mobile);
                ismobile = getArguments().getString(Constant.ISMOBILE);

            }
    }
    }

    @Override
    protected void initView(View mView) {

        mPinFirstDigitEditText  = mView.findViewById(R.id.edt_one);
        mPinSecondDigitEditText = mView.findViewById(R.id.edt_two);
        mPinThirdDigitEditText  = mView.findViewById(R.id.edt_three);
        mPinForthDigitEditText  = mView.findViewById(R.id.edt_four);
        mPinHiddenEditText      = mView.findViewById(R.id.pin_hidden_edittext);
        btnDone                 = mView.findViewById(R.id.btn_done);
        resendotp               = mView.findViewById(R.id.resendotp);


    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        intializeframework();
        ((LoginActivity)getActivity()).setTitle("Verification");
        btnDone.setVisibility(View.VISIBLE);
        gpsTracker = new GPSTracker(getActivity());
        setPINListeners();
        setFocus(mPinHiddenEditText);
        resendotp.setOnClickListener(this);
    }
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);
        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
        btnDone.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_login:
                ((LoginActivity)getActivity()).replaceFragment(SignInFragment.newInstance());
                break;
            case R.id.tv_signup:
                ((LoginActivity)getActivity()).replaceFragment(SignInFragment.newInstance());
                break;

            case R.id.btn_done:
                btnDone.setVisibility(View.GONE);
                otptext = mPinHiddenEditText.getText().toString().trim();
                if(otp != null) {
                    if (otp.equals(otptext)) {
                        if (TYPE == 1)
                            if (CommonUtil.isConnectingToInternet(getContext())){
                                signUp();

                            }else{
                                snackBar(getContext().getResources().getString(R.string.internetmsg));
                            }

                        else if (TYPE == 2) {
                            ((LoginActivity) getActivity()).replaceFragment(RenewPasswordFragment.newInstance(ismobile));
                        }
                    } else {
                        snackBar(getContext().getString(R.string.validotpmsg));
                    }
                }else if(AppController.getManager().getOtp() != null){
                    if (AppController.getManager().getOtp().equals(otptext)) {


                        if (TYPE == 1)
                            if (CommonUtil.isConnectingToInternet(getContext())){
                                signUp();
                            }else{
                                snackBar(getContext().getResources().getString(R.string.internetmsg));
                            }
                        else if (TYPE == 2) {
                            ((LoginActivity)getActivity()).popStack();
                            ((LoginActivity) getActivity()).replaceFragment(RenewPasswordFragment.newInstance(ismobile));
                        }
                    } else {
                        snackBar(getContext().getString(R.string.validotpmsg));
                    }
                }
                // ((LoginActivity)getActivity()).replaceFragment(CreatePinFragment.newInstance());
                break;
            case  R.id.resendotp :
                btnDone.setVisibility(View.VISIBLE);
                otptext="";
                mPinHiddenEditText.setText("");
                mPinFirstDigitEditText.setText("");
                mPinSecondDigitEditText.setText("");
                mPinThirdDigitEditText.setText("");
                mPinForthDigitEditText.setText("");
                if (TYPE == 1) {
                    SignUpApi();
                }
                else if (TYPE == 2){
                    forgotPass(ismobile);
                }

                break;
        }
    }


    private void SignUpApi() {

//        try {
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<OtpMain> call = Service.register(username, email,
                    mobile, password,dob,gender,location,latitude,longitute,"91","0",AppController.getManager().getTocken(),"1",Build.MANUFACTURER,Build.MODEL,Build.VERSION.RELEASE,"12345");
            call.enqueue(new Callback<OtpMain>() {
                @Override
                public void onResponse(Call<OtpMain> call, Response<OtpMain> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "1" + response.body().toString());
                    OtpMain otpMain = response.body();
                    if (otpMain.getStatus()) {
                        CommonUtil.showLongToast(getContext(), "otp=" + otpMain.getData().getOtp());
                        otp = String.valueOf(otpMain.getData().getOtp());
                    } else {

                        snackBar(otpMain.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<OtpMain> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
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
                       // Toast.makeText(getActivity(), "otp is : " + AppController.getManager().getOtp(), Toast.LENGTH_SHORT).show();
                        snackBar(forgotPassword.getMessage());
                        otp = forgotPassword.getData().getOtp().toString();
//                        ((LoginActivity) getActivity()).replaceFragment(OTPFragment.newInstance(2, AppController.getManager().getOtp(), emailText));
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

    private void signUp() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<UserDataMain> call = Service.registerApi(username,email,
                mobile, password,dob,gender,location,String.valueOf(gpsTracker.getLatitude()),String.valueOf(gpsTracker.getLongitude()),"91","0","1",otp,quickbloxId,Build.MANUFACTURER,Build.MODEL,Build.VERSION.RELEASE,"123456");
        call.enqueue(new Callback<UserDataMain>() {
            @Override
            public void onResponse(Call<UserDataMain> call, Response<UserDataMain> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                 userDataMain = response.body();
                if (userDataMain.getStatus()) {
                    BaseManager.saveDataIntoPreferences(userDataMain.getData(),kCurrentUser);
                    AppController.getManager().setMobile(mobile);
                    AppController.getManager().setEmail(email);
                    AppController.getManager().setUser_name(username);
                    AppController.getManager().setId(userDataMain.getData().getId());
                    //startSignUpNewUser(createUserWithEnteredData());
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
                    FingureprintFragment fingureprintFragment=new FingureprintFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("fragment","0");
                    fingureprintFragment.setArguments(bundle);
                    ((LoginActivity)getActivity()).replaceFragment(fingureprintFragment);
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

    public void ChangeBackground(AppEditText ET, int set) {
        if (set == 1)
            ET.setBackgroundResource(R.drawable.otp_btn_selected);
        else if (set == 2)
            ET.setBackgroundResource(R.drawable.otp_btn_default);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if (s.length() == 0) {
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            ChangeBackground(mPinFirstDigitEditText, 1);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            ChangeBackground(mPinSecondDigitEditText, 1);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            ChangeBackground(mPinThirdDigitEditText, 1);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            ChangeBackground(mPinForthDigitEditText, 1);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
        }
    }
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        final int id = view.getId();
        switch (id) {
            case R.id.edt_one:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.edt_two:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.edt_three:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.edt_four:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            default:
                break;

        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = view.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (i == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4) {
                            ChangeBackground(mPinForthDigitEditText, 2);
                            mPinForthDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 3) {
                            ChangeBackground(mPinThirdDigitEditText, 2);

                            mPinThirdDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 2) {
                            ChangeBackground(mPinSecondDigitEditText, 2);

                            mPinSecondDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 1) {
                            ChangeBackground(mPinFirstDigitEditText, 2);
                            mPinFirstDigitEditText.setText("");
                        }
//
//                        if (mPinHiddenEditText.length() > 0)
//                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
//                        return true;
                    }
                    break;

                default:
                    return false;
            }
        }
        return false;
    }

    // for vedio audio chat

    private void startSignUpNewUser(final QBUser newUser) {
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {

                        quickbloxId=result.getId().toString();
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser, false);
                        } else {

                            Toast.makeText(getContext(),R.string.sign_up_error, Toast.LENGTH_LONG);
                        }
                    }
                }
        );
    }


    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
        startLoginService(qbUser);
        saveUserData(qbUser);
        userForSave = qbUser;

//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        getActivity().finish();
    }
    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
                    removeAllUserData(result);
                } else {
                   // startOpponentsActivity();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {

                Toast.makeText(getContext(),R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getActivity());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {

                Toast.makeText(getActivity(),R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startOpponentsActivity() {
        OpponentsActivity.start(getActivity(), false);

    }

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(userDataMain.getData().getId(),
                String.valueOf("needy"));
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
    }


    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);
            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(userName);
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }
    private void intializeframework() {
        QBSettings.getInstance().init(getActivity(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(getContext(), CallService.class);
        PendingIntent pendingIntent = getActivity().createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(getContext(), qbUser, pendingIntent);
    }
    private String getCurrentDeviceId() {
        return Utils.generateDeviceId(getActivity());
    }

}


package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.register.OtpMain;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.utils.GooglePlaceAutocompleteAdapter;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WIFI_SERVICE;
import static org.webrtc.ContextUtils.getApplicationContext;

public class SignUpFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private TextView tvLogin,tvSignup;
    private TextInputEditText edtUserName,edtMobile,edtEmail,edtPassword,edtDob,et_gender;
    private TextInputLayout textinputUser,textinputEmail,textinputMobile,textinputPassword,textinputDob;
    private GPSTracker gpsTracker ;
    AutoCompleteTextView  edtCity ;

    //    Spinner edtGender ;
    String gender ;

    public SignUpFragment() {
        // Required empty public constructor

    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.fragment_sign_up);
    }

    @Override
    protected void initView(View mView) {
        tvLogin            = mView.findViewById(R.id.tv_login);
        tvSignup           = mView.findViewById(R.id.tv_signup);
        edtUserName        = mView.findViewById(R.id.edt_username);
        edtMobile          = mView.findViewById(R.id.edt_mobile);
        edtEmail           = mView.findViewById(R.id.edt_email);
        edtPassword        = mView.findViewById(R.id.edt_password);
        edtDob             = mView.findViewById(R.id.edt_dob);
        edtCity            = mView.findViewById(R.id.edt_current_city);
        textinputUser      = mView.findViewById(R.id.til_username);
        textinputEmail     = mView.findViewById(R.id.til_email);
        textinputMobile    = mView.findViewById(R.id.til_mobile);
        textinputPassword  = mView.findViewById(R.id.til_password);
        textinputDob       = mView.findViewById(R.id.til_dob);
        et_gender          = mView.findViewById(R.id.et_gender);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

        ((LoginActivity)getActivity()).setTitle("Sign Up");
        gpsTracker = new GPSTracker(getActivity());
        tvLogin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        edtDob.setOnClickListener(this);
        et_gender.setOnClickListener(this);

        edtCity.setAdapter(new GooglePlaceAutocompleteAdapter(getActivity(), R.layout.place_textview_layout));
        edtCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String str = (String) adapterView.getItemAtPosition(i);
                Log.e("onItemClick", str.toString());

        }
        });



//        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getContext(),R.layout.gender_spinner_row,R.id.gender,getResources().getStringArray(R.array.gender_array));
//        edtGender.setAdapter(genderAdapter);
//        edtGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                gender = String.valueOf(edtGender.getSelectedItemId());
//                if (position==0){
//                    edtGender.setBackground(getResources().getDrawable(R.drawable.edt_unselected_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        String reqString = Build.MANUFACTURER
//                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
//                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_login:
                ((LoginActivity)getActivity()).replaceFragment(SignInFragment.newInstance());
                break;
            case R.id.tv_signup:
                checkValidation();
                break;
            case R.id.edt_dob:
                CommonUtil.DobDatePicker(getActivity(),edtDob,SignUpFragment.this);
                break;

            case R.id.et_gender:
                selectGender(et_gender);
                break;
        }
    }

    private void checkValidation() {
        if (!edtUserName.getText().toString().isEmpty()){
            if (!edtMobile.getText().toString().isEmpty()){
                if ((edtMobile.getText().toString().length()>=10)){
                    if (!edtEmail.getText().toString().isEmpty()){
                        if (CommonUtil.checkEmail(edtEmail.getText().toString())){
                            if (!edtPassword.getText().toString().isEmpty()){
                                if (edtPassword.getText().toString().length()>=8){
                                    if (!edtDob.getText().toString().isEmpty()){
                                        if (CommonUtil.getAge(edtDob.getText().toString()) > 18){
                                            if (!et_gender.getText().toString().isEmpty()){
                                                if (!edtCity.getText().toString().isEmpty()){
                                                    SignUpApi();
                                                }else{
                                                    snackBar("please enter city");
                                                }
                                            }else{
                                                snackBar("please enter gender");
                                            }
                                        }else{
                                            snackBar("you aren't 18 years old");
                                        }
                                    }else{
                                        snackBar("please enter dob");
                                    }
                                }else{
                                    snackBar("passwords must contain at least eight characters");
                                }
                            }else{
                                snackBar("please enter password");
                            }
                        }else{
                            snackBar("please enter valid email");
                        }
                    }else{
                        snackBar("please enter Email");
                    }
                }else{
                    snackBar("please enter valid mobile number");
                }
            }else{
                snackBar("please enter Mobile number");
            }
        }else{
            snackBar("please enter user name");
        }
    }

    private void SignUpApi() {
//        try {
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
//        } catch (Exception e) {
//            e.printStackTrace();

        if (et_gender.getText().toString().trim().equalsIgnoreCase(getString(R.string.male))) {
            gender = "0";
        } else {
            gender = "1";
        }
//        }
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<OtpMain> call = Service.register(edtUserName.getText().toString().trim(), edtEmail.getText().toString().trim(),
                    edtMobile.getText().toString().trim(), edtPassword.getText().toString().trim(), edtDob.getText().toString(),gender,edtCity.getText().toString().replace("/","-"),String.valueOf(gpsTracker.getLatitude()),String.valueOf(gpsTracker.getLongitude()),"91","0",AppController.getManager().getTocken(),"1",Build.MANUFACTURER,Build.MODEL,Build.VERSION.RELEASE,"123456");
            call.enqueue(new Callback<OtpMain>() {
                @Override
                public void onResponse(Call<OtpMain> call, Response<OtpMain> response) {
                    cancelProgressDialog();
                    //registersession();
                    Log.e("dssfsfssf", "1" + response.body().toString());
                    OtpMain otpMain = response.body();
                    if (otpMain.getStatus()) {
                        CommonUtil.showLongToast(getContext(), "otp=" + otpMain.getData().getOtp());
                        ((LoginActivity) getActivity()).replaceFragment(OTPFragment.newInstance(1,1,edtUserName.getText().toString().trim(), edtEmail.getText().toString().trim(),
                                edtMobile.getText().toString().trim(), edtPassword.getText().toString().trim(), edtDob.getText().toString(),gender,edtCity.getText().toString(),"91","1","ssfscs","fsffss", String.valueOf(otpMain.getData().getOtp()),String.valueOf(gpsTracker.getLatitude()),String.valueOf(gpsTracker.getLongitude())));
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
        }
        else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @SuppressLint("RestrictedApi")
    private void selectGender(View view) {
        MenuBuilder menuBuilder = new MenuBuilder(getActivity());
        MenuInflater inflater = new MenuInflater(getActivity());
        inflater.inflate(R.menu.gender_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(getActivity(), menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                et_gender.setText(item.getTitle());
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        optionsMenu.show();
    }
    private void registersession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                //signup();
                Toast.makeText(getContext(),"session sucess",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getContext(),"session falied",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup()
    {
        QBUser qbUsers=new QBUser("kaka","12345678");

        StringifyArrayList<String> userTags = new StringifyArrayList<>();
        userTags.add("needy");

        qbUsers.setFullName("kaka");
        qbUsers.setLogin("kaka");
        qbUsers.setPassword("12345678");
        qbUsers.setTags(userTags);

        QBUsers.signUp(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(getContext(),"signup sucess",Toast.LENGTH_SHORT).show();
               // login();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getContext(),"signup failed",Toast.LENGTH_SHORT).show();
            }
        });
    }



}


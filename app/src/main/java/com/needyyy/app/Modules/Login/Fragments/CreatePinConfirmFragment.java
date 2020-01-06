package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Login.model.setPin.SetPinBase;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.otpview.OnOtpCompletionListener;
import com.needyyy.app.views.otpview.OtpView;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.kCurrentUser;


public class CreatePinConfirmFragment extends BaseFragment implements View.OnClickListener , OnOtpCompletionListener {

    private Button validateButton;
    private OtpView otpView;
    private TextView tvTitle ;
    private String code ;
    int index ;
    private Button btnDone;
    private String fingurestatus;
    public CreatePinConfirmFragment() {
        // Required empty public constructor
    }

    public static CreatePinConfirmFragment newInstance(String code, int index,String fingurestatus) {
        CreatePinConfirmFragment fragment = new CreatePinConfirmFragment();
        Bundle args = new Bundle();
        args.putString(Constant.CODE,code);
        args.putString("fingurestatus",fingurestatus);
        args.putInt(Constant.INDEX,index);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_pin);
        if(getArguments()!=null){
            code = getArguments().getString(Constant.CODE);
            index = getArguments().getInt(Constant.INDEX);
            fingurestatus=getArguments().getString("fingurestatus");
        }
    }

    @Override
    protected void initView(View mView) {
        otpView = mView.findViewById(R.id.otp_view);
        tvTitle = mView.findViewById(R.id.tv_title);
        btnDone = mView.findViewById(R.id.btn_done);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        if(index==1)
            ((LoginActivity)getActivity()).setTitle("Pincode");
        else{
            ((HomeActivity)getActivity()).manageToolbar("Pincode", "");
        }
        tvTitle.setText(getContext().getResources().getString(R.string.confirmcode));
//        llCreatePin.setOnClickListener(this);
//        llCreateFingerPrint.setOnClickListener(this);
        otpView.setOtpCompletionListener(this);
        otpView.setFocusable(true);
        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_done:
                validatecode();
                break;
        }
    }

    private void validatecode() {
        if (code.equals(otpView.getText().toString())){
            setPinCode();
        }else{
            snackBar(getContext().getResources().getString(R.string.pinnotmatch));
        }
    }

    private void setPinCode() {
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            UserDataResult userData =(BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<SetPinBase> call = Service.setPin(code, AppController.getManager().getId(),fingurestatus);
            call.enqueue(new Callback<SetPinBase>() {
                @Override
                public void onResponse(Call<SetPinBase> call, Response<SetPinBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    SetPinBase setPinBase = response.body();
                    if (setPinBase.getStatus()) {
                        userData.setPin(setPinBase.getData().getPin());
                        userData.setIs_fingerprint_enable(fingurestatus);
                        BaseManager.saveDataIntoPreferences(userData,kCurrentUser);
                        if (index==1){
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{

                            ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
                        }

                    } else {
//                        if (setPinBase.getMessage().equals(110110)){
//                            ((HomeActivity)getActivity()).logout();
//                        }else{
//                            snackBar(setPinBase.getMessage());
//                        }
                        snackBar(setPinBase.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SetPinBase> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }

    @Override
    public void onOtpCompleted(String otp) {
//        Toast.makeText(getActivity(), "OnOtpCompletionListener called", Toast.LENGTH_SHORT).show();

    }
}

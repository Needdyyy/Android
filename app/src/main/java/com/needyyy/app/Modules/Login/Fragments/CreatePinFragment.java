package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.otpview.OnOtpCompletionListener;
import com.needyyy.app.views.otpview.OtpView;

import static com.needyyy.app.constants.Constants.kCurrentUser;


public class CreatePinFragment extends BaseFragment implements View.OnClickListener , OnOtpCompletionListener {

    private Button btnDone;
    private OtpView otpView;
    int index ;
    private TextView tvTitle ;
    UserDataResult userData ;
    private Boolean isVerify = false ;
    private String fingurestatus;
    public CreatePinFragment() {
        // Required empty public constructor
    }

    public static CreatePinFragment newInstance(int index,String fingurestatus) {
        CreatePinFragment fragment = new CreatePinFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.INDEX,index);
        args.putString("fingurestatus",fingurestatus);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_pin);
        if(getArguments()!=null){
            index = getArguments().getInt(Constant.INDEX);
            fingurestatus=getArguments().getString("fingurestatus");
        }
    }

    @Override
    protected void initView(View mView) {
        otpView  = mView.findViewById(R.id.otp_view);
        btnDone  = mView.findViewById(R.id.btn_done);
        tvTitle = mView.findViewById(R.id.tv_title);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        if (index==1){
            ((LoginActivity)getActivity()).setTitle("Pincode");
            tvTitle.setText(getContext().getResources().getString(R.string.newcode));
        }
        else if (index==2){
            ((HomeActivity)getActivity()).manageToolbar("Pincode", "2");
            tvTitle.setText("enter your pin");
        }

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
                if (otpView.getText().toString().trim().length()==4){
                    if (index==1)
                        ((LoginActivity)getActivity()).replaceFragment(CreatePinConfirmFragment.newInstance(otpView.getText().toString(), index,fingurestatus));
                    else if(index==2){
                        if (isVerify){
                            ((HomeActivity)getActivity()).replaceFragment(CreatePinConfirmFragment.newInstance(otpView.getText().toString(),index,""), true);
                        }else{
                            if (otpView.getText().toString().trim().equals(userData.getPin())){
                                otpView.setText("");
                                tvTitle.setText(getContext().getResources().getString(R.string.newcode));
                                isVerify=true;
                            }else{
                                isVerify=false;
                                snackBar("enter your current pin");
                            }
                        }
                    }
                }else{
                    snackBar("enter 4 digit pin");
                }

                break;

        }
    }

    @Override
    public void onOtpCompleted(String otp) {
//        Toast.makeText(getActivity(), "OnOtpCompletionListener called", Toast.LENGTH_SHORT).show();

    }
}

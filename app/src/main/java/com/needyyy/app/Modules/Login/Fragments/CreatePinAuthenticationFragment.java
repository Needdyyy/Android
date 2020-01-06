package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.R;


public class CreatePinAuthenticationFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvSignup ,tvSignin,tvForgetPassword;
    private LinearLayout llCreatePin,llCreateFingerPrint;
    public CreatePinAuthenticationFragment() {
        // Required empty public constructor
    }

    public static CreatePinAuthenticationFragment newInstance() {
        CreatePinAuthenticationFragment fragment = new CreatePinAuthenticationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_pincode);
    }

    @Override
    protected void initView(View mView) {
        llCreatePin         = mView.findViewById(R.id.ll_createPin);
        llCreateFingerPrint = mView.findViewById(R.id.ll_create_FingerPrint);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).setTitle("Pincode");

        llCreatePin.setOnClickListener(this);
        llCreateFingerPrint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_createPin:
                ((LoginActivity)getActivity()).replaceFragment(CreatePinFragment.newInstance(1,""));
                break;
            case R.id.ll_create_FingerPrint:
                Intent intent = new Intent(getActivity(),CreateFingerPrintAuthenticationFragment.class);
                startActivity(intent);
                getActivity().finish();
                break;

        }
    }

}

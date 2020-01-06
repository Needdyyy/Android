package com.needyyy.app.fingerprint;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.Activities.SplashActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.views.otpview.OnOtpCompletionListener;
import com.needyyy.app.views.otpview.OtpView;

import static com.needyyy.app.constants.Constants.kCurrentUser;
import static com.needyyy.app.utils.CommonUtil.generateFirebaseTocken;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinCheck extends Fragment implements View.OnClickListener , OnOtpCompletionListener {
    private Button btnDone;
    private OtpView otpView;
    int index ;
    private TextView tvTitle,forgotpin ;
    private UserDataResult userData ;
    private String pin;
    public PinCheck() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otpView = view.findViewById(R.id.otp_view);
        tvTitle = view.findViewById(R.id.tv_title);
        btnDone = view.findViewById(R.id.btn_done);
        forgotpin=view.findViewById(R.id.forgotpin);
        btnDone.setOnClickListener(this);
        forgotpin.setOnClickListener(this);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        if(getActivity() instanceof LoginActivity)
        {
            ((LoginActivity) getActivity()).setTitle("PIN");
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_done:
                validatecode();
                break;
            case R.id.forgotpin:
                logout();
                break;
        }
    }


        public void logout() {
            AppController.getManager().clearPreference();
            BaseManager.clearPreference();
            generateFirebaseTocken();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("fromsplash","2");
            intent.putExtra("userdata","");
            startActivity(intent);
            getActivity().finish();

    }

    private void validatecode() {
        pin=userData.getPin();
        if(pin.equals(otpView.getText().toString()))
        {
            Toast.makeText(getContext(),"Authentication Success",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),HomeActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        else
        {
            Toast.makeText(getContext(),"Authentication failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOtpCompleted(String otp) {

    }
}

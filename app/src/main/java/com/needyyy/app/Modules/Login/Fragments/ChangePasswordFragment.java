package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private TextInputEditText etCurrentPassword,etNewPassword,etConfirmPassword;
    private TextView tvDone;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_change_password);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {
        etCurrentPassword  = mView.findViewById(R.id.et_currentpassword);
        etNewPassword      = mView.findViewById(R.id.et_newpassword);
        etConfirmPassword  = mView.findViewById(R.id.et_reenterpassword);
        tvDone             = mView.findViewById(R.id.btn_done);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).setTitle("Change Password");
        tvDone.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_done:
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        if (!etCurrentPassword.getText().toString().isEmpty()){
            if (!etNewPassword.getText().toString().isEmpty()){
                if (!etConfirmPassword.getText().toString().isEmpty()){
                    if (etNewPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())){
                        changePasswordApi();
                    }else{
                        snackBar("Password and confirm password does not match");
                    }
                }else{
                    snackBar("ReEnter Password field can't be empty");
                }
            }else{
                snackBar("New Password field can't be empty");
            }
        }else{
            snackBar("Current Password field can't be empty");
        }
    }

    private void changePasswordApi() {
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.changePassword(etNewPassword.getText().toString().trim(),etCurrentPassword.getText().toString().trim());
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo commonPojo = response.body();
                    if (commonPojo.getStatus()) {
                        snackBar(commonPojo.getMessage());
                        ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
                    } else {

                        if (commonPojo.getMessage().equals(110110)){
                            ((HomeActivity)getActivity()).logout();
                        }else{
                            snackBar(commonPojo.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonPojo> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }
}

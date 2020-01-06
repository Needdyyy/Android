package com.needyyy.app.Modules.Login.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.forgotPassword.ForgotPassword;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenewPasswordFragment extends BaseFragment implements View.OnClickListener {

    EditText etPassword, etRePassword;
    TextView tvDone;
    String etmail;
    int index ;
    public RenewPasswordFragment() {
        // Required empty public constructor
    }


    public static RenewPasswordFragment newInstance(String etmail) {
        RenewPasswordFragment fragment = new RenewPasswordFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ISMOBILE, etmail);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_renew_password);
        if (getArguments() != null) {
            etmail = getArguments().getString(Constant.ISMOBILE);
        }
    }


    @Override
    protected void initView(View mView) {
        etPassword = mView.findViewById(R.id.et_password);
        etRePassword = mView.findViewById(R.id.et_re_password);
        tvDone = mView.findViewById(R.id.tv_done);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).setTitle("Change Password");
        tvDone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_done:
                if(etPassword.getText().toString().equals(etRePassword.getText().toString())){
                    forgotPass(etPassword.getText().toString());
                }else{
                    Toast.makeText(getActivity(), "Password didn't match", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void forgotPass(String emailText) {
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<ForgotPassword> call = Service.otpPassword(AppController.getManager().getOtp(), etmail, emailText);
            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    ForgotPassword forgotPassword = response.body();
                    if (forgotPassword.getStatus()) {
                        snackBar(forgotPassword.getMessage());
//                        Intent intent =new Intent(getActivity(), HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        getActivity().finish();
                        ((LoginActivity)getActivity()).replaceFragment(SignInFragment.newInstance());
                    } else {

//                        if (forgotPassword.getMessage().equals(110110)){
//                            ((HomeActivity)getActivity()).logout();
//                        }else{
//                            snackBar(forgotPassword.getMessage());
//                        }
                        snackBar(forgotPassword.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }
}

package com.needyyy.app.Modules.Home.Fragments;

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
import com.needyyy.app.R;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends BaseFragment implements View.OnClickListener {

    private TextInputEditText etSubject,etMessage;
    private TextView tvSubmit;
    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_contact_us);

    }

    @Override
    protected void initView(View mView) {
        etSubject  = mView.findViewById(R.id.et_subject);
        etMessage  = mView.findViewById(R.id.et_message);
        tvSubmit   = mView.findViewById(R.id.tv_submit);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.helpandsupport), "2");
        tvSubmit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                if (!etSubject.getText().toString().isEmpty()){
                    if (!etMessage.getText().toString().isEmpty()){
                        helpAndSuppotApi();
                    }else{
                        snackBar("Enter the Message");
                    }
                }else{
                    snackBar("Enter the Subject");
                }

                break;
        }
    }

    private void helpAndSuppotApi() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.helpAndSupport(etSubject.getText().toString(),etMessage.getText().toString());
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo commonPojo = response.body();
                if (commonPojo.getStatus()) {
                    snackBar(commonPojo.getMessage());
                    etSubject.setText("");
                    etMessage.setText("");
                } else {
                    if (commonPojo.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();
                    }else{
                        snackBar(commonPojo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                cancelProgressDialog();
            }
        });

    }



}

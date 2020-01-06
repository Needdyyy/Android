package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.adsAndPage.fragment.PaymentActivity;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.AppTextView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class WalletFragment extends BaseFragment implements View.OnClickListener {
    private AppTextView tv_available_balance,tv_five_hundred,tv_one_thousand,tv_fifteen_hundred,btnDone;

    private EditText edt_price ;
    private String amount,txn_id,status;
    private String orderId = "";
    private final  int PAYMENTREQUESTCODE=100;
    public WalletFragment() {
        // Required empty public constructor
    }


    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_wallet);

    }

    @Override
    protected void initView(View mView) {
        tv_available_balance = mView.findViewById(R.id.tv_available_balance);
        btnDone = mView.findViewById(R.id.btn_addmoney);
        edt_price = mView.findViewById(R.id.edt_price);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).manageToolbar(getString(R.string.add_money), "2");
        tv_available_balance .setOnClickListener(this);
        btnDone .setOnClickListener(this);
        setWalletBalance();

    }

    private void setWalletBalance() {
        UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        tv_available_balance.setText(userData.getWalletBal());
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_available_balance:

                break ;
            case R.id.btn_addmoney:
                addMoneyToWallet();
//                if (edt_price.getText().toString().isEmpty()) {
//                    CommonUtil.showLongToast(getContext(), getContext().getResources().getString(R.string.enterprice));
//                }else{
//                    amount = edt_price.getText().toString();
//                    txn_id ="txn_123";
//                    status ="1";
//                    addMoneyToWalletApi();
//                }

                break ;
        }
    }

    private void addMoneyToWallet() {
        if (edt_price.getText().toString().isEmpty()) {
            CommonUtil.showLongToast(getContext(), getContext().getResources().getString(R.string.enterprice));
        }else{
            amount = edt_price.getText().toString();
            goPayment();
        }
    }

    private void goPayment() {

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(Constants.WALLET,Integer.parseInt(edt_price.getText().toString()));
        startActivityForResult(intent,PAYMENTREQUESTCODE);
        // ((HomeActivity)getActivity()).replaceFragment(PaymentActivity.newInstance(Integer.parseInt(edt_price.getText().toString())));
    }



    private void addMoneyToWalletApi() {
        showProgressDialog();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == PAYMENTREQUESTCODE ) {
            if (data!=null){
                String mode ,status,txn_id,txn_amount,order_id;
                mode       = (String) data.getExtras().get(Constant.MODE);
                txn_amount = (String) data.getExtras().get(Constant.FINAL_AMOUNT);
                txn_id     = (String) data.getExtras().get(Constant.TXNID);
                status     = (String) data.getExtras().get(Constant.STATUS);
                setWalletBalance();
                edt_price.setText("");
            }

        }
    }

}


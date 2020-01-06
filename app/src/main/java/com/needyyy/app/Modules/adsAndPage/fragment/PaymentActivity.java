package com.needyyy.app.Modules.adsAndPage.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.adsAndPage.modle.GetCheckSumHash;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.PaymentPojo;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PaymentResultListener {

    TextView tvPaytm, tvRazorPay;
    float subtotal, discount, tax, total;
    String status,txn_id,txn_amount,order_id,response,date;
    private String orderId = "",checkOutIds="";
    int addWalletBalance ;
    private final  int PAYMENTREQUESTCODE=100;
    public PaymentActivity() {
        // Required empty public constructor
    }

//    public static PaymentActivity newInstance(int balance) {
//        PaymentActivity fragment = new PaymentActivity();
//        Bundle args = new Bundle();
//        args.putInt(Constants.WALLET,balance);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment);
        if (getIntent().getExtras()!=null) {
            addWalletBalance = getIntent().getExtras().getInt(Constants.WALLET);;
        }
        initView();
        bindControls();
    }

    protected void initView() {
        tvPaytm = findViewById(R.id.tv_paytm);
        tvRazorPay = findViewById(R.id.tv_razorpay);
    }

    protected void bindControls() {
        tvPaytm.setOnClickListener(this);
        tvRazorPay.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_paytm:
                paytmCheckSum();
                break;
            case R.id.tv_razorpay:
                startPayment();
                break;
        }

    }

    public  void paytmCheckSum(){
        orderId = UUID.randomUUID().toString().replace("-", "");
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetCheckSumHash> call = Service.getCheckSum(Constant.PAYTM_MID,orderId,
                AppController.getManager().getId(),
                Constant.INDUSTRYTYPE_ID,
                Constant.CHANNELID,
                Constant.PAYTM_WEBSITE,
                String.valueOf(addWalletBalance),
                "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
        call.enqueue(new Callback<GetCheckSumHash>() {
            @Override
            public void onResponse(Call<GetCheckSumHash> call, Response<GetCheckSumHash> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());

                GetCheckSumHash checkSumPojo = response.body();
//                checkSumPojo=new Gson().fromJson(response.body().getCHECKSUMHASH()getJSONObject(Constant.RESULT).toString(),CheckSumPojo.class);
                AppController.getManager().setCHECKSUMHASH(checkSumPojo.getData().getCHECKSUMHASH());
                makePaytmTransaction();
//                CheckSumPojo createPageModel = response.body();
//                if (createPageModel.getStatus()) {
//                    pageData = createPageModel.getData();
//                    showCustomDialog();
//                } else {
//                    if (createPageModel.getMessage().equals("110110")){
//                        ((HomeActivity)getActivity()).logout();
//
//                    }else{
//                        snackBar(createPageModel.getMessage());
//                    }
//                    snackBar(createPageModel.getMessage());
//                }
            }
            @Override
            public void onFailure(Call<GetCheckSumHash> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage(),findViewById(R.id.item_view));
            }
        });
    }

    private void makePaytmTransaction() {

        // this is for local/Staging services
        PaytmPGService Service = PaytmPGService.getStagingService();

        // this is for live/production services
//        PaytmPGService Service = PaytmPGService.getProductionService();
        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Constant.MID, Constant.PAYTM_MID);
        paramMap.put(Constant.ORDER_ID, orderId);
        paramMap.put(Constant.CUST_ID, AppController.getManager().getId());
        paramMap.put(Constant.INDUSTRY_TYPE_ID, Constant.INDUSTRYTYPE_ID);
        paramMap.put(Constant.CHANNEL_ID, Constant.CHANNELID);
        paramMap.put(Constant.TXN_AMOUNT, String.valueOf(addWalletBalance));
        paramMap.put(Constant.WEBSITE, Constant.PAYTM_WEBSITE);
        paramMap.put(Constant.CALLBACK_URL, "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
//        paramMap.put(Constant.CALLBACK_URL, "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
        paramMap.put(Constant.CHECKSUMHASH, AppController.getManager().getCHECKSUMHASH());
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                        Log.e("SOMEUI ERROR", inErrorMessage);
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);


                        switch (inResponse.getString("STATUS")) {
                            case "TXN_FAILURE":
                                status     = "3";
                                txn_id     = "";
                                txn_amount = inResponse.getString(Constant.TXNAMOUNT);
                                order_id   = inResponse.getString(Constant.ORDERID);
                                response   = "";
                                date       = "";
                                paymentApi(2);
                                if (!inResponse.getString("RESPMSG").equals(""))
                                    snackBar(inResponse.getString("RESPMSG"),findViewById(R.id.item_view));
                                break;
                            case "TXN_SUCCESS":
                                status     = "1";
                                txn_id     = inResponse.getString(Constant.TXN_ID);
                                txn_amount = inResponse.getString(Constant.TXNAMOUNT);
                                order_id   = inResponse.getString(Constant.ORDERID);
                                response   = inResponse.getString(Constant.RESP_MSG);
                                date       = inResponse.getString(Constant.TXN_DATE);
//                                mode       = 2;
                                paymentApi(2);
                                snackBar(inResponse.getString("RESPMSG"),findViewById(R.id.item_view));
                                break;
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        Log.e("clientAuthentication", inErrorMessage);
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Log.e("onErrorLoadingWebPage", inErrorMessage + "" + iniErrorCode);
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        snackBar("Payment Transaction Failed ",findViewById(R.id.item_view));
                    }
                });
    }

    private void paymentApi(int mode) {
        showProgressDialog();
        if (CommonUtil.isConnectingToInternet(this)) {
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PaymentPojo> call = Service.payment(mode ,txn_amount,txn_id,status);
            call.enqueue(new Callback<PaymentPojo>() {
                @Override
                public void onResponse(Call<PaymentPojo> call, Response<PaymentPojo> response) {
                    cancelProgressDialog();
                    if (response.body() != null) {
                        PaymentPojo cartDetailpojo = response.body();
                        if (cartDetailpojo.getStatus()) {
                            if (status.equals("1")){
                                UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                                userData.setWalletBal(String.valueOf(Integer.parseInt(userData.getWalletBal())+addWalletBalance));
                                BaseManager.saveDataIntoPreferences(userData,kCurrentUser);
                                Intent intent=new Intent();
                                intent.putExtra(Constant.MODE,mode);
                                intent.putExtra(Constant.FINAL_AMOUNT,txn_amount);
                                intent.putExtra(Constant.TXNID,txn_id);
                                intent.putExtra(Constant.STATUS,status);
                                intent.putExtra(Constant.ORDER_ID,order_id);
                                setResult(PAYMENTREQUESTCODE,intent);
                                finish();//finishing activity
                            }
//                            showDialogAfterPaymnet();
                        } else {
                            snackBar(cartDetailpojo.getMessage(),findViewById(R.id.item_view));
                        }
                    }
                }

                @Override
                public void onFailure(Call<PaymentPojo> call, Throwable t) {
                    cancelProgressDialog();
                }
            });
        } else {
            snackBar(Constant.INTERNETERROR,findViewById(R.id.item_view));
        }
    }



    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = (this);

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", addWalletBalance*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
//    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            txn_amount = String.valueOf(addWalletBalance);
            txn_id     = razorpayPaymentID;
            status     = String.valueOf(1);
            paymentApi(1);
        } catch (Exception e) {
            Log.e("razorPay", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
//    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            txn_amount = String.valueOf(addWalletBalance);
            txn_id     = String.valueOf(code);
            status     = String.valueOf(3);
            paymentApi(1);
        } catch (Exception e) {
            Log.e("razorpayFailure", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onPaymentSuccess(data.getData().toString());
    }
}

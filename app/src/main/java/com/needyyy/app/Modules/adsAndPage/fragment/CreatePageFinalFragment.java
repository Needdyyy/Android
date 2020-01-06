package com.needyyy.app.Modules.adsAndPage.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.adsAndPage.adapter.PagePermotionBudgetAdapter;
import com.needyyy.app.Modules.adsAndPage.modle.Budget;
import com.needyyy.app.Modules.adsAndPage.modle.BudgetCalculation;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.PageData;
import com.needyyy.app.Modules.adsAndPage.modle.PageDataBase;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.PromoteBase;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.addmore.AddMoreCityAdapter;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GooglePlaceAutocompleteAdapter;
import com.needyyy.app.views.MyRangeSeekbar;
import com.needyyy.app.webutils.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter.convertno;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class CreatePageFinalFragment extends BaseFragment implements View.OnClickListener {

    private PageData pageData;
    private PageDataBase pageDataBase;
    private static final String TAG = "CreatePageFinalFragment";
    private CircleImageView imgPageProfile;
    private TextView tvPageTitle, tvPageCreationTime, tvPageDescription, tvPromoteNow,tvmaxSeekbar,bugutcalulate;
    private ImageView imgPageBanner;
    private RecyclerView rvBudget;
    AddMoreCityAdapter addMoreCityAdapter;
    AutoCompleteTextView edtCity ;
    private ArrayList<String> count=new ArrayList<>();
    JSONArray jsonArray;
    private RecyclerView addmorecity;
    private Button addmore,delete;
    private MyRangeSeekbar seekbar;
    private RadioGroup rgGender;
    private RadioButton radioMale,radioFemale,radioOther;
    private TextInputEditText etDateForm,etDateto;
    private PagePermotionBudgetAdapter pagePermotionBudgetAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<BudgetCalculation> budgetCalculation;
    private List<Budget> budgetList;
    private EditText etBudget ,etInterest;
    private RadioButton radioBudget;
    private int rangeMin=0;
    private int rangeMax=0;
    private int gender;
    private Boolean isPage;
    private String budgetPrice ="";
    int selectedItem=-1, finaAmount;
    String pageId="";
    private final  int PAYMENTREQUESTCODE=100;

    public CreatePageFinalFragment() {
        // Required empty public constructor
    }


    public static CreatePageFinalFragment newInstance(PageDataBase pageData,boolean isPage) {
        CreatePageFinalFragment fragment = new CreatePageFinalFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.PAGEDATA, pageData);
        args.putString("type", "1");
        args.putBoolean(Constant.ISPAGE, isPage);
        fragment.setArguments(args);
        return fragment;
    }

    public static CreatePageFinalFragment newInstance(String pageId) {
        CreatePageFinalFragment fragment = new CreatePageFinalFragment();
        Bundle args = new Bundle();
        args.putString(Constant.PAGE_ID, pageId);
        args.putString("type", "2");
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_your_page);
        if (getArguments() != null) {
            String type = getArguments().getString("type");
            if(type.equals("1")) {
                isPage = getArguments().getBoolean(Constant.ISPAGE);
            }
            pageDataBase = (PageDataBase) getArguments().getSerializable(Constant.PAGEDATA);

            jsonArray = new JSONArray();
//            }else if(type.equals("2")) {
//                pageId = getArguments().getString(Constant.PAGE_ID);
//            }
//            if(!pageId.isEmpty() || pageId.equals("")){
//                getPage(pageId);
//            }
        }
    }

    @Override
    protected void initView(View mView) {
        delete=mView.findViewById(R.id.delete);
        addmore=mView.findViewById(R.id.Addmore);
        addmorecity          =mView.findViewById(R.id.recaddcity);
        tvPromoteNow        = mView.findViewById(R.id.tv_promotenow);
        imgPageProfile      = mView.findViewById(R.id.img_pageprofile);
        tvPageTitle         = mView.findViewById(R.id.tv_pagename);
        tvPageCreationTime  = mView.findViewById(R.id.tv_pagecreation_time);
        tvPageDescription   = mView.findViewById(R.id.tv_page_description);
        imgPageBanner       = mView.findViewById(R.id.img_page_banner);
        rvBudget            = mView.findViewById(R.id.rvBudget);
        seekbar             = mView.findViewById(R.id.seekBar);
        edtCity             = mView.findViewById(R.id.edt_city);
        etDateForm          = mView.findViewById(R.id.et_date_form);
        etDateto            = mView.findViewById(R.id.et_date_to);
        rgGender            = mView.findViewById(R.id.rg_gender);
        etBudget            = mView.findViewById(R.id.etbudget);
        radioBudget         = mView.findViewById(R.id.radio_select_budget);
        tvmaxSeekbar        = mView.findViewById(R.id.maxValue);
        etInterest          = mView.findViewById(R.id.et_interest);
        bugutcalulate          =mView.findViewById(R.id.bugutcalulate);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (radioBudget!= null){
            if (radioBudget.isChecked())
                etBudget.setVisibility(View.VISIBLE);
            else{
                etBudget.setVisibility(View.GONE);
                if (selectedItem!=-1)
                    pagePermotionBudgetAdapter.mSelectedItem = selectedItem ;
            }
        }
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).manageToolbar("Boost", "");
        tvPromoteNow.setOnClickListener(this);
        etDateForm.setOnClickListener(this);
        etDateto.setOnClickListener(this);
        addmore.setOnClickListener(this);
        delete.setOnClickListener(this);
//       count.add("a");
//        addMoreCityAdapter=new AddMoreCityAdapter(getContext(),count);
//        addmorecity.setHasFixedSize(false);
//        addmorecity.setLayoutManager(new LinearLayoutManager(getActivity()));
//        addmorecity.setAdapter(addMoreCityAdapter);
        pageData            = pageDataBase.getPages();
        budgetCalculation   = pageDataBase.getBudgetCalculation();
        budgetList          = pageDataBase.getBudget();
        setPageDetails();
        setPermotionAdapter();
        CalculateTargetUser( Long.valueOf(0));
        edtCity.setAdapter(new GooglePlaceAutocompleteAdapter(getActivity(), R.layout.place_textview_layout));
        edtCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String str = (String) adapterView.getItemAtPosition(i);
                Log.e("onItemClick", str.toString());
            }
        });

        radioBudget.setOnClickListener(this);
        seekbar.setMinValue(18);
        seekbar.setMaxValue(60);
        seekbar.setOnRangeSeekbarChangeListener(rangeListener);


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected...
                if(checkedId == R.id.radio_male) {
                    gender=1 ;
                } else if(checkedId == R.id.radio_female) {
                    gender=2 ;
                } else if(checkedId == R.id.radio_other){
                    gender=3 ;
                }
            }
        });


        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" +s.toString());
                //budgetCalculation();
                if(s.length()>0) {
                    rvBudget.setVisibility(View.GONE);
                    CalculateTargetUser(Long.parseLong(s.toString()));
                }
                else
                {
                    CalculateTargetUser(Long.valueOf(0));
                    rvBudget.setVisibility(View.VISIBLE);
                    bugutcalulate.setText("");
                }
            }
        });
    }

    OnRangeSeekbarChangeListener rangeListener = new OnRangeSeekbarChangeListener() {
        @Override
        public void valueChanged(Number minValue, Number maxValue) {
            rangeMin = Integer.parseInt(String.valueOf(minValue));
            rangeMax = Integer.parseInt(String.valueOf(maxValue));
            tvmaxSeekbar.setText(rangeMin+"-"+rangeMax);
            Log.e("rangeListener",""+rangeMin);
            Log.e("rangeListener",""+rangeMax);
        }
    };

    private void setPermotionAdapter() {
        pagePermotionBudgetAdapter = new PagePermotionBudgetAdapter(getActivity(), budgetList, CreatePageFinalFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvBudget.setLayoutManager(linearLayoutManager);
        rvBudget.setAdapter(pagePermotionBudgetAdapter);
    }

    public String getLatLong(String myAddress){
        Geocoder coder = new Geocoder(getActivity());
        String latlan=null;
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(myAddress, 1);
            for(Address add : adresses){
                if (add.hasLatitude() && add.hasLongitude()) {//Controls to ensure it is right address such as country etc.
                    latlan  = add.getLongitude()+","+add.getLatitude();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latlan;
    }

    private void setPageDetails() {
        if (!TextUtils.isEmpty(pageData.getProfile())) {
            Glide.with(getActivity())
                    .load(pageData.getProfile())
                    .into(imgPageProfile);
        } else {
            imgPageProfile.setImageResource(R.drawable.needyy);
        }
        if (!TextUtils.isEmpty(pageData.getBanner())) {
            Glide.with(getActivity())
                    .load(pageData.getBanner())
                    .thumbnail(Glide.with(getContext()).load(imgPageBanner))
                    .into(imgPageBanner);
        } else {
            imgPageBanner.setVisibility(View.GONE);
        }
        tvPageTitle.setText(pageData.getTitle());
        tvPageCreationTime.setText(CommonUtil.getDate(Long.parseLong(pageData.getCreated())));
        tvPageDescription.setText(pageData.getDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Addmore:
                if(edtCity.getText().toString().equals(null)||edtCity.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(),"Please enter the city",Toast.LENGTH_SHORT).show();
                }
                else {
                    count.add(edtCity.getText().toString());
                    edtCity.setText("");
                    addMoreCityAdapter = new AddMoreCityAdapter(getContext(), count);
                    addmorecity.setHasFixedSize(false);
                    addmorecity.setLayoutManager(new LinearLayoutManager(getActivity()));
                    addmorecity.setAdapter(addMoreCityAdapter);
                }
                break;
            case R.id.delete:
                if(count.size()==0)
                {
                    Toast.makeText(getContext(),"Please enter the city",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    count.remove(count.size()-1);
                    addMoreCityAdapter = new AddMoreCityAdapter(getContext(), count);
                    addmorecity.setHasFixedSize(false);
                    addmorecity.setLayoutManager(new LinearLayoutManager(getActivity()));
                    addmorecity.setAdapter(addMoreCityAdapter);
                }
                break;
            case R.id.tv_promotenow:
                JSONObject jsonObject = null;
                for(int i=0;i<count.size();i++) {
                    jsonObject =new JSONObject();
                  String latlan=  getLatLong(count.get(i));
                  String array[]=latlan.split(",");
                    try {
                        jsonObject.put("city",count.get(i) );
                        jsonObject.put("latitude", array[0]);
                            jsonObject.put("longitude", array[1]);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                selectedItem = pagePermotionBudgetAdapter.mSelectedItem ;
                if(radioBudget.isChecked()){
                    if (!etBudget.getText().toString().isEmpty())
                        budgetPrice = etBudget.getText().toString();
                }
                else{
                    if (pagePermotionBudgetAdapter.mSelectedItem!=-1){
                        budgetPrice = String.valueOf(budgetList.get(pagePermotionBudgetAdapter.mSelectedItem).getBudgetPrice());
                    }
                }
                checkValidation();
                break;
            case R.id.radio_select_budget :
                pagePermotionBudgetAdapter.mSelectedItem = -1;
                etBudget.setVisibility(View.VISIBLE);
                pagePermotionBudgetAdapter.notifyDataSetChanged();
                break;
            case R.id.et_date_form:
                CommonUtil.DatePickerWithouValidation(getActivity(), etDateForm, CreatePageFinalFragment.this);
                break;
            case R.id.et_date_to:
                CommonUtil.DatePickerWithouValidation(getActivity(), etDateto,CreatePageFinalFragment.this);
                break;
        }
    }


    private void checkValidation() {
        if (count.size()!=0){
            if (rangeMin!=0 && rangeMax!=0){
                if (!etDateForm.getText().toString().isEmpty()){
                    if (!etDateto.getText().toString().isEmpty()){
                        if (gender!=0){
                            if (!etInterest.getText().toString().isEmpty()){
                                if (!budgetPrice.isEmpty()){
                                    getWalletBalance();
                                }else{
                                    snackBar("please enter budget price");
                                }
                            }else{
                                snackBar("please enter interest");
                            }
                        }else{
                            snackBar("please Select Gender");
                        }
                    }else{
                        snackBar("please Select to date");
                    }
                }else{
                    snackBar("please Select From date");
                }
            }else{
                snackBar("please select Age range");
            }
        }
        else{
            snackBar("please enter city");
        }
    }

    private void getWalletBalance() {
        UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        if((CommonUtil.getNumberOfDays(etDateForm.getText().toString(),etDateto.getText().toString()))>0){
            finaAmount = (CommonUtil.getNumberOfDays(etDateForm.getText().toString(),etDateto.getText().toString()))*Integer.parseInt(budgetPrice);
        }else{
            finaAmount = Integer.parseInt(budgetPrice);
        }
        if (Integer.parseInt(userData.getWalletBal())>= finaAmount){
            showDialogPromotePage();
        }
        else{
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(Constants.WALLET,finaAmount - Integer.parseInt(userData.getWalletBal()));
            startActivityForResult(intent,PAYMENTREQUESTCODE);
            //((HomeActivity)getActivity()).replaceFragment(PaymentActivity.newInstance(finaAmount - Integer.parseInt(userData.getWalletBal())));
        }
    }

    private void showDialogPromotePage() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("If you want to promote then your payment is deducted from wallet");
        builder1.setCancelable(false);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isPage)promotePage();
                        else promotePost();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
//                        intent.putExtra(Constants.WALLET,finaAmount - Integer.parseInt(userData.getWalletBal()));
//                        startActivityForResult(intent,PAYMENTREQUESTCODE);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == PAYMENTREQUESTCODE ) {
            String mode ,status,txn_id,txn_amount,order_id;
            mode = (String) data.getExtras().get(Constant.MODE);
            txn_amount= (String) data.getExtras().get(Constant.FINAL_AMOUNT);
            txn_id= (String) data.getExtras().get(Constant.TXNID);
            status= (String) data.getExtras().get(Constant.STATUS);
            if (isPage)promotePage();
            else promotePost();
        }
    }

    private void promotePage() {
        showProgressDialog();
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PromoteBase> call = Service.promotePage(pageData.getId(),jsonArray.toString(),rangeMin,rangeMax,gender,etInterest.getText().toString(),budgetPrice,etDateForm.getText().toString(),etDateto.getText().toString(),finaAmount);
            call.enqueue(new Callback<PromoteBase>() {
                @Override
                public void onResponse(Call<PromoteBase> call, Response<PromoteBase> response) {
                    cancelProgressDialog();
                    if (response.body() != null) {
                        PromoteBase cartDetailpojo = response.body();
                        if (cartDetailpojo.getStatus()) {
                            UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                            userData.setWalletBal(String.valueOf(Integer.parseInt(userData.getWalletBal())- finaAmount));
                            BaseManager.saveDataIntoPreferences(userData,kCurrentUser);
                            showDialogPagePromote();
                        } else {
                            snackBar(cartDetailpojo.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PromoteBase> call, Throwable t) {
                    cancelProgressDialog();
                }
            });
        } else {
            snackBar(Constant.INTERNETERROR);
        }
    }

    private void promotePost() {
        showProgressDialog();
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PromoteBase> call = Service.promotePost(pageData.getId(),jsonArray.toString(),rangeMin,rangeMax,gender,etInterest.getText().toString(),budgetPrice,etDateForm.getText().toString(),etDateto.getText().toString(),finaAmount);
            call.enqueue(new Callback<PromoteBase>() {
                @Override
                public void onResponse(Call<PromoteBase> call, Response<PromoteBase> response) {
                    cancelProgressDialog();
                    if (response.body() != null) {
                        PromoteBase cartDetailpojo = response.body();
                        if (cartDetailpojo.getStatus()) {
                            UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                            userData.setWalletBal(String.valueOf(Integer.parseInt(userData.getWalletBal())- finaAmount));
                            BaseManager.saveDataIntoPreferences(userData,kCurrentUser);
                            showDialogPagePromote();
                        } else {
                            snackBar(cartDetailpojo.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PromoteBase> call, Throwable t) {
                    cancelProgressDialog();
                }
            });
        } else {
            snackBar(Constant.INTERNETERROR);
        }
    }

    private void showDialogPagePromote() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Page promote successfully");
        builder1.setCancelable(false);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
                        dialog.cancel();
                    }
                });

//        builder1.setNegativeButton(
//                "Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void CalculateTargetUser(Long price) {
        if (price == 0) {
            Double targetuser = 0.0;
            int size = budgetCalculation.size();
            for (int i = 0; i < budgetList.size(); i++) {
                double budget = Double.parseDouble(String.valueOf(budgetList.get(i).getBudgetPrice()));
                for (int j = 0; j < size; j++) {
                    Log.e("gngnggngn", "fgnbgnggn" + j);
                    if (Double.compare(budget, Double.parseDouble(budgetCalculation.get(j).getMinReach())) >= 0 && Double.compare(budget, Double.parseDouble(budgetCalculation.get(j).getMaxReach())) <= 0) {
                        Double cost = Double.parseDouble(budgetCalculation.get(j).getCost());
                        targetuser = budget / cost;
                        budgetList.get(i).setTargetuser(targetuser);
                    }
                }
                pagePermotionBudgetAdapter.notifyDataSetChanged();
            }
        } else {
            Double targetuser = 0.0;
            int size = budgetCalculation.size();
            for (int i = 0; i < budgetList.size(); i++) {
                double budget = price;
                for (int j = 0; j < size; j++) {
                    Log.e("gngnggngn", "fgnbgnggn" + j);
                    if (Double.compare(budget, Double.parseDouble(budgetCalculation.get(j).getMinReach())) >= 0 && Double.compare(budget, Double.parseDouble(budgetCalculation.get(j).getMaxReach())) <= 0) {
                        Double cost = Double.parseDouble(budgetCalculation.get(j).getCost());
                        targetuser = budget / cost;
                        int dub=targetuser.intValue();
                        budgetList.get(i).setTargetuser(targetuser);
                        bugutcalulate.setText(convertno(String.valueOf(dub)));
                        budgetPrice=String.valueOf(targetuser);
                        break;
                    //    bugetvalue.setText(String.valueOf(targetuser));
                    }
                }
            }
        }
    }

    private void getPage(String pageId) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CreatePageModel> call = Service.getPage(pageId);
        call.enqueue(new Callback<CreatePageModel>() {
            @Override
            public void onResponse(Call<CreatePageModel> call, Response<CreatePageModel> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CreatePageModel createPageModel = response.body();
                if (createPageModel.getStatus()) {
                    pageData = createPageModel.getData().getPages();
                    pageDataBase = createPageModel.getData();
//                    showCustomDialog();
                } else {
                    if (createPageModel.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }else{
                        snackBar(createPageModel.getMessage());
                    }
                    snackBar(createPageModel.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CreatePageModel> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }


    AlertDialog alertDialog ;
    AlertDialog.Builder builder ;

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_promote_page, viewGroup, false);
        Button btnOk     = dialogView.findViewById(R.id.buttonOk);
        Button btnCalcel = dialogView.findViewById(R.id.buttoncancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getFragmentManager().popBackStack();
                // ((HomeActivity)getActivity()).replaceFragment(CreatePageFinalFragment.newInstance(pageDataBase), true);
                alertDialog.dismiss();
            }
        });
        btnCalcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(AdsManagerFragment.newInstance(), true);
                alertDialog.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void setBudget() {
        etBudget.setVisibility(View.GONE);
        radioBudget.setChecked(false);
        etBudget.setText("");
        budgetPrice="";
    }


}
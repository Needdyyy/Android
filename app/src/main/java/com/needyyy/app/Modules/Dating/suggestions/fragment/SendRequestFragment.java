package com.needyyy.app.Modules.Dating.suggestions.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.builder.Builders;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Dating.BlurTransformation;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.CircleImageView;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SendRequestFragment extends BaseFragment implements View.OnClickListener {

    Activity activity;
    People people;
    CircleImageView civProfileImage;
    TextView tvProfileCode, tvProfileGender, tvSendRequest,interestedIn,tvopenDating,tvAddress;


    public static SendRequestFragment newInstance(People people) {
        SendRequestFragment fragment = new SendRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable("people", people);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_send_request);
        activity  = getActivity();
        if (getArguments() != null) {
            people = (People) getArguments().getSerializable("people");
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView(View mView) {
        if(activity instanceof HomeActivity)
        {
            ((HomeActivity) activity).manageToolbar("Request","1");
        }
        civProfileImage = mView.findViewById(R.id.img_profile);
        tvProfileCode = mView.findViewById(R.id.tv_profile_code);
        tvProfileGender = mView.findViewById(R.id.tv_gender);
        tvSendRequest = mView.findViewById(R.id.tv_send_request);
        interestedIn=mView.findViewById(R.id.interestedIn);
        tvopenDating=mView.findViewById(R.id.tvopenDating);
        tvAddress=mView.findViewById(R.id.tvAddress);

        String s;

        if(!people.getFromCognitoRemoveDate().equals(""))
        {

            s=people.getSsn();

            if (!people.getProfilePicture().isEmpty()){
                Glide.with(activity).load(people.getProfilePicture())
                        .into(civProfileImage);
            }else {
                Glide.with(activity).load(activity.getDrawable(R.drawable.xx))
                        .into(civProfileImage);
            }

        }
        else {
            s = people.getSsn().substring(0, 2);
            for (int i = s.length(); i <people.getSsn().length() ; i++) {
                s=s+"*";
            }
            if (!people.getProfilePicture().isEmpty()){
                Glide.with(activity).load(people.getProfilePicture())
                        .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)))
                        .into(civProfileImage);
            }else {
                Glide.with(activity).load(activity.getDrawable(R.drawable.xx))
                        .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)))
                        .into(civProfileImage);
            }

        }

        tvProfileCode.setText(s);
        tvAddress.setText(people.getAddress());

        if (people.getOpen_dating().equals("0"))
        {
            tvopenDating.setText("Open Dating : "+"Not Sure");
        }
        else if (people.getOpen_dating().equals("1"))
        {
            tvopenDating.setText("Open Dating : "+"Yes");
        }
        else {
            tvopenDating.setText("Open Dating :  "+"No");
        }
        if (people.getInterested_in().equals("0"))
        {
            interestedIn.setText("Interested In : "+"Both");
        }
        else if (people.getInterested_in().equals("1"))
        {
            interestedIn.setText("Interested In : "+"Male");
        }
        else {
            interestedIn.setText("Interested In : "+"Female");
        }
        tvProfileGender.setText(CommonUtil.getGender(people.getGender())+" , "+people.getDob());
        showDialog = true;
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        civProfileImage.setOnClickListener(this);
        tvProfileCode.setOnClickListener(this);
        tvProfileGender.setOnClickListener(this);
        tvSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send_request:
                if(showDialog) {
                    addMessageDialog();
                }
                break;
        }
    }

    String messsage;
    boolean showDialog;

    public void addMessageDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.send_message_dialog);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        final EditText etCreate = dialog.findViewById(R.id.et_create);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sendKnockRequest(etCreate.getText().toString().trim());
                messsage = etCreate.getText().toString().trim();
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog = false;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void sendKnockRequest(String message){
        showProgressDialog();

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequest(people.getId(),1);
        if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
            call = Service.sendKnockRequestDating(people.getId(),1, message);
        }
        else{
            call = Service.sendKnockRequest(people.getId(),1);
        }

        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if(getReceivedRequest.getStatus()) {

                    Toast.makeText(getContext(),getReceivedRequest.getMessage(),Toast.LENGTH_SHORT).show();
//                    ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
                    getFragmentManager().popBackStack();
                } else {
                    if (getReceivedRequest.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();
                    }else{
                        snackBar(getReceivedRequest.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });

    }

}
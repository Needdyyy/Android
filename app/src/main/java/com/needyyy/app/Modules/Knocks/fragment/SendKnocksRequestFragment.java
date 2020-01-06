package com.needyyy.app.Modules.Knocks.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.KnockRequestAdapter;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Knocks.models.ReceivedData;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.views.CircleImageView;
import com.needyyy.app.webutils.WebInterface;

import java.net.URL;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendKnocksRequestFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView rvKnockrequest;
    private ArrayList<ReceivedData> arrGetRequests = new ArrayList<>();
    private KnockRequestAdapter knockRequestAdapter ;
    AppTextView atvKnockText;
    TextView tvNoResult;
    URL url ;

    private CircleImageView civProfile;
    private EditText etMessage;
    private TextView tvSendKnockRequest,tvSSN;
    private People people ;
    Bitmap image ;
    public SendKnocksRequestFragment() {
        // Required empty public constructor
    }

    public static SendKnocksRequestFragment newInstance(People people) {
        SendKnocksRequestFragment fragment = new SendKnocksRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.kData,people);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.fragment_send_message);
        if (getArguments() != null) {
            people = (People) getArguments().getSerializable(Constant.kData);
        }

//        ((HomeActivity)getActivity()).onNavi(R.id.nv_knocks);

    }


    @Override
    protected void initView(View mView) {
        ((HomeActivity)getActivity()).manageToolbar("Knocks", "");
        civProfile         = mView.findViewById(R.id.img_profile);
        etMessage          = mView.findViewById(R.id.edt_message);
        tvSendKnockRequest = mView.findViewById(R.id.tv_send_knock);
        tvSSN              = mView.findViewById(R.id.tv_profile_code);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

//        if (!TextUtils.isEmpty(people.getProfilePicture())) {
//            Glide.with(getContext())
//                    .load(people.getProfilePicture())
//                    .into(civProfile);
//        } else {
//            civProfile.setImageResource(R.drawable.amritanshu);
//        }
//        Ion.with(this)
//                .load(people.getProfilePicture()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
//            @Override
//            public void onCompleted(Exception e, Bitmap result) {
//                if (result!=null)
//                setBlur(25,result);
//            }
//        });

        CommonUtil.ConvertURLToBitmap(people.getProfilePicture(),getContext() ,civProfile);

        String s = people.getSsn().substring(0, 2);
        for (int i = s.length(); i <people.getSsn().length() ; i++) {
            s=s+"*";
        }
        tvSSN.setText(s);
        tvSendKnockRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendKnockRequest();
            }
        });
    }

    public void sendKnockRequest(){
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequest(people.getId(),1);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if(getReceivedRequest.getStatus()) {
                    CommonUtil.showShortToast(getActivity(),getReceivedRequest.getMessage());
                    ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
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

    @Override
    public void onClick(View v) {

    }
}

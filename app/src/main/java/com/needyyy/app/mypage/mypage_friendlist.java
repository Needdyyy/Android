package com.needyyy.app.mypage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mypage_friendlist extends BaseFragment implements View.OnClickListener{

    ArrayList<People> arrfriends = new ArrayList<>();
    RecyclerView rvFriendsList;
    private Button invitebutton;
    TextView tvNoFriends;
    private String pageid;
    private int index;
    mypage_adapter mypage_adapterr;
    private GPSTracker gpsTracker;
    private Double latitude,lognitude;
    CommonPojo commonPojo;


    public static mypage_friendlist newInstance(int index, String pageid) {
        mypage_friendlist fragment = new mypage_friendlist();
        Bundle args = new Bundle();
        args.putInt(Constant.INDEX,index);
        args.putString("pageid",pageid);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.mypage_friendlist);
        if (getArguments() != null) {
            index = getArguments().getInt(Constant.INDEX);
            pageid=getArguments().getString("pageid");
        }
    }


    @Override
    protected void initView(View mView) {
        gpsTracker = new GPSTracker(getActivity());
        rvFriendsList = mView.findViewById(R.id.rv_friend_list);
        tvNoFriends = mView.findViewById(R.id.tv_no_friends);
        invitebutton=mView.findViewById(R.id.invitebutton);
        invitebutton.setOnClickListener(this);
        ((HomeActivity)getActivity()).manageToolbar("Friends", "");
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        mypage_adapterr = new mypage_adapter(getContext(),arrfriends);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFriendsList.setLayoutManager(layoutManager);
        rvFriendsList.setAdapter(mypage_adapterr);
        Log.e("index","index"+index);
        if (index==1){
            latitude  = gpsTracker.getLatitude();
            lognitude = gpsTracker.getLongitude();
        }else{
            latitude=0.0;
            lognitude=0.0;
        }
        getFriendsCount();
    }

    private void getFriendsCount() {
        if (CommonUtil.isConnectingToInternet(getActivity())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call = Service.getFriends(1,20,"",String.valueOf(latitude),String.valueOf(lognitude));
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        arrfriends.clear();
                        arrfriends.addAll(peopleBase.getData());
                        mypage_adapterr.notifyDataSetChanged();

                    } else {
                        if (peopleBase.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();
                        }else{
                            snackBar(peopleBase.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PeopleBase> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
        }else{

        }

    }




    public mypage_friendlist() {

    }

    public static mypage_friendlist newInstance(String param1, String param2) {
        mypage_friendlist fragment = new mypage_friendlist();

        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.invitebutton:
                if (!mypage_adapterr.getselectrdId().isEmpty())
                hitapi(mypage_adapterr.getselectrdId());
                else{

                }
        }
    }

    private void hitapi(String data) {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.Invite(pageid, data);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    cancelProgressDialog();
                    commonPojo= response.body();
                    if (commonPojo.getStatus().equals("true")) {

                        Toast.makeText(getContext(), commonPojo.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), commonPojo.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonPojo> call, Throwable t) {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

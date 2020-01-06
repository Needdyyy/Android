package com.needyyy.app.Chat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Chat.Adapter.DatingRequestAdapter;
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

public class ChatReceiveFragment extends BaseFragment implements View.OnClickListener{

    ArrayList<People> arrfriends = new ArrayList<>();
    RecyclerView rvFriendsList;
    TextView tvNoFriends;
    private int index;
  //  FriendChatAdapters friendListAdapters;
    DatingRequestAdapter datingRequestAdapter;
    private GPSTracker gpsTracker;
    private Double latitude,lognitude;

    public ChatReceiveFragment() {
        // Required empty public constructor
    }


    public static ChatReceiveFragment newInstance(int index) {
        ChatReceiveFragment fragment = new ChatReceiveFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.INDEX,index);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_friends_list);
        if (getArguments() != null) {
            index = getArguments().getInt(Constant.INDEX);
        }
    }


    @Override
    protected void initView(View mView) {
        gpsTracker = new GPSTracker(getActivity());
        rvFriendsList = mView.findViewById(R.id.rv_friend_list);
        tvNoFriends = mView.findViewById(R.id.tv_no_friends);


    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {


     //   friendListAdapters = new FriendChatAdapters(getActivity(),arrfriends);
        datingRequestAdapter = new DatingRequestAdapter(getActivity(), arrfriends);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFriendsList.setLayoutManager(layoutManager);
        rvFriendsList.setAdapter(datingRequestAdapter);
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
            Call<PeopleBase> call = Service.getReceivedRequestWithMessage("20", "1","1");

            if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
                call = Service.getReceivedRequestWithMessageDating("20", "1","1");
            }
            else{
                call = Service.getReceivedRequestWithMessage("20", "1","1");
            }

            //Call<PeopleBase> call = Service.getReceivedRequestWithMessage("20", "1","1");
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        arrfriends.clear();
                        arrfriends.addAll(peopleBase.getData());
                        datingRequestAdapter.notifyDataSetChanged();

                    } else {
                        tvNoFriends.setVisibility(View.VISIBLE);
                        if (peopleBase.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();
                        }else{
                          //  CommonUtil.showLongToast(getActivity(),peopleBase.getMessage());
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

    @Override
    public void onClick(View v) {

    }
}

package com.needyyy.app.Modules.Profile.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Profile.adapters.FriendListAdapters;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListFragment extends BaseFragment implements View.OnClickListener{

    ArrayList<People> arrfriends = new ArrayList<>();
    RecyclerView rvFriendsList;
    TextView tvNoFriends;
    private int index;
    FriendListAdapters friendListAdapters;
    private int mPage = 1;
    private boolean loading = false;
    private int pageSize;
    private GPSTracker gpsTracker;
    private Double latitude,lognitude;
    private SwipeRefreshLayout pullToReferesh;
    private NestedScrollView nestedScrollView;

    public FriendsListFragment() {
        // Required empty public constructor
    }


    public static FriendsListFragment newInstance(int index) {
        FriendsListFragment fragment = new FriendsListFragment();
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
        pullToReferesh = mView.findViewById(R.id.pullto_referesh);
        nestedScrollView = mView.findViewById(R.id.nested_scroll);

        ((HomeActivity)getActivity()).manageToolbar("Friends", "2");
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

         if(index==1)
         {
             friendListAdapters = new FriendListAdapters(getActivity(),arrfriends,"");
         }
         else
         {
             friendListAdapters = new FriendListAdapters(getActivity(),arrfriends,"profile");
         }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFriendsList.setLayoutManager(layoutManager);
        rvFriendsList.setAdapter(friendListAdapters);
        Log.e("index","index"+index);
        if (index==1){
            latitude  = gpsTracker.getLatitude();
            lognitude = gpsTracker.getLongitude();
        }else{
            latitude=0.0;
            lognitude=0.0;
        }

        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialState();
                getFriendsCount("",false);
                pullToReferesh.setRefreshing(false);
            }
        });


        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        getFriendsCount("",true);
                    }
                }
            }
        });




        getFriendsCount("",false);
    }


    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = arrfriends.size();
    }








    private void getFriendsCount(String search,Boolean status) {
        if (CommonUtil.isConnectingToInternet(getActivity())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call = Service.getFriends(1,20,"",String.valueOf(latitude),String.valueOf(lognitude));
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {
                    cancelProgressDialog();
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        if (peopleBase.getData().size() == 0) {
                            tvNoFriends.setVisibility(View.VISIBLE);
                        } else {
                            tvNoFriends.setVisibility(View.GONE);
                            if (status) {
                                arrfriends.addAll(peopleBase.getData());
                                loading = !(peopleBase.getData().size() == 0);
                            } else {
                                initialState();
                                if (arrfriends != null && arrfriends.size() != 0) {
                                    arrfriends.clear();
                                }
                                arrfriends.addAll(peopleBase.getData());
                            }
                            friendListAdapters.notifyDataSetChanged();
//                        snackBar(getReceivedRequest.getMessage());
                        }
                    } else {
                        tvNoFriends.setVisibility(View.VISIBLE);
                        if (peopleBase.getMessage().equals("110110")) {
                            ((HomeActivity) getActivity()).logout();

                        }
                        else {
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

    @Override
    public void onClick(View v) {

    }
}

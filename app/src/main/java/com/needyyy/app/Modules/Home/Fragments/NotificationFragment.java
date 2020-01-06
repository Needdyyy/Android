package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.NotificationAdapter;
import com.needyyy.app.Modules.adsAndPage.modle.PageData;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends BaseFragment implements View.OnClickListener{
    private RecyclerView rvNotification;
    private NotificationAdapter notificationAdapter;
    private LinearLayoutManager linearLayoutManager ;
    private List<PageData> pageDataList = new ArrayList<>();

    SwipeRefreshLayout pullToRefresh;
    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_mypage);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {
        ((HomeActivity) getActivity()).setTitle("Notification");
        rvNotification = mView.findViewById(R.id.rv_mypage_container);
        pullToRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("Notifications", "");
        if (CommonUtil.isConnectingToInternet(getContext())){

        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
        setAdapter();

        //setting an setOnRefreshListener on the SwipeDownLayout
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {
                //Here you can update your data from internet or from local SQLite data

                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void setAdapter() {
        notificationAdapter = new NotificationAdapter(getActivity(),pageDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvNotification.setLayoutManager(linearLayoutManager);
        rvNotification.setAdapter(notificationAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

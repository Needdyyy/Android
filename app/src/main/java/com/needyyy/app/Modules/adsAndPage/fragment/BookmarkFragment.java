package com.needyyy.app.Modules.adsAndPage.fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.adsAndPage.adapter.MyPageAdapter;
import com.needyyy.app.Modules.adsAndPage.modle.MyPage;
import com.needyyy.app.Modules.adsAndPage.modle.PageData;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkFragment extends BaseFragment implements View.OnClickListener{
    private RecyclerView rvMyPage;
    private MyPageAdapter myPageAdapter;
    private LinearLayoutManager linearLayoutManager ;
    private List<PageData> pageDataList = new ArrayList<>();
    SwipeRefreshLayout pullToRefresh;
    public BookmarkFragment() {
    }


    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_bookmark);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {
        rvMyPage = mView.findViewById(R.id.rv_mypage_container);
        pullToRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("BookMark", "2");
        if (CommonUtil.isConnectingToInternet(getContext())){
            getCreatedPage();
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }


        //setting an setOnRefreshListener on the SwipeDownLayout
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {
                //Here you can update your data from internet or from local SQLite data
                getCreatedPage();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void getCreatedPage() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<MyPage> call = Service.getBookmarkPages(1,20);
        call.enqueue(new Callback<MyPage>() {
            @Override
            public void onResponse(Call<MyPage> call, Response<MyPage> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                MyPage myPage = response.body();
                if (myPage.getStatus()) {
                    pageDataList.clear();
                    pageDataList.addAll(myPage.getData());
                    setAdapter();
                } else {
                    if (myPage.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }else{
                        snackBar(myPage.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<MyPage> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    private void setAdapter() {
        myPageAdapter = new MyPageAdapter(getActivity(),pageDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMyPage.setLayoutManager(linearLayoutManager);
        rvMyPage.setAdapter(myPageAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
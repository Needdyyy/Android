package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.AddPost.TaggedPerson;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Profile.adapters.FriendListAdapters;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFriendFragment extends BaseFragment implements View.OnClickListener {

    FriendListAdapters friendListAdapters;
    public String last_post_id;
    public String tag_id = "";
    ArrayList<People> arrfriends = new ArrayList<>();
    public int previousTotalItemCount;
    public int firstVisibleItem, visibleItemCount, totalItemCount;

    private RecyclerView rvPeople;
    public boolean isRefresh;
    private LinearLayoutManager linearLayoutManager ;
    private List<TaggedPerson> userDataList ;
    private SwipeRefreshLayout swipeRefreshLayout ;

    public static SearchFriendFragment newInstance() {
        SearchFriendFragment fragment = new SearchFriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_search_friend);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.search_hint);//

        setHasOptionsMenu(true);
        if (getArguments() != null){
        }
    }

    @Override
    protected void initView(View mView) {
        swipeRefreshLayout = mView.findViewById(R.id.swipe_referesh);
        rvPeople = mView.findViewById(R.id.recycler_newview_people);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("Search", "");
        linearLayoutManager = new LinearLayoutManager(getContext());
        friendListAdapters = new FriendListAdapters(getActivity(),arrfriends,"");
        rvPeople.setLayoutManager(linearLayoutManager);
        rvPeople.setAdapter(friendListAdapters);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              //  getFriendsCount();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void RefreshFeedList(boolean show) {
//        if (!swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(show);
//            isRefresh = show;
//        }
        getFriendsCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comment:
                break;
        }
    }
    private void getFriendsCount() {
        if (CommonUtil.isConnectingToInternet(getActivity())){

            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call = Service.getAllFriends(1,20,AppController.getManager().getSearchQuery());
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {

                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        arrfriends.clear();
                        arrfriends.addAll(peopleBase.getData());
                        friendListAdapters.notifyDataSetChanged();
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

                }
            });
        }else{

        }

    }


}

package com.needyyy.app.Modules.Dating;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Dating.suggestions.adapter.SuggestionAdapter;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendSuggestion extends BaseFragment {
    Location location;
    RecyclerView recyclerView;
    SuggestionAdapter suggestionAdapter;
    ArrayList<People> arrfriends = new ArrayList<>();

    String range;
    String gender;
    String ageFrom;
    private int pageSize;
    String ageTo;
    String latlan="";
    Double longitute;
    Double latitude;
    private SwipeRefreshLayout pullToReferesh;
    private NestedScrollView nestedScrollView;
    private int mPage = 1;
    private boolean loading = false;


    public FriendSuggestion() {
    }

    public static FriendSuggestion newInstance() {
        FriendSuggestion fragment = new FriendSuggestion();
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_friend_suggestion);
        GPSTracker gpsTracker=new GPSTracker(getContext());

        if (getArguments()!= null){
            latlan     =(getArguments().getString("latlan"));
            range     = (getArguments().getString("Distance"));
            ageFrom   = (getArguments().getString("From_age"));
            ageTo     = (getArguments().getString("To_age"));
            gender    =(getArguments().getString("Gender"));
            if(!(latlan==null || latlan.equals(""))) {
                String loca[] = latlan.split(",");
                longitute = Double.parseDouble(loca[0]);
                latitude = Double.parseDouble(loca[1]);
            }
            else
            {
                location=gpsTracker.getLocation();
                latitude=location.getLatitude();
                longitute=location.getLongitude();
            }
        } else{
            range = "";
            ageFrom = "";
            ageTo = "";
            gender = "";
            location=gpsTracker.getLocation();
            longitute=location.getLongitude();
            latitude=location.getLatitude();
        }

        getFriendsCount("",false);





    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = arrfriends.size();
    }
    @Override
    protected void initView(View mView) {
      recyclerView = mView.findViewById(R.id.recyclerView);
        pullToReferesh = mView.findViewById(R.id.pullto_referesh);
        nestedScrollView = mView.findViewById(R.id.nested_scroll);
    }
    @Override
    protected void bindControls(Bundle savedInstanceState) {


        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.suggestion), "");
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



        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 18, true));
        suggestionAdapter = new SuggestionAdapter(getContext(),arrfriends);
        recyclerView.setAdapter(suggestionAdapter);
    }

     void getFriendsCount(String search,Boolean status) {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
//         String latitude;
//         String  longitude;
//         if(range.isEmpty()){
//             latitude = "";
//             longitude = "";
//         }else {
//             latitude= String.valueOf(location.getLatitude());
//             longitude= String.valueOf(location.getLongitude());
//        }

            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call =Service.getAllSuggestionFriends(mPage, 20,range,latitude,longitute,ageFrom,ageTo,gender, AppController.getManager().getSearchQuery());
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {

                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        if (peopleBase.getData().size() == 0) {
                        } else {
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
                            suggestionAdapter.notifyDataSetChanged();
//                        snackBar(getReceivedRequest.getMessage());
                        }
                    }
                    else {
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
                }
            });
        }
    }
}



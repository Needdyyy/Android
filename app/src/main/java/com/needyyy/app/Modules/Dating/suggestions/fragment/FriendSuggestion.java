package com.needyyy.app.Modules.Dating.suggestions.fragment;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
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
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;
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
    String ageTo;

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
            range = (getArguments().getString("Distance"));
            ageFrom = (getArguments().getString("From_age"));
            ageTo = (getArguments().getString("To_age"));
            gender =(getArguments().getString("Gender"));
        } else{
            range = "50";
            ageFrom = "18";
            ageTo = "60";
            gender = "1";
        }
        location=gpsTracker.getLocation();
        getFriendsCount();

    }
    @Override
    protected void initView(View mView) {
      recyclerView = mView.findViewById(R.id.recyclerView);
    }
    @Override
    protected void bindControls(Bundle savedInstanceState) {

        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.comment), "");
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.comments), "");
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.suggestion), "");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 18, true));
        suggestionAdapter = new SuggestionAdapter(getContext(),arrfriends);
        recyclerView.setAdapter(suggestionAdapter);
    }

     public  void getFriendsCount() {
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
            Call<PeopleBase> call =Service.getAllSuggestionFriends(1, 20,
                    range,location.getLatitude() ,location.getLongitude(),
                    ageFrom,ageTo,gender, AppController.getManager().getSearchQuery());
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {

                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        arrfriends.clear();
                        arrfriends.addAll(peopleBase.getData());
                        suggestionAdapter.notifyDataSetChanged();
                    } else {
                        if (peopleBase.getMessage().equals("110110")) {
                            ((HomeActivity) getActivity()).logout();

                        } else {
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



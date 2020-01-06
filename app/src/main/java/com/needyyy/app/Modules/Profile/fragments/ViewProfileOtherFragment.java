package com.needyyy.app.Modules.Profile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.ImageAdapter;
import com.needyyy.app.Modules.Profile.models.GetViewProfile;
import com.needyyy.app.Modules.Profile.models.Photo;
import com.needyyy.app.Modules.Profile.models.ViewProfileData;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.kCurrentUser;

//import com.needyyy.app.Modules.Home.Activities.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileOtherFragment extends BaseFragment implements View.OnClickListener {

    CircleImageView profile_circle_iv;
    TextView tv_name, tv_dob, tv_from, tv_address, tvFriendsCount, tvAddPhoto, iv_edit, tvBio, tvSSN;
    RecyclerView rvFeeds;
    SingleFeedsAdapter singleFeedsAdapter;
    ImageAdapter imageAdapter;
    ImageView cover_photo;
    private ArrayList<PostResponse> arrFeeds = new ArrayList<>() ;
    private ArrayList<Photo> arrPhotos = new ArrayList<>() ;
    private ArrayList<People> arrFriends = new ArrayList<>() ;
    //    ImageView iv_edit;
    ViewProfileData updateProfileResult;
    CoordinatorLayout profile;
    NestedScrollView nestedView;
    Activity activity;
    String memberId="";
    LinearLayout llBio,llAcceptRequest;
    RecyclerView gridPhoto, gridFriends;

    public ViewProfileOtherFragment() {
        // Required empty public constructor
    }

    public static ViewProfileOtherFragment newInstance(String memberId) {
        ViewProfileOtherFragment fragment = new ViewProfileOtherFragment();
        Bundle args = new Bundle();
        args.putString("memberId", memberId);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.fragment_view_profile);
        activity = getActivity();
        if (getArguments() != null) {
            memberId = "";
            memberId = getArguments().getString("memberId");
        }
    }

    @Override
    protected void initView(View view) {
        nestedView = view.findViewById(R.id.nestedView);
        llAcceptRequest = view.findViewById(R.id.ll_acceptrequest);
//        profile = view.findViewById(R.id.profile);
        profile_circle_iv = view.findViewById(R.id.profile_circle_iv);
        tv_name = view.findViewById(R.id.tv_profile_name);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_from = view.findViewById(R.id.tv_from);
        tv_address = view.findViewById(R.id.tv_address);
        rvFeeds = view.findViewById(R.id.rv_feeds);
        iv_edit = view.findViewById(R.id.iv_edit);
        gridFriends = view.findViewById(R.id.rv_friends_pics);
        gridPhoto  = view.findViewById(R.id.rv_photos);
        tvFriendsCount = view.findViewById(R.id.tv_friends_count);
        tvAddPhoto = view.findViewById(R.id.tv_add_photo);
        tvBio       = view.findViewById(R.id.tv_add_bio);
        llBio       = view.findViewById(R.id.ll_bio);
        tvSSN       = view.findViewById(R.id.tv_profile_code);
        setFeedRv();




    }
    private UserDataResult userData ;
    ImageAdapter friendListAdapters;

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("profile",memberId);

        iv_edit.setOnClickListener(this);
        profile_circle_iv.setOnClickListener(this);
        tvFriendsCount.setOnClickListener(this);
        tvAddPhoto.setOnClickListener(this);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        if(userData.getId().equals(memberId) || memberId.equals("")){
            iv_edit.setVisibility(View.VISIBLE);
            llAcceptRequest.setVisibility(View.GONE);
            memberId="";
        } else {
            llAcceptRequest.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.GONE);
        }

        callProfileApi(memberId);
    }


    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


    public void setFeedRv() {


        rvFeeds.setVisibility(View.VISIBLE);
        singleFeedsAdapter = new SingleFeedsAdapter(activity, arrFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(activity));
        rvFeeds.setAdapter(singleFeedsAdapter);
        rvFeeds.setNestedScrollingEnabled(false);

        int noOfColumns = calculateNoOfColumns(getActivity(), 120);

        gridPhoto.setVisibility(View.VISIBLE);
        imageAdapter = new ImageAdapter(activity, arrPhotos,memberId);
        gridPhoto.setLayoutManager(new GridLayoutManager(activity, noOfColumns));
        gridPhoto.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
        gridPhoto.setAdapter(imageAdapter);
        gridPhoto.setNestedScrollingEnabled(false);

        gridFriends.setVisibility(View.VISIBLE);
        friendListAdapters = new ImageAdapter(activity, arrFriends, ViewProfileOtherFragment.this);
        gridFriends.setLayoutManager(new GridLayoutManager(activity, 3));
        gridFriends.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.dp10), true));
        gridFriends.setAdapter(friendListAdapters);
        gridFriends.setNestedScrollingEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                ((HomeActivity) getActivity()).replaceFragment(ProfileFragment.newInstance(), true);
                break;

            case R.id.profile_circle_iv:
                //  ((HomeActivity) getActivity()).replaceFragment(ViewFullImageFragment.newInstance(updateProfileResult.getProfilePicture()), true);
                break;
            case R.id.tv_add_photo:
                CommonUtil.showShortToast(getContext(),"add Photo");
                break;


        }

    }

    public void callProfileApi(String id) {
        if (CommonUtil.isConnectingToInternet(activity)) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GetViewProfile> call = Service.viewProfile(id);
            call.enqueue(new Callback<GetViewProfile>() {
                @Override
                public void onResponse(Call<GetViewProfile> call, Response<GetViewProfile> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    GetViewProfile getViewProfile = response.body();
                    if (getViewProfile.getStatus()) {
                        updateProfileResult = getViewProfile.getData();
                        arrPhotos.clear();
                        arrFriends.clear();
                        arrFeeds.clear();
                        memberId = id;
                        arrPhotos.addAll(updateProfileResult.getPhotos());
                        imageAdapter.notifyDataSetChanged();
                        arrFriends.addAll(updateProfileResult.getFriends());
                        friendListAdapters.notifyDataSetChanged();
                        getUserPost();
                        setProfileData();
                    } else {
                        nestedView.setVisibility(View.GONE);
                        CommonUtil.snackBar("", activity);
                    }
                }

                @Override
                public void onFailure(Call<GetViewProfile> call, Throwable t) {
                    CommonUtil.cancelProgress();
                    nestedView.setVisibility(View.GONE);
                    CommonUtil.snackBar(t.getMessage(), activity);
                }
            });
        } else {
            nestedView.setVisibility(View.GONE);
            CommonUtil.snackBar(getString(R.string.internetmsg), activity);
        }
    }

    private void getUserPost() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<PostDataBase> call = Service.getUserPost(1,20, memberId);
        call.enqueue(new Callback<PostDataBase>() {
            @Override
            public void onResponse(Call<PostDataBase> call, Response<PostDataBase> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                PostDataBase myPage = response.body();
                if (myPage.getStatus()) {
                    if (arrFeeds!=null || arrFeeds.size()==0){
                        arrFeeds.clear();
                    }
                    arrFeeds.addAll(myPage.getData());
                    singleFeedsAdapter.notifyDataSetChanged();
                } else {
                    if (myPage.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    } else {
                        snackBar(myPage.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<PostDataBase> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });

    }


    private void setProfileData() {
        nestedView.setVisibility(View.VISIBLE);
//        profile.setVisibility(View.VISIBLE);
        tv_name.setText(!TextUtils.isEmpty(updateProfileResult.getName()) ? updateProfileResult.getName() : "Ambuj Sukla");
        String dob= updateProfileResult.getDob().replace("-","");
        if(dob.length()<=8){
            tv_dob.setText(updateProfileResult.getDob());
        }else{
            Timestamp ts = new Timestamp(Long.parseLong(updateProfileResult.getDob())*1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = ts;
            try {
                date=(Date) formatter.parse(String.valueOf(ts.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_dob.setText(!TextUtils.isEmpty(date.toString()) ?  formatter.format(date) : "");
        }

        int size = updateProfileResult.getUserAddress().size();
        if (size >= 2){
            tv_from.setText("From " + (!TextUtils.isEmpty(updateProfileResult.getUserAddress().get(size - 2).getLocation()) ?
                    updateProfileResult.getUserAddress().get(size - 2).getLocation() :" Delhi, India"));

            tv_address.setText("Lives in " + (!TextUtils.isEmpty(updateProfileResult.getUserAddress().get(size - 2).getLocation()) ?
                    updateProfileResult.getUserAddress().get(size - 1).getLocation() :" Delhi, India"));
        } else {
            tv_from.setText("From " +
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"));

            tv_address.setText("Lives in " +
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"));
        }
//        tv_from.setText("From " + (!TextUtils.isEmpty(updateProfileResult.getLocationDetail().get(size - 2).getLocation()) ?
//                updateProfileResult.getLocationDetail().get(size - 2).getLocation() :" Delhi, India"));
//        tv_address.setText("Lives in " + (!TextUtils.isEmpty(updateProfileResult.getLocationDetail().get(size - 2).getLocation()) ?
//                updateProfileResult.getLocationDetail().get(size - 1).getLocation() :" Delhi, India"));

        Glide.with(this)
                .load(updateProfileResult.getProfilePicture())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                .into(profile_circle_iv);


        tvFriendsCount.setText(updateProfileResult.getFriends().size() + " Friends");

        if(userData.getId().equals(memberId) || memberId.equals("")){
            iv_edit.setVisibility(View.VISIBLE);
            //tvAddPhoto.setVisibility(View.VISIBLE);
        } else {
            iv_edit.setVisibility(View.GONE);
            //tvAddPhoto.setVisibility(View.GONE);
        }

        if(!updateProfileResult.getBio().equals("")) {
            tvBio.setText(updateProfileResult.getBio().toString());
            llBio.setVisibility(View.VISIBLE);
        } else {
            llBio.setVisibility(View.GONE);
        }

        tvSSN.setText(updateProfileResult.getSsn().toString());

    }

//    @Override
//    public void onDestroyView() {
//        FragmentManager manager = (ViewProfileFragment.this).getFragmentManager();
//        FragmentTransaction trans = manager.beginTransaction();
//        trans.remove(ViewProfileFragment.this);
//        trans.commit();
//        super.onDestroyView();
//    }
}
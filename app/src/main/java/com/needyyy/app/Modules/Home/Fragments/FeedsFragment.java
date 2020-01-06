package com.needyyy.app.Modules.Home.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Listener;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.Home.Activities.CheckInActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Home.modle.googlePlace.PlaceBase;
import com.needyyy.app.Modules.Login.Fragments.CreatePinFragment;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.OnFragmentVisibleListener;
import com.needyyy.app.utils.SearchListener;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.constants.Constants.kCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends BaseFragment implements View.OnClickListener, SearchListener, Listener {
    FloatingActionButton goToTop;
    LinearLayout llFeeds, llTop;
    //    CircleImageView civProfilePic;
    LinearLayoutManager LM;
    Boolean is_post_exit=true;
    private int mPage = 1;
    private boolean loading = false;
    TextView tvLetsTalk;
    private int pageSize;
    RecyclerView rvFeeds;
    public static final int REQUEST_CODE = 11;
    SingleFeedsAdapter singleFeedsAdapter;
    ArrayList<PostResponse> postResponseArrayList = new ArrayList<>();
    Activity activity;
    CircleImageView civ_profile_pic;
    private SwipeRefreshLayout pullToReferesh;
    private NestedScrollView nestedScrollView;
    public UserDataResult userData;
    private AppTextView tvCheckin;


    public static Fragment newInstance() {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(String searchtext) {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public FeedsFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_feeds);
        activity = getActivity();
        HomeActivity.setListener(this);

    }

    @Override
    protected void initView(View mView) {
        llFeeds = mView.findViewById(R.id.ll_feeds);
        goToTop = mView.findViewById(R.id.go_to_top);
        llTop = mView.findViewById(R.id.ll_top);
        tvLetsTalk = mView.findViewById(R.id.tv_lets_talk);
        rvFeeds = mView.findViewById(R.id.rv_feeds);
        civ_profile_pic = mView.findViewById(R.id.civ_profile_pic);
        pullToReferesh = mView.findViewById(R.id.pullto_referesh);
        nestedScrollView = mView.findViewById(R.id.nested_scroll);
        tvCheckin = mView.findViewById(R.id.tv_checking);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        ((HomeActivity) getActivity()).manageToolbar("Feeds", "");

            llTop.setVisibility(View.GONE);

        if(userData.getPin()==null || userData.getPin().equals(""))
        {
            if(userData.getIs_fingerprint_enable().equals("0"))
            {
                if(getContext() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).replaceFragment(CreatePinFragment.newInstance(1,"0"),false);
                }
            }
            else
            {
                if(getContext() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).replaceFragment(CreatePinFragment.newInstance(1,"1"),false);
                }
            }
        }
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        if (userData != null) {
            if (!TextUtils.isEmpty(userData.getProfilePicture())) {
                Glide.with(this)
                        .load(userData.getProfilePicture())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                        .into(civ_profile_pic);
            } else {
                civ_profile_pic.setImageResource(R.drawable.needyy);
            }
        }

        setClikListeners(llTop, tvLetsTalk, civ_profile_pic);
        getfeeds(false,"");
//        if(frag.equals("search"))
//        {
//            if (postResponseArrayList != null && postResponseArrayList.size() == 0) {
//                getfeeds(false,"");
//            }
//        }
//        else
//        {
//            if (postResponseArrayList != null && postResponseArrayList.size() == 0) {
//                getfeeds(false,frag);
//            }
//        }
//
//        if (postResponseArrayList != null && postResponseArrayList.size() == 0) {
//            getfeeds(false,"");
//        }
        goToTop.setOnClickListener(this);
        tvCheckin.setOnClickListener(this);
        singleFeedsAdapter = new SingleFeedsAdapter(getActivity(), postResponseArrayList);
        rvFeeds.setHasFixedSize(true);
        LM = new LinearLayoutManager(getContext());
        LM.setOrientation(LinearLayoutManager.VERTICAL);
        rvFeeds.setLayoutManager(LM);
        rvFeeds.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvFeeds, false);
        rvFeeds.setAdapter(singleFeedsAdapter);

      pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    is_post_exit=true;
                    initialState();
                    getfeeds(false,"");
                    pullToReferesh.setRefreshing(false);
                }
            });


        // rvFeeds.addOnScrollListener(onScrollListener);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;

                        if(is_post_exit==true) {
                            getfeeds(true,"");
                    }
                        else if(is_post_exit==false)
                        {
                            snackBar("No data Found");
                        }

                    }

                }
            }
        });
    }
    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = postResponseArrayList.size();
    }
    private void getfeeds(boolean status,String search) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<PostDataBase> call = Service.getMyFeeds(mPage, 20, userData.getDob(), userData.getGender(),search);
        call.enqueue(new Callback<PostDataBase>() {
            @Override
            public void onResponse(Call<PostDataBase> call, Response<PostDataBase> response) {

                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                PostDataBase postDataBase = response.body();
                if (postDataBase.getStatus()) {
                    if (status) {
                        postResponseArrayList.addAll(postDataBase.getData());
                        loading = !(postDataBase.getData().size() == 0);
                    } else {
                        initialState();
                        if (postResponseArrayList != null && postResponseArrayList.size() != 0) {
                            postResponseArrayList.clear();
                        }
                        postResponseArrayList.addAll(postDataBase.getData());
                    }
                    singleFeedsAdapter.notifyDataSetChanged();
                } else {
                    if (postDataBase.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        is_post_exit=false;
                        snackBar(postDataBase.getMessage());
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


    private void setClikListeners(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lets_talk:
                ((HomeActivity) getActivity()).replaceFragment(PostFragment.newInstance(null), true);
                break;

            case R.id.civ_profile_pic:
                showProfile();
                break;

            case R.id.tv_checking:
                Intent intent = new Intent(getActivity(), CheckInActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.go_to_top:
                nestedScrollView.smoothScrollTo(0,0);
                // ((LinearLayoutManager)rvFeeds.getLayoutManager()).scrollToPositionWithOffset(0,200);
                break;
        }
    }
    public void showProfile() {
        ((HomeActivity) getActivity()).replaceFragment(ViewProfileFragment.newInstance(userData.getId(), null), true);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            PlaceBase placeBase = (PlaceBase) data.getExtras().getSerializable("key");
            ((HomeActivity) getActivity()).replaceFragment(CheckinFragment.newInstance(placeBase), true);
            Log.e("response", "===" + placeBase.getResult().getName());
            Log.e("response", "===" + placeBase.getResult().getUrl());
            Log.e("response", "===" + placeBase.getResult().getWebsite());
            Log.e("response", "===" + placeBase.getResult().getVicinity());
            Log.e("response", "===" + placeBase.getResult().getGeometry().getLocation().getLat());
            Log.e("response", "===" + placeBase.getResult().getGeometry().getLocation().getLng());
        }
    }

    boolean isAttached;

    OnFragmentVisibleListener mapFragmentVisibilityListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            isAttached = true;
            mapFragmentVisibilityListener = (OnFragmentVisibleListener) activity;
            //flag for whether this fragment is attached to pager
            Log.d("response","onAttach");
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(activity.toString() + " must implement interface onAttach");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        Log.d("response","setUserVisibleHint:"+isVisibleToUser);
        if(isVisibleToUser && isAttached){ //if listener is called before fragment is attached will throw NPE
            Log.d("response","setUserVisibleHint");
            mapFragmentVisibilityListener.fragmentVisible(true, FeedsFragment.this);
        }
    }

    @Override
    public void onClickSearch(String text) {
        try{
            initialState();
            getfeeds(false,text);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void searchatfeeds(String search)
    {
        initialState();
        getfeeds(false,"");
    }

    @Override
    public void onSearchCkicked(String test) {
        initialState();
        getfeeds(false,test);
    }

}

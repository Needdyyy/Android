package com.needyyy.app.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Profile.adapters.chat_ImageAdapter;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileOtherFragment;
import com.needyyy.app.Modules.Profile.models.Photo;
import com.needyyy.app.R;
import com.needyyy.app.mypage.model.pagedata.Data;
import com.needyyy.app.mypage.model.pagedata.GetPageData;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class my_chatpagedetails extends BaseFragment implements View.OnClickListener {
    private TabLayout tabLayout;
    private com.needyyy.app.ImageClasses.WrapContentViewPager viewPager;
    private Pager viewPagerAdapter;
    private String id;
    private boolean loading = false;
    private Data data;
    private Button invite;
    int noOfColumns;
    private ArrayList<Photo> arrPhotos = new ArrayList<>();
    private ArrayList<Photo> arrvideo = new ArrayList<>();
    RecyclerView gridPhoto, gridvideo;
    private Button follow;
    chat_ImageAdapter chat_imageAdapter;
    private TextView tvpagecreationtime;
    TextView likes, commenttv, followtv, address, website, contact;
    TextView tv_pagecreation_time;

    private CircleImageView circleImageView;
    private ImageView banner;


    public static my_chatpagedetails newInstance() {
        my_chatpagedetails fragment = new my_chatpagedetails();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatmypage_details, container, false);

        return view;

    }

    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    private void getPageData() {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetPageData> call = Service.getPageDetails(id);
        call.enqueue(new Callback<GetPageData>() {
            @Override
            public void onResponse(Call<GetPageData> call, Response<GetPageData> response) {
                //   Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetPageData getPageData = response.body();
                if (getPageData.getStatus()) {
                    if (getPageData.getStatus()) {
                        loading = !(getPageData.getData() == null);
                        data = getPageData.getData();
                        if (data.getWebsite() != null) {
                            website.setVisibility(View.VISIBLE);
                            website.setText("Website : " + data.getWebsite());
                        } else {
                            website.setVisibility(View.GONE);
                        }
                        if (data.getAddress() != null) {
                            address.setVisibility(View.VISIBLE);
                            address.setText("Address : " + data.getAddress());
                        } else {
                            address.setVisibility(View.GONE);
                        }
                        if (data.getContact() != null) {
                            contact.setVisibility(View.VISIBLE);
                            contact.setText("Contact no :" + data.getContact());
                        } else {
                            contact.setVisibility(View.GONE);
                        }
                        if (data.getCategory() != null) {
                            tv_pagecreation_time.setText(data.getCategory());
                        } else {
                            tv_pagecreation_time.setVisibility(View.GONE);
                        }
                        if (data.getLikes() != null)
                            likes.setText("Likes"+":"+data.getLikes());

                        if (data.getComments() != null)
                            commenttv.setText("Comments"+":"+data.getComments());

                        if (data.getFollowers() != null) {
                            followtv.setText("Followers"+":"+data.getFollowers());
                        }

                        if (data.getIsFollow().equals("1")) {
                            follow.setText("Unfollow");
                        } else {
                            follow.setText("Follow");
                        }


                        if (data.getVideos() != null) {
                            arrvideo.clear();
                            arrvideo.addAll(data.getVideos());
                        }
                        if (data.getPhotos() != null) {
                            arrPhotos.clear();
                            arrPhotos.addAll(data.getPhotos());
                            //chat_imageAdapter.notifyDataSetChanged();
                        }
                       /* if (data.getVideos()!=null)
                        {
                            arrPhotos.addAll(data.getVideos());
                            im.notifyDataSetChanged();
                        }*/
                        // tvpagecreationtime.setText(data.);

                        chat_imageAdapter = new chat_ImageAdapter(getContext(), arrPhotos,data.getId());
                        gridPhoto.setLayoutManager(new GridLayoutManager(getContext(), noOfColumns));
                        gridPhoto.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
                        gridPhoto.setAdapter(chat_imageAdapter);
                        gridPhoto.setNestedScrollingEnabled(false);


                        chat_imageAdapter = new chat_ImageAdapter(getContext(), arrvideo,"",data.getId());
                        gridvideo.setLayoutManager(new GridLayoutManager(getContext(), noOfColumns));
                        gridvideo.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
                        gridvideo.setAdapter(chat_imageAdapter);
                        gridvideo.setNestedScrollingEnabled(false);


                        // tvpagecreationtime.setText(data.);
                    } else {

                    }
                } else {
                    if (getPageData.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(getPageData.getMessage());
                    }
                }
                Glide.with(getContext())
                        .load(data.getBanner())
                        .into(banner);
                Glide.with(getContext())
                        .load(data.getProfile())
                        .into(circleImageView);
            }

            @Override
            public void onFailure(Call<GetPageData> call, Throwable t) {

                snackBar(t.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridvideo = view.findViewById(R.id.rv_chatVideo);
        tabLayout = (TabLayout) view.findViewById(R.id.tabss);
        viewPager = view.findViewById(R.id.viewPagers);
        banner = view.findViewById(R.id.banner);
        website = view.findViewById(R.id.website);
        contact = view.findViewById(R.id.contact);
        gridPhoto = view.findViewById(R.id.rv_chatphotos);
        circleImageView = view.findViewById(R.id.img_pageprofile);
        invite = view.findViewById(R.id.invite);
        follow = view.findViewById(R.id.follow);
        address = view.findViewById(R.id.address);
        likes = view.findViewById(R.id.like);
        commenttv = view.findViewById(R.id.comment);
        noOfColumns = calculateNoOfColumns(getActivity(), 120);
        followtv = view.findViewById(R.id.followtv);
        tv_pagecreation_time = view.findViewById(R.id.tv_pagecreation_time);
        invite.setOnClickListener(this);
        follow.setOnClickListener(this);

        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
        getPageData();

        tabLayout.addTab(tabLayout.newTab().setText(R.string.chat));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.post));




        viewPagerAdapter = new Pager(getContext(), getChildFragmentManager(), tabLayout.getTabCount(), id);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


    private void likeDislikeApi(int status) {
        //   ((HomeActivity)context).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.likeDislike(id, status, data.getPostType(), 3);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                //((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo myPage = response.body();
                if (myPage.getStatus()) {

                    if (data.getIsFollow().equals("1")) {
                        data.setIsFollow("0");
                        follow.setText("Follow");
                    } else {
                        data.setIsFollow("1");
                        follow.setText("Unfollow");
                    }


                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                snackBar(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite:
                ((HomeActivity) getContext()).replaceFragment(mypage_friendlist.newInstance(1, id), true);
                break;

            case R.id.follow:
                if (data.getIsFollow().equals("1")) {
                    likeDislikeApi(0);
                } else {
                    likeDislikeApi(1);
                }

                break;
        }
    }
}
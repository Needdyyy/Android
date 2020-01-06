package com.needyyy.app.Modules.adsAndPage.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.needyyy.app.Base.BaseFragment;

import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.adsAndPage.adapter.AdsManagerPagerAdapter;
import com.needyyy.app.R;

public class AdsManagerFragment extends BaseFragment implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager ;

    private AdsManagerPagerAdapter viewPagerAdapter ;
    private static final String TAG = "AdsManagerFragment";
    public AdsManagerFragment() {
        // Required empty public constructor
    }


    public static AdsManagerFragment newInstance() {
        AdsManagerFragment fragment = new AdsManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_ads_manager);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager!=null){
           viewPager.setCurrentItem(viewPager.getCurrentItem());
        }
    }

    @Override
    protected void initView(View mView) {
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        viewPager = mView.findViewById(R.id.viewPager);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("Pages", "2");
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabLayout.addOnTabSelectedListener(this);
        setupviewPager(viewPager);
    }

    private void setupviewPager(ViewPager viewPager) {
        viewPagerAdapter  = new AdsManagerPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment( MyPageFragment.newInstance(),getContext().getString(R.string.mypages));
        viewPagerAdapter.addFragment(CreatePageFragment.newInstance(),getContext().getString(R.string.create_page));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
            viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("response2","Image Data:"+new Gson().toJson(data));

        Fragment fragment=viewPagerAdapter.getmFragmentList().get(viewPager.getCurrentItem());
        fragment.onActivityResult(requestCode,resultCode,data);

    }
}

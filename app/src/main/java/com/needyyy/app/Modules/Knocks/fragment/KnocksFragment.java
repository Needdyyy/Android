package com.needyyy.app.Modules.Knocks.fragment;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.KnockRequestAdapter;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Knocks.models.ReceivedData;
import com.needyyy.app.Modules.Profile.fragments.FriendsListFragment;
import com.needyyy.app.R;
import com.needyyy.app.utils.OnFragmentVisibleListener;
import com.needyyy.app.utils.SearchListener;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnocksFragment extends BaseFragment implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewpager;

    public KnocksFragment() {
    }

    public static KnocksFragment newInstance() {
        KnocksFragment fragment = new KnocksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.fragment_knocks);
        if (getArguments() != null) {
        }
//        ((HomeActivity)getActivity()).onNavi(R.id.nv_knocks);

    }

    @Override
    protected void initView(View mView)
    {
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        viewpager = (ViewPager)mView. findViewById(R.id.viewpager);
        ((HomeActivity)getActivity()).manageToolbar("Knocks", "");
        setupViewPager(viewpager);
    }
    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    private void setupViewPager(ViewPager viewPager) {

        try{

            ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
            adapter.addFragment(new Allmember().newInstance(), "Suggestion");
            adapter.addFragment(new GetKnocksFriend().newInstance(), "Friends");
            adapter.addFragment(ReceivedKnocks.newInstance() , "Received");
            adapter.addFragment(new GetSendKnocks().newInstance(), "Sent Knocks");
            adapter.addFragment(new BlockedMember().newInstance(), "Blocked Memeber");

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


            viewPager.setCurrentItem(1, false);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(0, false);
                }
            },500);

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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).manageToolbar("Knocks", "");
        setupViewPager(viewpager);
    }
}

package com.needyyy.app;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    private String searchtext;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.sliding_tabs);
        if(getArguments()!=null)
        {
            searchtext=getArguments().getString("search");
        }

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(context,getChildFragmentManager(),searchtext);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1, false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(0, false);
            }
        },500);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }
}

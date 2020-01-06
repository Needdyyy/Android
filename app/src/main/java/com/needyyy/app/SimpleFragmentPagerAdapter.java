package com.needyyy.app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Knocks.fragment.Allmember;
import com.needyyy.app.mypage.my_chatpagedetails;
import com.needyyy.app.mypage.mypage_details;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String searchtext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm,String searchtext) {
        super(fm);
        this.searchtext=searchtext;
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return  HomeFragment.newInstance(searchtext);
        } else if (position == 1){
            return new Allmember();
        }
        return null;
    }
    // This determines the number of tabs

    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.page);
            case 1:
                return mContext.getString(R.string.Usersearch);
            default:
                return null;
        }
    }
}

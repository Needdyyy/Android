package com.needyyy.app.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import com.koushikdutta.ion.builder.Builders;
import com.needyyy.app.Chat.fragment.ChatFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;

public class Pager extends FragmentPagerAdapter {

    int tabCount;
    Context context;
    private String id;

    //Constructor to the class
    public Pager(Context context,FragmentManager fm, int tabCount,String id) {
        super(fm);
        this.context=context;
        //Initializing tab count
        this.tabCount = tabCount;
        this.id=id;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
               return ChatFragment.newInstance("page",id);
            case 1:
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                post post=new post();
                post.setArguments(bundle);
                return  post;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}

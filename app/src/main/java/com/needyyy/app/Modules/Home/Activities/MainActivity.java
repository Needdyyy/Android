package com.needyyy.app.Modules.Home.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.needyyy.app.Modules.Home.Fragments.SearchFriendFragment;
import com.needyyy.app.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        //showEnterDataFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showEnterDataFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SearchFriendFragment())
                .commit();
    }
}

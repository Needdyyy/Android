package com.needyyy.app.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.needyyy.app.Chat.fragment.MyGroupsFragment;
import com.needyyy.app.Modules.Login.Fragments.CreatePinAuthenticationFragment;
import com.needyyy.app.Modules.Login.Fragments.SignInFragment;
import com.needyyy.app.R;
import com.needyyy.app.utils.Progress;
import com.needyyy.app.views.AppTextView;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GroupActivity";
    private ImageView imgBack ;
    private AppTextView tvTitle;
    Progress mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mprogress = new Progress(GroupActivity.this);

        imgBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);

        imgBack.setOnClickListener(this);
        tvTitle.setText("Groups");

        addFragment(MyGroupsFragment.newInstance());
    }


    public void popStack() {
        getSupportFragmentManager().popBackStack();
    }

    public  void setTitle(String title){
        tvTitle.setText(title);
        //setVisiblity();
    }
    public void replaceFragment(Fragment fragment) {
        Log.d(TAG, "replaceFragment: " + getSupportFragmentManager().getBackStackEntryCount());
        String backStateName = fragment.getClass().getSimpleName();
        String fragmentTag = backStateName;

        FragmentManager manager = this.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_group, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
    public  void setVisiblity(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        if(fragment instanceof CreatePinAuthenticationFragment){
            imgBack.setVisibility(View.GONE);
        }else{
            imgBack.setVisibility(View.VISIBLE);
        }
    }
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame_group, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_group);
        if(fragment instanceof CreatePinAuthenticationFragment){
            popStack();
        } else if (fragment instanceof SignInFragment) {
            popStack();
        }
        else {
            super.onBackPressed();
        }
    }
    public void getActiveFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_group);
        popStack();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    public void showProgress(){
        if(mprogress!=null){
            mprogress.show();
        }
    }

    public void hideProgress(){
        if(mprogress!=null && mprogress.isShowing()){
            mprogress.dismiss();
        }
    }

}

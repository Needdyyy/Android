package com.needyyy.app.Modules.Login.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.koushikdutta.ion.builder.Builders;
import com.needyyy.app.Modules.Login.Fragments.CreatePinAuthenticationFragment;
import com.needyyy.app.Modules.Login.Fragments.CreatePinFragment;
import com.needyyy.app.Modules.Login.Fragments.SignInFragment;
import com.needyyy.app.R;
import com.needyyy.app.fingerprint.FingureprintFragment;
import com.needyyy.app.fingerprint.PinCheck;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.views.AppTextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ImageView imgBack ;
    private AppTextView tvTitle;
    private boolean closeApp = false;
    public android.widget.Toolbar toolbar;
    public String check=null;
    private String fingureprintenable=null;
    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        check=getIntent().getExtras().getString("fromsplash");
        fingureprintenable=getIntent().getExtras().getString("userdata");
        if(check.equals("1"))
        {
            if(fingureprintenable.equals("1"))
            {
                Bundle bundle=new Bundle();
                bundle.putString("fragment","1");
                FingureprintFragment fingureprintFragment=new FingureprintFragment();
                fingureprintFragment.setArguments(bundle);
                addFragment(fingureprintFragment);
            }
            else
            {
                addFragment(new PinCheck());
            }
        }
        else {
            addFragment(SignInFragment.newInstance());
        }
        imgBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    public void popStack() {
        getSupportFragmentManager().popBackStack();
    }

    public  void setTitle(String title){
        tvTitle.setText(title);
        setVisiblity();
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
            ft.replace(R.id.frame_main, fragment, fragmentTag);
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
        }
        else if(fragment instanceof FingureprintFragment || fragment instanceof PinCheck)
        {
            closeApp=true;
            imgBack.setVisibility(View.GONE);
        }
        else{
            imgBack.setVisibility(View.VISIBLE);
        }
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame_main, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        if(fragment instanceof CreatePinAuthenticationFragment){
            imgBack.setVisibility(View.GONE);
        } else if (fragment instanceof SignInFragment || fragment instanceof FingureprintFragment || fragment instanceof PinCheck) {

            if (closeApp) {
                finish();
                super.onBackPressed();
            } else {
                CommonUtil.showLongToast(LoginActivity.this, "Press back button again to exit ");
                new CountDownTimer(5000, 500) {
                    public void onTick(long millisUntilFinished) {
                        closeApp = true;
                    }
                    public void onFinish() {
                        closeApp = false;
                    }
                }.start();
            }
        }
        else {
            getActiveFragment();
        }
    }
    public void getActiveFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        popStack();
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}

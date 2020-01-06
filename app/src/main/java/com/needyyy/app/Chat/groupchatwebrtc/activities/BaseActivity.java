package com.needyyy.app.Chat.groupchatwebrtc.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.needyyy.AppController;
import com.needyyy.app.R;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.needyyy.app.Chat.groupchatwebrtc.gcm.GooglePlayServicesHelper;
import com.needyyy.app.Chat.groupchatwebrtc.util.QBResRequestExecutor;
import com.needyyy.app.Chat.groupchatwebrtc.utils.Consts;
import com.needyyy.app.Chat.groupchatwebrtc.utils.ErrorUtils;
import com.needyyy.app.Chat.groupchatwebrtc.utils.SharedPrefsHelper;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;
import com.needyyy.app.utils.Progress;

import androidx.annotation.StringRes;


import java.lang.reflect.Field;

/**
 * Anže Kožar
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected android.support.v7.app.ActionBar actionBar;

    Context mContext;
    SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progressDialog;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    protected QBResRequestExecutor requestExecutor;
    Progress mprogress;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 404;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        actionBar = getSupportActionBar();

        // Hack. Forcing overflow button on actionbar on devices with hardware menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        requestExecutor = AppController.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();
    }

    public void initDefaultActionBar() {
        String currentUserFullName = "";
        String currentRoomName = sharedPrefsHelper.get(Consts.PREF_CURREN_ROOM_NAME, "");

        if (sharedPrefsHelper.getQbUser() != null) {
            currentUserFullName = sharedPrefsHelper.getQbUser().getFullName();
        }

        setActionBarTitle(currentRoomName);
        setActionbarSubTitle(String.format(getString(R.string.subtitle_text_logged_in_as), currentUserFullName));
    }


    public void setActionbarSubTitle(String subTitle) {
        if (actionBar != null)
            actionBar.setSubtitle(subTitle);
    }

    public void removeActionbarSubTitle() {
        if (actionBar != null)
            actionBar.setSubtitle(null);
    }

    void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = (dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK;
            progressDialog.setOnKeyListener(keyListener);
        }

        progressDialog.setMessage(getString(messageId));

        progressDialog.show();

    }


    public void  showProgressDialog() {
        mprogress = new Progress(mContext);
        mprogress.setCancelable(false);
//        dialog=new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.loader_layout);
//
//        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        rotation.setFillAfter(true);
//        ImageView iv=dialog.findViewById(R.id.iv_loader);
//        iv.startAnimation(rotation);


        mprogress.show();
    }


    public void cancelProgressDialog() {
        if(mprogress.isShowing()) {
            mprogress.hide();
//            dialog=null;
        }
    }

    public void snackBar(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }



    void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showErrorSnackbar(@StringRes int resId, Exception e,
                                     View.OnClickListener clickListener) {
        if (getSnackbarAnchorView() != null) {
            ErrorUtils.showSnackbar(getSnackbarAnchorView(), resId, e,
                    R.string.dlg_retry, clickListener);
        }
    }

    protected abstract View getSnackbarAnchorView();

    public void setActionBarTitle(int title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setActionBarTitle(CharSequence title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void longToast(String message){
        Toast.makeText(mContext,message, Toast.LENGTH_LONG).show();
    }

    public void longToast(int resId){
        Toast.makeText(mContext,resId, Toast.LENGTH_LONG).show();
    }

    public void shortToast(String message){
        Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();
    }

    public void shortToast(int resId){
        Toast.makeText(mContext,resId, Toast.LENGTH_SHORT).show();
    }
}





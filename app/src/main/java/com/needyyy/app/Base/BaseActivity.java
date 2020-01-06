package com.needyyy.app.Base;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.needyyy.app.R;
import com.needyyy.app.utils.Progress;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    Context context;

//    public SpotsDialog progressDialog;
Progress mprogress;


    public static final int PICK_FROM_CAMERA = 3, PICK_FROM_FILE = 2;
    public static final int MEDIA_TYPE_IMAGE = 1, MEDIA_TYPE_VIDEO = 6,CUSTOM_CAMERA=4;

    public Uri fileUri = null;

    byte[] inputData = null;
    private static final String TAG = "BaseActivity";
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

    }



    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void popStack() {
        getSupportFragmentManager().popBackStack();
    }

    public  void popAllStack(){
        FragmentManager fm =this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void  showProgressDialog() {
        mprogress = new Progress(context);
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



    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 404;


    public boolean checkPermissions(Context context, String[] permissions) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
//            }else {
//                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                    buildAlertMessageNoGps();
//                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



}

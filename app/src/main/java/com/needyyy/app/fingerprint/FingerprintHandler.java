package com.needyyy.app.fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.Fragments.CreateFingerPrintAuthenticationFragment;
import com.needyyy.app.Modules.Login.Fragments.CreatePinAuthenticationFragment;
import com.needyyy.app.Modules.Login.Fragments.CreatePinFragment;
import com.needyyy.app.constants.Blocks.Block;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.needyyy.app.Modules.Login.Activities.LoginActivity.getIntent;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private CreateFingerPrintAuthenticationFragment createFingerPrintAuthenticationFragment;
    private CancellationSignal cancellationSignal;
    private Context context;
    private String status;


    public FingerprintHandler(Context mContext,String status) {
        context = mContext;
        this.status=status;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        Toast.makeText(context,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context,
                "Authentication failed",
                Toast.LENGTH_LONG).show();
        if(status.equals("1")) {
            if (context instanceof LoginActivity) {
                ((LoginActivity) context).replaceFragment(new PinCheck());
            }
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        Toast.makeText(context,
                "Authentication help\n" + helpString,
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        if(status.equals("1"))
        {
            Intent intent=new Intent(context,HomeActivity.class);
            context.startActivity(intent);
        }
        else
        {
            if(context instanceof LoginActivity)
            {
                ((LoginActivity) context).replaceFragment(CreatePinFragment.newInstance(1,"1"));
            }
        }

//           if(context instanceof HomeActivity)
//           {
//               ((HomeActivity) context).replaceFragment();
//           }
    }
}


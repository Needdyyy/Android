package com.needyyy.app.Modules.Login.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.needyyy.app.Base.BaseActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.views.AppTextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.annotation.RequiresApi;


public class CreateFingerPrintAuthenticationFragment extends BaseActivity implements View.OnClickListener {

    private AppTextView tvtitlemsg ,tvHintmsg,tvdone;
    private LinearLayout llCreatePin,llCreateFingerPrint;
    private ImageView imgBack,imgFinger;
    private FingerprintManager fingerprintManager;
    private ProgressBar CircularProgressBar;
    private KeyguardManager keyguardManager;
    private FingerprintHandler fingerprintHandler;
    private KeyStore keyStore;
    private Cipher cipher;
    private KeyGenerator keyGenerator;
    private String KEY_NAME = "AndroidKey";
    private static final String FINGERPRINT_KEY = "key_name";
    private static final int REQUEST_USE_FINGERPRINT = 300;

    private static String userString;
    private FingerprintManager.CryptoObject cryptoObject;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_fingerprint);
        initView();
        bindControls();
        fingerprintHandler = new FingerprintHandler(this);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        // check support for android fingerprint_small on device
        checkDeviceFingerprintSupport();
        //generate fingerprint_small keystore
        generateFingerprintKeyStore();
        //instantiate Cipher class
        Cipher mCipher = instantiateCipher();
        if(mCipher != null){
            cryptoObject = new FingerprintManager.CryptoObject(mCipher);
        }
        fingerprintHandler.completeFingerAuthentication(fingerprintManager, cryptoObject);
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkDeviceFingerprintSupport() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT);
        } else {
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(CreateFingerPrintAuthenticationFragment.this, "Fingerprint is not supported in this device", Toast.LENGTH_LONG).show();
            }
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(CreateFingerPrintAuthenticationFragment.this, "Fingerprint not yet configured", Toast.LENGTH_LONG).show();
            }
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(CreateFingerPrintAuthenticationFragment.this, "Screen lock is not secure and enable", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void generateFingerprintKeyStore(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator.init(new KeyGenParameterSpec.Builder(FINGERPRINT_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        keyGenerator.generateKey();
    }
    private Cipher instantiateCipher(){
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey)keyStore.getKey(FINGERPRINT_KEY, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnrecoverableKeyException |
                CertificateException | IOException | KeyStoreException | InvalidKeyException e) {
            throw new RuntimeException("Failed to instantiate Cipher class");
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_USE_FINGERPRINT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // check support for android fingerprint_small on device
                checkDeviceFingerprintSupport();
                //generate fingerprint_small keystore
                generateFingerprintKeyStore();
                //instantiate Cipher class
                Cipher mCipher = instantiateCipher();
                if(mCipher != null){
                    cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                }
            }
            else{
                Toast.makeText(this, R.string.permission_refused, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.Unknown_permission_request), Toast.LENGTH_LONG).show();
        }
    }

    protected void initView() {
        tvtitlemsg          = findViewById(R.id.tv_startmsg);
        tvHintmsg           = findViewById(R.id.tv_hintmsg);
        imgBack             = findViewById(R.id.img_back);
        tvdone              = findViewById(R.id.btn_done);
        CircularProgressBar = findViewById(R.id.circularProgressbar);
        imgFinger           = findViewById(R.id.img_finger);
    }


    protected void bindControls() {

//        mHeadingLabel = (TextView) findViewById(R.id.headingLabel);
//        mFingerprintImage = (ImageView) findViewById(R.id.fingerprintImage);
//        mParaLabel = (TextView) findViewById(R.id.paraLabel);

        // Check 1: Android version should be greater or equal to Marshmallow
        // Check 2: Device has Fingerprint Scanner
        // Check 3: Have permission to use fingerprint_small scanner in the app
        // Check 4: Lock screen is secured with atleast 1 type of lock
        // Check 5: Atleast 1 Fingerprint is registered

        tvdone.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_done:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;


        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public  class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

        private  final String TAG = FingerprintHandler.class.getSimpleName();

        private Context context;

        public FingerprintHandler(Context context){
            this.context = context;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Toast.makeText(context, R.string.auth_successful, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            ObjectAnimator animation = ObjectAnimator.ofInt(CircularProgressBar, "progress", 0, 100); // see this max value coming back here, we animate towards that value
            animation.setDuration(2000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    tvtitlemsg.setText(context.getString(R.string.done));
                    tvHintmsg.setText(context.getString(R.string.auth_successful));
                    imgFinger.setImageResource(R.drawable.fingerprint_blue);
                    tvdone.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try{
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            }catch (SecurityException ex) {
                Log.d(TAG, "An error occurred:\n" + ex.getMessage());
            } catch (Exception ex) {
                Log.d(TAG, "An error occurred\n" + ex.getMessage());
            }
        }
    }

}

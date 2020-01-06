package com.needyyy.app.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.needyyy.app.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainActivity extends AppCompatActivity {

    private static final String APP_ID = "78030";
    private static final String AUTH_KEY = "af72FtfUNJAkyL9";
    private static final String AUTH_SECRET = "B4xkMzhbc8MVLt7";
    private static final String ACCOUNT_KEY = "7yvNe17TnjNUqDoPwfqp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //registersession();
        intializeframework();
        //registersession();
        login();


    }

    private void registersession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                signup();
                Toast.makeText(getApplicationContext(),"session sucess",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(),"session falied",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intializeframework() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    public void signup()
    {
        QBUser qbUsers=new QBUser("kaka","12345678");
        QBUsers.signUp(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(getApplicationContext(),"signup sucess",Toast.LENGTH_SHORT).show();
                login();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(),"signup failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        QBUser qbUsers=new QBUser("kaka","12345678");
        QBUsers.signIn(qbUsers).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(getApplicationContext(),"login sucess",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(MainActivity.this, chatDialogActivity.class);
                intent.putExtra("user","kaka");
                intent.putExtra("password","12345678");
                startActivity(intent);
            }

            @Override
            public void onError(QBResponseException e) {


//                signup();
                Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.needyyy.app.Chat.groupchatwebrtc.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.needyyy.app.R;
import androidx.annotation.Nullable;


import com.needyyy.app.Chat.groupchatwebrtc.services.CallService;
import com.needyyy.app.Chat.groupchatwebrtc.utils.Consts;
import com.needyyy.app.Chat.groupchatwebrtc.utils.KeyboardUtils;
import com.needyyy.app.Chat.groupchatwebrtc.utils.QBEntityCallbackImpl;
import com.needyyy.app.Chat.groupchatwebrtc.utils.SharedPrefsHelper;
import com.needyyy.app.Chat.groupchatwebrtc.utils.UsersUtils;
import com.needyyy.app.Chat.groupchatwebrtc.utils.ValidationUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;

import com.quickblox.users.model.QBUser;

public class LoginActivity extends BaseActivity {

    private EditText userNameEditText;
    private EditText chatRoomNameEditText;
    Button btnLogin;

    private QBUser userForSave;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initUI();
    }

    @Override
    protected View getSnackbarAnchorView() {
      return findViewById(R.id.root_view_login_activity);
    }

    private void initUI() {
        setActionBarTitle(R.string.title_login_activity);
        userNameEditText = findViewById(R.id.user_name);
        chatRoomNameEditText = findViewById(R.id.chat_room_name);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                    hideKeyboard();
                    startSignUpNewUser(createUserWithEnteredData());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login_user_done:
                if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                    hideKeyboard();
                    startSignUpNewUser(createUserWithEnteredData());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isEnteredRoomNameValid() {
        return ValidationUtils.isRoomNameValid(this, chatRoomNameEditText);
    }

    private boolean isEnteredUserNameValid() {
        return ValidationUtils.isUserNameValid(this, userNameEditText);
    }

    private void hideKeyboard() {
        KeyboardUtils.hideKeyboard(userNameEditText);
        KeyboardUtils.hideKeyboard(chatRoomNameEditText);
    }

    private void startSignUpNewUser(final QBUser newUser) {
        showProgressDialog(R.string.dlg_creating_new_user);
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser, true);
                        } else {
                            hideProgressDialog();
                            Toast.makeText(mContext,R.string.sign_up_error, Toast.LENGTH_LONG);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void startOpponentsActivity() {
        OpponentsActivity.start(LoginActivity.this, false);
        finish();
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(String.valueOf(userNameEditText.getText()),
                String.valueOf(chatRoomNameEditText.getText()));
    }

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);

            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(getCurrentDeviceId());
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            hideProgressDialog();
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);

                signInCreatedUser(userForSave, false);
            } else {
                Toast.makeText(mContext,getString(R.string.login_chat_login_error) + errorMessage, Toast.LENGTH_LONG).show();
                userNameEditText.setText(userForSave.getFullName());
                chatRoomNameEditText.setText(userForSave.getTags().get(0));
            }
        }
    }

    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
                    removeAllUserData(result);
                } else {
                    startOpponentsActivity();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                hideProgressDialog();
                Toast.makeText(mContext,R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                Toast.makeText(mContext,R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }

    private String getCurrentDeviceId() {
        return Utils.generateDeviceId(this);
    }
}

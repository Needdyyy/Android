package com.needyyy.app.Modules.Home.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.policy.Resource;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.builder.Builders;
import com.needyyy.AppController;
import com.needyyy.app.Chat.GroupActivity;
import com.needyyy.app.Chat.chatDialogActivity;
import com.needyyy.app.Chat.groupchatwebrtc.activities.OpponentsActivity;
import com.needyyy.app.Chat.groupchatwebrtc.services.CallService;
import com.needyyy.app.Chat.groupchatwebrtc.utils.Consts;
import com.needyyy.app.Chat.groupchatwebrtc.utils.QBEntityCallbackImpl;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Listener;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.Dating.FilterFragment;
import com.needyyy.app.Modules.Dating.FriendSuggestion;
import com.needyyy.app.Modules.Dating.suggestions.fragment.SendRequestFragment;
import com.needyyy.app.Modules.Home.Fragments.ContactUsFragment;
import com.needyyy.app.Modules.Home.Fragments.FeedsFragment;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Home.Fragments.NotificationFragment;
import com.needyyy.app.Modules.Home.Fragments.SearchFriendFragment;
import com.needyyy.app.Modules.Home.Fragments.SecurityFragment;
import com.needyyy.app.Modules.Home.Fragments.ViewFullImageFragment;
import com.needyyy.app.Modules.Home.Fragments.WalletFragment;
import com.needyyy.app.Modules.Home.modle.PostMetum;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Knocks.fragment.KnocksFragment;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.fragments.FriendsListFragment;
import com.needyyy.app.Modules.Profile.fragments.SeeAllPhotoFragment;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.AdsManagerFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.BookmarkFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.GlobalPostFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.MyPageFragment;
import com.needyyy.app.Modules.adsAndPage.modle.MyPage;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.GlobalPost;
import com.needyyy.app.R;
import com.needyyy.app.SearchFragment;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.EmotionData;
import com.needyyy.app.mypage.EmotionFragment;
import com.needyyy.app.mypage.model.Activities.Getpostdata;
import com.needyyy.app.mypage.model.EmotionStausFragment;
import com.needyyy.app.mypage.mypage_details;
import com.needyyy.app.utils.BottomNavigationViewBehavior;
import com.needyyy.app.utils.BottomNavigationViewHelper;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.OnFragmentVisibleListener;
import com.needyyy.app.utils.SearchListener;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.model.QBUser;
import com.razorpay.PaymentResultListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.Modules.adsAndPage.fragment.GlobalPostFragment.tv_writesomething;
import static com.needyyy.app.constants.Constants.kCurrentUser;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class HomeActivity extends com.needyyy.app.Chat.groupchatwebrtc.activities.BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PaymentResultListener, EmotionData, OnFragmentVisibleListener {
    private String idd = "";
    public PostResponse postresponse;
    public MyPage myPage;
    public static long count1;
    public static long count2;
    Boolean status=false,statusdating=false;
    public long count =0,var=0,i=0,countdating=0,vardating=0,j=0;
    ArrayList<Getpostdata> getPostData = new ArrayList<>();
    public static final int TAG_USER = 2929;
    public static final int TAG_USER2 = 2930;
    private Boolean checktypenoti = false;
    public static final int DIALOG_FRAGMENT = 1;
    private File destFile;
    private File file;
    private File sourceFile;
    public static FragmentManager manager;
    private SimpleDateFormat dateFormatter;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String TAG = "HomeActivity";
    public CircleImageView imgProfile;
    public Fragment fragment;
    public BottomNavigationView navigation, navigation1;
    private UserDataResult userData;
    public BottomNavigationItemView nvHome, nvPost, nvKnocks, nvProfile, nvfeeds;
    private View SocialView, DiatView;
    private LinearLayout llSocial, llDating;
    Button toolbar_post;
    private SearchView toolbarSearch;
    private DatabaseReference mUserDatabase;
    private String memberId, type;
    private int taskid;
    private Boolean isSocial = true;
    private TextView notificationcount, chatcount;
    private BroadcastReceiver mReceiver;
    DatabaseReference mDatabaseReference;
    private static int  notificationn;

    public static int loadCount=0;

    private TextView toolbarTitle, tvSocial, tvUserName, tvDating, tvHomeSocial, tvChatgroupSocial, tvBookmarkSocial, tv_global_post, tvNearfriendSocial, tvAddmanagerSocial, tvMypageSocial, tvWalletSocial, tvAboutusSocial, tvSecuritySocial, tvHelpsupportSocial, tvLogoutSocial, tvHomeDaiting, tvAboutusDaiting, tvSecurityDaiting, tvHelpsupportDaiting, tvLogoutDaiting;
    private ImageView ivSocial, ivDating, toolbarMenu, toolbarBack, toolbarChat, toolbarVideoCall, toolbarNotification, toolbarFilter, coverpic;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nv_home:
                    tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                    tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_global_post.setBackgroundColor(getResources().getColor(R.color.white));
                    tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    popAllStack();
                    replaceFragment(HomeFragment.newInstance(), false);
                    return true;

                case R.id.nv_post:

                    tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    replaceFragment(PostFragment.newInstance(null), true);
                    return true;

                case R.id.nv_knocks:
                    tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    replaceFragment(KnocksFragment.newInstance(), true);
                    return true;

                case R.id.nv_profile:
                    tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    replaceFragment(ViewProfileFragment.newInstance(userData.getId(), null), true);
                    return true;

                case R.id.nv_feeeds:
                    tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_global_post.setBackgroundColor(getResources().getColor(R.color.white));
                    tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                    tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                    replaceFragment(FeedsFragment.newInstance(), true);
                    return true;

                case R.id.nv_suggestion:
                    replaceFragment(FriendSuggestion.newInstance(), true);
                    return true;

                case R.id.nv_myfriend:
                    replaceFragment(FriendsListFragment.newInstance(1), true);
                    return true;

                case R.id.nv_profile_dating:
                    replaceFragment(ViewProfileFragment.newInstance(userData.getId(), null), true);
                    return true;

                case R.id.nv_knocks_dating:
                    replaceFragment(KnocksFragment.newInstance(), true);
                    return true;
            }
            return false;
        }
    };


    private boolean closeApp = false;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mDatabaseReference.child(userData.getId()).child("is_online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("status1",String.valueOf(status));
        status=false;statusdating=false;countdating=0;count=0;var=0;vardating=0;i=0;j=0;
        Log.d("status2",String.valueOf(status));
    }

    DatabaseReference databaseReference ;

    @Override
    protected void onStart() {
        super.onStart();
        if (userData == null) {

        } else {
            //---IF LOGIN , ADD ONLINE VALUE TO TRUE---
            mDatabaseReference.child(userData.getId()).child("is_online").setValue("online");
        }


        databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(userData.getId());

//        if(loadCount==0){
//
//            loadCount++;

        if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
           count=0;
            //  Query lastQuery = databaseReference.limitToLast(1);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();


                        for (Map.Entry me : td2.entrySet()) {

                            DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(me.getKey().toString()).child(userData.getId());

                            mMessagecount.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(AppController.getManager().getInterest().equals(Constant.SOCIAL)) {

                                        Log.d("friendss", dataSnapshot.toString());
                                        if (dataSnapshot.getValue() != null) {
                                            Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                                            for (Map.Entry me : td.entrySet()) {
                                                Log.d("friend name", me.toString());

                                                if (me.getKey().toString().equals("count")) {
                                                    var = Long.parseLong(me.getValue().toString());
                                                    Log.d("count", String.valueOf(var));
                                                } else {
                                                    Log.d("values", "error");
                                                }
                                                if (td2.size() > 0) {

                                                } else {
                                                    break;
                                                }
                                            }
                                            Log.d("statusDetails", String.valueOf(status));
                                            if (i == (td2.size())) {
                                                status = true;
                                            }

                                            count = count + var;
                                            i++;

                                            if (count == 0) {
                                                chatcount.setVisibility(View.GONE);
                                            } else {
                                                SetValue(count,countdating);
                                              //  chatcount.setVisibility(View.VISIBLE);
                                                //   chatcount.setText(String.valueOf(count));
                                            }
                                        } else {
                                            chatcount.setVisibility(View.GONE);
                                        }
                                    }
                                    else
                                    {
                                        chatcount.setVisibility(View.GONE);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                    else {
                        chatcount.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        } else if (AppController.getManager().getInterest().equals(Constant.DATING)) {
            countdating = 0;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(userData.getId());
            //  Query lastQuery = databaseReference.limitToLast(1);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();
                        for (Map.Entry me : td2.entrySet()) {
                            DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(me.getKey().toString()).child(userData.getId());

                            mMessagecount.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("friendss", dataSnapshot.toString());
                                    if(AppController.getManager().getInterest().equals(Constant.DATING)) {
                                        if (dataSnapshot.getValue() != null) {
                                            Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                                            for (Map.Entry me : td.entrySet()) {
                                                Log.d("friend name", me.toString());

                                                if (me.getKey().toString().equals("count")) {
                                                    vardating = Long.parseLong(me.getValue().toString());
                                                    Log.d("count", String.valueOf(var));
                                                } else {
                                                    Log.d("values", "error");
                                                }
                                            }
                                            if (j == (td2.size())) {
                                                statusdating = true;
                                            }

                                                countdating = countdating + vardating;
                                                j++;

                                            if (countdating == 0) {
                                                chatcount.setVisibility(View.GONE);
                                            } else {
                                                SetValue(count,countdating);
                                                //  chatcount.setText(String.valueOf(countdating));
                                            }
                                            Log.d("addition", String.valueOf(countdating));
                                        } else {
                                            chatcount.setVisibility(View.GONE);

                                        }
                                    }
                                    else
                                    {
                                        chatcount.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                    }
                    else {
                        chatcount.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

        }

        }



//        DatabaseReference mMessagecount2=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(userData.getId());
//
//        mMessagecount2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String key =  dataSnapshot.getKey();
//
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

  //  }

//    private void fireBaseOperation() {
////        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child(SharedPreference.getInstance().getLoggedInUser().getId());
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/users/"+userData.getId());
////        mFirebaseDatabaseReferenceName = FirebaseDatabase.getInstance().getReference().child("user").child(SharedPreference.getInstance().getLoggedInUser().getId());
//
//        ivSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // chatHit();
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy");
//                String datetime = dateformat.format(c.getTime());
//                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//
//                if (!etMessage.getText().toString().equals("")) {
//                    ChatPojo message = new ChatPojo(userData.getId(), etMessage.getText().toString(),userData.getName(),datetime,"1",userData.getProfilePicture(),"message","",currentTime,"1");
//                    mFirebaseDatabaseReference.push().setValue(message);
//                    etMessage.setText("");
//                } else {
////                    Helper.showSnackBar(ivSend, "Please enter your query first.");
//                }
//            }
//        });
//
//        arrChat.clear();
//        chatAdapter = new ChatAdapter(this, arrChat);
//        recyclerChat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        recyclerChat.setAdapter(chatAdapter);
//
//        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.getValue() != null) {
//                    //user exists, do something
//                } else {
//
//                    Log.d(TAG, "onDataChange: ");
//                    //user does not exist, do something else
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//
//        mFirebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                ChatPojo message = dataSnapshot.getValue(ChatPojo.class);
//                Log.d(TAG, "onChildAdded: "+message.getId());
//                arrChat.add(message);
//                chatAdapter.notifyDataSetChanged();
//                recyclerChat.smoothScrollToPosition(arrChat.size() - 1);
//
//            }
//
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildChanged: ");
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onChildRemoved: ");
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildMoved: ");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    static Listener mListener;

    public static void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        Uri data = intent.getData();
      //  AppController.getManager().setInterest(Constant.SOCIAL);
        addFragment(HomeFragment.newInstance());
        if (data != null) {
            String postid = data.getQueryParameter("postId");

            Log.d("resonse", "postId:" + postid);
            if (postid != null) {
                getpostdata(postid);
            }
            else {

            }
        }
        registerReceiver();


        checktypenoti = BaseManager.getnotificationstatus();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/users");
        mUserDatabase=  FirebaseDatabase.getInstance().getReference().child("needyyy/notification_management/dating_private");

        if (getIntent().hasExtra(Constant.PUSH_TYPE)) {
            type = getIntent().getStringExtra(Constant.PUSH_TYPE);
            taskid = Integer.parseInt(getIntent().getStringExtra("taskid"));
            switch (Integer.parseInt(type)) {
                case 1:
                    replaceFragment(HomeFragment.newInstance(), true);
                    break;
                case 2:
                    logout();
                    break;
                case 3:
                    replaceFragment(HomeFragment.newInstance(), true);
                    break;
                case 5:
                case 20:
                    replaceFragment(new com.needyyy.app.notifications.NotificationFragment(), true);
                    break;
                case 15:
                    getpostdata(String.valueOf(taskid));
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                    getpostdata(String.valueOf(taskid));
                    break;
                case 25:
                case 26:
                case 27:
                    replaceFragment(new mypage_details().newInstance(String.valueOf(taskid)), true);
                    break;
                case 28:
                case 29:
                    replaceFragment(new mypage_details().newInstance(String.valueOf(taskid)), true);
                    break;
                case 40:
                    replaceFragment(WalletFragment.newInstance(), true);
                    break;
                case 50:
                    replaceFragment(ViewProfileFragment.newInstance(String.valueOf(taskid), null), true);
                    break;
                case 51:
                    replaceFragment(ViewProfileFragment.newInstance(String.valueOf(taskid), null), true);
                    break;
                case 35:
                            AppController.getManager().setInterest(Constant.DATING);
                            Intent intent1 = new Intent(HomeActivity.this, chatDialogActivity.class);
                    //CommonUtil.showLongToast(this, "under development");
                    //  Intent intent=new Intent(HomeActivity.this, com.needyyy.app.Chat.groupchatwebrtc.activities.LoginActivity.class);
                    intent1.putExtra("activity", "isha2");
                    intent1.putExtra("password", "12345678");
                    intent1.putExtra("selectedTab",1);
                    startActivity(intent1);
                    break;
                case 36:
                    replaceFragment(new com.needyyy.app.notifications.NotificationFragment(), true);
                    break;
                case 37:
                    replaceFragment(ViewProfileFragment.newInstance(String.valueOf(taskid), null), true);
                    break;
                case 60:
                    AppController.getManager().setInterest(Constant.SOCIAL);
                    Intent intent3 = new Intent(HomeActivity.this, chatDialogActivity.class);
                    //CommonUtil.showLongToast(this, "under development");
                    //  Intent intent=new Intent(HomeActivity.this, com.needyyy.app.Chat.groupchatwebrtc.activities.LoginActivity.class);
                    intent3.putExtra("activity", "isha2");
                    intent3.putExtra("password", "12345678");
                    intent3.putExtra("selectedTab",0);
                    startActivity(intent3);
                    break;
                case 61:
                    AppController.getManager().setInterest(Constant.DATING);
                    Intent intent2 = new Intent(HomeActivity.this, chatDialogActivity.class);
                    //CommonUtil.showLongToast(this, "under development");
                    //  Intent intent=new Intent(HomeActivity.this, com.needyyy.app.Chat.groupchatwebrtc.activities.LoginActivity.class);
                    intent2.putExtra("activity", "isha2");
                    intent2.putExtra("password", "12345678");
                    intent2.putExtra("selectedTab",0);
                    startActivity(intent2);
                    break;
            }
        }

        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        //signInCreatedUser(createUserWithEnteredData(), false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation1 = (BottomNavigationView) findViewById(R.id.navigation1);
        BottomNavigationViewHelper.removeShiftMode(navigation1);
        navigation1.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) navigation1.getLayoutParams();
        layoutParams1.setBehavior(new BottomNavigationViewBehavior());

        setReference();
        setListner();
        initSearchView();
        if (userData != null) {
            tvUserName.setText(userData.getName());
            if (!TextUtils.isEmpty(userData.getProfilePicture())) {
                Glide.with(this)
                        .load(userData.getProfilePicture())
                        .into(imgProfile);
            } else {
                imgProfile.setImageResource(R.drawable.needyy);
            }

            if (!TextUtils.isEmpty(userData.getCoverpicture())) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT));
                Glide.with(this)
                        .load(userData.getCoverpicture())
                        .apply(requestOptions)
                        .into(coverpic);

            }
        }

//        if(notificationn==0)
//        {
//            notificationcount.setVisibility(View.GONE);
//        }
//
//        else
//        {
//            notificationcount.setVisibility(View.VISIBLE);
//            notificationcount.setText(String.valueOf(notificationn));
//        }

        getnotification(userData.getId());
    }

    @SuppressLint("RestrictedApi")
    public void setNotificationcount() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        if (fragment instanceof HomeFragment
                || fragment instanceof FeedsFragment
                || fragment instanceof KnocksFragment || fragment instanceof PostFragment||fragment instanceof FriendsListFragment || fragment instanceof FriendSuggestion)
        {
            if (notificationn == 0) {
                //  nvKnocks.setIcon(getResources().getDrawable(R.drawable.unblock));
                notificationcount.setVisibility(View.GONE);
            } else {
                //nvKnocks.setIcon(getResources().getDrawable(R.drawable.unblock));
                notificationcount.setVisibility(View.VISIBLE);
                notificationcount.setText(String.valueOf(notificationn));
            }
        }
        else
        {
            notificationcount.setVisibility(View.GONE);
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                loginToChat(result);
                if (deleteCurrentUser) {
                    // removeAllUserData(result);
                } else {
                    //start your home activity here
                    //startActivity(new Intent(getActivity(), HomeActivity.class));
                    // startOpponentsActivity();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {

                Toast.makeText(HomeActivity.this, R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
        StringifyArrayList<String> userTags = new StringifyArrayList<>();
        userTags.add("needy");
        startLoginService(qbUser);
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }


    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(userData.getId(), String.valueOf("needy"));
    }

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);

            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(userName);
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }

    public void closeToolbarSearch() {
        if (toolbarSearch != null)
            if (!toolbarSearch.isIconified()) {
                toolbarSearch.setIconified(true);
                toolbarSearch.onActionViewCollapsed();
                AppController.getManager().setSearchQuery("");
            }
    }

    private void initSearchView() {

        toolbarSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                if (fragment instanceof SearchFriendFragment)
                    getActiveFragment();
                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarChat.setVisibility(View.VISIBLE);
                toolbarNotification.setVisibility(View.VISIBLE);
                setNotificationcount();
                SetValue(count,countdating);
                AppController.getManager().setSearchQuery("");
                return false;
            }
        });


        toolbarSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                toolbarChat.setVisibility(View.GONE);
                chatcount.setVisibility(View.GONE);
                toolbarNotification.setVisibility(View.GONE);
                notificationcount.setVisibility(View.GONE);
//                toolbarSearch.setFocusable(true);
//                fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
//                if (fragment instanceof HomeFragment)
//                 //   replaceFragment(SearchFriendFragment.newInstance(), true);
//               replaceFragment(new SearchFragment(), true);
            }
        });

        View searchPlateView = toolbarSearch.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent
        ));
        // use this method for search process
        toolbarSearch.setQueryHint(getResources().getString(R.string.search_here));
        toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("response", "onQueryTextSubmit");
                toolbarChat.setVisibility(View.GONE);
                chatcount.setVisibility(View.GONE);
                toolbarNotification.setVisibility(View.GONE);
                AppController.getManager().setSearchQuery(query);
                // use this method when query submitted
                fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                if (fragment instanceof HomeFragment) {
                    //   replaceFragment(SearchFriendFragment.newInstance(), true);
                    SearchFragment searchFragment = new SearchFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("search", query);
                    searchFragment.setArguments(bundle);
                    replaceFragment(searchFragment, true);
                    toolbarTitle.setVisibility(View.VISIBLE);
                }

                if (fragment instanceof FeedsFragment) {
                    if (mListener != null) {
                        mListener.onSearchCkicked(query);
                    }
                    toolbarTitle.setVisibility(View.VISIBLE);
                    /*FeedsFragment feedsFragment=new FeedsFragment();
                    feedsFragment.searchatfeeds(query);
                    replaceFragment(feedsFragment,true);*/
                }
                if (searchListener != null) {
                    Log.d("response", "searchListener !=null");
                    searchListener.onClickSearch(query);
                    toolbarTitle.setVisibility(View.VISIBLE);
                }
                //SearchDataonFragment(fragment, 0);
                CommonUtil.closeKeyboard(HomeActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("response", "onQueryTextChange");
                AppController.getManager().setSearchQuery(newText);

                //performSearch(searchString);

                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        toolbarSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

       /* etSearch = findViewById(R.id.search_yo);
        etSearch.setFocusable(false);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setFocusableInTouchMode(true);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etSearch.setFocusableInTouchMode(true);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonUtil.hideKeyboard(activity, etSearch);
                    String searchString = etSearch.getText().toString();
                    //performSearch(searchString);
                    if(searchListener !=null){
                        searchListener.onClickSearch(searchString);
                    }
                    return true;
                }
                return false;
            }
        });*/
    }

    private void SearchDataonFragment(Fragment fragment, int i) {
        if (fragment instanceof SearchFriendFragment) {
            ((SearchFriendFragment) fragment).isRefresh = true;
            ((SearchFriendFragment) fragment).last_post_id = "";
            ((SearchFriendFragment) fragment).firstVisibleItem = 0;
            ((SearchFriendFragment) fragment).previousTotalItemCount = 0;
            ((SearchFriendFragment) fragment).visibleItemCount = 0;

            ((SearchFriendFragment) fragment).RefreshFeedList(true);
        }
    }


    private void setListner() {
//        toolbarVideoCall.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        llSocial.setOnClickListener(this);
        llDating.setOnClickListener(this);
        tvHomeSocial.setOnClickListener(this);
        tvChatgroupSocial.setOnClickListener(this);
        tvBookmarkSocial.setOnClickListener(this);
        tv_global_post.setOnClickListener(this);
        tvNearfriendSocial.setOnClickListener(this);
        tvAddmanagerSocial.setOnClickListener(this);
        tvMypageSocial.setOnClickListener(this);
        tvWalletSocial.setOnClickListener(this);
        tvAboutusSocial.setOnClickListener(this);
        tvSecuritySocial.setOnClickListener(this);
        tvHelpsupportSocial.setOnClickListener(this);
        tvLogoutSocial.setOnClickListener(this);
        tvHomeDaiting.setOnClickListener(this);
        tvAboutusDaiting.setOnClickListener(this);
        tvSecurityDaiting.setOnClickListener(this);
        tvHelpsupportDaiting.setOnClickListener(this);
        tvLogoutDaiting.setOnClickListener(this);
        toolbarBack.setOnClickListener(this);
        toolbarMenu.setOnClickListener(this);
        toolbarSearch.setOnClickListener(this);
        toolbarChat.setOnClickListener(this);
        toolbarNotification.setOnClickListener(this);
        toolbarFilter.setOnClickListener(this);
        toolbar_post.setOnClickListener(this);
    }

    private void setReference() {
        //toolbarVideoCall=findViewById(R.id.video_call);
        chatcount = findViewById(R.id.chatcount);
        notificationcount = findViewById(R.id.count);
        toolbar_post = findViewById(R.id.toolbar_post);
        SocialView = findViewById(R.id.layout_social);
        tvUserName = findViewById(R.id.tv_username);
        DiatView = findViewById(R.id.layout_daiting);
        llSocial = findViewById(R.id.ll_social);
        llDating = findViewById(R.id.ll_daiting);
        tvSocial = findViewById(R.id.tv_social);
        tvDating = findViewById(R.id.tv_daiting);
        ivSocial = findViewById(R.id.img_social);
        ivDating = findViewById(R.id.img_daiting);
        tvHomeSocial = findViewById(R.id.tv_home_social);
        tvChatgroupSocial = findViewById(R.id.tv_chatgroup_social);
        tvBookmarkSocial = findViewById(R.id.tv_bookmark_social);
        tv_global_post = findViewById(R.id.tv_global_post);
        tvNearfriendSocial = findViewById(R.id.tv_nearfriend_social);
        tvAddmanagerSocial = findViewById(R.id.tv_addmanager_social);
        tvMypageSocial = findViewById(R.id.tv_mypage_social);
        tvWalletSocial = findViewById(R.id.tv_wallet_social);
        tvAboutusSocial = findViewById(R.id.tv_aboutus_social);
        tvSecuritySocial = findViewById(R.id.tv_Security_social);
        tvHelpsupportSocial = findViewById(R.id.tv_helpsupport_social);
        tvLogoutSocial = findViewById(R.id.tv_logout_social);
        tvHomeDaiting = findViewById(R.id.tv_home_daiting);
        tvAboutusDaiting = findViewById(R.id.tv_aboutus_daiting);
        tvSecurityDaiting = findViewById(R.id.tv_Security_daiting);
        tvHelpsupportDaiting = findViewById(R.id.tv_helpsupport_daiting);
        tvLogoutDaiting = findViewById(R.id.tv_logout_daiting);
        nvHome = findViewById(R.id.nv_home);
        nvPost = findViewById(R.id.nv_post);
        nvKnocks = findViewById(R.id.nv_knocks);
        nvProfile = findViewById(R.id.nv_profile);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarMenu = findViewById(R.id.toolbar_menu);
        nvfeeds = findViewById(R.id.nv_feeeds);
        toolbarSearch = findViewById(R.id.toolbar_search);
        toolbarChat = findViewById(R.id.toolbar_chat);
        toolbarFilter = findViewById(R.id.toolbar_filter);
        toolbarNotification = findViewById(R.id.toolbar_notification);
        imgProfile = findViewById(R.id.civ_profile_pic);
        coverpic = findViewById(R.id.cover_photo);
    }

    public void manageToolbar(String title, String memberId) {
        this.memberId = memberId;
        toolbarTitle.setText(title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        if (memberId.equals("2")) {
            toolbar_post.setVisibility(View.GONE);
            toolbarMenu.setVisibility(View.GONE);
            toolbarBack.setVisibility(View.VISIBLE);
            notificationcount.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
            toolbarNotification.setVisibility(View.GONE);
            toolbarSearch.setVisibility(View.GONE);
            toolbarChat.setVisibility(View.GONE);
            chatcount.setVisibility(View.GONE);
        }
        if (!(fragment instanceof SearchFriendFragment))

            closeToolbarSearch();
        if (fragment instanceof HomeFragment
                || fragment instanceof FeedsFragment
                || fragment instanceof KnocksFragment) {
            toolbarMenu.setVisibility(View.VISIBLE);
//            toolbarVideoCall.setVisibility(View.VISIBLE);
            toolbarBack.setVisibility(View.GONE);
            // notificationcount.setVisibility(View.VISIBLE);
            setNotificationcount();
            SetValue(count,countdating);
            navigation.setVisibility(View.VISIBLE);
            toolbarFilter.setVisibility(View.GONE);
            toolbar_post.setVisibility(View.GONE);
            toolbarNotification.setVisibility(View.VISIBLE);
            toolbarSearch.setVisibility(View.VISIBLE);
            toolbarChat.setVisibility(View.VISIBLE);
        } else if (fragment instanceof PostFragment) {
            toolbarMenu.setVisibility(View.VISIBLE);
//            toolbarVideoCall.setVisibility(View.VISIBLE);
            toolbarBack.setVisibility(View.GONE);
            // notificationcount.setVisibility(View.VISIBLE);
            setNotificationcount();
            SetValue(count,countdating);
            navigation.setVisibility(View.VISIBLE);
            toolbarFilter.setVisibility(View.GONE);
            toolbar_post.setVisibility(View.GONE);
            toolbarNotification.setVisibility(View.VISIBLE);
            toolbarSearch.setVisibility(View.GONE);
            toolbarChat.setVisibility(View.VISIBLE);
        } else if (fragment instanceof ViewProfileFragment
                || fragment instanceof com.needyyy.app.notifications.NotificationFragment
                || fragment instanceof mypage_details
                || fragment instanceof SeeAllPhotoFragment
                || fragment instanceof SendRequestFragment
                || fragment instanceof ZoomImage
                || fragment instanceof ViewProfileFragment
        ) {
            toolbar_post.setVisibility(View.GONE);
            toolbarFilter.setVisibility(View.GONE);

            if (!userData.getId().equals(memberId)) {
                toolbar_post.setVisibility(View.GONE);
                toolbarMenu.setVisibility(View.GONE);
                toolbarBack.setVisibility(View.VISIBLE);
                notificationcount.setVisibility(View.GONE);
                navigation.setVisibility(View.GONE);
            } else {
                setNotificationcount();
                toolbar_post.setVisibility(View.GONE);
                toolbarMenu.setVisibility(View.VISIBLE);
                toolbarBack.setVisibility(View.GONE);
                //  notificationcount.setVisibility(View.VISIBLE);
                navigation.setVisibility(View.VISIBLE);
            }
        } else if ((fragment instanceof FriendsListFragment || fragment instanceof FriendSuggestion) && isSocial == false) {
            setNotificationcount();
            SetValue(count,countdating);
            toolbarMenu.setVisibility(View.VISIBLE);
            toolbarBack.setVisibility(View.GONE);
            // notificationcount.setVisibility(View.VISIBLE);
            toolbar_post.setVisibility(View.GONE);
            toolbarSearch.setVisibility(View.VISIBLE);
            toolbarChat.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.VISIBLE);
            toolbarNotification.setVisibility(View.VISIBLE);
            toolbarFilter.setVisibility(View.VISIBLE);
        } else if (fragment instanceof GlobalPostFragment) {
            //    toolbarVideoCall.setVisibility(View.VISIBLE);
            toolbar_post.setVisibility(View.VISIBLE);
            toolbarMenu.setVisibility(View.GONE);
            toolbarBack.setVisibility(View.VISIBLE);
            notificationcount.setVisibility(View.GONE);
            toolbarNotification.setVisibility(View.GONE);
            toolbarSearch.setVisibility(View.GONE);
            toolbarChat.setVisibility(View.GONE);
            chatcount.setVisibility(View.GONE);
        } else {
            toolbarMenu.setVisibility(View.GONE);
            toolbarBack.setVisibility(View.VISIBLE);
            notificationcount.setVisibility(View.GONE);
            toolbar_post.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
            toolbarFilter.setVisibility(View.GONE);
        }
        manageBottomnavigation(fragment);
    }

    @SuppressLint("RestrictedApi")
    private void manageBottomnavigation(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            navigation.getMenu().getItem(0).setChecked(true);
            if (checktypenoti == false) {

            } else {
                nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_noti_gray));
            }
        } else if (fragment instanceof PostFragment) {
            navigation.getMenu().getItem(2).setChecked(true);
            if (checktypenoti == false) {

            } else {
                nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_noti_gray));
            }

        } else if (fragment instanceof KnocksFragment) {
            navigation.getMenu().getItem(1).setChecked(true);
            checktypenoti = false;
            BaseManager.savenotificationstatus(checktypenoti);
        } else if (fragment instanceof ViewProfileFragment) {
            navigation.getMenu().getItem(3).setChecked(true);
            if (checktypenoti == false) {

            } else {
                nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_noti_gray));
            }

        } else if (fragment instanceof FeedsFragment) {
            navigation.getMenu().getItem(4).setChecked(true);
            if (checktypenoti == false) {

            } else {
                nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_noti_gray));
            }

        }
    }

    public void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(this.findViewById(R.id.frame_main), message, 10000);
        View mView = snackbar.getView();
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        closeToolbarSearch();

        if (fragment instanceof ViewFullImageFragment || fragment instanceof ViewProfileFragment) {
            if (fragmentManager.getBackStackEntryCount() == 0) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof HomeFragment
                || fragment instanceof PostFragment
                || fragment instanceof KnocksFragment) {
            if (closeApp) {
                finish();
                super.onBackPressed();
            } else {
                CommonUtil.showLongToast(HomeActivity.this, "Press back button again to exit ");
                new CountDownTimer(5000, 500) {
                    public void onTick(long millisUntilFinished) {
                        closeApp = true;
                    }

                    public void onFinish() {
                        closeApp = false;
                    }
                }.start();
            }
        } else if (fragment instanceof FriendSuggestion
                || fragment instanceof FriendsListFragment
                && isSocial == false) {
            if (closeApp) {
                finish();
                super.onBackPressed();
            } else {
                CommonUtil.showLongToast(HomeActivity.this, "Press back button again to exit ");
                new CountDownTimer(5000, 500) {
                    public void onTick(long millisUntilFinished) {
                        closeApp = true;
                    }

                    public void onFinish() {
                        closeApp = false;
                    }
                }.start();
            }

        } else if (fragment instanceof ViewProfileFragment) {
            if (!userData.getId().equals(memberId)) {
                super.onBackPressed();
            } else {
                if (closeApp) {
                    finish();
                    super.onBackPressed();
                } else {
                    CommonUtil.showLongToast(HomeActivity.this, "Press back button again to exit ");
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

        } else if (fragment instanceof ViewProfileFragment) {
            replaceFragment(HomeFragment.newInstance(), true);
        } else {
            getActiveFragment();
        }
    }


    public void getActiveFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
//        if(fragment instanceof PaymentActivity)
//            ((PaymentActivity) fragment).removeCheckOutItems();
//        if(fragment instanceof ForeignInvestmentListFragment ||fragment instanceof CommunicationListFragment|fragment instanceof CompetitionListFragment) {
//            popAllStack();
//            addFragment(CategoryFragment.newInstance());
//        }else if(fragment instanceof TweetTagSearchFragment) {
//            popAllStack();
//            addFragment(TweetListFragment.newInstance());
//        }else{
//            popStack();
//        }

        popStack();
    }

    public void popAllStack() {
        FragmentManager fm = this.getSupportFragmentManager();
        toolbarTitle.setVisibility(View.VISIBLE);
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void popStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, boolean ispopstack) {
        Log.d(TAG, "replaceFragment: " + getSupportFragmentManager().getBackStackEntryCount());
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = this.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_main, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (ispopstack)
                ft.addToBackStack(backStateName);
//            ft.commitAllowingStateLoss( );
            ft.commit();
        }
    }


    public void addFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame_main, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (v.getId()) {
            case R.id.ll_social:
                isSocial = true;
                status=false;
                i=0;
              //  count=0;
                AppController.getManager().setInterest(Constant.SOCIAL);

                // this will clear the back stack and displays no animation on the screen
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragment(HomeFragment.newInstance(), true);
                navigation.setVisibility(View.VISIBLE);
                navigation1.setVisibility(View.GONE);
                SocialView.setVisibility(View.VISIBLE);
                DiatView.setVisibility(View.GONE);
                llSocial.setBackground(getResources().getDrawable(R.drawable.bg_blue_curve));
                llDating.setBackgroundColor(Color.TRANSPARENT);
                tvSocial.setTextColor(getResources().getColor(R.color.white));
                tvDating.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                ivSocial.setImageResource(R.drawable.social_active);
                ivDating.setImageResource(R.drawable.dating_inactive);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


                if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                    count = 0;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(userData.getId());
                    //  Query lastQuery = databaseReference.limitToLast(1);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                for (Map.Entry me : td2.entrySet()) {
                                    DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(me.getKey().toString()).child(userData.getId());

                                    mMessagecount.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("friendss", dataSnapshot.toString());
                                            if (dataSnapshot.getValue() != null) {
                                                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                                                for (Map.Entry me : td.entrySet()) {
                                                    Log.d("friend name", me.toString());

                                                    if (me.getKey().toString().equals("count")) {
                                                        var = Long.parseLong(me.getValue().toString());
                                                        Log.d("count", String.valueOf(var));
                                                    } else {
                                                        Log.d("values", "error");
                                                    }
                                                }
                                                if(i==(td2.size()))
                                                {
                                                    status=true;
                                                }

                                                    count = count + var;
                                                    i++;

                                                if (count == 0) {
                                                    chatcount.setVisibility(View.GONE);
                                                } else {
                                                    SetValue(count,0);
                                                   // chatcount.setText(String.valueOf(count));
                                                }


                                                Log.d("addition222", String.valueOf(count));
                                            }

                                            else
                                            {
                                                chatcount.setVisibility(View.GONE);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }


                            }
                            else {
                                chatcount.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
                }


                // set chatcount

//                if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(userData.getId());
//                    //  Query lastQuery = databaseReference.limitToLast(1);
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();
//                                for (Map.Entry me : td2.entrySet()) {
//                                    DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(me.getKey().toString()).child(userData.getId());
//
//                                    mMessagecount.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            Log.d("values", dataSnapshot.toString());
//                                            if (dataSnapshot.getValue() != null) {
//
//                                                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
//                                                for (Map.Entry me : td.entrySet()) {
//                                                    Log.d("values", me.toString());
//
//                                                    if (me.getKey().toString().equals("count")) {
//                                                        count = Long.parseLong(me.getValue().toString());
//                                                        Log.d("values", me.getValue().toString());
//                                                    } else {
//                                                        Log.d("values", "error");
//
//                                                    }
//                                                }
//                                                if (count == 0) {
//                                                    chatcount.setVisibility(View.GONE);
//                                                } else {
//                                                    chatcount.setVisibility(View.VISIBLE);
//                                                    chatcount.setText(String.valueOf(count));
//                                                }
//                                            }
//                                            else
//                                            {
//                                                chatcount.setVisibility(View.GONE);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//                            else {
//                                chatcount.setVisibility(View.GONE);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Handle possible errors.
//                        }
//                    });
//                }

                // end



                break;
            case R.id.ll_daiting:
                isSocial = false;
                statusdating=false;
                j=0;
                AppController.getManager().setInterest(Constant.DATING);

                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragment(FriendSuggestion.newInstance(), true);
                navigation.setVisibility(View.GONE);
                navigation1.setVisibility(View.VISIBLE);
                SocialView.setVisibility(View.GONE);
                DiatView.setVisibility(View.VISIBLE);
                llSocial.setBackgroundColor(Color.TRANSPARENT);
                llDating.setBackground(getResources().getDrawable(R.drawable.bg_blue_curve));
                tvSocial.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                tvDating.setTextColor(getResources().getColor(R.color.white));
                ivSocial.setImageResource(R.drawable.social_inactive);
                ivDating.setImageResource(R.drawable.dating_active);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


                if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                    countdating = 0;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(userData.getId());
                    //  Query lastQuery = databaseReference.limitToLast(1);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                for (Map.Entry me : td2.entrySet()) {
                                    DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(me.getKey().toString()).child(userData.getId());

                                    mMessagecount.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("friendss", dataSnapshot.toString());
                                            if (dataSnapshot.getValue() != null) {
                                                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                                                for (Map.Entry me : td.entrySet()) {
                                                    Log.d("friend name", me.toString());

                                                    if (me.getKey().toString().equals("count")) {
                                                        vardating = Long.parseLong(me.getValue().toString());
                                                        Log.d("count", String.valueOf(var));
                                                    } else {
                                                        Log.d("values", "error");
                                                    }
                                                }
                                                if(j==(td2.size()))
                                                {
                                                    statusdating=true;
                                                }

                                                    countdating = countdating + vardating;
                                                    j++;
                                                    if (countdating == 0) {
                                                    chatcount.setVisibility(View.GONE);
                                                } else {
                                                        SetValue(0,countdating);
                                                  //  chatcount.setVisibility(View.VISIBLE);
                                                  //  chatcount.setText(String.valueOf(countdating));
                                                }


                                              //  Log.d("addition", String.valueOf(countdating));
                                            }

                                            else
                                            {
                                                chatcount.setVisibility(View.GONE);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }


                            }
                            else {
                                chatcount.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });

                }

                // set chatcount
//                if (AppController.getManager().getInterest().equals(Constant.DATING)) {
//
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(userData.getId());
//                    //  Query lastQuery = databaseReference.limitToLast(1);
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                Map<String, Object> td2 = (HashMap<String, Object>) dataSnapshot.getValue();
//                                for (Map.Entry me : td2.entrySet()) {
//                                    DatabaseReference mMessagecount = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(me.getKey().toString()).child(userData.getId());
//
//                                    mMessagecount.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            Log.d("values", dataSnapshot.toString());
//                                            if (dataSnapshot.getValue() != null) {
//
//                                                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
//                                                for (Map.Entry me : td.entrySet()) {
//                                                    Log.d("values", me.toString());
//
//                                                    if (me.getKey().toString().equals("count")) {
//                                                        count = Long.parseLong(me.getValue().toString());
//                                                        Log.d("values", me.getValue().toString());
//                                                    } else {
//                                                        Log.d("values", "error");
//
//                                                    }
//                                                }
//                                                if (count == 0) {
//                                                    chatcount.setVisibility(View.GONE);
//
//                                                } else {
//                                                    chatcount.setVisibility(View.VISIBLE);
//                                                    chatcount.setText(String.valueOf(count));
//
//                                                }
//                                            }
//                                            else
//                                            {
//                                                chatcount.setVisibility(View.GONE);
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//                            else
//                            {
//                                chatcount.setVisibility(View.GONE);
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Handle possible errors.
//                        }
//                    });
//
//                }


                // end
                break;

            case R.id.tv_home_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                popAllStack();
                replaceFragment(HomeFragment.newInstance(), false);
                nvHome.performClick();

                break;
            case R.id.tv_chatgroup_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (fragment instanceof HomeFragment) {

                } else {
                    replaceFragment(HomeFragment.newInstance(), true);
                }
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                //CommonUtil.showLongToast(this, "under development");
                Intent groupChat = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(groupChat);

                break;
            case R.id.tv_bookmark_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (fragment instanceof HomeFragment) {

                } else {
                    replaceFragment(HomeFragment.newInstance(), true);
                }
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
//                CommonUtil.showLongToast(this, "under development");
                replaceFragment(BookmarkFragment.newInstance(), true);
                break;
            case R.id.tv_nearfriend_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(FriendsListFragment.newInstance(1), true);
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_addmanager_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(AdsManagerFragment.newInstance(), true);
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));

                break;
            case R.id.tv_mypage_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                replaceFragment(MyPageFragment.newInstance(), true);

                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
//                tvHomeSocial.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.home_side),null,null,null);
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
//                tvMypageSocial.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.my_pages_side),null,null,null);
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_wallet_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(WalletFragment.newInstance(), true);
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));

                break;
            case R.id.tv_aboutus_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                CommonUtil.GoToWebViewActivity(this, "http://3.83.140.213/needy/about-us.html", "About us");
                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_Security_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                replaceFragment(SecurityFragment.newInstance(), true);

                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_helpsupport_social:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                replaceFragment(ContactUsFragment.newInstance(), true);

                tvHomeSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvMypageSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvChatgroupSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvBookmarkSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvNearfriendSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAddmanagerSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvWalletSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvAboutusSocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvSecuritySocial.setBackgroundColor(getResources().getColor(R.color.white));
                tvHelpsupportSocial.setBackgroundColor(getResources().getColor(R.color.color_gray_bg));
                break;
            case R.id.tv_logout_social:
                confirm();
                break;
            case R.id.tv_global_post:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toolbar_post.setText("Post");
                replaceFragment(GlobalPostFragment.newInstance(), true);
                break;
            case R.id.tv_home_daiting:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                replaceFragment(FriendSuggestion.newInstance(), true);
                break;
            case R.id.tv_aboutus_daiting:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                CommonUtil.GoToWebViewActivity(this, "http://3.83.140.213/needy/about-us.html", "About us");
                break;
            case R.id.tv_Security_daiting:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(SecurityFragment.newInstance(), true);
                break;
            case R.id.tv_helpsupport_daiting:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(ContactUsFragment.newInstance(), true);
                break;
            case R.id.tv_logout_daiting:
                confirm();
                break;
            case R.id.toolbar_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.toolbar_back:
                onBackPressed();
                break;
            case R.id.toolbar_chat:
                Fragment fragmentt = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                if (fragment != null) {
                    if (fragment instanceof FriendSuggestion || fragment instanceof SendRequestFragment) {
                        AppController.getManager().setInterest(Constant.DATING);
                    } else if (fragment instanceof HomeFragment) {
                        AppController.getManager().setInterest(Constant.SOCIAL);
                    }
                }

                Intent intent = new Intent(HomeActivity.this, chatDialogActivity.class);
                //CommonUtil.showLongToast(this, "under development");
                //  Intent intent=new Intent(HomeActivity.this, com.needyyy.app.Chat.groupchatwebrtc.activities.LoginActivity.class);
                intent.putExtra("activity", "isha2");
                intent.putExtra("password", "12345678");
                startActivity(intent);

                break;
            case R.id.toolbar_notification:
                mUserDatabase.child(userData.getId()).child("count").setValue("0");
               // BaseManager.savenotificationcount(0);
                notificationcount.setText(String.valueOf(notificationn));
                replaceFragment(new com.needyyy.app.notifications.NotificationFragment(), true);
                notificationcount.setVisibility(View.GONE);
                break;

            case R.id.toolbar_search:
                //replaceFragment(SearchFriendFragment.newInstance());
                //  startActivity(new Intent(this,MainActivity.class));
//                CommonUtil.showLongToast(this, "under development");
                break;
            case R.id.toolbar_filter:
                replaceFragment(FilterFragment.newInstance(), true);
                break;
            case R.id.toolbar_post:
                if (toolbar_post.getText().equals("Post")) {
                    idd = null;
                    if (tv_writesomething.getText().toString().equals("") || tv_writesomething.getText().toString() == null) {
                        Toast.makeText(getApplicationContext(), "Please write post", Toast.LENGTH_SHORT).show();
                    } else {
                        hitGlobalPostApi(String.valueOf(tv_writesomething.getText()));
                    }
                } else if (toolbar_post.getText().equals("update")) {
                    hitGlobalPostApi(String.valueOf(tv_writesomething.getText()));
                }
                break;
            case R.id.civ_profile_pic:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(ViewProfileFragment.newInstance(userData.getId(), null), true);
                break;
//            case R.id.video_call:
//
//                startOpponentsActivity();
//
//                break;
            case R.id.tv_username:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                replaceFragment(ViewProfileFragment.newInstance(userData.getId(), null), true);
        }
    }

    private void hitGlobalPostApi(String caption) {

        if (CommonUtil.isConnectingToInternet(this)) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GlobalPost> call = Service.ADDGLOBALPOST(caption, idd);
            call.enqueue(new Callback<GlobalPost>() {
                @Override
                public void onResponse(Call<GlobalPost> call, Response<GlobalPost> response) {
                    cancelProgressDialog();
                    GlobalPost globalPost = response.body();
                    if (globalPost.getStatus()) {
                        tv_writesomething.setText("");
                        toolbar_post.setText("Post");
                        Toast.makeText(getApplicationContext(), globalPost.getMessage(), Toast.LENGTH_SHORT).show();
                        getpostData();

                    } else {
                        if (globalPost.getMessage().equals("110110")) {
                            logout();
                        } else {
                            Toast.makeText(getApplicationContext(), globalPost.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GlobalPost> call, Throwable t) {
                }
            });
        }
    }

    public void getpostData() {
        if (CommonUtil.isConnectingToInternet(this)) {

            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GlobalPost> call = Service.GLOBALPOST("");
            call.enqueue(new Callback<GlobalPost>() {
                @Override
                public void onResponse(Call<GlobalPost> call, Response<GlobalPost> response) {
                    GlobalPost globalPost = response.body();
                    if (globalPost.getStatus()) {
                        fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                        if (fragment instanceof GlobalPostFragment) {
                            ((GlobalPostFragment) fragment).getUpdatedData(globalPost);
                        }
                    } else {
                        if (globalPost.getMessage().equals("110110")) {
                            logout();

                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(Call<GlobalPost> call, Throwable t) {
                }
            });
        }
    }

    public void logout() {
        AppController.getManager().clearPreference();
        BaseManager.clearPreference();
        generateFirebaseTocken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fromsplash", "2");
        intent.putExtra("userdata", "");
        startActivity(intent);
        finish();
    }

    private void generateFirebaseTocken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("SplashActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("tocken", "=====" + token);
                        AppController.getManager().setTocken(token);

                    }
                });
    }

    //logout check
    public void confirm() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
//                        AppController.getManager().clearPreference();
//                        Intent i = new Intent(this, LoginActivity.class);
//                        startActivity(i);
//                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_USER) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);

//      if(fragment instanceof QuestionFragment)
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == TAG_USER2) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);

//      if(fragment instanceof QuestionFragment)
            fragment.onActivityResult(requestCode, resultCode, data);
        }



        if(requestCode==CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);

//      if(fragment instanceof QuestionFragment)
            fragment.onActivityResult(requestCode, resultCode, data);

            Log.d("response","Image Data:"+new Gson().toJson(data));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
//    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("razorPay", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
//    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("razorpayFailure", "Exception in onPaymentError", e);
        }
    }

    private void getpostdata(String taskid) {
        if (CommonUtil.isConnectingToInternet(this)) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<Getpostdata> call = Service.getPost(taskid);
            call.enqueue(new Callback<Getpostdata>() {
                @Override
                public void onResponse(Call<Getpostdata> call, Response<Getpostdata> response) {
                    cancelProgressDialog();
                    Getpostdata getpostdata = response.body();
                    String banner = "";
                    postresponse = getpostdata.getData();
                    if (postresponse.getLatitude() != null && postresponse.getLongitude() != null) {
                        String latEiffelTower = String.valueOf(postresponse.getLatitude());
                        String lngEiffelTower = String.valueOf(postresponse.getLongitude());
                        String url = "https://maps.googleapis.com/maps/api/staticmap?";
                        url += "&zoom=13";
                        url += "&size=600x300";
                        url += "&maptype=roadmap";
                        url += "&markers=color:green%7Clabel:G%7C" + latEiffelTower + "," + lngEiffelTower;
                        url += "&key=" + "AIzaSyAy9jwmpKuuNJdID26ChQADu0HofAWGZNc";
                        Log.e("response", "===" + url);
                        banner = url;
                        List<PostMetum> postMetums = new ArrayList<>();
                        for (int l = 0; l < 1; l++) {
                            PostMetum postMetum = new PostMetum();
                            //   postMetum.setViews(postResponse.getPostMeta().get(l).getViews());
                            postMetum.setLink(banner);
                            //   postMetum.setFilelink(postResponse.getPostMeta().get(l).getFilelink());
                            //  postMetum.setId(postResponse.getPostMeta().get(l).getId());
                            postMetum.setFileType("webview");
                            //  postMetum.setPostId(postResponse.getPostMeta().get(l).getPostId());
                            //  postMetum.setThumbnail(postResponse.getPostMeta().get(l).getThumbnail());
                            postMetums.add(postMetum);
                        }
                        postresponse.setPostMeta(postMetums);
                        //  replaceFragment(ViewFullImageFragment.newInstance(postresponse),true);
                    }
                    replaceFragment(ViewFullImageFragment.newInstance(postresponse), true);
                }

                @Override
                public void onFailure(Call<Getpostdata> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
        } else {

        }

    }

    public void setpostbutton(String name, String id) {
        toolbar_post.setText(name);
        idd = id;
    }


//    private void getpagedata(String taskid) {
//        if (CommonUtil.isConnectingToInternet(this)){
//            showProgressDialog();
//            WebInterface Service = AppController.getRetrofitInstance().create(WebInterface.class);
//            Call<MyPage> call = Service.getPageDetails(taskid);
//            call.enqueue(new Callback<MyPage>() {
//                @Override
//                public void onResponse(Call<MyPage> call, Response<MyPage> response) {
//                    cancelProgressDialog();
//                    MyPage data = response.body();
//                    PageData pageData= (PageData) data.getData();
//                    replaceFragment(mypage_details.newInstance(pageData),false);
//                }
//
//                @Override
//                public void onFailure(Call<MyPage> call, Throwable t) {
//                    cancelProgressDialog();
//
//                }
//            });
//        }else{
//
//        }
//
//    }

    private void startOpponentsActivity() {
        OpponentsActivity.start(HomeActivity.this, false);
        //finish();
    }

    @Override
    public void emotioncommunication(Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        {
            if (fragment instanceof EmotionFragment || fragment instanceof EmotionStausFragment) {
                PostFragment postFragment = new PostFragment();
                postFragment.emotion(bundle);
                replaceFragment(new PostFragment(), true);
            }
        }
    }

    SearchListener searchListener;

    @Override
    public void fragmentVisible(boolean flag, SearchListener listener) {
        searchListener = listener;
        Log.d("response", "listener set successfully");
        //Toast.makeText(activity, "isVisible:"+flag+",searchListener:"+ searchListener, Toast.LENGTH_SHORT).show();
    }

    private void registerReceiver() {

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onReceive(Context context, Intent intent) {
                String push_type = intent.getStringExtra("push_type");
                Log.i("Broadcast Received", "push_type:" + push_type);
                //Toast.makeText(context, ""+push_type, Toast.LENGTH_SHORT).show();
                //setNotificationCount();
                if(push_type.equals("60")||push_type.equals("61"))
                {

                }
                else
                {
                  //  BaseManager.savenotificationcount(1);

                }


                if (push_type.equals("50") || (push_type.equals("51"))) {

                    nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_noti_gray));
                    checktypenoti = true;
                    BaseManager.savenotificationstatus(checktypenoti);

                } else {
                    checktypenoti = false;
                    nvKnocks.setIcon(getResources().getDrawable(R.drawable.knocks_active));
                    BaseManager.savenotificationstatus(checktypenoti);
                }
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void SetValue(long count,long countdating) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_main);
        if (fragment instanceof HomeFragment
                || fragment instanceof FeedsFragment
                || fragment instanceof KnocksFragment ||
                fragment instanceof PostFragment ||
                fragment instanceof FriendsListFragment
                || fragment instanceof FriendSuggestion) {

            if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                if (count == 0) {
                    chatcount.setVisibility(View.GONE);
                } else {
                    chatcount.setVisibility(View.VISIBLE);
                }
            } else if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                if (countdating == 0) {
                    chatcount.setVisibility(View.GONE);
                } else {
                    chatcount.setVisibility(View.VISIBLE);
                }
            }
        }
        else
        {
            chatcount.setVisibility(View.GONE);
        }
    }
    public void getnotification(String id)
    {

        mUserDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationn = Integer.parseInt(dataSnapshot.child("count").getValue().toString());
                setNotificationcount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}


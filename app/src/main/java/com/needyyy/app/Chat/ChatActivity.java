package com.needyyy.app.Chat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.MaskFilterSpan;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.ion.builder.Builders;
import com.needyyy.AppController;
import com.needyyy.app.Base.GetTimeAgo;
import com.needyyy.app.Chat.Adapter.MessageAdapter;
import com.needyyy.app.Chat.groupchatwebrtc.activities.CallActivity;
import com.needyyy.app.Chat.groupchatwebrtc.utils.PushNotificationSender;
import com.needyyy.app.Chat.groupchatwebrtc.utils.WebRtcSessionManager;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;
import com.needyyy.app.Chat.model.Body;
import com.needyyy.app.Chat.model.Data;
import com.needyyy.app.Chat.model.Json;
import com.needyyy.app.Chat.model.Notification;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.Cognito.Cognito;
import com.needyyy.app.mypage.model.memberRating.MemberRating;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.Progress;
import com.needyyy.app.utils.UploadAmazonS3;
import com.needyyy.app.webutils.WebConstants;
import com.needyyy.app.webutils.WebInterface;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.needyyy.AppController.getContext;
import static com.needyyy.app.constants.Constants.kCurrentUser;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener, MessageAdapter.DeleteMessage {
    Boolean buttonstatus=false;
    private String mChatUserId, pageid, userName, type, userid;
    TextView mUserName;
    TextView mUserLastSeen;
    String file_path="";
    CircleImageView mUserImage;
    private FirebaseAuth mAuth;
    UserDataResult userData;
    String mCurrentUserId;
    boolean seenmanagement;
    String quickblox_id;
    Dialog dialog;
    private long count;
    Button ratingDone;
    Progress mprogress;
    private float ratedValue;
    private DatabaseReference mMessagecount,setstatus;
    private DatabaseReference mUsersDatabase;
    private File newFile;
    private Uri newProfileImageUri;
    private String state, imageName, profile_picture = "";
    public static int REQUEST_CODE_GALLERY = 901;
    public static int REQUEST_CODE_CAMERA = 902;
    public boolean status=false;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference,updatecount;
    private DatabaseReference mConvDatabase,messagecounter;
    private ImageButton mChatSendButton, mChatAddButton;
    private EditText mMessageView;

    private RecyclerView mMessagesList;


    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;

    public static final int TOTAL_ITEM_TO_LOAD = 10000;
    private int mCurrentPage = 1;

    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private ImageView toolbarBack, anonymousChat, videoCalling, audioCalling;
    private static final int GALLERY_PICK = 1;
    StorageReference mImageStorage;
    String member_id, iscongnito, israting;
    private TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        toolbarBack = findViewById(R.id.toolbar_back);
        username=findViewById(R.id.user_name);
        anonymousChat = findViewById(R.id.anonomous_chat);
        videoCalling = findViewById(R.id.video_call);
        audioCalling = findViewById(R.id.audio_call);
        mChatAddButton = (ImageButton) findViewById(R.id.chatAddButton);
        mChatSendButton = (ImageButton) findViewById(R.id.chatSendButton);
        mMessageView = (EditText) findViewById(R.id.chatMessageView);
       if(AppController.getManager().getInterest().equals(Constant.DATING))
       {
           anonymousChat.setBackgroundResource(R.drawable.revel_profile);
       }
       else
       {
           anonymousChat.setBackgroundResource(R.drawable.anonymous_chat);
       }

        //-----GETING FROM INTENT----
        iscongnito = getIntent().getStringExtra("iscongnito");
        israting = getIntent().getStringExtra("israting");
        pageid = getIntent().getStringExtra("page_id");
        userName = getIntent().getStringExtra("user_name");
        type = getIntent().getStringExtra("type");
        userid = getIntent().getStringExtra("user_id");
        mChatUserId = getIntent().getStringExtra("user_id");
        quickblox_id= getIntent().getStringExtra("quickbloxid");
        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("needyyy/users");


        member_id = getIntent().getStringExtra("user_id");

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserLastSeen = (TextView) findViewById(R.id.tv_lastseen);
        mUserImage = (CircleImageView) findViewById(R.id.iv_profile);

        updatecount= FirebaseDatabase.getInstance().getReference();
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = userData.getId();

        mMessageAdapter = new MessageAdapter(ChatActivity.this, messagesList, this);

        mMessagesList = (RecyclerView) findViewById(R.id.recycleViewMessageList);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        // mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);
        mMessagesList.setAdapter(mMessageAdapter);
        toolbarBack.setOnClickListener(this);
        anonymousChat.setOnClickListener(this);
        videoCalling.setOnClickListener(this);
        audioCalling.setOnClickListener(this);
        username.setOnClickListener(this);

//        if(AppController.getManager().getInterest().equals(Constant.DATING))
//        {
//            anonymousChat.setVisibility(View.VISIBLE);
//        }
//        else if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
//        {
//            anonymousChat.setVisibility(View.VISIBLE);
//        }


        if (type != null && type.equals("page")) {
            anonymousChat.setVisibility(View.GONE);
            videoCalling.setVisibility(View.GONE);
            audioCalling.setVisibility(View.GONE);
            mUserLastSeen.setVisibility(View.GONE);
        }

        if(pageid.equals("fromsame"))
        {
            status=true;
        }
        loadMessages();


        if (type != null && type.equals("single")) {
            if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(mCurrentUserId);
                setstatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private").child(mCurrentUserId);

            } else {
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(mCurrentUserId);
                setstatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(mCurrentUserId);
            }
        }
        else {

        }


        //----ADDING LAST SEEN-----
        mRootReference.child("needyyy/users").child(mChatUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String onlineValue = dataSnapshot.child("is_online").getValue().toString();
                String imageValue = dataSnapshot.child("profile_picture").getValue().toString();
                    if(iscongnito==null||iscongnito.equals("")) {
                        if (!imageValue.isEmpty())
                        {
                            Picasso.get().load(imageValue).placeholder(R.drawable.needyy).into(mUserImage);
                        }
                        mUserName.setText(userName);
                    }
                    else if(iscongnito.equals("0"))
                    {
                        if (!imageValue.isEmpty())
                        {
                            Picasso.get().load(imageValue).placeholder(R.drawable.needyy).into(mUserImage);
                        }                        mUserName.setText(userName);
                    }
                    else if(iscongnito.equals("1"))
                    {
                        if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
                        {
                            if (!imageValue.isEmpty())
                            {
                                Picasso.get().load(imageValue).placeholder(R.drawable.needyy).into(mUserImage);
                            }
                            mUserName.setText(userName);
                        }
                        else if(AppController.getManager().getInterest().equals(Constant.DATING))
                        {
                            if (!imageValue.isEmpty())
                            {
                                Glide.with(ChatActivity.this).load(imageValue)
                                        .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)).placeholder(R.drawable.needyy))
                                        .into(mUserImage);
                            }
                            String namehidden="";
//                        SpannableString string = new SpannableString(userName);
//                        MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
//                        string.setSpan(new MaskFilterSpan(blurMask), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            for(int k=0;k<userName.length();k++)
                            {
                                namehidden=namehidden+"*";
                            }
                            mUserName.setText(namehidden);
                        }


                    }

                if (onlineValue.equals("online")) {
                    mUserLastSeen.setText("online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(onlineValue);
                    String lastSeen = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    mUserLastSeen.setText(lastSeen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //----ADDING SEEN OF MESSAGES----
//        mRootReference.child("needyyy/friend_management/social_private").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (!dataSnapshot.hasChild(mChatUserId)) {
//
//                    Map chatAddMap = new HashMap();
//                    chatAddMap.put("seen", false);
//                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);
//
//                    Map chatUserMap = new HashMap();
//
//                    if (type != null && type.equals("single")) {
//                        if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
//                            chatUserMap.put("needyyy/friend_management/dating_private/" + mChatUserId + "/" + mCurrentUserId, chatAddMap);
//                            chatUserMap.put("needyyy/friend_management/dating_private/" + mCurrentUserId + "/" + mChatUserId, chatAddMap);
//
//                        } else {
//                            chatUserMap.put("needyyy/friend_management/social_private/" + mChatUserId + "/" + mCurrentUserId, chatAddMap);
//                            chatUserMap.put("needyyy/friend_management/social_private/" + mCurrentUserId + "/" + mChatUserId, chatAddMap);
//                        }
//                    } else {
//                        chatUserMap.put("needyyy/friend_management/social_private/" + mChatUserId + "/" + mCurrentUserId, chatAddMap);
//                        chatUserMap.put("needyyy/friend_management/social_private/" + mCurrentUserId + "/" + mChatUserId, chatAddMap);
//                    }
//
//
////                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
////                        @Override
////                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////                            if (databaseError == null) {
////                                Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
////                            } else
////                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
////                        }
////                    });
//                }
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
//            }
//        });

        //----SEND MESSAGE--BUTTON----

        mChatAddButton.setOnClickListener(this);
        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonstatus=true;
                String message = mMessageView.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    String increasechatvalue="needyyy/seen_management/social_private/"+ mCurrentUserId+ "/" +mChatUserId;

                    String current_user_ref = "needyyy/chat_module/social_private/" + mCurrentUserId + "/" + mChatUserId;
                    String chat_user_ref = "needyyy/chat_module/social_private/" + mChatUserId + "/" + mCurrentUserId;
                    DatabaseReference user_message_push = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId).push();

                    if (type != null && type.equals("single")) {
                        if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
                            current_user_ref = "needyyy/chat_module/dating_private/" + mCurrentUserId + "/" + mChatUserId;
                            chat_user_ref = "needyyy/chat_module/dating_private/" + mChatUserId + "/" + mCurrentUserId;
                            user_message_push = mRootReference.child("needyyy/chat_module/dating_private/").child(mCurrentUserId).child(mChatUserId).push();
                            increasechatvalue="needyyy/seen_management/dating_private/"+ mCurrentUserId+ "/" +mChatUserId;
                        } else {
                            current_user_ref = "needyyy/chat_module/social_private/" + mCurrentUserId + "/" + mChatUserId;
                            chat_user_ref = "needyyy/chat_module/social_private/" + mChatUserId + "/" + mCurrentUserId;
                            user_message_push = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId).push();
                             increasechatvalue="needyyy/seen_management/social_private/"+ mCurrentUserId+ "/" +mChatUserId;
                        }
                    }
                    else {
//                        current_user_ref = "needyyy/chat_module/page_private/" + mCurrentUserId + "/" + mChatUserId;
                        chat_user_ref = "needyyy/page_management/chat_module/" + pageid + "/" + userid;
                        user_message_push = mRootReference.child("needyyy/page_management/chat_module/").child(pageid).child(userid).push();
                    }


                    String push_id = user_message_push.getKey();

                   // mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(mCurrentUserId).child(mChatUserId);
                    if (type != null && type.equals("single")) {


                        mMessagecount.child(mChatUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("count").getValue() == null) {
                                    count = 0;
                                } else {
                                    count = (long) dataSnapshot.child("count").getValue();
                                }
//                        String quickbloxid=dataSnapshot.child("quick_blox_media_json").getValue().toString();;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        setstatus.child(mChatUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("seen").getValue() == null) {
                                    seenmanagement = false;
                                } else {
                                    seenmanagement = (Boolean) dataSnapshot.child("seen").getValue();
                                }
//                        String quickbloxid=dataSnapshot.child("quick_blox_media_json").getValue().toString();;
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                 //   setstatus.child(mChatUserId).child("seen").getKey()

//                    Map map=new HashMap();
//                    map.put("count",count+1);
//                    map.put("seen",seenmanagement);

                    Map messageMap = new HashMap();
                    messageMap.put("message", message);
                    messageMap.put("seen", false);
                    messageMap.put("type", "text");
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from", mCurrentUserId);
                    messageMap.put("status", "1");
                    Map messageUserMap = new HashMap();
                  //  Map countmap=new HashMap();
                    if (type != null && type.equals("single")) {
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                     //   countmap.put(increasechatvalue,map);
                    }
                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);



                    mMessageView.setText("");
                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                            } else {
                                //Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    if (type != null && type.equals("single")) {
                        if (seenmanagement == false) {
                            setstatus.child(mChatUserId).child("count").setValue(count + 1);
                            Log.i("displayNotification"," if (type != null && type.equals(\"single\"))");
                               displayNotification(message,userid);
//                        updatecount.updateChildren(countmap, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                                if (databaseError != null) {
//                                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
//                                } else {
//                                    //Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
                        } else {

                        }
                    }

                }

            }
        });

        //----LOADING 10 MESSAGES ON SWIPE REFRESH----
/*
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                mCurrentPage++;
                loadMoreMessages();
                ;

            }
        });
*/


        if(AppController.getManager().getInterest().equals(Constant.DATING)) {
            messagecounter = mRootReference.child("needyyy/chat_module/dating_private/").child(mCurrentUserId).child(mChatUserId);
            messagecounter.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("value",dataSnapshot.toString());

                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    Log.d("value",hashMap.toString());
                    if (hashMap.size() > 2) {
                        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
                        mConvDatabase.keepSynced(true);

                        mConvDatabase.child(mCurrentUserId).child(mChatUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                israting = dataSnapshot.child("is_rated").getValue().toString();
                                if (Double.parseDouble(israting) > Double.parseDouble("0.0")) {
                                    Log.d("Correct","yes");
                                } else {
                                    Log.d("isratedback", israting);
                                    customdialog();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void checkPermission() {
        Dexter.withActivity(ChatActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            //showProfilePictureDialog();
                            showSelectImageDialog();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(ChatActivity.this, "You must grant all permissions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .onSameThread()
                .check();
    }

    public void showSelectImageDialog() {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.layout_image_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvCamera = (dialog.findViewById(R.id.tvCamera));
        TextView tvGallery = (dialog.findViewById(R.id.tvGallery));


        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkStorage();
                getImageFromCamera();
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkStorage();
                getImageFromGallery();
            }
        });
        dialog.show();
    }

    public void checkStorage() {
        imageName = "";
        state = Environment.getExternalStorageState();
        String folder_main = "Needyyy";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        imageName = "Needyyy" + "_" + String.valueOf(System.nanoTime()) + ".png";

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newFile = new File(f, imageName);
            newProfileImageUri = Uri.fromFile(newFile);
            //newProfileImageUri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider",newFile);
        } else {
            newFile = new File(getFilesDir(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
            //cameranewProfileImageUri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider",newFile);
        }
    }

    private void getImageFromGallery() {
        checkStorage();
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.putExtra("return-data", true);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            Toast.makeText(ChatActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }

    private void getImageFromCamera() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            cameraIntent.putExtra("return-data", true);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            Toast.makeText(ChatActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        //    customdialog();
        // setstatus.child(mChatUserId).child("seen").setValue(false);

        if(AppController.getManager().getInterest().equals(Constant.DATING))
        {
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
            mConvDatabase.keepSynced(true);

            mConvDatabase.child(mCurrentUserId).child(mChatUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    israting = dataSnapshot.child("is_rated").getValue().toString();
                    if (status == false) {
                        if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                            if((israting==null && israting.equals(""))|| israting.equals("0"))
                            {
                                customdialog();
                            }
                            else
                            {
                                finish();
                            }
                        }
                        else if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                            if(pageid.equals("fromsamesocial"))
                            {
                                AppController.getManager().setInterest(Constant.DATING);
                                finish();
                            }
                            else
                            {
                                finish();

                            }
                        }
                    }
                    else {
                        // Intent intent=getIntent();
                        //  intent.putExtra("userid",mChatUserId);
                        if(status==true) {

                            if ((israting == null && israting.equals("")) || israting.equals("0")) {
                                customdialog();
                            } else {
                                AppController.getManager().setInterest(Constant.SOCIAL);
                                finish();
                            }
                        }

                        //  setResult(0, intent);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
        {

            if (status == false) {
                if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                    if((israting==null && israting.equals(""))|| israting.equals("0"))
                    {
                        customdialog();
                    }
                    else
                    {
                        finish();
                    }
                }
                else if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                    if(pageid.equals("fromsamesocial"))
                    {
                        AppController.getManager().setInterest(Constant.DATING);
                        finish();
                    }
                    else
                    {
                        finish();

                    }
                }
            }
            else {
                // Intent intent=getIntent();
                //  intent.putExtra("userid",mChatUserId);
                if(status==true) {

                    if ((israting == null && israting.equals("")) || israting.equals("0")) {
                        customdialog();
                    } else {
                        AppController.getManager().setInterest(Constant.SOCIAL);
                        finish();
                    }
                }

                //  setResult(0, intent);

            }

        }
    }

    private void customdialog() {
        dialog = new Dialog(this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.rating_dialog);
        CircleImageView circleImageView = dialog.findViewById(R.id.raitngCircleimage);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        ratingDone = dialog.findViewById(R.id.rank_dialog_button);
        RatingBar ratingBar = dialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratedValue = ratingBar.getRating();
            }
        });

        ratingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getrating();
            }
        });
    }



    private void displayNotification(String Msg, String id) {
        Log.i("displayNotification","displayNotification");
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("needyyy/users");
        mUsersDatabase.keepSynced(true);
        mUsersDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String userName = dataSnapshot.child("name").getValue().toString();
                String DEVICETOKEN = dataSnapshot.child("device_token").getValue().toString();
                String userid = dataSnapshot.child("user_id").getValue().toString();
               // SENDNOTIFICATION(DEVICETOKEN,userName,Msg);
                if(buttonstatus==true) {
                    if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                        String meaage = userName + " send you message : " + Msg;
                        sendNotification(userid, DEVICETOKEN, meaage, "60");
                    } else {
                        String meaage = "Someone send you message : " + Msg;
                        sendNotification(userid, DEVICETOKEN, meaage, "61");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String userid1, String device, String message,String pushtype) {
        Log.i("displayNotification","sendNotification");
        Body body=new Body();
        body.setMessage(message);
        body.setPushType(pushtype);
        body.setTitle("Chat message");
        body.setDevice_token(device);
        Json json=new Json();
        json.setTaskId(Integer.parseInt(userid1));
        body.setJson(json);
        List<Json> jsons=new ArrayList<>();
        jsons.add(json);
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        String bodyParam=new Gson().toJson(body);
        Log.d("response",bodyParam);
        Call<String> call = Service.PushNotification(device,"my chat",message,pushtype,new Gson().toJson(json));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
             Log.d("sucess","status");
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("faliusre","status");
            }
        });
    }


//    private void SENDNOTIFICATION(String userid1, String device, String message,String pushtype) {
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//                                      @Override
//                                      public okhttp3.Response intercept(Chain chain) throws IOException {
//                                          Request original = chain.request();
//
//                                          Request request = original.newBuilder()
//                                                  .header("Content-Type", "application/json")
//                                                  .header("Authorization", "key=AIzaSyBohyr-o5sMydaZ7lnJVpcRSJiwGDN8gdI")
//                                                  .method(original.method(), original.body())
//                                                  .build();
//
//                                          return chain.proceed(request);
//                                      }
//                                  });
//
//                OkHttpClient client = httpClient.build();
//
//
//
//        Retrofit retrofitnode = new Retrofit.Builder()
//                .baseUrl(WebConstants.Firebase)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//       WebInterface webInterface=retrofitnode.create(WebInterface.class);
//
//        Data data=new Data();
//        data.setTo(device);
//        data.setPriority("high");
//
//        Notification notification=new Notification();
//        notification.setTitle("chat");
//        notification.setBody("hi");
////
//        data.setNotification(notification);
//
//        Body body=new Body();
//        body.setMessage(message);
//        body.setPushType(pushtype);
//        body.setTitle("Chat message");
//        //body.setDevice_token(device);
//        data.setBody(body);
//
//        Json json=new Json();
//        json.setTaskId(Integer.parseInt(pushtype));
//
//        Log.e("notification","notification"+new Gson().toJson(data));
//
//        Call<String> call=  webInterface.PushNotification(data);
//
//       call.enqueue(new Callback<String>() {
//           @Override
//           public void onResponse(Call<String> call, Response<String> response) {
//               Log.d("pushnotification",response.toString());
//           }
//           @Override
//           public void onFailure(Call<String> call, Throwable t) {
//           }
//       });
//    }

//                        String quickblo








    public void addMessageDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.welcomemessage);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvNew = dialog.findViewById(R.id.et_create);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendwelcome(tvNew.getText().toString());
                dialog.dismiss();

//                AppController.getManager().setInterest(Constant.DATING);
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendwelcome(String toString) {
        showProgressDialog();

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequestDating(mChatUserId,1, toString);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if(getReceivedRequest.getStatus()) {

                    Toast.makeText(getContext(),getReceivedRequest.getMessage(),Toast.LENGTH_SHORT).show();
//                    ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance(), true);
                    getFragmentManager().popBackStack();
                } else {
                    if (getReceivedRequest.getMessage().equals("110110")){

                    }else{

                    }
                }
            }
            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {
                cancelProgressDialog();

            }
        });

    }


    public void showProgressDialog() {
        mprogress = new Progress(this);
        mprogress.setCancelable(false);
        mprogress.show();
    }

    public void cancelProgressDialog() {
        if (mprogress != null)
            if (mprogress.isShowing()) {
                mprogress.dismiss();
            }
    }

    private void getrating() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<MemberRating> call = Service.getrating(member_id, String.valueOf(ratedValue));
        call.enqueue(new Callback<MemberRating>() {
            @Override
            public void onResponse(Call<MemberRating> call, Response<MemberRating> response) {
                cancelProgressDialog();
                if (response.body() != null) {
                    MemberRating memberRating = response.body();
                    if (memberRating.getStatus()) {
                        Toast.makeText(ChatActivity.this, "Rated Successfully" + ratedValue + "!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
                        {
                            finish();
                        }
                        else
                        {
                            if(status==true)
                            {
                                AppController.getManager().setInterest(Constant.SOCIAL);
                                finish();
                            }
                            else
                            {
                                finish();
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<MemberRating> call, Throwable t) {
                cancelProgressDialog();

                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {
        DatabaseReference messageRef = null;
        if (type != null && type.equals("single")) {

            if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
                messageRef = mRootReference.child("needyyy/chat_module/dating_private/").child(mCurrentUserId).child(mChatUserId);
            } else {
                messageRef = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId);
            }

        } else {
            messageRef = mRootReference.child("needyyy/page_management/chat_module/").child(pageid).child(userid);
        }


        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = (Messages) dataSnapshot.getValue(Messages.class);
                messages.setKey(dataSnapshot.getKey());
                itemPos++;

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();

                    mLastKey = mMessageKey;
                    mPrevKey = mMessageKey;
                }
                if (messages.getStatus().equals("1"))
                    messagesList.add(messages);
                mMessageAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1);


                Log.d("messagesize",String.valueOf(messagesList.size()));


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId);

        if (type != null && type.equals("single")) {

            if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
                messageRef = mRootReference.child("needyyy/chat_module/dating_private/").child(mCurrentUserId).child(mChatUserId);
              //  anonymousChat.setVisibility(View.VISIBLE);
            } else {
                messageRef = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId);
              //  anonymousChat.setVisibility(View.GONE);
            }

        } else {
            messageRef = mRootReference.child("needyyy/page_management/chat_module/").child(pageid).child(userid);
        }

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();
                message.setKey(messageKey);

                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, message);

                } else {
                    mPrevKey = mLastKey;
                }

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();
                    mLastKey = mMessageKey;
                }
                mMessageAdapter.notifyDataSetChanged();

                mLinearLayoutManager.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---THIS FUNCTION IS CALLED WHEN SYSTEM ACTIVITY IS CALLED---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //---FOR PICKING IMAGE FROM GALLERY ACTIVITY AND SENDING---
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();

                showProgressDialog();
                uploadPicToAmazon();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ChatActivity.this, "Something went wrong, Try again...", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            cancelProgressDialog();
            uploadPicToAmazon();
        }
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mDatabaseReference.child(mCurrentUserId).child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mDatabaseReference.child(mCurrentUserId).child("online").setValue(ServerValue.TIMESTAMP);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
              //  setstatus.child(mChatUserId).child("seen").setValue(false);
                //  Intent intent=getIntent();
              //  intent.putExtra("userid",mChatUserId);
              //  setResult(0, intent);

                if(AppController.getManager().getInterest().equals(Constant.DATING))
                {
                    mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
                    mConvDatabase.keepSynced(true);

                    mConvDatabase.child(mCurrentUserId).child(mChatUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            israting = dataSnapshot.child("is_rated").getValue().toString();
                            if (status == false) {
                                if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                                    if((israting==null && israting.equals(""))|| israting.equals("0"))
                                    {
                                        customdialog();
                                    }
                                    else
                                    {
                                        finish();
                                    }
                                }
                                else if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                                    if(pageid.equals("fromsamesocial"))
                                    {
                                        AppController.getManager().setInterest(Constant.DATING);
                                        finish();
                                    }
                                    else
                                    {
                                        finish();

                                    }
                                }
                            }
                            else {
                                // Intent intent=getIntent();
                                //  intent.putExtra("userid",mChatUserId);
                                if(status==true) {

                                    if ((israting == null && israting.equals("")) || israting.equals("0")) {
                                        customdialog();
                                    } else {
                                        AppController.getManager().setInterest(Constant.SOCIAL);
                                        finish();
                                    }
                                }

                                //  setResult(0, intent);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
                {

                    if (status == false) {
                        if (AppController.getManager().getInterest().equals(Constant.DATING)) {
                            if((israting==null && israting.equals(""))|| israting.equals("0"))
                            {
                                customdialog();
                            }
                            else
                            {
                                finish();
                            }
                        }
                        else if (AppController.getManager().getInterest().equals(Constant.SOCIAL)) {
                            if(pageid.equals("fromsamesocial"))
                            {
                                AppController.getManager().setInterest(Constant.DATING);
                                finish();
                            }
                            else
                            {
                                finish();

                            }
                        }
                    }
                    else {
                        // Intent intent=getIntent();
                        //  intent.putExtra("userid",mChatUserId);
                        if(status==true) {

                            if ((israting == null && israting.equals("")) || israting.equals("0")) {
                                customdialog();
                            } else {
                                AppController.getManager().setInterest(Constant.SOCIAL);
                                finish();
                            }
                        }

                        //  setResult(0, intent);

                    }

                }

                    //  setResult(0, intent
                break;
            case R.id.iv_profile:
                break;
            case R.id.anonomous_chat:
                if(AppController.getManager().getInterest().equals(Constant.DATING))
        {
            showcognitodialog();
        }
        else if(AppController.getManager().getInterest().equals(Constant.SOCIAL)) {

                    mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
                    mConvDatabase.keepSynced(true);

                    mConvDatabase.child(mChatUserId).child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.child("is_rated").exists())
                            {
                                addMessageDialog();
                            }
                            else
                            {
                                iscongnito = dataSnapshot.child("is_cognito").getValue().toString();
                                israting = dataSnapshot.child("is_rated").getValue().toString();

                                AppController.getManager().setInterest(Constant.DATING);
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("page_id", "fromsame");
                                chatIntent.putExtra("user_id", mChatUserId);
                                chatIntent.putExtra("user_name", userName);
                                chatIntent.putExtra("iscongnito", iscongnito);
                                chatIntent.putExtra("israting", israting);
                                chatIntent.putExtra("type", "single");
                                startActivity(chatIntent);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                break;
            case R.id.video_call:
//                CommonUtil.showShortToast(this,"video call click");
                if (quickblox_id != null) {
                    startCall(true);
                } else {
                    Toast.makeText(this, "Error initiating video call", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.audio_call:
                // CommonUtil.showShortToast(this,"audio call click");

                if (quickblox_id != null) {
                    startCall(false);
                } else {
                    Toast.makeText(this, "Error initiating audio call", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chatAddButton:
                checkPermission();
                break;
            case R.id.user_name:

//                Activity activity=(HomeActivity) getApplicationContext();
//                ((HomeActivity) activity).replaceFragment(ViewProfileFragment.newInstance(userData.getId(),null),true);
                break;
        }
    }

    private void startCall(boolean isVideoCall) {
        addCallMessage(isVideoCall);
        Log.d("CALL", "startCall()");
        ArrayList<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(Integer.parseInt(quickblox_id));
        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(ChatActivity.this);

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        PushNotificationSender.sendPushMessage(opponentsList, userName);

        CallActivity.start(this, false);
        Log.d("CALL", "conferenceType = " + conferenceType);
    }

    @Override
    public void deleteMsg(String key) {
        DatabaseReference messageRef = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId);
        DatabaseReference messageRef1 = mRootReference.child("needyyy/chat_module/social_private/").child(mChatUserId).child(mCurrentUserId);

        if (type != null && type.equals("single")) {

            if (AppController.getManager().getInterest() != null && AppController.getManager().getInterest().equals(Constant.DATING)) {
                messageRef = mRootReference.child("needyyy/chat_module/dating_private/").child(mCurrentUserId).child(mChatUserId);
                messageRef1 = mRootReference.child("needyyy/chat_module/dating_private/").child(mChatUserId).child(mCurrentUserId);
            } else {
                messageRef = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId);
                messageRef1 = mRootReference.child("needyyy/chat_module/social_private/").child(mChatUserId).child(mCurrentUserId);
            }
        } else {
            messageRef = mRootReference.child("needyyy/page_management/chat_module/").child(pageid).child(userid);
            messageRef1 = mRootReference.child("needyyy/page_management/chat_module/").child(pageid).child(userid);
        }


        messageRef1.child(key).child("status").setValue("2");
        messageRef.child(key).child("status").setValue("2");
    }


    private void showcognitodialog() {

        FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private/").child(mCurrentUserId).child(mChatUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                      Map<String, Long> map  = (Map<String, Long>) dataSnapshot.getValue();
                      if(map.get("is_cognito")==1)
                      {
                          dialog = new Dialog(ChatActivity.this);
                          dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                          dialog.setContentView(R.layout.dialog_promote_page);
                          dialog.setCancelable(false);
                          dialog.show();
                          Button buttonOk, buttonCancel;
                          buttonOk = dialog.findViewById(R.id.buttonOk);
                          buttonCancel = dialog.findViewById(R.id.buttoncancel);

                          buttonOk.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  getcognito();
                              }
                          });

                          buttonCancel.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  dialog.dismiss();
                              }
                          });
                      }
                      else if(map.get("is_cognito")==0)
                      {
                          mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/social_private");

                          mConvDatabase.keepSynced(true);

                          mConvDatabase.child(mCurrentUserId).child(mChatUserId).addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                  if(dataSnapshot.getValue()!=null)
                                  {
                                      Movetosocial();

                                  }
                                  else
                                  {
                                      SendRequest();
                                  }
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {

                              }
                          });
                        //  Toast.makeText(getApplicationContext(),"User already reviel its profile",Toast.LENGTH_SHORT).show();
                      }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void SendRequest(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sendrequest);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendKnockRequest(userid,1);
                dialog.dismiss();

//                AppController.getManager().setInterest(Constant.DATING);
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AppController.getManager().setInterest(Constant.DATING);
            }
        });
        dialog.show();
    }



    public void sendKnockRequest(String id, int statuss) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequest(id, statuss);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    Toast.makeText(ChatActivity.this, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    //((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
                } else {
                        Toast.makeText(ChatActivity.this, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {


            }
        });

    }



    public void Movetosocial(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.movetosocial);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/social_private");

                mConvDatabase.keepSynced(true);

                mConvDatabase.child(mCurrentUserId).child(mChatUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AppController.getManager().setInterest(Constant.SOCIAL);
                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                            chatIntent.putExtra("page_id", "fromsamesocial");
                            chatIntent.putExtra("user_id", mChatUserId);
                            chatIntent.putExtra("user_name", userName);
                            chatIntent.putExtra("iscongnito", "");
                            chatIntent.putExtra("israting", "");
                            chatIntent.putExtra("type", "single");
                            startActivity(chatIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                dialog.dismiss();

//                AppController.getManager().setInterest(Constant.DATING);
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AppController.getManager().setInterest(Constant.DATING);
            }
        });
        dialog.show();
    }


    public void addCallMessage(boolean isVideo) {
        String message = "call";
        if (isVideo) {
            message = "Video Call";
        } else {
            message = "Audio Call";
        }
        String current_user_ref = "needyyy/chat_module/social_private/" + mCurrentUserId + "/" + mChatUserId;
        String chat_user_ref = "needyyy/chat_module/social_private/" + mChatUserId + "/" + mCurrentUserId;

        DatabaseReference user_message_push = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId).push();
        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message", message);
        messageMap.put("seen", false);
        messageMap.put("type", "call");
        messageMap.put("time", ServerValue.TIMESTAMP);
        messageMap.put("from", mCurrentUserId);
        messageMap.put("status", "1");
        Map messageUserMap = new HashMap();
        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                }
            }
        });
    }

    private void uploadPicToAmazon() {
        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(ChatActivity.this, Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME, "PostImage/" + newProfileImageUri.getLastPathSegment(), new File(newProfileImageUri.getPath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String image_url) {

             //   profile_image = Constant.AWS_URL +Constant.BUCKET_NAME +"/PostImage/" + arr[arr.length - 1];

                profile_picture = Constant.AWS_URL + Constant.BUCKET_NAME+ "/PostImage/" + newProfileImageUri.getLastPathSegment();
                Log.d("IMAGE_URL", "" + image_url);

                String current_user_ref = "needyyy/chat_module/social_private/" + mCurrentUserId + "/" + mChatUserId;
                String chat_user_ref = "needyyy/chat_module/social_private/" + mChatUserId + "/" + mCurrentUserId;

                DatabaseReference user_message_push = mRootReference.child("needyyy/chat_module/social_private/").child(mCurrentUserId).child(mChatUserId).push();

                String push_id = user_message_push.getKey();

                Map messageMap = new HashMap();
                messageMap.put("message", profile_picture);
                messageMap.put("seen", false);
                messageMap.put("type", "image");
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("from", mCurrentUserId);
                messageMap.put("status", "1");
                Map messageUserMap = new HashMap();
                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        cancelProgressDialog();
                        if (databaseError != null) {
                        }
                    }
                });

            }

            @Override
            public void error(String errormsg) {
                cancelProgressDialog();
                Toast.makeText(ChatActivity.this, errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", "" + errormsg);
            }
        });
    }



    private void getcognito() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<Cognito> call = Service.cognito(member_id, "0");
        call.enqueue(new Callback<Cognito>() {
            @Override
            public void onResponse(Call<Cognito> call, Response<Cognito> response) {

                if (response.body() != null) {
                    Cognito cognito = response.body();
                    if (cognito.getStatus()) {
                        cancelProgressDialog();
                        Toast.makeText(ChatActivity.this, "Profile reveiled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Cognito> call, Throwable t) {
                cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        HomeActivity.loadCount
//    }

    //

        //



}



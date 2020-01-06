package com.needyyy.app.Chat.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.GetTimeAgo;
import com.needyyy.app.Chat.Adapter.FriendChatAdapters;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Chat.Conv;
import com.needyyy.app.Chat.common.ChatPojo;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.FriendListAdapters;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.webutils.WebInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.needyyy.app.constants.Constants.kContactRegistrationStatus;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class ChatFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase,mConvDatabase2;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mMessagecount,pagestatus;
    private FirebaseAuth mAuth;

    UserDataResult userData ;
    private long count;
    private String mCurrent_user_id;
    public static Context context;
    private View mMainView;
    boolean isAttached=false;
    private DatabaseReference updatecount;

    String type = "single";
    private String pageid;
    private String iscongnito;
    private String israting;
    private String userid;
    private Boolean status=false;
    String list_user_id="";
    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String type,String page_id){
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("pageid",page_id);

        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null){
            type = getArguments().getString("type");
            pageid=getArguments().getString("pageid");
        }
        context =getActivity();
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_chat, container, false);
        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        //--DEFINING RECYCLERVIEW OF THIS FRAGMENT---
        mConvList = (RecyclerView)mMainView.findViewById(R.id.chatRecycleList);

        //--GETTING CURRENT USER ID---
        mAuth            = FirebaseAuth.getInstance();
        mCurrent_user_id = userData.getId();

        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = null;

        if(type!=null && type.equals("single")){
            if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
                mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private").child(mCurrent_user_id);
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private");
                pagestatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private");
                mConvDatabase2 = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");

            }
            else{
                mConvDatabase2 = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
                mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/social_private").child(mCurrent_user_id);
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private");
                pagestatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private");
            }
        }
        else{
            mConvDatabase2 = FirebaseDatabase.getInstance().getReference().child("needyyy/friend_management/dating_private");
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/page_management/seen_management").child(pageid);

        }
        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);
        updatecount= FirebaseDatabase.getInstance().getReference();
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("needyyy/users");
        mUsersDatabase.keepSynced(true);
        if(type!=null && type.equals("single")){
            if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
                mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/chat_module/dating_private").child(mCurrent_user_id);
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private");
                pagestatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/dating_private");

            }else{
                mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/chat_module/social_private").child(mCurrent_user_id);
                mMessagecount=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private");
                pagestatus=FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private");

            }
        }else{
            mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/page_management/chat_module").child(pageid);

        }
        //mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/chat_module/social_private").child(mCurrent_user_id);

        //---SETTING LAYOUT FOR RECYCLER VIEW----
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

        //--RETURNING THE VIEW OF FRAGMENT--
        return mMainView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isAttached=true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached=false;
    }

    @Override
    public void onStart() {
        super.onStart();
        //---ADDING THE RECYCLERVIEW TO FIREBASE DATABASE DIRECTLY----

        mUsersDatabase.keepSynced(true);
        //--ORDERING THE MESSAGE BY TIME----
        Query conversationQuery = mConvDatabase.orderByChild("time_stamp");
        FirebaseRecyclerAdapter<Conv,ConvViewHolder> friendsConvAdapter=new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(

                //--CLASS FETCHED FROM DATABASE-- LAYOUT OF THE SINGLE ITEM--- HOLDER CLASS(DEFINED BELOW)---QUERY
                Conv.class,
                R.layout.single_chat_layout,
                ConvViewHolder.class,
                conversationQuery
        )
        {
            //---- GETTING DATA FROM DATABSE AND ADDING TO VIEWHOLDER-----
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder,
                                              final Conv conv, int position) {
                 list_user_id = getRef(position).getKey();

                pagestatus.child(list_user_id).child(mCurrent_user_id).child("seen").setValue(false);

                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                mMessagecount.child(list_user_id).child(mCurrent_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("count").getValue()==null)
                        {
                            count=0;
                        }
                        else{
                            count  = (long) dataSnapshot.child("count").getValue();
                            convViewHolder.setcount(count);
                        }
//                        if(count.equals("0"))
//                        {
//
//                        }
//                        else
//                        {
//                            status=true;
//                        }


//                        String quickbloxid=dataSnapshot.child("quick_blox_media_json").getValue().toString();;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                //---IT WORKS WHENEVER CHILD OF mMessageDatabase IS CHANGED---
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        convViewHolder.setMessage(data,conv.isSeen());

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

//                // check econgniyo
//                mConvDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                         iscongnito = dataSnapshot.child().child("is_cognito").toString();
//                        israting = dataSnapshot.child("is_rated").getValue().toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

                //


              //  mMessagecount.child(list_user_id).child("seen_management").setValue("false");


                //---ADDING NAME , IMAGE, ONLINE FEATURE , AND OPENING CHAT ACTIVITY ON CLICK----
                FirebaseDatabase.getInstance().getReference().child("needyyy/users").child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot3) {
                        Log.d("listofuser",dataSnapshot3.toString());
                         String userName = dataSnapshot3.child("name").getValue().toString();
                        String userThumb = dataSnapshot3.child("profile_picture").getValue().toString();
                        String userid = dataSnapshot3.child("user_id").getValue().toString();

//                        String quickbloxid=dataSnapshot.child("quick_blox_media_json").getValue().toString();;


                        mConvDatabase2.child(userid).child(mCurrent_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if(dataSnapshot2.child("is_rated").getValue()!=null)
                                {
                                    israting=dataSnapshot2.child("is_rated").getValue().toString();
                                    iscongnito=dataSnapshot2.child("is_cognito").getValue().toString();
                                    if(iscongnito==null || iscongnito.equals(""))
                                    {
                                        convViewHolder.setUserImage(userThumb,context,"0",userName);
                                    }
                                    else
                                    {
                                        if(iscongnito.equals("1"))
                                        {
                                            convViewHolder.setUserImage(userThumb,context,"1",userName);

                                        }
                                        else
                                        {
                                            convViewHolder.setUserImage(userThumb,context,"0",userName);

                                        }
                                    }
                                    Log.d("iscongnito",iscongnito);
                                }
                                else
                                {
                                    if(iscongnito==null || iscongnito.equals(""))
                                    {
                                        convViewHolder.setUserImage(userThumb,context,"0",userName);
                                    }
                                    else
                                    {
                                        if(iscongnito.equals("1"))
                                        {
                                            convViewHolder.setUserImage(userThumb,context,"1",userName);

                                        }
                                        else
                                        {
                                            convViewHolder.setUserImage(userThumb,context,"0",userName);

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("error",databaseError.getMessage());

                            }
                        });

                        if(dataSnapshot3.hasChild("is_online")){
                            String userOnline = dataSnapshot3.child("is_online").getValue().toString();
                            convViewHolder.setUserOnline(userOnline);
                        }

                       // convViewHolder.setName(userName);



                        //--OPENING CHAT ACTIVITY FOR CLICKED USER----
                        String finalQuickbloxid = "12345678";
                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if(type!=null && type.equals("single")){
                                    if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
                                        String   increasechatvalue="needyyy/seen_management/dating_private/"+ userid+ "/" +mCurrent_user_id;
                                        //pagestatus.child(mCurrent_user_id).child(userid).child("seen").setValue("true");
                                        Map map=new HashMap();
                                        map.put("seen",true);
                                        map.put("count",0);
                                        Map countmap=new HashMap();
                                        countmap.put(increasechatvalue,map);

                                        updatecount.updateChildren(countmap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                                } else {
                                                    //Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });



                                    }
                                    else
                                        {
                                            String   increasechatvalue="needyyy/seen_management/social_private/"+ userid+ "/" +mCurrent_user_id;
                                            Map map=new HashMap();
                                            map.put("seen",true);
                                            map.put("count",0);
                                            Map countmap=new HashMap();
                                            countmap.put(increasechatvalue,map);

                                            updatecount.updateChildren(countmap, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                        Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                                    } else {
                                                        //Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                    }
                                }else{

                                }



                                mConvDatabase2.child(userid).child(mCurrent_user_id)
                                        .addValueEventListener(new ValueEventListener() {
                                                                                      @Override
                                                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                                                          if (dataSnapshot2.child("is_rated").getValue() != null) {
                                                                                              israting = dataSnapshot2.child("is_rated").getValue().toString();
                                                                                              iscongnito = dataSnapshot2.child("is_cognito").getValue().toString();
                                                                                              Intent chatIntent = new Intent(context, ChatActivity.class);
                                                                                              chatIntent.putExtra("page_id",pageid);
                                                                                              chatIntent.putExtra("user_id",userid);
                                                                                              chatIntent.putExtra("user_name",userName);
                                                                                              chatIntent.putExtra("iscongnito",iscongnito);
                                                                                              chatIntent.putExtra("israting",israting);
                                                                                              chatIntent.putExtra("type",type);
                                                                                              chatIntent.putExtra("quickbloxid", finalQuickbloxid);

                                                                                              if(isAttached)
                                                                                              startActivityForResult(chatIntent,0);
                                                                                          }
                                                                                          else
                                                                                          {
                                                                                              Intent chatIntent = new Intent(context, ChatActivity.class);
                                                                                              chatIntent.putExtra("page_id",pageid);
                                                                                              chatIntent.putExtra("user_id",userid);
                                                                                              chatIntent.putExtra("user_name",userName);
                                                                                              chatIntent.putExtra("iscongnito",iscongnito);
                                                                                              chatIntent.putExtra("israting",israting);
                                                                                              chatIntent.putExtra("type",type);
                                                                                              chatIntent.putExtra("quickbloxid", finalQuickbloxid);
                                                                                              if(isAttached)
                                                                                              startActivityForResult(chatIntent,0);
                                                                                          }
                                                                                      }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mConvList.setAdapter(friendsConvAdapter);
    }

    //--- DATA IS ADDING WITHIN SINGLE HOLDER-----
    public static class ConvViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMessage(String message, boolean isSeen) {
            TextView userStatusView = (TextView) mView.findViewById(R.id.tv_chattext);
            userStatusView.setText(message);

            //--SETTING BOLD FOR NOT SEEN MESSAGES---
            if (isSeen) {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }

        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.tv_profile_name);
            userNameView.setText(name);
        }

        public void setcount(Long name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.count);
            RelativeLayout circle = (RelativeLayout) mView.findViewById(R.id.circle);

            if(name==0)
            {
                circle.setVisibility(View.GONE);
            }
            else
            {
                circle.setVisibility(View.VISIBLE);
                userNameView.setText(String.valueOf(name));
            }
        }



        public void setUserImage(String userThumb, Context context,String value,String username) {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.civ_profile_pic);
            TextView userNameView = (TextView) mView.findViewById(R.id.tv_profile_name);


            //--SETTING IMAGE FROM USERTHUMB TO USERIMAGEVIEW--- IF ERRORS OCCUR , ADD USER_IMG----
            if(value.equals("0")) {
                if (!userThumb.isEmpty()) {
                    Picasso.get().load(userThumb).placeholder(R.drawable.needyy).into(userImageView);
                }
                userNameView.setText(username);
            }
            else if(value.equals("1"))
            {
                if(AppController.getManager().getInterest().equals(Constant.DATING))
                {
                    String hiddenname="";
                    for(int k=0;k<username.length();k++)
                    {
                        hiddenname=hiddenname+"*";
                    }
//                SpannableString string = new SpannableString(username);
//                MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
//                string.setSpan(new MaskFilterSpan(blurMask), 0, username.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    userNameView.setText(hiddenname);
                    Glide.with(context).load(userThumb)
                            .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)).placeholder(R.drawable.needyy))
                            .into(userImageView);
                }
                else if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
                {
                    if (!userThumb.isEmpty()) {
                        Picasso.get().load(userThumb).placeholder(R.drawable.needyy).into(userImageView);
                    }
                    userNameView.setText(username);
                }


            }
        }


        public void setUserOnline(String onlineStatus) {

            RelativeLayout userOnlineView = (RelativeLayout) mView.findViewById(R.id.rl_online);

            TextView tvLastseen = (TextView) mView.findViewById(R.id.tv_lastseen);


            if (onlineStatus.equals("online")) {
                userOnlineView.setVisibility(View.VISIBLE);
                tvLastseen.setVisibility(View.INVISIBLE);
            } else {
                userOnlineView.setVisibility(View.INVISIBLE);
                tvLastseen.setVisibility(View.VISIBLE);
                GetTimeAgo getTimeAgo = new GetTimeAgo();
                long lastTime = Long.parseLong(onlineStatus);
                String lastSeen = getTimeAgo.getTimeAgo(lastTime, context);
                tvLastseen.setText(lastSeen);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        pagestatus.child(userid).child(mCurrent_user_id).child("seen").setValue(false);
    }




//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==0)
//        {
//            String userid=data.getStringExtra("userid");
//            pagestatus.child(userid).child(mCurrent_user_id).child("seen_management").setValue("false");
//        }
//    }
}

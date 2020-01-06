package com.needyyy.app.Chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.needyyy.app.Base.GetTimeAgo;
import com.needyyy.app.Chat.Adapter.AllUserAdapter;
import com.needyyy.app.Chat.Adapter.ParticipantAdapter;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Chat.Conv;
import com.needyyy.app.Chat.GroupActivity;
import com.needyyy.app.Chat.interfaces.ItemClickListener;
import com.needyyy.app.Chat.model.Room;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Progress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class NewGroupFragment extends Fragment implements  View.OnClickListener{

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    UserDataResult userData ;
    private String mCurrent_user_id;
    public static Context context;
    private View mMainView;

    EditText etGroupName;
    TextView tvDone;
    ArrayList<People> allUserList;
    ArrayList<People> selectedUserList;
    ItemClickListener itemClickListener;

    ParticipantAdapter participantAdapter;
    AllUserAdapter allUserAdapter;
    RecyclerView rvParticipant, rvAllUser;

    private Set<String> listIDChoose;
    private Set<String> listIDRemove;

    public NewGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context =getActivity();

        ((GroupActivity)getActivity()).setTitle("Create Group");

        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_new_group, container, false);
        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        listIDChoose = new HashSet<>();
        listIDRemove = new HashSet<>();
        listIDChoose.add(userData.getId());

        //--DEFINING RECYCLERVIEW OF THIS FRAGMENT---
        mConvList = (RecyclerView)mMainView.findViewById(R.id.chatRecycleList);
        etGroupName = mMainView.findViewById(R.id.etGroupName);
        rvParticipant = mMainView.findViewById(R.id.rvParticipant);
        tvDone = mMainView.findViewById(R.id.tvDone);

        tvDone.setOnClickListener(this);

        //--GETTING CURRENT USER ID---
        mAuth            = FirebaseAuth.getInstance();
        mCurrent_user_id = userData.getId();

        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/seen_management/social_private").child(mCurrent_user_id);

        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("needyyy/users");
        mUsersDatabase.keepSynced(true);

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("needyyy/chat_module/social_private").child(mCurrent_user_id);

        //---SETTING LAYOUT FOR RECYCLER VIEW----
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

        initAdapters();

        //--RETURNING THE VIEW OF FRAGMENT--
        return mMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        //---ADDING THE RECYCLERVIEW TO FIREBASE DATABASE DIRECTLY----

        //--ORDERING THE MESSAGE BY TIME----
        Query conversationQuery = mConvDatabase.orderByChild("time_stamp");
        FirebaseRecyclerAdapter<Conv,ConvViewHolder> friendsConvAdapter=new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(

                //--CLASS FETCHED FROM DATABASE-- LAYOUT OF THE SINGLE ITEM--- HOLDER CLASS(DEFINED BELOW)---QUERY
                Conv.class,
                R.layout.layout_group_user_item,
                ConvViewHolder.class,
                conversationQuery
        ) {

            //---- GETTING DATA FROM DATABSE AND ADDING TO VIEWHOLDER-----
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder, final Conv conv, int position) {

                final String list_user_id = getRef(position).getKey();
                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        People user  = allUserList.get(position);

                        //Toast.makeText(context, ""+user.getName(), Toast.LENGTH_SHORT).show();

                        if(allUserList.get(position).isSelected()){
                            allUserList.get(position).setSelected(false);
                            convViewHolder.rl_check.setVisibility(View.GONE);
                            selectedUserList.remove(selectedUserList.size()-1);
                            listIDChoose.remove(user.getId());
                            participantAdapter.notifyDataSetChanged();
                        }else{
                            convViewHolder.rl_check.setVisibility(View.VISIBLE);
                            selectedUserList.add(user);
                            allUserList.get(position).setSelected(true);
                            participantAdapter.notifyDataSetChanged();
                            listIDChoose.add(user.getId());

                        }

                                /*Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id",list_user_id);
                                chatIntent.putExtra("user_name",userName);
                                startActivity(chatIntent);*/
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

                //---ADDING NAME , IMAGE, ONLINE FEATURE , AND OPENING CHAT ACTIVITY ON CLICK----
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("profile_picture").getValue().toString();

                        People user = new People();
                        user.setName(userName);
                        user.setId(dataSnapshot.getKey());

                        allUserList.add(user);



                        convViewHolder.setName(userName);
                        convViewHolder.setUserImage(userThumb,getContext());

                        //--OPENING CHAT ACTIVITY FOR CLICKED USER----
                        /*convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id",list_user_id);
                                chatIntent.putExtra("user_name",userName);
                                startActivity(chatIntent);
                            }
                        });*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mConvList.setAdapter(friendsConvAdapter);

    }


    private void createGroup() {

        ((GroupActivity)getActivity()).showProgress();

        final String idGroup = (userData.getId() + System.currentTimeMillis()).hashCode() + "";
        Room room = new Room();
        for (String id : listIDChoose) {
            room.member.add(id);
        }
        room.groupInfo.put("name", etGroupName.getText().toString());
        room.groupInfo.put("admin", userData.getId());
        FirebaseDatabase.getInstance().getReference().child("needyyy/group/" + idGroup).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addRoomForUser(idGroup, 0);
            }
        });
    }

    private void addRoomForUser(final String roomId, final int userIndex) {
        if (userIndex == listIDChoose.size()) {
            ((GroupActivity)getActivity()).hideProgress();
                Toast.makeText(context, "Create group success", Toast.LENGTH_SHORT).show();
                //AddGroupActivity.this.finish();
                ((GroupActivity)getActivity()).popStack();

        } else {
            FirebaseDatabase.getInstance().getReference().child("needyyy/users/" + listIDChoose.toArray()[userIndex] + "/group/" + roomId).setValue(roomId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addRoomForUser(roomId, userIndex + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ((GroupActivity)getActivity()).hideProgress();
                    Toast.makeText(context, "Unable create group", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    public void initAdapters(){
        allUserList = new ArrayList<>();
        selectedUserList = new ArrayList<>();

        participantAdapter = new ParticipantAdapter(context, selectedUserList);
        allUserAdapter = new AllUserAdapter(context, allUserList, itemClickListener);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        /*rvAllUser.setLayoutManager(mLayoutManager);
        rvAllUser.setItemAnimator(new DefaultItemAnimator());
        rvAllUser.setAdapter(allUserAdapter);*/

        rvParticipant.setItemAnimator(new DefaultItemAnimator());
        rvParticipant.setAdapter(participantAdapter);


    }

    public void checkValidation(){
        if(etGroupName.getText().toString().trim().isEmpty()){
            Toast.makeText(context, "Enter group name", Toast.LENGTH_SHORT).show();
        }else if(selectedUserList!=null && selectedUserList.size()<2){
            Toast.makeText(context, "Select atleast 2 users to create group", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, ""+selectedUserList.size()+" users selected", Toast.LENGTH_SHORT).show();
            createGroup();
        }
    }


    //--- DATA IS ADDING WITHIN SINGLE HOLDER-----
    public static class ConvViewHolder extends RecyclerView.ViewHolder{

        View mView;
        RelativeLayout rl_check;

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView =itemView;
            rl_check = itemView.findViewById(R.id.rl_check);
        }

        public void setMessage(String message,boolean isSeen){
            TextView userStatusView = (TextView) mView.findViewById(R.id.tv_chattext);
            userStatusView.setText(message);

            //--SETTING BOLD FOR NOT SEEN MESSAGES---

        }

        public void setSelected(){

        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.tv_profile_name);
            userNameView.setText(name);
        }


        public void setUserImage(String userThumb, Context context) {

            CircleImageView userImageView = (CircleImageView)mView.findViewById(R.id.civ_profile_pic);

            //--SETTING IMAGE FROM USERTHUMB TO USERIMAGEVIEW--- IF ERRORS OCCUR , ADD USER_IMG----
            if (!userThumb.isEmpty())
            Picasso.get().load(userThumb).placeholder(R.drawable.profile_default).into(userImageView);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDone:
                checkValidation();
                break;
        }
    }



}

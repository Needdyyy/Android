package com.needyyy.app.Chat.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.needyyy.app.Chat.Adapter.AllUserAdapter;
import com.needyyy.app.Chat.Adapter.ParticipantAdapter;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Chat.Conv;
import com.needyyy.app.Chat.GroupActivity;
import com.needyyy.app.Chat.interfaces.ItemClickListener;
import com.needyyy.app.Chat.model.Group;
import com.needyyy.app.Chat.model.ListFriend;
import com.needyyy.app.Chat.model.Room;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.Progress;
import com.needyyy.app.utils.UploadAmazonS3;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class CreateGroupFragment extends Fragment implements  View.OnClickListener{

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

    private ListFriend listFriend;

    private Set<String> listIDChoose;
    private Set<String> listIDRemove;
    private boolean isEditGroup;
    String group_name = "";
    Group groupEdit;
    ImageView ivIcon;
    Progress mprogress;

    private File newFile;
    private Uri newProfileImageUri;
    private String state, imageName, profile_picture = "";
    public static int REQUEST_CODE_GALLERY = 901;
    public static int REQUEST_CODE_CAMERA = 902;


    public CreateGroupFragment() {
        // Required empty public constructor
    }

    public static CreateGroupFragment newInstance(Bundle args){
        CreateGroupFragment fragment = new CreateGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context =getActivity();

        ((GroupActivity)getActivity()).setTitle("Create Group");



        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_create_group, container, false);
        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        listIDChoose = new HashSet<>();
        listIDRemove = new HashSet<>();
        listIDChoose.add(userData.getId());

        //--DEFINING RECYCLERVIEW OF THIS FRAGMENT---
        mConvList = (RecyclerView)mMainView.findViewById(R.id.chatRecycleList);
        etGroupName = mMainView.findViewById(R.id.etGroupName);
        rvParticipant = mMainView.findViewById(R.id.rvParticipant);
        tvDone = mMainView.findViewById(R.id.tvDone);
        ivIcon = mMainView.findViewById(R.id.ivIcon);

        tvDone.setOnClickListener(this);
        ivIcon.setOnClickListener(this);


        if (getArguments()!=null&& getArguments().getString("groupId") != null) {
            group_name = getArguments().getString("groupName");
            isEditGroup = true;
            String idGroup = getArguments().getString("groupId");
            etGroupName.setText(group_name);
            groupEdit = (Group) getArguments().getSerializable("group");
            listFriend = groupEdit.listFriend;
            tvDone.setText("Save");
        } else {
            isEditGroup = false;
        }

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
        //  linearLayoutManager.setReverseLayout(true);
        // linearLayoutManager.setStackFromEnd(true);

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
                R.layout.groupchatpage,
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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mConvList.setAdapter(friendsConvAdapter);

    }

    private void editGroup() {
        ((GroupActivity)getActivity()).showProgress();

        final String idGroup = groupEdit.id;
        Room room = new Room();
        for (String id : listIDChoose) {
            room.member.add(id);
        }
        room.groupInfo.put("name", etGroupName.getText().toString().trim());
        room.groupInfo.put("admin", userData.getId());
        FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/" + idGroup).setValue(room)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addRoomForUser(idGroup, 0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((GroupActivity)getActivity()).showProgress();
                        Toast.makeText(getActivity(), "Unable to edit group", Toast.LENGTH_SHORT).show();
                    }
                });
    }


/*
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
*/

    private void uploadPicToAmazon() {
        showProgressDialog();
        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(getActivity(), Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME, "PostImage/" + newProfileImageUri.getLastPathSegment(), new File(newProfileImageUri.getPath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String image_url) {
                cancelProgressDialog();

                //   profile_image = Constant.AWS_URL +Constant.BUCKET_NAME +"/PostImage/" + arr[arr.length - 1];

                profile_picture = Constant.AWS_URL + Constant.BUCKET_NAME+ "/PostImage/" + newProfileImageUri.getLastPathSegment();
                Log.d("IMAGE_URL", "" + profile_picture);

            }

            @Override
            public void error(String errormsg) {
                cancelProgressDialog();
                Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", "" + errormsg);
            }
        });
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
        room.groupInfo.put("icon", profile_picture);
        //room.groupInfo.put("icon", "https://toppng.com/public/uploads/preview/roup-of-people-in-a-formation-free-icon-svg-psd-png-employee-engagement-stats-11563001272via6lcn5i3.png");
        FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/" + idGroup).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addRoomForUser(idGroup, 0);
            }
        });
    }

/*
    private void addRoomForUser(final String roomId, final int userIndex) {
        if (userIndex == listIDChoose.size()) {
            ((GroupActivity)getActivity()).hideProgress();
            Toast.makeText(context, "Group Created", Toast.LENGTH_SHORT).show();
            //AddGroupActivity.this.finish();
            ((GroupActivity)getActivity()).popStack();

        } else {
            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info/" + listIDChoose.toArray()[userIndex] + "/" + roomId).setValue(roomId).addOnCompleteListener(new OnCompleteListener<Void>() {
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
*/


    private void addRoomForUser(final String roomId, final int userIndex) {
        if (userIndex == listIDChoose.size()) {
            if (!isEditGroup) {
                ((GroupActivity)getActivity()).hideProgress();
                Toast.makeText(context, "Group Created", Toast.LENGTH_SHORT).show();
                ((GroupActivity)getActivity()).popStack();
            } else {
                deleteRoomForUser(roomId, 0);
            }
        } else {
            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info/" + listIDChoose.toArray()[userIndex] + "/" + roomId).setValue(roomId).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    private void deleteRoomForUser(final String roomId, final int userIndex) {
        if (userIndex == listIDRemove.size()) {
            ((GroupActivity)getActivity()).hideProgress();
            Toast.makeText(context, "Group Edited", Toast.LENGTH_SHORT).show();
            ((GroupActivity)getActivity()).popStack();
        } else {
            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info/" + listIDRemove.toArray()[userIndex] + "/" + roomId).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            deleteRoomForUser(roomId, userIndex + 1);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ((GroupActivity)getActivity()).hideProgress();
                            Toast.makeText(context, "Unable edit group", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


/*
    private void addRoomForUser(final String roomId, final int userIndex) {
        if (userIndex == listIDChoose.size()) {
            ((GroupActivity)getActivity()).hideProgress();
                Toast.makeText(context, "Group Created", Toast.LENGTH_SHORT).show();
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
*/


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


    public void checkPermission() {
        Dexter.withActivity(getActivity())
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
                            Toast.makeText(getActivity(), "You must grant all permissions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .onSameThread()
                .check();
    }


    public void showSelectImageDialog() {

        final Dialog dialog = new Dialog(getActivity());
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
            newFile = new File(getActivity().getFilesDir(), imageName);
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
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }


    public void checkValidation(){
        if(etGroupName.getText().toString().trim().isEmpty()){
            Toast.makeText(context, "Enter group name", Toast.LENGTH_SHORT).show();
        }else if(selectedUserList!=null && selectedUserList.size()<2){
            Toast.makeText(context, "Select atleast 2 users to create group", Toast.LENGTH_SHORT).show();
        }else{
            if (isEditGroup){
                editGroup();
            }else{
                createGroup();
            }
            //Toast.makeText(context, ""+selectedUserList.size()+" users selected", Toast.LENGTH_SHORT).show();

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
        }

        public void setSelected(){

        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.tv_profile_name);
            userNameView.setText(name);
        }


        public void setUserImage(String userThumb, Context context) {

            CircleImageView userImageView = (CircleImageView)mView.findViewById(R.id.civ_profile_pic);
            if (!userThumb.isEmpty())
                Picasso.get().load(userThumb).placeholder(R.drawable.profile_default).into(userImageView);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();
                uploadPicToAmazon();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong, Try again...", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            uploadPicToAmazon();
        }
    }


    public void showProgressDialog() {
        mprogress = new Progress(getActivity());
        mprogress.setCancelable(false);
        mprogress.show();
    }

    public void cancelProgressDialog() {

        if (mprogress != null)
            if (mprogress.isShowing()) {
                mprogress.dismiss();
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDone:
                checkValidation();
                break;
            case R.id.ivIcon:
                checkPermission();
                break;

        }
    }



}

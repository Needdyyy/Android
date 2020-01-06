package com.needyyy.app.Chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.needyyy.app.Chat.model.Consersation;
import com.needyyy.app.Chat.model.Message;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.AppTextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    private String roomId;
    private ArrayList<CharSequence> idFriend;
    private Consersation consersation;
    private ImageButton btnSend;
    private EditText editWriteMessage;
    private LinearLayoutManager linearLayoutManager;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;
    public UserDataResult userData ;
    private AppTextView group_name, tv_lastseen;
    private ImageView toolbar_back,anonymousChat,videoCalling,audioCalling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        Intent intentData = getIntent();
        idFriend = intentData.getCharSequenceArrayListExtra(Constant.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(Constant.INTENT_KEY_CHAT_ROOM_ID);
        String nameFriend = intentData.getStringExtra(Constant.INTENT_KEY_CHAT_FRIEND);

        consersation = new Consersation();
        group_name = findViewById(R.id.user_name);
        tv_lastseen = findViewById(R.id.tv_lastseen);
        editWriteMessage = findViewById(R.id.editWriteMessage);
        toolbar_back = findViewById(R.id.toolbar_back);
        btnSend = (ImageButton) findViewById(R.id.btnSend);

        anonymousChat = findViewById(R.id.anonomous_chat);
        videoCalling  = findViewById(R.id.video_call);
        audioCalling  = findViewById(R.id.audio_call);

        btnSend.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);

        String base64AvataUser = userData.getProfilePicture();
        if (base64AvataUser!=null && !base64AvataUser.equals(Constant.STR_DEFAULT_BASE64)) {
            try{
                byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
                bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }catch (Exception e){

            }

        } else {
            bitmapAvataUser = null;
        }

        anonymousChat.setVisibility(View.GONE);
        videoCalling.setVisibility(View.GONE);
        audioCalling.setVisibility(View.GONE);
        tv_lastseen.setVisibility(View.GONE);



        if (idFriend != null && nameFriend != null) {
            group_name.setText(nameFriend);
            //getSupportActionBar().setTitle(nameFriend);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageAdapter(this, consersation, bitmapAvataFriend, bitmapAvataUser);
            FirebaseDatabase.getInstance().getReference().child("needyyy/message/" + roomId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        Message newMessage = new Message();
                        newMessage.idSender = (String) mapMessage.get("idSender");
                        newMessage.idReceiver = (String) mapMessage.get("idReceiver");
                        newMessage.text = (String) mapMessage.get("text");
                        newMessage.timestamp = (long) mapMessage.get("timestamp");
                        newMessage.image=(String) mapMessage.get("image");
                        consersation.getListMessageData().add(newMessage);
                        adapter.notifyDataSetChanged();
                        linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                    }
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
            recyclerChat.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("idFriend", idFriend.get(0));
        setResult(RESULT_OK, result);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.text = content;
                newMessage.idSender = userData.getId();
                newMessage.idReceiver = roomId;
                newMessage.timestamp = System.currentTimeMillis();
                newMessage.image=userData.getProfilePicture();
                FirebaseDatabase.getInstance().getReference().child("needyyy/message/" + roomId).push().setValue(newMessage);
            }
        }else if(view.getId()== R.id.toolbar_back){
            onBackPressed();
        }
    }
}

class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Consersation consersation;
    private HashMap<String, Bitmap> bitmapAvata;
    private HashMap<String, DatabaseReference> bitmapAvataDB;
    private Bitmap bitmapAvataUser;
    private UserDataResult userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));


    public ListMessageAdapter(Context context, Consersation consersation, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
        this.context = context;
        this.consersation = consersation;
        this.bitmapAvata = bitmapAvata;
        this.bitmapAvataUser = bitmapAvataUser;
        bitmapAvataDB = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GroupChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == GroupChatActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMessageFriendHolder) {
            ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
        //    Bitmap currentAvata = bitmapAvata.get(consersation.getListMessageData().get(position).idSender);
            Glide.with(context)
                    .load(consersation.getListMessageData().get(position).image)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(((ItemMessageFriendHolder) holder).avata);

//            if (currentAvata != null) {
//                ((ItemMessageFriendHolder) holder).avata.setImageBitmap(currentAvata);
//            } else {
//                final String id = consersation.getListMessageData().get(position).idSender;
//                if(bitmapAvataDB.get(id) == null){
//                    bitmapAvataDB.put(id, FirebaseDatabase.getInstance().getReference().child("needyyy/user/" + id + "/avata"));
//                    bitmapAvataDB.get(id).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                String avataStr = (String) dataSnapshot.getValue();
//                                if(!avataStr.equals(Constant.STR_DEFAULT_BASE64)) {
//                                    byte[] decodedString = Base64.decode(avataStr, Base64.DEFAULT);
//                                    GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
//                                }else{
//                                    GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_default));
//                                }
//                                notifyDataSetChanged();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
        } else if (holder instanceof ItemMessageUserHolder) {
            ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
//            if (bitmapAvataUser != null) {
//                ((ItemMessageUserHolder) holder).avata.setImageBitmap(bitmapAvataUser);
//            }
            Glide.with(context)
                    .load(consersation.getListMessageData().get(position).image)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(((ItemMessageUserHolder) holder).avata);


        }
    }

    @Override
    public int getItemViewType(int position) {
        return consersation.getListMessageData().get(position).idSender.equals(userData.getId()) ? GroupChatActivity.VIEW_TYPE_USER_MESSAGE : GroupChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
    }
    @Override
    public int getItemCount() {
        return consersation.getListMessageData().size();
    }
}

class ItemMessageUserHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageUserHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView2);
    }
}

class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageFriendHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView3);
    }
}

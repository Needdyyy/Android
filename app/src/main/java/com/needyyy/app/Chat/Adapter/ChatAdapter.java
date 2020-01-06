package com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.needyyy.app.Chat.Messages;
import com.needyyy.app.Chat.common.ChatPojo;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {

    private List<ChatPojo> dataSet;
    Context mContext;
    private static final String TAG = "LiveAdapter";
    UserDataResult userData ;
    private  int SENDMESSAGETYPE=1,RECEIVEMESSAGETYPE=2 ;


    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;
    Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView ivAdmin,ivUser;
        TextView tvAdmin,tvUser,tv_username1,tv_username;
        LinearLayout llAdmin,llUser;

        public MyViewHolder(View itemView) {
            super(itemView);
//            tv_username1= itemView.findViewById(R.id.tv_username1);
//            tv_username= itemView.findViewById(R.id.tv_username);
//            ivUser= itemView.findViewById(R.id.iv_user);
//            ivAdmin= itemView.findViewById(R.id.iv_admin);
//            tvUser= itemView.findViewById(R.id.tv_user);
//            tvAdmin= itemView.findViewById(R.id.tv_admin);
//            llUser= itemView.findViewById(R.id.ll_user);
//            llAdmin= itemView.findViewById(R.id.ll_admin);
        }
    }

    public ChatAdapter() {
    }
    public ChatAdapter(Context  context, List<ChatPojo> dataSet) {
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        this.mContext =context ;
        this.dataSet = dataSet;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View sendmessageview = inflater.inflate(R.layout.list_send_message, parent, false);
                viewHolder = new SendMessageViewHolder(sendmessageview);
                break;
            case 2:
                View receivemessageview = inflater.inflate(R.layout.list_recv_message, parent, false);
                viewHolder = new ReceiveMessageViewHolder(receivemessageview);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatPojo chatPojo = dataSet.get(i);
        switch (viewHolder.getItemViewType()) {
            case 1:
                SendMessageViewHolder sendMessageViewHolder = (SendMessageViewHolder) viewHolder;
                sendMessageViewHolder.bindContent(chatPojo);
                sendMessageViewHolder.bubbleTextView.setText(chatPojo.getMessage());
                break;
            case 2:
                ReceiveMessageViewHolder receiveMessageViewHolder = (ReceiveMessageViewHolder) viewHolder;
                receiveMessageViewHolder.bindContent(chatPojo);
                receiveMessageViewHolder.bubbleTextView.setText(chatPojo.getMessage());
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (userData.getId().equals(dataSet.get(position).getId()))
            return SENDMESSAGETYPE;
        else
            return RECEIVEMESSAGETYPE;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    public class SendMessageViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        ChatPojo chatPojo ;
        CircleImageView civProfile;
        TextView tvProfileName,tvLastdeen,tvmessage;
        BubbleTextView bubbleTextView ;
        public SendMessageViewHolder(View itemView) {
            super(itemView);
            bubbleTextView = itemView.findViewById(R.id.tvMessage);
            civProfile     = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName  = itemView.findViewById(R.id.tv_profile_name);
            tvLastdeen     = itemView.findViewById(R.id.tv_lastseen);
            tvmessage      = itemView.findViewById(R.id.tv_chattext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

                }
            });
        }

        public void bindContent(ChatPojo chatPojo) {
            this.chatPojo = chatPojo ;
        }
    }
    public class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        TextView bubbleTextView ;
        ChatPojo chatPojo ;
        CircleImageView civProfile;
        TextView tvProfileName,tvLastdeen,tvmessage;

        public ReceiveMessageViewHolder(View itemView) {
            super(itemView);
            bubbleTextView = itemView.findViewById(R.id.tvMessage);
            civProfile     = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName  = itemView.findViewById(R.id.tv_profile_name);
            tvLastdeen     = itemView.findViewById(R.id.tv_lastseen);
            tvmessage      = itemView.findViewById(R.id.tv_chattext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

                }
            });
        }

        public void bindContent(ChatPojo chatPojo) {
            this.chatPojo = chatPojo ;
        }
    }

}

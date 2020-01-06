package com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Knocks.fragment.SendKnocksRequestFragment;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class FriendChatAdapters extends RecyclerView.Adapter<FriendChatAdapters.SignleFriendsViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    ArrayList<People> data = new ArrayList<>();

    public FriendChatAdapters(Context context, ArrayList<People> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public SignleFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_layout, parent, false);
        return new SignleFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignleFriendsViewHolder viewHolder, final int position) {
        People people = data.get(position);
        viewHolder.tvProfileName.setText(people.getName());
        viewHolder.tvLastdeen.setText(people.getLastSeen());
        viewHolder.tvmessage.setText(people.getMessage());
        if (Constant.ISONLINE.equals(people.getIsOnline()))
            viewHolder.rlOnline.setVisibility(View.VISIBLE);
        else
            viewHolder.rlOnline.setVisibility(View.GONE);
        if (people.getIsFriend().equals("1") || people.getIsFriend().equals("4")){
            if (!TextUtils.isEmpty(people.getProfilePicture())) {
                Glide.with(context)
                        .load(people.getProfilePicture())
                        .into(viewHolder.civProfile);
            } else {
                viewHolder.civProfile.setImageResource(R.drawable.needyy);
            }
        }else{
            CommonUtil.ConvertURLToBitmap(people.getProfilePicture(),context ,viewHolder.civProfile);
        }

//        viewHolder.accept.setVisibility(View.VISIBLE);
//        viewHolder.decline.setVisibility(View.VISIBLE);
//        viewHolder.tvRequestStatus.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
//        return 10;
    }

    public class SignleFriendsViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        CircleImageView civProfile;
        TextView tvProfileName,tvLastdeen,tvmessage;
        private RelativeLayout rlOnline;
        public SignleFriendsViewHolder(View itemView) {
            super(itemView);
            rlOnline       = itemView.findViewById(R.id.rl_online);
            civProfile     = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName  = itemView.findViewById(R.id.tv_profile_name);
            tvLastdeen     = itemView.findViewById(R.id.tv_lastseen);
            tvmessage      = itemView.findViewById(R.id.tv_chattext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chat_node",data.get(getAdapterPosition()).getChatNode());
                    intent.putExtra(Constant.kData, data.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

//        public void getRequestStatus(String id, String status){
//            WebInterface Service = AppController.getRetrofitInstance().create(WebInterface.class);
//            Call<AcceptRejectRequest> call = Service.acceptRejectRequest(id, status);
//            call.enqueue(new Callback<AcceptRejectRequest>() {
//                @Override
//                public void onResponse(Call<AcceptRejectRequest> call, Response<AcceptRejectRequest> response) {
////                cancelProgressDialog();
//                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
//                    AcceptRejectRequest acceptRejectRequest = response.body();
//                    if (acceptRejectRequest.getStatus()) {
//
////                    arrGetRequests.addAll(acceptRejectRequest.getData());
//                    } else {
////
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AcceptRejectRequest> call, Throwable t) {
//                    cancelProgressDialog();
//                }
//            });
//
//        }
}
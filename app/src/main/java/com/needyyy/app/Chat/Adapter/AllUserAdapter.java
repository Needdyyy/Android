package com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.needyyy.app.Chat.interfaces.ItemClickListener;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.SignleFriendsViewHolder> {

    Context context;
    ArrayList<People> data ;
    ItemClickListener itemClickListener;

    public AllUserAdapter(Context context, ArrayList<People> data, ItemClickListener itemClickListener) {
        this.context = context;
        this.data = data;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SignleFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_user_item, parent, false);
        return new SignleFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignleFriendsViewHolder viewHolder, int position) {
        People people = data.get(position);
        viewHolder.tvProfileName.setText(people.getName());

        if (!TextUtils.isEmpty(people.getProfilePicture())) {
            Glide.with(context)
                    .load(people.getProfilePicture())
                    .into(viewHolder.civProfile);
        }

        viewHolder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(position);
                    /*UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chat_node",data.get(getAdapterPosition()).getChatNode());
                    intent.putExtra(Constant.kData, data.get(getAdapterPosition()));
                    context.startActivity(intent);*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SignleFriendsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civProfile;
        TextView tvProfileName;
        RelativeLayout rl_check;
        LinearLayout layout_item;

        public SignleFriendsViewHolder(View itemView) {
            super(itemView);
            civProfile     = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName  = itemView.findViewById(R.id.tv_profile_name);
            rl_check  = itemView.findViewById(R.id.rl_check);
            layout_item  = itemView.findViewById(R.id.layout_item);

        }
    }

}
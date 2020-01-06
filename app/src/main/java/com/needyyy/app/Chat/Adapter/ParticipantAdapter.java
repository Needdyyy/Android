package com.needyyy.app.Chat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.app.Chat.interfaces.ItemClickListener;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 12/18/2018.
 */

public class ParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<People> userArrayList;
    Context context;

    public ParticipantAdapter(Context context, ArrayList<People> userArrayList) {
        this.userArrayList = userArrayList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_participant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sholder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) sholder;

        Glide.with(context).load(userArrayList.get(position).getProfilePicture())
                .apply(new RequestOptions()
                        .error(R.drawable.profile_default)
                        .centerCrop())
                .into(viewHolder.ivUser);

        viewHolder.layout_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



    }

    @Override
    public int getItemCount() {
        return userArrayList == null ? 0 : userArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivUser;
        RelativeLayout layout_profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.ivUser);
            layout_profile_image = (RelativeLayout) itemView.findViewById(R.id.layout_profile_image);
        }
    }
}
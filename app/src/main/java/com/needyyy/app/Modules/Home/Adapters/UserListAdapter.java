package com.needyyy.app.Modules.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.needyyy.app.Modules.AddPost.models.AddPost.TaggedPerson;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Home.callback.CommentCallback;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.SingleFeedsViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    ArrayList<TaggedPerson> data = new ArrayList<>();
    private CommentCallback commentCallback ;
    private int type;
    public UserListAdapter(int type, Context context, ArrayList<TaggedPerson> data )
    {
        this.context = context;
        this.data    = data;
        this.type    = type ;
        this.commentCallback = commentCallback ;
    }


    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_friend_list_layout, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder,final int position) {
        TaggedPerson person = data.get(position);
        viewHolder.tvUserName.setText(person.getName());

        if(!TextUtils.isEmpty(person.getProfilePicture())){
            Glide.with(context)
                    .load(person.getProfilePicture())
                    .into(viewHolder.civProfile);
        }else{
            viewHolder.civProfile.setImageResource(R.drawable.needyy);
        }
        if(data.size()== position+1){
            viewHolder.footer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        CircleImageView civProfile ;
        TextView tvUserName;
        private View footer;


        public SingleFeedsViewHolder(View itemView) {
            super(itemView);

            civProfile       = itemView.findViewById(R.id.civ_profile_pic);
            tvUserName       = itemView.findViewById(R.id.tv_profile_name);
            footer           = itemView.findViewById(R.id.item_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)context).replaceFragment(ViewProfileFragment.newInstance(data.get(getAdapterPosition()).getId(),null), true);

                }
            });
        }
    }
}
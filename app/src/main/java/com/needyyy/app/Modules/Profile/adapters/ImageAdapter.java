package com.needyyy.app.Modules.Profile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.ViewFullImageFragment;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Profile.fragments.FriendsListFragment;
import com.needyyy.app.Modules.Profile.fragments.SeeAllPhotoFragment;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileOtherFragment;
import com.needyyy.app.Modules.Profile.models.Photo;
import com.needyyy.app.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Photo> data = new ArrayList<>();
    ArrayList<People> dataFriend = new ArrayList<>();
    String type="";
    public boolean isClickable = true;
    Fragment fragment;
    String memberid;
    int VIEWALL=2,IMAGE = 1;

    public ImageAdapter(Context context,ArrayList<Photo> data,String id) {
        this.context = context;
        this.data = data;
        this.type = "1";
        this.memberid=id;
//        this.fragment = fragment;
        Log.e("data", "EducationAdapter: " + data );
    }

    public ImageAdapter(Context context, ArrayList<People> data, Fragment fragment) {
        this.context = context;
        this.dataFriend = data;
        this.type = "2";
        this.fragment = fragment;
//        this.fragment = fragment;
        Log.e("data", "EducationAdapter: " + data );
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View v1 = inflater.inflate(R.layout.single_image_adapter, parent, false);
                viewHolder = new ImageViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.layout_view_all, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
        }
        return viewHolder;
    }

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image_adapter, parent, false);
//        return new ImageAdapter.ImageViewHolder(view);
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        switch (viewHolder.getItemViewType()) {
            case 2:
                ViewHolder2 viewAllHolder = (ViewHolder2) viewHolder;
                viewAllHolder.tvall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type.equals("1")) {
                            String id;
                            if (memberid.equals(AppController.getManager().getId())) {
                                id = "";
                            } else {
                                id = memberid;
                            }

                            ((HomeActivity) context).replaceFragment(SeeAllPhotoFragment.newInstance(id), true);
                       /* if(type.equals("2")){
                            ((HomeActivity) context).replaceFragment(FriendsListFragment.newInstance(2), true);
                        } else if(type.equals("1")){
                          //  ((HomeActivity) context).replaceFragment(FriendsListFragment.newInstance());
                        }*/
                        }
                        else if(type.equals("2")){
                                ((HomeActivity) context).replaceFragment(FriendsListFragment.newInstance(3), true);
                            }

                    }
                });
                break;
            case 1:
                ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;

                if(type.equals("2")){
                    Glide.with(context)
                            .load(dataFriend.get(position).getProfilePicture().toString())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.needyy)
                                    .error(R.drawable.needyy))
                            .into(imageViewHolder.ivImageAll);

                    imageViewHolder.tvFriendName.setVisibility(View.VISIBLE);
                    imageViewHolder.tvFriendName.setText(dataFriend.get(position).getName());

                }else {
                    Glide.with(context)
                            .load(data.get(position).getLink().toString())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.needyy)
                                    .error(R.drawable.needyy))
                            .into(imageViewHolder.ivImageAll);

                    imageViewHolder.tvFriendName.setVisibility(View.GONE);
                }

                imageViewHolder.ivImageAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("1")){
                            ((HomeActivity) context).replaceFragment(ZoomImage.newInstance(data.get(position).getLink().toString()), true);
                            //((HomeActivity) context).replaceFragment(ViewFullImageFragment.newInstance(PostResponse postResponse), true);
//                    ViewFullImageFragment viewFullImageFragment = ViewFullImageFragment.newInstance(data.get(position).getLink().toString());
                        } else if(type.equals("2")) {
//                    fragment = new ViewProfileFragment();
//                    ((HomeActivity) context).popStack();
                            String id;
                            if(dataFriend.get(position).getId().equals(AppController.getManager().getId())){
                                id = "";
                            } else
                                {
                                id = dataFriend.get(position).getId();
                            }
                            ((HomeActivity)context).replaceFragment(ViewProfileOtherFragment.newInstance(id), true);
//                    ((HomeActivity) context).replaceFragment(ViewProfileFragment.newInstance(dataFriend.get(position).getId().toString()));
//                    ViewProfileFragment viewProfileFragment = ViewProfileFragment.newInstance(dataFriend.get(position).getId());
                        }
                    }
                });
                imageViewHolder.llImage.setOnClickListener(new View.OnClickListener() {
                    //
                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;

                        // do your click stuff
//                FragmentManager fm = activity.getSupportFragmentManager();
                        if(type.equals("1")){
                            ((HomeActivity) context).replaceFragment(ZoomImage.newInstance(data.get(position).getLink().toString()), true);
                            //((HomeActivity) context).replaceFragment(ViewFullImageFragment.newInstance(PostResponse postResponse), true);
//                    ViewFullImageFragment viewFullImageFragment = ViewFullImageFragment.newInstance(data.get(position).getLink().toString());
                        } else if(type.equals("2")) {
//                    fragment = new ViewProfileFragment();
//                    ((HomeActivity) context).popStack();
                            String id;
                            if(dataFriend.get(position).getId().equals(AppController.getManager().getId())){
                                id = "";
                            } else{
                                id = dataFriend.get(position).getId();
                            }
                            ((HomeActivity)context).replaceFragment(ViewProfileOtherFragment.newInstance(id), true);
//                    ((HomeActivity) context).replaceFragment(ViewProfileFragment.newInstance(dataFriend.get(position).getId().toString()));
//                    ViewProfileFragment viewProfileFragment = ViewProfileFragment.newInstance(dataFriend.get(position).getId());
                        }
                    }
                });
                break;

        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(type.equals("2")){
            size = dataFriend.size();
            if (size > 6) {
                return size;
            } else {
                return size;
            }
        } else {
            size = data.size();
            if (size > 6) {
                return size;
            } else {
                return size;
            }
        }
//        return data.size();
//        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position<5) {
            return IMAGE;
        } else if (position==5) {
            return VIEWALL;
        }
        return -1;
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImageAll;
        TextView tvFriendName;
        LinearLayout llImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImageAll  = itemView.findViewById(R.id.iv_image_all);
            tvFriendName = itemView.findViewById(R.id.tv_friend_name);
            llImage     = itemView.findViewById(R.id.ll_image);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView tvall;

        public ViewHolder2(View v) {
            super(v);
            tvall = (TextView) v.findViewById(R.id.tv_viewall);
        }
        public TextView getImageView() {
            return tvall;
        }

        public void setImageView(TextView ivExample) {
            this.tvall = ivExample;
        }
    }}



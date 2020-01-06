package com.needyyy.app.Modules.Profile.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.needyyy.app.Modules.Dating.BlurTransformation;
import com.needyyy.app.Modules.Home.Activities.ExoPlayerActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Profile.fragments.GallerySlider;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileOtherFragment;
import com.needyyy.app.Modules.Profile.models.GetViewProfile;
import com.needyyy.app.Modules.Profile.models.UserPicture.Datum;
import com.needyyy.app.R;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SeeAllPhotoAdapter extends RecyclerView.Adapter<SeeAllPhotoAdapter.modelViewHolder> {
    private Context context;
    private ArrayList<Datum> getUserPicturesArrayList ;
    private Boolean checkphotovedio;

    public SeeAllPhotoAdapter(Context context,ArrayList<Datum> getUserPicturesArrayList) {
        this.context = context;
        this.getUserPicturesArrayList = getUserPicturesArrayList;
         this.checkphotovedio=false;
    }

    public SeeAllPhotoAdapter(Context context,ArrayList<Datum> getUserPicturesArrayList,String s) {
        this.context = context;
        this.getUserPicturesArrayList = getUserPicturesArrayList;
        this.checkphotovedio=true;

    }
    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.see_all_photos_item,viewGroup,false);
        return new modelViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull SeeAllPhotoAdapter.modelViewHolder modelViewHolder, int i) {
        if(checkphotovedio==false)
        {
            modelViewHolder.playicon.setVisibility(View.GONE);
        }
        else if(checkphotovedio==true)
        {
            modelViewHolder.playicon.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(getUserPicturesArrayList.get(i).getLink())
                .into(modelViewHolder.imageView);
        modelViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkphotovedio == false) {
                    ((HomeActivity) context).replaceFragment(GallerySlider.newInstance(getUserPicturesArrayList, i), true);
                }
                else if(checkphotovedio==true)
                {
                    Intent mIntent = ExoPlayerActivity.getStartIntent(context,getUserPicturesArrayList.get(i).getLink().toString() );
                    context.startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getUserPicturesArrayList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,playicon;
        public modelViewHolder(@NonNull View itemView) {

            super(itemView);
            playicon=itemView.findViewById(R.id.play);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }
}

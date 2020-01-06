package com.needyyy.app.Modules.AddPost.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.AddPost.models.addMedia.PostMedia;
import com.needyyy.app.R;
import com.needyyy.app.mypage.model.AddPagePost;
import com.needyyy.app.utils.Constant;

import java.util.ArrayList;

public class MediaAdapter  extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {



    Context context;
    private ArrayList<PostMedia> postMediaArrayList= new ArrayList<>();
    RelativeLayout rlContentTypes;
    private PostFragment postFragment ;
    private AddPagePost addPagePost;


    public MediaAdapter(Context context,
                        ArrayList<PostMedia> postMediaArrayList, PostFragment postFragment, AddPagePost addPagePost)
    {
        this.context            = context;
        this.postMediaArrayList = postMediaArrayList ;
        this.postFragment        = postFragment;
        this.addPagePost            = addPagePost;
    }


    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder viewHolder, final int position) {
//        viewHolder.imgMedia.setImageURI(postMediaArrayList.get(position).getLink());
        PostMedia postMedia= postMediaArrayList.get(position);
        if (postMedia.getFiletype().equals(Constant.VIDEO)){
            viewHolder.playIV.setVisibility(View.VISIBLE);
            Glide.with(context).load(postMedia.getFileUri())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(viewHolder.imgMedia);
        }else{
            viewHolder.playIV.setVisibility(View.GONE);
            Bitmap link =(postMediaArrayList.get(position).getLink()) ;
            viewHolder.imgMedia.setImageBitmap(link);
        }

//        if (!link.isEmpty()){
//            Glide.with(context).load(link)
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(viewHolder.imgMedia);
//
//        }else{
//
//        }
        viewHolder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postFragment != null) {
                    postFragment.deleteMediaFile(position);
                }
                else if(addPagePost != null) {
                    addPagePost.deleteMediaFile(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
//        return data.size();
        return postMediaArrayList.size();
    }



    public class MediaViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        private ImageView imgMedia ;
        ImageView ImageIV, deleteIV, playIV;
        public MediaViewHolder(View itemView) {
            super(itemView);

            imgMedia = itemView.findViewById(R.id.img_media);
            playIV = (ImageView) itemView.findViewById(R.id.playIV);
//            ImageIV = (ImageView) itemView.findViewById(R.id.imageIV);
            deleteIV = (ImageView) itemView.findViewById(R.id.deleteIV);
//            tvTitle = itemView.findViewById(R.id.tv_title);
//            tvDescription = itemView.findViewById(R.id.tv_description);
//            rlContentTypes = itemView.findViewById(R.id.rl_ContentTypes);
        }
    }

}

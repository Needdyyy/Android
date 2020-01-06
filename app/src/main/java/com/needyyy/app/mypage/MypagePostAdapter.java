package com.needyyy.app.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.Home.Activities.ExoPlayerActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.CommentFragment;
import com.needyyy.app.Modules.Home.Fragments.UserListFragment;
import com.needyyy.app.Modules.Home.bottomsfeetdialog.ShareNowBottomSheet;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.CreatePageFinalFragment;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.PageDataBase;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.vedio.Vedio;
import com.needyyy.app.webutils.WebInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter.convertno;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class MypagePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String pagecreaterid;
    public UserDataResult userData;
    private ArrayList<PostResponse> postResponseArrayList;
    public MypagePostAdapter(Context context, ArrayList<PostResponse> postResponseArrayList,String pagecreaterid) {
        this.context=context;
        this.postResponseArrayList=postResponseArrayList;
        this.pagecreaterid=pagecreaterid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout= LayoutInflater.from(context).inflate(R.layout.single_post_layout,null);
        return new MypagePostAdapter.Myviewholder(Layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        userData          = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        Myviewholder myviewholder= (Myviewholder) viewHolder;
        myviewholder.setdata(i,viewHolder);

    }

    @Override
    public int getItemCount() {
        return postResponseArrayList.size();
    }

    class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PostResponse postResponse ;
        CircleImageView imgProfile;
        LinearLayout llPostProfile,ll_feeds;
        RelativeLayout more;
        private ImageView imgLike,imgDislike,imgBoaring,imgComment,imgShare,imgBookmark,imgPost,playicon;
        TextView views,tvPageTitle, tvPageCreatedTime ,tvPageDescription,tvTotalLike,tvTotalComment,tvFollow,tv_totalboring, tv_totaldislike;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            views=itemView.findViewById(R.id.views);
            playicon=itemView.findViewById(R.id.playicon);
            tv_totalboring=itemView.findViewById(R.id.tv_totalboring);
            tv_totaldislike=itemView.findViewById(R.id.tv_totaldislike);
            ll_feeds=itemView.findViewById(R.id.ll_feeds);
            more=itemView.findViewById(R.id.more);
            imgProfile = itemView.findViewById(R.id.img_pageprofile);
            imgPost = itemView.findViewById(R.id.img_page_banner);
            tvPageTitle = itemView.findViewById(R.id.tv_page_title);
            tvPageCreatedTime = itemView.findViewById(R.id.tv_pagecreation_time);
            tvPageDescription = itemView.findViewById(R.id.tv_page_description);
            tvFollow = itemView.findViewById(R.id.tv_follow);
            tvTotalLike = itemView.findViewById(R.id.tv_totallike);
            tvTotalComment = itemView.findViewById(R.id.tv_totalcomment);
            imgLike = itemView.findViewById(R.id.img_like);
            imgDislike = itemView.findViewById(R.id.img_dislike);
            imgBoaring = itemView.findViewById(R.id.img_boaring);
            imgComment = itemView.findViewById(R.id.img_comment);
            imgShare = itemView.findViewById(R.id.img_share);
            imgBookmark = itemView.findViewById(R.id.img_bookmark);
            llPostProfile = itemView.findViewById(R.id.ll_post_profile);



            imgLike.setOnClickListener(this);
            imgDislike.setOnClickListener(this);
            imgBoaring.setOnClickListener(this);
            imgComment.setOnClickListener(this);
            imgShare.setOnClickListener(this);
            imgBookmark.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
            tvFollow.setOnClickListener(this);
            tvTotalLike.setOnClickListener(this);
            tvTotalComment.setOnClickListener(this);
            llPostProfile.setOnClickListener(this);
            tv_totalboring.setOnClickListener(this);
            tv_totaldislike.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_totaldislike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponseArrayList.get(getAdapterPosition()).getId(),"2"), true);
                    break;
                case R.id.tv_totalboring:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponseArrayList.get(getAdapterPosition()).getId(),"3"), true);
                    break;
                case R.id.img_pageprofile:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponseArrayList.get(getAdapterPosition()).getUserId(), null), true);
                    break;
                case R.id.tv_follow:
                    break;
                case R.id.tv_totallike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponseArrayList.get(getAdapterPosition()).getId(),"1"), true);
                    break;
                case R.id.tv_totalcomment:
                    break;
                case R.id.img_like:
                    if (!postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("1"))
                        likeDislikeApi(1, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_dislike:
                    if (!postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("2"))
                        likeDislikeApi(2, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_boaring:
                    if (!postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("3"))
                        likeDislikeApi(3, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_comment:
                    postResponse=postResponseArrayList.get(getAdapterPosition());
                    ((HomeActivity) context).replaceFragment(CommentFragment.newInstance(postResponse), true);
                    break;
                case R.id.img_share:
                    ShareNowBottomSheet fragment = new ShareNowBottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("postid", Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getId()));
                    fragment.setArguments(args);
                    fragment.show(((HomeActivity) context).getSupportFragmentManager(), "TAG");
                    break;
                case R.id.img_bookmark:
                    if (postResponseArrayList.get(getAdapterPosition()).getIsBookmark().equals("0")) {
                        likeDislikeApi(1, 2);
                    }
                    else {
                        likeDislikeApi(0, 2);
                    }
//
            }
        }

        public void setdata(int i, RecyclerView.ViewHolder viewHolder) {

            if(userData.getId().equals(pagecreaterid)){
                more.setVisibility(View.VISIBLE);
            }
            else
            {
                if(postResponseArrayList.get(i).getUserId().equals(userData.getId()))
                {
                    more.setVisibility(View.VISIBLE);
                }
                else
                {
                    more.setVisibility(View.GONE);
                }
            }
            if(postResponseArrayList.get(i).getPostMeta()!=null && postResponseArrayList.get(i).getPostMeta().size()!=0)
            {
                if(postResponseArrayList.get(getAdapterPosition()).getPostMeta().get(0).getFileType().equals("youtube")|| postResponseArrayList.get(getAdapterPosition()).getPostMeta().get(0).getFileType().equals("video")) {
                    playicon.setVisibility(View.VISIBLE);
                    views.setVisibility(View.VISIBLE);
                    views.setText(convertno(postResponseArrayList.get(i).getPostMeta().get(0).getViews()));
                }
                else
                {
                    views.setVisibility(View.GONE);
                    playicon.setVisibility(View.GONE);
                }
            }
            else
            {
                views.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
            }

            if (postResponseArrayList.get(i).getMyVote().equals("1")){
                imgLike.setImageResource(R.drawable.like_active);
                imgDislike.setImageResource(R.drawable.dislike);
                imgBoaring.setImageResource(R.drawable.boring);
            }else if(postResponseArrayList.get(i).getMyVote().equals("2")){
                imgLike.setImageResource(R.drawable.like);
                imgDislike.setImageResource(R.drawable.dislike_active);
                imgBoaring.setImageResource(R.drawable.boring);
            }else if(postResponseArrayList.get(i).getMyVote().equals("3")){
                imgLike.setImageResource(R.drawable.like);
                imgDislike.setImageResource(R.drawable.dislike);
                imgBoaring.setImageResource(R.drawable.boring_active);
            }else if(postResponseArrayList.get(i).getMyVote().equals("0")){
                imgLike.setImageResource(R.drawable.like);
                imgDislike.setImageResource(R.drawable.dislike);
                imgBoaring.setImageResource(R.drawable.boring);
            }


            //   spanna


            String tag = "";
            if (postResponseArrayList.get(i).getTaggedPeople() != null && postResponseArrayList.get(i).getTaggedPeople().size() != 0) {

                if (postResponseArrayList.get(i).getTaggedPeople().size() > 1) {
                    tag = " is with "  + postResponseArrayList.get(i).getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponseArrayList.get(i).getTaggedPeople().size() - 1) + " other");

                } else {
                    tag = " is with " + postResponseArrayList.get(i).getTaggedPeople().get(0).getName();
                }
                if (postResponseArrayList.get(i).getLocation() != null) {
                    if (postResponseArrayList.get(i).getLocation().equals("")) {
                    } else {
                        tag = tag + " at location " + postResponseArrayList.get(i).getLocation();
                    }
                }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

            } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                if(postResponseArrayList.get(i).getLocation()==null||postResponseArrayList.get(i).getLocation().equals(""))
                {

                }
                else
                {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                    tag= "at location "  + postResponseArrayList.get(i).getLocation();
                }

            }


            //   start   of    a    spanable string


            String text=postResponseArrayList.get(i).getUserName() + tag;

            SpannableString spannableString=new SpannableString(text);
            ClickableSpan clickableSpan=new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    if(context instanceof HomeActivity) {
                        ((HomeActivity) context).replaceFragment(ViewProfileFragment.newInstance(postResponseArrayList.get(i).getTaggedPeople().get(0).getId(), null), true);

                    }
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLACK);
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan2=new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponseArrayList.get(i).getTaggedPeople()), true);
                }

                @Override
                public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLACK);
                    ds.setUnderlineText(false);
                }
            };

            ClickableSpan clickableSpan3=new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    ((HomeActivity) context).replaceFragment(ViewProfileFragment.newInstance(postResponseArrayList.get(i).getUserId(),null), true);
                }

                @Override
                public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLACK);
                    ds.setUnderlineText(false);
                }
            };
            if (postResponseArrayList.get(i).getTaggedPeople() != null && postResponseArrayList.get(i).getTaggedPeople().size() != 0) {

                if(postResponseArrayList.get(i).getTaggedPeople().size()==1) {

                    spannableString.setSpan(clickableSpan3, 0,
                            postResponseArrayList.get(i).getUserName().length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(clickableSpan, postResponseArrayList.get(i).getUserName().length() + 8,
                            postResponseArrayList.get(i).getUserName().length() + 8 + postResponseArrayList.get(i).getTaggedPeople().get(0).getName().length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                else

                {
                    spannableString.setSpan(clickableSpan3, 0,
                            postResponseArrayList.get(i).getUserName().length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(clickableSpan, postResponseArrayList.get(i).getUserName().length() + 8,
                            postResponseArrayList.get(i).getUserName().length() + 8 + postResponseArrayList.get(i).getTaggedPeople().get(0).getName().length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(clickableSpan2, postResponseArrayList.get(i).getUserName().length()+8+postResponseArrayList.get(i).getTaggedPeople().get(0).getName().length()+4
                            , postResponseArrayList.get(i).getUserName().length()+8+postResponseArrayList.get(i).getTaggedPeople().get(0).getName().length()+4
                                    +postResponseArrayList.get(i).getTaggedPeople().size()+4
                            , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

            }

            else
            {
                spannableString.setSpan(clickableSpan3, 0,
                        postResponseArrayList.get(i).getUserName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

           tvPageTitle.setText(spannableString);
            tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




            //   end of a spanable string

//                           singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + tag)) ;




            //  ens

            //tvPageTitle.setText(postResponseArrayList.get(i).getUserName());

            if (!TextUtils.isEmpty(postResponseArrayList.get(i).getProfilePicture())) {
                // Bitmap bmp = decodeFile(profile);
                Glide.with(context)
                        .load(postResponseArrayList.get(i).getProfilePicture())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                        .into(imgProfile);
            } else
                {
                imgProfile.setImageResource(R.drawable.needyy);
                }

            if (postResponseArrayList.get(i).getPostMeta().size()!=0)
            {
                imgPost.setVisibility(View.VISIBLE);
                // Bitmap bmp = decodeFile(profile);
                Glide.with(context)
                        .load(postResponseArrayList.get(i).getPostMeta().get(0).getLink())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy).apply(new RequestOptions().override(1000, 500)))
                        .thumbnail(Glide.with(context).load(postResponseArrayList.get(i).getPostMeta().get(0).getLink()))
                        .into(imgPost);
            }
            else
                {
                imgPost.setVisibility(View.GONE);
                imgPost.setImageResource(R.drawable.needyy);
                }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopupWindow(v,i);
//

                }
            });

              imgPost.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (postResponseArrayList.get(i).getPostMeta().get(0).getFileType().equals("image")) {
                          ((HomeActivity) context).replaceFragment(ZoomImage.newInstance(postResponseArrayList.get(i).getPostMeta().get(0).getLink().toString()), true);
                      }
                      else if (postResponseArrayList.get(i).getPostMeta().get(0).getFileType().equals("youtube"))
                      {
                          Intent intent=new Intent(context, Vedio.class);
                          intent.putExtra("vedio_id",postResponseArrayList.get(i).getPostMeta().get(0).getFilelink());
                          context.startActivity(intent);
                          hitapi(postResponseArrayList.get(i).getPostMeta().get(0).getId());
                          postResponseArrayList.get(i).getPostMeta().get(0).setViews(String.valueOf(Integer.parseInt(postResponseArrayList.get(i).getPostMeta().get(0).getViews())+1));
                      }
                      else if(postResponseArrayList.get(i).getPostMeta().get(0).getFileType().equals("video"))
                      {
                          Intent mIntent = ExoPlayerActivity.getStartIntent(context,postResponseArrayList.get(i).getPostMeta().get(0).getLink() );
                          context.startActivity(mIntent);
                          hitapi(postResponseArrayList.get(i).getPostMeta().get(0).getId());
                          postResponseArrayList.get(i).getPostMeta().get(0).setViews(String.valueOf(Integer.parseInt(postResponseArrayList.get(i).getPostMeta().get(0).getViews())+1));
                      }
                  }
              });



            if (!TextUtils.isEmpty(postResponseArrayList.get(i).getText())) {
                // Bitmap bmp = decodeFile(profile);
                tvPageDescription.setVisibility(View.VISIBLE);
                tvPageDescription.setText(postResponseArrayList.get(i).getText());
            } else {
                tvPageDescription.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(postResponseArrayList.get(i).getCreated())) {
                // Bitmap bmp = decodeFile(profile);
                tvPageDescription.setVisibility(View.VISIBLE);
                tvPageDescription.setText(postResponseArrayList.get(i).getText());
            } else {
                tvPageDescription.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(postResponseArrayList.get(i).getCreated())) {
                tvPageCreatedTime.setVisibility(View.VISIBLE);
                tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponseArrayList.get(i).getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponseArrayList.get(i).getCreated()) * 1000));
            }
            else {
                tvPageCreatedTime.setVisibility(View.GONE);
            }

////.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000));
            if (Integer.parseInt(postResponseArrayList.get(i).getLikes()) <= 1) {
                tvTotalLike.setText(convertno(postResponseArrayList.get(i).getLikes()) + " " + context.getResources().getString(R.string.like));

            } else {
                tvTotalLike.setText(convertno(postResponseArrayList.get(i).getLikes()) + " " + context.getResources().getString(R.string.likes));

            }

            if (Integer.parseInt(postResponseArrayList.get(0).getDislikes()) <= 1) {
                tv_totaldislike.setText(convertno(postResponseArrayList.get(i).getDislikes()) + " " + "Intrested");
            } else {
                tv_totaldislike.setText(convertno(postResponseArrayList.get(i).getDislikes()) + " " + "Intrested");
            }
            if (Integer.parseInt(postResponseArrayList.get(i).getBorings()) <= 1) {
                tv_totalboring.setText(convertno(postResponseArrayList.get(i).getBorings()) + " " + "Boring");

            } else {
                tv_totalboring.setText(convertno(postResponseArrayList.get(i).getBorings()) + " " + "Borings");
            }

            if (Integer.parseInt(postResponseArrayList.get(i).getComments()) <= 1) {
                tvTotalComment.setText(convertno(postResponseArrayList.get(i).getComments()) + " " + context.getResources().getString(R.string.comment));
            } else {
                tvTotalComment.setText(convertno(postResponseArrayList.get(i).getComments()) + " " + context.getResources().getString(R.string.comments));
            }
        }


        private void likeDislikeApi(int status,int taskFor) {
            //   ((HomeActivity)context).showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.likeDislike(postResponseArrayList.get(getAdapterPosition()).getId(),status,postResponseArrayList.get(getAdapterPosition()).getPostType(),taskFor);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    //((HomeActivity)context).cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo myPage = response.body();
                    if (myPage.getStatus()) {

                        if (taskFor==1){
                            if (status==1){
                                postResponseArrayList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getLikes()) +1));
                                imgLike.setImageResource(R.drawable.like_active);
                                if(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getBorings())>0)
                                {
                                    postResponseArrayList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getBorings())-1));
                                }
                                if(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getDislikes())>0)
                                {
                                    postResponseArrayList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getDislikes())-1));
                                }
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            else if(status==2){
                                if ( postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponseArrayList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getLikes()) -1));
                                }
                                if(postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponseArrayList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getBorings())-1));
                                }

                                postResponseArrayList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getDislikes())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike_active);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }else if(status==3){
                                if ( postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponseArrayList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getLikes()) -1));
                                }
                                postResponseArrayList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getBorings())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring_active);
                                if ( postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponseArrayList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponseArrayList.get(getAdapterPosition()).getDislikes())-1));
                                }

                            }
                            else if(status==0){
                                if ( postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponseArrayList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                if(postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponseArrayList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1));
                                }
                                if(postResponseArrayList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponseArrayList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1));
                                }
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            postResponseArrayList.get(getAdapterPosition()).setMyVote(String.valueOf(status));
                            postResponseArrayList.get(getAdapterPosition()).setMyVote(String.valueOf(status));

                        }else{
                            if (status==1){
                                imgBookmark.setImageResource(R.drawable.bookmark);
                                postResponseArrayList.get(getAdapterPosition()).setIsBookmark("1");
                            }else if (status==0){
                                postResponseArrayList.get(getAdapterPosition()).setIsBookmark("0");
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        if (myPage.getMessage().equals("110110")){
                            ((HomeActivity)context).logout();

                        }else{
                            ((HomeActivity)context).snackBar(myPage.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonPojo> call, Throwable t) {
                    //((HomeActivity)context).cancelProgressDialog();
                    ((HomeActivity)context).snackBar(t.getMessage());
                }
            });
        }

    }
    private void hitapi(String id) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.VideoCount(id);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo commonPojo = response.body();
                if (commonPojo.getStatus()) {

                    // Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showPopupWindow(View view, int i) {
        PopupMenu popup = new PopupMenu(context, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.pagepost, popup.getMenu());
        if (!(postResponseArrayList.get(i).getBoostid()!=null && postResponseArrayList.get(i).getBoostid().equals("0")))
            popup.getMenu().findItem(R.id.Boost).setVisible(false);
        else
            popup.getMenu().findItem(R.id.Boost).setVisible(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Delete")) {
                    deletepost(postResponseArrayList.get(i).getId(),i);

                }
                else if(item.getTitle().equals("Boost")) {
                    PostResponse postResponse1=postResponseArrayList.get(i);
                    postdetails(postResponse1);
                }

                return true;
            }
        });
        popup.show();
    }

    private void deletepost(String id, int i) {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.deletePost(id);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                //((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo myPage = response.body();
                if (myPage.getStatus()) {

                    Toast.makeText(context,myPage.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    postResponseArrayList.remove(i);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                //((HomeActivity)context).cancelProgressDialog();
                ((HomeActivity)context).snackBar(t.getMessage());
            }
        });
    }
    private void postdetails(PostResponse postResponse1) {
        ((HomeActivity)context).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CreatePageModel> call = Service.getPostdetails(postResponse1.getId(),1);
        call.enqueue(new Callback<CreatePageModel>() {
            @Override
            public void onResponse(Call<CreatePageModel> call, Response<CreatePageModel> response) {
                ((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CreatePageModel createPageModel = response.body();
                if (createPageModel.getStatus()) {
                    PageDataBase pageData = createPageModel.getData();
                    ((HomeActivity) context).replaceFragment(CreatePageFinalFragment.newInstance(pageData,false), true);
                } else {
                    if (createPageModel.getMessage().equals("110110")){
                        ((HomeActivity)context).logout();

                    }else{
                        ((HomeActivity)context).snackBar(createPageModel.getMessage());
                    }
                    ((HomeActivity)context).snackBar(createPageModel.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CreatePageModel> call, Throwable t) {
                ((HomeActivity)context).cancelProgressDialog();
                ((HomeActivity)context).snackBar(t.getMessage());
            }
        });
    }

}


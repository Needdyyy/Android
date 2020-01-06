package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.CommentAdapter;
import com.needyyy.app.Modules.Home.bottomsfeetdialog.ShareNowBottomSheet;
import com.needyyy.app.Modules.Home.callback.CommentCallback;
import com.needyyy.app.Modules.Home.modle.CommentBase;
import com.needyyy.app.Modules.Home.modle.CommentData;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter.convertno;

public class ViewFullImageFragment extends BaseFragment implements View.OnClickListener,CommentCallback{
    private com.needyyy.app.ImageClasses.WrapContentViewPager ivImageFullView;
    private RecyclerView commentrec;
    public  PostResponse postresponse=null;
    private de.hdodenhof.circleimageview.CircleImageView img_pageprofile;
    private TextView tv_page_title;
    private LinearLayoutManager linearLayoutManager ;
    private TextView tv_totallike;
    private ArrayList<CommentData> commentDataList ;
    private CommentCallback commentCallback ;
    private String postId ,commentId = "0";
    private CommentAdapter commentAdapter ;
    private TextView tv_totalcomment;
    private TextView tv_pagecreation_time;
    private TextView tag_people;
    private TextView tv_page_description,tv_totaldislike,tv_totalboring,view;
    private ImageView playicon,imglike,imgdislike,imgcomment,imgboaring;
    private WebView webView;
    // ZoomageView ivImageFullView;
    String picUrl;
    private int NUM_PAGES = 0;
    private int savedpage;
    public ViewFullImageFragment() {

    }


    public static ViewFullImageFragment newInstance(PostResponse postresponse) {
        ViewFullImageFragment fragment = new ViewFullImageFragment();
        Bundle args = new Bundle();
        args.putSerializable("postresponse", postresponse);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_view_full_image);
    }

    @Override
    protected void initView(View mView) {
        if (getArguments() != null) {
            postresponse = (PostResponse)getArguments().getSerializable("postresponse");
        }
        webView=mView.findViewById(R.id.webView);
        view=mView.findViewById(R.id.views);
        imgboaring=mView.findViewById(R.id.img_boaring);
        imgcomment=mView.findViewById(R.id.img_comment);
        imgdislike=mView.findViewById(R.id.img_dislike);
        imglike=mView.findViewById(R.id.img_like);
        playicon=mView.findViewById(R.id.playicon);
        //ivImageFullView = mView.findViewById(R.id.iv_full_view);
        tv_totaldislike=mView.findViewById(R.id.tv_totaldislike);
        tv_totalboring=mView.findViewById(R.id.tv_totalboring);
        ivImageFullView=mView.findViewById(R.id.iv_full_view);
        img_pageprofile=mView.findViewById(R.id.img_pageprofile);
        tv_page_title=mView.findViewById(R.id.tv_page_title);
        tv_pagecreation_time=mView.findViewById(R.id.tv_pagecreation_time);
        tv_totallike=mView.findViewById(R.id.tv_totallike);
        tv_totalcomment=mView.findViewById(R.id.tv_totalcomment);
        commentrec=mView.findViewById(R.id.commentrec);
        tv_page_description=mView.findViewById(R.id.tv_page_description);
        commentDataList=new ArrayList<>();

        if (postresponse.getMyVote().equals("1")) {
            imglike.setImageResource(R.drawable.like_active);
            imgdislike.setImageResource(R.drawable.dislike);
            imgboaring.setImageResource(R.drawable.boring);
        } else if (postresponse.getMyVote().equals("2")) {
            imglike.setImageResource(R.drawable.like);
            imgdislike.setImageResource(R.drawable.dislike_active);
            imgboaring.setImageResource(R.drawable.boring);
        } else if (postresponse.getMyVote().equals("3")) {
            imglike.setImageResource(R.drawable.like);
            imgdislike.setImageResource(R.drawable.dislike);
            imgboaring.setImageResource(R.drawable.boring_active);
        } else if (postresponse.getMyVote().equals("0")) {
            imglike.setImageResource(R.drawable.like);
            imgdislike.setImageResource(R.drawable.dislike);
            imgboaring.setImageResource(R.drawable.boring);
        }
        imglike.setOnClickListener(this);
        imgdislike.setOnClickListener(this);
        imgboaring.setOnClickListener(this);
        imgcomment.setOnClickListener(this);
        tv_totallike.setOnClickListener(this);
        tv_totaldislike.setOnClickListener(this);
        tv_totalboring.setOnClickListener(this);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        tv_page_title.setText(postresponse.getUserName());
        if(postresponse.getPostMeta()!=null && postresponse.getPostMeta().size()!=0)
        {
            if(postresponse.getPostMeta().get(0).getFileType().equals("youtube")) {
                playicon.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                view.setText(convertno(postresponse.getPostMeta().get(0).getViews()));
            }
            else if(postresponse.getPostMeta().get(0).getFileType().equals("video"))
            {
                playicon.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                view.setText(convertno(postresponse.getPostMeta().get(0).getViews()));
            }
            else
            {
                view.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
            }
        }
        else
        {
            view.setVisibility(View.GONE);
            playicon.setVisibility(View.GONE);
        }

        if(Integer.parseInt(postresponse.getLikes())<2) {
            tv_totallike.setText(convertno(postresponse.getLikes())+ " Like");
        }
        else
        {
            tv_totallike.setText(convertno(postresponse.getLikes()) + " Likes");
        }
        if(Integer.parseInt(postresponse.getComments())<2) {
            tv_totalcomment.setText(convertno(postresponse.getComments()) + " Comment");
        }
        else
        {
            tv_totalcomment.setText(convertno(postresponse.getComments()) + " Comments");
        }
        if(Integer.parseInt(postresponse.getDislikes())<2) {
            tv_totaldislike.setText(convertno(postresponse.getDislikes()) + " Interesting");
        }
        else
        {
            tv_totaldislike.setText(convertno(postresponse.getDislikes()) + " Interesting");
        }
        if(Integer.parseInt(postresponse.getBorings())<2) {
            tv_totalboring.setText(convertno(postresponse.getBorings()) + " Boring");
        }
        else
        {
            tv_totalboring.setText(convertno(postresponse.getBorings()) + " Borings");
        }
        if(postresponse.getDescription()!=null) {
            tv_page_description.setText(postresponse.getDescription());
        }
        else
        {
            tv_page_description.setText("");
        }
        if(postresponse.getText()!=null)
        {
            tv_pagecreation_time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postresponse.getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postresponse.getCreated()) * 1000));

            //tv_pagecreation_time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postresponse.getCreated())));
        }
        else
        {
            tv_pagecreation_time.setText("");
        }
        ((HomeActivity)getActivity()).manageToolbar("Post Detail","2");
        Glide.with(getContext())
                .load(postresponse.getProfilePicture().toString())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                .into(img_pageprofile);
        init();
        getCommentApi();
        commentAdapter = new CommentAdapter(1,getActivity(),commentDataList ,this,postresponse.getUserId(),null);
        linearLayoutManager = new LinearLayoutManager(getContext());
        commentrec.setLayoutManager(linearLayoutManager);
        commentrec.setAdapter(commentAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_totaldislike:
                if(getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).replaceFragment(UserListFragment.newInstance(postresponse.getId(), "2"), true);
                }
                break;
            case R.id.tv_totalboring:
                ((HomeActivity) getActivity()).replaceFragment(UserListFragment.newInstance(postresponse.getId(),"3"), true);
                break;
            case R.id.tv_totallike:
                ((HomeActivity) getActivity()).replaceFragment(UserListFragment.newInstance(postresponse.getId(),"1"), true);
                break;
            case R.id.img_like:
                if (!postresponse.getMyVote().equals("1"))
                    likeDislikeApi(1, 1);
                else {
                    likeDislikeApi(0, 1);
                }
                break;
            case R.id.img_dislike:
                if (!postresponse.getMyVote().equals("2"))
                    likeDislikeApi(2, 1);
                else {
                    likeDislikeApi(0, 1);
                }
                break;
            case R.id.img_boaring:
                if (!postresponse.getMyVote().equals("3"))
                    likeDislikeApi(3, 1);
                else {
                    likeDislikeApi(0, 1);
                }
                break;
            case R.id.img_comment:
                ((HomeActivity) getContext()).replaceFragment(CommentFragment.newInstance(postresponse), true);
                break;
            case R.id.img_share:
                ShareNowBottomSheet fragment = new ShareNowBottomSheet();
                Bundle args = new Bundle();
                args.putInt("postid", Integer.parseInt(postresponse.getId()));
                fragment.setArguments(args);
                fragment.show(((HomeActivity) getContext()).getSupportFragmentManager(), "TAG");
                break;
        }
    }
    private void init() {

//        if (product.size() < 1) {
//            dat = SharedPreference.getInstance().getmasterhit(activity);
//            banner = dat.getBanners();
//        }
        ivImageFullView.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        ivImageFullView.setPadding(0, 0, 0, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        ivImageFullView.setPageMargin(20);
        ivImageFullView.setAdapter(new imageslider(getContext(), postresponse));
        ivImageFullView.setCurrentItem(savedpage);
        NUM_PAGES = postresponse.getPostMeta().size();

        ivImageFullView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int i, float v, int i1) {


            }
            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }



        });

    }
    private void getCommentApi() {
        if (CommonUtil.isConnectingToInternet(getActivity())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommentBase> call = Service.getComment(postresponse.getId(),commentId,"20","1");
            call.enqueue(new Callback<CommentBase>() {
                @Override
                public void onResponse(Call<CommentBase> call, Response<CommentBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommentBase commentBase = response.body();
                    if (commentBase.getStatus()) {
                        if (commentDataList!= null && commentDataList.size()!=0){
                            commentDataList.clear();
                        }
                        commentDataList.addAll(commentBase.getData());
                        commentAdapter.notifyDataSetChanged();
                    } else {
                        if (commentBase.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();

                        }else{
                            snackBar(commentBase.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentBase> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }
        else{

        }
    }

    @Override
    public void postComments(String Comment, String cpmmentId) {
        ((HomeActivity)getActivity()).replaceFragment(CommentReplyFragment.newInstance(postresponse.getPostMeta().get(0).getPostId(),cpmmentId,postresponse.getUserId()), true);
    }
    private void likeDislikeApi(int status,int taskFor) {
        //   ((HomeActivity)context).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.likeDislike(postresponse.getId(),status,postresponse.getPostType(),taskFor);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                //((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo myPage = response.body();
                if (myPage.getStatus()) {
                    if (taskFor==1){
                        if (status==1){
                            postresponse.setLikes(String.valueOf(Integer.parseInt(convertno(postresponse.getLikes()))+1));
                            //postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) +1));
                            imglike.setImageResource(R.drawable.like_active);
                            if(Integer.parseInt(postresponse.getBorings())>0)
                            {
                                postresponse.setBorings(String.valueOf(Integer.parseInt(convertno(postresponse.getBorings()))-1));
                                // postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())-1));
                            }
                            if(Integer.parseInt(postresponse.getDislikes())>0)
                            {
                                postresponse.setDislikes(String.valueOf(Integer.parseInt(convertno(postresponse.getDislikes()))-1));
                                //postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                            }
                            imgdislike.setImageResource(R.drawable.dislike);
                            imgboaring.setImageResource(R.drawable.boring);

                        }
                        else if(status==2){
                            if ( postresponse.getMyVote().equals("1")){
                                postresponse.setLikes(String.valueOf(Integer.parseInt(convertno(postresponse.getLikes()))-1));
                                // postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                            }
                            if(postresponse.getMyVote().equals("3"))
                            {
                                postresponse.setBorings(String.valueOf(Integer.parseInt(convertno(postresponse.getBorings()))-1));
                            }
                            postresponse.setDislikes(String.valueOf(Integer.parseInt(convertno(postresponse.getDislikes()))+1));
                            // postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1));
                            imglike.setImageResource(R.drawable.like);
                            imgdislike.setImageResource(R.drawable.dislike_active);
                            imgboaring.setImageResource(R.drawable.boring);
                        }else if(status==3){
                            if ( postresponse.getMyVote().equals("1")){
                                postresponse.setLikes(String.valueOf(Integer.parseInt(convertno(postresponse.getLikes()))-1));
                                //  postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                            }
                            postresponse.setBorings(String.valueOf(Integer.parseInt(convertno(postresponse.getBorings()))+1));
                            // postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())+1));
                            imglike.setImageResource(R.drawable.like);
                            imgdislike.setImageResource(R.drawable.dislike);
                            imgboaring.setImageResource(R.drawable.boring_active);
                            if (postresponse.getMyVote().equals("2")) {
                                postresponse.setDislikes(String.valueOf(Integer.parseInt(convertno(postresponse.getDislikes()))-1));
                                //postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                            }

                        }
                        else if(status==0){
                            if ( postresponse.getMyVote().equals("1")){
                                postresponse.setLikes(String.valueOf(Integer.parseInt(convertno(postresponse.getLikes()))-1));
                                //   postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                            }
                            if(postresponse.getMyVote().equals("2"))
                            {
                                postresponse.setDislikes(String.valueOf(Integer.parseInt(convertno(postresponse.getDislikes()))-1));
                                //  postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1));
                            }
                            if(postresponse.getMyVote().equals("3"))
                            {
                                //postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1));
                                postresponse.setBorings(String.valueOf(Integer.parseInt(convertno(postresponse.getBorings()))-1));
                            }
                            imglike.setImageResource(R.drawable.like);
                            imgdislike.setImageResource(R.drawable.dislike);
                            imgboaring.setImageResource(R.drawable.boring);
                        }
                        postresponse.setMyVote(String.valueOf(status));
                        postresponse.setMyVote(String.valueOf(status));

                        if(Integer.parseInt(postresponse.getLikes())<2) {
                            tv_totallike.setText(convertno(postresponse.getLikes()) + " Like");
                        }
                        else
                        {
                            tv_totallike.setText(convertno(postresponse.getLikes()) + " Likes");
                        }
                        if(Integer.parseInt(postresponse.getComments())<2) {
                            tv_totalcomment.setText(convertno(postresponse.getComments()) + " Comment");
                        }
                        else
                        {
                            tv_totalcomment.setText(convertno(postresponse.getComments()) + " Comments");
                        }
                        if(Integer.parseInt(postresponse.getDislikes())<2) {
                            tv_totaldislike.setText(convertno(postresponse.getDislikes()) + " Interesting");
                        }
                        else
                        {
                            tv_totaldislike.setText(convertno(postresponse.getDislikes()) + " Interesting");
                        }
                        if(Integer.parseInt(postresponse.getBorings())<2) {
                            tv_totalboring.setText(convertno(postresponse.getBorings()) + " Boring");
                        }
                        else
                        {
                            tv_totalboring.setText(convertno(postresponse.getBorings()) + " Borings");
                        }
                        if(postresponse.getDescription()!=null) {
                            tv_page_description.setText(postresponse.getDescription());
                        }
                        else
                        {
                            tv_page_description.setText("");
                        }
                        if(postresponse.getText()!=null)
                        {
                            tv_pagecreation_time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postresponse.getCreated())));
                        }
                        else
                        {
                            tv_pagecreation_time.setText("");
                        }




                    }else{
                        if (status==1){
                            //imgbookmark.setImageResource(R.drawable.bookmark);
                            postresponse.setIsBookmark("1");
                        }else if (status==0){
                            postresponse.setIsBookmark("0");
                        }
                    }

                } else {
                    if (myPage.getMessage().equals("110110")){
                        ((HomeActivity)getContext()).logout();
                    }else{
                        ((HomeActivity)getContext()).snackBar(myPage.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                //((HomeActivity)context).cancelProgressDialog();
                ((HomeActivity)getContext()).snackBar(t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            postresponse = (PostResponse)getArguments().getSerializable("postresponse");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

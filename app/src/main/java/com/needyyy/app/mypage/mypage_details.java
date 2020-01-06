package com.needyyy.app.mypage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Modules.Home.Fragments.CommentFragment;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.chat_ImageAdapter;
import com.needyyy.app.Modules.Profile.models.Photo;
import com.needyyy.app.Modules.adsAndPage.modle.PageData;
import com.needyyy.app.R;
import com.bumptech.glide.Glide;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.AddPagePost;
import com.needyyy.app.mypage.model.pagedata.Data;
import com.needyyy.app.mypage.model.pagedata.GetPageData;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.AppController.getContext;
import static com.needyyy.app.constants.Constants.kCurrentUser;


public class mypage_details extends BaseFragment implements View.OnClickListener{
    public PageData pageData;
    private CircleImageView circleImageView;
    private ImageView banner;
    private Data data;
    private String nooflikes;
    ArrayList<PostResponse> postResponseArrayList = new ArrayList<>();
    ArrayList<GetPageData> postResponseArrayList2 = new ArrayList<>();
    private int mPage=1;
    public UserDataResult userData;
    private LinearLayoutManager Lm;
    private TextView et_comment,address,website,contact;
    private RecyclerView my_page_recycle;
    private boolean loading = false;
    private int pageSize;
    private Button invite;
    private ArrayList<Photo> arrPhotos = new ArrayList<>();
    private ArrayList<Photo> arrvideo = new ArrayList<>();
    private TextView pagetitle;
    private Button follow;
    private Button view_message;
    private String id, page_name, page_creator_id;
    private TextView tvpagecreationtime;
    private Button message;
    TextView likes,comment,followers;
    TabLayout tabLayout;
    int noOfColumns;
    ViewPager viewPager;
    chat_ImageAdapter chatImageAdapter;
    RecyclerView gridPhoto,gridVideo;
    private MypagePostAdapter mypagePostAdapter;
    private ImageView img_like,img_dislike,img_comment,img_bookmark;


    public static mypage_details newInstance(String receivedData) {
        mypage_details fragment = new mypage_details();
        Bundle args = new Bundle();
        args.putString("receivedData", receivedData);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static mypage_details newInstance(String page_id, String page_name, String page_creator_id) {
        mypage_details fragment = new mypage_details();
        Bundle args = new Bundle();
        args.putString("receivedData", page_id);
        args.putString("page_name", page_name);
        args.putString("page_creator_id", page_creator_id);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//            mParam2 = getArguments().getString(ARG_PARAM2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof HomeActivity)
        {
            ((HomeActivity) getActivity()).manageToolbar("Page Details","2");
        }
        return inflater.inflate(R.layout.fragment_mypage_details, container, false);
    }

    @Override
    protected void initView(View mView) {

    }


    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData          = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        if (getArguments() != null) {
            id =  getArguments().getString("receivedData");
            page_name =  getArguments().getString("page_name");
            page_creator_id =  getArguments().getString("page_creator_id");
        }
        gridPhoto=view.findViewById(R.id.rv_pagephotos);
        gridVideo=view.findViewById(R.id.rv_pageVideo);
        pagetitle=view.findViewById(R.id.tv_page_title);
        img_like=view.findViewById(R.id.img_like);
        img_dislike=view.findViewById(R.id.img_dislike);
        img_bookmark=view.findViewById(R.id.img_bookmark);
        followers=view.findViewById(R.id.followtv);
        likes=view.findViewById(R.id.like);
        comment=view.findViewById(R.id.comment);
        message=view.findViewById(R.id.message);
        address = view.findViewById(R.id.address);
        website=view.findViewById(R.id.website);
        contact=view.findViewById(R.id.contact);
        tvpagecreationtime=view.findViewById(R.id.tv_pagecreation_time);
        et_comment=view.findViewById(R.id.et_comment);
        banner=view.findViewById(R.id.banner);
        circleImageView=view.findViewById(R.id.img_pageprofile);
        invite=view.findViewById(R.id.invite);
        follow=view.findViewById(R.id.follow);
        noOfColumns = calculateNoOfColumns(getActivity(), 120);
        my_page_recycle=view.findViewById(R.id.my_page_recycle);
        invite.setOnClickListener(this);
        et_comment.setOnClickListener(this);
        getPageData();
        getPost(false);
        mypagePostAdapter = new MypagePostAdapter(getActivity(), postResponseArrayList,page_creator_id);
        my_page_recycle.setHasFixedSize(true);
        Lm = new LinearLayoutManager(getContext());
        my_page_recycle.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(my_page_recycle, false);
        my_page_recycle.setLayoutManager(Lm);
        my_page_recycle.setAdapter(mypagePostAdapter);
        follow.setOnClickListener(this);
        message.setOnClickListener(this);
        img_like.setOnClickListener(this);
        img_dislike.setOnClickListener(this);
        img_bookmark.setOnClickListener(this);

        if(userData.getId().equals(page_creator_id)){
            message.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.invite:
                ((HomeActivity)getContext()).replaceFragment(mypage_friendlist.newInstance(1,id), true);
                break;
            case R.id.et_comment:
                ((HomeActivity) getActivity()).replaceFragment(AddPagePost.newInstance(id), true);
                break;
            case R.id.follow:
                if(data.getIsFollow().equals("1"))
                {
                    likeDislikeApi(0);
                }
                else
                {
                    likeDislikeApi(1);
                }

                break;

            case R.id.message:

                if(data.getIsChatIniatialised().equals("0"))
                {
                    addMessageDialog();


                }
                else {
                    //   String room_id = id.compareTo(userData.getId()) > 0 ? (userData.getId() + id).hashCode() + "" : "" + (id + userData.getId()).hashCode();

                /*Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                intent.putExtra(Constant.INTENT_KEY_CHAT_FRIEND, ""+page_name);
                ArrayList<CharSequence> idFriend = new ArrayList<>();
                GroupChatActivity.bitmapAvataFriend = new HashMap<>();
                idFriend.add(id);
                intent.putCharSequenceArrayListExtra(Constant.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(Constant.INTENT_KEY_CHAT_ROOM_ID, room_id);
                getActivity().startActivity(intent);*/

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("page_id", id);
                    intent.putExtra("user_id",userData.getId());
                    intent.putExtra("user_name", "" + page_name);
                    intent.putExtra("type", "page");
                    getActivity().startActivity(intent);

                }
                break;
            case R.id.img_like:
                if (!data.getMyVote().equals("1")) {
                    likeDislikeApi(1, 1);
                }
                else{
                    likeDislikeApi(0,1);
                }
                nooflikes=String.valueOf(Integer.parseInt(nooflikes)+1);
                likes.setText("Likes "+nooflikes);
                img_like.setImageResource(R.drawable.like_active);
                img_dislike.setImageResource(R.drawable.dislike);
                break;
            case R.id.img_dislike:
                if (! data.getMyVote().equals("2"))
                    likeDislikeApi(2,1);
                else{
                    likeDislikeApi(0,1);
                }
                nooflikes=String.valueOf(Integer.parseInt(nooflikes)-1);
                likes.setText("Likes "+nooflikes);
                //  postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1));
                img_like.setImageResource(R.drawable.like);
                img_dislike.setImageResource(R.drawable.dislike_active);
                //imgBoaring.setImageResource(R.drawable.boring);
                break;
            case R.id.img_bookmark:
                if (data.getIsBookmark().equals("0")){
                    likeDislikeApi(1,2);
                }else{
                    likeDislikeApi(0,2);
                }
                break;

        }
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


    private void getPost(boolean status) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<PostDataBase> call = Service.getMyPageMyPost(mPage,10,id);
        call.enqueue(new Callback<PostDataBase>() {
            @Override
            public void onResponse(Call<PostDataBase> call, Response<PostDataBase> response) {

                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                PostDataBase postDataBase = response.body();
                if (postDataBase.getStatus()) {
                    if (status){
                        postResponseArrayList.addAll(postDataBase.getData());
                        loading = !(postDataBase.getData().size()==0);
                    }else{
                        initialState();
                        if (postResponseArrayList!=null && postResponseArrayList.size()!=0){
                            postResponseArrayList.clear();
                        }
                        postResponseArrayList.addAll(postDataBase.getData());
                    }
                    mypagePostAdapter.notifyDataSetChanged();
                } else {
                    if (postDataBase.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();
                    }else{
                        snackBar(postDataBase.getMessage());
                    }
                }

            }
            @Override
            public void onFailure(Call<PostDataBase> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    private void getPageData() {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetPageData> call = Service.getPageDetails(id);
        call.enqueue(new Callback<GetPageData>() {
            @Override
            public void onResponse(Call<GetPageData> call, Response<GetPageData> response) {


                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetPageData getPageData = response.body();
                if (getPageData.getStatus()) {
                    if (getPageData.getStatus()) {
                        loading = !(getPageData.getData()==null);
                        data=getPageData.getData();

                        if (data.getMyVote().equals("1")) {
                            img_like.setImageResource(R.drawable.like_active);
                            img_dislike.setImageResource(R.drawable.dislike);
                            //pagepost.imgBoaring.setImageResource(R.drawable.boring);
                        } else if (data.getMyVote().equals("2")) {
                            img_like.setImageResource(R.drawable.like);
                            img_dislike.setImageResource(R.drawable.dislike_active);
                           // pagepost.imgBoaring.setImageResource(R.drawable.boring);
                        } else if (data.getMyVote().equals("3")) {
                            img_like.setImageResource(R.drawable.like);
                            img_dislike.setImageResource(R.drawable.dislike);
                           // pagepost.imgBoaring.setImageResource(R.drawable.boring_active);
                        } else if (data.getMyVote().equals("0")) {
                            img_like.setImageResource(R.drawable.like);
                            img_dislike.setImageResource(R.drawable.dislike);
                            //pagepost.imgBoaring.setImageResource(R.drawable.boring);
                        }

                        if(data.getIsBookmark().equals("0"))
                        {
                            img_bookmark.setImageResource(R.drawable.bookmark_side);
                        }
                        else if(data.getIsBookmark().equals("1"))
                        {
                            img_bookmark.setImageResource(R.drawable.bookmark);
                        }


                        if (data.getLikes() != null) {
                            nooflikes=data.getLikes();
                            likes.setText("Likes" +":"+ data.getLikes());
                        } else
                        {
                            likes.setVisibility(View.GONE);
                        }
                        if (data.getFollowers()!=null)
                        {
                            followers.setText("Followers"+":"+data.getFollowers());
                        }
                        else
                        {
                            followers.setVisibility(View.GONE);
                        }
                        if (data.getComments()!=null)
                        {
                            comment.setText("Comments"+":"+data.getComments());
                        }
                        else
                        {
                            comment.setVisibility(View.GONE);
                        }
                        if(data.getWebsite()!=null)
                        {
                            website.setVisibility(View.VISIBLE);
                            website.setText("Website : "+data.getWebsite());
                        }
                        else
                        {
                            website.setVisibility(View.GONE);
                        }
                        if(data.getAddress()!=null)
                        {
                            address.setVisibility(View.VISIBLE);
                            address.setText("Address : "+data.getAddress());
                        }
                        else
                        {
                            address.setVisibility(View.GONE);
                        }
                        if(data.getContact()!=null)
                        {
                            contact.setVisibility(View.VISIBLE);
                            contact.setText("Contact no :"+data.getContact());
                        }
                        else
                        {
                            contact.setVisibility(View.GONE);
                        }
                        if(data.getIsFollow().equals("1"))
                        {
                            follow.setText("Unfollow");
                        }
                        else
                        {
                            follow.setText("Follow");
                        }



                        if (data.getVideos() != null) {
                            arrvideo.clear();
                            arrvideo.addAll(data.getVideos());
                        }
                        if (data.getPhotos() != null) {
                            arrPhotos.clear();
                            arrPhotos.addAll(data.getPhotos());
                        }


                        chatImageAdapter = new chat_ImageAdapter(getContext(), arrPhotos,data.getId());
                        gridPhoto.setLayoutManager(new GridLayoutManager(getContext(), noOfColumns));
                        gridPhoto.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
                        gridPhoto.setAdapter(chatImageAdapter);
                        gridPhoto.setNestedScrollingEnabled(false);


                        chatImageAdapter = new chat_ImageAdapter(getContext(), arrvideo,"",data.getId());
                        gridVideo.setLayoutManager(new GridLayoutManager(getContext(), noOfColumns));
                        gridVideo.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
                        gridVideo.setAdapter(chatImageAdapter);
                        gridVideo.setNestedScrollingEnabled(false);
                          if(!(data.getCategory()==null || data.getCategory().equals("")))
                          {
                              tvpagecreationtime.setText(data.getCategory());
                          }
                          else
                          {
                              tvpagecreationtime.setText("");
                          }

                    } else {

                    }
                } else {
                    if (getPageData.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(getPageData.getMessage());
                    }
                }
                Glide.with(getContext())
                        .load(data.getBanner())
                        .into(banner);
                Glide.with(getContext())
                        .load(data.getProfile())
                        .into(circleImageView);

            }

            @Override
            public void onFailure(Call<GetPageData> call, Throwable t) {

                snackBar(t.getMessage());
            }
        });
    }

    private void likeDislikeApi(int status) {
        //   ((HomeActivity)context).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.likeDislike(id,status,data.getPostType(),3);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                //((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo myPage = response.body();
                if (myPage.getStatus()) {

                    if(data.getIsFollow().equals("1"))
                    {
                        data.setIsFollow("0");
                        follow.setText("Follow");
                    }
                    else
                    {
                        data.setIsFollow("1");
                        follow.setText("Unfollow");
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {

            }
        });
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = postResponseArrayList.size();
    }

    private void sendwelcome(String s) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.WelcomeMessage(id,s);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {

                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo commonPojo=response.body();
                if (commonPojo.getStatus()) {

                    String room_id = id.compareTo(userData.getId()) > 0 ? (userData.getId() + id).hashCode() + "" : "" + (id + userData.getId()).hashCode();

                /*Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                intent.putExtra(Constant.INTENT_KEY_CHAT_FRIEND, ""+page_name);
                ArrayList<CharSequence> idFriend = new ArrayList<>();
                GroupChatActivity.bitmapAvataFriend = new HashMap<>();
                idFriend.add(id);
                intent.putCharSequenceArrayListExtra(Constant.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(Constant.INTENT_KEY_CHAT_ROOM_ID, room_id);
                getActivity().startActivity(intent);*/

                    data.setIsChatIniatialised("1");

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("page_id", id);
                    intent.putExtra("user_name", "" + page_name);
                    intent.putExtra("type", "page");
                    intent.putExtra("user_id",userData.getId());
                    getActivity().startActivity(intent);

                } else {
                    if (commonPojo.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(commonPojo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    public void addMessageDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.welcomemessage);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvNew = dialog.findViewById(R.id.et_create);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendwelcome(tvNew.getText().toString());
                dialog.dismiss();

//                AppController.getManager().setInterest(Constant.DATING);
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void likeDislikeApi(int status,int taskFor) {
        //   ((HomeActivity)context).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.likeDislike(data.getId(),status,data.getPostType(),taskFor);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                //((HomeActivity)context).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo myPage = response.body();
                if (myPage.getStatus()) {
                    if (taskFor==1){
//                        if (status==1){
//                            nooflikes=String.valueOf(Integer.parseInt(nooflikes)+1);
//                            likes.setText("Likes "+String.valueOf(Integer.parseInt(nooflikes) +1));
//                            img_like.setImageResource(R.drawable.like_active);
//                            img_dislike.setImageResource(R.drawable.dislike);
//                        }
//                        else if(status==2){
//                            if ( data.getMyVote().equals("1")){
//                                likes.setText("Likes "+String.valueOf(Integer.parseInt(nooflikes) -1));
//                            }
//                            nooflikes=String.valueOf(Integer.parseInt(nooflikes)-1);
//                          //  postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1));
//                            img_like.setImageResource(R.drawable.like);
//                            img_dislike.setImageResource(R.drawable.dislike_active);
//                            //imgBoaring.setImageResource(R.drawable.boring);
//                        }
                    }else{
                        if (status==1){
                            img_bookmark.setImageResource(R.drawable.bookmark);
                            data.setIsBookmark("1");
                        }
                        else if (status==0){
                            img_bookmark.setImageResource(R.drawable.bookmark_side_menu);
                            data.setIsBookmark("0");
                        }
                    }
                } else {
                    if (myPage.getMessage().equals("110110")){
                        ((HomeActivity)getContext()).logout();

                    }
                    else{
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
}

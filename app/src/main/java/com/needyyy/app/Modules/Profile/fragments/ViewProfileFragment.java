package com.needyyy.app.Modules.Profile.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
//import com.needyyy.app.Modules.Home.Activities.ProfileActivity;
import com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter;
import com.needyyy.app.Modules.Home.Fragments.ViewFullImageFragment;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Knocks.models.AcceptRejectRequest;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.EducationAdapter;
import com.needyyy.app.Modules.Profile.adapters.ImageAdapter;
import com.needyyy.app.Modules.Profile.models.GetViewProfile;
import com.needyyy.app.Modules.Profile.models.Photo;
import com.needyyy.app.Modules.Profile.models.UserPicture.ProfessionDetails;
import com.needyyy.app.Modules.Profile.models.ViewProfileData;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.Activities.Getpostdata;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.needyyy.app.constants.Constants.kCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends BaseFragment implements View.OnClickListener {
    EducationAdapter eduAdapter2;
    private int mPage=1;
    public boolean checkblock;
    private int pageSize;
    public PostResponse postresponse;
    private NestedScrollView nestedScrollView;
    private boolean loading = false;
    ArrayList<ProfessionDetails> proFeeds = new ArrayList<ProfessionDetails>();
    ImageView coverphoto,copy;
    TextView tv_name, tv_dob, tv_from, tv_address, tvFriendsCount, tvAddPhoto, iv_edit, tvBio, tvSSN,show;
    RecyclerView rvFeeds;
    private SwipeRefreshLayout pullToReferesh ;
    SingleFeedsAdapter singleFeedsAdapter;
    ImageAdapter imageAdapter;
    private ArrayList<PostResponse> arrFeeds = new ArrayList<>() ;
    private ArrayList<Photo> arrPhotos = new ArrayList<>() ;
    private ArrayList<People> arrFriends = new ArrayList<>() ;
    // ImageView iv_edit;
    ViewProfileData updateProfileResult;
    CoordinatorLayout profile;
    NestedScrollView nestedView;
    AlertDialog.Builder alertBuild;
    Activity activity;
    String memberId="";
    LinearLayout llBio,llPrivatePost,llAcceptRequest;
    String isprivate="";
    RecyclerView gridPhoto, gridFriends,professionaldetailsrec;
    private TextView tvnickname;
    Button declinebutton,acceptbutton;
    LinearLayout professionalDetailsLayout;
    ProgressBar profileCompleteProgressBar;
    ImageView block;
    TextView profileCompleteTextView;
    TextView emailTextView,mobileTextView,genderTextView,interestedInTextView,hobbiesTextView,relationshipTextView;
    private Map<String,String> profileMap=new HashMap<>();

//sagar

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "ProfileFragment";
    CircleImageView profile_circle_iv;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private File newFile;
    private Uri newProfileImageUri;
    private String state, imageName;

    public ViewProfileFragment() {
// Required empty public constructor
    }

    public static ViewProfileFragment newInstance(String memberId,String isprivate) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString("memberId", memberId);
        args.putString("isprivate",isprivate);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.fragment_view_profile);
        activity = getActivity();
        if (getArguments() != null) {
            memberId = "";
            isprivate="";
            memberId = getArguments().getString("memberId");
            isprivate=getArguments().getString("isprivate");
        }
    }

    @Override
    protected void initView(View view) {
        block=view.findViewById(R.id.block);
        acceptbutton=view.findViewById(R.id.accept_button);
        declinebutton=view.findViewById(R.id.decline_button);
        tvnickname=view.findViewById(R.id.tv_nickname);
        nestedScrollView = view.findViewById(R.id.nestedView);
        professionaldetailsrec=view.findViewById(R.id.professional_details_rec);

        professionalDetailsLayout=view.findViewById(R.id.professionalDetailsLayout);
        profileCompleteProgressBar=view.findViewById(R.id.profileCompleteProgressBar);

        emailTextView= view.findViewById(R.id.emailTextView);

        genderTextView=view.findViewById(R.id.genderTextView);
        interestedInTextView=view.findViewById(R.id.interestedInTextView);
        hobbiesTextView=view.findViewById(R.id.hobbiesTextView);
        relationshipTextView=view.findViewById(R.id.relationshipTextView);

        profileCompleteTextView=view.findViewById(R.id.profileCompleteTextView);

        show=view.findViewById(R.id.show);
        copy=view.findViewById(R.id.copy);
        llPrivatePost =view.findViewById(R.id.ll_top);
        llAcceptRequest = view.findViewById(R.id.ll_acceptrequest);
        nestedView = view.findViewById(R.id.nestedView);
        // profile = view.findViewById(R.id.profile);
        coverphoto = view.findViewById(R.id.cover_photo);
        profile_circle_iv = view.findViewById(R.id.profile_circle_iv);
        tv_name = view.findViewById(R.id.tv_profile_name);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_from = view.findViewById(R.id.tv_from);
        tv_address = view.findViewById(R.id.tv_address);
        rvFeeds = view.findViewById(R.id.rv_feeds);
        iv_edit = view.findViewById(R.id.iv_edit);
        gridFriends = view.findViewById(R.id.rv_friends_pics);
        gridPhoto = view.findViewById(R.id.rv_photos);
        tvFriendsCount = view.findViewById(R.id.tv_friends_count);
        pullToReferesh = view.findViewById(R.id.pullto_referesh);
        tvBio = view.findViewById(R.id.tv_add_bio);
        llBio = view.findViewById(R.id.ll_bio);
        tvSSN = view.findViewById(R.id.tv_profile_code);
        block.setOnClickListener(this);

        if(isprivate==null||isprivate.equals("")||isprivate.equals("0")) {
            setFeedRv();
            show.setVisibility(View.VISIBLE);
        }
        else {
            show.setVisibility(View.GONE);
        }
    }
    private UserDataResult userData ;
    ImageAdapter friendListAdapters;

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        show.setText("See More");
        iv_edit.setOnClickListener(this);
        profile_circle_iv.setOnClickListener(this);
        tvFriendsCount.setOnClickListener(this);
        coverphoto.setOnClickListener(this);
        copy.setOnClickListener(this);
        show.setOnClickListener(this);
        tvSSN.setOnClickListener(this);
        llPrivatePost.setOnClickListener(this);
        // tvAddPhoto.setOnClickListener(this);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        if(userData.getId().equals(memberId) || memberId.equals("")){
            iv_edit.setVisibility(View.VISIBLE);
            llAcceptRequest.setVisibility(View.GONE);
            profileCompleteTextView.setVisibility(View.VISIBLE);
            profileCompleteProgressBar.setVisibility(View.VISIBLE);
            block.setVisibility(View.GONE);
            memberId="";
        }
        else {
            profileCompleteTextView.setVisibility(View.GONE);
            llAcceptRequest.setVisibility(View.VISIBLE);
            profileCompleteProgressBar.setVisibility(View.GONE);
            block.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.GONE);
        }
        callProfileApi(memberId,false);
        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialState();
                callProfileApi(memberId,false);
                pullToReferesh.setRefreshing(false);
            }
        });
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        //  callProfileApi(memberId,true);
                        getUserPost(true);
                    }

                }
            }
        });
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = arrFeeds.size();
    }
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


    public void setFeedRv() {
        rvFeeds.setVisibility(View.VISIBLE);
        singleFeedsAdapter = new SingleFeedsAdapter(activity, arrFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(activity));
        rvFeeds.setAdapter(singleFeedsAdapter);
        rvFeeds.setNestedScrollingEnabled(false);
        int noOfColumns = calculateNoOfColumns(getActivity(), 120);
        gridPhoto.setVisibility(View.VISIBLE);
        imageAdapter = new ImageAdapter(activity,arrPhotos,memberId);
        gridPhoto.setLayoutManager(new GridLayoutManager(activity, noOfColumns));
        gridPhoto.addItemDecoration(new GridSpacingItemDecoration(noOfColumns, getResources().getDimensionPixelSize(R.dimen.dp10), true));
        gridPhoto.setAdapter(imageAdapter);
        gridPhoto.setNestedScrollingEnabled(false);
        gridFriends.setVisibility(View.VISIBLE);
        friendListAdapters = new ImageAdapter(activity, arrFriends, ViewProfileFragment.this);
        gridFriends.setLayoutManager(new GridLayoutManager(activity, 3));
        gridFriends.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.dp10), true));
        gridFriends.setAdapter(friendListAdapters);
        gridFriends.setNestedScrollingEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_top:
                ((HomeActivity) getActivity()).replaceFragment(PostFragment.newInstance(memberId,"1"), true);
                break;
            case R.id.iv_edit:
                ((HomeActivity) getActivity()).replaceFragment(ProfileFragment.newInstance(), true);
                break;

            case R.id.profile_circle_iv:
// AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
// View mView = getLayoutInflater().inflate(R.layout.zoom_image, null);
// PhotoView photoView = mView.findViewById(R.id.imageView);
// //photoView.setImageResource(android.R.mipmap.sym_def_app_icon);
// Glide.with(activity)
// .load(updateProfileResult.getProfilePicture())
// .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
// .placeholder(R.drawable.needyy)
// .error(R.drawable.needyy))
// .into(photoView);
// mBuilder.setView(mView);
// AlertDialog mDialog = mBuilder.create();
// mDialog.show();


                if(isprivate==null||isprivate.equals("")||isprivate.equals("0"))
                {
                    getpostdata(updateProfileResult.getProfile_picture_id());
                   // ((HomeActivity) getActivity()).replaceFragment(ViewProfileFragment.newInstance(String.valueOf(updateProfileResult.getProfile_picture_id()),null), true);

                    //((HomeActivity) getActivity()).replaceFragment(ZoomImage.newInstance(updateProfileResult.getProfilePicture()), true);
                }
                else if(isprivate.equals("1"))
                {
                    Toast.makeText(getContext(),"profile is private",Toast.LENGTH_SHORT).show();
                }
                break;
// case R.id.tv_add_photo:
// CommonUtil.showShortToast(getContext(),"add Photo");
//

            case R.id.block:
                if(checkblock==false)
                {
                    popupforblock();
                }
                else if(checkblock==true)
                {
                    popupforunblock();
                }
                break;

            case R.id.cover_photo:
                getpostdata(updateProfileResult.getCover_picture_id());
                //((HomeActivity) getActivity()).replaceFragment(ViewProfileFragment.newInstance(String.valueOf(updateProfileResult.getCover_picture_id()),null), true);
                //((HomeActivity) getActivity()).replaceFragment(ZoomImage.newInstance(updateProfileResult.getCoverpicture()), true);
                break;
            case R.id.show:
                if(show.getText().equals("See More"))
                {
                    professionaldetailsrec.setVisibility(View.VISIBLE);
                    professionalDetailsLayout.setVisibility(View.VISIBLE);
                    if(proFeeds.equals("")||proFeeds.size()==0)
                    {
                        //Toast.makeText(getContext(),"please fill professional details",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        eduAdapter2 = new EducationAdapter(getActivity(), null, ViewProfileFragment.this, proFeeds);
                        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
                        professionaldetailsrec.setLayoutManager(layoutManager2);
                        professionaldetailsrec.setAdapter(eduAdapter2);
                    }
                    show.setText("See Less");
                }
                else if(show.getText().equals("See Less")) {
                    show.setText("See More");
                    professionalDetailsLayout.setVisibility(View.GONE);
                    professionaldetailsrec.setVisibility(View.GONE);
                }
                break;
            case R.id.copy:
                ClipboardManager cm = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvSSN.getText());
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_profile_code:
                ClipboardManager cm2 = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm2.setText(tvSSN.getText());
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private  void  getpostdata(String taskid) {
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<Getpostdata> call = Service.getPost(taskid);
            call.enqueue(new Callback<Getpostdata>() {
                @Override
                public void onResponse(Call<Getpostdata> call, Response<Getpostdata> response) {
                    cancelProgressDialog();
                    Getpostdata getpostdata = response.body();
                    if(getpostdata.getStatus()==true) {
                        postresponse = getpostdata.getData();
                        ((HomeActivity) getActivity()).replaceFragment(ViewFullImageFragment.newInstance(postresponse), true);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"No Post found",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Getpostdata> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
        }else{

        }

    }



    private void popupforunblock() {
        alertBuild = new AlertDialog.Builder(getContext());

        alertBuild
                .setTitle("Are You Sure to unblock this User?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getblock(memberId, "0");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = alertBuild.create();
        dialog.show();

    }

    private void popupforblock() {

        alertBuild = new AlertDialog.Builder(getContext());

        alertBuild
                .setTitle("Are You Sure to block this User?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getblock(memberId, "1");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = alertBuild.create();
        dialog.show();
    }

    private void getblock(String id,String statuss) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.BlockUnblock(id, statuss);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                cancelProgressDialog();
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    if(checkblock==false) {
                        block.setImageDrawable(getResources().getDrawable(R.drawable.unblock));
                        checkblock = true;
                    }
                    else if(checkblock==true)
                    {
                        block.setImageDrawable(getResources().getDrawable(R.drawable.block));

                        checkblock=false;
                    }

//                    block.setText("Unblock");
//                    ((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);

                }
            }
            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {

                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callProfileApi(String id,Boolean status) {
        if (CommonUtil.isConnectingToInternet(activity)) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GetViewProfile> call = Service.viewProfile(id);
            call.enqueue(new Callback<GetViewProfile>() {
                @Override
                public void onResponse(Call<GetViewProfile> call, Response<GetViewProfile> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());

                    GetViewProfile getViewProfile = response.body();
                    if (getViewProfile.getStatus()) {
                        updateProfileResult = getViewProfile.getData();
                        ((HomeActivity)getActivity()).manageToolbar(!TextUtils.isEmpty(updateProfileResult.getName()) ? updateProfileResult.getName() : "Ambuj Sukla","2");
                        proFeeds.clear();
                        for(int i=0;i<updateProfileResult.getProfessionDetails().size();i++) {
                            proFeeds.addAll(updateProfileResult.getProfessionDetails());
                        }
                        int type=0,position=0;
                        if(updateProfileResult.getIs_block().equals("1")) {
                            block.setImageDrawable(getResources().getDrawable(R.drawable.unblock));
                            checkblock = true;
                        }
                        else if(updateProfileResult.getIs_block().equals("0"))
                        {
                            block.setImageDrawable(getResources().getDrawable(R.drawable.block));
                            checkblock=false;
                        }
                        if(updateProfileResult.getIsfriend()!=null) {
                            if (updateProfileResult.getIsfriend().equals("1")) {
                                acceptbutton.setVisibility(View.VISIBLE);
                                acceptbutton.setText("Remove");
                                declinebutton.setVisibility(View.GONE);
                                acceptbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  updateProfileResult.setIsfriend("2");
                                        sendKnockRequest(id, 2, type, position);
                                    }
                                });
                            } else if (updateProfileResult.getIsfriend().equals("2")) {
                                acceptbutton.setVisibility(View.VISIBLE);
                                declinebutton.setVisibility(View.VISIBLE);
                                acceptbutton.setText("Add Friend");
                                declinebutton.setText("Cancel");
                                acceptbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //updateProfileResult.setIsfriend("1");
                                        sendKnockRequest(id, 1, type, position);
                                    }
                                });
                            } else if (updateProfileResult.getIsfriend().equals("3")) {
                                acceptbutton.setVisibility(View.GONE);
                                declinebutton.setVisibility(View.VISIBLE);
                                declinebutton.setText("Cancel");
                                declinebutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // updateProfileResult.setIsfriend("2");
                                        sendKnockRequest(id, 2, type, position);
                                    }
                                });
                            } else if (updateProfileResult.getIsfriend().equals("4")) {
                                acceptbutton.setVisibility(View.VISIBLE);
                                declinebutton.setVisibility(View.VISIBLE);
                                declinebutton.setText("Cancel");
                                acceptbutton.setText("Accept");
                                declinebutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // updateProfileResult.setIsfriend("2");

                                        getRequestStatus(id, "2", type, position);
                                    }
                                });
                                acceptbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  updateProfileResult.setIsfriend("2");
                                        getRequestStatus(id, "1", type, position);
                                    }
                                });
                            }
                        }


                        if(isprivate==null||isprivate.equals("")||isprivate.equals("0")) {
                            arrPhotos.clear();
                            arrFriends.clear();
// arrFeeds.clear();
                            if(updateProfileResult.getNickname()==null || updateProfileResult.getNickname().equals(""))
                            {
                                tvnickname.setVisibility(View.GONE);
                            }
                            else
                            {
                                tvnickname.setVisibility(View.VISIBLE);
                                tvnickname.setText("("+updateProfileResult.getNickname()+")");
                            }
                            //  tvnickname.setText("("+updateProfileResult.getNickname()+")");
                            memberId = id;
                            arrPhotos.addAll(updateProfileResult.getPhotos());
                            imageAdapter.notifyDataSetChanged();
                            arrFriends.addAll(updateProfileResult.getFriends());
                            friendListAdapters.notifyDataSetChanged();
                            getUserPost(status);
                        }
                        else{
                        }
                        setProfileData();
                    } else {
                        nestedView.setVisibility(View.GONE);
                        CommonUtil.snackBar("", activity);
                    }
                }

                @Override
                public void onFailure(Call<GetViewProfile> call, Throwable t) {
                    CommonUtil.cancelProgress();
                    nestedView.setVisibility(View.GONE);
                    CommonUtil.snackBar(t.getMessage(), activity);
                }
            });
        }
        else {
            nestedView.setVisibility(View.GONE);
            CommonUtil.snackBar(getString(R.string.internetmsg), activity);
        }
    }

    private void getUserPost(Boolean status) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<PostDataBase> call = Service.getUserPost(mPage,20, memberId);
        call.enqueue(new Callback<PostDataBase>() {
            @Override
            public void onResponse(Call<PostDataBase> call, Response<PostDataBase> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                PostDataBase myPage = response.body();
                if (myPage.getStatus()) {
                    if(status) {
                        arrFeeds.addAll(myPage.getData());
                        loading = !(myPage.getData().size()==0);
                    }
                    else {
                        initialState();
                        if (arrFeeds != null && arrFeeds.size() != 0) {
                            arrFeeds.clear();
                        }
                        arrFeeds.addAll(myPage.getData());
                    }
                    singleFeedsAdapter.notifyDataSetChanged();
                }else {
                    if (myPage.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }else{
                        cancelProgressDialog();
                        snackBar(myPage.getMessage());
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


    private void setProfileData() {
        nestedView.setVisibility(View.VISIBLE);
// profile.setVisibility(View.VISIBLE);
        tv_name.setText(!TextUtils.isEmpty(updateProfileResult.getName()) ? updateProfileResult.getName() : "Ambuj Sukla");

        profileMap.put("tv_name",tv_name.getText().toString());

        Glide.with(getActivity()).load(updateProfileResult.getCoverpicture())
                .apply((RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy)))
                .into(coverphoto);

        profileMap.put("coverphoto",updateProfileResult.getCoverpicture());

        String dob = updateProfileResult.getDob().replace("-", "");
        if (dob.length() <= 8) {
            tv_dob.setText(updateProfileResult.getDob());
        } else {
            Timestamp ts = new Timestamp(Long.parseLong(updateProfileResult.getDob()) * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
            Date date = ts;
            try {
                date = (Date) formatter.parse(String.valueOf(ts.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_dob.setText(!TextUtils.isEmpty(date.toString()) ? formatter.format(date) : "");
        }

        profileMap.put("tv_dob",tv_dob.getText().toString());


        int size = updateProfileResult.getUserAddress().size();
        if (size >= 2) {
            tv_from.setText("From " + (!TextUtils.isEmpty(updateProfileResult.getUserAddress().get(size - 2).getLocation()) ?
                    updateProfileResult.getUserAddress().get(size - 2).getLocation() : " Delhi, India"));

            tv_address.setText("Lives in " + (!TextUtils.isEmpty(updateProfileResult.getUserAddress().get(size - 2).getLocation()) ?
                    updateProfileResult.getUserAddress().get(size - 1).getLocation() : " Delhi, India"));
        }
        else if(size==1)
        {
            tv_from.setText("");

            tv_address.setText("Lives in " + (!TextUtils.isEmpty(updateProfileResult.getUserAddress().get(size - 1).getLocation()) ?
                    updateProfileResult.getUserAddress().get(size - 1).getLocation() : " Delhi, India"));
        }

        else {
            tv_from.setText("From " +
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"));

            tv_address.setText("Lives in " +
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"));
        }

        profileMap.put("tv_from",tv_from.getText().toString());

        profileMap.put("tv_address",tv_address.getText().toString());
// tv_from.setText("From " + (!TextUtils.isEmpty(updateProfileResult.getLocationDetail().get(size - 2).getLocation()) ?
// updateProfileResult.getLocationDetail().get(size - 2).getLocation() :" Delhi, India"));
// tv_address.setText("Lives in " + (!TextUtils.isEmpty(updateProfileResult.getLocationDetail().get(size - 2).getLocation()) ?
// updateProfileResult.getLocationDetail().get(size - 1).getLocation() :" Delhi, India"));
        if (isprivate == null || isprivate.equals("")) {
            Glide.with(this)
                    .load(updateProfileResult.getProfilePicture())
                    // .apply(bitmapTransform(new BlurTransformation(25, 3)))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(profile_circle_iv);
        } else if (isprivate.equals("1")) {
            Glide.with(this).load(updateProfileResult.getProfilePicture())
                    .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(profile_circle_iv);
        } else if (isprivate.equals("0")) {
            Glide.with(this)
                    .load(updateProfileResult.getProfilePicture())
                    //.apply(bitmapTransform(new BlurTransformation(25,3)))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(profile_circle_iv);
        }

        profileMap.put("profile_circle_iv",updateProfileResult.getProfilePicture());

        tvFriendsCount.setText(updateProfileResult.getFriends().size() + " Friends");

        profileMap.put("tvFriendsCount",tvFriendsCount.getText().toString());


        if (userData.getId().equals(memberId) || memberId.equals("")) {
            iv_edit.setVisibility(View.VISIBLE);
// tvAddPhoto.setVisibility(View.VISIBLE);
        } else {
            iv_edit.setVisibility(View.GONE);
// tvAddPhoto.setVisibility(View.GONE);
        }

        if (!updateProfileResult.getBio().equals("")) {
            tvBio.setText(updateProfileResult.getBio().toString());
            llBio.setVisibility(View.VISIBLE);
        } else {
            llBio.setVisibility(View.GONE);
        }

        profileMap.put("tvBio",tvBio.getText().toString());

        if (isprivate == null || isprivate.equals("0") || isprivate.equals("")) {
            tvSSN.setText(updateProfileResult.getSsn().toString());
        }
        else
        {
            String showtext="";
            for(int i=0;i<updateProfileResult.getSsn().length();i++)
            {
                if(i<3) {
                    showtext = showtext + updateProfileResult.getSsn().charAt(i);
                }
                else
                {
                    showtext =showtext+"*";
                }
            }
            tvSSN.setText(showtext);
        }
        profileMap.put("tvSSN",tvSSN.getText().toString());

        emailTextView.setText(updateProfileResult.getEmail());

        profileMap.put("emailTextView",emailTextView.getText().toString());
        if("0".equalsIgnoreCase(updateProfileResult.getGender())){
            genderTextView.setText("Female");
        } else if("1".equalsIgnoreCase(updateProfileResult.getGender())){
            genderTextView.setText("Male");
        }
        profileMap.put("genderTextView",genderTextView.getText().toString());

        if("0".equalsIgnoreCase(updateProfileResult.getInterested_in())){
            interestedInTextView.setText("Female");
        }
        else if("1".equalsIgnoreCase(updateProfileResult.getInterested_in())){
            interestedInTextView.setText("Male");
        }

        profileMap.put("interestedInTextView",interestedInTextView.getText().toString());

        hobbiesTextView.setText(updateProfileResult.getHobbies());
        profileMap.put("hobbiesTextView",hobbiesTextView.getText().toString());

        relationshipTextView.setText(updateProfileResult.getRelation_ship());
        profileMap.put("relationshipTextView",relationshipTextView.getText().toString());


        int count=0;
        for (Map.Entry<String,String> entry : profileMap.entrySet()){

            if(!entry.getValue().equalsIgnoreCase("")){
                count++;
            }

        }

        int per=((count*100)/profileMap.size());


        Log.d("response","profileMap.size():"+profileMap.size()+",count:"+count+",per:"+per);

        profileCompleteTextView.setText("(Profile "+per+"% Completed)");

        profileCompleteProgressBar.setProgress(per);

    }

// @Override
// public void onDestroyView() {
// FragmentManager manager = (ViewProfileFragment.this).getFragmentManager();
// FragmentTransaction trans = manager.beginTransaction();
// trans.remove(ViewProfileFragment.this);
// trans.commit();
// super.onDestroyView();
// }


    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            memberId = "";
            isprivate="";
            memberId = getArguments().getString("memberId");
            isprivate=getArguments().getString("isprivate");
        }
    }

    public void sendKnockRequest(String id, int statuss, int type, int position) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequest(id, statuss);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    callProfileApi(id,false);
                    //((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
                } else {
                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {


            }
        });

    }


    public void getRequestStatus(String id, String status, int type, int position) {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<AcceptRejectRequest> call = Service.acceptRejectRequest(id, status);
        call.enqueue(new Callback<AcceptRejectRequest>() {
            @Override
            public void onResponse(Call<AcceptRejectRequest> call, Response<AcceptRejectRequest> response) {
//                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                AcceptRejectRequest acceptRejectRequest = response.body();
                if (acceptRejectRequest.getStatus()) {
                    Toast.makeText(activity, acceptRejectRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    //((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
//                    arrGetRequests.addAll(acceptRejectRequest.getData());
                    if (type == 1) {
                        callProfileApi(id,false);
                    }
                } else {
                    if (acceptRejectRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        Toast.makeText(activity, acceptRejectRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptRejectRequest> call, Throwable t) {
                //  cancelProgressDialog();
            }
        });
    }
}
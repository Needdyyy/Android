package com.needyyy.app.Modules.Home.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.needyyy.AppController;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.CommentFragment;
import com.needyyy.app.Modules.Home.Fragments.UserListFragment;
import com.needyyy.app.Modules.Home.Fragments.ViewFullImageFragment;
//import com.needyyy.app.Modules.Home.Fragments.WebViewFragment;
import com.needyyy.app.Modules.Home.Fragments.WebViewFragment;
import com.needyyy.app.Modules.Home.bottomsfeetdialog.ShareNowBottomSheet;
import com.needyyy.app.Modules.Home.modle.PostMetum;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.CreatePageFinalFragment;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.PageDataBase;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.mypage.model.masterindex.masterindex.ReportPost;
import com.needyyy.app.mypage.mypage_details;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.vedio.Vedio;
import com.needyyy.app.webutils.WebInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.needyyy.app.constants.Constants.Kmasterhit;
import static com.needyyy.app.constants.Constants.kCurrentUser;
import static com.needyyy.app.manager.BaseManager.BaseManager.SaveImage;

public class SingleFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static String Reportid;
    private File destFile;
    private File file;
    private File sourceFile;
    private SimpleDateFormat dateFormatter;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    Context context;
    RelativeLayout rlContentTypes;
    private ArrayList<PostResponse> postResponsesList = new ArrayList<>() ;
    private  int VIEWTYPEPOST=1,VIEWTYPECHECKIN=2,VIEWTYPProfile=3,VIEWTYPEEDUCATION=4,VIEWTYPEDDRESS=5,VIEWTYPERELATIONSHIP=6,VIEWTYPEPAGE=7,VIEWTYPEGLOBALPOST=10,VIEWTYPESHAREPOST=9,VIEWTYPEPROFESSION=8,VIEWTYPEPAGEPOST=11;
    //1-post,2-checkin,3-profile_pictures,4-education,5-address
    public SingleFeedsAdapter(Context context, ArrayList<PostResponse> postResponsesList) {
        this.context = context;
        this.postResponsesList = postResponsesList;
    }


    @Override
    public int getItemViewType(int position) {
        if (postResponsesList.get(position).getPostType().equals("1"))
        {
            if (postResponsesList.get(position).getType().equals("1")){
                return VIEWTYPEPOST;
            }else if(postResponsesList.get(position).getType().equals("2")){
                return VIEWTYPECHECKIN;
            }else if(postResponsesList.get(position).getType().equals("3")){
                return VIEWTYPProfile;
            }else if(postResponsesList.get(position).getType().equals("4")){
                return VIEWTYPEEDUCATION;
            }else if(postResponsesList.get(position).getType().equals("5")){
                return VIEWTYPEDDRESS;
            }else if(postResponsesList.get(position).getType().equals("6")){
                return VIEWTYPERELATIONSHIP;
            }
            else if(postResponsesList.get(position).getType().equals("7"))
            {
                return  VIEWTYPEPAGE;
            }
            else if(postResponsesList.get(position).getType().equals("8"))
            {
                return VIEWTYPEPROFESSION;
            }
        } else if (postResponsesList.get(position).getPostType().equals("2")) {
            return VIEWTYPEPAGEPOST;
        }else if (postResponsesList.get(position).getPostType().equals("3")) {
            return VIEWTYPEPAGE;
        }else if (postResponsesList.get(position).getPostType().equals("4")) {
//            if(postResponsesList.get(position).getType().equals("0"))
//            {
//                return VIEWTYPEGLOBALPOST;
//            }
//            else {
            return VIEWTYPESHAREPOST;
//            }
        }else if (postResponsesList.get(position).getPostType().equals("5")) {
            return VIEWTYPEGLOBALPOST;
        }
        return -1;
    }
//    @NonNull
//    @Override
//    public SearchPlaceeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_layout, parent, false);
//        return new SearchPlaceeViewHolder(view);
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case 10:
                View v0 = inflater.inflate(R.layout.single_feed_globalpost, parent, false);
                viewHolder = new Globalpost(v0);
                break;

            case 1:
            case 3:
            case 7:
            case 8:
            case 2:
                View v1 = inflater.inflate(R.layout.single_post_layout, parent, false);
                viewHolder = new SingleFeedsViewHolder(v1);
                break;

            case 4:
            case 5:
            case 6:
                View v2 = inflater.inflate(R.layout.single_feed_education, parent, false);
                viewHolder = new EducationViewHolder(v2);
                break;

            case 9:
                View v3 = inflater.inflate(R.layout.single_feed_share_post, parent, false);
                viewHolder = new SharePostViewHolder(v3);
                break;

            case 11:
                View v5 = inflater.inflate(R.layout.pagepost, parent, false);
                viewHolder = new Pagepost(v5);
                break;

            default:
                View v4 = inflater.inflate(R.layout.single_post_layout, parent, false);
                viewHolder = new SingleFeedsViewHolder(v4);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PostResponse postResponse = postResponsesList.get(i);
        HomeActivity activity = (HomeActivity) context;
        switch (viewHolder.getItemViewType()) {
            case 11:
                Pagepost pagepost = (Pagepost) viewHolder;
                pagepost.bindContentpagepost(postResponse);
                if (postResponse.getMyVote().equals("1")) {
                    pagepost.imgLike.setImageResource(R.drawable.like_active);
                    pagepost.imgDislike.setImageResource(R.drawable.dislike);
                    pagepost.imgBoaring.setImageResource(R.drawable.boring);
                } else if (postResponse.getMyVote().equals("2")) {
                    pagepost.imgLike.setImageResource(R.drawable.like);
                    pagepost.imgDislike.setImageResource(R.drawable.dislike_active);
                    pagepost.imgBoaring.setImageResource(R.drawable.boring);
                } else if (postResponse.getMyVote().equals("3")) {
                    pagepost.imgLike.setImageResource(R.drawable.like);
                    pagepost.imgDislike.setImageResource(R.drawable.dislike);
                    pagepost.imgBoaring.setImageResource(R.drawable.boring_active);
                } else if (postResponse.getMyVote().equals("0")) {
                    pagepost.imgLike.setImageResource(R.drawable.like);
                    pagepost.imgDislike.setImageResource(R.drawable.dislike);
                    pagepost.imgBoaring.setImageResource(R.drawable.boring);
                }


                //   spanna


                String tags = "";
                if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                    if (postResponse.getTaggedPeople().size() > 1) {
                        tags = " is with "  + postResponse.getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");

                    } else {
                        tags = " is with " + postResponse.getTaggedPeople().get(0).getName();
                    }
                    if (postResponse.getLocation() != null) {
                        if (postResponse.getLocation().equals("")) {
                        } else {
                            tags = tags + " at location " + postResponse.getLocation();
                        }
                    }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

                } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                    if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
                    {

                    }
                    else
                    {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                        tags= " at location"  + postResponse.getLocation();
                    }

                }


                //   start   of    a    spanable string


                String textt=postResponse.getUserName()+tags;

                SpannableString spannableStringg=new SpannableString(textt);
                ClickableSpan clickableSpann=new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getTaggedPeople().get(0).getId(),null), true);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLACK);
                        ds.setUnderlineText(false);
                    }
                };
                ClickableSpan clickableSpan22=new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getTaggedPeople()), true);
                    }

                    @Override
                    public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLACK);
                        ds.setUnderlineText(false);
                    }
                };

                ClickableSpan clickableSpan33=new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                    }

                    @Override
                    public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLACK);
                        ds.setUnderlineText(false);
                    }
                };
                if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                    if(postResponse.getTaggedPeople().size()==1) {

                        spannableStringg.setSpan(clickableSpan33, 0,
                                postResponse.getUserName().length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        spannableStringg.setSpan(clickableSpann, postResponse.getUserName().length() + 8,
                                postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    else
                    {
                        spannableStringg.setSpan(clickableSpan33, 0,
                                postResponse.getUserName().length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        spannableStringg.setSpan(clickableSpann, postResponse.getUserName().length() + 8,
                                postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        spannableStringg.setSpan(clickableSpan22, postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                , postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                        +postResponse.getTaggedPeople().size()+4
                                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }

                }

                else
                {
                    spannableStringg.setSpan(clickableSpan33, 0,
                            postResponse.getUserName().length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


                pagepost.tvPageTitle.setText(spannableStringg);
                pagepost.tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




                //   end of a spanable string

//                           singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + tag)) ;


                //  ens

                //tvPageTitle.setText(postResponseArrayList.get(i).getUserName());

                if (!TextUtils.isEmpty(postResponse.getProfilePicture())) {
                    // Bitmap bmp = decodeFile(profile);
                    Glide.with(context)
                            .load(postResponse.getProfilePicture())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(pagepost.imgProfile);
                }
                else {
                    pagepost.imgProfile.setImageResource(R.drawable.needyy);
                }

                if (postResponse.getPostMeta().size() != 0) {
                    pagepost.imgPost.setVisibility(View.VISIBLE);
                    // Bitmap bmp = decodeFile(profile);
                    Glide.with(context)
                            .load(postResponse.getPostMeta().get(0).getLink())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(pagepost.imgPost);
                } else {
                    pagepost.imgPost.setVisibility(View.GONE);
                    pagepost.imgPost.setImageResource(R.drawable.needyy);
                }
                if (!TextUtils.isEmpty(postResponse.getText())) {
                    pagepost.tvPageDescription.setVisibility(View.VISIBLE);
                    // Bitmap bmp = decodeFile(profile);
                    if(postResponse.getText().length()>200) {
                        String seemore="SeeMore";
                        String texttt=postResponse.getText().substring(0,200)+seemore;
                        String fullstring=postResponse.getText()+"Seeless";
                        SpannableString spannableStrin = new SpannableString(texttt);
                        SpannableString spannableString8 = new SpannableString(fullstring);

                        ClickableSpan clickableSpann66=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                pagepost.tvPageDescription.setText(spannableString8);
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLUE);
                                ds.setUnderlineText(false);
                            }
                        };

                        ClickableSpan clickableSpann6=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                pagepost.tvPageDescription.setText(spannableStrin);
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLUE);
                                ds.setUnderlineText(false);
                            }
                        };

                        spannableStrin.setSpan(clickableSpann66, texttt.length()-7,
                                texttt.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        spannableString8.setSpan(clickableSpann6, postResponse.getText().length(),
                                postResponse.getText().length()+7,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        pagepost.tvPageDescription.setText(spannableStrin);

                    }
                    else
                    {

                        pagepost.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                    }

                }
                else {
                    pagepost.tvPageDescription.setVisibility(View.GONE);
                }
                pagepost.tvPageDescription.setMovementMethod(LinkMovementMethod.getInstance());

                //  singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                pagepost.tvPageDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((postResponse.getText().contains("www")
                                || postResponse.getText().contains("http"))
                                && postResponse.getText().contains(".com")) {
                            if (activity instanceof HomeActivity) {
                                activity.replaceFragment(WebViewFragment.newInstance(postResponse.getText()), true);
                            }
                        }
                    }
                });

//                if (!TextUtils.isEmpty(postResponse.getCreated())) {
//                    // Bitmap bmp = decodeFile(profile);
//                    pagepost.tvPageDescription.setVisibility(View.VISIBLE);
//                    pagepost.tvPageDescription.setText(postResponse.getText());
//                } else {
//                    pagepost.tvPageDescription.setVisibility(View.GONE);
//                }

                if (!TextUtils.isEmpty(postResponse.getCreated())) {
                    pagepost.tvPageCreatedTime.setVisibility(View.VISIBLE);
                    pagepost.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000));
                } else {
                    pagepost.tvPageCreatedTime.setVisibility(View.GONE);
                }

////.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000));
                if (Integer.parseInt(postResponse.getLikes()) <= 1) {
                    pagepost.tvTotalLike.setText(convertno(postResponse.getLikes()) + " " + context.getResources().getString(R.string.like));
                }
                else {
                    pagepost.tvTotalLike.setText(convertno(postResponse.getLikes()) + " " + context.getResources().getString(R.string.likes));

                }
                if (Integer.parseInt(postResponse.getDislikes()) <= 1) {
                    pagepost.tv_totaldislike.setText(convertno(postResponse.getDislikes()) + " " + "Interesting");
                }
                else {
                    pagepost.tv_totaldislike.setText(convertno(postResponse.getDislikes()) + " " + "Interesting");
                }
                if (Integer.parseInt(postResponse.getBorings()) <= 1) {
                    pagepost.tv_totalboring.setText(convertno(postResponse.getBorings()) + " " + "Boring");

                }
                else {
                    pagepost.tv_totalboring.setText(convertno(postResponse.getBorings()) + " " + "Borings");
                }
                if (Integer.parseInt(postResponse.getComments()) <= 1) {
                    pagepost.tvTotalComment.setText(convertno(postResponse.getComments()) + " " + context.getResources().getString(R.string.comment));
                }
                else {
                    pagepost.tvTotalComment.setText(convertno(postResponse.getComments()) + " " + context.getResources().getString(R.string.comments));
                }
        break;

            case 10:
                Globalpost globalpost=(Globalpost) viewHolder;
                globalpost.bindContentGlobal(postResponse);
                globalpost.captiontext.setText(postResponse.getCaption());
                globalpost.postname.setText("-- "+postResponse.getName());
                break;
            case 1:
            case 3:
            case 7:
            case 8:
            case 2:
                SingleFeedsViewHolder singlePostviewAllHolder = (SingleFeedsViewHolder) viewHolder;
                singlePostviewAllHolder.bindContent(postResponse);
                String banner = "", profile = "";
                if(postResponse.getPostMeta()!=null && postResponse.getPostMeta().size()!=0)
                {

                    if(postResponse.getPostMeta().get(0).getFileType().equals("youtube")||postResponse.getPostMeta().get(0).getFileType().equals("video")) {
                        singlePostviewAllHolder.playicon.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.views.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.views.setText(convertno(postResponse.getPostMeta().get(0).getViews()));
                    }
                    else
                    {
                        singlePostviewAllHolder.views.setVisibility(View.GONE);
                        singlePostviewAllHolder.playicon.setVisibility(View.GONE);
                    }
                }
                else
                {
                    singlePostviewAllHolder.views.setVisibility(View.GONE);
                    singlePostviewAllHolder.playicon.setVisibility(View.GONE);
                }


                if (postResponse.getPostType().equals("1")) {

                    //                             for profile pic update

                    if(postResponse.getType().equals("3"))
                    {
                        singlePostviewAllHolder.imgBoaring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.tv_totalboring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.imgBookmark.setVisibility(View.GONE);
                        singlePostviewAllHolder.tvFollow.setVisibility(View.GONE);
                        singlePostviewAllHolder.imgShare.setVisibility(View.VISIBLE);

//                           if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {
//                               String tag = "";
//                               if (postResponse.getTaggedPeople().size() > 1) {
//                                   tag = " Update Profile Picture "  +" is with "  + postResponse.getTaggedPeople().get(0).getName()  + " & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");
//
//                               }
//                               else {
//                                   tag = " Update Profile Picture "+" is with " + postResponse.getTaggedPeople().get(0).getName();
//                               }
//                               if (postResponse.getLocation() != null) {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +
//                                            tag) + " at location "  + postResponse.getLocation()
//                                   );
//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName()  + tag));
//                               }
//                           } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName()+" Update Profile Picture ");
//                           }
//                           if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
//                           {
//
//                           }
//                           else
//                           {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName()
//                                       + " at location " + postResponse.getLocation()
//                               ));
//                           }
//
//                           singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));



                        //  start   spannable string

                        String tag = "";
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if (postResponse.getTaggedPeople().size() > 1) {
                                tag = " is with "  + postResponse.getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");

                            } else {
                                tag = " is with " + postResponse.getTaggedPeople().get(0).getName();
                            }
                            if (postResponse.getLocation() != null) {
                                if (postResponse.getLocation().equals("")) {
                                } else {
                                    tag = tag + " at location " + postResponse.getLocation();
                                }
                            }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

                        } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                            if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
                            {

                            }
                            else
                            {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                                tag= " at location"  + postResponse.getLocation();
                            }

                        }

                        if(postResponse.getFeeling()!=null && postResponse.getFeeling_status()!=null)
                        {
                            tag=tag+" "+postResponse.getFeeling()+" "+postResponse.getFeeling_status();
                        }



                        //   start   of    a    spanable string
                        String text="";

                        if(postResponse.getGender().equals("0"))
                        {
                            text =postResponse.getUserName()+ " Update Profile Picture" + tag;

                        }
                        else if(postResponse.getGender().equals("1"))
                        {
                             text=postResponse.getUserName()+ " Update his Profile Picture" + tag;

                        }
                        else if(postResponse.getGender().equals("2"))
                        {
                             text=postResponse.getUserName()+ " Update her Profile Picture" + tag;

                        }



                        SpannableString spannableString=new SpannableString(text);
                        ClickableSpan clickableSpan=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getTaggedPeople().get(0).getId(),null), true);
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
                                ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getTaggedPeople()), true);
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
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                            }

                            @Override
                            public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLACK);
                                ds.setUnderlineText(false);
                            }
                        };
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if(postResponse.getTaggedPeople().size()==1) {

                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }

                            else

                            {
                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan2, postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                        , postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                                +postResponse.getTaggedPeople().size()+4
                                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            }

                        }

                        else
                        {
                            spannableString.setSpan(clickableSpan3, 0,
                                    postResponse.getUserName().length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }

                        singlePostviewAllHolder.tvPageTitle.setText(spannableString);
                        singlePostviewAllHolder.tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




                        // end spanable string



                        if (!TextUtils.isEmpty(postResponse.getText())) {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.VISIBLE);
                            // Bitmap bmp = decodeFile(profile);
                            if(postResponse.getText().length()>200) {
                                String seemore="SeeMore";
                                String texttt=postResponse.getText().substring(0,200)+seemore;
                                String fullstring=postResponse.getText()+"Seeless";
                                SpannableString spannableStrin = new SpannableString(texttt);
                                SpannableString spannableString8 = new SpannableString(fullstring);

                                ClickableSpan clickableSpann66=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableString8);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                ClickableSpan clickableSpann6=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                spannableStrin.setSpan(clickableSpann66, texttt.length()-7,
                                        texttt.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString8.setSpan(clickableSpann6, postResponse.getText().length(),
                                        postResponse.getText().length()+7,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);

                            }
                            else
                            {

                                singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                            }

                        }
                        else {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.GONE);

                        }
                        singlePostviewAllHolder.tvPageDescription.setMovementMethod(LinkMovementMethod.getInstance());

                        //  singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                        singlePostviewAllHolder.tvPageDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((postResponse.getText().contains("www")
                                        || postResponse.getText().contains("http"))
                                        && postResponse.getText().contains(".com")) {
                                    if (activity instanceof HomeActivity) {
                                        activity.replaceFragment(WebViewFragment.newInstance(postResponse.getText()), true);
                                    }
                                }
                            }
                        });


                        if (postResponse.getPostMeta() != null && postResponse.getPostMeta().size() != 0) {
                            if (!TextUtils.isEmpty(postResponse.getPostMeta().get(0).getLink())) {
                                banner = postResponse.getPostMeta().get(0).getLink();
                            } else {
                                banner = "";
                            }
                        }
                        profile = postResponse.getProfilePicture();

                    }
                    // end of profile pic update

                    // update cover picture


                    else if(postResponse.getType().equals("7"))
                    {
                        singlePostviewAllHolder.imgBoaring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.tv_totalboring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.imgBookmark.setVisibility(View.GONE);
                        singlePostviewAllHolder.tvFollow.setVisibility(View.GONE);
                        singlePostviewAllHolder.imgShare.setVisibility(View.VISIBLE);

//                                              if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {
//                            String tag = "";
//                            if (postResponse.getTaggedPeople().size() > 1) {
//
//
//                                tag = " Update Cover Picture "  +" is with "  + postResponse.getTaggedPeople().get(0).getName()  + " & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");
//
//                            } else {
//                                tag = " Update Cover Picture "+" is with" + postResponse.getTaggedPeople().get(0).getName();
//                            }
//                            if (postResponse.getLocation() != null) {
//                                singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +
//                                        tag) + " at location "  + postResponse.getLocation()
//
//                                );
//                            } else {
//                                singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName()  + tag));
//                            }
//
//
//                        }
//                        else {
//                            singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName()+" Update Cover Picture ");
//                        }
//                        if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
//                        {
//
//                        }
//                        else
//                        {
//                            singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName()
//                                     + " at location "  + postResponse.getLocation()
//
//                            ));
//                        }

                        //  start of spanable string

                        String tag = "";
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if (postResponse.getTaggedPeople().size() > 1) {
                                tag = " is with "  + postResponse.getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");

                            } else {
                                tag = " is with " + postResponse.getTaggedPeople().get(0).getName();
                            }
                            if (postResponse.getLocation() != null) {
                                if (postResponse.getLocation().equals("")) {
                                } else {
                                    tag = tag + " at location " + postResponse.getLocation();
                                }
                            }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

                        } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                            if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
                            {

                            }
                            else
                            {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                                tag= " at location "  + postResponse.getLocation();
                            }

                        }

                        if(postResponse.getFeeling()!=null && postResponse.getFeeling_status()!=null)
                        {
                            tag=tag+" "+postResponse.getFeeling()+" "+postResponse.getFeeling_status();
                        }



                        //   start   of    a    spanable string


                        String text="";

                        if(postResponse.getGender().equals("0"))
                        {
                            text =postResponse.getUserName()+ " Update Cover Picture" + tag;

                        }
                        else if(postResponse.getGender().equals("1"))
                        {
                            text=postResponse.getUserName()+ " Update his Cover Picture" + tag;

                        }
                        else if(postResponse.getGender().equals("2"))
                        {
                            text=postResponse.getUserName()+ " Update her Cover Picture" + tag;

                        }


                        SpannableString spannableString=new SpannableString(text);
                        ClickableSpan clickableSpan=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getTaggedPeople().get(0).getId(),null), true);
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
                                ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getTaggedPeople()), true);
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
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                            }

                            @Override
                            public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLACK);
                                ds.setUnderlineText(false);
                            }
                        };
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if(postResponse.getTaggedPeople().size()==1) {

                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }

                            else
                            {
                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan2, postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                        , postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                                +postResponse.getTaggedPeople().size()+4
                                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            }

                        }

                        else
                        {
                            spannableString.setSpan(clickableSpan3, 0,
                                    postResponse.getUserName().length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }

                        singlePostviewAllHolder.tvPageTitle.setText(spannableString);
                        singlePostviewAllHolder.tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




                        //  end of spanable string

                        if (!TextUtils.isEmpty(postResponse.getText())) {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.VISIBLE);
                            // Bitmap bmp = decodeFile(profile);
                            if(postResponse.getText().length()>200) {
                                String seemore="SeeMore";
                                String texttt=postResponse.getText().substring(0,200)+seemore;
                                String fullstring=postResponse.getText()+"Seeless";
                                SpannableString spannableStrin = new SpannableString(texttt);
                                SpannableString spannableString8 = new SpannableString(fullstring);

                                ClickableSpan clickableSpann66=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableString8);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                ClickableSpan clickableSpann6=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                spannableStrin.setSpan(clickableSpann66, texttt.length()-7,
                                        texttt.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString8.setSpan(clickableSpann6, postResponse.getText().length(),
                                        postResponse.getText().length()+7,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);

                            }
                            else
                            {

                                singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                            }

                        }
                        else {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.GONE);

                        }
                        singlePostviewAllHolder.tvPageDescription.setMovementMethod(LinkMovementMethod.getInstance());

                        //  singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                        singlePostviewAllHolder.tvPageDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((postResponse.getText().contains("www")
                                        || postResponse.getText().contains("http"))
                                        && postResponse.getText().contains(".com")) {
                                    if (activity instanceof HomeActivity) {
                                        activity.replaceFragment(WebViewFragment.newInstance(postResponse.getText()), true);
                                    }
                                }
                            }
                        });
                        if (postResponse.getPostMeta() != null && postResponse.getPostMeta().size() != 0) {
                            if (!TextUtils.isEmpty(postResponse.getPostMeta().get(0).getLink())) {
                                banner = postResponse.getPostMeta().get(0).getLink();
                            } else {
                                banner = "";
                            }
                        }
                        profile = postResponse.getProfilePicture();

                    }
                    // end of update profile picture
                    //      start   for   location
                    else if(postResponse.getType().equals("2"))
                    {
                        singlePostviewAllHolder.imgBoaring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.tv_totalboring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.imgBookmark.setVisibility(View.GONE);
                        singlePostviewAllHolder.tvFollow.setVisibility(View.GONE);
                        singlePostviewAllHolder.imgShare.setVisibility(View.VISIBLE);

//                           if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {
//                               String tag = "";
//                               if (postResponse.getTaggedPeople().size() > 1) {
//
//
//                                   tag =  " Check In " + postResponse.getLocation()+" is with "  + postResponse.getTaggedPeople().get(0).getName() + " & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");
//
//                               } else {
//                                   tag = " Check In " + postResponse.getLocation()+" is with " + postResponse.getTaggedPeople().get(0).getName();
//                               }
//                               if (postResponse.getLocation() != null) {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +
//                                          tag) + " at location" +  postResponse.getLocation()
//                                   );
//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName() + tag));
//                               }
//
//                           } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName()+" Check In " + postResponse.getLocation());
//                           }


                        //                start of spanable string

                        //   start   of    a    spanable string

                        String tag = "";
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if (postResponse.getTaggedPeople().size() > 1) {
                                tag = " is with "  + postResponse.getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");

                            } else {
                                tag = " is with " + postResponse.getTaggedPeople().get(0).getName();
                            }
                            if (postResponse.getLocation() != null) {
                                if (postResponse.getLocation().equals("")) {
                                } else {
                                    tag = tag + " at location " + postResponse.getLocation();
                                }
                            }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

                        } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                            if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
                            {

                            }
                            else
                            {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                                tag= " at location "  + postResponse.getLocation();
                            }

                        }

                        if(postResponse.getFeeling()!=null && postResponse.getFeeling_status()!=null)
                        {
                            tag=tag+" "+postResponse.getFeeling()+" "+postResponse.getFeeling_status();
                        }


                        String text=postResponse.getUserName()+" Check In"+ tag;

                        SpannableString spannableString=new SpannableString(text);
                        ClickableSpan clickableSpan=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getTaggedPeople().get(0).getId(),null), true);
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
                                ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getTaggedPeople()), true);
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
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                            }

                            @Override
                            public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLACK);
                                ds.setUnderlineText(false);
                            }
                        };
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if(postResponse.getTaggedPeople().size()==1) {

                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }

                            else

                            {
                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan2, postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                        , postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                                +postResponse.getTaggedPeople().size()+4
                                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            }

                        }

                        else
                        {
                            spannableString.setSpan(clickableSpan3, 0,
                                    postResponse.getUserName().length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }

                        singlePostviewAllHolder.tvPageTitle.setText(spannableString);
                        singlePostviewAllHolder.tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




                        //  end of spanable string


                        //                end of spanable string
                        if (!TextUtils.isEmpty(postResponse.getText())) {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.VISIBLE);
                            // Bitmap bmp = decodeFile(profile);
                            if(postResponse.getText().length()>200) {
                                String seemore="SeeMore";
                                String texttt=postResponse.getText().substring(0,200)+seemore;
                                String fullstring=postResponse.getText()+"Seeless";
                                SpannableString spannableStrin = new SpannableString(texttt);
                                SpannableString spannableString8 = new SpannableString(fullstring);

                                ClickableSpan clickableSpann66=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableString8);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                ClickableSpan clickableSpann6=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                spannableStrin.setSpan(clickableSpann66, texttt.length()-7,
                                        texttt.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString8.setSpan(clickableSpann6, postResponse.getText().length(),
                                        postResponse.getText().length()+7,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);

                            }
                            else
                            {

                                singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                            }
                        }
                        else {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.GONE);
                        }
                        singlePostviewAllHolder.tvPageDescription.setMovementMethod(LinkMovementMethod.getInstance());

                        //  singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                        singlePostviewAllHolder.tvPageDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((postResponse.getText().contains("www")
                                        || postResponse.getText().contains("http"))
                                        && postResponse.getText().contains(".com")) {
                                    if (activity instanceof HomeActivity) {
                                        activity.replaceFragment(WebViewFragment.newInstance(postResponse.getText()), true);
                                    }
                                }
                            }
                        });
                        if(postResponse.getLatitude()!=null && postResponse.getLongitude()!=null)
                        {



                            String latEiffelTower = String.valueOf(postResponse.getLatitude());
                            String lngEiffelTower = String.valueOf(postResponse.getLongitude());
                            String url ="https://maps.googleapis.com/maps/api/staticmap?";
                            url+="&zoom=13";
                            url+="&size=600x300";
                            url+="&maptype=roadmap";
                            url+="&markers=color:green%7Clabel:G%7C"+latEiffelTower+","+lngEiffelTower;
                            url+="&key="+"AIzaSyAy9jwmpKuuNJdID26ChQADu0HofAWGZNc";
                            Log.e("response","==="+url);
                            banner =url;
                        }
                        List<PostMetum> postMetums=new ArrayList<>();
                        for(int l=0;l<1;l++)
                        {
                            PostMetum postMetum=new PostMetum();
                         //   postMetum.setViews(postResponse.getPostMeta().get(l).getViews());
                            postMetum.setLink(banner);
                         //   postMetum.setFilelink(postResponse.getPostMeta().get(l).getFilelink());
                          //  postMetum.setId(postResponse.getPostMeta().get(l).getId());
                            postMetum.setFileType("webview");
                          //  postMetum.setPostId(postResponse.getPostMeta().get(l).getPostId());
                          //  postMetum.setThumbnail(postResponse.getPostMeta().get(l).getThumbnail());
                            postMetums.add(postMetum);
                        }
                        postResponse.setPostMeta(postMetums);
                        profile = postResponse.getProfilePicture();
                    }
                    //   end of  location
                    else {
                        singlePostviewAllHolder.imgBoaring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.tv_totalboring.setVisibility(View.VISIBLE);
                        singlePostviewAllHolder.imgBookmark.setVisibility(View.GONE);
                        singlePostviewAllHolder.tvFollow.setVisibility(View.GONE);
                        singlePostviewAllHolder.imgShare.setVisibility(View.VISIBLE);
                        String tag = "";
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if (postResponse.getTaggedPeople().size() > 1) {
                                tag = " is with "  + postResponse.getTaggedPeople().get(0).getName() +" & " + String.valueOf((postResponse.getTaggedPeople().size() - 1) + " other");

                            } else {
                                tag = " is with " + postResponse.getTaggedPeople().get(0).getName();
                            }
                            if (postResponse.getLocation() != null) {
                                if (postResponse.getLocation().equals("")) {
                                } else {
                                    tag = tag + " at location " + postResponse.getLocation();
                                }
                            }

//                               } else {
//                                   singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml( postResponse.getUserName() +  tag));
//                               }

                        } else {
//                               singlePostviewAllHolder.tvPageTitle.setText(postResponse.getUserName());
                            if(postResponse.getLocation()==null||postResponse.getLocation().equals(""))
                            {

                            }
                            else
                            {
//                               singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + " at location"  + postResponse.getLocation()
//
//                               ));

                                tag= " at location "  + postResponse.getLocation();
                            }

                        }
                        if(postResponse.getFeeling()!=null && postResponse.getFeeling_status()!=null)
                        {
                            tag=tag+" "+postResponse.getFeeling()+" "+postResponse.getFeeling_status();
                        }




                        //   start   of    a    spanable string


                        String text=postResponse.getUserName() + tag;

                        SpannableString spannableString=new SpannableString(text);
                        ClickableSpan clickableSpan=new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getTaggedPeople().get(0).getId(),null), true);
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
                                ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getTaggedPeople()), true);
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
                                activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                            }

                            @Override
                            public void updateDrawState(@androidx.annotation.NonNull @NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.BLACK);
                                ds.setUnderlineText(false);
                            }
                        };
                        if (postResponse.getTaggedPeople() != null && postResponse.getTaggedPeople().size() != 0) {

                            if(postResponse.getTaggedPeople().size()==1) {

                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }

                            else

                            {
                                spannableString.setSpan(clickableSpan3, 0,
                                        postResponse.getUserName().length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan, postResponse.getUserName().length() + 8,
                                        postResponse.getUserName().length() + 8 + postResponse.getTaggedPeople().get(0).getName().length() + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString.setSpan(clickableSpan2, postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                        , postResponse.getUserName().length()+8+postResponse.getTaggedPeople().get(0).getName().length()+4
                                                +postResponse.getTaggedPeople().size()+4
                                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            }

                        }

                        else
                        {
                            spannableString.setSpan(clickableSpan3, 0,
                                    postResponse.getUserName().length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }

                        singlePostviewAllHolder.tvPageTitle.setText(spannableString);
                        singlePostviewAllHolder.tvPageTitle.setMovementMethod(LinkMovementMethod.getInstance());




                        //   end of a spanable string

//                           singlePostviewAllHolder.tvPageTitle.setText(Html.fromHtml(postResponse.getUserName()
//                                       + tag)) ;


                        if (!TextUtils.isEmpty(postResponse.getText())) {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.VISIBLE);
                            // Bitmap bmp = decodeFile(profile);
                            if(postResponse.getText().length()>200) {
                                String seemore="SeeMore";
                                String texttt=postResponse.getText().substring(0,200)+seemore;
                                String fullstring=postResponse.getText()+"Seeless";
                                SpannableString spannableStrin = new SpannableString(texttt);
                                SpannableString spannableString8 = new SpannableString(fullstring);

                                ClickableSpan clickableSpann66=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableString8);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                ClickableSpan clickableSpann6=new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View view) {
                                        singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.BLUE);
                                        ds.setUnderlineText(false);
                                    }
                                };

                                spannableStrin.setSpan(clickableSpann66, texttt.length()-7,
                                        texttt.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                spannableString8.setSpan(clickableSpann6, postResponse.getText().length(),
                                        postResponse.getText().length()+7,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                singlePostviewAllHolder.tvPageDescription.setText(spannableStrin);

                            }
                            else
                            {

                                singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                            }

                        }
                        else {
                            singlePostviewAllHolder.tvPageDescription.setVisibility(View.GONE);

                        }
                        singlePostviewAllHolder.tvPageDescription.setMovementMethod(LinkMovementMethod.getInstance());

                        //  singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getText()));
                        singlePostviewAllHolder.tvPageDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((postResponse.getText().contains("www")
                                        || postResponse.getText().contains("http"))
                                        && postResponse.getText().contains(".com")) {
                                    if (activity instanceof HomeActivity) {
                                        activity.replaceFragment(WebViewFragment.newInstance(postResponse.getText()), true);
                                    }
                                }
                            }
                        });
                        if (postResponse.getPostMeta() != null && postResponse.getPostMeta().size() != 0) {
                            if (!TextUtils.isEmpty(postResponse.getPostMeta().get(0).getLink())) {
                                banner = postResponse.getPostMeta().get(0).getLink();
                            } else {
                                banner = "";
                            }
                        }
                        profile = postResponse.getProfilePicture();
                    }
                }
                else {

                    if (postResponse.getIsFollow().equals("1"))
                        singlePostviewAllHolder.tvFollow.setText("Unfollow");
                    else{
                        singlePostviewAllHolder.tvFollow.setText("follow");
                    }
                    singlePostviewAllHolder.imgBoaring.setVisibility(View.GONE);
                    singlePostviewAllHolder.tv_totalboring.setVisibility(View.GONE);
                    singlePostviewAllHolder.imgBookmark.setVisibility(View.VISIBLE);
                    singlePostviewAllHolder.imgShare.setVisibility(View.GONE);
                    singlePostviewAllHolder.tvFollow.setVisibility(View.VISIBLE);
                    singlePostviewAllHolder.tvPageTitle.setText(postResponse.getTitle());
                    singlePostviewAllHolder.tvPageDescription.setText(Html.fromHtml(postResponse.getDescription()));
                    banner = postResponse.getBanner();
                    profile = postResponse.getProfile();
                    singlePostviewAllHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if ( postResponse.getIsFollow().equals("1")) {
                                singlePostviewAllHolder.likeDislikeApi(0,3);
                            }

                            else{
                                singlePostviewAllHolder.likeDislikeApi(1,3);
                            }
                        }
                    });

                    if(postResponse.getIsBookmark().equals("0"))
                    {
                        singlePostviewAllHolder.imgBookmark.setImageResource(R.drawable.bookmark_side);
                    }
                    else if(postResponse.getIsBookmark().equals("1"))
                    {
                        singlePostviewAllHolder.imgBookmark.setImageResource(R.drawable.bookmark);
                    }

                    singlePostviewAllHolder.imgBoaring.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (postResponse.getIsBookmark().equals("0")){

                                singlePostviewAllHolder.likeDislikeApi(1,2);

                            }else{
                                singlePostviewAllHolder.likeDislikeApi(0,2);
                            }
                        }
                    });
                }

                if (!TextUtils.isEmpty(profile)) {
//                    Bitmap bmp = decodeFile(profile);
                    Glide.with(context)
                            .load(profile)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(singlePostviewAllHolder.imgProfile);
                } else {
                    singlePostviewAllHolder.imgProfile.setImageResource(R.drawable.needyy);
                }

                if (!TextUtils.isEmpty(banner)) {
                    singlePostviewAllHolder.imgPost.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(banner)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy).apply(new RequestOptions().override(1000, 500)))
                            .thumbnail(Glide.with(context).load(banner))
                            .into(singlePostviewAllHolder.imgPost);
                } else {
                    singlePostviewAllHolder.imgPost.setVisibility(View.GONE);
                }

                singlePostviewAllHolder.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated()) * 1000));
                if (Integer.parseInt(postResponse.getLikes()) <= 1) {
                    singlePostviewAllHolder.tvTotalLike.setText(convertno(postResponse.getLikes()) + " " + context.getResources().getString(R.string.like));

                } else {
                    singlePostviewAllHolder.tvTotalLike.setText(convertno(postResponse.getLikes()) + " " + context.getResources().getString(R.string.likes));

                }

                if (Integer.parseInt(postResponse.getDislikes()) <= 1) {
                    singlePostviewAllHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes()) + " " + "Interesting");

                } else {
                    singlePostviewAllHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes()) + " " + "Interesting");

                }
                if (Integer.parseInt(postResponse.getBorings()) <= 1) {
                    singlePostviewAllHolder.tv_totalboring.setText(convertno(postResponse.getBorings()) + " " + "Boring");

                } else {
                    singlePostviewAllHolder.tv_totalboring.setText(convertno(postResponse.getBorings()) + " " + "Borings");
                }

                if (Integer.parseInt(postResponse.getComments()) <= 1) {
                    singlePostviewAllHolder.tvTotalComment.setText(convertno(postResponse.getComments()) + " " + context.getResources().getString(R.string.comment));
                } else {
                    singlePostviewAllHolder.tvTotalComment.setText(convertno(postResponse.getComments()) + " " + context.getResources().getString(R.string.comments));
                }

                if (postResponse.getMyVote().equals("1")) {
                    singlePostviewAllHolder.imgLike.setImageResource(R.drawable.like_active);
                    singlePostviewAllHolder.imgDislike.setImageResource(R.drawable.dislike);
                    singlePostviewAllHolder.imgBoaring.setImageResource(R.drawable.boring);
                } else if (postResponse.getMyVote().equals("2")) {
                    singlePostviewAllHolder.imgLike.setImageResource(R.drawable.like);
                    singlePostviewAllHolder.imgDislike.setImageResource(R.drawable.dislike_active);
                    singlePostviewAllHolder.imgBoaring.setImageResource(R.drawable.boring);
                } else if (postResponse.getMyVote().equals("3")) {
                    singlePostviewAllHolder.imgLike.setImageResource(R.drawable.like);
                    singlePostviewAllHolder.imgDislike.setImageResource(R.drawable.dislike);
                    singlePostviewAllHolder.imgBoaring.setImageResource(R.drawable.boring_active);
                } else if (postResponse.getMyVote().equals("0")) {
                    singlePostviewAllHolder.imgLike.setImageResource(R.drawable.like);
                    singlePostviewAllHolder.imgDislike.setImageResource(R.drawable.dislike);
                    singlePostviewAllHolder.imgBoaring.setImageResource(R.drawable.boring);
                }

                singlePostviewAllHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;
                        //Toast.makeText(activity,"inner1",Toast.LENGTH_SHORT).show();
                        if (postResponse.getPostType().equals("1")) {
                            activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                        } else {
                            activity.replaceFragment(mypage_details.newInstance(postResponsesList.get(i).getId()), true);
                        }
                    }
                });

                UserDataResult userDataResult = BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class);
                if (postResponsesList.get(i).getUserId().equals(userDataResult.getId()) &&
                        (postResponsesList.get(i).getPostType().equals("1") ||
                                postResponsesList.get(i).getPostType().equals("2"))) {
                    singlePostviewAllHolder.more.setVisibility(View.VISIBLE);
                    singlePostviewAllHolder.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow(v,i,singlePostviewAllHolder.ll_feeds,postResponsesList.get(i).getId());

//                            PopupMenu popup = new PopupMenu(context, singlePostviewAllHolder.more);
//                            popup.getMenuInflater()
//                                    .inflate(R.menu.home, popup.getMenu());
//                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                public boolean onMenuItemClick(MenuItem item) {
//                                    if(item.getTitle().equals("Delete"))
//                                    {
//                                        deletepost(postResponsesList.get(i).getId(),i, singlePostviewAllHolder.ll_feeds);
//                                    }
//                                    else if(item.getTitle().equals("Edit"))
//                                    {
//                                        PostResponse postResponse1=postResponsesList.get(i);
//                                        activity.replaceFragment(PostFragment.newInstance(postResponse1), true);
//                                    }
//                                    return true;
//                                }
//                            });
//                            popup.show();
                        }
                    });
                } else if (postResponsesList.get(i).getUserId().equals(userDataResult.getId()) &&
                        postResponsesList.get(i).getPostType().equals("3")) {
                    singlePostviewAllHolder.more.setVisibility(View.VISIBLE);
                    singlePostviewAllHolder.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindowdelete(v,i,singlePostviewAllHolder.ll_feeds);

//                            PopupMenu popup = new PopupMenu(context, singlePostviewAllHolder.more);
//                            popup.getMenuInflater()
//                                    .inflate(R.menu.home, popup.getMenu());
//                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                public boolean onMenuItemClick(MenuItem item) {
//                                  if(item.getTitle().equals("Delete"))
//                                  {
//                                      deletepost(postResponsesList.get(i).getId(),i,singlePostviewAllHolder.ll_feeds);
//                                  }
//                                  else if(item.getTitle().equals("Edit"))
//                                  {
//                                      PostResponse postResponse1=postResponsesList.get(i);
//                                      activity.replaceFragment(PostFragment.newInstance(postResponse1), true);
//                                  }
//                                    return true;
//                                }
//                            });

                            // popup.show();

                        }
                    });
                }
                else {
                    singlePostviewAllHolder.more.setVisibility(View.VISIBLE);
                    singlePostviewAllHolder.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow2(v,i,singlePostviewAllHolder.ll_feeds,postResponsesList.get(i).getId());
                        }
                    });
                }
                singlePostviewAllHolder.imgPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(context,"inner2",Toast.LENGTH_SHORT).show();
                        HomeActivity activity = (HomeActivity) context;
                        //ViewFullImageFragment.postresponse=postResponse;
                        if (postResponse.getPostType().equals("1")) {
                            activity.replaceFragment(ViewFullImageFragment.newInstance(postResponse), true);
                        }
                        else {
                            activity.replaceFragment(mypage_details.newInstance(postResponsesList.get(i).getId(), postResponsesList.get(i).getTitle(), postResponse.getUserId()), true);
                        }
                    }
                });
                System.gc();
                break;

            case 4:
            case 5:
            case 6:
                EducationViewHolder educationViewHolder = (EducationViewHolder) viewHolder;

                if (!TextUtils.isEmpty(postResponse.getProfilePicture())) {
                    Glide.with(context)
                            .load(postResponse.getProfilePicture())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(educationViewHolder.imgProfile);
                } else {
                    educationViewHolder.imgProfile.setImageResource(R.drawable.needyy);
                }
                if ((postResponse.getPostMeta()!=null && postResponse.getPostMeta().size()!=0)) {
                    educationViewHolder.imgPost.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(postResponse.getPostMeta().get(0).getLink())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.redio_button_active).error(R.drawable.redio_button_active))
                            .into(educationViewHolder.imgPost);
                }
                else {
                    educationViewHolder.imgPost.setVisibility(View.GONE);
                }

                educationViewHolder.bindContent(postResponse);
                educationViewHolder.tvStatustime.setText(CommonUtil.getDate(Long.parseLong(postResponse.getCreated())));
                educationViewHolder.tvRelationstatus.setText(postResponse.getText());
                educationViewHolder.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000));
               if(postResponse.getType().equals("6"))
               {
                   if(postResponse.getGender().equals("0"))
                   {
                       educationViewHolder.tvPageTitle.setText(postResponse.getUserName()+" Updated relationship status to "+postResponse.getText());

                   }
                   else if(postResponse.getGender().equals("1"))
                   {
                       educationViewHolder.tvPageTitle.setText(postResponse.getUserName()+" Updated his relationship status to "+postResponse.getText());

                   }
                   else if(postResponse.getGender().equals("2"))
                   {
                      // text=postResponse.getUserName()+ " Update her Cover Picture" + tag;
                       educationViewHolder.tvPageTitle.setText(postResponse.getUserName()+" Updated her relationship status to "+postResponse.getText());
                   }

               }
               else
               {
                   educationViewHolder.tvPageTitle.setText(postResponse.getUserName());
               }

                educationViewHolder.tvPageDescription.setText(postResponse.getText());

                if (Integer.parseInt(postResponse.getLikes())<=1){
                    educationViewHolder.tvTotalLike.setText(convertno(postResponse.getLikes())+" "+context.getResources().getString(R.string.like));

                }else{
                    educationViewHolder.tvTotalLike.setText(convertno(postResponse.getLikes())+" "+context.getResources().getString(R.string.likes));
                }

                if (Integer.parseInt(postResponse.getBorings())<=1){
                    educationViewHolder.tv_totalboring.setText(convertno(postResponse.getBorings())+" "+"Boring");

                }else{
                    educationViewHolder.tv_totalboring.setText(convertno(postResponse.getBorings())+" "+"Borings");
                }

                if (Integer.parseInt(postResponse.getDislikes())<=1){
                    educationViewHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes())+" "+"Interesting");

                }else{
                    educationViewHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes())+" "+"Interesting");
                }
                if (Integer.parseInt(postResponse.getComments())<=1){
                    educationViewHolder.tvTotalComment.setText(convertno(postResponse.getComments())+" "+context.getResources().getString(R.string.comment));
                }else{
                    educationViewHolder.tvTotalComment.setText(convertno(postResponse.getComments())+" "+context.getResources().getString(R.string.comments));
                }

                if (postResponse.getMyVote().equals("1")){
                    educationViewHolder.imgLike.setImageResource(R.drawable.like_active);
                    educationViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    educationViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }else if(postResponse.getMyVote().equals("2")){
                    educationViewHolder.imgLike.setImageResource(R.drawable.like);
                    educationViewHolder.imgDislike.setImageResource(R.drawable.dislike_active);
                    educationViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }else if(postResponse.getMyVote().equals("3")){
                    educationViewHolder.imgLike.setImageResource(R.drawable.like);
                    educationViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    educationViewHolder.imgBoaring.setImageResource(R.drawable.boring_active);
                }else if(postResponse.getMyVote().equals("0")){
                    educationViewHolder.imgLike.setImageResource(R.drawable.like);
                    educationViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    educationViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }

                educationViewHolder.llPostProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;
                        if (postResponse.getPostType().equals("1")) {
                            activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                        }
                        else {
                            activity.replaceFragment(mypage_details.newInstance(postResponsesList.get(i).getId()), true);
                        }
                    }
                });

                educationViewHolder.imgPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;
                        if (postResponse.getPostMeta() != null && postResponse.getPostMeta().size() != 0)
                        {

                        }
                           // Toast.makeText(context,"inner",Toast.LENGTH_SHORT).show();
                        // activity.replaceFragment(ViewFullImageFragment.newInstance(postResponse.getPostType().equals("1")?postResponse.getPostMeta().get(0).getLink():postResponse.getBanner()), true);
                    }
                });
                break;
            case 9:
                SharePostViewHolder sharePostViewHolder = (SharePostViewHolder) viewHolder;
                sharePostViewHolder.bindContent(postResponse);

                if(postResponse.getPostMeta()!=null && postResponse.getPostMeta().size()!=0)
                {
                    if(postResponse.getPostMeta().get(0).getFileType().equals("youtube")) {
                        sharePostViewHolder.playicon.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        sharePostViewHolder.playicon.setVisibility(View.GONE);
                    }
                }

                if (postResponse.getPostType().equals("4")) {
                    sharePostViewHolder.tvSharename.setText(postResponse.getSharesName());
                    sharePostViewHolder.imgBookmark.setVisibility(View.GONE);
                    sharePostViewHolder.tvFollow.setVisibility(View.GONE);
                    sharePostViewHolder.imgShare.setVisibility(View.VISIBLE);
                    sharePostViewHolder.tvPageTitle.setText(postResponse.getUserName());
                    sharePostViewHolder.tvPageDescription.setText(postResponse.getText());
                    sharePostViewHolder.tvShareTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getShareDate())*1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getShareDate())*1000));
                    sharePostViewHolder.tvShareDescription.setText(Html.fromHtml(postResponse.getCaption()));
                  //  sharePostViewHolder.tvSharename.setText(postResponse.getSharesName());
                }

                if (!TextUtils.isEmpty(postResponse.getSharesProfile())) {
                    Glide.with(context)
                            .load(postResponse.getSharesProfile())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(sharePostViewHolder.imgShareProfile);
                } else {
                    sharePostViewHolder.imgShareProfile.setImageResource(R.drawable.needyy);
                }
                if (!TextUtils.isEmpty(postResponse.getProfilePicture())) {
                    Glide.with(context)
                            .load(postResponse.getProfilePicture())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                            .into(sharePostViewHolder.imgProfile);
                } else {
                    sharePostViewHolder.imgProfile.setImageResource(R.drawable.needyy);
                }

                if (postResponse.getPostMeta()!=null && postResponse.getPostMeta().size()!=0){
                    if (!TextUtils.isEmpty(postResponse.getPostMeta().get(0).getLink())) {
                        Glide.with(context)
                                .load(postResponse.getPostMeta().get(0).getLink())
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                                .into(sharePostViewHolder.imgPost);
                    } else {
                        sharePostViewHolder.imgPost.setImageResource(R.drawable.needyy);
                    }
//                    sharePostViewHolder.imgPost.setVisibility(View.GONE);
                }

                sharePostViewHolder.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000));
                if (Integer.parseInt(postResponse.getLikes())<=1){
                    sharePostViewHolder.tvTotalLike.setText(convertno(postResponse.getLikes())+" "+context.getResources().getString(R.string.like));

                }else{
                    sharePostViewHolder.tvTotalLike.setText(convertno(postResponse.getLikes())+" "+context.getResources().getString(R.string.likes));
                }
                if (Integer.parseInt(postResponse.getComments())<=1){
                    sharePostViewHolder.tvTotalComment.setText(convertno(postResponse.getComments())+" "+context.getResources().getString(R.string.comment));

                }else{
                    sharePostViewHolder.tvTotalComment.setText(convertno(postResponse.getComments())+" "+context.getResources().getString(R.string.comments));
                }

                if (Integer.parseInt(postResponse.getComments())<=1){
                    sharePostViewHolder.tv_totalboring.setText(convertno(postResponse.getBorings())+" "+"Boring");

                }else{
                    sharePostViewHolder.tv_totalboring.setText(convertno(postResponse.getBorings())+" "+"Borings");
                }

                if (Integer.parseInt(postResponse.getComments())<=1){
                    sharePostViewHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes())+" "+"Interesting");
                }
                else{
                    sharePostViewHolder.tv_totaldislike.setText(convertno(postResponse.getDislikes())+" "+"Interesting");
                }

                if (postResponse.getMyVote().equals("1")){
                    sharePostViewHolder.imgLike.setImageResource(R.drawable.like_active);
                    sharePostViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    sharePostViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }else if(postResponse.getMyVote().equals("2")){
                    sharePostViewHolder.imgLike.setImageResource(R.drawable.like);
                    sharePostViewHolder.imgDislike.setImageResource(R.drawable.dislike_active);
                    sharePostViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }else if(postResponse.getMyVote().equals("3")){
                    sharePostViewHolder.imgLike.setImageResource(R.drawable.like);
                    sharePostViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    sharePostViewHolder.imgBoaring.setImageResource(R.drawable.boring_active);
                }else if(postResponse.getMyVote().equals("0")){
                    sharePostViewHolder.imgLike.setImageResource(R.drawable.like);
                    sharePostViewHolder.imgDislike.setImageResource(R.drawable.dislike);
                    sharePostViewHolder.imgBoaring.setImageResource(R.drawable.boring);
                }

                sharePostViewHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;
                      //  Toast.makeText(context,"inner",Toast.LENGTH_SHORT).show();
                        activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(i).getUserId(),null), true);
                    }
                });
                System.gc();
                break;
        }
    }

    private void deletepost(String id, int i,LinearLayout ll_feeds) {
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
                    postResponsesList.remove(i);
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

//    @Override
//    public void onBindViewHolder(@NonNull SearchPlaceeViewHolder viewHolder, final int position) {
//        PostResponse postResponse = postResponsesList.get(position);
//        viewHolder.bindContent(postResponse);
//        String banner="",profile="";
//        if (postResponse.getPostType().equals("1")){
//            viewHolder.imgBookmark.setVisibility(View.GONE);
//            viewHolder.tvFollow.setVisibility(View.GONE);
//            viewHolder.imgShare.setVisibility(View.VISIBLE);
//            viewHolder.tvPageTitle.setText(postResponse.getUserName());
//            viewHolder.tvPageDescription.setText(postResponse.getText());
//            if (postResponse.getPostMeta()!=null && postResponse.getPostMeta().size()!=0) {
//                if (!TextUtils.isEmpty(postResponse.getPostMeta().get(0).getLink())) {
//                    banner = postResponse.getPostMeta().get(0).getLink();
//                }else{
//                    banner = "";
//                }
//            }
//            profile =postResponse.getProfilePicture() ;
//        }else{
//            viewHolder.imgBookmark.setVisibility(View.VISIBLE);
//            viewHolder.imgShare.setVisibility(View.GONE);
//            viewHolder.tvFollow.setVisibility(View.VISIBLE);
//            viewHolder.tvPageTitle.setText(postResponse.getTitle());
//            viewHolder.tvPageDescription.setText(postResponse.getDescription());
//            banner = postResponse.getBanner();
//            profile =postResponse.getProfile() ;
//        }
//
//
//        if (!TextUtils.isEmpty(profile)) {
//            Glide.with(context)
//                    .load(profile)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
//                    .into(viewHolder.imgProfile);
//        } else {
//            viewHolder.imgProfile.setImageResource(R.drawable.needyy);
//        }
//
//        if (!TextUtils.isEmpty(banner)) {
//            Glide.with(context)
//                    .load(banner)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
//                    .into(viewHolder.imgPost);
//        } else {
//            viewHolder.imgPost.setImageResource(R.drawable.needyy);
//        }
////        }
////        else{
////            viewHolder.imgPost.setVisibility(View.GONE);
////        }
//
//
//        viewHolder.tvPageCreatedTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000).equals("0 minutes ago") ? "Just Now" : DateUtils.getRelativeTimeSpanString(Long.parseLong(postResponse.getCreated())*1000));
//        if (Integer.parseInt(postResponse.getLikes())<=1){
//            viewHolder.tvTotalLike.setText(postResponse.getLikes()+" "+context.getResources().getString(R.string.like));
//
//        }else{
//            viewHolder.tvTotalLike.setText(postResponse.getLikes()+" "+context.getResources().getString(R.string.likes));
//
//        }
//        if (Integer.parseInt(postResponse.getComments())<=1){
//            viewHolder.tvTotalComment.setText(postResponse.getComments()+" "+context.getResources().getString(R.string.comment));
//
//        }else{
//            viewHolder.tvTotalComment.setText(postResponse.getComments()+" "+context.getResources().getString(R.string.comments));
//
//        }
//
//        if (postResponse.getMyVote().equals("1")){
//            viewHolder.imgLike.setImageResource(R.drawable.like_active);
//            viewHolder.imgDislike.setImageResource(R.drawable.dislike);
//            viewHolder.imgBoaring.setImageResource(R.drawable.boring);
//        }else if(postResponse.getMyVote().equals("2")){
//            viewHolder.imgLike.setImageResource(R.drawable.like);
//            viewHolder.imgDislike.setImageResource(R.drawable.dislike_active);
//            viewHolder.imgBoaring.setImageResource(R.drawable.boring);
//        }else if(postResponse.getMyVote().equals("3")){
//            viewHolder.imgLike.setImageResource(R.drawable.like);
//            viewHolder.imgDislike.setImageResource(R.drawable.dislike);
//            viewHolder.imgBoaring.setImageResource(R.drawable.boring_active);
//        }else if(postResponse.getMyVote().equals("0")){
//            viewHolder.imgLike.setImageResource(R.drawable.like);
//            viewHolder.imgDislike.setImageResource(R.drawable.dislike);
//            viewHolder.imgBoaring.setImageResource(R.drawable.boring);
//        }
//
//        viewHolder.llPostProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HomeActivity activity = (HomeActivity) context;
//                activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(position).getUserId()));
//            }
//        });
//
//        viewHolder.imgPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HomeActivity activity = (HomeActivity) context;
//                activity.replaceFragment(ViewFullImageFragment.newInstance(postResponse.getPostType().equals("1")?postResponse.getPostMeta().get(0).getLink():postResponse.getBanner()));
//            }
//        });
//        System.gc();
//    }

    @Override
    public int getItemCount() {
        return postResponsesList.size();
    }

    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PostResponse postResponse ;
        CircleImageView imgProfile;
        LinearLayout llPostProfile,ll_feeds;
        RelativeLayout more;
        private ImageView imgLike,imgDislike,imgBoaring,imgComment,imgShare,imgBookmark,imgPost,playicon;
        TextView views,tvPageTitle, tvPageCreatedTime ,tvPageDescription,tvTotalLike,tvTotalComment,tvFollow,tv_totalboring, tv_totaldislike;
        public SingleFeedsViewHolder(View itemView) {
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
            tvTotalLike.setOnClickListener(this);
            tvTotalComment.setOnClickListener(this);
            llPostProfile.setOnClickListener(this);
            tv_totalboring.setOnClickListener(this);
            tv_totaldislike.setOnClickListener(this);
        }

        void bindContent(PostResponse postResponse) {
            this.postResponse = postResponse;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

//                case R.id.img_page_banner:
//                    HomeActivity activity1 = (HomeActivity) context;
//                    Toast.makeText(context,"inner",Toast.LENGTH_SHORT).show();
//                    activity.replaceFragment(ViewFullImageFragment.newInstance(postResponsesList.get(getAdapterPosition()).getL()));
//                    break;
//                case R.id.ll_post_profile:
//                    HomeActivity activity = (HomeActivity) context;
//                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId()));
//                    break;
                case R.id.tv_totaldislike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"2"), true);
                    break;
                case R.id.tv_totalboring:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"3"), true);
                    break;
                case R.id.img_pageprofile:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId(),null), true);
                    break;
                case R.id.tv_totallike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"1"), true);
                    break;
                case R.id.tv_totalcomment:
                    break;
                case R.id.img_like:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("1"))
                        likeDislikeApi(1,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_dislike:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("2"))
                        likeDislikeApi(2,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_boaring:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("3"))
                        likeDislikeApi(3,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_comment:
                    ((HomeActivity)context).replaceFragment(CommentFragment.newInstance(postResponse), true);
                    break;

                case R.id.img_share:
                    ShareNowBottomSheet fragment = new ShareNowBottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("postid", Integer.parseInt(postResponse.getId()));
                    fragment.setArguments(args);
                    fragment.show(((HomeActivity)context).getSupportFragmentManager(), "TAG");
                    break;

                case R.id.img_bookmark:
                    if (postResponse.getIsBookmark().equals("0")){
                        likeDislikeApi(1,2);
                    }else{
                        likeDislikeApi(0,2);
                    }
                    break;
            }
        }

        private void likeDislikeApi(int status,int taskFor) {
            //   ((HomeActivity)context).showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.likeDislike(postResponse.getId(),status,postResponse.getPostType(),taskFor);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    //((HomeActivity)context).cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo myPage = response.body();
                    if (myPage.getStatus()) {

                        if(taskFor==3)
                        {
                            if (postResponsesList.get(getAdapterPosition()).getIsFollow().equals("1"))
                                postResponsesList.get(getAdapterPosition()).setIsFollow("0");
                            else{
                                postResponsesList.get(getAdapterPosition()).setIsFollow("1");
                            }
                        }
                        if (taskFor==1){
                            if (status==1){
                                postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) +1));
                                imgLike.setImageResource(R.drawable.like_active);
                                if(Integer.parseInt(postResponse.getBorings())>0)
                                {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())-1));
                                }
                                if(Integer.parseInt(postResponse.getDislikes())>0)
                                {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                                }
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            else if(status==2){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())-1));
                                }

                                postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike_active);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }else if(status==3){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring_active);
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                                }

                            }
                            else if(status==0){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1));
                                }
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            postResponse.setMyVote(String.valueOf(status));
                            postResponsesList.get(getAdapterPosition()).setMyVote(String.valueOf(status));

                        }else{
                            if (status==1){
                                imgBookmark.setImageResource(R.drawable.bookmark);
                                postResponse.setIsBookmark("1");
                            }
                            else if (status==0){
                                postResponse.setIsBookmark("0");
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        if (myPage.getMessage().equals("110110")){
                            ((HomeActivity)context).logout();

                        }
                        else{
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

    public class Globalpost extends RecyclerView.ViewHolder implements  View.OnClickListener
    {
        PostResponse postResponse ;
        TextView captiontext,postname;
        public Globalpost(@NonNull View itemView) {
            super(itemView);
            captiontext=itemView.findViewById(R.id.captiontext);
            postname=itemView.findViewById(R.id.postname);
            postname.setOnClickListener(this);
        }

        void bindContentGlobal(PostResponse postResponse) {
            this.postResponse = postResponse;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.postname:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId(),null), true);
            }
        }
    }

    public class EducationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PostResponse postResponse ;
        CircleImageView imgProfile,imgPost;
        LinearLayout llPostProfile;
        private ImageView imgLike,imgDislike,imgBoaring,imgComment,imgShare,imgBookmark;
        TextView tvPageTitle, tvPageCreatedTime ,tvPageDescription,tvTotalLike,tvTotalComment,tvFollow,tvRelationstatus,
                tvStatustime,tv_totaldislike,tv_totalboring;
        public EducationViewHolder(View itemView) {
            super(itemView);
            tv_totaldislike     =itemView.findViewById(R.id.tv_totaldislike);
            tv_totalboring       =itemView.findViewById(R.id.tv_totalboring);
            tvStatustime        = itemView.findViewById(R.id.tv_status_time);
            tvRelationstatus    = itemView.findViewById(R.id.tv_relation_status);
            imgProfile          = itemView.findViewById(R.id.img_pageprofile);
            imgPost             = itemView.findViewById(R.id.img_relationship);
            tvPageTitle         = itemView.findViewById(R.id.tv_page_title);
            tvPageCreatedTime   = itemView.findViewById(R.id.tv_pagecreation_time);
            tvPageDescription   = itemView.findViewById(R.id.tv_page_description);
            tvFollow            = itemView.findViewById(R.id.tv_follow);
            tvTotalLike         = itemView.findViewById(R.id.tv_totallike);
            tvTotalComment      = itemView.findViewById(R.id.tv_totalcomment);
            imgLike             = itemView.findViewById(R.id.img_like);
            imgDislike          = itemView.findViewById(R.id.img_dislike);
            imgBoaring          = itemView.findViewById(R.id.img_boaring);
            imgComment          = itemView.findViewById(R.id.img_comment);

            imgShare            = itemView.findViewById(R.id.img_share);
            imgBookmark         = itemView.findViewById(R.id.img_bookmark);
            llPostProfile       = itemView.findViewById(R.id.ll_post_profile);

            imgLike    .setOnClickListener(this);
            imgDislike .setOnClickListener(this);
            imgBoaring .setOnClickListener(this);
            imgComment .setOnClickListener(this);

            imgShare   .setOnClickListener(this);
            imgBookmark.setOnClickListener(this);
            imgProfile .setOnClickListener(this);
            tvFollow   .setOnClickListener(this);
            tvTotalLike.setOnClickListener(this);
            tvTotalComment.setOnClickListener(this);
            llPostProfile.setOnClickListener(this);
            tv_totalboring.setOnClickListener(this);
            tv_totaldislike.setOnClickListener(this);
        }

        void bindContent(PostResponse postResponse) {
            this.postResponse = postResponse;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
//                case R.id.img_page_banner:
//                    HomeActivity activity = (HomeActivity) context;
//                    activity.replaceFragment(ViewFullImageFragment.newInstance(postResponsesList.get(getAdapterPosition()).getL()));
//                    break;
//                case R.id.ll_post_profile:
//                    HomeActivity activity = (HomeActivity) context;
//                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId()));
//                    break;
                case R.id.tv_totaldislike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"2"), true);
                    break;
                case R.id.tv_totalboring:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"3"), true);
                    break;
                case R.id.img_pageprofile:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId(),null), true);
                    break;
                case R.id.tv_follow:
                    break;
                case R.id.tv_totallike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"1"), true);
                    break;
                case R.id.tv_totalcomment:
                    break;
                case R.id.img_like:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("1"))
                        likeDislikeApi(1,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_dislike:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("2"))
                        likeDislikeApi(2,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_boaring:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("3"))
                        likeDislikeApi(3,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_comment:
                    ((HomeActivity)context).replaceFragment(CommentFragment.newInstance(postResponse), true);
                    break;

                case R.id.img_share:
                    ShareNowBottomSheet fragment = new ShareNowBottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("postid", Integer.parseInt(postResponse.getId()));
                    fragment.setArguments(args);
                    fragment.show(((HomeActivity)context).getSupportFragmentManager(), "TAG");
                    break;
                case R.id.img_bookmark:
                    if (postResponse.getIsBookmark().equals("0")){
                        likeDislikeApi(1,2);
                    }else{
                        likeDislikeApi(0,2);
                    }

                    break;
            }
        }

        private void likeDislikeApi(int status,int taskFor) {
            //  ((HomeActivity)context).showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.likeDislike(postResponse.getId(),status,postResponse.getPostType(),taskFor);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    //   ((HomeActivity)context).cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo myPage = response.body();
                    if (myPage.getStatus()) {

                        if (taskFor==1){
                            if (status==1){
                                postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) +1));
                                imgLike.setImageResource(R.drawable.like_active);
                                if(Integer.parseInt(postResponse.getBorings())>0)
                                {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())-1));
                                }
                                if(Integer.parseInt(postResponse.getDislikes())>0)
                                {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                                }
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            else if(status==2){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){

                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3"))
                                {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())-1));
                                }
                                postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike_active);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }else if(status==3){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings())+1));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring_active);
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1));
                                }

                            }
                            else if(status==0){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("2"))
                                {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3"))
                                {
                                    postResponsesList.get(getAdapterPosition()).setBorings(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1));
                                }
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            postResponse.setMyVote(String.valueOf(status));
                            postResponsesList.get(getAdapterPosition()).setMyVote(String.valueOf(status));

                        }else{
                            if (status==1){
                                imgBookmark.setImageResource(R.drawable.bookmark);
                                postResponse.setIsBookmark("1");
                            }else if (status==0){
                                postResponse.setIsBookmark("0");
                            }
                        }
                        notifyDataSetChanged();
                    }else {
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

    public class SharePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PostResponse postResponse ;
        CircleImageView imgProfile, imgShareProfile;
        LinearLayout llPostProfile;
        private ImageView imgLike,imgDislike,imgBoaring,imgComment,imgShare,imgBookmark,imgPost,playicon;
        TextView tvPageTitle, tvPageCreatedTime ,tvPageDescription,tvTotalLike,tvTotalComment,tvFollow,tvRelationstatus,tvStatustime,tvShareTime,
                tvShareDescription,tvSharename,tv_totaldislike,tv_totalboring;
        public SharePostViewHolder(View itemView) {
            super(itemView);
            playicon=itemView.findViewById(R.id.playicon);
            tv_totalboring      =itemView.findViewById(R.id.tv_totalboring);
            tv_totaldislike     =itemView.findViewById(R.id.tv_totaldislike);
            imgShareProfile     = itemView.findViewById(R.id.img_shareuser);
            tvShareTime         = itemView.findViewById(R.id.tv_share_time);
            tvShareDescription  = itemView.findViewById(R.id.tv_share_description);
            tvSharename         = itemView.findViewById(R.id.tv_share_name);
            tvStatustime        = itemView.findViewById(R.id.tv_status_time);
            imgProfile          = itemView.findViewById(R.id.img_pageprofile);
            imgPost             = itemView.findViewById(R.id.img_page_banner);
            tvPageTitle         = itemView.findViewById(R.id.tv_page_title);
            tvPageCreatedTime   = itemView.findViewById(R.id.tv_pagecreation_time);
            tvPageDescription   = itemView.findViewById(R.id.tv_page_description);
            tvFollow            = itemView.findViewById(R.id.tv_follow);
            tvTotalLike         = itemView.findViewById(R.id.tv_totallike);
            tvTotalComment      = itemView.findViewById(R.id.tv_totalcomment);
            imgLike             = itemView.findViewById(R.id.img_like);
            imgDislike          = itemView.findViewById(R.id.img_dislike);
            imgBoaring          = itemView.findViewById(R.id.img_boaring);
            imgComment          = itemView.findViewById(R.id.img_comment);

            imgShare            = itemView.findViewById(R.id.img_share);
            imgBookmark         = itemView.findViewById(R.id.img_bookmark);
            llPostProfile       = itemView.findViewById(R.id.ll_post_profile);
            tv_totalboring.setOnClickListener(this);
            tv_totaldislike.setOnClickListener(this);
            imgLike    .setOnClickListener(this);
            imgDislike .setOnClickListener(this);
            imgBoaring .setOnClickListener(this);
            imgComment .setOnClickListener(this);
            imgPost.setOnClickListener(this);
            imgShare   .setOnClickListener(this);
            imgBookmark.setOnClickListener(this);
            imgProfile .setOnClickListener(this);
            tvFollow   .setOnClickListener(this);
            tvTotalLike.setOnClickListener(this);
            tvTotalComment.setOnClickListener(this);
            llPostProfile.setOnClickListener(this);
            imgShareProfile.setOnClickListener(this);
            tvSharename.setOnClickListener(this);

        }

        void bindContent(PostResponse postResponse) {
            this.postResponse = postResponse;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_totaldislike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"2"), true);
                    break;
                case R.id.tv_totalboring:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"3"), true);
                    break;

                case R.id.img_page_banner:
                    HomeActivity activity1 = (HomeActivity) context;
                    if(postResponse.getPostMeta()!=null) {
                        if(postResponse.getPostMeta().get(0).getFileType().equals("youtube"))
                        {
                            Intent intent=new Intent(context,Vedio.class);
                            intent.putExtra("vedio_id",postResponse.getPostMeta().get(0).getFilelink());
                            context.startActivity(intent);
                        }
                        else {
                            activity1.replaceFragment(ViewFullImageFragment.newInstance(postResponse), true);
                        }
                    }
                    break;
//                case R.id.ll_post_profile:
//                    HomeActivity activity = (HomeActivity) context;
//                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId()));
//                    break;
                case R.id.img_pageprofile:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId(),null), true);
                    break;
                case R.id.tv_follow:
                    break;
                case R.id.tv_totallike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponsesList.get(getAdapterPosition()).getId(),"1"), true);
                    break;
                case R.id.tv_totalcomment:
                    break;
                case R.id.img_like:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("1"))
                        likeDislikeApi(1,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_dislike:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("2"))
                        likeDislikeApi(2,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_boaring:
                    if (! postResponsesList.get(getAdapterPosition()).getMyVote().equals("3"))
                        likeDislikeApi(3,1);
                    else{
                        likeDislikeApi(0,1);
                    }
                    break;
                case R.id.img_comment:
                    if(postResponse.getComments().equals("0"))

                    ((HomeActivity)context).replaceFragment(CommentFragment.newInstance(postResponse), true);
                    break;

                case R.id.img_share:
                    ShareNowBottomSheet fragment = new ShareNowBottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("postid", Integer.parseInt(postResponse.getId()));
                    fragment.setArguments(args);
                    fragment.show(((HomeActivity)context).getSupportFragmentManager(), "TAG");
                    break;
                case R.id.img_bookmark:
                    if (postResponse.getIsBookmark().equals("0")){
                        likeDislikeApi(1,2);
                    }else{
                        likeDislikeApi(0,2);
                    }
                case R.id.img_shareuser:
                case R.id.tv_share_name:
                    ((HomeActivity) context).replaceFragment(ViewProfileFragment.newInstance(postResponsesList.get(getAdapterPosition()).getUserId(),null), true);
                    break;
            }
        }

        private void likeDislikeApi(int status,int taskFor) {
            //((HomeActivity)context).showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.likeDislike(postResponse.getId(),status,postResponse.getPostType(),taskFor);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    //    ((HomeActivity)context).cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo myPage = response.body();
                    if (myPage.getStatus()) {

                        if (taskFor==1){
                            if (status==1){
                                postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) +1)));
                                imgLike.setImageResource(R.drawable.like_active);
                                if(Integer.parseInt(postResponse.getBorings())>0) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())-1)));
                                }
                                if(Integer.parseInt(postResponse.getDislikes())>0) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1)));
                                }
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            else if(status==2){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){

                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())-1)));
                                }
                                postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1)));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike_active);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }else if(status==3){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())+1)));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring_active);
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1)));
                                }

                            }
                            else if(status==0){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1)));
                                }
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            postResponse.setMyVote(String.valueOf(status));
                            postResponsesList.get(getAdapterPosition()).setMyVote(String.valueOf(status));

                        }else{
                            if (status==1){
                                imgBookmark.setImageResource(R.drawable.bookmark);
                                postResponse.setIsBookmark("1");
                            }else if (status==0){
                                postResponse.setIsBookmark("0");
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
                    //  ((HomeActivity)context).cancelProgressDialog();
                    ((HomeActivity)context).snackBar(t.getMessage());
                }
            });
        }
    }
    private Bitmap decodeFile(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        destFile = new File(file, "img_"+".png");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    void showPopupWindow(View view, int i, LinearLayout ll_feeds,String id) {
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
        popup.getMenuInflater().inflate(R.menu.home, popup.getMenu());
        if (!(postResponsesList.get(i).getBoostid()!=null && postResponsesList.get(i).getBoostid().equals("0")))
            popup.getMenu().findItem(R.id.Boost).setVisible(false);
        else
            popup.getMenu().findItem(R.id.Boost).setVisible(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Delete")) {
                    deletepost(postResponsesList.get(i).getId(),i,ll_feeds);

                }
                else if(item.getTitle().equals("Edit")) {
                    HomeActivity activity = (HomeActivity) context;
                    PostResponse postResponse1=postResponsesList.get(i);
                    activity.replaceFragment(PostFragment.newInstance(postResponse1), true);
                }
                else if(item.getTitle().equals("Boost")) {
                    PostResponse postResponse1=postResponsesList.get(i);
                    postdetails(postResponse1);
                }
                else if(item.getItemId()==R.id.share) {
                    //Toast.makeText(context,"Share",Toast.LENGTH_SHORT).show();

                    //SaveImage(postResponsesList.get(i).getPostMeta().get(0).getLink());

                    generateLink(id);
                }
                return true;
            }
        });
        popup.show();
    }

    private void generateLink(String id){

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()

                .setLink(Uri.parse("https://www.needyyy.com/forward?postId=" + id))
                .setDynamicLinkDomain("needyyy.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String msgHtml = String.format("<p>Let's check  App."
                                    + "<a href=\"%s\">Click Here</a>!</p>", shortLink.toString());

                            String msg = "Let's check  App.. Click on Link : " + shortLink.toString();

                            open((Activity) context,msg,msgHtml);


                        } else {
                            Log.d("TASK_EXCEPTION", task.getException().toString());
                            Toast.makeText(context, "Link could not be generated please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void open(Activity activity, String link,String msgHTML){
        String shareBody = link;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHTML);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
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

    void showPopupWindow2(View view, int i, LinearLayout ll_feeds,String id) {
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
        popup.getMenuInflater().inflate(R.menu.homedel, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                PopupWindow popupWindow;
                LinearLayout linearLayout1;
                if(item.getTitle().equals("Report"))
                {
                    showReportPostView(i);

//                    //instantiate the popup.xml layout file
//                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View customView = layoutInfla
//                    ter.inflate(R.layout.poupwindow,null);
//
//
//                    //instantiate popup window
//                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    //display the popup window
//                    popupWindow.showAtLocation(ll_feeds, Gravity.CENTER, 0, 0);
//
//                    //close the popup window on button click
////                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            popupWindow.dismiss();
////                        }
////                    });


                }

                if(item.getTitle().equals("Share"))
                {
                    generateLink(id);
                }

                return true;
            }
        });
        popup.show();
    }


    void showPopupWindowdelete(View view, int i, LinearLayout ll_feeds) {
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
        popup.getMenuInflater().inflate(R.menu.delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Disable")) {
                    deletepost(postResponsesList.get(i).getId(),i,ll_feeds);
                }
                return true;
            }
        });
        popup.show();
    }

    private void showReportPostView(int i) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = li.inflate(R.layout.poupwindow, null, false);
        final Dialog reportPost = new Dialog(context);
        reportPost.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportPost.setCanceledOnTouchOutside(false);
        reportPost.setContentView(v);
        reportPost.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reportPost.show();

        RecyclerView reportPostList;
        CardView submitCard, upperCard;
        ImageView cross;
        RelativeLayout bottomCardReportSubmitt;
        EditText writefeedbackET;

        reportPostList = v.findViewById(R.id.reasonListReport);
        submitCard = v.findViewById(R.id.bottomCard);
        upperCard = v.findViewById(R.id.upperCard);
        cross = v.findViewById(R.id.crossimageIV);
        bottomCardReportSubmitt=v.findViewById(R.id.bottomCardReportSubmit);
        writefeedbackET = v.findViewById(R.id.writefeedbackET);

        if (reportPostList.getVisibility() == View.GONE) {
            reportPostList.setVisibility(View.VISIBLE);
            writefeedbackET.setVisibility(View.VISIBLE);
            upperCard.setVisibility(View.VISIBLE);
        }
        Data masterHitData = (BaseManager.getMasterHitData(Kmasterhit, Data.class));
        ArrayList<ReportPost> reportPosts= (ArrayList<ReportPost>) masterHitData.getReportPost();
        reportPostList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ReportAbuseListAdapter reportAbuseListAdapter = new ReportAbuseListAdapter(context,reportPosts);
        reportPostList.setAdapter(reportAbuseListAdapter);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPost.dismiss();
            }
        });
        submitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postResponsesList.get(i).getId()==null||postResponsesList.get(i).getId().equals("")) {
                    Toast.makeText(context,"postid is null",Toast.LENGTH_SHORT).show();
                }
                else if(Reportid.equals("")||Reportid==null) {
                    Toast.makeText(context,"Report is null",Toast.LENGTH_SHORT).show();

                }
                else if(writefeedbackET.equals("")||writefeedbackET.getText()==null) {
                    Toast.makeText(context," please fill feedback is null",Toast.LENGTH_SHORT).show();
                }
                else {
                    hitapi(i);
                }
            }

            private void hitapi(int i) {

                if (CommonUtil.isConnectingToInternet(context)){

                    WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
                    Call<CommonPojo> call = Service.ReportPost(postResponsesList.get(i).getId(),Reportid,writefeedbackET.getText().toString());
                    call.enqueue(new Callback<CommonPojo>() {
                        @Override
                        public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {

                            CommonPojo commonPojo=response.body();
                            if(commonPojo.getStatus()) {
                                Toast.makeText(context,commonPojo.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                reportPost.dismiss();
                            }
                            else {
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonPojo> call, Throwable t) {
                        }
                    });
                }else{
                }
            }
        });
    }
    public static void getid(String id) {
        Reportid=id;
    }

    public static String convertno(String no)
    {
//        String str=null;
//        int noo =Integer.parseInt(no);
//        if(noo>1000 && noo<=100000){
//            str= (noo/1000)+" K";
//        }else if(noo>100000 && noo<=10000000){
//            str= (noo/100000)+" M";
//        }else if(noo>10000000 && noo<=1000000000){
//            str= (noo/10000000)+" B";
//        }
//        else
//        {
//            str=String.valueOf(noo);
//        }
//        return str;
        String str = null;
        int count=no.length();
        if(count==5)
        {
            if(Character.getNumericValue(no.charAt(2))==0)
            {
                str = Character.getNumericValue(no.charAt(0))+""+ Character.getNumericValue(no.charAt(1))  + "K";
            }
            else
                {
                str = Character.getNumericValue(no.charAt(0)) +""+ Character.getNumericValue(no.charAt(1)) + "." + Character.getNumericValue(no.charAt(2)) + "K";
                }
        }
        else if(count==4)
        {
            if(Character.getNumericValue(no.charAt(1))>0)
            {
                str=Character.getNumericValue(no.charAt(0))+"."+Character.getNumericValue(no.charAt(1))+"K";
            }
            else
            {
                str=Character.getNumericValue(no.charAt(0))+"K";
            }
        }
        else if(count<4)
        {
            str=no;
        }
        else if(count==6) {
            if (Character.getNumericValue(no.charAt(3)) == 0) {
                str = Character.getNumericValue(no.charAt(0)) +""+ Character.getNumericValue(no.charAt(1)) + "M";
            } else {
                str = Character.getNumericValue(no.charAt(0)) +""+Character.getNumericValue(no.charAt(1)) + "." + Character.getNumericValue(no.charAt(3)) + "M";
            }
        }
        return str;
    }


    // pagepost

    public class Pagepost extends RecyclerView.ViewHolder implements View.OnClickListener {

        PostResponse postResponse ;
        CircleImageView imgProfile;
        LinearLayout llPostProfile,ll_feeds;
        RelativeLayout more;
        private ImageView imgLike,imgDislike,imgBoaring,imgComment,imgShare,imgBookmark,imgPost,playicon;
        TextView views,tvPageTitle, tvPageCreatedTime ,tvPageDescription,tvTotalLike,tvTotalComment,tvFollow,tv_totalboring, tv_totaldislike;
        public Pagepost(View itemView) {
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

            imgBookmark = itemView.findViewById(R.id.img_bookmark);
            llPostProfile = itemView.findViewById(R.id.ll_post_profile);



            imgLike.setOnClickListener(this);
            imgDislike.setOnClickListener(this);
            imgBoaring.setOnClickListener(this);
            imgComment.setOnClickListener(this);
            imgBookmark.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
            tvFollow.setOnClickListener(this);
            tvTotalLike.setOnClickListener(this);
            tvTotalComment.setOnClickListener(this);
            llPostProfile.setOnClickListener(this);
            tv_totalboring.setOnClickListener(this);
            tv_totaldislike.setOnClickListener(this);


        }

        void bindContentpagepost(PostResponse postResponse) {
            this.postResponse = postResponse;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_totaldislike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getId(),"2"), true);
                    break;
                case R.id.tv_totalboring:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getId(),"3"), true);
                    break;
                case R.id.img_pageprofile:
                    HomeActivity activity = (HomeActivity) context;
                    activity.replaceFragment(ViewProfileFragment.newInstance(postResponse.getUserId(), null), true);
                    break;
                case R.id.tv_follow:
                    break;
                case R.id.tv_totallike:
                    ((HomeActivity) context).replaceFragment(UserListFragment.newInstance(postResponse.getId(),"1"), true);
                    break;
                case R.id.tv_totalcomment:
                    break;
                case R.id.img_like:
                    if (!postResponse.getMyVote().equals("1"))
                        likeDislikeApi(1, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_dislike:
                    if (!postResponse.getMyVote().equals("2"))
                        likeDislikeApi(2, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_boaring:
                    if (!postResponse.getMyVote().equals("3"))
                        likeDislikeApi(3, 1);
                    else {
                        likeDislikeApi(0, 1);
                    }
                    break;
                case R.id.img_comment:
                    ((HomeActivity)context).replaceFragment(CommentFragment.newInstance(postResponse), true);
                    break;
                case R.id.img_bookmark:
                    if (postResponse.getIsBookmark().equals("0")) {
                        likeDislikeApi(1, 2);
                    } else {
                        likeDislikeApi(0, 2);
                    }
            }
        }

        private void likeDislikeApi(int status,int taskFor) {
            //((HomeActivity)context).showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommonPojo> call = Service.likeDislike(postResponse.getId(),status,postResponse.getPostType(),taskFor);
            call.enqueue(new Callback<CommonPojo>() {
                @Override
                public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                    //    ((HomeActivity)context).cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommonPojo myPage = response.body();
                    if (myPage.getStatus()) {



                        if (taskFor==1){
                            if (status==1){
                                postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) +1)));
                                imgLike.setImageResource(R.drawable.like_active);
                                if(Integer.parseInt(postResponse.getBorings())>0) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())-1)));
                                }
                                if(Integer.parseInt(postResponse.getDislikes())>0) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1)));
                                }
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            else if(status==2){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){

                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())-1)));
                                }
                                postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())+1)));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike_active);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }else if(status==3){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings())+1)));
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring_active);
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes())-1)));
                                }

                            }
                            else if(status==0){
                                if ( postResponsesList.get(getAdapterPosition()).getMyVote().equals("1")){
                                    postResponsesList.get(getAdapterPosition()).setLikes(convertno(String.valueOf(Integer.parseInt(postResponse.getLikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("2")) {
                                    postResponsesList.get(getAdapterPosition()).setDislikes(convertno(String.valueOf(Integer.parseInt(postResponse.getDislikes()) -1)));
                                }
                                if(postResponsesList.get(getAdapterPosition()).getMyVote().equals("3")) {
                                    postResponsesList.get(getAdapterPosition()).setBorings(convertno(String.valueOf(Integer.parseInt(postResponse.getBorings()) -1)));
                                }
                                imgLike.setImageResource(R.drawable.like);
                                imgDislike.setImageResource(R.drawable.dislike);
                                imgBoaring.setImageResource(R.drawable.boring);
                            }
                            postResponse.setMyVote(String.valueOf(status));
                            postResponsesList.get(getAdapterPosition()).setMyVote(String.valueOf(status));

                        }else{
                            if (status==1){
                                imgBookmark.setImageResource(R.drawable.bookmark);
                                postResponse.setIsBookmark("1");
                            }else if (status==0){
                                postResponse.setIsBookmark("0");
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
                    //  ((HomeActivity)context).cancelProgressDialog();
                    ((HomeActivity)context).snackBar(t.getMessage());
                }
            });
        }
    }

    //  end of page post
}
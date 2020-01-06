package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Activities.TagSelection;
import com.needyyy.app.Modules.Home.Adapters.CommentAdapter;
import com.needyyy.app.Modules.Home.callback.CommentCallback;
import com.needyyy.app.Modules.Home.modle.CommentBase;
import com.needyyy.app.Modules.Home.modle.CommentData;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import de.measite.minidns.record.A;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class CommentFragment extends BaseFragment implements View.OnClickListener , CommentCallback {
    private RecyclerView rvComments;
    private String taggedpeopleid;
    private LinearLayoutManager linearLayoutManager ;
    private CommentAdapter commentAdapter ;
    private ArrayList<CommentData> commentDataList ;
    private AppTextView tvComment;
    private String commentId = "0";
    private PostResponse postresponse;
    EditText etComment;
    private CommentCallback commentCallback ;
    private CircleImageView circleImageProfile ;
    private String replytext="";
    ArrayList<People> taggedpeoplearrList;
    ArrayList<People> taggedpeople;
    UserDataResult userData ;
    private static String updateid;
    private static String check="";
    private String commentcount;

    public static CommentFragment newInstance(PostResponse postResponse) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
       args.putSerializable("postresponse",postResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_comment);
        if (getArguments() != null) {

           postresponse= (PostResponse) getArguments().getSerializable("postresponse");

        }
        taggedpeople=new ArrayList<>();
        taggedpeoplearrList=new ArrayList<>();
    }

    @Override
    protected void initView(View mView) {
        rvComments         = mView.findViewById(R.id.rv_comment);
        tvComment          = mView.findViewById(R.id.tv_comment);
        etComment          = mView.findViewById(R.id.et_comment);
        circleImageProfile = mView.findViewById(R.id.img_profile);
        commentDataList = new ArrayList<>();
         userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.comments), "2");
        tvComment.setOnClickListener(this);
        if(!postresponse.getComments().equals("0"))
        {
            getCommentApi();
        }
        etComment.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(s.length()>0) {
                    if (s.charAt(s.length() - 1) == '@') {
                        Intent tagUsers = new Intent(getActivity(), TagSelection.class);
                        tagUsers.putExtra(Constant.ALREADY_TAGGED_PEOPLE, taggedpeoplearrList);
                        getActivity().startActivityForResult(tagUsers, ((HomeActivity) getActivity()).TAG_USER2);
                    }
                    else
                    {

                    }
                }
            }
        });


        commentAdapter = new CommentAdapter(1,getActivity(),commentDataList ,this,postresponse.getUserId(),etComment);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(commentAdapter);
        if (!TextUtils.isEmpty(userData.getProfilePicture())) {
            Glide.with(getActivity())
                    .load(userData.getProfilePicture())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(circleImageProfile);
        } else {
            circleImageProfile.setImageResource(R.drawable.needyy);
        }

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comment:
                replytext="";
                if(taggedpeoplearrList.size()>0) {
                    String text=null;
                    String[] checkteggedpeople=etComment.getText().toString().split("@");
                    for(int k=1;k<checkteggedpeople.length;k++)
                    {
                        if(taggedpeoplearrList.size()>0)
                        {
                            for (int l=0;l<taggedpeoplearrList.size();l++) {
                                if(checkteggedpeople[k].equals(taggedpeoplearrList.get(l).getName()))
                                {
                                    taggedpeopleid = "," + taggedpeoplearrList.get(l).getId();
                                }
                                else
                                {
                                    taggedpeoplearrList.remove(l);
                                }
                            }
                        }
                    }
                }
                    taggedpeoplearrList.clear();
                if (etComment.getText().toString().length()!=0){
                    if(check.equals("1"))
                    {
                        postComment(etComment.getText().toString(),"0" ,updateid);
                    }
                    else {
                        postComment(etComment.getText().toString(), "0","");
                    }
                }
                else{
                    snackBar("comment field cant empty");
                }
                etComment.setText("");
                break;
        }
    }

    public void postComment(String comment, String cpmmentId,String id){
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.postComment(postresponse.getId(), comment,cpmmentId,id,taggedpeopleid);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {

                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo acceptRejectRequest = response.body();
                if (acceptRejectRequest.getStatus()) {
                    etComment.setText("");
                    taggedpeopleid="";
                    postresponse.setComments(String.valueOf(Integer.parseInt(postresponse.getComments())+1));
                    getCommentApi();
                } else {
                    if (acceptRejectRequest.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }
                    else{
                        ((HomeActivity)getActivity()).snackBar(acceptRejectRequest.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                //  cancelProgressDialog();
            }
        });

    }

    @Override
    public void postComments(String Comment, String cpmmentId) {
        ((HomeActivity)getActivity()).replaceFragment(CommentReplyFragment.newInstance(postresponse.getId(),cpmmentId,postresponse.
                getUserId()), true);
    }


    public static void update(String commentid, String checks)
    {
        updateid=commentid;
        check=checks;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==HomeActivity.TAG_USER2)
        {
            if (resultCode == RESULT_OK && data != null) {
                onAddPeopleTagList((ArrayList<People>) data.getExtras().getSerializable(Constant.ALREADY_TAGGED_PEOPLE));
//                Toast.makeText(activity, "You have tagged successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onAddPeopleTagList(ArrayList<People> taggedpeoplearrLists)
    {
        if (taggedpeoplearrList == null) taggedpeoplearrList = new ArrayList<>();
        if (taggedpeoplearrList.size() > 0) taggedpeoplearrList.clear();
        taggedpeople.addAll(taggedpeoplearrLists);
        taggedpeoplearrList.addAll(taggedpeoplearrLists);
        for(int i=0;i<taggedpeoplearrList.size();i++)
        {
            if(i==0)
            {
                replytext=replytext+"<b>"+taggedpeoplearrList.get(i).getName()+"</b>";
            }
            else
            {
                replytext=replytext+" @<b>"+taggedpeoplearrList.get(i).getName()+"</b>";
            }

        }
        etComment.setText(Html.fromHtml(etComment.getText()+(replytext)));
      //  etComment.setFocusable(true);
        replytext="";
    }
}

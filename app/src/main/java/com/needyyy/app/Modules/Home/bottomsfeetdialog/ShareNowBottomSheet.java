package com.needyyy.app.Modules.Home.bottomsfeetdialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Activities.TagSelection;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.constants.Constants.kCurrentUser;
import static com.needyyy.app.utils.CommonUtil.snackBar;

public class ShareNowBottomSheet extends BottomSheetDialogFragment {
    int postid ;
    private TextInputEditText etMessage;
    private LinearLayout llTag;
    private TextView shareNow,tvUserNAme;
    private CircleImageView imgProfile;
    ArrayList<People> taggedpeoplearrList;
    int isPostEdit = 0 ;
    ArrayList<String> oldids, newids;
    String taggedPeopleIdsAdded;
    List<View> LinearLayoutList, LinearLayoutIconList, taggedpeopleList;

    FlowLayout taggedpeopleFL;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_share_post, null);
        dialog.setContentView(view);
        postid           = getArguments().getInt("postid");
        etMessage        = view.findViewById(R.id.et_message);
        shareNow         = view.findViewById(R.id.btn_next);
        imgProfile       = view.findViewById(R.id.img_pageprofile);
        tvUserNAme       = view.findViewById(R.id.tv_username);
        llTag            = view.findViewById(R.id.lltag);
        taggedpeopleFL   = (FlowLayout) view.findViewById(R.id.taggedpeopleFL);
        LinearLayoutList = new ArrayList();
        taggedpeopleList = new ArrayList();
        LinearLayoutIconList = new ArrayList();
        UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        if (!TextUtils.isEmpty(userData.getProfilePicture())) {
            Glide.with(getContext())
                    .load(userData.getProfilePicture())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.needyy);
        }

        tvUserNAme.setText(userData.getName());
        shareNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shatePost();
            }
        });

        llTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tagUsers = new Intent(getActivity(), TagSelection.class);
                tagUsers.putExtra(Constant.ALREADY_TAGGED_PEOPLE, taggedpeoplearrList);
                startActivityForResult(tagUsers, ((HomeActivity) getActivity()).TAG_USER);
            }
        });
        Log.e("postid","======"+postid);
    }

    private void shatePost() {
        if (taggedpeoplearrList != null && taggedpeoplearrList.size() > 0) {
            for (People res : taggedpeoplearrList) {
                if (TextUtils.isEmpty(taggedPeopleIdsAdded))
                    taggedPeopleIdsAdded = ","+res.getId();
                else taggedPeopleIdsAdded = taggedPeopleIdsAdded + "," + res.getId();
            }
            taggedPeopleIdsAdded=taggedPeopleIdsAdded+",";
        }
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.sharePost(postid,"1",etMessage.getText().toString(),taggedPeopleIdsAdded);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
//               cancelProgressDialog();
//                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo commonPojo = response.body();
                if (commonPojo.getStatus()) {
                    CommonUtil.showLongToast(getActivity(),commonPojo.getMessage());
                    dismiss();
                } else {
                    snackBar(commonPojo.getMessage(),getActivity());
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {

            }
        });

    }
    /**
     *  get selected file in onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (((HomeActivity) getActivity()).TAG_USER == requestCode) {
            if (resultCode == RESULT_OK && data != null) {
                onAddPeopleTagList((ArrayList<People>) data.getExtras().getSerializable(Constant.ALREADY_TAGGED_PEOPLE));
//                Toast.makeText(activity, "You have tagged successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void onAddPeopleTagList(ArrayList<People> taggedpeoplearrLists) {

        if (taggedpeoplearrList == null) taggedpeoplearrList = new ArrayList<>();
        if (taggedpeoplearrList.size() > 0) taggedpeoplearrList.clear();
        taggedpeoplearrList.addAll(taggedpeoplearrLists);
        if (taggedpeopleFL.getChildCount() > 0) taggedpeopleFL.removeAllViews();

        for (int i = 0; i < taggedpeoplearrList.size(); i++) {
            People response = taggedpeoplearrList.get(i);
            if (isPostEdit != 0) {
                if (newids == null) newids = new ArrayList<>();
                newids.add(response.getId());
            }

            View v = View.inflate(getActivity(), R.layout.single_textview_people_tag, null);
            TextView tv = (TextView) v.findViewById(R.id.nameTV);
            ImageView delete = (ImageView) v.findViewById(R.id.deleteIV);
            tv.setText(response.getName());
            v.setTag(response);
            delete.setTag(response);
            final ArrayList<People> finalTaggedpeoplearrList = taggedpeoplearrList;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    People rep = (People) view.getTag();
                    int pos = finalTaggedpeoplearrList.indexOf(rep);
                    finalTaggedpeoplearrList.remove(rep);
                    taggedpeopleFL.removeViewAt(pos);
                    taggedpeopleList.remove(pos);
                    if (isPostEdit != 0) {
                        newids.remove(pos);
                    }
                }
            });

            taggedpeopleFL.addView(v);
            taggedpeopleList.add(v);
        }
    }

}
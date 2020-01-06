package com.needyyy.app.Modules.Home.Adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Home.callback.CommentCallback;
import com.needyyy.app.Modules.Home.modle.CommentBase;
import com.needyyy.app.Modules.Home.modle.CommentData;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.needyyy.app.Modules.Home.Fragments.CommentFragment.update;
import static com.needyyy.app.Modules.Home.Fragments.CommentReplyFragment.updatee;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.SingleFeedsViewHolder> {
    Context context;
    RelativeLayout more, rlContentTypes;
    private CommentAdapter commentAdapter ;
    private ArrayList<CommentData> commentDataList ;
    ArrayList<CommentData> data = new ArrayList<>();
    HomeFragment homeFragment;
    UserDataResult userData;
    private CommentCallback commentCallback ;
    private int type;
    private String postUserId;
    EditText editText;
    public CommentAdapter(int type, Context context, ArrayList<CommentData> data , CommentCallback commentCallback, String postUserId, EditText editText) {
        this.postUserId=postUserId;
        this.context = context;
        this.data    = data;
        this.type    = type ;
        this.commentCallback = commentCallback ;
        this.editText=editText;
    }

    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comments_layout, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder,final int position) {
        CommentData commentData = data.get(position);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, position,commentData.getId(),commentData.getUserId());

            }
        });
        if (userData.getId().equals(commentData.getUserId())||postUserId.equals(userData.getId())) {
            more.setVisibility(View.VISIBLE);
        } else {
            more.setVisibility(View.GONE);
        }
        if (Integer.parseInt(commentData.getReplycount()) > 0) {
            viewHolder.replycount.setText("View Replies" + " (" + commentData.getReplycount() + ")");
        } else {
            viewHolder.tvReply.setVisibility(View.VISIBLE);
            viewHolder.replycount.setVisibility(View.GONE);
        }
        viewHolder.tvUserName.setText(commentData.getName());
        viewHolder.tvLastseen.setText(commentData.getComment());
        if (!TextUtils.isEmpty(commentData.getProfilePicture())) {
            Glide.with(context)
                    .load(commentData.getProfilePicture())
                    .into(viewHolder.civProfile);
        } else {
            viewHolder.civProfile.setImageResource(R.drawable.needyy);
        }
        if (type == 1) {
            if (Integer.parseInt(commentData.getReplycount()) > 0) {
                viewHolder.tvReply.setVisibility(View.GONE);
            } else {
                viewHolder.tvReply.setVisibility(View.VISIBLE);
            }
        } else if (type == 2) {
            viewHolder.tvReply.setVisibility(View.GONE);
            viewHolder.replycount.setVisibility(View.GONE);
        }
        viewHolder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentCallback.postComments(commentData.getComment(), commentData.getId());
            }
        });
        viewHolder.replycount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentCallback.postComments(commentData.getComment(), commentData.getId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
//        return 10;
    }

    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civProfile ;
        TextView tvUserName,tvLastseen,tvReply,replycount,tvdelete, tvhide;
        private View footer;

        public SingleFeedsViewHolder(View itemView) {
            super(itemView);
            more=itemView.findViewById(R.id.more1);
            civProfile       = itemView.findViewById(R.id.img_profile);
            civProfile       = itemView.findViewById(R.id.img_profile);

            civProfile       = itemView.findViewById(R.id.img_profile);
            tvUserName       = itemView.findViewById(R.id.tv_username);
            tvLastseen       = itemView.findViewById(R.id.tv_lastseen);
            tvReply          = itemView.findViewById(R.id.tv_reply);
            footer           = itemView.findViewById(R.id.footer);
            replycount       = itemView.findViewById(R.id.replycount);
        }
    }

    private void deletecomment( String commentId,int type,int position) {
        if (CommonUtil.isConnectingToInternet(context)){

            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<CommentBase> call = Service.deletePostComment(commentId,type);
            call.enqueue(new Callback<CommentBase>() {
                @Override
                public void onResponse(Call<CommentBase> call, Response<CommentBase> response) {

                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    CommentBase commentBase = response.body();
                    if (commentBase.getStatus()) {

                    } else {
                        if (commentBase.getMessage().equals("110110")){
                            ((HomeActivity)context).logout();

                        }else{
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommentBase> call, Throwable t) {
                }
            });
        }
        else{
        }
    }

    void showPopupWindow(View view, int position,String id,String userid) {
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

        popup.getMenuInflater().inflate(R.menu.comment_delete, popup.getMenu());
        if (userData.getId().equals(userid)) {
            popup.getMenu().findItem(R.id.Delete).setVisible(true);
            popup.getMenu().findItem(R.id.Edit).setVisible(true);
            popup.getMenu().findItem(R.id.Hide).setVisible(true);
        } else if(postUserId!=null){
            if(postUserId.equals(userData.getId()))
            popup.getMenu().findItem(R.id.Delete).setVisible(true);
            popup.getMenu().findItem(R.id.Edit).setVisible(false);
            popup.getMenu().findItem(R.id.Hide).setVisible(true);
        }else{
            popup.getMenu().findItem(R.id.Delete).setVisible(false);
            popup.getMenu().findItem(R.id.Edit).setVisible(false);
            popup.getMenu().findItem(R.id.Hide).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Delete")) {
                    data.remove(position);
                    notifyDataSetChanged();
                    deletecomment(id,1,position);
                }
                else if(item.getTitle().equals("Hide")) {
                    deletecomment(id,2,position);
                }
                else if(item.getTitle().equals("Edit")) {
                    if (type == 1) {
                        update(id, "1");
                        editText.setText(data.get(position).getComment());
                    }
                    else if(type==2)
                        updatee(id,"1");
                    editText.setText(data.get(position).getComment());
                }
                return true;
            }
        });
        popup.show();
    }
}
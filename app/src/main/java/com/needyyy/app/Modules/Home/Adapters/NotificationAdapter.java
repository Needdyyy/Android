package com.needyyy.app.Modules.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.CreatePageFinalFragment;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.PageData;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.utils.CommonUtil.cancelProgress;
import static com.needyyy.app.utils.CommonUtil.cancelProgressDialog;
import static com.needyyy.app.utils.CommonUtil.showProgress;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.SingleFeedsViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    private List<PageData> pageDataList;

    public NotificationAdapter(Context context, List<PageData> data)
    {
        this.context         = context;
        this.pageDataList    = data;
    }


    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_mypage_adapter, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder,final int position) {
        PageData receivedData = pageDataList.get(position);
        if (!TextUtils.isEmpty(receivedData.getProfile())) {
            Glide.with(context)
                    .load(receivedData.getProfile())
                    .into(viewHolder.pageProfile);
        } else {
            viewHolder.pageProfile.setImageResource(R.drawable.needyy);
        }

        if (!TextUtils.isEmpty(receivedData.getBanner())) {
            Glide.with(context)
                    .load(receivedData.getBanner())
                    .into(viewHolder.imgPageBanner);
        } else {
            viewHolder.imgPageBanner.setImageResource(R.drawable.needyy);
        }
        viewHolder.tvPageTitle.setText(receivedData.getTitle());
        viewHolder.tvPageCreatedTime.setText(CommonUtil.getDate(Long.parseLong(receivedData.getCreated())));
        viewHolder.tvPageDescription.setText(receivedData.getDescription());

        viewHolder.btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPage(pageDataList.get(position).getId());
//                ((HomeActivity)context).replaceFragment(CreatePageFinalFragment.newInstance(pageDataList.get(position).getId().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pageDataList.size();

    }



    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView imgPageBanner;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        CircleImageView pageProfile;
        TextView tvPageTitle, tvPageCreatedTime ,tvPageDescription,btnEdit,btnRenew;

        public SingleFeedsViewHolder(View itemView) {
            super(itemView);

            pageProfile         = itemView.findViewById(R.id.img_pageprofile);
            tvPageTitle         = itemView.findViewById(R.id.tv_page_title);
            tvPageCreatedTime   = itemView.findViewById(R.id.tv_pagecreation_time);
            tvPageDescription   = itemView.findViewById(R.id.tv_page_description);
            btnEdit             = itemView.findViewById(R.id.btn_edit);
            btnRenew            = itemView.findViewById(R.id.btn_renew);
            imgPageBanner       = itemView.findViewById(R.id.img_page_banner);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_edit :
                    break;
                case R.id.btn_renew :
                   // ((HomeActivity)context).replaceFragment(CreatePageFinalFragment.newInstance(pageDataList.get(getAdapterPosition()).getId().toString()));
                    break;
            }
        }
    }

    private void getPage(String pageId) {
        showProgress((HomeActivity) context);
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CreatePageModel> call = Service.getPage(pageId);
        call.enqueue(new Callback<CreatePageModel>() {
            @Override
            public void onResponse(Call<CreatePageModel> call, Response<CreatePageModel> response) {
                cancelProgress();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CreatePageModel createPageModel = response.body();
                if (createPageModel.getStatus()) {
                    ((HomeActivity)context).replaceFragment(CreatePageFinalFragment.newInstance(createPageModel.getData(),true), true);
//                    pageData = createPageModel.getData().getPages();
//                    pageDataBase = createPageModel.getData();
//                    showCustomDialog();
                }
                else {
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
                cancelProgressDialog();
                ((HomeActivity)context).snackBar(t.getMessage());
            }
        });
    }
}
package com.needyyy.app.Chat.Adapter;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Knocks.models.AcceptRejectRequest;
import com.needyyy.app.R;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.needyyy.app.utils.CommonUtil.cancelProgress;
import static com.needyyy.app.utils.CommonUtil.showProgress;

public class DatingRequestAdapter extends RecyclerView.Adapter<DatingRequestAdapter.SingleFeedsViewHolder> {
    Activity activity;
    ArrayList<People> data;
    private DatingRequestAdapter knockRequestAdapter;

    public DatingRequestAdapter(Activity activity, ArrayList<People> data) {
        this.activity=activity;
        this.data = data;
    }


    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.knockfriend_layout, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder, final int position) {
        People people = data.get(position);
        String hiddenname="";
        for(int k=0;k<people.getName().length();k++)
        {
            hiddenname=hiddenname+"*";
        }
//        SpannableString string = new SpannableString(people.getName());
//        MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
//        string.setSpan(new MaskFilterSpan(blurMask), 0, people.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvProfileName.setText(hiddenname);

        //viewHolder.tvProfileName.setText(people.getName());
        if (!TextUtils.isEmpty(people.getProfilePicture())) {

            Glide.with(activity).load(people.getProfilePicture())
                    .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)).placeholder(R.drawable.needyy))
                    .into(viewHolder.civProfile);
        } else {
            viewHolder.civProfile.setImageResource(R.drawable.needyy);
        }



            viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRejectRequest(data.get(position).getId(), "2", position);

                }
            });

            viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRejectRequest(data.get(position).getId(), "1", position);

                }
            });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civProfile;
        TextView tvProfileName;
        Button decline, accept;

        public SingleFeedsViewHolder(View itemView) {
            super(itemView);

            civProfile = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            decline = itemView.findViewById(R.id.decline_button);
            accept = itemView.findViewById(R.id.accept_button);
        }
    }


    public void acceptRejectRequest(String id, String status, int position) {
        showProgress(activity);
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<AcceptRejectRequest> call;

        if(AppController.getManager().getInterest()!=null && AppController.getManager().getInterest().equals(Constant.DATING)){
            call = Service.acceptRejectRequestDating(id, status);
        }else{
            call = Service.acceptRejectRequest(id, status);
        }
        call.enqueue(new Callback<AcceptRejectRequest>() {
            @Override
            public void onResponse(Call<AcceptRejectRequest> call, Response<AcceptRejectRequest> response) {
                cancelProgress();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                AcceptRejectRequest acceptRejectRequest = response.body();
                if (acceptRejectRequest.getStatus()) {
                    Toast.makeText(activity, acceptRejectRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    data.remove(position);
                    notifyDataSetChanged();

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
                  cancelProgress();
            }
        });

    }
}
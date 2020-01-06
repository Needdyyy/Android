package com.needyyy.app.Modules.adsAndPage.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.adsAndPage.fragment.GlobalPostFragment;

import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.GlobalPost_;
import com.needyyy.app.R;
import java.util.ArrayList;
import java.util.List;

public class MyGlobalPostAdapter extends RecyclerView.Adapter<MyGlobalPostAdapter.modelViewHolder> {
    private Context context;
    List<GlobalPost_> list = new ArrayList<>();
    private static String id;

    public MyGlobalPostAdapter(Context context, List<GlobalPost_> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.layout_global_post,viewGroup,false);
        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder modelViewHolder, int i) {
        modelViewHolder.writeSomething.setText(list.get(i).getCaption());
        if(list.get(i).getIsApproved().equals("0"))
        {
            modelViewHolder.approved.setText("Pending");
            modelViewHolder.edit.setVisibility(View.VISIBLE);
            modelViewHolder.date.setVisibility(View.GONE);

        }
       else if(list.get(i).getIsApproved().equals("1"))
        {
            modelViewHolder.approved.setText("Approved");
            modelViewHolder.edit.setVisibility(View.GONE);
            modelViewHolder.date.setVisibility(View.VISIBLE);
            String Date=list.get(i).getDateFrom()+"-"+list.get(i).getDateTo();
            modelViewHolder.date.setText(Date);
        }
        else if(list.get(i).getIsApproved().equals("2"))
        {
            modelViewHolder.approved.setText("Expired");
            modelViewHolder.edit.setVisibility(View.GONE);
            modelViewHolder.date.setVisibility(View.VISIBLE);
            String Date=list.get(i).getDateFrom()+"-"+list.get(i).getDateTo();
            modelViewHolder.date.setText(Date);
        }
        modelViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GlobalPostFragment.tv_writesomething.setText(list.get(i).getCaption());
              if(context instanceof HomeActivity)
              {
                   id=list.get(i).getId();
                  ((HomeActivity) context).setpostbutton("update",id);
              }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder{
       TextView writeSomething,date,approved,edit;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            writeSomething=itemView.findViewById(R.id.tv_writesomething);
            date=itemView.findViewById(R.id.tv_date);
            approved=itemView.findViewById(R.id.tv_approved);
        }
    }
}


package com.needyyy.app.Modules.Home.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import com.needyyy.app.R;
import com.needyyy.app.Modules.Profile.models.UserPicture.ProfessionDetails;
import com.needyyy.app.mypage.model.masterindex.masterindex.ReportPost;

import java.util.ArrayList;

import static com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter.getid;

/**
 * Created by Admin on 07-08-2019.
 */

class ReportAbuseListAdapter extends RecyclerView.Adapter<ReportAbuseListAdapter.ReportAbuseHolder> {

    ArrayList<ReportPost> report_reasonsArrayList;
    Context context;

    private int lastSelectedPosition = -1;

    public ReportAbuseListAdapter(Context activity, ArrayList<ReportPost> report_reasonsArrayList) {
        this.context = activity;
        this.report_reasonsArrayList = report_reasonsArrayList;
    }

    @Override
    public ReportAbuseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_page_dialog, parent, false);
        return new ReportAbuseHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportAbuseHolder holder, final int position) {
        holder.radioButton.setText(report_reasonsArrayList.get(position).getTitle());
        holder.radioButton.setChecked(lastSelectedPosition == position);

        /*holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SingleFeedView().setReportId(report_reasonsArrayList.get(position).getId());
                *//*if (!holder.radioButton.isChecked()) {
                    holder.radioButton.setChecked(true);
                } else {
                    holder.radioButton.setChecked(false);
                }*//*
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return report_reasonsArrayList.size();
    }

    public class ReportAbuseHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        LinearLayout mainLL;

        public ReportAbuseHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioOption);
            mainLL = itemView.findViewById(R.id.mainLL);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                   String id= report_reasonsArrayList.get(lastSelectedPosition).getId();
                    getid(id);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
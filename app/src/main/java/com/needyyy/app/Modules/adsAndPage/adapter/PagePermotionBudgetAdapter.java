package com.needyyy.app.Modules.adsAndPage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.adsAndPage.fragment.CreatePageFinalFragment;
import com.needyyy.app.Modules.adsAndPage.modle.Budget;
import com.needyyy.app.R;

import java.util.List;

import static com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter.convertno;

public class PagePermotionBudgetAdapter extends RecyclerView.Adapter<PagePermotionBudgetAdapter.SingleFeedsViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    private List<Budget> pageDataList;
    public int mSelectedItem = -1;
    private CreatePageFinalFragment createPageFinalFragment ;
    public PagePermotionBudgetAdapter(Context context, List<Budget> data, CreatePageFinalFragment createPageFinalFragment)
    {
        this.context                 = context;
        this.pageDataList            = data;
        this.createPageFinalFragment = createPageFinalFragment;
    }


    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagepermotion_adapter, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder,final int position) {
        Budget budget = pageDataList.get(position);
        int targetuser=(int) budget.getTargetuser() ;
        String value=convertno(String.valueOf(targetuser));
        viewHolder.tvTargetuser.setText(value+" People per day");
        viewHolder.radioBudget.setText("Rs. "+budget.getBudgetPrice());
        viewHolder.radioBudget.setChecked(position == mSelectedItem);

    }

    @Override
    public int getItemCount() {
        return pageDataList.size();

    }



    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTargetuser ;
        private RadioButton radioBudget;
        private View itemView;

        public SingleFeedsViewHolder(View itemView) {
            super(itemView);

            tvTargetuser         = itemView.findViewById(R.id.tv_targetuser);
            radioBudget          = itemView.findViewById(R.id.radio_budget);
            itemView             = itemView.findViewById(R.id.item_view);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                    createPageFinalFragment.setBudget();
                }
            };
            itemView.setOnClickListener(clickListener);
            radioBudget.setOnClickListener(clickListener);
        }
    }


}
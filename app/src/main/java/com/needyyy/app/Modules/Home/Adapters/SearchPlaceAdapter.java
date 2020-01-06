package com.needyyy.app.Modules.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.needyyy.app.Modules.Home.Activities.CheckInActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Home.modle.GooglePlaceResult;
import com.needyyy.app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.SearchPlaceeViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    ArrayList<GooglePlaceResult> data ;
    private int type;
    public SearchPlaceAdapter(int type, Context context, ArrayList<GooglePlaceResult> data )
    {
        this.context = context;
        this.data    = data;
        this.type    = type ;

    }


    @NonNull
    @Override
    public SearchPlaceeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_checkin_layout, parent, false);
        return new SearchPlaceeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlaceeViewHolder viewHolder, final int position) {
        GooglePlaceResult googlePlaceResult= data.get(position);
        viewHolder.tvPlaceName.setText(googlePlaceResult.getName());
        viewHolder.tvLocation.setText(googlePlaceResult.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchPlaceeViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView ivImage;
////        AppTextView tvTitle,tvDescription;
//        RelativeLayout rlContentTypes;
        CircleImageView civProfile ;
        TextView tvPlaceName,tvLocation,tvReply;
        private View footer;


        public SearchPlaceeViewHolder(View itemView) {
            super(itemView);


            tvPlaceName       = itemView.findViewById(R.id.tvname);
            tvLocation        = itemView.findViewById(R.id.tv_location);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof CheckInActivity){
                        ((CheckInActivity)context).getPlaceDetails(data.get(getAdapterPosition()).getId(),(data.get(getAdapterPosition()).getName()));
                    }
                }
            });

        }
    }
}
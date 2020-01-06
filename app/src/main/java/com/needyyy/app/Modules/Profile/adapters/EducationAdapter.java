package com.needyyy.app.Modules.Profile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Profile.fragments.EducationFragment;
import com.needyyy.app.Modules.Profile.fragments.ProfessionFragment;
import com.needyyy.app.Modules.Profile.fragments.ProfileFragment;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.Modules.Profile.models.EducationalDetail;
import com.needyyy.app.Modules.Profile.models.UserPicture.ProfessionDetails;
import com.needyyy.app.R;

import java.util.ArrayList;

import static com.needyyy.app.Modules.Profile.fragments.EducationFragment.REQUEST_CODE;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    ArrayList<ProfessionDetails> data2 = new ArrayList<ProfessionDetails>();
    ArrayList<EducationalDetail> data = new ArrayList<EducationalDetail>();
    String type;
    public boolean isClickable = true;
    Fragment fragment;

    public EducationAdapter(Context context, ArrayList<EducationalDetail> data, Fragment fragment,ArrayList<ProfessionDetails> data2) {
        this.context = context;
        this.data = data;
        this.fragment = fragment;
        this.data2=data2;
        Log.e("data", "EducationAdapter: " + data );
    }


    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_education_adapter, parent, false);
        return new EducationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder viewHolder, final int position) {
        if(data2==null) {
            EducationalDetail receivedData = data.get(position);
            if (receivedData.getType().equals("1")) {
                type = "school";
            } else if (receivedData.getType().equals("2")) {
                type = "Graduation";
            } else if (receivedData.getType().equals("3")) {
                type = "Post Graduation";
            }
//        viewHolder.tvEducationDetails.setText("Studied "+ receivedData.getQualification()
//                + " from " + receivedData.getName()
//                + " from " + receivedData.getFromYear()
//                + " to " + receivedData.getToYear() );

            viewHolder.tvEducationName.setText(receivedData.getName());

            viewHolder.tvEducationDetails.setText(receivedData.getQualification() + "\n"
                    + receivedData.getFromYear() + " - " + receivedData.getToYear());

//        viewHolder.tvEducationDetails.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_student,0, R.drawable.edit,0);

            viewHolder.llEdu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    HomeActivity activity = (HomeActivity) context;

                    if (!isClickable)
                        return;
                    // do your click stuff

                    FragmentManager fm = activity.getSupportFragmentManager();
                    EducationFragment dialogFragment = EducationFragment.newInstance(receivedData.getId(),
                            type,
                            receivedData.getName(),
                            receivedData.getQualification(),
                            receivedData.getFromYear(),
                            receivedData.getToYear());
                    dialogFragment.setTargetFragment(fragment, REQUEST_CODE);
                    dialogFragment.show(fm, "fragment_education");
                }
            });

        }
        else if(data==null) {
            ProfessionDetails receivedData2 = data2.get(position);
            viewHolder.tvEducationName.setText(receivedData2.getCompany());
            if (receivedData2.getDateLeft().equals("0")) {

                viewHolder.tvEducationDetails.setText(receivedData2.getDesignation() + "\n"
                        + DateUtils.getRelativeTimeSpanString(Long.parseLong(receivedData2.getDateJoining())* 1000) + " - " +
                        "till now");
            }
            else
            {

                viewHolder.tvEducationDetails.setText(receivedData2.getDesignation() + "\n"
                        + DateUtils.getRelativeTimeSpanString(Long.parseLong(receivedData2.getDateJoining())* 1000) + " - " +
                        DateUtils.getRelativeTimeSpanString(Long.parseLong(receivedData2.getDateLeft())* 1000));
            }


//        viewHolder.tvEducationDetails.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_student,0, R.drawable.edit,0);
            if(fragment instanceof ViewProfileFragment)
            {

            }
            else {
                viewHolder.llEdu.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        HomeActivity activity = (HomeActivity) context;

                        if (!isClickable)
                            return;
                        // do your click stuff


                        FragmentManager fm = activity.getSupportFragmentManager();
                        ProfessionFragment dialogFragment = ProfessionFragment.newInstance(receivedData2.getId(),
                                receivedData2.getCompany(),
                                receivedData2.getDesignation(),
                                DateUtils.getRelativeTimeSpanString(Long.parseLong(receivedData2.getDateJoining()) * 1000).toString(),
                                DateUtils.getRelativeTimeSpanString(Long.parseLong(receivedData2.getDateLeft()) * 1000).toString());

                        dialogFragment.setTargetFragment(fragment, REQUEST_CODE);
                        dialogFragment.show(fm, "fragment_profession");




                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(data2==null) {
            return data.size();
        }
        else
        {
            return data2.size();
        }
//        return 10;
    }


    public class EducationViewHolder extends RecyclerView.ViewHolder {

        TextView tvEducationDetails, tvEducationName;
        LinearLayout llEdu;

        public EducationViewHolder(View itemView) {
            super(itemView);

            tvEducationDetails = itemView.findViewById(R.id.tv_education_details);
            tvEducationName     = itemView.findViewById(R.id.tv_education_name);
            llEdu               = itemView.findViewById(R.id.lledu);
        }


    }


}
package com.needyyy.app.Modules.Dating.suggestions.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.R;
import com.needyyy.app.views.CircleImageView;

public class ViewDatingProfile extends BaseFragment implements View.OnClickListener {

    CardView profileCard, aboutCard;
    TextView tvProfileName, tvDob, tvDistance, tvGender, tvAbout;
    CircleImageView civProfileImage;

    public ViewDatingProfile() {
        // Required empty public constructor
    }

    public static ViewDatingProfile newInstance() {
        ViewDatingProfile fragment = new ViewDatingProfile();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_view_dating_profile);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected void initView(View mView) {
        profileCard = mView.findViewById(R.id.profileCard);
        aboutCard = mView.findViewById(R.id.aboutCard);
        tvProfileName = mView.findViewById(R.id.tv_profile_name);
        tvDob = mView.findViewById(R.id.tv_dob);
        tvGender = mView.findViewById(R.id.tv_gender);
        tvDistance = mView.findViewById(R.id.tv_distance);
        tvAbout = mView.findViewById(R.id.tv_about);
        civProfileImage = mView.findViewById(R.id.userImageProfile);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        civProfileImage.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userImageProfile:
                break;

            case R.id.tv_profile_name:
                break;
        }
    }
}

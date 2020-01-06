package com.needyyy.app.Modules.AddPost.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.R;

public class CreatePage extends BaseFragment implements View.OnClickListener{

    public CreatePage() {
        // Required empty public constructor
    }


    public static CreatePage newInstance() {
        CreatePage fragment = new CreatePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_page);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {

    }
}

package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.R;
public class CreatePageFragment extends BaseFragment {

    public CreatePageFragment() {
        // Required empty public constructor
    }


    public static CreatePageFragment newInstance() {
        CreatePageFragment fragment = new CreatePageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

}

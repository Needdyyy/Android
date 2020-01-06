package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;

public class CreateYourPage extends BaseFragment {

    SeekBar seekBar;
    HomeActivity homeActivity;

    public CreateYourPage() {
        // Required empty public constructor
    }


    public static CreateYourPage newInstance() {
        CreateYourPage fragment = new CreateYourPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.fragment_create_your_page);
        if (getArguments() != null) {

        }

        ((HomeActivity)getActivity()).manageToolbar("Create Page", "");
//        seekBar=(SeekBar)findViewById(R.id.seekBar);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress,
//                                          boolean fromUser) {
//                Toast.makeText(getActivity(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getActivity(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }


}

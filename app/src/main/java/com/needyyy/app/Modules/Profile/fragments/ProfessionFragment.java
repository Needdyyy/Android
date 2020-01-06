package com.needyyy.app.Modules.Profile.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Modules.Profile.models.Datum;
import com.needyyy.app.Modules.Profile.models.GetEducation;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.webutils.WebInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.utils.CommonUtil.cancelProgress;
import static com.needyyy.app.utils.CommonUtil.cancelProgressDialog;
import static com.needyyy.app.utils.CommonUtil.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessionFragment extends DialogFragment implements View.OnClickListener{

    RadioButton genderradioButton;
    TextInputLayout leftframe;
    RadioGroup radioGroup;
    TextInputEditText etjoining, etleft, spcompany, spdesignation;
    RadioButton currentcompany,previouscompant;
    Button btnsavepro;
    String company,designation,eduId="", qualiId,joining,left;
    public final static int REQUEST_CODEPRO = 10002;
    Boolean status=true;
    public ProfessionFragment() {
        // Required empty public constructor
    }


    public static ProfessionFragment newInstance() {
        ProfessionFragment fragment = new ProfessionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfessionFragment newInstance(String id, String company, String designation, String joining, String left) {
        ProfessionFragment fragment = new ProfessionFragment();
        Bundle args = new Bundle();
        args.putString("eduId", id);
        args.putString("company", company);
        args.putString("designation", designation);
        args.putString("joining", joining);
        args.putString("left", left);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            eduId = getArguments().getString("eduId");
            company = getArguments().getString("company");
            designation = getArguments().getString("designation");
            joining = getArguments().getString("joining");
            left = getArguments().getString("left");
            status=false;
        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profession, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState)

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindControls(savedInstanceState);
    }
    TextView tvText;
    protected void initView(View mView) {
        leftframe =(TextInputLayout) mView.findViewById(R.id.leftframe);
        previouscompant=(RadioButton) mView.findViewById(R.id.previouscompant);
        radioGroup=(RadioGroup)mView.findViewById(R.id.Radio_Group);
        currentcompany     =mView.findViewById(R.id.currentcompany);
        spcompany         = mView.findViewById(R.id.sp_company);
        spdesignation        = mView.findViewById(R.id.sp_destination);
        etleft              = mView.findViewById(R.id.et_left);
        etjoining                = mView.findViewById(R.id.et_joining);
        btnsavepro             = mView.findViewById(R.id.btn_savepro);
        tvText              = mView.findViewById(R.id.tv_text);
        if(status==true) {
            currentcompany.setVisibility(View.VISIBLE);
            etleft.setVisibility(View.GONE);
            etjoining.setVisibility(View.GONE);
        }
        else if(status==false)
        {

            if(left.equals("1 Jan 1970"))
            {
                currentcompany.setChecked(true);
                etleft.setVisibility(View.GONE);
                leftframe.setVisibility(View.GONE);
            }
            else
            {
                leftframe.setVisibility(View.VISIBLE);
                previouscompant.setChecked(true);
                etleft.setVisibility(View.VISIBLE);
                etjoining.setVisibility(View.VISIBLE);
            }

        }
        if(getArguments() != null)
        {
            spcompany.setText(company);
            spdesignation.setText(designation);
            etleft.setText(left);
            etjoining.setText(joining);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                genderradioButton = (RadioButton) mView.findViewById(selectedId);


                if(selectedId==-1){
                    Toast.makeText(getContext(),"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(genderradioButton.getText().equals("Current Company"))
                    {
                        leftframe.setVisibility(View.GONE);
                        etleft.setVisibility(View.GONE);
                        etleft.setText("");
                        etjoining.setVisibility(View.VISIBLE);
                    }
                    else if(genderradioButton.getText().equals("Previous Company"))
                    {
                        etleft.setText("");
                        leftframe.setVisibility(View.VISIBLE);
                        etleft.setVisibility(View.VISIBLE);
                        etjoining.setVisibility(View.VISIBLE);
                    }
                }



            }
        });


    }

    protected void bindControls(Bundle savedInstanceState) {
        btnsavepro.setOnClickListener(this);
        spcompany.setOnClickListener(this);
        etleft.setOnClickListener(this);
        spdesignation.setOnClickListener(this);
        etjoining.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_left:
                CommonUtil.DatePicker(getActivity(), etleft, ProfessionFragment.this);
                break;

            case R.id.et_joining:
                CommonUtil.DatePicker(getActivity(), etjoining, ProfessionFragment.this);
                break;

            case R.id.btn_savepro:
                if(eduId.isEmpty()){
                    eduId = "";
                }
                if(checkMandatory()) {
                    Intent intent = new Intent();
                    intent.putExtra("eduId", eduId);
                    //intent.putExtra("academicsId", academicStatus);
                    intent.putExtra("company", spcompany.getText().toString());
                    intent.putExtra("designation", spdesignation.getText().toString());//qualiId
                    intent.putExtra("leftdate", etleft.getText().toString());
                    intent.putExtra("joiningdate", etjoining.getText().toString());
                    dismiss();

                    Toast.makeText(getActivity(), "Profession Details Added Successfully", Toast.LENGTH_SHORT).show();

                    getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODEPRO, intent);

                }else{
//                    Toast.makeText(getActivity(), "Please check all fields", Toast.LENGTH_SHORT).show();
                }
        }
    }
    public boolean checkMandatory(){
        if(!TextUtils.isEmpty(spcompany.getText())){
            if(!TextUtils.isEmpty(spdesignation.getText())){
                if(checkValidity()){
                    return true;
                }else{
                    return false;
                }

            }else{
                Toast.makeText(getActivity(), "Please choose valid destination", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please enter valid company Detail", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public boolean checkValidity(){
        if (genderradioButton.getText().equals("Previous Company"))
        {
            if(!TextUtils.isEmpty(etjoining.getText()) && !TextUtils.isEmpty(etleft.getText())){
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date dateFrom , dateTo;
                long timeFrom, timeTo;
                try {
                    dateFrom = dateFormat.parse(etjoining.getText().toString());
                    dateTo = dateFormat.parse(etleft.getText().toString());
                    timeFrom = dateFrom.getTime();
                    timeTo = dateTo.getTime();

                    if(timeFrom < timeTo){
                        return true;
                    } else {
                        Toast.makeText(getActivity(), "left time is not less than equal to joining time", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        else
        {
            if(!TextUtils.isEmpty(etjoining.getText()) || !TextUtils.isEmpty(etleft.getText())){
                return true;
            }
            else
            {
                return false;
            }
        }
        Toast.makeText(getActivity(), "Please enter valid Dates", Toast.LENGTH_SHORT).show();
        return  false;
    }
}

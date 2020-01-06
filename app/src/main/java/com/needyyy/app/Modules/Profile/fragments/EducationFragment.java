package com.needyyy.app.Modules.Profile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
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

public class EducationFragment extends DialogFragment implements View.OnClickListener {
//    Spinner spAcademics, spEducation, spQualification;
    TextInputEditText etFrom, etTo, spAcademics, spEducation, spQualification;
    Button btnSave;
    String edu,academics,quali, qualiId;
    String eduId="", acaEdit, eduEdit, qualiEdit,fromYearEdit, toYearEdit;
    public final static int REQUEST_CODE = 10001;

    public EducationFragment() {
        // Required empty public constructor
    }


    public static EducationFragment newInstance() {
        EducationFragment fragment = new EducationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EducationFragment newInstance(String id, String academics, String education, String qualification, String fromYear, String toYear) {
        EducationFragment fragment = new EducationFragment();
        Bundle args = new Bundle();
        args.putString("eduId", id);
        args.putString("acaEdit", academics);
        args.putString("eduEdit", education);
        args.putString("qualiEdit", qualification);
        args.putString("fromEdit", fromYear);
        args.putString("toEdit", toYear);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eduId = getArguments().getString("eduId");
            acaEdit = getArguments().getString("acaEdit");
            eduEdit = getArguments().getString("eduEdit");
            qualiEdit = getArguments().getString("qualiEdit");
            fromYearEdit = getArguments().getString("fromEdit");
            toYearEdit = getArguments().getString("toEdit");

        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_education, container, false);
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
        spAcademics         = mView.findViewById(R.id.sp_academics);
        spEducation         = mView.findViewById(R.id.sp_education);
        spQualification     = mView.findViewById(R.id.sp_qualification);
        etFrom              = mView.findViewById(R.id.et_from);
        etTo                = mView.findViewById(R.id.et_to);
        btnSave             = mView.findViewById(R.id.btn_save);
        tvText              = mView.findViewById(R.id.tv_text);
    }


    protected void bindControls(Bundle savedInstanceState) {
        btnSave.setOnClickListener(this);
        etFrom.setOnClickListener(this);
        etTo.setOnClickListener(this);
        spQualification.setOnClickListener(this);
        spEducation.setOnClickListener(this);
        spAcademics.setOnClickListener(this);

        spAcademics.setText(acaEdit);
        spEducation.setText(eduEdit);
        spQualification.setText(qualiEdit);
        etFrom.setText(fromYearEdit);
        etTo.setText(toYearEdit);

        if(!eduId.isEmpty()){
            tvText.setText("Edit Education");
        } else{
            tvText.setText("Add Education");
        }
    }

    @SuppressLint("RestrictedApi")
    private void selectAcademics(View view) {
        MenuBuilder menuBuilder = new MenuBuilder(getActivity());
        MenuInflater inflater = new MenuInflater(getActivity());
        inflater.inflate(R.menu.academics_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(getActivity(), menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                spAcademics.setText(item.getTitle());
                if (spAcademics.getText().toString().trim().equalsIgnoreCase("School")) {
                    academicStatus = "1";
                    getQualification(Integer.parseInt(academicStatus));
                } else if(spAcademics.getText().toString().trim().equalsIgnoreCase("Graduation")){
                    academicStatus = "2";
                    getQualification(Integer.parseInt(academicStatus));
                } else if(spAcademics.getText().toString().trim().equalsIgnoreCase("Post Graduation")){
                    academicStatus = "3";
                    getQualification(Integer.parseInt(academicStatus));
                }
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        optionsMenu.show();
    }

    @SuppressLint("RestrictedApi")
    private void selectQualification(View view) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenu().clear();
        for(int i=0; i<getquali.size(); i++) {
            popupMenu.getMenu().add(getquali.get(i).getTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                spQualification.setText(item.getTitle());
                qualiId = String.valueOf(getquali.get(item.getItemId()).getId());
//                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    String academicStatus = "0", qualiStatus = "0";
    String[] qualiList;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:

//                if(spQualification.getText().toString().trim().equalsIgnoreCase("Married")){
//                    qualiStatus = "3";
//                } else if(spQualification.getText().toString().trim().equalsIgnoreCase("Divorced")){
//                    qualiStatus = "4";
//                } else if(spQualification.getText().toString().trim().equalsIgnoreCase("It's complicated")){
//                    qualiStatus = "5";
//                }

                if(eduId.isEmpty()){
                    eduId = "";
                }

                if(checkMandatory()) {
                    Intent intent = new Intent();
                    intent.putExtra("eduId", eduId);
                    intent.putExtra("academicsId", academicStatus);
                    intent.putExtra("educationId", spEducation.getText());
                    intent.putExtra("qualificationId", spQualification.getText().toString());//qualiId
                    intent.putExtra("qualiName", spQualification.getText().toString());
                    intent.putExtra("fromYear", etFrom.getText().toString());
                    intent.putExtra("toYear", etTo.getText().toString());
                    dismiss();

                    Toast.makeText(getActivity(), "Education Details Added Successfully", Toast.LENGTH_SHORT).show();

                    getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);

                }else{
//                    Toast.makeText(getActivity(), "Please check all fields", Toast.LENGTH_SHORT).show();
                }

                break;

            case  R.id.et_from:
                CommonUtil.DatePicker(getActivity(), etFrom,EducationFragment.this);
                break;

            case R.id.et_to:
                CommonUtil.DatePicker(getActivity(), etTo,EducationFragment.this);
                break;

            case R.id.sp_academics:
                selectAcademics(spAcademics);
                break;
//            case R.id.sp_education:
//                selectGender(et_gender);
//                break;
            case R.id.sp_qualification:
//                getQualification(Integer.parseInt(academicStatus));
                selectQualification(spQualification);
                break;

        }
    }

    ArrayList<Datum> getquali = new ArrayList<>();

    public void getQualification(int type){
        if (CommonUtil.isConnectingToInternet(getActivity())) {
           showProgress(getActivity());
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GetEducation> call = Service.getQuali(type);

            call.enqueue(new Callback<GetEducation>() {
                @Override
                public void onResponse(Call<GetEducation> call, Response<GetEducation> response) {
                    cancelProgress();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    GetEducation getEducation = response.body();
                    if (getEducation.getStatus()) {
                        getquali.clear();
                        getquali.addAll(getEducation.getData());
//                        for(int i=0; i<getquali.size(); i++){
//                            qualiList[i] = getquali.get(i).getTitle().toString();
//                        }
//                        spQualification.setText(getEducation.getData().get);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetEducation> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
//        }else{

        }
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
//        for(int i=0; i<getquali.size(); i++){
//            menu.add(0, i, Menu.NONE, getquali.get(i).getTitle());
//        }
//        super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        for(int i=0; i<getquali.size(); i++){
//            if(i == item.getItemId()){
//                Toast.makeText(getActivity(), "items" + getquali.get(i).getId(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public boolean checkValidity(){
        if(!TextUtils.isEmpty(etFrom.getText()) && !TextUtils.isEmpty(etTo.getText())){
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date dateFrom , dateTo;
            long timeFrom, timeTo;
            try {
                dateFrom = dateFormat.parse(etFrom.getText().toString());
                dateTo = dateFormat.parse(etTo.getText().toString());
                timeFrom = dateFrom.getTime();
                timeTo = dateTo.getTime();

                if(timeFrom < timeTo){
                    return true;
                } else {
                    Toast.makeText(getActivity(), "To time is not less than equal to from time", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Toast.makeText(getActivity(), "Please enter valid Dates", Toast.LENGTH_SHORT).show();
        return  false;
    }

    public boolean checkMandatory(){
        if(!TextUtils.isEmpty(spAcademics.getText())){
            if(!TextUtils.isEmpty(spEducation.getText())){
                if(!TextUtils.isEmpty(spQualification.getText())){
                    if(checkValidity()){
                        return true;
                    }else{
                        return false;
                    }

                }else{
                    Toast.makeText(getActivity(), "Please choose valid Qualification", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please enter valid Education Detail", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please choose valid Academic", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}

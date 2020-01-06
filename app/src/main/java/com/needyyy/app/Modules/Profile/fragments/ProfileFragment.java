package com.needyyy.app.Modules.Profile.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.EducationAdapter;
import com.needyyy.app.Modules.Profile.models.AddressDetails;
import com.needyyy.app.Modules.Profile.models.EducationalDetail;
import com.needyyy.app.Modules.Profile.models.GetRelationStatus;
import com.needyyy.app.Modules.Profile.models.NewEducationDetail;
import com.needyyy.app.Modules.Profile.models.RelationStatus;
import com.needyyy.app.Modules.Profile.models.UpdateProfile;
import com.needyyy.app.Modules.Profile.models.UserPicture.NewProfessionDetails;
import com.needyyy.app.Modules.Profile.models.UserPicture.ProfessionDetails;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.mypage.model.masterindex.masterindex.Hobbies;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GooglePlaceAutocompleteAdapter;
import com.needyyy.app.utils.UploadAmazonS3;
import com.needyyy.app.webutils.WebInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_GALLERY;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_TAKE_PICTURE;
import static com.needyyy.app.Modules.Profile.fragments.EducationFragment.REQUEST_CODE;
import static com.needyyy.app.Modules.Profile.fragments.ProfessionFragment.REQUEST_CODEPRO;
import static com.needyyy.app.constants.Constants.Kmasterhit;
import static com.needyyy.app.constants.Constants.kCurrentUser;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    public Boolean iscover=false;
    CircleImageView tv_editImage;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "ProfileFragment";
    private String sendhobies;
    public String education_details;
    private  Data data;
    public String professional_details;
    Uri uri;
    public Button btnSave, btnAddMore,btnAddMorePro;
    public TextView tvFriends, tvProfileName, tvProfileCode, tvFriendsCount, tvMale, tvFemale, tvBoth,tvBio,
            tvEducation,tvprofession, tvPlaces, tvBasicInfo, tvInterested, tvRelationship, tvYes, tvNo, tvNotSure, tvOpenForDating;
    EditText etAddBio, etNickname;
    TextInputEditText et_gender, etDob, etFrom, etTo, spRelationship;
    LinearLayout llProfileMain, llProfileTop, llBio, llEducation, llPlaces, llBasicInfo, llFriends, llInterestedIn, llRelationship, llProfileEdu;
    CircleImageView profileImage;
    int size = 0;
    Spinner spGender, spAcademics, spEducation, spQualification;
    AutoCompleteTextView atHomecity, atCurrentCity;
    String gender, profileName, ssnid, bio, collegeName, schoolName, dob, nickname, interestedIn, relationStatus="0", openDating="1", hometown, current_address;
    String relation, quali, academics, edu;
    String educationJson,professionJson, genderId, interestedId="2", openDatingId="1";
    String location;
    String academicsId, educationId, qualiId, fromYear, toYear;
    List<NewEducationDetail> details = new ArrayList<>();
    List<NewProfessionDetails> Professiondetails = new ArrayList<NewProfessionDetails>();
    List<AddressDetails> location_details = new ArrayList<>();
    NewEducationDetail newEducationDetail;
    private ArrayList<Hobbies> hobbies=new ArrayList<>();
    private ArrayList<Hobbies> currenthobbies;
    NewProfessionDetails newProfessionDetails;
    AddressDetails addressDetailsHome, addressDetailsCurrent;
    ArrayList<String>arrayListinterest=new ArrayList<>();

    String locationJson;
    UserDataResult updateProfileResult;
    RecyclerView rvEdu,rvpro;
    EducationAdapter eduAdapter;
    EducationAdapter eduAdapter2;
    String company,designation,joiningdate,leftdate;
    ArrayList<EducationalDetail> arrFeeds = new ArrayList<EducationalDetail>();
    ArrayList<ProfessionDetails> proFeeds = new ArrayList<ProfessionDetails>();
    Activity activity;
    Button addmore;
    TextView tv_search,tv_interest;
    File file;
    Compressor compressor;
    private String longitutehome="",latitudehome="",longitutecurrent="",langitudecurrent="",homelocation="",currentlocation="";


    AutoCompleteTextView autoCompleteTextView;

    String text;
    RecyclerView recyclerView;
    HobbieAdapter hobbieAdapter;
    List<String> arrayList = new ArrayList();

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    TextView tvEducationLatest;
    TextView tvProfessionLatest;
    String eduId,proid="";
    private File newFile;
    private Uri newProfileImageUri;
    private String state, imageName;
    private UserDataResult userData;
    ImageView iveduadd,iv_pro_add,cover_photo;
    LinearLayout llRv;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(UserDataResult updateProfileResult) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.USER_PROFILE_DATA, updateProfileResult);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_profile);
        activity = getActivity();
        if (getArguments() != null) {
            updateProfileResult = (UserDataResult) getArguments().getSerializable(Constants.USER_PROFILE_DATA);
        }
    }

    @Override
    protected void initView(View mView) {
        tv_interest=mView.findViewById(R.id.tv_interest);
        recyclerView=mView.findViewById(R.id.recyclerview);
        addmore=mView.findViewById(R.id.addmore);
        autoCompleteTextView=mView.findViewById(R.id.autoCompleteTextView1);
        cover_photo=mView.findViewById(R.id.cover_photo);
        profileImage = mView.findViewById(R.id.profile_image);
        tvProfileName = mView.findViewById(R.id.tv_profile_name);
        tvProfileCode = mView.findViewById(R.id.tv_profile_code);
        tvMale = mView.findViewById(R.id.tv_male);
        tvFemale = mView.findViewById(R.id.tv_female);
        tvBoth = mView.findViewById(R.id.tv_both);
        tvEducation = mView.findViewById(R.id.tv_education);
        tvprofession = mView.findViewById(R.id.tv_profession);
        iv_pro_add=mView.findViewById(R.id.iv_pro_add);
        tvEducationLatest = mView.findViewById(R.id.tv_education_details_latest);
        tvProfessionLatest=mView.findViewById(R.id.tv_profession_details_latest);
        tvPlaces = mView.findViewById(R.id.tv_places);
        tvBasicInfo = mView.findViewById(R.id.tv_basic_info);
        tvFriends = mView.findViewById(R.id.tv_friends);
        tvFriendsCount = mView.findViewById(R.id.tv_friends_count);
        tvInterested = mView.findViewById(R.id.tv_interested);
        tvRelationship = mView.findViewById(R.id.tv_relationship);
        tvBio = mView.findViewById(R.id.tv_bio);

        etAddBio = mView.findViewById(R.id.et_add_bio);
        spAcademics = mView.findViewById(R.id.sp_academics);
        spEducation = mView.findViewById(R.id.sp_education);
        spQualification = mView.findViewById(R.id.sp_qualification);
        etFrom = mView.findViewById(R.id.et_from);
        etTo = mView.findViewById(R.id.et_to);
        llRv = mView.findViewById(R.id.ll_rv);
        iveduadd = mView.findViewById(R.id.iv_edu_add);
        tv_editImage = mView.findViewById(R.id.tv_editImage);
//        spGender = mView.findViewById(R.id.spinner_gender);
        atHomecity = mView.findViewById(R.id.edt_home_city);
        atCurrentCity = mView.findViewById(R.id.edt_current_city);
        etDob = mView.findViewById(R.id.et_dob);
        etNickname = mView.findViewById(R.id.et_nickname);
        spRelationship = mView.findViewById(R.id.spinner_relationship);
        llFriends = mView.findViewById(R.id.ll_friends);

        tvYes = mView.findViewById(R.id.tv_yes);
        tvNo = mView.findViewById(R.id.tv_no);
        tvNotSure = mView.findViewById(R.id.tv_not_sure);
        tvOpenForDating = mView.findViewById(R.id.tv_open_for_dating);

        btnSave = mView.findViewById(R.id.btn_save);
        btnAddMore = mView.findViewById(R.id.btn_add_more);
        btnAddMorePro=mView.findViewById(R.id.btn_add_morepro);
        rvEdu = mView.findViewById(R.id.rv_edu);
        rvpro=mView.findViewById(R.id.rv_pro);
        llProfileEdu = mView.findViewById(R.id.ll_profile_education);
        et_gender = mView.findViewById(R.id.et_gender);

        ((HomeActivity) getActivity()).manageToolbar("Profile", "");

        btnSave.setVisibility(View.GONE);
        btnAddMore.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).navigation.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



//        setDisability();
//        getFriendsCount();
//        setProfileData();

    }

    public void setEnability() {
        spAcademics.setEnabled(true);
        spQualification.setEnabled(true);
        etFrom.setEnabled(true);
        etTo.setEnabled(true);
        spEducation.setEnabled(true);
        spRelationship.setEnabled(true);
//        spGender.setEnabled(true);
        atHomecity.setEnabled(true);
        atCurrentCity.setEnabled(true);
        etNickname.setEnabled(true);
        et_gender.setEnabled(true);
        etDob.setEnabled(true);
        tvMale.setEnabled(true);
        tvFemale.setEnabled(true);
        tvBoth.setEnabled(true);
        tvYes.setEnabled(true);
        tvNo.setEnabled(true);
        tvNotSure.setEnabled(true);
        btnAddMore.setEnabled(true);
        btnAddMorePro.setEnabled(true);
        etAddBio.setEnabled(true);

        btnSave.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
        btnAddMore.setVisibility(View.VISIBLE);
        btnAddMorePro.setVisibility(View.VISIBLE);
        tvEducation.setCompoundDrawables(null, null, null, null);
        tvPlaces.setCompoundDrawables(null, null, null, null);
        tvBasicInfo.setCompoundDrawables(null, null, null, null);
        tvInterested.setCompoundDrawables(null, null, null, null);
        tvRelationship.setCompoundDrawables(null, null, null, null);
        tvOpenForDating.setCompoundDrawables(null, null, null, null);

//        AppController.getManager().setSavebtnstatus("true");

    }

    public void setDisability() {
        addmore.setEnabled(false);
        autoCompleteTextView.setEnabled(false);
        spAcademics.setEnabled(false);
        spQualification.setEnabled(false);
        etFrom.setEnabled(false);
        etTo.setEnabled(false);
        spEducation.setEnabled(false);
        spRelationship.setEnabled(false);
//        spGender.setEnabled(false);
        atHomecity.setEnabled(false);
        atCurrentCity.setEnabled(false);
        etNickname.setEnabled(false);
        etDob.setEnabled(false);
        tvMale.setEnabled(false);
        tvFemale.setEnabled(false);
        tvBoth.setEnabled(false);
        tvYes.setEnabled(false);
        tvNo.setEnabled(false);
        tvNotSure.setEnabled(false);
        et_gender.setEnabled(false);
        rvEdu.setEnabled(false);
        rvpro.setEnabled(false);
        rvEdu.setClickable(false);
        rvpro.setClickable(false);
        llRv.setEnabled(false);
        llRv.setClickable(false);
        etAddBio.setEnabled(false);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).manageToolbar("profile", "");
        profileImage.setOnClickListener(this);
        tv_interest.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        tvProfileCode.setOnClickListener(this);
        tvMale.setOnClickListener(this);
        tvFemale.setOnClickListener(this);
        tvBoth.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        tvNotSure.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvEducation.setOnClickListener(this);
        tvprofession.setOnClickListener(this);
        tvPlaces.setOnClickListener(this);
        tvBasicInfo.setOnClickListener(this);
        tvFriends.setOnClickListener(this);
        tvFriendsCount.setOnClickListener(this);
        tvInterested.setOnClickListener(this);
        tvRelationship.setOnClickListener(this);
        etAddBio.setOnClickListener(this);
        tv_editImage.setOnClickListener(this);
        etDob.setOnClickListener(this);
        etNickname.setOnClickListener(this);
        tvOpenForDating.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etFrom.setOnClickListener(this);
        etTo.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnAddMorePro.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        tvProfileCode.setOnClickListener(this);
        tvMale.setOnClickListener(this);
        tvFemale.setOnClickListener(this);
        tvBoth.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        tvNotSure.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvEducation.setOnClickListener(this);
        tvPlaces.setOnClickListener(this);
        tvBasicInfo.setOnClickListener(this);
        tvFriends.setOnClickListener(this);
        tvFriendsCount.setOnClickListener(this);
        tvInterested.setOnClickListener(this);
        tvRelationship.setOnClickListener(this);
        etAddBio.setOnClickListener(this);
        etDob.setOnClickListener(this);
        etNickname.setOnClickListener(this);
        tvOpenForDating.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etFrom.setOnClickListener(this);
        etTo.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        llRv.setOnClickListener(this);
        tvBio.setOnClickListener(this);
        addmore.setOnClickListener(this);
        cover_photo.setOnClickListener(this);
        setClickListener(profileImage, tvProfileName, tvProfileCode, tvMale, tvFemale, tvBoth, tvYes, tvNotSure, tvNo, tvEducation, tvPlaces, tvBasicInfo, tvFriends,
                tvFriendsCount, tvInterested, tvRelationship, etAddBio, etDob, etNickname, et_gender, tvOpenForDating, llFriends, btnSave, etFrom, etTo, btnAddMore, spRelationship , btnAddMorePro,cover_photo);

        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        setDisability();
        getFriendsCount();
      // getRelationStatus();
//        setProfileData();
        viewProfileData();
        rvEdu.setVisibility(View.VISIBLE);
        rvpro.setVisibility(View.VISIBLE);
//        llProfileEdu.setVisibility(View.VISIBLE);
        iveduadd.setVisibility(View.GONE);
        iv_pro_add.setVisibility(View.GONE);
        tvEducationLatest.setVisibility(View.GONE);
        tvProfessionLatest.setVisibility(View.GONE);



        Glide.with(this)
                .load(userData.getProfilePicture())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                .into(profileImage);
        Glide.with(this)
                .load(userData.getCoverpicture())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                .into(cover_photo);

        tvProfileName.setText(userData.getName());
        // get home address
        atHomecity.setAdapter(new GooglePlaceAutocompleteAdapter(getActivity(), R.layout.place_textview_layout));
        atHomecity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String athome = (String) adapterView.getItemAtPosition(i);
                Log.e("onItemClick", athome.toString());

                homelocation=getLatLong(atHomecity.getText().toString());
                String Array[]=homelocation.split(",");
                longitutehome=Array[0];
                latitudehome=Array[1];
//                atHomecity.setBackground(getResources().getDrawable(R.drawable.edt_selected_background));
            }
        });

        //get current address
        atCurrentCity.setAdapter(new GooglePlaceAutocompleteAdapter(getActivity(), R.layout.place_textview_layout));
        atCurrentCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     final String atcity = (String) adapterView.getItemAtPosition(i);
                                                     Log.e("onItemClick", atcity.toString());
//                atCurrentCity.setBackground(getResources().getDrawable(R.drawable.edt_selected_background));

                                                     currentlocation=getLatLong(atCurrentCity.getText().toString());
                                                     String Array[]=currentlocation.split(",");
                                                     longitutecurrent=Array[0];
                                                     langitudecurrent=Array[1];
                                                 }
                                             }
        );


        //demo data for education replace with actual data and selection
//        ArrayAdapter<String> academicsAdapter = new ArrayAdapter<String>(getContext(),
//                R.layout.gender_spinner_row,
//                R.id.gender,
//                getResources().getStringArray(R.array.gender_array));
//        spAcademics.setAdapter(academicsAdapter);
//        spAcademics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                academics = String.valueOf(spAcademics.getSelectedItemId());
//                if (position == 0) {
//                    spAcademics.setBackground(getResources().getDrawable(R.drawable.edt_unselected_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ArrayAdapter<String> educationAdapter = new ArrayAdapter<String>(getContext(),
//                R.layout.gender_spinner_row,
//                R.id.gender,
//                getResources().getStringArray(R.array.relation_array));
//
//        spEducation.setAdapter(educationAdapter);
//        spEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                edu = String.valueOf(spEducation.getSelectedItemId());
//                if (position == 0) {
//                    spEducation.setBackground(getResources().getDrawable(R.drawable.edt_unselected_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(getContext(),
//                R.layout.gender_spinner_row,
//                R.id.gender,
//                getResources().getStringArray(R.array.relation_array));
//
//        spQualification.setAdapter(qualificationAdapter);
//        spQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                quali = String.valueOf(spQualification.getSelectedItemId());
//                if (position == 0) {
//                    spQualification.setBackground(getResources().getDrawable(R.drawable.edt_unselected_background));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        arrFeeds = userData.getEducationalDetail();
        proFeeds=userData.getProfessionDetail();
        eduAdapter = new EducationAdapter(getActivity(), arrFeeds, ProfileFragment.this,null);
        eduAdapter2 = new EducationAdapter(getActivity(), null, ProfileFragment.this,proFeeds);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        autoCompleteTextView.setAdapter(getEmailAddressAdapter(getContext()));
        rvEdu.setLayoutManager(layoutManager);
        rvpro.setLayoutManager(layoutManager2);
        rvEdu.setAdapter(eduAdapter);
        rvpro.setAdapter(eduAdapter2);
        eduAdapter.isClickable=false;
        eduAdapter2.isClickable=false;
        rvEdu.setClickable(false);
        rvpro.setClickable(false);
        rvEdu.setEnabled(false);
        rvpro.setEnabled(false);
        autoCompleteTextView.setThreshold(1);
//        autoCompleteTextView.showDropDown();

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autoCompleteTextView.showDropDown();
                return false;
            }
        });
    }


    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        data =(BaseManager.getDataFromPreferences(Kmasterhit, Data.class));
        if(data.getHobbies_master()!=null) {
            hobbies = (ArrayList<Hobbies>) data.getHobbies_master();
        }
        String[] addresses=null;
        if(hobbies.size()>0) {
            addresses = new String[hobbies.size()];
            for (int i = 0; i < hobbies.size(); i++) {
                addresses[i] = hobbies.get(i).getText();
            }
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }
    private void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions(getActivity(), permissions)) {
//                if (STORAGE_PERMISSION_TYPE==1)
                onPickImage();
//                else  if (STORAGE_PERMISSION_TYPE==2){
//                    onPicDocument();
//                }
////                Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
//            }else {
////                Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
//                requestPermission();
            }
        } else {
//            if (STORAGE_PERMISSION_TYPE==1)
            onPickImage();
//            else  if (STORAGE_PERMISSION_TYPE==2){
//                onPicDocument();
//            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    public void onPickImage() {
        getImagePickerDialog(getActivity(), "Select Option");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                ArrayList<String> _arPermission = new ArrayList<String>();
                if (grantResults.length > 0) {
                    Log.d(TAG, "length" + permissions.length);
                    for (int i = 0; i < permissions.length; i++) {
                        Log.d(TAG, "lengthch" + permissions[i] + " " + grantResults[i]);
                        if (grantResults[i] != 0) {
                            _arPermission.add("" + grantResults[i]);
                        }
                    }

                    if (_arPermission.size() == 0) {
                        onPickImage();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        CommonUtil.showAlert(getActivity(), "These Permissions required for this app.Go to settings and enable permissions.", "Permissions");
                    }
                }
            }
        }
    }

    /**
     * now upload image to s3 bucket and get its url
     *
     * @param
     * @param
     * @param newProfileImageUri
     */
    private void uploadPicToAmazon(Uri newProfileImageUri) {

        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(getActivity(), Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME, "ProfileMedia/" + newProfileImageUri.getLastPathSegment(), new File(newProfileImageUri.getPath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String sucess) {

                String profilepic = Constant.AWS_URL + Constant.BUCKET_NAME + "/ProfileMedia/" + newProfileImageUri.getLastPathSegment();
                Log.e("uploadPicToAmazon", "uploadPicToAmazon" + profilepic);
                cancelProgressDialog();
                if (iscover == false) {
                    updateProfilepic(profilepic, null);
                }
                else
                {
                    updateProfilepic(null, profilepic);
                }
            }

            @Override
            public void error(String errormsg) {

                Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", "" + errormsg);
            }
        });
    }

    private void updateProfilepic(String profilepic,String coverpic) {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<UpdateProfile> call = Service.getUpdatedProfile(profilepic,coverpic);

            call.enqueue(new Callback<UpdateProfile>() {
                @Override
                public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                    cancelProgressDialog();

                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    UpdateProfile updateProfile = response.body();
                    if (updateProfile.getStatus()) {

                        BaseManager.saveDataIntoPreferences(updateProfile.getData(), kCurrentUser);


                        Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();

                        ((HomeActivity) getActivity()).popStack();

                    } else {
                        if (updateProfile.getMessage().equals(110110)) {
                            ((HomeActivity) getActivity()).logout();

                        } else {
                            snackBar(updateProfile.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfile> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
//        }else{

        }

    }

    private void showEditNameDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.edit_profile_name);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);
        TextView tvSend = dialog.findViewById(R.id.tv_send);
        TextInputEditText etProfileName = dialog.findViewById(R.id.et_edit_name);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String editName = etProfileName.getText().toString().trim();
                tvProfileName.setText(editName);

            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_interest:
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                addmore.setEnabled(true);
                autoCompleteTextView.setEnabled(true);
                break;
            case R.id.tv_bio:
//                setEnability();
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                etAddBio.setEnabled(true);
                tvBio.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_editImage:
                permissionCheck();
                iscover=true;
                break;
            case R.id.profile_image:
                permissionCheck();
                break;
            case R.id.tv_education:
//                setEnability();
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                spAcademics.setEnabled(true);
                spEducation.setEnabled(true);
                spQualification.setEnabled(true);
                etFrom.setEnabled(true);
                etTo.setEnabled(true);
                rvEdu.setEnabled(true);
                eduAdapter.isClickable=true ;
                btnAddMore.setVisibility(View.VISIBLE);
                tvEducation.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_profession:
//                setEnability();
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                rvpro.setEnabled(true);
                eduAdapter2.isClickable=true ;
                btnAddMorePro.setVisibility(View.VISIBLE);
                tvEducation.setCompoundDrawables(null, null, null, null);
                break;

            case R.id.tv_places:
//                setEnability();
                atCurrentCity.setEnabled(true);
                atHomecity.setEnabled(true);
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                tvPlaces.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_basic_info:
//                setEnability();
//                spGender.setEnabled(true);
                etDob.setEnabled(true);
                etNickname.setEnabled(true);
                et_gender.setEnabled(true);
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                tvBasicInfo.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_interested:
//                setEnability();
                tvMale.setEnabled(true);
                tvFemale.setEnabled(true);
                tvBoth.setEnabled(true);
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                tvInterested.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_relationship:
//              setEnability();
                spRelationship.setEnabled(true);
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                tvRelationship.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_open_for_dating:
//                setEnability();
                tvYes.setEnabled(true);
                tvNo.setEnabled(true);
                tvNotSure.setEnabled(true);
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                tvOpenForDating.setCompoundDrawables(null, null, null, null);
                break;

            case R.id.et_dob:
                hideKeyboard(getActivity());
                CommonUtil.DobDatePicker(getActivity(), etDob, ProfileFragment.this);
                break;

            case R.id.btn_add_more:
//                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//                android.app.Fragment fragment =  new Fragment();
//                EducationFragment educationFragment = new EducationFragment();
//                educationFragment.setTargetFragment(ProfileFragment.this, AppController.FRAGMENT_CODE);
//                ft.replace(R.id.container, fragment).commit();

//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                EducationFragment dialogFragment = new EducationFragment();
                dialogFragment.setTargetFragment(this, REQUEST_CODE);
                dialogFragment.show(fm, "fragment_education");


//                ((HomeActivity) getActivity()).replaceFragment(EducationFragment.newInstance());
                break;

            case R.id.btn_add_morepro:

                FragmentManager profm = getActivity().getSupportFragmentManager();
                ProfessionFragment prodialogFragment = new ProfessionFragment();
                prodialogFragment.setTargetFragment(this, REQUEST_CODEPRO);
                prodialogFragment.show(profm, "fragment_education");

                break;

            case R.id.et_from:
                hideKeyboard(getActivity());
                CommonUtil.DatePicker(getActivity(), etFrom, ProfileFragment.this);
                break;
            case R.id.et_to:
                hideKeyboard(getActivity());
                CommonUtil.DatePicker(getActivity(), etTo, ProfileFragment.this);
                break;

            case R.id.btn_save:
                //to send education data to save
//                newEducationDetail = new NewEducationDetail("",
//                        String.valueOf(spEducation.getSelectedItemId()),
//                        String.valueOf(spQualification.getSelectedItemId()),
//                        "28.344", "76.7324",
//                        etFrom.getText().toString(),
//                        etTo.getText().toString(),
//                        String.valueOf(spAcademics.getSelectedItemId()));

//                details.add(newEducationDetail);

//                details.clear();

//                if(checkValidity()) {

                Gson gson = new Gson();
                educationJson = gson.toJson(details);

                professionJson = gson.toJson(Professiondetails);

                size = userData.getLocationDetail().size();
                if(userData.getLocationDetail().size() >= 2){
                    addressDetailsHome = new AddressDetails(userData.getLocationDetail().get(size - 2).getId().toString(),atHomecity.getText().toString(), latitudehome, longitutehome, "home");
                    addressDetailsCurrent = new AddressDetails(userData.getLocationDetail().get(size - 1).getId().toString(),atCurrentCity.getText().toString(), langitudecurrent, longitutecurrent, "current");
                } else {
                    addressDetailsHome = new AddressDetails("",atHomecity.getText().toString(), latitudehome, longitutehome, "home");
                    addressDetailsCurrent = new AddressDetails("",atCurrentCity.getText().toString(), langitudecurrent, longitutecurrent, "current");
                }

                location_details.add(addressDetailsHome);
                location_details.add(addressDetailsCurrent);
                locationJson = gson.toJson(location_details);
                eduAdapter.isClickable=false ;
                if(arrayList.size()>0)
                {
                    Boolean check=false;

                    currenthobbies=new ArrayList<>();
                    for(int i=0;i<arrayList.size();i++)
                    {
                        for(int j=0;j<hobbies.size();j++)
                        {
                            if(arrayList.get(i).equals(hobbies.get(j).getText()))
                            {
                                check=true;
                                currenthobbies.add(hobbies.get(j));
                                break;
                            }
                            else
                            {
                                check=false;
                            }
                        }
                        if(check==false)
                        {
                            Hobbies hobbies=new Hobbies();
                            hobbies.setId("");
                            hobbies.setText(arrayList.get(i));
                            currenthobbies.add(hobbies);
                        }
                    }
                    if(currenthobbies.size()>0)
                    {
                        sendhobies=new Gson().toJson(currenthobbies);
                    }
                }


                setDisability();
                getSavedData();
                ((HomeActivity) getActivity()).navigation.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                btnAddMore.setVisibility(View.GONE);
                btnAddMorePro.setVisibility(View.GONE);

                updateProfile();
//                } else{
//                    Toast.makeText(getActivity(), "Please enter valid Dates", Toast.LENGTH_SHORT).show();
//                }

                break;
            case R.id.ll_friends:
            case R.id.tv_friends:
            case R.id.tv_friends_count:
                ((HomeActivity) getActivity()).replaceFragment(FriendsListFragment.newInstance(2), true);
                break;

            case R.id.tv_male:
                interestedId = "1";
                tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                break;

            case R.id.tv_female:
                interestedId = "2";
                tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                break;
            case R.id.tv_both:
                interestedId = "0";
                tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                break;

            case R.id.tv_yes:
                openDatingId = "1";
                tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                break;
            case R.id.tv_no:
                openDatingId = "2";
                tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                break;

            case R.id.tv_not_sure:
                openDatingId = "0";
                tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
                tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
                break;


            case R.id.et_gender:
                selectGender(et_gender);
                break;


            case R.id.spinner_relationship:
                selectRelationStatus(spRelationship);
                break;


            case R.id.tv_profile_name:
                ((HomeActivity) getActivity()).navigation.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                showEditNameDialog();
                hideKeyboard(getActivity());
                break;


            case R.id.cover_photo:
                ((HomeActivity) getActivity()).replaceFragment(ZoomImage.newInstance(userData.getCoverpicture()),true);
                break;

            case  R.id.addmore:
                Hobii(true);
                text = autoCompleteTextView.getText().toString();
                if(text==null|| text.equals(""))
                {
                    Toast.makeText(getContext(),"Please enter valid details",Toast.LENGTH_SHORT).show();
                }
                else {
                    arrayList.add(text);
                    hobbieAdapter.notifyDataSetChanged();
                    autoCompleteTextView.setText("");
                }
//                arrayListinterest.add(autoCompleteTextView.getText().toString());
                break;


        }

    }

    private void Hobii(boolean b) {
        String hobbitext="";
        if(b==true)
        {
            if(autoCompleteTextView.getText().toString().equals(""))
            {
                Toast.makeText(getContext(),"plese enter hobbies",Toast.LENGTH_SHORT).show();
            }
            else
            {
                arrayListinterest.add(autoCompleteTextView.getText().toString());
            }

        }
        else if(b==false) {
            if (arrayListinterest.size() == 0) {
                Toast.makeText(getContext(),"plese enter hobbies",Toast.LENGTH_SHORT).show();
            } else {
                arrayListinterest.remove(arrayListinterest.size() - 1);
            }
        }
        for(int i=0;i<arrayListinterest.size();i++)
        {
            hobbitext=hobbitext+","+arrayListinterest.get(i);
        }

    }

    private void getFriendsCount() {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call = Service.getFriends(1, 20,"","","");
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        getRelationStatus();
                        size = peopleBase.getData().size();
                        if (size < 10) {
                            tvFriendsCount.setText("0" + String.valueOf(size));
                        } else {
                            tvFriendsCount.setText(String.valueOf(size));
                        }
                    } else {
                        if (peopleBase.getMessage().equals("110110")) {
                            ((HomeActivity) getActivity()).logout();
                        } else {
//                            snackBar(peopleBase.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PeopleBase> call, Throwable t) {
                    cancelProgressDialog();
                    getRelationStatus();

                }
            });
//        }else{

        }

    }

    private void updateProfile() {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<UpdateProfile> call = Service.getUpdatedProfile(
                    profileName,
//                      ssnid,
                    bio, professional_details,education_details, locationJson, gender, dob.replace("/","-"), nickname, interestedIn, relationStatus, openDating,sendhobies);

            call.enqueue(new Callback<UpdateProfile>() {
                @Override
                public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    UpdateProfile updateProfile = response.body();
                    if (updateProfile.getStatus()) {

                        BaseManager.saveDataIntoPreferences(updateProfile.getData(), kCurrentUser);

                        //save these data when profile update api hits successfully
//                        AppController.getManager().setProfile_image(profileImage);
//                        AppController.getManager().setName(profileName);
//                        AppController.getManager().setSSNID(ssnid);
//                        AppController.getManager().setGender(gender);
//                        AppController.getManager().setDob(dob);
//                        AppController.getManager().setBio(bio);
//                        AppController.getManager().setNickname(nickname);
//                        AppController.getManager().setRelation_status(relationStatus);
//                        AppController.getManager().setInterested_in(interestedIn);
//                        AppController.getManager().setOpen_dating_id(openDatingId);
//                        AppController.getManager().setEducation(education_details);
//                        AppController.getManager().setProfession(professional_details);
//                        AppController.getManager().setHometown(atHomecity.getText().toString());
//                        AppController.getManager().setCurrent_city(atCurrentCity.getText().toString());

                        Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();

                        ((HomeActivity) getActivity()).replaceFragment(ViewProfileFragment.newInstance(userData.getId(),null), true);

                    } else {
                        if (updateProfile.getMessage().equals(110110)) {
                            ((HomeActivity) getActivity()).logout();

                            snackBar(updateProfile.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfile> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
//        }else{

        }

    }


    ArrayList<RelationStatus> getRelaStatus = new ArrayList<>();
    private void getRelationStatus() {
        if (CommonUtil.isConnectingToInternet(getActivity())) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GetRelationStatus> call = Service.getRelationStatus(userData.getId());
            call.enqueue(new Callback<GetRelationStatus>() {
                @Override
                public void onResponse(Call<GetRelationStatus> call, Response<GetRelationStatus> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    GetRelationStatus getRelationStatus = response.body();
                    if (getRelationStatus.getStatus()) {
                        getRelaStatus.clear();
                        getRelaStatus.addAll(getRelationStatus.getData());
                    } else {
                        if (getRelationStatus.getMessage().equals("110110")) {
                            ((HomeActivity) getActivity()).logout();
                        } else {
//                            snackBar(peopleBase.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetRelationStatus> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
//        }else{

        }

    }

    @SuppressLint("RestrictedApi")
    private void selectRelationStatus(View view) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenu().clear();
        for(int i=0; i<getRelaStatus.size(); i++) {
            popupMenu.getMenu().add(getRelaStatus.get(i).getName());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                spRelationship.setText(item.getTitle());
                for(int i= 0; i<getRelaStatus.size(); i++){
                    if(item.getTitle().equals(getRelaStatus.get(i).getName())){
                        relationStatus = String.valueOf(getRelaStatus.get(i).getId());
                        break;
                    }
                }

//                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }


    public boolean checkValidity() {
        if (!TextUtils.isEmpty(etFrom.getText()) && !TextUtils.isEmpty(etTo.getText())) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom, dateTo;
            long timeFrom, timeTo;
            try {
                dateFrom = dateFormat.parse(etFrom.getText().toString());
                dateTo = dateFormat.parse(etTo.getText().toString());
                timeFrom = dateFrom.getTime();
                timeTo = dateTo.getTime();

                if (timeFrom < timeTo) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public void getDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_TRADITIONAL, this, year, month, day);

        //Get the DatePicker instance from DatePickerDialog
        DatePicker dp = dpd.getDatePicker();
        //Set the DatePicker minimum date selection to current date
        dp.setMinDate(c.getTimeInMillis());//get the current day
        //dp.setMinDate(System.currentTimeMillis() - 1000);// Alternate way to get the current day

        //Add 6 days with current date
        c.add(Calendar.DAY_OF_MONTH, 6);

        //Set the maximum date to select from DatePickerDialog
        dp.setMaxDate(c.getTimeInMillis());
        //Now DatePickerDialog have 7 days range to get selection any one from those dates
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) getActivity()).navigation.setVisibility(View.VISIBLE);

    }

    public void getImagePickerDialog(final Activity ctx, final String title) {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(ctx);

        alertBuild
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getImageFromCamera();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getImageFromGallery();
                    }
                });
        AlertDialog dialog = alertBuild.create();
        dialog.show();
        int alertTitle = ctx.getResources().getIdentifier("alertTitle", "id", "android");
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
    }

    private void getImageFromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/* video/*");
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }

    /**
     * get image from camera
     */
    private void getImageFromCamera() {
        checkStorage();
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }

    public void checkStorage() {
        imageName = "";
        state = Environment.getExternalStorageState();

        imageName = Constant.PARENT_FOLDER + "_" + String.valueOf(System.nanoTime()) + ".png";

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newFile = new File(Environment.getExternalStorageDirectory(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        } else {
            newFile = new File(getActivity().getFilesDir(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        }
        Log.e("createVideofile", "newProfileImageUri" + newProfileImageUri);
        // Log.e("createVideofile","imageName"+imageName);
    }

    public void getSavedData() {
//        profileImage = profileImage.
        profileName = tvProfileName.getText().toString();
        ssnid = tvProfileCode.getText().toString();
        bio = etAddBio.getText().toString();
        dob = etDob.getText().toString();
        if (et_gender.getText().toString().trim().equalsIgnoreCase(getString(R.string.male))) {
            gender = "0";
        } else {
            gender = "1";
        }
        nickname = etNickname.getText().toString();

        education_details = educationJson;
        professional_details=professionJson;

        interestedIn = interestedId;

//        if (spRelationship.getText().toString().trim().equalsIgnoreCase("Single")) {
//            relationStatus = "0";
//        } else if (spRelationship.getText().toString().trim().equalsIgnoreCase("In a relationship")) {
//            relationStatus = "1";
//        } else if (spRelationship.getText().toString().trim().equalsIgnoreCase("Engaged")) {
//            relationStatus = "2";
//        } else if (spRelationship.getText().toString().trim().equalsIgnoreCase("Married")) {
//            relationStatus = "3";
//        } else if (spRelationship.getText().toString().trim().equalsIgnoreCase("Divorced")) {
//            relationStatus = "4";
//        } else if (spRelationship.getText().toString().trim().equalsIgnoreCase("It's complicated")) {
//            relationStatus = "5";
//        }
//        relationStatus = spRelationship.getText().toString();
//        gender = String.valueOf(spGender.getSelectedItemId());
        openDating = openDatingId;
        hometown = atHomecity.getText().toString();
        current_address = atCurrentCity.getText().toString();


        tvEducation.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvBasicInfo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvOpenForDating.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvRelationship.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvPlaces.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvInterested.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);
        tvBio.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getActivity().getResources().getDrawable(R.drawable.edit), null);

    }

    public void viewProfileData() {

        tvProfileCode.setText(userData.getSsn().toString());
//        Glide.with(getActivity()).load(userData.getCover_picture())
//               .apply((RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy)))
//               .into(cover_photo);
        etAddBio.setText(!TextUtils.isEmpty(userData.getBio()) ? userData.getBio() : "");
        String dob = userData.getDob().replace("-", "");
        if (dob.length() <= 8) {
            etDob.setText(userData.getDob());
        } else {
            Timestamp ts = new Timestamp(Long.parseLong(userData.getDob()) * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = ts;
            try {
                date = (Date) formatter.parse(String.valueOf(ts.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            etDob.setText(!TextUtils.isEmpty(date.toString()) ? formatter.format(date) : "");

        }
        etNickname.setText(!TextUtils.isEmpty(userData.getNickName()) ? userData.getNickName() : "");

        et_gender.setText(!TextUtils.isEmpty(userData.getGender()) ?
                userData.getGender().equalsIgnoreCase("0") ?
                        getString(R.string.male) : getString(R.string.female) : "");

//        spRelationship.setText(!TextUtils.isEmpty(updateProfileResult.getRelationShipId()) ?
//                updateProfileResult.getRelationShipId().equalsIgnoreCase("0") ?
//                        "Single" : updateProfileResult.getRelationShipId().equalsIgnoreCase("1") ?
//                        "In a relationship" : updateProfileResult.getRelationShipId().equalsIgnoreCase("2") ?
//                        "Engaged" : updateProfileResult.getRelationShipId().equalsIgnoreCase("3") ?
//                        "Married" : updateProfileResult.getRelationShipId().equalsIgnoreCase("4") ?
//                        "Divorced" : "It's complicated"
////                        updateProfileResult.getRelationShipId().equalsIgnoreCase("5") ?
////                        "It's complicated" : ""
//                : "");

        spRelationship.setText(userData.getRelationShipId().toString());
//        spGender.setSelection(!TextUtils.isEmpty(updateProfileResult.getGender()) ? Integer.parseInt(updateProfileResult.getGender()) : 0);

        int size = userData.getLocationDetail().size();
        if (size >= 2) {
            atHomecity.setText((!TextUtils.isEmpty(userData.getLocationDetail().get(size - 2).getLocation()) ?
                    userData.getLocationDetail().get(size - 2).getLocation() : " Delhi, India"));
            atCurrentCity.setText((!TextUtils.isEmpty(userData.getLocationDetail().get(size - 1).getLocation()) ?
                    userData.getLocationDetail().get(size - 1).getLocation() : " Delhi, India"));
        }
        else if(size==1)
        {
            atHomecity.setText(
                    (""
                    )
            );
            atCurrentCity.setText((!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                    userData.getLocationDetail().get(0).getLocation() :
                    " Delhi, India"
            ) );
        }
        else {
            atHomecity.setText(
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"
                    )
            );
            atCurrentCity.setText(
                    (!TextUtils.isEmpty(userData.getLocationDetail().get(0).getLocation()) ?
                            userData.getLocationDetail().get(0).getLocation() :
                            " Delhi, India"
                    )
            );
        }

        hometown = atHomecity.getText().toString();
        current_address = atCurrentCity.getText().toString();

        if(!(hometown==null || hometown.equals(""))) {
            homelocation=getLatLong(hometown);
            String Array[] = homelocation.split(",");
            longitutehome = Array[0];
            langitudecurrent = Array[1];
        }

        if(!(current_address==null || current_address.equals(""))) {
            currentlocation=getLatLong(current_address);
            String Array2[]=currentlocation.split(",");
            longitutecurrent=Array2[0];
            langitudecurrent=Array2[1];
        }
        if(!TextUtils.isEmpty(userData.getHobbies()))
        {
            String array[]=userData.getHobbies().split(",");
            for (int i=0;i<array.length;i++)
            {
                arrayList.add(array[i]);
            }
        }

        hobbieAdapter = new HobbieAdapter(arrayList);
        recyclerView.setAdapter(hobbieAdapter);
        if (userData.getInterestedIn().equals("1")) {

            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
        } else if (userData.getInterestedIn().equals("2")) {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (userData.getInterestedIn().equals("0")) {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);

        } else {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
        }

        if (userData.getOpenDating().equals("1")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (userData.getOpenDating().equals("2")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (userData.getOpenDating().equals("0")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);

        } else {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
        }

        education_details = educationJson;
        professional_details=professionJson;

    }

    public void setProfileData() {
//        profileName = tvProfileName.getText().toString();
//        ssnid = tvProfileCode.getText().toString();
//        bio = etAddBio.getText().toString();
//        dob = etDob.getText().toString();
//        nickname = etNickname.getText().toString();
//        relationStatus = String.valueOf(spRelationship.getSelectedItemId());
//        gender = String.valueOf(spGender.getSelectedItemId());
//        hometown_new = atHomecity.getText().toString();
//        current_address = atCurrentCity.getText().toString();
//        openDating = openDatingId;
//        interestedIn = interestedId;

        etAddBio.setText(AppController.getManager().getBio().toString());
        etDob.setText(AppController.getManager().getDob().toString());
        etNickname.setText(AppController.getManager().getNickname().toString());

        spRelationship.setSelection(Integer.parseInt(AppController.getManager().getRelation_status()));

        spGender.setSelection(Integer.parseInt(AppController.getManager().getGender()));
        atHomecity.setText(hometown);

        if(!(hometown==null && hometown.equals(""))) {
            homelocation=getLatLong(hometown);
            String Array[] = homelocation.split(",");
            longitutehome = Array[0];
            langitudecurrent = Array[1];
        }
        atCurrentCity.setText(current_address);

        if(!(current_address==null && current_address.equals(""))) {
            currentlocation=getLatLong(current_address);
            String Array2[]=currentlocation.split(",");
            longitutecurrent=Array2[0];
            langitudecurrent=Array2[1];
        }

        if (interestedId.equals("1")) {

            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
        } else if (interestedId.equals("2")) {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (interestedId.equals("0")) {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);

        } else {
            tvMale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvFemale.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvBoth.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
        }

        if (openDatingId.equals("1")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (openDatingId.equals("2")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);

        } else if (openDatingId.equals("0")) {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);

        } else {
            tvYes.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_active), null);
            tvNo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
            tvNotSure.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getActivity().getResources().getDrawable(R.drawable.redio_button_gray), null);
        }

        education_details = educationJson;
        professional_details=professionJson;
    }

    @SuppressLint("RestrictedApi")
    private void selectGender(View view) {
        MenuBuilder menuBuilder = new MenuBuilder(activity);
        MenuInflater inflater = new MenuInflater(activity);
        inflater.inflate(R.menu.gender_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(activity, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                et_gender.setText(item.getTitle());
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        optionsMenu.show();
    }

//    @SuppressLint("RestrictedApi")
//    private void selectRelation(View view) {
//        MenuBuilder menuBuilder = new MenuBuilder(activity);
//        MenuInflater inflater = new MenuInflater(activity);
//        inflater.inflate(R.menu.relation_menu, menuBuilder);
//        MenuPopupHelper optionsMenu = new MenuPopupHelper(activity, menuBuilder, view);
//        optionsMenu.setForceShowIcon(true);
//        menuBuilder.setCallback(new MenuBuilder.Callback() {
//            @Override
//            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
//                spRelationship.setText(item.getTitle());
//                return true;
//            }
//
//            @Override
//            public void onMenuModeChange(MenuBuilder menu) {
//            }
//        });
//        optionsMenu.show();
//    }

    /**
     * get selected file in onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {

            try {
                // When an Image is picked
                if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
                        && null != data) {

                    // Get the Image from data
                    if (data.getData() != null) {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        checkStorage();
                        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                        CommonUtil.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        File compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                        Uri uri2 = Uri.fromFile(compressedImgFile);

                        if(iscover==true)
                        {
                            cover_photo.setImageURI(uri2);
                        }
                        else {
                            profileImage.setImageURI(uri2);

                        }
                        showProgressDialog();
                        uploadPicToAmazon(uri2);
                    } else {
                        Toast.makeText(getActivity(), "You haven't picked any Media",
                                Toast.LENGTH_LONG).show();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong, Try again...", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            if (newProfileImageUri != null) {
                if(iscover==true)
                {
                    File compressedImgFile = null;
                    try {
                        compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                        uri = Uri.fromFile(compressedImgFile);
                        cover_photo.setImageURI(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    File compressedImgFile = null;
                    try {
                        compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                        uri = Uri.fromFile(compressedImgFile);
                        profileImage.setImageURI(newProfileImageUri);

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                showProgressDialog();
                uploadPicToAmazon(uri);
            }
        } else if (requestCode == REQUEST_CODE)  {

            if(resultCode==10002)
            {

                company = data.getExtras().get("company").toString();
                designation = data.getExtras().get("designation").toString();
                joiningdate = data.getExtras().get("joiningdate").toString();
                leftdate = data.getExtras().get("leftdate").toString();
                proid = data.getExtras().get("eduId").toString();
                if (proid.equals("")) {
                    proid = "";
                }

                newProfessionDetails = new NewProfessionDetails(proid,company,designation,"noida","28.434", "75.747",joiningdate,leftdate);
                Professiondetails.add(newProfessionDetails);

                tvProfessionLatest.setText(company +"\n" +designation +"\n"+joiningdate+" - "+leftdate);
                tvProfessionLatest.setVisibility(View.VISIBLE);
                iv_pro_add.setVisibility(View.VISIBLE);
            }
            else {
                eduId = data.getExtras().get("eduId").toString();
                academicsId = data.getExtras().get("academicsId").toString();
                educationId = data.getExtras().get("educationId").toString();
                qualiId = data.getExtras().get("qualificationId").toString();
                fromYear = data.getExtras().get("fromYear").toString();
                toYear = data.getExtras().get("toYear").toString();
                if (eduId.equals("")) {
                    eduId = "";
                }

                newEducationDetail = new NewEducationDetail(eduId, educationId, qualiId, "28.434", "75.747", fromYear, toYear, academicsId);
                details.add(newEducationDetail);

                tvEducationLatest.setText(educationId + "\n" + qualiId
                        + " \n " + fromYear
                        + " - " + toYear);
                tvEducationLatest.setVisibility(View.VISIBLE);
                iveduadd.setVisibility(View.VISIBLE);

//            }
            }
        }
        else if(requestCode == REQUEST_CODEPRO)
        {
            proid = data.getExtras().get("eduId").toString();
            company = data.getExtras().get("company").toString();
            designation = data.getExtras().get("designation").toString();
            joiningdate = data.getExtras().get("joiningdate").toString();
            leftdate = data.getExtras().get("leftdate").toString();
            if (proid.equals("")) {
                proid = "";
            }
            newProfessionDetails = new NewProfessionDetails(proid,company,designation,"noida","28.434", "75.747",joiningdate,leftdate);
            Professiondetails.add(newProfessionDetails);

            tvProfessionLatest.setText(company +"\n" +designation +"\n"+joiningdate+" - "+leftdate);
            tvProfessionLatest.setVisibility(View.VISIBLE);
            iv_pro_add.setVisibility(View.VISIBLE);
        }
    }
    public String getLatLong(String myAddress){
        Geocoder coder = new Geocoder(getActivity());
        String latlan=null;
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(myAddress, 1);
            for(Address add : adresses){
                if (add.hasLatitude() && add.hasLongitude()) {//Controls to ensure it is right address such as country etc.
                    latlan  = add.getLongitude()+","+add.getLatitude();;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latlan;
    }
}

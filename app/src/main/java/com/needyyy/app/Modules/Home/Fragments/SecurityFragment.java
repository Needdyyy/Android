package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.UserListAdapter;
import com.needyyy.app.Modules.Login.Fragments.ChangePasswordFragment;
import com.needyyy.app.Modules.Login.Fragments.CreatePinFragment;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.models.UpdateProfile;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class SecurityFragment extends BaseFragment implements View.OnClickListener {

    private UserDataResult userData;
    private UserListAdapter userListAdapter ;
    private AppTextView tvChangePin,tvChangePassword;
    private RadioGroup privacy;
    private Switch enablefingure,notifiation;
    private RadioButton rb_public,rb_private;
    public String last_post_id;
    public String tag_id = "";
    public int previousTotalItemCount;
    public int firstVisibleItem, visibleItemCount, totalItemCount;
    public boolean isRefresh;

    private SwipeRefreshLayout swipeRefreshLayout ;
    public static SecurityFragment newInstance() {
        SecurityFragment fragment = new SecurityFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_securyity);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.search_hint);//
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {
        rb_public=mView.findViewById(R.id.rb_public);
        rb_private=mView.findViewById(R.id.rb_private);
        privacy=mView.findViewById(R.id.privacy);
        enablefingure=mView.findViewById(R.id.enablefingure);
        notifiation=mView.findViewById(R.id.notifiation);
        tvChangePin       = mView.findViewById(R.id.tv_changepin);
        tvChangePassword  = mView.findViewById(R.id.tv_changepassword);
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        if(userData.getIsPrivate().equals("0")) {
            rb_private.setChecked(false);
            rb_public.setChecked(true);
        }
        else if(userData.getIsPrivate().equals("1"))
        {
            rb_private.setChecked(true);
            rb_public.setChecked(false);
        }
        if(userData.getIs_fingerprint_enable().equals("0"))
        {
            enablefingure.setChecked(false);
        }
        else
        {
            enablefingure.setChecked(true);
        }
        if(userData.getNoti_status().equals("0"))
        {
            notifiation.setChecked(false);
        }
        else
        {
            notifiation.setChecked(true);
        }

        privacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_public:
                        hitapi("0");
                        userData.setIsPrivate("0");
                       // Toast.makeText(getContext(),"yes",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_private:
                        userData.setIsPrivate("1");
                        hitapi("1");
                        // do operations specific to this selection
                        break;
                }
            }
        });

        enablefingure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hitapi2("1");
                } else {
                    hitapi2("0");
                }
            }




            private void hitapi2(String s) {

                if (CommonUtil.isConnectingToInternet(getActivity())) {
                    showProgressDialog();
                    WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
                    Call<UpdateProfile> call = Service.updatefingure(s);

                    call.enqueue(new Callback<UpdateProfile>() {
                        @Override
                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                            cancelProgressDialog();
                            Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                            UpdateProfile updateProfile = response.body();
                            if (updateProfile.getStatus()) {
                                BaseManager.saveDataIntoPreferences(updateProfile.getData(), kCurrentUser);
//                            AppController.getManager().setGender(gender);
//                            AppController.getManager().setDob(dob);
//                            AppController.getManager().setBio(bio);
//                            AppController.getManager().setNickname(nickname);
//                            AppController.getManager().setRelation_status(relationStatus);
//                            AppController.getManager().setInterested_in(interestedIn);
//                            AppController.getManager().setOpen_dating_id(openDatingId);
//                            AppController.getManager().setEducation(education_details);
//                            AppController.getManager().setProfession(professional_details);
//                            AppController.getManager().setHometown(atHomecity.getText().toString());
//                            AppController.getManager().setCurrent_city(atCurrentCity.getText().toString());

                                Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_SHORT).show();

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
        });


        // notification setting



        notifiation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hitapi2("1");
                } else {
                    hitapi2("0");
                }
            }




            private void hitapi2(String s) {

                if (CommonUtil.isConnectingToInternet(getActivity())) {
                    showProgressDialog();
                    WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
                    Call<UpdateProfile> call = Service.updatenotification(s);

                    call.enqueue(new Callback<UpdateProfile>() {
                        @Override
                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                            cancelProgressDialog();
                            Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                            UpdateProfile updateProfile = response.body();
                            if (updateProfile.getStatus()) {
                                BaseManager.saveDataIntoPreferences(updateProfile.getData(), kCurrentUser);
//                            AppController.getManager().setGender(gender);
//                            AppController.getManager().setDob(dob);
//                            AppController.getManager().setBio(bio);
//                            AppController.getManager().setNickname(nickname);
//                            AppController.getManager().setRelation_status(relationStatus);
//                            AppController.getManager().setInterested_in(interestedIn);
//                            AppController.getManager().setOpen_dating_id(openDatingId);
//                            AppController.getManager().setEducation(education_details);
//                            AppController.getManager().setProfession(professional_details);
//                            AppController.getManager().setHometown(atHomecity.getText().toString());
//                            AppController.getManager().setCurrent_city(atCurrentCity.getText().toString());

                                Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_SHORT).show();

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
        });

        // end of notification
    }

    private void hitapi(String s) {

            if (CommonUtil.isConnectingToInternet(getActivity())) {
                showProgressDialog();
                WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
                Call<UpdateProfile> call = Service.updateprivacy(s);

                call.enqueue(new Callback<UpdateProfile>() {
                    @Override
                    public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                        cancelProgressDialog();
                        Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                        UpdateProfile updateProfile = response.body();
                        if (updateProfile.getStatus()) {

                            BaseManager.saveDataIntoPreferences(updateProfile.getData(), kCurrentUser);

//                            AppController.getManager().setGender(gender);
//                            AppController.getManager().setDob(dob);
//                            AppController.getManager().setBio(bio);
//                            AppController.getManager().setNickname(nickname);
//                            AppController.getManager().setRelation_status(relationStatus);
//                            AppController.getManager().setInterested_in(interestedIn);
//                            AppController.getManager().setOpen_dating_id(openDatingId);
//                            AppController.getManager().setEducation(education_details);
//                            AppController.getManager().setProfession(professional_details);
//                            AppController.getManager().setHometown(atHomecity.getText().toString());
//                            AppController.getManager().setCurrent_city(atCurrentCity.getText().toString());

                            Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_SHORT).show();



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

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.setting), "2");
        tvChangePin.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       // boolean  checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.tv_changepin:
                ((HomeActivity)getActivity()).replaceFragment(CreatePinFragment.newInstance(2,""), true);
                break;
            case R.id.tv_changepassword:
                ((HomeActivity)getActivity()).replaceFragment(ChangePasswordFragment.newInstance(), true);
                break;
        }
    }
}

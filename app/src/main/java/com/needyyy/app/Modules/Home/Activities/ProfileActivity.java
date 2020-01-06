package com.needyyy.app.Modules.Home.Activities;//package com.needyyy.app.Modules.Home.Activities;
//
//import android.content.Intent;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.needyyy.AppController;
//import com.needyyy.app.Modules.Home.Adapters.SingleFeedsAdapter;
//import com.needyyy.app.Modules.Login.Activities.LoginActivity;
//import com.needyyy.app.Modules.Profile.models.UpdateProfile;
//import com.needyyy.app.Modules.Profile.models.UpdateProfileResult;
//import com.needyyy.app.R;
//import com.needyyy.app.utils.CommonUtil;
//import com.needyyy.app.webutils.WebInterface;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
//
//    CircleImageView profile_circle_iv;
//    TextView tv_title, tv_name, tv_dob, tv_from, tv_address, tv_error;
//    RecyclerView rvFeeds;
//    SingleFeedsAdapter singleFeedsAdapter;
//    ArrayList<String> arrFeeds = new ArrayList<String>();
//    public Toolbar toolbar;
//    ImageView btn_back;
//    UpdateProfileResult updateProfileResult;
//    CoordinatorLayout profile;
//    NestedScrollView nestedView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        getIntentData();
//        initViews();
//        setClickListener();
//        setFeedRv();
//        callProfileApi();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    private void getIntentData() {
//
//    }
//
//    private void initViews() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        nestedView = findViewById(R.id.nestedView);
//        profile = findViewById(R.id.profile);
//        btn_back = findViewById(R.id.btn_back);
//        profile_circle_iv = findViewById(R.id.profile_circle_iv);
//        tv_title = findViewById(R.id.tv_title);
//        tv_name = findViewById(R.id.tv_name);
//        tv_dob = findViewById(R.id.tv_dob);
//        tv_from = findViewById(R.id.tv_from);
//        tv_address = findViewById(R.id.tv_address);
//        rvFeeds = findViewById(R.id.rv_feeds);
//        tv_error = findViewById(R.id.tv_error);
//
//        tv_title.setText("Profile");
//    }
//
//    private void setClickListener() {
//        btn_back.setOnClickListener(this);
//    }
//
//    private void setProfileData() {
//        tv_error.setVisibility(View.GONE);
//        nestedView.setVisibility(View.VISIBLE);
//        profile.setVisibility(View.VISIBLE);
//
//        tv_name.setText(!TextUtils.isEmpty(updateProfileResult.getName()) ? updateProfileResult.getName() : "Ambuj Sukla");
//        tv_dob.setText(!TextUtils.isEmpty(updateProfileResult.getDob()) ? updateProfileResult.getDob() : "10 Sep, 1990");
//        tv_from.setText( "From Chandigarh");
//        tv_address.setText( "Lives in Delhi, India");
//
//        Glide.with(this)
//                .load(updateProfileResult.getProfilePicture())
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.amritanshu).error(R.drawable.amritanshu))
//                .into(profile_circle_iv);
//    }
//
//    public void setFeedRv() {
//        rvFeeds.setVisibility(View.VISIBLE);
//        singleFeedsAdapter = new SingleFeedsAdapter(this, arrFeeds);
//        rvFeeds.setLayoutManager(new LinearLayoutManager(this));
//        rvFeeds.setAdapter(singleFeedsAdapter);
//        rvFeeds.setNestedScrollingEnabled(false);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_back:
//                onBackPressed();
//                break;
//            case R.id.tv_error:
//                tv_error.setVisibility(View.GONE);
//                nestedView.setVisibility(View.VISIBLE);
//                callProfileApi();
//                break;
//        }
//    }
//
//    public void callProfileApi() {
//        if (CommonUtil.isConnectingToInternet(this)) {
//            CommonUtil.showProgress(this);
//            WebInterface Service = AppController.getRetrofitInstance().create(WebInterface.class);
//            Call<UpdateProfile> call = Service.viewProfile(AppController.getManager().getId());
//            call.enqueue(new Callback<UpdateProfile>() {
//                @Override
//                public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
//                    CommonUtil.cancelProgress();
//                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
//                    UpdateProfile userDataMain = response.body();
//                    if (userDataMain.getStatus()) {
//                        updateProfileResult = userDataMain.getData();
//                        setProfileData();
//                    } else {
//                        tv_error.setVisibility(View.VISIBLE);
//                        nestedView.setVisibility(View.GONE);
//                        if (userDataMain.getMessage().equals("110110")){
//                            logout();
//
//                        }else{
//                            CommonUtil.snackBar(userDataMain.getMessage(), ProfileActivity.this);
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<UpdateProfile> call, Throwable t) {
//                    CommonUtil.cancelProgress();
//                    tv_error.setVisibility(View.VISIBLE);
//                    nestedView.setVisibility(View.GONE);
//                    CommonUtil.snackBar(t.getMessage(), ProfileActivity.this);
//                }
//            });
//        } else {
//            tv_error.setVisibility(View.VISIBLE);
//            nestedView.setVisibility(View.GONE);
//            CommonUtil.snackBar(getString(R.string.internetmsg), ProfileActivity.this);
//        }
//    }
//
//    private void logout() {
//
//            AppController.getManager().clearPreference();
//            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//
//    }
//}

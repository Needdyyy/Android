package com.needyyy.app.Modules.adsAndPage.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.PageDataBase;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.mypage.model.masterindex.masterindex.BoostCategory;
import com.needyyy.app.mypage.model.masterindex.masterindex.Data;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.UploadAmazonS3;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.Kmasterhit;

public class PageCategoryFragment extends BaseFragment implements View.OnClickListener{

    private ImageView imgBanner;
    private CircleImageView imgPage;
    private AppCompatAutoCompleteTextView etCategory ;
    private TextView btnNext;
    private int mediaType,uploadFileCount=0;;
    private AppTextView btnSports,btnMusic,btnDailyNews,btnFashion;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "PostFragment";
    String banerImageUrl,profileImageurl,title,description,contact,address,website;
    AlertDialog alertDialog ;
    private ArrayList<Uri> imagelist = new ArrayList<>();
    AlertDialog.Builder builder ;
    private Uri bannerUri,profileUri;
    private ArrayList<String> boostarray ;
    private PageDataBase pageData ;


    public PageCategoryFragment() {
        // Required empty public constructor
    }


    public static PageCategoryFragment newInstance(ArrayList<Uri> imagelist, String title, String description,String contact,String website,String address) {
        PageCategoryFragment fragment = new PageCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.IMAGE,imagelist);

        args.putString(Constant.Title,title);
        args.putString(Constant.DESCRIPTION,description);
        args.putString("contact",contact);
        args.putString("website",website);
        args.putString("address",address);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_page_categories);
        if (getArguments() != null) {
//            banerImageUrl    = getArguments().getString(Constant.BANNER_IMAGE);
//            profileImageurl  = getArguments().getString(Constant.PAGE_IMAGE);
            imagelist        = (ArrayList<Uri>) getArguments().getSerializable(Constant.IMAGE);
            title            = getArguments().getString(Constant.Title);
            description      = getArguments().getString(Constant.DESCRIPTION);
            contact          =getArguments().getString("contact");
            website          =getArguments().getString("website");
            address          =getArguments().getString("address");
        }
    }

    @Override
    protected void initView(View mView) {
        btnSports     = mView.findViewById(R.id.btn_sports);
        btnMusic      = mView.findViewById(R.id.btn_music);
        btnDailyNews  = mView.findViewById(R.id.btn_daily_news);
        btnFashion    = mView.findViewById(R.id.btn_fashions);
        btnNext       = mView.findViewById(R.id.btn_next);
        etCategory   = mView.findViewById(R.id.et_category);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("Create Your Page", "");
        Data masterHitData = (BaseManager.getMasterHitData(Kmasterhit, Data.class));
        ArrayList<BoostCategory> reportPosts= (ArrayList<BoostCategory>) masterHitData.getBoosycategory();
        boostarray=new ArrayList<>();
        if(reportPosts.size()>0) {
            for (int i = 0; i < reportPosts.size(); i++) {

                boostarray.add(reportPosts.get(i).getText());

            }
        }
        btnSports.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        btnDailyNews.setOnClickListener(this);
        btnFashion.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, boostarray);
        etCategory.setThreshold(1); //will start working from first character
        etCategory.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sports:
                btnSports.setTextColor(getResources().getColor(R.color.white));
                btnMusic.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnDailyNews.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnFashion.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnSports.setBackground(getResources().getDrawable(R.drawable.bg_login_text));
                btnMusic.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnDailyNews.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnFashion.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                etCategory.setText(getContext().getResources().getString(R.string.sports));
                break;
            case R.id.btn_music:
                btnSports.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnMusic.setTextColor(getResources().getColor(R.color.white));
                btnDailyNews.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnFashion.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnSports.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnMusic.setBackground(getResources().getDrawable(R.drawable.bg_login_text));
                btnDailyNews.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnFashion.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                etCategory.setText(getContext().getResources().getString(R.string.music));
                break;
            case R.id.btn_daily_news :
                btnSports.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnMusic.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnDailyNews.setTextColor(getResources().getColor(R.color.white));
                btnFashion.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnSports.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnMusic.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnDailyNews.setBackground(getResources().getDrawable(R.drawable.bg_login_text));
                btnFashion.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                etCategory.setText(getContext().getResources().getString(R.string.daily_news));
                break;
            case R.id.btn_fashions :
                btnSports.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnMusic.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnDailyNews.setTextColor(getResources().getColor(R.color.colorText_light_gray));
                btnFashion.setTextColor(getResources().getColor(R.color.white));
                btnSports.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnMusic.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnDailyNews.setBackground(getResources().getDrawable(R.drawable.bg_login_unselect));
                btnFashion.setBackground(getResources().getDrawable(R.drawable.bg_login_text));
                etCategory.setText(getContext().getResources().getString(R.string.fashion));
                break;
            case R.id.btn_next :
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        if (!etCategory.getText().toString().isEmpty()){
            if (CommonUtil.isConnectingToInternet(getContext())){
                uploadData();
            }else{
                snackBar(getContext().getResources().getString(R.string.internetmsg));
            }
        }else{
            snackBar(getContext().getResources().getString(R.string.category_msg));
        }
    }

    private void uploadData() {
        if (imagelist != null && imagelist.size() > 0) {
            hideKeyboard(getContext());
            uploadFileCount = imagelist.size();
            if (uploadFileCount > 0) {
                showProgressDialog();
                for (int i = 0; i < imagelist.size(); i++) {
                    uploadPicToAmazon(i,imagelist.get(i));
                }
            } else {
                createPage();
            }
        } else {
            CommonUtil.showShortToast(getContext(),"error");
        }        for (int i = 0; i < imagelist.size(); i++) {

        }
    }

    private void createPage() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CreatePageModel> call = Service.createPage(profileImageurl,banerImageUrl,title,description,etCategory.getText().toString(),contact,website,address);
        call.enqueue(new Callback<CreatePageModel>() {
            @Override
            public void onResponse(Call<CreatePageModel> call, Response<CreatePageModel> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CreatePageModel createPageModel = response.body();
                if (createPageModel.getStatus()) {
                    pageData = createPageModel.getData();
                    showCustomDialog();
                } else {
                    if (createPageModel.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }else{
                        snackBar(createPageModel.getMessage());
                    }
                    snackBar(createPageModel.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CreatePageModel> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.promotepage, viewGroup, false);
        Button btnOk     = dialogView.findViewById(R.id.buttonOk);
        Button btnCalcel = dialogView.findViewById(R.id.buttoncancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getFragmentManager().popBackStack();
                ((HomeActivity)getActivity()).replaceFragment(CreatePageFinalFragment.newInstance(pageData,true), true);
                alertDialog.dismiss();
            }
        });
        btnCalcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(AdsManagerFragment.newInstance(), true);
                alertDialog.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * now upload image to s3 bucket and get its url
     * @param
     * @param
     * @param mediaType
     * @param newProfileImageUri
     */
    private void uploadPicToAmazon(int mediaType, Uri newProfileImageUri) {

        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(getActivity(), Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME,"PageMedia/"+ newProfileImageUri.getLastPathSegment(), new File(newProfileImageUri.getPath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String sucess) {
                if (mediaType==0){
                    banerImageUrl = Constant.AWS_URL +Constant.BUCKET_NAME +"/PageMedia/" +  newProfileImageUri.getLastPathSegment();
                }else if(mediaType ==1){
                    profileImageurl = Constant.AWS_URL +Constant.BUCKET_NAME +"/PageMedia/" +  newProfileImageUri.getLastPathSegment();
                }
                uploadFileCount--;
                Log.d("PROFILE_IMAGE", ""+profileImageurl);
                Log.d("PROFILE_IMAGE", ""+banerImageUrl);
                if (uploadFileCount<=0){
                    cancelProgressDialog();
                    createPage();
                }

            }

            @Override
            public void error(String errormsg) {

                Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", ""+errormsg);
            }
        });
    }
}

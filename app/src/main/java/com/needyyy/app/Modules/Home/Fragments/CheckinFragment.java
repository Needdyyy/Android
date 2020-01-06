package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.BasePojo;

import com.needyyy.app.Modules.Home.Activities.HomeActivity;

import com.needyyy.app.Modules.Home.modle.googlePlace.PlaceBase;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;

import com.needyyy.app.webutils.WebInterface;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class CheckinFragment extends BaseFragment implements View.OnClickListener {
    private PlaceBase placeBase;
    private CircleImageView imgProfile ;
    private TextView tvPlaceName ,tvPost;
    private EditText etDescription;
    private WebView webView;
    private ImageView imageview;

    public static CheckinFragment newInstance(PlaceBase placeBase) {
        CheckinFragment fragment = new CheckinFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.PLACEDATA,placeBase);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_checkin);
        if (getArguments() != null) {
            placeBase = (PlaceBase) getArguments().getSerializable(Constant.PLACEDATA);
        }
    }

    @Override
    protected void initView(View mView) {
        tvPlaceName    = mView.findViewById(R.id.tv_placename);
        imgProfile     = mView.findViewById(R.id.img_pageprofile);
        etDescription  = mView.findViewById(R.id.et_description);
        webView        = mView.findViewById(R.id.webView);
        imageview      = mView.findViewById(R.id.imageviewmap);
        tvPost         = mView.findViewById(R.id.tv_post_button);

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar(getContext().getResources().getString(R.string.checkin), "");
        UserDataResult userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        // imgProfile
        String latEiffelTower = String.valueOf(placeBase.getResult().getGeometry().getLocation().getLat());
        String lngEiffelTower = String.valueOf(placeBase.getResult().getGeometry().getLocation().getLng());
        String url ="https://maps.googleapis.com/maps/api/staticmap?";
        url+="&zoom=13";
        url+="&size=600x300";
        url+="&maptype=roadmap";
        url+="&markers=color:green%7Clabel:G%7C"+latEiffelTower+","+lngEiffelTower;
        url+="&key="+"AIzaSyAy9jwmpKuuNJdID26ChQADu0HofAWGZNc";
        Log.e("response","==="+url);
        webView.loadUrl(url);
        Picasso.get().
                load(url)
                .into(imageview);

        if (!TextUtils.isEmpty(userData.getProfilePicture())) {
            Glide.with(getActivity())
                    .load(userData.getProfilePicture())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.needyy);
        }
        String username = "<b>"+userData.getName()+"</b>"+" - At "+"<b>"+placeBase.getResult().getFullname()+"</b>";
        tvPlaceName.setText(Html.fromHtml(username));
//        tvPlaceName.setText(userData.getName()+"- At"+placeBase.getResult().getName());
        tvPost.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_post_button:
                if (CommonUtil.isConnectingToInternet(getActivity())){
                    uploadPost();
                }else{
                    snackBar(getContext().getString(R.string.add_post_errmsg));
                }

                break;
        }
    }
    private void uploadPost() {
        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<BasePojo> call = Service.CheckIn(etDescription.getText().toString(),"",placeBase.getResult().getFormattedAddress(),
                    String.valueOf(placeBase.getResult().getGeometry().getLocation().getLat()), String.valueOf(placeBase.getResult().getGeometry().getLocation().getLng()),"2");
            call.enqueue(new Callback<BasePojo>() {
                @Override
                public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    BasePojo basePojo = response.body();
                    if (basePojo.getStatus()) {
                        CommonUtil.showShortToast(getActivity(),basePojo.getMessage());
                        getFragmentManager().popBackStack();
                    } else {
                        if (basePojo.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();
                        }else{
                            snackBar(basePojo.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<BasePojo> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }


}

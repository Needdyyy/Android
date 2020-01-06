package com.needyyy.app.Modules.Profile.fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Profile.adapters.SeeAllPhotoAdapter;
import com.needyyy.app.Modules.Profile.models.UserPicture.Datum;
import com.needyyy.app.Modules.Profile.models.UserPicture.GetUserPictures;
import com.needyyy.app.R;
import com.needyyy.app.webutils.WebInterface;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllPhotoFragment extends BaseFragment  {
    SeeAllPhotoAdapter seeAllPhotoAdapter;
    String memberId="";
    RecyclerView recyclerView;
    private ImageView playicon;
    private Boolean checkfragment=false;
    private Boolean checkvideo=false;
    String farg="",pageid="";
    private ArrayList<Datum> getUserPicturesArrayList = new ArrayList<com.needyyy.app.Modules.Profile.models.UserPicture.Datum>() ;

    public SeeAllPhotoFragment() {
    }

    public static Fragment newInstance(String id) {
        SeeAllPhotoFragment seeAllPhotoFragment=new SeeAllPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        seeAllPhotoFragment.setArguments(bundle);
        return seeAllPhotoFragment;
    }
    public static Fragment newInstance(String s,String pageid) {
        SeeAllPhotoFragment seeAllPhotoFragment = new SeeAllPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Frag", s);
        bundle.putString("pageid",pageid);
        seeAllPhotoFragment.setArguments(bundle);
        return seeAllPhotoFragment;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if(getArguments().getString("Frag")!=null)
            {
                farg = getArguments().getString("Frag");
            }
            if(getArguments().getString("pageid")!=null)
            {
                pageid=getArguments().getString("pageid");
            }

            if(getArguments().getString("id")!=null) {
                memberId = getArguments().getString("id");
            }
            if (farg.equals("Chat")) {
                if(getActivity() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).manageToolbar("All Photos","1");
                }
                checkfragment = true;
            }
            if(farg.equals("video"))
            {
                if(getActivity() instanceof HomeActivity)
                {
                    ((HomeActivity) getActivity()).manageToolbar("All Videos","1");
                }
                checkvideo=true;
            }
        }



        if (checkfragment == true && checkvideo==false) {
            getUserImagedata();
        }
        else if(checkfragment==false && checkvideo==true)
        {
            getUserVideodata();
        }
        else if(checkfragment==false && checkvideo==false){
            getUserPicture();
        }
        super.onCreate(savedInstanceState,R.layout.fragment_see_all_photo);
    }

    private void getUserImagedata() {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetUserPictures> call = Service.getUserChatImage(pageid, 50, 1, "images");
        call.enqueue(new Callback<GetUserPictures>() {
            @Override
            public void onResponse(Call<GetUserPictures> call, Response<GetUserPictures> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetUserPictures getUserPictures = response.body();
                if (getUserPictures.getStatus()) {
                    if (getUserPicturesArrayList != null || getUserPicturesArrayList.size() == 0) {
                        getUserPicturesArrayList.clear();
                    }
                    for (int i = 0; i < getUserPictures.getData().size(); i++) {
                        getUserPicturesArrayList.add(getUserPictures.getData().get(i));
                    }

                } else {
                    if (getUserPictures.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(getUserPictures.getMessage());
                    }
                }

                setPicture();

            }

            @Override
            public void onFailure(Call<GetUserPictures> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    private void getUserVideodata() {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetUserPictures> call = Service.getUserChatImage(pageid, 50, 1, "videos");
        call.enqueue(new Callback<GetUserPictures>() {
            @Override
            public void onResponse(Call<GetUserPictures> call, Response<GetUserPictures> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetUserPictures getUserPictures = response.body();
                if (getUserPictures.getStatus()) {
                    if (getUserPicturesArrayList != null || getUserPicturesArrayList.size() == 0) {
                        getUserPicturesArrayList.clear();
                    }
                    for (int i = 0; i < getUserPictures.getData().size(); i++) {
                        getUserPicturesArrayList.add(getUserPictures.getData().get(i));
                    }

                } else {
                    if (getUserPictures.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(getUserPictures.getMessage());
                    }
                }

                setVideo();

            }

            @Override
            public void onFailure(Call<GetUserPictures> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }


    @Override
    protected void initView(View mView) {
        recyclerView = mView.findViewById(R.id.recycler_view);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        if(getUserPicturesArrayList.size()!=0) {
            setPicture();
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        public GridSpacingItemDecoration(int i, int i1, boolean b) {
        }
    }

    private void getUserPicture() {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetUserPictures> call = Service.getUserPictures(1,50,memberId);
        call.enqueue(new Callback<GetUserPictures>() {
            @Override
            public void onResponse(Call<GetUserPictures> call, Response<GetUserPictures> response) {
                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetUserPictures getUserPictures = response.body();
                if (getUserPictures.getStatus()) {
                    if (getUserPicturesArrayList!=null || getUserPicturesArrayList.size()==0){
                        getUserPicturesArrayList.clear();
                    }
                    for(int i=0;i<getUserPictures.getData().size();i++)
                    {
                        getUserPicturesArrayList.add(getUserPictures.getData().get(i));
                    }

                } else {
                    if (getUserPictures.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    } else {
                        snackBar(getUserPictures.getMessage());
                    }
                }

                setPicture();

            }
            @Override
            public void onFailure(Call<GetUserPictures> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public  void setPicture(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new com.needyyy.app.views.GridSpacingItemDecoration(3, 18, true));
        seeAllPhotoAdapter = new SeeAllPhotoAdapter(getContext(),getUserPicturesArrayList);
        recyclerView.setAdapter(seeAllPhotoAdapter);
    }

    public  void setVideo(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new com.needyyy.app.views.GridSpacingItemDecoration(3, 18, true));
        seeAllPhotoAdapter = new SeeAllPhotoAdapter(getContext(),getUserPicturesArrayList,"");
        recyclerView.setAdapter(seeAllPhotoAdapter);
    }

}

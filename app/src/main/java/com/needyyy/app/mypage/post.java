package com.needyyy.app.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.mypage.model.AddPagePost;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class post extends BaseFragment implements View.OnClickListener {

    TextView et_comment;
    Context context;
    RecyclerView my_page_recycler;
    private LinearLayoutManager Lm;
    private int mPage = 1;
    private boolean loading = false;
    private int pageSize;
    private String id;
    public UserDataResult userData;
    ArrayList<PostResponse> postResponseArrayList = new ArrayList<>();
    private MypagePostAdapter mypagePostAdapter;



    public static post newInstance()
    {
        post post=new post();
        return post;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentmypost, container, false);

        if (getArguments() != null) {
            id = getArguments().getString("id");
        }

        et_comment = view.findViewById(R.id.et_comments);
        context=getContext();

        my_page_recycler = view.findViewById(R.id.my_page_recycler);
        mypagePostAdapter = new MypagePostAdapter(getActivity(), postResponseArrayList,id);
        my_page_recycler.setHasFixedSize(true);
        Lm = new LinearLayoutManager(getContext());
        my_page_recycler.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(my_page_recycler, false);
        my_page_recycler.setLayoutManager(Lm);
        my_page_recycler.setAdapter(mypagePostAdapter);
        et_comment.setOnClickListener(this);
        getPost(false);
        return view;
    }

    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    private void getPost(boolean status) {
        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<PostDataBase> call = Service.getMyPageMyPost(mPage, 10, id);
        call.enqueue(new Callback<PostDataBase>() {
            @Override
            public void onResponse(Call<PostDataBase> call, Response<PostDataBase> response) {

                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                PostDataBase postDataBase = response.body();
                if (postDataBase.getStatus()) {
                    if (status) {
                        postResponseArrayList.addAll(postDataBase.getData());
                        loading = !(postDataBase.getData().size() == 0);
                    } else {
                        initialState();
                        if (postResponseArrayList != null && postResponseArrayList.size() != 0) {
                            postResponseArrayList.clear();
                        }
                        postResponseArrayList.addAll(postDataBase.getData());
                    }
                    mypagePostAdapter.notifyDataSetChanged();
                } else {
                    if (postDataBase.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();
                    } else {
                        snackBar(postDataBase.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<PostDataBase> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = postResponseArrayList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.et_comments:
                ((HomeActivity) getActivity()).replaceFragment(AddPagePost.newInstance(id), true);
                break;
        }
    }
}

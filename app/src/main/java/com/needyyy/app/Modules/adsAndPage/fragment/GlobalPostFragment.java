package com.needyyy.app.Modules.adsAndPage.fragment;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;

import com.needyyy.app.Modules.adsAndPage.adapter.MyGlobalPostAdapter;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.Data;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.GlobalPost;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.GlobalPost_;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.views.GridSpacingItemDecoration;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalPostFragment extends Fragment {
    RecyclerView recyclerView;
    Data data;
    private ImageView information;
    public static EditText tv_writesomething;
    List<GlobalPost_> list = new ArrayList<>();
    MyGlobalPostAdapter myGlobalPostAdapter;
    private GlobalPost globalPost;
    public GlobalPostFragment() {
    }

    public static Fragment newInstance() {
        GlobalPostFragment globalPostFragment = new GlobalPostFragment();
        return globalPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global_post, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_View);
        information=view.findViewById(R.id.information);
        tv_writesomething=view.findViewById(R.id.tv_writesomething);
        if(getActivity() instanceof HomeActivity)
        {
            ((HomeActivity) getActivity()).manageToolbar("Global Post","1");
        }
        getpostData();
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportPostView();
            }
        });


    }

    public void getUpdatedData(GlobalPost globalPost)
    {
        Data data=globalPost.getData();
        this.globalPost=globalPost;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.clear();
        list.addAll(data.getGlobalPost());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,18,true));
        myGlobalPostAdapter = new MyGlobalPostAdapter(getActivity(), list);
        recyclerView.setAdapter(myGlobalPostAdapter);
        myGlobalPostAdapter.notifyDataSetChanged();
    }
    public void getpostData() {
        if (CommonUtil.isConnectingToInternet(getActivity())) {

            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GlobalPost> call =Service.GLOBALPOST("");
            call.enqueue(new Callback<GlobalPost>() {
                @Override
                public void onResponse(Call<GlobalPost> call, Response<GlobalPost> response) {
                    GlobalPost globalPost = response.body();
                    if (globalPost.getStatus()) {
                        data=globalPost.getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        list.clear();
                        list.addAll(data.getGlobalPost());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,18,true));
                        myGlobalPostAdapter = new MyGlobalPostAdapter(getActivity(), list);
                        recyclerView.setAdapter(myGlobalPostAdapter);
                    } else {
                        if (globalPost.getMessage().equals("110110")) {
                            ((HomeActivity) getActivity()).logout();

                        } else {

                        }
                    }
                }
                @Override
                public void onFailure(Call<GlobalPost> call, Throwable t) {
                }
            });
        }
    }

    private void showReportPostView() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = li.inflate(R.layout.information_popup, null, false);
        final Dialog reportPost = new Dialog(getContext());
        reportPost.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportPost.setCanceledOnTouchOutside(false);
        reportPost.setContentView(v);
        reportPost.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reportPost.show();

        RecyclerView reportPostList;
        CardView submitCard, upperCard;
        ImageView cross;
        RelativeLayout bottomCardReportSubmitt;
        TextView wrongTV;

        wrongTV=v.findViewById(R.id.wrongTV);
        submitCard = v.findViewById(R.id.bottomCard);
        upperCard = v.findViewById(R.id.upperCard);
        cross = v.findViewById(R.id.crossimageIV);
        bottomCardReportSubmitt=v.findViewById(R.id.bottomCardReportSubmit);
        wrongTV.setText(Html.fromHtml(data.getDisclaimer()));
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPost.dismiss();
            }
        });
        bottomCardReportSubmitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPost.dismiss();
            }
        });

    }
}




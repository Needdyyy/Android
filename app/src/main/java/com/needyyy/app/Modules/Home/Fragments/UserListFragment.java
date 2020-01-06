package com.needyyy.app.Modules.Home.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.AddPost.models.AddPost.TaggedPerson;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.UserListAdapter;
import com.needyyy.app.Modules.Home.modle.UserListBase;
import com.needyyy.app.R;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView rvPeople;
    private LinearLayoutManager linearLayoutManager ;
    private UserListAdapter userListAdapter ;
    private String postId ;
    private List<TaggedPerson> userDataList ;
    private List<TaggedPerson> userDataList1 =new ArrayList<>() ;
    private String Status;

    public static UserListFragment newInstance(String id,String Status) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString(Constant.POST_ID,id);
        args.putString("status",Status);
        fragment.setArguments(args);
        return fragment;
    }
    public static UserListFragment newInstance(List<TaggedPerson> taggedPerson) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putSerializable("TaggedPerson", (Serializable) taggedPerson);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_userlist);
        if (getArguments() != null) {
            postId = getArguments().getString(Constant.POST_ID);
            Status=getArguments().getString("status");
            userDataList1= (List<TaggedPerson>) getArguments().getSerializable("TaggedPerson");
        }
    }

    @Override
    protected void initView(View mView) {
        rvPeople = mView.findViewById(R.id.rv_people);
        if (userDataList == null) {
            userDataList = new ArrayList<>();
        }
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {


        if(postId!=null) {
            if(Status.equals("1")) {
                ((HomeActivity) getActivity()).manageToolbar(getContext().getResources().getString(R.string.likes), "");
            }
            else if(Status.equals("2"))
            {
                ((HomeActivity) getActivity()).manageToolbar("Interested", "");
            }
            else if(Status.equals("3"))
            {
                ((HomeActivity) getActivity()).manageToolbar("Boring", "");
            }
            LikedUserList(Status);
            userListAdapter = new UserListAdapter(1, getActivity(), (ArrayList<TaggedPerson>) userDataList);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rvPeople.setLayoutManager(linearLayoutManager);
            rvPeople.setAdapter(userListAdapter);
        }

        if(userDataList1!=null)
        {
            ((HomeActivity)getActivity()).manageToolbar("Tagged people", "");
            userListAdapter = new UserListAdapter(1, getActivity(), (ArrayList<TaggedPerson>) userDataList1);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rvPeople.setLayoutManager(linearLayoutManager);
            rvPeople.setAdapter(userListAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comment:

                break;
        }
    }
    private void LikedUserList(String Status) {
        ((HomeActivity)getActivity()).showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<UserListBase> call = Service.likePostUserList(postId,1,30,Status);
        call.enqueue(new Callback<UserListBase>() {
            @Override
            public void onResponse(Call<UserListBase> call, Response<UserListBase> response) {
                ((HomeActivity)getActivity()).cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                UserListBase userlistData = response.body();
                if (userlistData.getStatus()) {
                    if (userlistData!=null && userDataList.size()!=0){
                        userDataList.clear();
                    }
                    userDataList.addAll(userlistData.getData());
                    userListAdapter.notifyDataSetChanged();
                } else {
                    if (userlistData.getMessage().equals("110110")){
                        ((HomeActivity)getActivity()).logout();

                    }else{
                        snackBar(userlistData.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListBase> call, Throwable t) {
                ((HomeActivity)getActivity()).cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });

    }

}

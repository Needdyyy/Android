package com.needyyy.app.Modules.Knocks.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Adapters.KnockRequestAdapter;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Knocks.models.ReceivedData;
import com.needyyy.app.R;
import com.needyyy.app.utils.OnFragmentVisibleListener;
import com.needyyy.app.utils.SearchListener;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlockedMember extends BaseFragment implements View.OnClickListener, SearchListener {

    private int mPage = 1;
    private boolean loading = false;
    private SwipeRefreshLayout pullToReferesh;
    private NestedScrollView nestedScrollView;
    private int pageSize;
    private RecyclerView rvKnockrequest;
    private ArrayList<ReceivedData> arrGetRequests = new ArrayList<>();
    private KnockRequestAdapter knockRequestAdapter ;
    AppTextView atvKnockText;
    TextView tvNoResult;

    public static BlockedMember newInstance() {
        BlockedMember blockedMember = new BlockedMember();
        Bundle args = new Bundle();
        blockedMember.setArguments(args);
        return blockedMember;
    }


    @Override
    protected void initView(View mView) {
        rvKnockrequest = mView.findViewById(R.id.rv_knock_request);
        tvNoResult     = mView.findViewById(R.id.tv_no_result);
        pullToReferesh = mView.findViewById(R.id.pullto_referesh);
        nestedScrollView = mView.findViewById(R.id.nested_scroll);

        ((HomeActivity)getActivity()).manageToolbar("Knock", "");
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_blocked_member);

    }
    @Override
    protected void bindControls(Bundle savedInstanceState) {
        tvNoResult.setVisibility(View.GONE);
        getRequestList("",false);
        setKnockFriendAdapter();

        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialState();
                getRequestList("",false);
                pullToReferesh.setRefreshing(false);
            }
        });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        getRequestList("",true);
                    }

                }
            }
        });

    }
    private void setKnockFriendAdapter() {
        knockRequestAdapter = new KnockRequestAdapter(getActivity(),arrGetRequests,3);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvKnockrequest.setLayoutManager(layoutManager);
        rvKnockrequest.setAdapter(knockRequestAdapter);

//        rvKnockrequest.setHasFixedSize(true);
//        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemRecyclerDecoration(getContext(), R.drawable.canvas_recycler_divider);
//        rvKnockrequest.addItemDecoration(dividerItemDecoration);
    }
    public void getRequestList(String text,Boolean status) {
//        showProgressDialog();
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.GetBlockedmember("20", String.valueOf(mPage),text);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
//                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {

                    if(status)
                    {
                        arrGetRequests.addAll(getReceivedRequest.getData());
                        loading = !(getReceivedRequest.getData().size() == 0);
                    }
                    else {
                        initialState();
                        if (arrGetRequests != null && arrGetRequests.size() != 0) {
                            arrGetRequests.clear();
                        }
                        arrGetRequests.addAll(getReceivedRequest.getData());
                    }

//                    arrGetRequests.clear();
//                    arrGetRequests.addAll(getReceivedRequest.getData());
                    knockRequestAdapter.notifyDataSetChanged();
//                        snackBar(getReceivedRequest.getMessage());
                } else {
                    tvNoResult.setVisibility(View.VISIBLE);
                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) getActivity()).logout();

                    } else {
                        snackBar(getReceivedRequest.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {
                cancelProgressDialog();
                snackBar(t.getMessage());
            }
        });
    }
    @Override
    public void onClick(View v) {

    }

    boolean isAttached;

    OnFragmentVisibleListener mapFragmentVisibilityListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            isAttached = true;
            mapFragmentVisibilityListener = (OnFragmentVisibleListener) activity;
            //flag for whether this fragment is attached to pager
            Log.d("response","onAttach");
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(activity.toString() + " must implement interface onAttach");
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        Log.d("response","setUserVisibleHint:"+isVisibleToUser);
        if(isVisibleToUser && isAttached){ //if listener is called before fragment is attached will throw NPE
            Log.d("response","setUserVisibleHint");
            mapFragmentVisibilityListener.fragmentVisible(true,BlockedMember.this);
        }
    }

    @Override
    public void onClickSearch(String text) {

        try{

           // Toast.makeText(getActivity(), "searchText:"+text, Toast.LENGTH_SHORT).show();
            initialState();
            getRequestList(text,false);

        }catch (Exception e){e.printStackTrace();}


    }
    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = arrGetRequests.size();
    }
}

package com.needyyy.app.notifications;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.notifications.notifications.Datum;
import com.needyyy.app.notifications.notifications.GetNotification;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends BaseFragment {
    private int mPage = 1;
    private boolean loading = false;
    private int pageSize;
    private RecyclerView recycleviwnotification;
    private SwipeRefreshLayout pullToReferesh;
    private NestedScrollView nestedScrollView;
    private ArrayList<Datum> arrayList=new ArrayList<>();

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof HomeActivity)
        {
            ((HomeActivity) getActivity()).manageToolbar("Notification","2");
        }
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    protected void initView(View mView) {

    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {

    }

    private void nitapi(Boolean status) {
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<GetNotification> call = Service.GETNOTIFICATION(mPage,10);
            call.enqueue(new Callback<GetNotification>() {
                @Override
                public void onResponse(Call<GetNotification> call, Response<GetNotification> response) {

                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    GetNotification getNotification = response.body();
                    if (getNotification.getStatus()) {
                        if (getNotification.getStatus()){
                            if (status) {
                                arrayList.addAll(getNotification.getData());
                                loading = !(getNotification.getData().size() == 0);
                            } else {
                                initialState();
                                if (arrayList != null && arrayList.size() != 0) {
                                    arrayList.clear();
                                }
                                arrayList.addAll(getNotification.getData());
                            }
                        }
                    } else {
                        if (getNotification.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();

                        }else{
                            Toast.makeText(getContext(),getNotification.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    NotificationAdapter notificationAdapter=new NotificationAdapter(getActivity(),arrayList,getContext());
                    recycleviwnotification.setHasFixedSize(false);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                    recycleviwnotification.setLayoutManager(linearLayoutManager);
                    recycleviwnotification.setAdapter(notificationAdapter);
                }

                @Override
                public void onFailure(Call<GetNotification> call, Throwable t) {

                }

            });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nitapi(false);
        pullToReferesh = view.findViewById(R.id.pullto_referesh);
        nestedScrollView = view.findViewById(R.id.nested_scroll);
        recycleviwnotification=view.findViewById(R.id.recycleviwnotification);

        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialState();
                nitapi(false);
                pullToReferesh.setRefreshing(false);
            }
        });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                       nitapi(true);
                    }

                }
            }
        });
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = arrayList.size();
    }

}

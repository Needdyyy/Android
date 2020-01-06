package com.needyyy.app.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.AppController;
import com.needyyy.app.Chat.chatDialogActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Home.Fragments.ViewFullImageFragment;
import com.needyyy.app.Modules.Home.Fragments.WalletFragment;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;
import com.needyyy.app.mypage.model.Activities.Getpostdata;
import com.needyyy.app.mypage.mypage_details;
import com.needyyy.app.notifications.notifications.Datum;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.Myviewholder> {
    private ArrayList<Datum> arrayList;
    private Activity activity;
    public PostResponse postresponse;
    private Context context;
    public NotificationAdapter(Activity activity, ArrayList<Datum> arrayList,Context context) {
        this.activity = activity;
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.fragment_notification_adapter, viewGroup, false);
        Myviewholder viewHolder = new Myviewholder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder myviewholder, int i) {
            Glide.with(context)
                    .load(arrayList.get(i).getImage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.needyy))
                    .into(myviewholder.img_pageprofile);
        myviewholder.desc.setText(Html.fromHtml(arrayList.get(i).getDescription()));
        myviewholder.time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(arrayList.get(i).getCreated())*1000));
        myviewholder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  HomeActivity activity = (HomeActivity) context;
                if(arrayList.get(i).getType().equals("22")||
                        arrayList.get(i).getType().equals("24")||
                        arrayList.get(i).getType().equals("21")||
                        arrayList.get(i).getType().equals("24")||
                        arrayList.get(i).getType().equals("23")||
                        arrayList.get(i).getType().equals("15"))
                {
                    if(activity instanceof HomeActivity)
                    {

                        if (CommonUtil.isConnectingToInternet(activity)){

                            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
                            Call<Getpostdata> call = Service.getPost(arrayList.get(i).getTaskId());
                            call.enqueue(new Callback<Getpostdata>() {
                                @Override
                                public void onResponse(Call<Getpostdata> call, Response<Getpostdata> response) {
                                    if(response.body().getStatus()==true) {
                                        Getpostdata getpostdata = response.body();
                                        postresponse = null;
                                        postresponse = getpostdata.getData();
                                        if (postresponse != null) {
                                            ((HomeActivity) activity).replaceFragment(ViewFullImageFragment.newInstance(postresponse), true);
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Getpostdata> call, Throwable t) {

                                }
                            });
                        }else{

                        }
                    }
                }
                else if(arrayList.get(i).getType().equals("40"))
                {
                    ((HomeActivity) activity).replaceFragment(WalletFragment.newInstance(), true);
                }

                else if(arrayList.get(i).getType().equals("1")||arrayList.get(i).getType().equals("3"))
                {
                    ((HomeActivity) activity).replaceFragment(HomeFragment.newInstance(),true);
                }
                else if(arrayList.get(i).getType().equals("37"))
                {
                    ((HomeActivity) activity).replaceFragment(ViewProfileFragment.newInstance(arrayList.get(i).getTaskId(),null), true);
                }

                else if(arrayList.get(i).getType().equals("2"))
                {
                    ((HomeActivity) activity).logout();
                }
                else if(arrayList.get(i).getType().equals("5")||arrayList.get(i).getType().equals("20"))
                {
                    ((HomeActivity) activity).replaceFragment(new com.needyyy.app.notifications.NotificationFragment(), true);
                }
                else if(arrayList.get(i).getType().equals("25")||arrayList.get(i).getType().equals("26")||arrayList.get(i).getType().equals("27")||arrayList.get(i).getType().equals("28")||arrayList.get(i).getType().equals("29"))
                {
                    ((HomeActivity) activity).replaceFragment(mypage_details.newInstance(arrayList.get(i).getTaskId()),true);
                    //  getpagedata(taskid);
                }
                else if(arrayList.get(i).getType().equals("50")||arrayList.get(i).getType().equals("51"))
                {
                    ((HomeActivity) activity).replaceFragment(ViewProfileFragment.newInstance(arrayList.get(i).getTaskId(),null), true);
                }
                else if(arrayList.get(i).getType().equals("27")||arrayList.get(i).getType().equals("28")||arrayList.get(i).getType().equals("29"))
                {
                    ((HomeActivity) activity).replaceFragment(mypage_details.newInstance(arrayList.get(i).getTaskId()), true);
                }
                else if(arrayList.get(i).getType().equals("35"))
                {
                    AppController.getManager().setInterest(Constant.DATING);
                    Intent intent1 = new Intent(context, chatDialogActivity.class);
                    //CommonUtil.showLongToast(this, "under development");
                    //  Intent intent=new Intent(HomeActivity.this, com.needyyy.app.Chat.groupchatwebrtc.activities.LoginActivity.class);
                    intent1.putExtra("activity", "isha2");
                    intent1.putExtra("password", "12345678");
                    intent1.putExtra("selectedTab",1);
                    context.startActivity(intent1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class Myviewholder extends RecyclerView.ViewHolder {
        private CircleImageView img_pageprofile;
        private RelativeLayout layout1;
        private TextView desc;
        private TextView time;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            img_pageprofile=itemView.findViewById(R.id.img_pageprofile);
            desc=itemView.findViewById(R.id.desc);
            layout1=itemView.findViewById(R.id.layout1);
            time=itemView.findViewById(R.id.time);
        }
    }
}

package com.needyyy.app.Modules.Home.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Knocks.models.AcceptRejectRequest;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Knocks.models.ReceivedData;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnockRequestAdapter extends RecyclerView.Adapter<KnockRequestAdapter.SingleFeedsViewHolder> {
    Activity activity;

    int type;
    RelativeLayout rlContentTypes;
    ArrayList<ReceivedData> data = new ArrayList<>();
    private KnockRequestAdapter knockRequestAdapter;

    public KnockRequestAdapter(Activity activity, ArrayList<ReceivedData> data, int type) {
        this.type = type;
        this.activity=activity;
        this.data = data;
    }


    @NonNull
    @Override
    public SingleFeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.knockfriend_layout, parent, false);
        return new SingleFeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFeedsViewHolder viewHolder, final int position) {
        ReceivedData receivedData = data.get(position);
        viewHolder.tvProfileName.setText(receivedData.getName());
        if (!TextUtils.isEmpty(receivedData.getProfilePicture())) {
            Glide.with(activity)
                    .load(receivedData.getProfilePicture())
                    .into(viewHolder.civProfile);
            viewHolder.civProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( activity instanceof HomeActivity) {
                       ((HomeActivity) activity).replaceFragment(ViewProfileFragment.newInstance(receivedData.getId(),receivedData.getIsprivate()),true);
                    }
                }
            });
        } else {
            viewHolder.civProfile.setImageResource(R.drawable.needyy);
            viewHolder.civProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( activity instanceof HomeActivity) {
                        ((HomeActivity) activity).replaceFragment(ViewProfileFragment.newInstance(receivedData.getId(),receivedData.getIsprivate()),true);
                    }
                }
            });
        }

//        viewHolder.accept.setVisibility(View.VISIBLE);
//        viewHolder.tvLastseen.setVisibility(View.VISIBLE);
//        viewHolder.tvRequestStatus.setVisibility(View.GONE);
        if (type == 2) {
            viewHolder.accept.setVisibility(View.VISIBLE);
            viewHolder.decline.setVisibility(View.VISIBLE);
            viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
//                    data.get(i).getCategoryId();
                    }
                    getRequestStatus(data.get(position).getId(), "2", type, position);
                    viewHolder.accept.setVisibility(View.GONE);
                    viewHolder.decline.setVisibility(View.GONE);
                    viewHolder.tvRequestStatus.setVisibility(View.VISIBLE);
                    viewHolder.tvRequestStatus.setText("Request Rejected");
                    //change api method name
//                ((HomeActivity)context).replaceFragment(ContentsSubTypeFragment.newInstance(data.get(position).getName(),data.get(position).getId()));

                }
            });

            viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
//                    data.get(i).getCategoryId();
                    }

                    getRequestStatus(data.get(position).getId(), "1", type, position);
                    viewHolder.accept.setVisibility(View.GONE);
                    viewHolder.decline.setVisibility(View.GONE);
                    viewHolder.tvRequestStatus.setVisibility(View.VISIBLE);
                    viewHolder.tvRequestStatus.setText("Request Accepted");

                    //change api method name
//                ((HomeActivity)context).replaceFragment(ContentsSubTypeFragment.newInstance(data.get(position).getName(),data.get(position).getId()));

                }
            });
        } else if (type == 1) {
            if (data.get(position).getIsFriend().equals("1")) {
                viewHolder.accept.setVisibility(View.VISIBLE);
                viewHolder.accept.setText("Remove");
                viewHolder.decline.setVisibility(View.GONE);
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendKnockRequest(data.get(position).getId(), 2, type, position);
                    }
                });
            } else if (data.get(position).getIsFriend().equals("2")) {
                viewHolder.accept.setVisibility(View.VISIBLE);
                viewHolder.decline.setVisibility(View.VISIBLE);
                viewHolder.accept.setText("Add Friend");
                viewHolder.decline.setText("Cancel");
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendKnockRequest(data.get(position).getId(), 1, type, position);
                    }
                });
            } else if (data.get(position).getIsFriend().equals("3")) {
                viewHolder.accept.setVisibility(View.GONE);
                viewHolder.decline.setVisibility(View.VISIBLE);
                viewHolder.decline.setText("Cancel");
                viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendKnockRequest(data.get(position).getId(), 2, type, position);
                    }
                });
            } else if (data.get(position).getIsFriend().equals("4")) {
                viewHolder.accept.setVisibility(View.VISIBLE);
                viewHolder.decline.setVisibility(View.VISIBLE);
                viewHolder.decline.setText("Cancel");
                viewHolder.accept.setText("Accept");
                viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequestStatus(data.get(position).getId(), "2", type, position);
                    }
                });
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequestStatus(data.get(position).getId(), "1", type, position);
                    }
                });
            }
        } else if (type == 5) {
            viewHolder.accept.setVisibility(View.VISIBLE);
            viewHolder.decline.setVisibility(View.VISIBLE);
            viewHolder.decline.setText("Remove");
            viewHolder.accept.setText("Block");
            viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlockUnblockfrd(data.get(position).getId(), "1", type);
                }
            });
            viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendKnockRequest(data.get(position).getId(), 2, type, position);
                }
            });
        } else if (type == 3) {
            viewHolder.accept.setVisibility(View.VISIBLE);
            viewHolder.decline.setVisibility(View.GONE);
            viewHolder.accept.setText("Unblock");
            viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlockUnblockfrd(data.get(position).getId(), "2", type);
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else if (type == 4) {
            viewHolder.accept.setVisibility(View.GONE);
            viewHolder.decline.setVisibility(View.VISIBLE);
            viewHolder.decline.setText("Cancel");
            viewHolder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendKnockRequest(data.get(position).getId(), 2, type, position);
                }
            });
        } else {
            viewHolder.accept.setVisibility(View.GONE);
            viewHolder.decline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
//        return 10;
    }


    public class SingleFeedsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civProfile;
        TextView tvProfileName, tvRequestStatus;
        Button decline, accept;

        public SingleFeedsViewHolder(View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            decline = itemView.findViewById(R.id.decline_button);
            accept = itemView.findViewById(R.id.accept_button);
            tvRequestStatus = itemView.findViewById(R.id.tv_request_status);
        }
    }

    public void getRequestStatus(String id, String status, int type, int position) {
        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<AcceptRejectRequest> call = Service.acceptRejectRequest(id, status);
        call.enqueue(new Callback<AcceptRejectRequest>() {
            @Override
            public void onResponse(Call<AcceptRejectRequest> call, Response<AcceptRejectRequest> response) {
//                cancelProgressDialog();
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                AcceptRejectRequest acceptRejectRequest = response.body();
                if (acceptRejectRequest.getStatus()) {
                    Toast.makeText(activity, acceptRejectRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    //((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
//                    arrGetRequests.addAll(acceptRejectRequest.getData());
                    if (type == 1) {
                        getRequestList(position);
                    }
                } else {
                    if (acceptRejectRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        Toast.makeText(activity, acceptRejectRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptRejectRequest> call, Throwable t) {
                //  cancelProgressDialog();
            }
        });

    }

    public void sendKnockRequest(String id, int statuss, int type, int position) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.sendKnockRequest(id, statuss);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    if (type == 1) {
                        getRequestList(position);
                    }
                    if (type == 5) {
                        getRequestListt();
                    }
                    if (type == 4) {
                      data.remove(position);
                        notifyDataSetChanged();
                    }
                    //((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
                } else {
                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {


            }
        });

    }

    public void BlockUnblockfrd(String id, String statuss, int type) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.BlockUnblock(id, statuss);
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    ((HomeActivity)context).replaceFragment(HomeFragment.newInstance(), true);
                     if(type==3)
                     {

                     }
                     else {
                         getRequestListt();
                     }

                } else {
                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        Toast.makeText(activity, getReceivedRequest.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {


            }
        });

    }

    public void getRequestList(int position) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.GetAllMember("20", "1","");
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    data.clear();
                    data.addAll(getReceivedRequest.getData());
                    notifyDataSetChanged();
                  /*  knockRequestAdapter = new KnockRequestAdapter(context,arrGetRequests,1);
                    knockRequestAdapter.notifyDataSetChanged();*/
                    // Allmember allmembe=new Allmember();
                    // allmembe.data(arrGetRequests,context);
//                        snackBar(getReceivedRequest.getMessage());
                } else {
                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        data.clear();
                        data.addAll(getReceivedRequest.getData());
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {

            }
        });
    }

    public void getRequestListt() {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.GetFriends("20", "1","");
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    data.clear();
                    data.addAll(getReceivedRequest.getData());
                    notifyDataSetChanged();
//                        snackBar(getReceivedRequest.getMessage());
                } else {

                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {

                        data.clear();
                        data.addAll(getReceivedRequest.getData());
                        notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {


            }
        });
    }

    public void getList() {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<GetReceivedRequest> call = Service.GetSentKnocks("20", "1","");
        call.enqueue(new Callback<GetReceivedRequest>() {
            @Override
            public void onResponse(Call<GetReceivedRequest> call, Response<GetReceivedRequest> response) {

                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                GetReceivedRequest getReceivedRequest = response.body();
                if (getReceivedRequest.getStatus()) {
                    data.clear();
                    data.addAll(getReceivedRequest.getData());
                    knockRequestAdapter.notifyDataSetChanged();
//                        snackBar(getReceivedRequest.getMessage());
                } else {

                    if (getReceivedRequest.getMessage().equals("110110")) {
                        ((HomeActivity) activity).logout();

                    } else {
                        data.clear();
                        data.addAll(getReceivedRequest.getData());
                        knockRequestAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<GetReceivedRequest> call, Throwable t) {

            }
        });
    }
}
package com.needyyy.app.mypage;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Fragments.HomeFragment;
import com.needyyy.app.Modules.Knocks.fragment.SendKnocksRequestFragment;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.Modules.Profile.adapters.FriendListAdapters;
import com.needyyy.app.Modules.Profile.fragments.ViewProfileFragment;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.CommonUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

public class mypage_adapter extends RecyclerView.Adapter<mypage_adapter.SignleFriendsViewHolderr> {
    private People people;
    private String id;
    ArrayList<String> arrayList = new ArrayList<>();
    String list_of_friendname = "";

    Context context;
    RelativeLayout rlContentTypes;
    HomeFragment homeFragment;
    ArrayList<People> data = new ArrayList<>();

    public mypage_adapter(Context context, ArrayList<People> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public SignleFriendsViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mypage_friendlist, parent, false);
        return new SignleFriendsViewHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignleFriendsViewHolderr viewHolder, final int position) {
        people = data.get(position);
        id = data.get(position).getId();
        viewHolder.tvProfileName.setText(people.getName());
        if (people.getIsFriend().equals("1") || people.getIsFriend().equals("4")) {
            if (!TextUtils.isEmpty(people.getProfilePicture())) {
                Glide.with(context)
                        .load(people.getProfilePicture())
                        .into(viewHolder.civProfile);
            } else {
                viewHolder.civProfile.setImageResource(R.drawable.needyy);
            }
        } else {
            CommonUtil.ConvertURLToBitmap(people.getProfilePicture(), context, viewHolder.civProfile);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                               if (isChecked == true) {
                                                                   arrayList.add(data.get(position).getId());
                                                               } else if (isChecked == false) {
                                                                   String friendname = data.get(position).getId();
                                                                   Iterator iterator = arrayList.iterator();
                                                                   while (iterator.hasNext()) {
                                                                       if (iterator.next().equals(friendname)) {
                                                                           arrayList.remove(friendname);
                                                                           break;
                                                                       }
                                                                   }

                                                               }
                                                               list_of_friendname = null;
                                                               if (arrayList.size() > 1) {
                                                                   list_of_friendname = "," + arrayList.get(0).toString() + ",";

                                                                   for (int i = 1; i < arrayList.size(); i++) {
                                                                       list_of_friendname = list_of_friendname + arrayList.get(i).toString() + ",";
                                                                   }

                                                                   Toast.makeText(context, list_of_friendname, Toast.LENGTH_SHORT).show();
                                                               } else if (arrayList.size() == 1) {
                                                                   list_of_friendname = arrayList.get(0).toString();
                                                                   Toast.makeText(context, list_of_friendname, Toast.LENGTH_SHORT).show();
                                                               } else if (arrayList.size() == 0) {
                                                                   Toast.makeText(context, "no friend selected", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       }
        );
//        viewHolder.accept.setVisibility(View.VISIBLE);
//        viewHolder.decline.setVisibility(View.VISIBLE);
//        viewHolder.tvRequestStatus.setVisibility(View.GONE);
    }

    public String getselectrdId() {
        return list_of_friendname;
    }


    @Override
    public int getItemCount() {
        return data.size();
//        return 10;
    }

    public class SignleFriendsViewHolderr extends RecyclerView.ViewHolder {
        CircleImageView civProfile;
        TextView tvProfileName;
        CheckBox checkBox;

        public SignleFriendsViewHolderr(View itemView) {
            super(itemView);

            civProfile = itemView.findViewById(R.id.civ_profile_pic);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            checkBox = itemView.findViewById(R.id.checkbox);


        }
    }

}

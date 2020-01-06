package com.needyyy.app.Chat.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.needyyy.app.Chat.GroupActivity;
import com.needyyy.app.Chat.GroupChatActivity;
import com.needyyy.app.Chat.model.Group;
import com.needyyy.app.Chat.model.ListFriend;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupsFragment extends Fragment implements View.OnClickListener{


    TextView tvCreate, tvNoData;
    UserDataResult userData ;
    Context context;
    private ArrayList<Group> listGroup;
    private ListGroupsAdapter adapter;
    private RecyclerView rvGroups;
    boolean isFirstItem = true;
    static String selected_group_name = "";
    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_EDIT = 2;
    public static final int CONTEXT_MENU_LEAVE = 3;
    public static final int REQUEST_EDIT_GROUP = 0;
    public static final String CONTEXT_MENU_KEY_INTENT_DATA_POS = "pos";

    public MyGroupsFragment() {
        // Required empty public constructor
    }

    public static MyGroupsFragment newInstance(){
        MyGroupsFragment fragment = new MyGroupsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_grops, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        listGroup = new ArrayList<>();

        rvGroups = view.findViewById(R.id.rvGroups);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvGroups.setLayoutManager(mLayoutManager);
        tvCreate = view.findViewById(R.id.tvCreate);
        tvNoData = view.findViewById(R.id.tvNoData);
        tvCreate.setOnClickListener(this);

        adapter = new ListGroupsAdapter(getContext(), listGroup);
        rvGroups.setAdapter(adapter);

        getListGroup();
    }

    @Override
    public void onResume() {
        ((GroupActivity)getActivity()).setTitle("Groups");
        listGroup.clear();
        ListGroupsAdapter.listFriend = null;
        getListGroup();
        super.onResume();
    }

/*
    private void getListGroup(){
        ((GroupActivity)getActivity()).showProgress();
        FirebaseDatabase.getInstance().getReference().child("needyyy/users/"+ userData.getId()+"/group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((GroupActivity)getActivity()).hideProgress();
                listGroup.clear();
                if(dataSnapshot.getValue() != null) {

                    tvNoData.setVisibility(View.GONE);
                    rvGroups.setVisibility(View.VISIBLE);

                    HashMap mapListGroup = (HashMap) dataSnapshot.getValue();
                    Iterator iterator = mapListGroup.keySet().iterator();
                    while (iterator.hasNext()){
                        String idGroup = (String) mapListGroup.get(iterator.next().toString());
                        Group newGroup = new Group();
                        newGroup.id = idGroup;
                        listGroup.add(newGroup);
                    }
                    getGroupInfo(0);
                }else{
                    rvGroups.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
*/

    private void getListGroup(){
        ((GroupActivity)getActivity()).showProgress();
        FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info/"+ userData.getId()+"/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((GroupActivity)getActivity()).hideProgress();
                listGroup.clear();
                if(dataSnapshot.getValue() != null) {

                    tvNoData.setVisibility(View.GONE);
                    rvGroups.setVisibility(View.VISIBLE);

                    HashMap mapListGroup = (HashMap) dataSnapshot.getValue();
                    Iterator iterator = mapListGroup.keySet().iterator();
                    while (iterator.hasNext()){
                        String idGroup = (String) mapListGroup.get(iterator.next().toString());
                        Group newGroup = new Group();
                        newGroup.id = idGroup;
                        listGroup.add(newGroup);
                    }
                    getGroupInfo(0);
                }else{
                    rvGroups.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void getGroupInfo(final int indexGroup){
        if(indexGroup == listGroup.size()){
            adapter.notifyDataSetChanged();
            //mSwipeRefreshLayout.setRefreshing(false);
        }else {
            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/"+listGroup.get(indexGroup).id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        HashMap mapGroup = (HashMap) dataSnapshot.getValue();
                        ArrayList<String> member = (ArrayList<String>) mapGroup.get("member");
                        HashMap mapGroupInfo = (HashMap) mapGroup.get("groupInfo");
                        for(String idMember: member){
                            listGroup.get(indexGroup).member.add(idMember);
                        }
                        listGroup.get(indexGroup).groupInfo.put("name", (String) mapGroupInfo.get("name"));
                        listGroup.get(indexGroup).groupInfo.put("admin", (String) mapGroupInfo.get("admin"));
                        listGroup.get(indexGroup).groupInfo.put("icon", (String) mapGroupInfo.get("icon"));
                    }
                    getGroupInfo(indexGroup +1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE:
                int posGroup = item.getIntent().getIntExtra(CONTEXT_MENU_KEY_INTENT_DATA_POS, -1);
                if(((String)listGroup.get(posGroup).groupInfo.get("admin")).equals(userData.getId())) {
                    Group group = listGroup.get(posGroup);
                    listGroup.remove(posGroup);
                    if(group != null){
                        deleteGroup(group, 0);
                    }
                }else{
                    Toast.makeText(getActivity(), "You are not admin", Toast.LENGTH_LONG).show();
                }
                break;
            case CONTEXT_MENU_EDIT:

                int posGroup1 = item.getIntent().getIntExtra(CONTEXT_MENU_KEY_INTENT_DATA_POS, -1);
                if(((String)listGroup.get(posGroup1).groupInfo.get("admin")).equals(userData.getId())) {

                    Group group = listGroup.get(posGroup1);

                    Bundle bundle = new Bundle();
                    bundle.putString("groupId", listGroup.get(posGroup1).id);
                    bundle.putString("groupName", selected_group_name);
                    bundle.putSerializable("group",group);
                    ((GroupActivity)getActivity()).replaceFragment(CreateGroupFragment.newInstance(bundle));
                }else{
                    Toast.makeText(getActivity(), "You are not admin", Toast.LENGTH_LONG).show();
                }

                break;

            case CONTEXT_MENU_LEAVE:
                int position = item.getIntent().getIntExtra(CONTEXT_MENU_KEY_INTENT_DATA_POS, -1);
                if(((String)listGroup.get(position).groupInfo.get("admin")).equals(userData.getId())) {
                    Toast.makeText(getActivity(), "Admin cannot leave the group", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        //((GroupActivity)getActivity()).hideProgress();
                        Group groupLeaving = listGroup.get(position);
                        leaveGroup(groupLeaving);
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void deleteGroup(final Group group, final int index){
        ((GroupActivity)getActivity()).showProgress();

        if(index == group.member.size()){

            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/"+group.id).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ((GroupActivity)getActivity()).hideProgress();
                            listGroup.remove(group);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Group Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ((GroupActivity)getActivity()).hideProgress();
                            Toast.makeText(context, "Cannot delete group right now, please try again", Toast.LENGTH_SHORT).show();
                        }
                    })
            ;
        }else{
            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info/"+group.member.get(index)+"/"+group.id).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            deleteGroup(group, index + 1);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ((GroupActivity)getActivity()).hideProgress();
                            Toast.makeText(context, "Cannot connect server", Toast.LENGTH_SHORT).show();

                        }
                    })
            ;
        }

    }


    public void leaveGroup(final Group group){
        FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/"+group.id+"/member")
                .orderByValue().equalTo(userData.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() == null) {
                            ((GroupActivity)getActivity()).hideProgress();
                            Toast.makeText(context, "Error occurred during leaving group", Toast.LENGTH_SHORT).show();
                        } else {
                            String memberIndex = "";
                            ArrayList<String> result = ((ArrayList<String>)dataSnapshot.getValue());
                            for(int i = 0; i < result.size(); i++){
                                if(result.get(i) != null){
                                    memberIndex = String.valueOf(i);
                                }
                            }

                            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/member_info").child(userData.getId()).child(group.id).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("needyyy/group_management/group/"+group.id+"/member")
                                    .child(memberIndex).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ((GroupActivity)getActivity()).hideProgress();
                                            listGroup.remove(group);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(context, "Group Leaved Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            ((GroupActivity)getActivity()).hideProgress();
                                            Toast.makeText(context, "Error occurred during leaving group", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        ((GroupActivity)getActivity()).hideProgress();
                        Toast.makeText(context, "Error occurred during leaving group", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvCreate:
                Bundle bundle = new Bundle();
                ((GroupActivity)getActivity()).replaceFragment(CreateGroupFragment.newInstance(bundle));
                break;
        }
    }
}

class ListGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Group> listGroup;
    public static ListFriend listFriend = null;
    private Context context;

    public ListGroupsAdapter(Context context,ArrayList<Group> listGroup){
        this.context = context;
        this.listGroup = listGroup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_group_user_item, parent, false);
        return new ItemGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String groupName = listGroup.get(position).groupInfo.get("name");
        final String groupIcon = listGroup.get(position).groupInfo.get("icon");
        if(groupName != null && groupName.length() > 0) {
            ((ItemGroupViewHolder) holder).txtGroupName.setText(groupName);
            //((ItemGroupViewHolder) holder).iconGroup.setText((groupName.charAt(0) + "").toUpperCase());
        }


        if(groupIcon != null && groupName.length() > 0) {
            ((ItemGroupViewHolder) holder).txtGroupName.setText(groupName);
            Glide.with(context).load(groupIcon).into(((ItemGroupViewHolder) holder).ivIcon);
            //((ItemGroupViewHolder) holder).iconGroup.setText((groupName.charAt(0) + "").toUpperCase());
        }

        ((ItemGroupViewHolder) holder).btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(new Object[]{groupName, position});
                view.getParent().showContextMenuForChild(view);
                MyGroupsFragment.selected_group_name = listGroup.get(position).groupInfo.get("name");
            }
        });

        ((LinearLayout)((ItemGroupViewHolder) holder).txtGroupName.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra(Constant.INTENT_KEY_CHAT_FRIEND, groupName);
                ArrayList<CharSequence> idFriend = new ArrayList<>();
                GroupChatActivity.bitmapAvataFriend = new HashMap<>();
                for(String id : listGroup.get(position).member) {
                    idFriend.add(id);
                    /*String avata = listFriend.getAvataById(id);
                    if(!avata.equals(Constant.STR_DEFAULT_BASE64)) {
                        byte[] decodedString = Base64.decode(avata, Base64.DEFAULT);
                        GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    }else if(avata.equals(Constant.STR_DEFAULT_BASE64)) {
                        GroupChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_default));
                    }else {*/
                        GroupChatActivity.bitmapAvataFriend.put(id, null);
                    //}
                }
                intent.putCharSequenceArrayListExtra(Constant.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(Constant.INTENT_KEY_CHAT_ROOM_ID, listGroup.get(position).id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }
}

class ItemGroupViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public TextView iconGroup, txtGroupName, tv_chattext;
    ImageButton btnMore;
    CircleImageView ivIcon;

    public ItemGroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
        //iconGroup =  itemView.findViewById(R.id.icon_group);
        txtGroupName =  itemView.findViewById(R.id.tv_profile_name);
        tv_chattext =  itemView.findViewById(R.id.tv_chattext);
        btnMore =  itemView.findViewById(R.id.btnMoreAction);
        ivIcon =  itemView.findViewById(R.id.civ_profile_pic);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.setHeaderTitle((String) ((Object[])btnMore.getTag())[0]);
        Intent data = new Intent();
        data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        data.putExtra(MyGroupsFragment.CONTEXT_MENU_KEY_INTENT_DATA_POS, (Integer) ((Object[])btnMore.getTag())[1]);
        menu.add(Menu.NONE, MyGroupsFragment.CONTEXT_MENU_EDIT, Menu.NONE, "Edit group").setIntent(data);
        menu.add(Menu.NONE, MyGroupsFragment.CONTEXT_MENU_DELETE, Menu.NONE, "Delete group").setIntent(data);
        menu.add(Menu.NONE, MyGroupsFragment.CONTEXT_MENU_LEAVE, Menu.NONE, "Leave group").setIntent(data);
    }
}

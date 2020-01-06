package com.needyyy.app.Chat.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needyyy.app.Chat.model.Group;
import com.needyyy.app.Chat.model.ListFriend;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.Progress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.needyyy.app.constants.Constants.kCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditGroupFragment extends Fragment implements View.OnClickListener {


    Activity activity;
    Progress mProgress;

    EditText etGroupName;
    TextView tvDone;
    ArrayList<People> allUserList;
    ArrayList<People> selectedUserList;

    UserDataResult userData ;
    RecyclerView rvParticipant, rvAllUser;

    private ListFriend listFriend;
    private Set<String> listIDChoose;
    private Set<String> listIDRemove;
    private boolean isEditGroup;
    String group_name = "";
    Group groupEdit;
    private ListPeopleAdapter adapter;


    public EditGroupFragment() {
        // Required empty public constructor
    }

    public static EditGroupFragment newInstance(Bundle args){
        EditGroupFragment fragment = new EditGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        mProgress = new Progress(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userData  = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));

        listIDChoose = new HashSet<>();
        listIDRemove = new HashSet<>();
        listIDChoose.add(userData.getId());

        rvAllUser = view.findViewById(R.id.rvAllUser);
        rvParticipant = view.findViewById(R.id.rvParticipant);
        etGroupName = view.findViewById(R.id.etGroupName);
        tvDone = view.findViewById(R.id.tvDone);
        tvDone.setOnClickListener(this);

        if (getArguments()!=null&& getArguments().getString("groupId") != null) {
            group_name = getArguments().getString("groupName");
            isEditGroup = true;
            String idGroup = getArguments().getString("groupId");
            etGroupName.setText(group_name);
            groupEdit = (Group) getArguments().getSerializable("group");
            listFriend = groupEdit.listFriend;
            tvDone.setText("Save");

        } else {
            isEditGroup = false;
        }

        initAdapters();

    }


    public void initAdapters(){
        allUserList = new ArrayList<>();
        selectedUserList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvAllUser.setLayoutManager(linearLayoutManager);
        adapter = new ListPeopleAdapter(activity, listFriend, listIDChoose, listIDRemove, isEditGroup, groupEdit);
        rvAllUser.setAdapter(adapter);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDone:
                break;
        }
    }


    class ListPeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ListFriend listFriend;
        private LinearLayout btnAddGroup;
        private Set<String> listIDChoose;
        private Set<String> listIDRemove;
        private boolean isEdit;
        private Group editGroup;

        public ListPeopleAdapter(Context context, ListFriend listFriend, Set<String> listIDChoose, Set<String> listIDRemove, boolean isEdit, Group editGroup) {
            this.context = context;
            this.listFriend = listFriend;
            this.btnAddGroup = btnAddGroup;
            this.listIDChoose = listIDChoose;
            this.listIDRemove = listIDRemove;

            this.isEdit = isEdit;
            this.editGroup = editGroup;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_add_friend, parent, false);
            return new ItemFriendHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ItemFriendHolder) holder).txtName.setText(listFriend.getListFriend().get(position).getName());
            final String id = listFriend.getListFriend().get(position).getId();
            if (!listFriend.getListFriend().get(position).getProfilePicture().isEmpty()) {

            }
            ((ItemFriendHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        listIDChoose.add(id);
                        listIDRemove.remove(id);
                    } else {
                        listIDRemove.add(id);
                        listIDChoose.remove(id);
                    }

                }
            });
            if (isEdit && editGroup.member.contains(id)) {
                ((ItemFriendHolder) holder).checkBox.setChecked(true);
            }else if(editGroup != null && !editGroup.member.contains(id)){
                ((ItemFriendHolder) holder).checkBox.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return listFriend.getListFriend().size();
        }
    }

    class ItemFriendHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtEmail;
        public CircleImageView avata;
        public CheckBox checkBox;

        public ItemFriendHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkAddPeople);
        }
    }
}

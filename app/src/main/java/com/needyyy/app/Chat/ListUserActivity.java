package com.needyyy.app.Chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.needyyy.app.Chat.Adapter.ListUsersAdapter;
import com.needyyy.app.Chat.common.Common;
import com.needyyy.app.Chat.holder.QbUsersHolder;
import com.needyyy.app.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUserActivity extends AppCompatActivity {

    ListView userList;
    Button createchat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        userList=(ListView)findViewById(R.id.lst_user);
        userList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        createchat=(Button) findViewById(R.id.create_chat);
        createchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=userList.getCount();
                if(userList.getCheckedItemPositions().size()==1)
                {
                    createPrivateChat(userList.getCheckedItemPositions());
                }
                else if(userList.getCheckedItemPositions().size()>1)
                {
                    createGroupchat(userList.getCheckedItemPositions());
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Please select friend to chat",Toast.LENGTH_SHORT).show();
                }
            }
        });
        retriveAllUser();
    }

    private void createPrivateChat(SparseBooleanArray checkedItemPosition) {

        final ProgressDialog mdialog=new ProgressDialog(ListUserActivity.this);
        mdialog.setMessage("please wait...");
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();


        int countchoide=userList.getCount();


        for(int i=0;i<countchoide;i++)
        {
            if(checkedItemPosition.get(i))
            {
                QBUser users= (QBUser) userList.getItemAtPosition(i);
                QBChatDialog dialog= DialogUtils.buildPrivateDialog(users.getId());


                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mdialog.dismiss();
                        Toast.makeText(getBaseContext(),"make private chat dialog secessfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private void createGroupchat(SparseBooleanArray checkedItemPosition) {
        final ProgressDialog mdialog=new ProgressDialog(ListUserActivity.this);
        mdialog.setMessage("please wait...");
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();

        int countchoide=userList.getCount();
        ArrayList<Integer>  occcupiedidlist=new ArrayList<>();

        for(int i=0;i<occcupiedidlist.size();i++)
        {
            if(checkedItemPosition.get(i))
            {
                QBUser users= (QBUser) userList.getItemAtPosition(i);
                occcupiedidlist.add(users.getId());
            }
        }
        // create chat dialog

        QBChatDialog dialog=new QBChatDialog();
        dialog.setName(Common.createchatdialogname(occcupiedidlist));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occcupiedidlist);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mdialog.dismiss();
                Toast.makeText(getBaseContext(),"make chat dialog secessfully",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retriveAllUser() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QbUsersHolder.getInstance().putUsers(qbUsers);
                ArrayList<QBUser> getcurrentuser=new ArrayList<QBUser>();
                for(QBUser user : qbUsers)
                {
                    if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()));
                    getcurrentuser.add(user);
                }

                ListUsersAdapter listUsersAdapter=new ListUsersAdapter(getBaseContext(),getcurrentuser);
                userList.setAdapter(listUsersAdapter);
                listUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

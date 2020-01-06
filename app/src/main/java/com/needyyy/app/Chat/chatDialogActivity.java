package com.needyyy.app.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Chat.Adapter.ChatDialogAdapter;
import com.needyyy.app.Chat.common.Common;
import com.needyyy.app.Chat.fragment.ChatFragment;
import com.needyyy.app.Chat.fragment.ChatReceiveFragment;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;
import com.needyyy.app.Chat.holder.QbUsersHolder;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.Constant;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;


public class chatDialogActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton floatingActionButton;
    ListView lstChatDialogs;
    private ImageView btnSearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_title;
    private ImageView btnBack;
    int selectedTab=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);

      //  createsessionforchat();
       // loadchatdialogs();
        tv_title =(TextView) findViewById(R.id.tv_title);
        lstChatDialogs =(ListView) findViewById(R.id.lstchatdialog);
        btnSearch      = findViewById(R.id.btn_serarch);
        btnSearch.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
        {
            tabLayout.setVisibility(View.GONE);
        }
        else if(AppController.getManager().getInterest().equals(Constant.DATING))
        {
            tabLayout.setVisibility(View.VISIBLE);
        }
        setupViewPager(viewPager);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(chatDialogActivity.this, HomeActivity.class);
//                startActivity(intent);

                finish();
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tv_title.setText("Chat");


        lstChatDialogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog =(QBChatDialog)lstChatDialogs.getAdapter().getItem(position);
                Intent intent=new Intent(chatDialogActivity.this, ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                // intent.putExtra("opponent_id", String.valueOf(qbChatDialog.getRecipientId()));
                startActivity(intent);

            }
        });

        floatingActionButton=(FloatingActionButton) findViewById(R.id.chatdialog_adduser);
        btnSearch.setOnClickListener(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(chatDialogActivity.this, ListUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadchatdialogs() {
        QBRequestGetBuilder qbRequestGetBuilder=new QBRequestGetBuilder();
        qbRequestGetBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null,qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                if(qbChatDialogs!=null) {
                    ChatDialogAdapter chatDialogAdapter = new ChatDialogAdapter(getBaseContext(), qbChatDialogs);
                    lstChatDialogs.setAdapter(chatDialogAdapter);
                    chatDialogAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createsessionforchat() {
        final ProgressDialog mdialog= new ProgressDialog(chatDialogActivity.this);
        //  mdialog.setMessage("please wait");
        // mdialog.setCanceledOnTouchOutside(false);
        //  mdialog.show();

        String user,password;
        user=getIntent().getStringExtra("user");
        password=getIntent().getStringExtra("password");


        // load all user and save to cache
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QbUsersHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
        final QBUser qbUser=new QBUser(user,password);

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        // mdialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        //      Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadchatdialogs();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_serarch:
                break;
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ChatFragment.newInstance("single",""), "Chats");
        if(AppController.getManager().getInterest().equals(Constant.SOCIAL))
        {

        }
        else
        {
            adapter.addFragment(ChatReceiveFragment.newInstance(1), "Request");
        }

        viewPager.setAdapter(adapter);

        if(getIntent()!=null && getIntent().hasExtra("selectedTab"))
        {
            selectedTab=getIntent().getIntExtra("selectedTab",0);
            if(selectedTab==1){
                viewPager.setCurrentItem(1);
            }
            if(selectedTab==0)
            {
                viewPager.setCurrentItem(0);
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

package com.needyyy.app.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.needyyy.app.Chat.Adapter.ChatMessageAdapter;
import com.needyyy.app.Chat.common.Common;
import com.needyyy.app.Chat.groupchatwebrtc.activities.BaseActivity;
import com.needyyy.app.Chat.holder.QBChatMessagesHolder;
import com.needyyy.app.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatMessageActivity extends BaseActivity {

    QBChatDialog qbChatDialog;
    ListView lstChatMessages;
    ImageButton submitButton;
    EditText edtContent;
    ChatMessageAdapter adapter;
    ImageView ivAudioCall, ivVideoCall;
    String opponent_id="96571905";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        initviews();

        initChatDialogs();

        retrieveMessage();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QBChatMessage chatMessage=new QBChatMessage();
                chatMessage.setBody(edtContent.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                // put message to cache

                QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(),chatMessage);
                ArrayList<QBChatMessage> messages= QBChatMessagesHolder.getInstance().getChatMessageByDialodId(qbChatDialog.getDialogId());
                adapter =new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                lstChatMessages.deferNotifyDataSetChanged();

                // remove text from edit text

                edtContent.setText("");
                edtContent.setFocusable(true);
            }
        });
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void retrieveMessage() {

        QBMessageGetBuilder messageGetBuilder=new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);
        if(qbChatDialog!=null)
        {
            QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {

                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(),qbChatMessages);
                    adapter=new ChatMessageAdapter(getBaseContext(),qbChatMessages);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
    }

    private void initChatDialogs() {

        qbChatDialog =(QBChatDialog) getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        // Register listner Incoming mesaage

        QBIncomingMessagesManager incomingMessage=QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessage.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });

        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                // cache message

                QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialodId(qbChatMessage.getDialogId());
                adapter=new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initviews() {
        lstChatMessages=(ListView) findViewById(R.id.list_of_message);
        submitButton=(ImageButton) findViewById(R.id.send_button);
        edtContent=(EditText) findViewById(R.id.edt_content);
    }
}

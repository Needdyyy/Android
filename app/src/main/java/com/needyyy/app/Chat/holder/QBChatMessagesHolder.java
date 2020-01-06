 package com.needyyy.app.Chat.holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 21-08-2019.
 */

public class QBChatMessagesHolder  {

    private static QBChatMessagesHolder instance;
    private HashMap<String,ArrayList<QBChatMessage>> qbChatMessageArray;

    public static synchronized QBChatMessagesHolder getInstance()
    {
        QBChatMessagesHolder qbChatMessagesHolder;
        synchronized (QBChatMessagesHolder.class)
        {
            if(instance==null)

                instance=new QBChatMessagesHolder();
                qbChatMessagesHolder=instance;

        }
        return qbChatMessagesHolder;
    }

    private QBChatMessagesHolder()
    {
        this.qbChatMessageArray=new HashMap<>();
    }
    public void putMessages(String dialogId,ArrayList<QBChatMessage> qbChatMessages)
    {
        this.qbChatMessageArray.put(dialogId,qbChatMessages);
    }

    public void putMessage(String dialogId,QBChatMessage qbChatMessage)
    {
        List<QBChatMessage> lstresult =(List)this.qbChatMessageArray.get(dialogId);
        lstresult.add(qbChatMessage);
        ArrayList<QBChatMessage> isAdded =new ArrayList<>(lstresult.size());
        isAdded.addAll(lstresult);
        putMessages(dialogId,isAdded);
    }

    public ArrayList<QBChatMessage> getChatMessageByDialodId(String dialodId)
    {
        return (ArrayList<QBChatMessage>)this.qbChatMessageArray.get(dialodId);
    }
 }

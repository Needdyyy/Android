package com.needyyy.app.Chat.holder;

import android.content.Intent;
import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21-08-2019.
 */

public class QbUsersHolder {

    private static QbUsersHolder instance;

    private SparseArray<QBUser> qbUserSparceArray;

    public static synchronized QbUsersHolder getInstance()
    {
        if(instance == null)
            instance=new QbUsersHolder();
            return instance;

    }

    private QbUsersHolder()
    {
        qbUserSparceArray=new SparseArray<>();
    }

    public void putUsers(List<QBUser> users)
    {
        for(QBUser user:users)
        {
            putUser(user);
        }
    }

    private void putUser(QBUser user) {

        qbUserSparceArray.put(user.getId(),user);
    }

    public QBUser getuserId(int id)
    {
        return qbUserSparceArray.get(id);
    }

    public List<QBUser> getUserByIds(List<Integer> ids)
    {
        List<QBUser> qbUser=new ArrayList<>();
        for(Integer id:ids)
        {
            QBUser user=getuserId(id);
            if(user!=null)
            {
                qbUser.add(user);
            }
        }
        return qbUser;
    }
}

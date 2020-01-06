package com.needyyy.app.Chat.common;


import com.needyyy.app.Chat.holder.QbUsersHolder;
import com.quickblox.users.model.QBUser;
import com.needyyy.app.R;
import java.util.List;

/**
 * Created by Admin on 21-08-2019.
 */

public class Common {

    public static final String DIALOG_EXTRA = "Dialogs";
    public static String createchatdialogname(List<Integer> qbUsers)
    {
        List<QBUser> qbUsers1= QbUsersHolder.getInstance().getUserByIds(qbUsers);
        StringBuilder name=new StringBuilder();
        for(QBUser qbUser : qbUsers1)
            name.append("kaka"+" ");
        if(name.length()>30)
        {
            name.replace(30,name.length()-1,"....");
        }
        return name.toString();

    }

}

package com.needyyy.app.Chat.model;

import java.io.Serializable;

public class Group extends Room implements Serializable {
    public String id;
    public ListFriend listFriend;

    public Group(){
        listFriend = new ListFriend();
    }
}

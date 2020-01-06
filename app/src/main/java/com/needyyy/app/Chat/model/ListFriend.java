package com.needyyy.app.Chat.model;

import com.needyyy.app.Modules.AddPost.models.People;

import java.util.ArrayList;

public class ListFriend {
    private ArrayList<People> listFriend;

    public ArrayList<People> getListFriend() {
        return listFriend;
    }

    public ListFriend(){
        listFriend = new ArrayList<>();
    }

    public String getAvataById(String id){
        for(People friend: listFriend){
            if(id.equals(friend.getId())){
                return friend.getProfilePicture();
            }
        }
        return "";
    }

    public void setListFriend(ArrayList<People> listFriend) {
        this.listFriend = listFriend;
    }
}
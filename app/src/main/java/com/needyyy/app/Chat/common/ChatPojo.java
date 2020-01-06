package com.needyyy.app.Chat.common;

import java.io.Serializable;

public class ChatPojo implements Serializable {

    String user_id;
    String message;
    String name;
    String date;
    String is_active;
    String profile_picture;
    String type;
    String duration;
    String time;

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ChatPojo(String user_id, String message, String name, String date, String is_active, String profile_picture, String type, String duration, String time, String stage) {
        this.user_id = user_id;
        this.message = message;
        this.name = name;
        this.date = date;
        this.is_active = is_active;
        this.profile_picture = profile_picture;
        this.type = type;
        this.duration = duration;
        this.time = time;
        this.stage = stage;
    }

    String stage;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChatPojo() {
    }




    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

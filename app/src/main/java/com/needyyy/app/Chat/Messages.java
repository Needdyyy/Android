package com.needyyy.app.Chat;

/**
 * Created by KSHITIZ on 3/27/2018.
 * ----CREATED TO WORK WITH "messages" CHILD IN DATABASE----
 */

public class Messages {

    private String message,type;
    private long time;
    private boolean seen;
    private String from;
    private String status;
    private String key;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Messages(){

    }
    public Messages(String message, String type, long time, boolean seen, String from,String status) {
        this.message = message;
        this.type    = type;
        this.time    = time;
        this.seen    = seen;
        this.from    = from;
        this.status  = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}

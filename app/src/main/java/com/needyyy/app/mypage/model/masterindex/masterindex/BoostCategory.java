package com.needyyy.app.mypage.model.masterindex.masterindex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BoostCategory implements Serializable {

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @SerializedName("id")
    @Expose
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("text")
    @Expose
    private String text;
}

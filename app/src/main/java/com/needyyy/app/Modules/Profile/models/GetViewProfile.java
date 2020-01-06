package com.needyyy.app.Modules.Profile.models;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetViewProfile implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ViewProfileData data;

    public GetViewProfile(Drawable drawable) {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ViewProfileData getData() {
        return data;
    }

    public void setData(ViewProfileData data) {
        this.data = data;
    }

}

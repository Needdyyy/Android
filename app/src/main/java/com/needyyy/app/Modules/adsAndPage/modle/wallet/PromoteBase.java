package com.needyyy.app.Modules.adsAndPage.modle.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoteBase {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PromoteData data;

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

    public PromoteData getData() {
        return data;
    }

    public void setData(PromoteData data) {
        this.data = data;
    }
}

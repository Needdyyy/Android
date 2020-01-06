package com.needyyy.app.Modules.adsAndPage.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCheckSumHash {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CheckSumPojo data;

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

    public CheckSumPojo getData() {
        return data;
    }

    public void setData(CheckSumPojo data) {
        this.data = data;
    }

}

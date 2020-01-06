package com.needyyy.app.Modules.adsAndPage.modle.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletData implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Result")
    @Expose
    private WalletDataResult result;

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

    public WalletDataResult getResult() {
        return result;
    }

    public void setResult(WalletDataResult result) {
        this.result = result;
    }

}

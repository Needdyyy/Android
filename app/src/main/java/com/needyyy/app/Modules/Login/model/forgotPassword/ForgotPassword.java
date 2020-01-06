
package com.needyyy.app.Modules.Login.model.forgotPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.needyyy.app.Modules.Login.model.register.OtpMainResult;

public class ForgotPassword {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private OtpMainResult data;

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

    public OtpMainResult getData() {
        return data;
    }

    public void setData(OtpMainResult data) {
        this.data = data;
    }

}

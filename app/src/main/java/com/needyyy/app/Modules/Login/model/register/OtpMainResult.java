package com.needyyy.app.Modules.Login.model.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpMainResult {
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}

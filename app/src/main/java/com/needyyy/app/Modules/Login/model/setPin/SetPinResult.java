package com.needyyy.app.Modules.Login.model.setPin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetPinResult {
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("is_fingerprint_enable")
    @Expose
    private String isFingerprintEnable;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getIsFingerprintEnable() {
        return isFingerprintEnable;
    }

    public void setIsFingerprintEnable(String isFingerprintEnable) {
        this.isFingerprintEnable = isFingerprintEnable;
    }

}
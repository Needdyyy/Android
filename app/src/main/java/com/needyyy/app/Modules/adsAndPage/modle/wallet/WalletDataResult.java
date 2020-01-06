package com.needyyy.app.Modules.adsAndPage.modle.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletDataResult implements Serializable {
    @SerializedName("wallet_bal")
    @Expose
    private String walletBal;

    public String getWalletBal() {
        return walletBal;
    }

    public void setWalletBal(String walletBal) {
        this.walletBal = walletBal;
    }
}
package com.needyyy.app.Modules.adsAndPage.modle.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {

    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}

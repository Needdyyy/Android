package com.needyyy.app.Modules.adsAndPage.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Budget implements Serializable {
    @SerializedName("budget_price")
    @Expose
    private Integer budgetPrice;
    private Boolean isselected;
    private Double targetuser ;

    public Boolean getIsselected() {
        return isselected;
    }

    public void setIsselected(Boolean isselected) {
        this.isselected = isselected;
    }

    public double getTargetuser() {
        return targetuser;
    }

    public void setTargetuser(Double targetuser) {
        this.targetuser = targetuser;
    }


    public Integer getBudgetPrice() {
        return budgetPrice;
    }

    public void setBudgetPrice(Integer budgetPrice) {
        this.budgetPrice = budgetPrice;
    }

}

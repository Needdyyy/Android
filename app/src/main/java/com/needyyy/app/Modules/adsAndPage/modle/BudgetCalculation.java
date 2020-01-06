package com.needyyy.app.Modules.adsAndPage.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BudgetCalculation implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("min_reach")
    @Expose
    private String minReach;
    @SerializedName("max_reach")
    @Expose
    private String maxReach;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getMinReach() {
        return minReach;
    }

    public void setMinReach(String minReach) {
        this.minReach = minReach;
    }

    public String getMaxReach() {
        return maxReach;
    }

    public void setMaxReach(String maxReach) {
        this.maxReach = maxReach;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}

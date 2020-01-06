
package com.needyyy.app.Modules.adsAndPage.modle.wallet.modle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("global_post")
    @Expose
    private List<GlobalPost_> globalPost = null;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public List<GlobalPost_> getGlobalPost() {
        return globalPost;
    }

    public void setGlobalPost(List<GlobalPost_> globalPost) {
        this.globalPost = globalPost;
    }

}

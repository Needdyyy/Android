
package com.needyyy.app.mypage.model.masterindex.masterindex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchHistory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("s_text")
    @Expose
    private String sText;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSText() {
        return sText;
    }

    public void setSText(String sText) {
        this.sText = sText;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}

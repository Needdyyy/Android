package com.needyyy.app.Modules.Home.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.needyyy.app.Modules.AddPost.models.AddPost.TaggedPerson;

import java.util.List;

public class UserListBase {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TaggedPerson> data = null;

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

    public List<TaggedPerson> getData() {
        return data;
    }

    public void setData(List<TaggedPerson> data) {
        this.data = data;
    }
}

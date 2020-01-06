
package com.needyyy.app.Modules.Profile.models;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("link")
    @Expose
    private Object link;
    @SerializedName("file_type")
    @Expose
    private Object fileType;

    public Photo(Drawable drawable) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Object getLink() {
        return link;
    }

    public void setLink(Object link) {
        this.link = link;
    }

    public Object getFileType() {
        return fileType;
    }

    public void setFileType(Object fileType) {
        this.fileType = fileType;
    }

}

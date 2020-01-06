package com.needyyy.app.Modules.Home.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostMetum implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("link")
    @Expose
    private String link;

    public String getFilelink() {
        return filelink;
    }

    public void setFilelink(String filelink) {
        this.filelink = filelink;
    }

    @SerializedName("file_link")
    @Expose
    private String filelink;
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;


    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    @SerializedName("views")
    @Expose
    private String views;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}

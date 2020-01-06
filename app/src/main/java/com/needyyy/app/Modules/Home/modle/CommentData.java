package com.needyyy.app.Modules.Home.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("name")
    @Expose
    private String name;

    public String getUserId() {
        return userId;
    }

    public String getIshide() {
        return ishide;
    }

    @SerializedName("user_id")
    @Expose
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIshide(String ishide) {
        this.ishide = ishide;
    }

    @SerializedName("is_hide")
    @Expose
    private String ishide;

    public String getReplycount() {
        return replycount;
    }

    public void setReplycount(String replycount) {
        this.replycount = replycount;
    }

    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;

    @SerializedName("sub_comment_count")
    @Expose
    private String replycount;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

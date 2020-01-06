
package com.needyyy.app.mypage.model.Activities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tagged_people")
    @Expose
    private List<Object> taggedPeople = null;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("shares_name")
    @Expose
    private String sharesName;
    @SerializedName("shares_profile")
    @Expose
    private String sharesProfile;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("share_date")
    @Expose
    private String shareDate;
    @SerializedName("my_vote")
    @Expose
    private String myVote;
    @SerializedName("my_share")
    @Expose
    private String myShare;
    @SerializedName("post_meta")
    @Expose
    private List<PostMetum> postMeta = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Object> getTaggedPeople() {
        return taggedPeople;
    }

    public void setTaggedPeople(List<Object> taggedPeople) {
        this.taggedPeople = taggedPeople;
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getSharesName() {
        return sharesName;
    }

    public void setSharesName(String sharesName) {
        this.sharesName = sharesName;
    }

    public String getSharesProfile() {
        return sharesProfile;
    }

    public void setSharesProfile(String sharesProfile) {
        this.sharesProfile = sharesProfile;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }

    public String getMyShare() {
        return myShare;
    }

    public void setMyShare(String myShare) {
        this.myShare = myShare;
    }

    public List<PostMetum> getPostMeta() {
        return postMeta;
    }

    public void setPostMeta(List<PostMetum> postMeta) {
        this.postMeta = postMeta;
    }

}

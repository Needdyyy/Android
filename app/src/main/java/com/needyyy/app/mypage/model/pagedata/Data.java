
package com.needyyy.app.mypage.model.pagedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.needyyy.app.Modules.Profile.models.Photo;

import java.util.List;

public class Data {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("dislikes")
    @Expose
    private String dislikes;
    @SerializedName("borings")
    @Expose
    private String borings;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("my_vote")
    @Expose
    private String myVote;
    @SerializedName("is_bookmark")
    @Expose
    private String isBookmark;
    @SerializedName("is_follow")
    @Expose
    private String isFollow;
    @SerializedName("is_chat_iniatialised")
    @Expose
    private String isChatIniatialised;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("videos")
    @Expose
    private List<Photo> videos = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getBorings() {
        return borings;
    }

    public void setBorings(String borings) {
        this.borings = borings;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }

    public String getIsBookmark() {
        return isBookmark;
    }

    public void setIsBookmark(String isBookmark) {
        this.isBookmark = isBookmark;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public String getIsChatIniatialised() {
        return isChatIniatialised;
    }

    public void setIsChatIniatialised(String isChatIniatialised) {
        this.isChatIniatialised = isChatIniatialised;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getVideos() {
        return videos;
    }

    public void setVideos(List<Photo> videos) {
        this.videos = videos;
    }


}

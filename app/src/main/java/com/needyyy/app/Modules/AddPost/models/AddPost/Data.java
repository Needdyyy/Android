
package com.needyyy.app.Modules.AddPost.models.AddPost;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("display_picture")
    @Expose
    private String displayPicture;
    @SerializedName("post_headline")
    @Expose
    private String postHeadline;
    @SerializedName("post_tag")
    @Expose
    private Object postTag;
    @SerializedName("post_tag_id")
    @Expose
    private String postTagId;
    @SerializedName("is_shared")
    @Expose
    private String isShared;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("dislikes")
    @Expose
    private String dislikes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("share")
    @Expose
    private String share;
    @SerializedName("report_abuse")
    @Expose
    private String reportAbuse;
    @SerializedName("pinned_post")
    @Expose
    private String pinnedPost;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("segment_type")
    @Expose
    private String segmentType;
    @SerializedName("is_liked")
    @Expose
    private String isLiked;
    @SerializedName("is_bookmarked")
    @Expose
    private Boolean isBookmarked;
    @SerializedName("post_data")
    @Expose
    private PostData postData;
    @SerializedName("post_owner_info")
    @Expose
    private PostOwnerInfo postOwnerInfo;
    @SerializedName("tagged_people")
    @Expose
    private List<TaggedPerson> taggedPeople = null;
    @SerializedName("sub_cate_id")
    @Expose
    private String subCateId;
    @SerializedName("coins_for_post")
    @Expose
    private String coinsForPost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public String getPostHeadline() {
        return postHeadline;
    }

    public void setPostHeadline(String postHeadline) {
        this.postHeadline = postHeadline;
    }

    public Object getPostTag() {
        return postTag;
    }

    public void setPostTag(Object postTag) {
        this.postTag = postTag;
    }

    public String getPostTagId() {
        return postTagId;
    }

    public void setPostTagId(String postTagId) {
        this.postTagId = postTagId;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getReportAbuse() {
        return reportAbuse;
    }

    public void setReportAbuse(String reportAbuse) {
        this.reportAbuse = reportAbuse;
    }

    public String getPinnedPost() {
        return pinnedPost;
    }

    public void setPinnedPost(String pinnedPost) {
        this.pinnedPost = pinnedPost;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public Boolean getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public PostData getPostData() {
        return postData;
    }

    public void setPostData(PostData postData) {
        this.postData = postData;
    }

    public PostOwnerInfo getPostOwnerInfo() {
        return postOwnerInfo;
    }

    public void setPostOwnerInfo(PostOwnerInfo postOwnerInfo) {
        this.postOwnerInfo = postOwnerInfo;
    }

    public List<TaggedPerson> getTaggedPeople() {
        return taggedPeople;
    }

    public void setTaggedPeople(List<TaggedPerson> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }

    public String getSubCateId() {
        return subCateId;
    }

    public void setSubCateId(String subCateId) {
        this.subCateId = subCateId;
    }

    public String getCoinsForPost() {
        return coinsForPost;
    }

    public void setCoinsForPost(String coinsForPost) {
        this.coinsForPost = coinsForPost;
    }

}

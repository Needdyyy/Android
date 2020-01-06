package com.needyyy.app.Modules.Home.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.needyyy.app.Modules.AddPost.models.AddPost.TaggedPerson;

import java.io.Serializable;
import java.util.List;

public class PostResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("boost_id")
    @Expose
    private String boostid;

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
    private List<TaggedPerson> taggedPeople = null;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comment")
    @Expose
    private String comment;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("gender")
    @Expose
    private String gender;

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public void setBorings(String borings) {
        this.borings = borings;
    }

    public String getBorings() {
        return borings;
    }

    @SerializedName("dislikes")
    @Expose
    private String dislikes;

    @SerializedName("borings")
    @Expose
    private String borings;

    @SerializedName("share_date")
    @Expose
    private String shareDate;

    @SerializedName("my_like")
    @Expose
    private String myLike;
    @SerializedName("my_share")
    @Expose
    private String myShare;
    @SerializedName("post_meta")
    @Expose
    private List<PostMetum> postMeta = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("is_vote")
    @Expose
    private String isVote;
    @SerializedName("is_bookmark")
    @Expose
    private String isBookmark;
    @SerializedName("is_follow")
    @Expose
    private String isFollow;

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public String getApprove_date() {
        return approve_date;
    }

    public String getName() {
        return name;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public void setApprove_date(String approve_date) {
        this.approve_date = approve_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    //   for anouther response
    @SerializedName("date_from")
    @Expose
    private String date_from;

    @SerializedName("date_to")
    @Expose
    private String date_to;

    @SerializedName("is_approved")
    @Expose
    private String is_approved;


    @SerializedName("approve_date")
    @Expose
    private String approve_date;

    @SerializedName("name")
    @Expose
    private String name;


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

    public List<TaggedPerson> getTaggedPeople() {
        return taggedPeople;
    }

    public void setTaggedPeople(List<TaggedPerson> taggedPeople) {
        this.taggedPeople = taggedPeople;
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

    @SerializedName("my_vote")
    @Expose
    private String myVote;

    public String getFeeling() {
        return feeling;
    }

    public String getFeeling_status() {
        return feeling_status;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public void setFeeling_status(String feeling_status) {
        this.feeling_status = feeling_status;
    }

    @SerializedName("feeling")
    @Expose
    private String feeling;

    @SerializedName("feeling_status")
    @Expose
    private String feeling_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoostid() {
        return boostid;
    }

    public void setBoostid(String boostid) {
        this.boostid = boostid;
    }


    public String getUserName() {
        return userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
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


    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
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

    public String getMyLike() {
        return myLike;
    }

    public void setMyLike(String myLike) {
        this.myLike = myLike;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIsVote() {
        return isVote;
    }

    public void setIsVote(String isVote) {
        this.isVote = isVote;
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

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }


}

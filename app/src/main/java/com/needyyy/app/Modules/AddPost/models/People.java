package com.needyyy.app.Modules.AddPost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Cbc-03 on 05/25/17.
 */

public class People implements Serializable {


    @SerializedName("total_rate")
    @Expose
    private String total_rate;

    @SerializedName("dob")
    @Expose
    private String dob;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @SerializedName("open_dating")
    @Expose
    private String open_dating;

    public String getOpen_dating() {
        return open_dating;
    }

    public void setOpen_dating(String open_dating) {
        this.open_dating = open_dating;
    }

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("interested_in")
    @Expose
    private String interested_in;

    public String getInterested_in() {
        return interested_in;
    }

    public void setInterested_in(String interested_in) {
        this.interested_in = interested_in;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    @SerializedName("avg_rating")
    @Expose
    private String avg_rating;

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    boolean isSelected  =false;
    @SerializedName("my_rate")
    @Expose
    private String my_rate;

    public String getMy_rate() {
        return my_rate;
    }

    public void setMy_rate(String my_rate) {
        this.my_rate = my_rate;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("memeber_since")
    @Expose
    private String memeberSince;
    @SerializedName("quick_blox_media_json")
    @Expose
    private String quickBloxMediaJson;
    @SerializedName("chat_node")
    @Expose
    private String chatNode;
    @SerializedName("is_friend")
    @Expose
    private String isFriend;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("from_id")
    @Expose
    private String fromCognito;

    @SerializedName("from_cognito_remove_date")
    @Expose
    private String fromCognitoRemoveDate;
    @SerializedName("to_id")
    @Expose
    private String toCognito;
    @SerializedName("to_cognito_remove_date")
    @Expose
    private String toCognitoRemoveDate;

    @SerializedName("distance")
    @Expose
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @SerializedName("is_online")
    @Expose
    private String isOnline;



    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    @SerializedName("last_seen")
    @Expose
    private String lastSeen;
    public String getQuickBloxMediaJson() {
        return quickBloxMediaJson;
    }

    public void setQuickBloxMediaJson(String quickBloxMediaJson) {
        this.quickBloxMediaJson = quickBloxMediaJson;
    }

    public String getChatNode() {
        return chatNode;
    }

    public void setChatNode(String chatNode) {
        this.chatNode = chatNode;
    }




    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromCognito() {
        return fromCognito;
    }

    public void setFromCognito(String fromCognito) {
        this.fromCognito = fromCognito;
    }

    public String getFromCognitoRemoveDate() {
        return fromCognitoRemoveDate;
    }

    public void setFromCognitoRemoveDate(String fromCognitoRemoveDate) {
        this.fromCognitoRemoveDate = fromCognitoRemoveDate;
    }

    public String getToCognito() {
        return toCognito;
    }

    public void setToCognito(String toCognito) {
        this.toCognito = toCognito;
    }

    public String getToCognitoRemoveDate() {
        return toCognitoRemoveDate;
    }

    public void setToCognitoRemoveDate(String toCognitoRemoveDate) {
        this.toCognitoRemoveDate = toCognitoRemoveDate;
    }


    public boolean isTagged() {
        return tagged;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    boolean tagged = false;
    public String getId() {
        return id;
    }
    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMemeberSince() {
        return memeberSince;
    }

    public void setMemeberSince(String memeberSince) {
        this.memeberSince = memeberSince;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package com.needyyy.app.Modules.Profile.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Profile.models.UserPicture.ProfessionDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewProfileData implements Serializable {




    @SerializedName("profile_picture_id")
    @Expose
    private String profile_picture_id;

    @SerializedName("cover_picture_id")
    @Expose
    private String cover_picture_id;


    @SerializedName("cover_picture")
    @Expose
    private String coverpicture;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("status")
    @Expose
    private String status;

    public String getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(String isfriend) {
        this.isfriend = isfriend;
    }

    @SerializedName("is_friend")
    @Expose
    private String isfriend;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @SerializedName("nick_name")
    @Expose
    private String nickname;

    public void setProfessionDetails(ArrayList<ProfessionDetails> professionDetails) {
        this.professionDetails = professionDetails;
    }

    public ArrayList<ProfessionDetails> getProfessionDetails() {
        return professionDetails;
    }

    @SerializedName("profession_detail")
    @Expose
    private ArrayList<ProfessionDetails> professionDetails = null;

    @SerializedName("user_address")
    @Expose
    private List<LocationDetailPojo> userAddress = null;


    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("friends")
    @Expose
    private List<People> friends = null;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }
    public void setCoverpicture(String coverpicture) {
        this.coverpicture = coverpicture;
    }

    public String getCoverpicture() {
        return coverpicture;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LocationDetailPojo> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<LocationDetailPojo> userAddress) {
        this.userAddress = userAddress;
    }
    public String getProfile_picture_id() {
        return profile_picture_id;
    }

    public void setProfile_picture_id(String profile_picture_id) {
        this.profile_picture_id = profile_picture_id;
    }

    public String getCover_picture_id() {
        return cover_picture_id;
    }

    public void setCover_picture_id(String cover_picture_id) {
        this.cover_picture_id = cover_picture_id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<People> getFriends() {
        return friends;
    }

    public void setFriends(List<People> friends) {
        this.friends = friends;
    }

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("c_code")
    @Expose
    private String c_code;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("is_private")
    @Expose
    private String is_private;

    @SerializedName("interested_in")
    @Expose
    private String interested_in;

    @SerializedName("hobbies")
    @Expose
    private String hobbies;

    @SerializedName("relation_ship")
    @Expose
    private String relation_ship;

    @SerializedName("open_dating")
    @Expose
    private String open_dating;

    public String getIs_block() {
        return is_block;
    }

    public void setIs_block(String is_block) {
        this.is_block = is_block;
    }

    @SerializedName("is_block")
    @Expose
    private String is_block;


    public String getEmail() {
        return email;
    }

    public String getC_code() {
        return c_code;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getIs_private() {
        return is_private;
    }

    public String getInterested_in() {
        return interested_in;
    }

    public String getHobbies() {
        return hobbies;
    }

    public String getRelation_ship() {
        return relation_ship;
    }

    public String getOpen_dating() {
        return open_dating;
    }
}

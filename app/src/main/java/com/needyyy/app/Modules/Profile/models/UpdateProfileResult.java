package com.needyyy.app.Modules.Profile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileResult implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nick_name")
    @Expose
    private String nickName;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("c_code")
    @Expose
    private String cCode;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("is_social")
    @Expose
    private String isSocial;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("social_token")
    @Expose
    private String socialToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("friends")
    @Expose
    private String friends;
    @SerializedName("interested_in")
    @Expose
    private String interestedIn;
    @SerializedName("relation_ship_id")
    @Expose
    private String relationShipId;
    @SerializedName("open_dating")
    @Expose
    private String openDating;

    public List<LocationDetailPojo> getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(List<LocationDetailPojo> locationDetail) {
        this.locationDetail = locationDetail;
    }

    @SerializedName("user_address")
    @Expose
    private List<LocationDetailPojo> locationDetail = null;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("educational_detail")
    @Expose
    private ArrayList<EducationalDetail> educationalDetail = null;


//    protected UpdateProfileResult(Parcel in) {
//        id = in.readString();
//        ssn = in.readString();
//        name = in.readString();
//        nickName = in.readString();
//        profilePicture = in.readString();
//        email = in.readString();
//        dob = in.readString();
//        cCode = in.readString();
//        mobile = in.readString();
//        gender = in.readString();
//        isSocial = in.readString();
//        socialType = in.readString();
//        socialToken = in.readString();
//        deviceType = in.readString();
//        deviceToken = in.readString();
//        pin = in.readString();
//        isPrivate = in.readString();
//        bio = in.readString();
//        friends = in.readString();
//        interestedIn = in.readString();
//        relationShipId = in.readString();
//        openDating = in.readString();
////        location = in.readString();
//        latitude = in.readString();
//        longitude = in.readString();
//        created = in.readString();
//        modified = in.readString();
//        status = in.readString();
//    }
//
//    public static final Creator<UpdateProfileResult> CREATOR = new Creator<UpdateProfileResult>() {
//        @Override
//        public UpdateProfileResult createFromParcel(Parcel in) {
//            return new UpdateProfileResult(in);
//        }
//
//        @Override
//        public UpdateProfileResult[] newArray(int size) {
//            return new UpdateProfileResult[size];
//        }
//    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCCode() {
        return cCode;
    }

    public void setCCode(String cCode) {
        this.cCode = cCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public void setSocialToken(String socialToken) {
        this.socialToken = socialToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getRelationShipId() {
        return relationShipId;
    }

    public void setRelationShipId(String relationShipId) {
        this.relationShipId = relationShipId;
    }

    public String getOpenDating() {
        return openDating;
    }

    public void setOpenDating(String openDating) {
        this.openDating = openDating;
    }

//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<EducationalDetail> getEducationalDetail() {
        return educationalDetail;
    }

    public void setEducationalDetail(ArrayList<EducationalDetail> educationalDetail) {
        this.educationalDetail = educationalDetail;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(ssn);
//        dest.writeString(name);
//        dest.writeString(nickName);
//        dest.writeString(profilePicture);
//        dest.writeString(email);
//        dest.writeString(dob);
//        dest.writeString(cCode);
//        dest.writeString(mobile);
//        dest.writeString(gender);
//        dest.writeString(isSocial);
//        dest.writeString(socialType);
//        dest.writeString(socialToken);
//        dest.writeString(deviceType);
//        dest.writeString(deviceToken);
//        dest.writeString(pin);
//        dest.writeString(isPrivate);
//        dest.writeString(bio);
//        dest.writeString(friends);
//        dest.writeString(interestedIn);
//        dest.writeString(relationShipId);
//        dest.writeString(openDating);
////        dest.writeString(location);
//        dest.writeString(latitude);
//        dest.writeString(longitude);
//        dest.writeString(created);
//        dest.writeString(modified);
//        dest.writeString(status);
//    }
}



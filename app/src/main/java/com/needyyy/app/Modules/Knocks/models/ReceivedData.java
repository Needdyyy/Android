
package com.needyyy.app.Modules.Knocks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceivedData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("memeber_since")
    @Expose
    private String memeberSince;
    @SerializedName("is_friend")
    @Expose
    private String isFriend;

    public String getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(String isprivate) {
        this.isprivate = isprivate;
    }

    @SerializedName("is_private")
    @Expose
    private String isprivate;

    public String getId() {
        return id;
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

}

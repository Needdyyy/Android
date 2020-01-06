
package com.needyyy.app.Modules.AddPost.models.AddPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOwnerInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("is_expert")
    @Expose
    private String isExpert;
    @SerializedName("is_mentor")
    @Expose
    private String isMentor;

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

    public String getIsExpert() {
        return isExpert;
    }

    public void setIsExpert(String isExpert) {
        this.isExpert = isExpert;
    }

    public String getIsMentor() {
        return isMentor;
    }

    public void setIsMentor(String isMentor) {
        this.isMentor = isMentor;
    }

}

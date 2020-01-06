
package com.needyyy.app.Modules.AddPost.models.AddPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostFile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("file_info")
    @Expose
    private String fileInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

}

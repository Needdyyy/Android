
package com.needyyy.app.Modules.AddPost.models.AddPost;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostData {

    @SerializedName("post_file")
    @Expose
    private List<PostFile> postFile = null;

    public List<PostFile> getPostFile() {
        return postFile;
    }

    public void setPostFile(List<PostFile> postFile) {
        this.postFile = postFile;
    }

}

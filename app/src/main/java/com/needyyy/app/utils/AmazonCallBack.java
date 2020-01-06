package com.needyyy.app.utils;

import com.needyyy.app.Modules.AddPost.models.addMedia.PostMedia;

import java.util.ArrayList;

public interface AmazonCallBack {
    void onS3UploadData(ArrayList<PostMedia> images);
}

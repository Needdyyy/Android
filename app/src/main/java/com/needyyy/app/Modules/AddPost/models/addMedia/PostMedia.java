package com.needyyy.app.Modules.AddPost.models.addMedia;

import android.graphics.Bitmap;
import android.net.Uri;

public class PostMedia {
    private String Filetype ;
    private Bitmap  link;
    private String file;
    private String id;
    private String file_name;

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    private Uri fileUri;
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath;
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFiletype() {
        return Filetype;
    }

    public void setFiletype(String filetype) {
        Filetype = filetype;
    }

    public Bitmap getLink() {
        return link;
    }

    public void setLink(Bitmap link) {
        this.link = link;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }



}

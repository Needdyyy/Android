package com.needyyy.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.needyyy.AppController;
import com.needyyy.app.Modules.AddPost.models.addMedia.PostMedia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class s3MediaUploading extends AsyncTask<ArrayList<PostMedia>, Integer, ArrayList<PostMedia>> {

    public ProgressBar progressBar;
    String MY_OBJECT_KEY;
    ArrayList<PostMedia> imagearrayStr;
    String amazonFileUploadLocationOriginal;
    AmazonCallBack amazonCallBack;
    Progress mprogress;
    Context context;
    Long contentLength;


    public s3MediaUploading(String amazonFileUploadLocationOriginal, Context context, AmazonCallBack amazonCallBack, ProgressBar progressBar) {
        this.amazonFileUploadLocationOriginal = amazonFileUploadLocationOriginal;
        this.amazonCallBack = amazonCallBack;
        this.progressBar = progressBar;
        this.context = context;
        mprogress = new Progress(context);
        mprogress.setCancelable(false);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (progressBar != null) {
            progressBar.setMax(100);
            if (values[1] > 0)
                progressBar.setProgress((values[0] / values[1]) * 100);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        imagearrayStr = new ArrayList<>();
        mprogress.show();
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<PostMedia> doInBackground(ArrayList<PostMedia>... params) {
        try {
            byte[] content = null;
            PutObjectRequest putObjectRequest;
            String finalimageurl = "";

            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    Constant.COGNITO_POOL_ID, // Identity pool ID
                    Regions.US_EAST_1 // Region
            );

//            AmazonS3Client amazonClient = new AmazonS3Client(new BasicAWSCredentials(Const.AMAZON_S3_ACCESS_KEY, Const.AMAZON_S3_SECRET_KEY));
            AmazonS3Client amazonClient = new AmazonS3Client(credentialsProvider);
            amazonClient.setEndpoint(Constant.AMAZON_S3_END_POINT);

            if (params[0].size() > 0) {
                for (PostMedia file : params[0]) {
                    int repeat = 1;
                    int i = 0;
                    if (file.getFiletype().equals(Constant.VIDEO)) repeat = 2;
                    else repeat = 1;

                    while (i < repeat) {

                        if (file.getFiletype().equals(Constant.VIDEO) && i == 0) {
                            file.setFile_name(Constant.AMAZON_S3_FILE_NAME_CREATION);
                            MY_OBJECT_KEY = file.getFile_name() + ".mp4";
                        } else if (file.getFiletype().equals(Constant.VIDEO) && i == 1) {
                            MY_OBJECT_KEY = file.getFile_name() + ".png";
                            amazonFileUploadLocationOriginal = Constant.AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES;
                        } else if (file.getFiletype().equals(Constant.IMAGE) && amazonFileUploadLocationOriginal.equals(Constant.AMAZON_S3_BUCKET_NAME_FEEDBACK))
                            MY_OBJECT_KEY = AppController.getManager().getId() + "_" + Calendar.getInstance().getTimeInMillis() + "_image.png";
                        else if (file.getFiletype().equals(Constant.IMAGE))
                            MY_OBJECT_KEY = AppController.getManager().getId() + "_" + Calendar.getInstance().getTimeInMillis() + "_image.jpg";
                        else if (file.getFiletype().equals(Constant.PDF))
                            MY_OBJECT_KEY = AppController.getManager().getId() + "_" + Calendar.getInstance().getTimeInMillis() + "_pdf.pdf";
                        else if (file.getFiletype().equals(Constant.DOC))
                            MY_OBJECT_KEY = AppController.getManager().getId() + "_" + Calendar.getInstance().getTimeInMillis() + "_doc.docx";
                        else if (file.getFiletype().equals(Constant.XLS))
                            MY_OBJECT_KEY = AppController.getManager().getId()  + "_" + Calendar.getInstance().getTimeInMillis() + "_xls.xlsx";

                        Log.e("Etag:", " MY_OBJECT_KEY: " + MY_OBJECT_KEY);

                        if (file.getFiletype().equals(Constant.PDF) ||
                                file.getFiletype().equals(Constant.DOC) ||
                                file.getFiletype().equals(Constant.XLS) ||
                                (file.getFiletype().equals(Constant.IMAGE) && amazonFileUploadLocationOriginal.equals(Constant.AMAZON_S3_BUCKET_NAME_FEEDBACK)) ||
                                (file.getFiletype().equals(Constant.VIDEO) && i == 0)) {
                            content = CommonUtil.FileToByteArray(file.getFile());

                        } else {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            file.getLink().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            content = stream.toByteArray();
                        }
                        ByteArrayInputStream bs = new ByteArrayInputStream(content);
                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        contentLength = Long.valueOf(content.length);
                        objectMetadata.setContentLength(contentLength);
                        putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, MY_OBJECT_KEY, bs, objectMetadata);
                        putObjectRequest.setProgressListener(new ProgressListener() {
                            @Override
                            public void progressChanged(com.amazonaws.services.s3.model.ProgressEvent progressEvent) {
                                Log.e("general Depreceateed", "total size " + contentLength);
                                Log.e("general Depreceateed", "total transferred " + String.valueOf(progressEvent.getBytesTransferred()));
                                Integer[] valuesProgress = new Integer[2];
                                valuesProgress[0] = Integer.valueOf(String.valueOf(contentLength));
                                valuesProgress[1] = Integer.valueOf(String.valueOf(progressEvent.getBytesTransferred()));
                                publishProgress(valuesProgress);
                            }
                        });
                        amazonClient.putObject(putObjectRequest);

                        if (amazonFileUploadLocationOriginal.equals(Constant.AMAZON_S3_BUCKET_NAME_VIDEO) || amazonFileUploadLocationOriginal.equals(Constant.AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES)) {
                            finalimageurl = MY_OBJECT_KEY;
                        } else
                            finalimageurl = Constant.AMAZON_S3_IMAGE_PREFIX + amazonFileUploadLocationOriginal + "/" + MY_OBJECT_KEY;
                        i++;
                    }
                    Log.e("Etag:", " image URL: " + finalimageurl);
                    PostMedia mediaFile = new PostMedia();
                    mediaFile.setFile(finalimageurl);
                    mediaFile.setLink(file.getLink());

                    switch (amazonFileUploadLocationOriginal) {
                        case Constant.AMAZON_S3_BUCKET_NAME_FANWALL_IMAGES:
                        case Constant.AMAZON_S3_BUCKET_NAME_PROFILE_IMAGES:
                            mediaFile.setFiletype(Constant.IMAGE);
                            mediaFile.setFile_name(MY_OBJECT_KEY);
                            break;
                        case Constant.AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES:
                        case Constant.AMAZON_S3_BUCKET_NAME_VIDEO:
                            mediaFile.setFiletype(Constant.VIDEO);//////=--------------
                            mediaFile.setFile_name(file.getFile_name());
                            break;

                        case Constant.AMAZON_S3_BUCKET_NAME_DOCUMENT:
                            mediaFile.setFiletype(file.getFiletype());
                            mediaFile.setFile_name(file.getFile_name());
                            break;
                    }


                    imagearrayStr.add(mediaFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagearrayStr;
    }

    @Override
    protected void onPostExecute(ArrayList<PostMedia> images) {
        super.onPostExecute(images);
        mprogress.hide();
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        amazonCallBack.onS3UploadData(images);
    }
}

package com.needyyy.app.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.File;

/**
 * Created by shobhit on 28/1/16.
 */
public class UploadAmazonS3 {
    private int tempid;
    private int count=0;
    private CognitoCachingCredentialsProvider credentialsProvider = null;
    private AmazonS3Client s3Client = null;
    private TransferUtility transferUtility = null;
    private static UploadAmazonS3 uploadAmazonS3;

    /**
     * Creating single tone object by defining private.
     * <p>
     * At the time of creating
     * </P>
     */
    private UploadAmazonS3(Context context, String canito_pool_id) {
        /**
         * Creating the object of the getCredentialProvider object. */
        credentialsProvider = getCredentialProvider(context, canito_pool_id);
        /**
         * Creating the object  of the s3Client */
        s3Client = getS3Client(context, credentialsProvider);

        /**
         * Creating the object of the TransferUtility of the Amazone.*/
        transferUtility = getTransferUtility(context, s3Client);

    }

    public static UploadAmazonS3 getInstance(Context context, String canito_pool_id) {
        if (uploadAmazonS3 == null) {
            uploadAmazonS3 = new UploadAmazonS3(context, canito_pool_id);
            return uploadAmazonS3;
        } else {
            return uploadAmazonS3;
        }

    }

    /**
     * <h3>Upload_data</h3>
     * <p>
     * Method is use to upload data in the amazone server.
     * <p>
     * </P>
     */

    public void Upload_data(String buckate_name, String fileName, File file, final Upload_CallBack callBack) {
//        profile_image = Constant.AWS_URL +Constant.BUCKET_NAME +"/PostImage/" + imageArraylist.get(i).getLastPathSegment();

        Log.d("Amazon transferUtility", transferUtility + "Amazon transferUtility file = " + file);
        if (transferUtility != null && file != null) {
            TransferObserver observer = transferUtility.upload(buckate_name, fileName, file);
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        callBack.sucess("sucess");
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

//                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
//                    if (percentage == 100) {
//
//
//                        if(observer.getBytesTransferred()==observer.getBytesTotal()){
//                            Log.e("percenrage","complate"+percentage);
//                            Log.e("percenrage","observer.getBytesTransferred()"+observer.getBytesTransferred());
//                            Log.e("percenrage","observer.getBytesTotal()"+observer.getBytesTotal());
//
//                        }
//
//                    }

                }

                @Override
                public void onError(int id, Exception ex) {
                    callBack.error(id + ":" + ex.toString());
                }
            });


        } else {
            callBack.error("Amamzones3 is not intialize or File is empty !");
        }
    }


    /**
     * This method is used to get the CredentialProvider and we provide only context as a parameter.
     *
     * @param context Here, we are getting the context from calling Activity.
     */
    private CognitoCachingCredentialsProvider getCredentialProvider(Context context, String pool_id) {
        if (credentialsProvider == null) {
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    pool_id, // Identity Pool ID
                    Constant.AWS_REGION // Region
            );
        }
        return credentialsProvider;
    }

    /**
     * This method is used to get the AmazonS3 Client
     * and we provide only context as a parameter.
     * and from here we are calling getCredentialProvider() function.
     *
     * @param context Here, we are getting the context from calling Activity.
     */
    private AmazonS3Client getS3Client(Context context, CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider) {
        if (s3Client == null) {
            s3Client = new AmazonS3Client(cognitoCachingCredentialsProvider);
        }
        return s3Client;
    }

    /**
     * This method is used to get the Transfer Utility
     * and we provide only context as a parameter.
     * and from here we are, calling getS3Client() function.
     *
     * @param context Here, we are getting the context from calling Activity.
     */
    private TransferUtility getTransferUtility(Context context, AmazonS3Client amazonS3Client) {
        if (transferUtility == null) {
            transferUtility = new TransferUtility(amazonS3Client, context.getApplicationContext());
        }
        return transferUtility;
    }


    /**
     * Interface for the sucess callback fro the Amazon uploading .
     */
    public interface Upload_CallBack {
        /**
         * Method for sucess .
         *
         * @param sucess it is true on sucess and false for falure.
         */
        void sucess(String sucess);

        /**
         * Method for falure.
         *
         * @param errormsg contains the error message.
         */
        void error(String errormsg);

    }

    public void delete_Item(String bucketName, String keyName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

}

package com.needyyy.app.ImageClasses;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;


/**
 * @author ASHUTOSH KUMAR
 *         This is Activity to initiate image taking either from gallery or by camera
 */
public class TakeImageClass {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    private static Activity mActivity;
    public static final String TAG = "MainActivity";
    public imagefromcropper imagecropInterface;

    public static String TEMP_PHOTO_FILE_NAME ="" ;
    public static String sImagePath = null;
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private static File mFileTemp;
    private static Uri mImageCaptureUri;

    public TakeImageClass(Activity activity, imagefromcropper imagecropInterface) {
        this.imagecropInterface = imagecropInterface;
        mActivity = activity;
        TEMP_PHOTO_FILE_NAME = AppController.getManager().getId() + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        String state = Environment.getExternalStorageState();
        sImagePath = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(activity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }


    public static void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                intent.putExtra("return-data", true);
                mActivity.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(mFileTemp));
                mActivity.sendBroadcast(scanIntent);
            } else {
                Toast.makeText(mActivity, "SD Card Is Not Avialable Can Not Capture the Image", Toast.LENGTH_LONG).show();
                /*
                 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
                //				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();
            Log.d(TAG, "cannot take picture", e);
        }
    }

    public static void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        mActivity.startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage(int orientationInDegree) {
        Intent intent = new Intent(mActivity, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        //intent.putExtra(CropImage.IMAGE_PATH, getRealPathFromURI(mImageCaptureUri));

        intent.putExtra(CropImage.ASPECT_X, 200);
        intent.putExtra(CropImage.ASPECT_Y, 200);

        if (mActivity instanceof BaseActivity) {
            intent.putExtra(CropImage.ASPECT_X, 0);
            intent.putExtra(CropImage.ASPECT_Y, 0);
            intent.putExtra(CropImage.SCALE, true);
        } else intent.putExtra(CropImage.SCALE, false);

        intent.putExtra(CropImage.ORIENTATION_IN_DEGREES, orientationInDegree);
//        mActivity.startActivity(intent);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public int getCameraPhotoOrientation(Context context) {
        int rotate = 0;
        try {

            File imageFile = new File(mFileTemp.getPath());
            context.getContentResolver().notifyChange(mImageCaptureUri, null);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("take image class", "in on activity result of  requestCode=>>>>>" + requestCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = mActivity.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(0);

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:
                startCropImage(0);
                Log.e(" REQUEST_CODE", "REQUEST_CODE_TAKE_PICTURE");
                break;
            case REQUEST_CODE_CROP_IMAGE:
                Log.e("Take Image Class ", "mTempFile Path =>" + mFileTemp.getPath());
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                Log.e("Yake Image Class ", "mTempFile Path =>" + mFileTemp.getPath());
                TakeImageClass.sImagePath = mFileTemp.getPath();
                imagecropInterface.imagePath(path);
                break;
        }
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    public static void getImagePickerDialog(final Activity ctx, final String title) {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(ctx);

        alertBuild
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        takePicture();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        openGallery();
                    }
                });
        AlertDialog dialog = alertBuild.create();
        dialog.show();
        int alertTitle = ctx.getResources().getIdentifier("alertTitle", "id", "android");
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
    }


    public Bitmap StringToBitMap(String str){
        return BitmapFactory.decodeFile(str);
    }

    public interface imagefromcropper {
        void imagePath(String str);
    }


}
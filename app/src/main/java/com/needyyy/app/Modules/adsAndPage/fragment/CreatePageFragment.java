package com.needyyy.app.Modules.adsAndPage.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import id.zelory.compressor.Compressor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import id.zelory.compressor.Compressor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_GALLERY;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_TAKE_PICTURE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class CreatePageFragment extends BaseFragment implements View.OnClickListener
{

    Uri selectedImage;
    private ImageView imgBanner;
    private ImageView imgPage;
    private TextInputEditText etTitle,etDescription,website,contact,address;
    private TextView btnNext;
    private int mediaType;
    Compressor compressor;
    Activity context;
    String banerImageUrl="",profileImageurl="";
    private Uri bannerUri,profileUri;
    private ArrayList<Uri> imagelist = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "PostFragment";
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public CreatePageFragment() {
        // Required empty public constructor
    }


    public static CreatePageFragment newInstance() {
        CreatePageFragment fragment = new CreatePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_create_page);
        context=getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View mView) {
        website       =mView.findViewById(R.id.website);
        contact       =mView.findViewById(R.id.contact);
        address       =mView.findViewById(R.id.address);
        imgBanner     = mView.findViewById(R.id.img_page_banner);
        imgPage       = mView.findViewById(R.id.img_add_pagephoto);
        etTitle       = mView.findViewById(R.id.et_page_title);
        etDescription = mView.findViewById(R.id.et_page_description);
        btnNext       = mView.findViewById(R.id.btn_next);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        btnNext.setOnClickListener(this);
        imgBanner.setOnClickListener(this);
        imgPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_page_banner:
                mediaType =1;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonIcon(R.drawable.ic_save)
                        .start(getActivity());
                break;
            case R.id.img_add_pagephoto:
                mediaType= 2;
                permissionCheck();
                break;
            case R.id.btn_next :
                checkValidation();
                break;

        }
    }

    private String getRealPathFromURI(final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    private void checkValidation() {
        if (bannerUri!=null){
            if (profileUri!=null){
                if (!etTitle.getText().toString().isEmpty()) {
                    if (!website.getText().toString().isEmpty()) {
                        if (!contact.getText().toString().isEmpty()) {
                            if (!address.getText().toString().isEmpty()) {
                                if (!etDescription.getText().toString().isEmpty()) {
                                    imagelist.add(0, bannerUri);
                                    imagelist.add(1, profileUri);
                                    ((HomeActivity) getActivity()).replaceFragment(PageCategoryFragment.newInstance(imagelist, etTitle.getText().toString().trim(), etDescription.getText().toString().trim(),contact.getText().toString(),website.getText().toString(),address.getText().toString()), true);
                                    etTitle.setText("");
                                    etDescription.setText("");
                                    contact.setText("");
                                    website.setText("");
                                    address.setText("");
                                } else {
                                    snackBar(getContext().getResources().getString(R.string.descriptionmsg));
                                }
                            } else {
                                snackBar(getContext().getResources().getString(R.string.titlemsg));
                            }
                        } else {
                            snackBar("Website is not empty");
                        }
                    } else {
                        snackBar("Contact is not empty");
                    }
                }
                else
                {
                    snackBar("Address is not empty");
                }
            }else{
                snackBar(getContext().getResources().getString(R.string.bannerprofilemsg));
            }
        }else{
            snackBar(getContext().getResources().getString(R.string.bannerimgmsg));
        }
    }

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions(getActivity(), permissions)) {

                onPickImage();

//                Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
//            }else {
////                Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
//                requestPermission();
            }
        } else {
            onPickImage();
        }
    }
    public void onPickImage() {
        getImagePickerDialog(getActivity(), "Select Option");
    }
    public void getImagePickerDialog(final Activity ctx, final String title) {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(ctx);

        alertBuild
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getImageFromCamera();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getImageFromGallery();
                    }
                });
        AlertDialog dialog = alertBuild.create();
        dialog.show();
        int alertTitle = ctx.getResources().getIdentifier("alertTitle", "id", "android");
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
    }

    /**
     * get image from camera
     */

    private void getImageFromCamera() {
        checkStorage();
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }

    /**
     * get image from camera or gallery
     */
    private void getImageFromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }


    /**
     * call this function to create image name and its uri
     */
    private File newFile;
    private Uri newProfileImageUri;
    private String state, imageName;

    public void checkStorage() {
        imageName = "";
        state = Environment.getExternalStorageState();

        imageName = Constant.PARENT_FOLDER + "_" + String.valueOf(System.nanoTime()) + ".png";

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newFile = new File(Environment.getExternalStorageDirectory(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        } else {
            newFile = new File(getActivity().getFilesDir(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        }
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                ArrayList<String> _arPermission = new ArrayList<String>();
                if (grantResults.length > 0) {
                    Log.d(TAG, "length" + permissions.length);
                    for (int i = 0; i < permissions.length; i++) {
                        Log.d(TAG, "lengthch" + permissions[i] + " " + grantResults[i]);
                        if (grantResults[i] != 0) {
                            _arPermission.add("" + grantResults[i]);
                        }
                    }
                    if (_arPermission.size() == 0) {
                        onPickImage();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        CommonUtil.showAlert(getActivity(), "These Permissions required for this app.Go to settings and enable permissions.", "Permissions");
                    }
                }
            }
        }
    }

    /**
     *  get selected file in onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("chekcer", "onActivityResult: 121");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                checkStorage();
                selectedImage = result.getUri();
                imageName = getRealPathFromURI(selectedImage);
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
//                    imgBanner.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                if (mediaType == 1) {
                    InputStream inputStream = null;
                    try {
                        inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(newFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        CommonUtil.copyStream(inputStream, fileOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File compressedImgFile = null;
                    try {
                        compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri2 = Uri.fromFile(compressedImgFile);
                    bannerUri=uri2;
                    imgBanner.setImageURI(bannerUri);

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                // Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }



        else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {

            try {
                // When an Image is picked
                if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
                        && null != data) {

                    // Get the Image from data
                    if (data.getData() != null) {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        checkStorage();
                        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                        CommonUtil.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
//                        if (mediaType == 1){
//                            imgBanner.setImageURI(newProfileImageUri);
//                        }else if(mediaType == 2){
//                            imgPage.setImageURI(newProfileImageUri);
//                        }
                        if (mediaType == 1) {
                            File compressedImgFile = null;
                            try {
                                compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Uri uri2 = Uri.fromFile(compressedImgFile);
                            selectedImage = uri2;
                            imgBanner.setImageURI(selectedImage);
                        } else if (mediaType == 2) {
                            File compressedImgFile = null;
                            try {
                                compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Uri uri2 = Uri.fromFile(compressedImgFile);
                            imgPage.setImageURI(newProfileImageUri);
                            profileUri = uri2;
                        }

                    } else {
                        Toast.makeText(getActivity(), "You haven't picked any Media",
                                Toast.LENGTH_LONG).show();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong, Try again...", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            if (newProfileImageUri != null) {
                if (mediaType == 1) {
                    File compressedImgFile = null;
                    try {
                        compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri2 = Uri.fromFile(compressedImgFile);

                    bannerUri = uri2;
                    imgBanner.setImageURI(newProfileImageUri);
                } else if (mediaType == 2) {
                    File compressedImgFile = null;
                    try {
                        compressedImgFile = new Compressor(getContext()).compressToFile(newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri2 = Uri.fromFile(compressedImgFile);

                    imgPage.setImageURI(newProfileImageUri);
                    profileUri = uri2;
                }
            }
        }
    }}

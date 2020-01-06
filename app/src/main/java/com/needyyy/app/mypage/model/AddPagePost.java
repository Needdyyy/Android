package com.needyyy.app.mypage.model;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.Base.BasePojo;
import com.needyyy.app.FileManager;
import com.needyyy.app.ImageClasses.TakeImageClass;
import com.needyyy.app.Modules.AddPost.Fragments.PostFragment;
import com.needyyy.app.Modules.AddPost.adapter.MediaAdapter;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.addMedia.PostMedia;
import com.needyyy.app.Modules.Home.Activities.CheckInActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Activities.TagSelection;
import com.needyyy.app.Modules.Home.modle.googlePlace.PlaceBase;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;
import com.needyyy.app.utils.AmazonCallBack;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.UploadAmazonS3;
import com.needyyy.app.webutils.WebInterface;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_GALLERY;
import static com.needyyy.app.ImageClasses.TakeImageClass.REQUEST_CODE_TAKE_PICTURE;
import static com.needyyy.app.constants.Constants.kCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPagePost extends BaseFragment implements  View.OnClickListener, TakeImageClass.imagefromcropper, AmazonCallBack, View.OnTouchListener {
    private File compressedImage;
    public ProgressBar progressBar;
    LinearLayout llProfile,llAddExtra;
    TextView tvPost, tvProfileName;
    TextInputLayout tilAddPost;
    private ArrayList<PostMedia> postMediaArrayList= new ArrayList<>();
    EditText etAddPost;
    ArrayList<People> taggedpeoplearrList;
    CircleImageView civProfile;
    public Activity activity;
    private  Uri selectedVideoUri;
    private String selectedVideoPath;
    private String type="1";
    private static final int SELECT_VIDEO = 1;
    private UserDataResult userData ;
    FlowLayout taggedpeopleFL;
    int isPostEdit = 0,uploadFileCount=0;
    public static final int REQUEST_CODE = 11;
    ArrayList<String> oldids, newids;
    List<View> LinearLayoutList, LinearLayoutIconList, taggedpeopleList;
    ArrayList<MediaFile> docMediaFiles;
    ImageView ivCamera,ivAttach,ivTag, ivUploaded;
    private ArrayList<Uri> imageArraylist= new ArrayList<>();
    private RecyclerView rvMeiaContainer;
    private MediaAdapter mediaAdapter ;
    File file = null;
    private int temp ;
    private String id;
    private Boolean isloading ;
    private ArrayList<String> uploadedImageList=new ArrayList<>();
    TakeImageClass takeImageClass;
    String profile_image,TEMP_Profile;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    int count=0;
    String MediaType;
    String taggedPeopleIdsAdded;
    private int STORAGE_PERMISSION_TYPE=0;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    Compressor compressor;
    public AddPagePost() {
        // Required empty public constructor
    }

    public static AddPagePost newInstance(String id) {
        AddPagePost fragment = new AddPagePost();
        Bundle args = new Bundle();
        args.putString("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_addpost);
        if (getArguments() != null) {
            id=getArguments().getString("id");
        }

        activity = getActivity();
    }

    @Override
    protected void initView(View mView) {
        checkStorage();
        rvMeiaContainer  = mView.findViewById(R.id.rv_media_container);
        llProfile        = mView.findViewById(R.id.ll_profile);
        llAddExtra       = mView.findViewById(R.id.ll_add_extra);
        tvPost           = mView.findViewById(R.id.tv_post_button);
        taggedpeopleFL   = (FlowLayout) mView.findViewById(R.id.taggedpeopleFL);
        tvProfileName    = mView.findViewById(R.id.tv_profile_name);
        civProfile       = mView.findViewById(R.id.civ_profile_pic);
        etAddPost        = mView.findViewById(R.id.et_add_post);
        tilAddPost       = mView.findViewById(R.id.til_add_post);
        ivCamera         = mView.findViewById(R.id.iv_camera);
        ivAttach         = mView.findViewById(R.id.iv_checkin);
        ivTag            = mView.findViewById(R.id.iv_tag);
        ivUploaded       = mView.findViewById(R.id.iv_uploaded);
        mView.findViewById(R.id.post_main).setOnTouchListener(this);

        LinearLayoutList = new ArrayList();
        taggedpeopleList = new ArrayList();
        docMediaFiles    = new ArrayList<>();
        LinearLayoutIconList = new ArrayList();

        compressor = new Compressor(getActivity());
        compressor.setQuality(50);


    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).manageToolbar("Add Post", "");
        civProfile.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivAttach.setOnClickListener(this);
        ivTag.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        setMediaAdapter();
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
        if (userData!=null){
            tvProfileName.setText(userData.getName());
            if (!TextUtils.isEmpty(userData.getProfilePicture())) {
                Glide.with(this)
                        .load(userData.getProfilePicture())
                        .into(civProfile);
            } else {
                civProfile.setImageResource(R.drawable.needyy);
            }
        }
        etAddPost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideSoftKeyboard(view);
                }
            }
        });
    }

    private void setMediaAdapter() {
        mediaAdapter = new MediaAdapter(getActivity(),postMediaArrayList, null, AddPagePost.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMeiaContainer.setLayoutManager(layoutManager);
        rvMeiaContainer.setAdapter(mediaAdapter);
        rvMeiaContainer.setHasFixedSize(true);

//        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemRecyclerDecoration(getContext(), R.drawable.canvas_recycler_divider);
//        rvKnockrequest.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_profile_pic:
                break;
            case R.id.tv_profile_name:
                break;
            case R.id.iv_camera:
                STORAGE_PERMISSION_TYPE=1;
                CommonUtil.hideKeyboard(getContext(),ivCamera);
                permissionCheck();
                break;
            case R.id.iv_checkin:
                Intent intent =new Intent(getActivity(), CheckInActivity.class);
                startActivityForResult(intent,REQUEST_CODE );
                break;
            case R.id.iv_tag:
                Intent tagUsers = new Intent(activity, TagSelection.class);
                tagUsers.putExtra(Constant.ALREADY_TAGGED_PEOPLE, taggedpeoplearrList);
                activity.startActivityForResult(tagUsers, ((HomeActivity) activity).TAG_USER);
                break;
            case R.id.ll_profile:
                break;
            case R.id.tv_post_button:
                if (!etAddPost.getText().toString().isEmpty()){
                    addPost();
                }
                else if(imageArraylist.size()!=0){
                    addPost();
                }else{
                    CommonUtil.showShortToast(getActivity(),(getContext().getString(R.string.add_post_errmsg)));
                }

                break;
        }

    }

    private void addPost() {

        if (postMediaArrayList != null && postMediaArrayList.size() > 0) {
            hideKeyboard(getContext());
            uploadFileCount = postMediaArrayList.size();
            if (uploadFileCount > 0) {
                showProgressDialog();
                for (int i = 0; i < postMediaArrayList.size(); i++) {
                    uploadPicToAmazon(i,postMediaArrayList.get(i));
                }
            } else {
                uploadPost();
            }
        } else {
            uploadPost();
        }
    }

    private void uploadPost() {
        JSONArray jsonArray = new JSONArray();
        for (PostMedia postMedia: postMediaArrayList) {
            JSONObject obj= new JSONObject();
            try {
                obj.put(Constant.FILE_TYPE,postMedia.getFiletype());
                obj.put(Constant.LINK,postMedia.getFile());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        if (taggedpeoplearrList != null && taggedpeoplearrList.size() > 0) {
            for (People res : taggedpeoplearrList) {
                if (TextUtils.isEmpty(taggedPeopleIdsAdded))
                    taggedPeopleIdsAdded = ","+res.getId();
                else taggedPeopleIdsAdded = taggedPeopleIdsAdded + "," + res.getId();
            }
            taggedPeopleIdsAdded=taggedPeopleIdsAdded+",";
        }


        if (CommonUtil.isConnectingToInternet(getContext())){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<BasePojo> call = Service.addPostt(etAddPost.getText().toString(),taggedPeopleIdsAdded,jsonArray,id,type,"","");
            call.enqueue(new Callback<BasePojo>() {
                @Override
                public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    BasePojo basePojo = response.body();
                    if (basePojo.getStatus()) {
                        CommonUtil.showShortToast(getActivity(),basePojo.getMessage());
                        getFragmentManager().popBackStack();
                    } else {
                        if (basePojo.getMessage().equals("110110")){
                            ((HomeActivity)getActivity()).logout();
                        }else{
                            snackBar(basePojo.getMessage());
                        }

                    }
                }
                @Override
                public void onFailure(Call<BasePojo> call, Throwable t) {
                    cancelProgressDialog();
                    snackBar(t.getMessage());
                }
            });
        }else{
            snackBar(getContext().getResources().getString(R.string.internetmsg));
        }
    }

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions(getActivity(), permissions)) {
//                if (STORAGE_PERMISSION_TYPE==1)
                onPickImage();
//                else  if (STORAGE_PERMISSION_TYPE==2){
//                    onPicDocument();
//                }
////                Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
//            }else {
////                Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
//                requestPermission();
            }
        } else {
//            if (STORAGE_PERMISSION_TYPE==1)
            onPickImage();
//            else  if (STORAGE_PERMISSION_TYPE==2){
//                onPicDocument();
//            }
        }
    }

    private void onPicDocument() {
        Intent intent = new Intent(activity, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setMaxSelection(1)
                .setShowFiles(true)
                .setSuffixes(new String[]{"txt", "pdf", "doc", "docx", "ppt", "pptx"})
                .setShowImages(false)
                .setShowVideos(false)
                .build());
        startActivityForResult(intent, Constant.REQUEST_TAKE_GALLERY_DOC);
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
                })
        .setNeutralButton("Video", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                showfilechooser();
            }
        })

        ;
        AlertDialog dialog = alertBuild.create();
        dialog.show();
        int alertTitle = ctx.getResources().getIdentifier("alertTitle", "id", "android");
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
        ((TextView) dialog.findViewById(alertTitle)).setGravity(Gravity.CENTER);
    }

    private void showfilechooser() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
    }

    /**
     * get image from camera or gallery
     */
    private void getImageFromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/* video/*");
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
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
        } catch (Exception e) {
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

    public void checkStorage(){
        imageName = "";
        state = Environment.getExternalStorageState();

        imageName = Constant.PARENT_FOLDER+"_"+ String.valueOf(System.nanoTime()) + ".png";

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newFile = new File(Environment.getExternalStorageDirectory(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        } else {
            newFile = new File(getActivity().getFilesDir(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        }
        Log.e("createVideofile","newProfileImageUri"+newProfileImageUri);
        // Log.e("createVideofile","imageName"+imageName);
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


            if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO &&  data !=null) {
            selectedVideoUri = data.getData();
            selectedVideoPath = FileManager.getPath(activity, selectedVideoUri);
            imageArraylist.add(Uri.parse(selectedVideoPath));
            MediaType = Constant.VIDEO;
            setupMedia(Constant.VIDEO);
        }

       else  if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {

            try {
                // When an Image is picked
                if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
                        && null != data) {

                    // Get the Image from data
                    if (data.getClipData()  != null) {
                        // showProgressDialog();
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            File f=new File(String.valueOf(uri));
                            File compressedImgFile = compressor.compressToFile(f);
                            Uri uri2= Uri.fromFile(compressedImgFile);
                            if(isImageFile(uri.getPath()) || uri.toString().contains("image")) {
                                checkStorage();
                            }else if(isVideoFile(uri.getPath()) || uri.toString().contains("video")) {
                                createVideofile();
                            }
                            mArrayUri.add(uri2);
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri2);
                            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                            CommonUtil.copyStream(inputStream, fileOutputStream);
                            fileOutputStream.close();
                            inputStream.close();
                            imageArraylist.add(newProfileImageUri);
                        }
                        setupMedia("");
                    }
                    else if(data.getData()!=null) {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        checkStorage();
                        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                        CommonUtil.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        imageArraylist.add(newProfileImageUri);
                        MediaType=Constant.IMAGE ;
                        setupMedia(Constant.IMAGE);

                    }


                    else {
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

        }
        else if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            if (newProfileImageUri!=null){
                ivUploaded.setImageURI(newProfileImageUri);
                imageArraylist.add(newProfileImageUri);
                //  uploadPicToAmazon(0,1);
                MediaType=Constant.IMAGE;
                setupMedia(Constant.IMAGE);

            }
        }else if (((HomeActivity) activity).TAG_USER == requestCode) {
            if (resultCode == RESULT_OK && data != null) {
                onAddPeopleTagList((ArrayList<People>) data.getExtras().getSerializable(Constant.ALREADY_TAGGED_PEOPLE));
//                Toast.makeText(activity, "You have tagged successfully", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == Constant.REQUEST_TAKE_GALLERY_DOC && resultCode == RESULT_OK && data != null) {
            docMediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (docMediaFiles != null && docMediaFiles.size() != 0)
                //  showProgressDialog();
                for (int i = 0; i < docMediaFiles.size() ; i++) {
                    uploadFileToAmazon(i,docMediaFiles.get(i));

                }
        } else if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

            PlaceBase placeBase =(PlaceBase)data.getExtras().getSerializable("key");
            // ((HomeActivity)getActivity()).replaceFragment(CheckinFragment.newInstance(placeBase));
            Log.e("response","==="+placeBase.getResult().getName());
            Log.e("response","==="+placeBase.getResult().getUrl());
            Log.e("response","==="+placeBase.getResult().getWebsite());
            Log.e("response","==="+placeBase.getResult().getVicinity());
            Log.e("response","==="+placeBase.getResult().getGeometry().getLocation().getLat());
            Log.e("response","==="+placeBase.getResult().getGeometry().getLocation().getLng());
        }
    }

    private void setupMedia(String type) {
        postMediaArrayList.clear();
        for (Uri img : imageArraylist) {
            if(isImageFile(img.getPath()) || img.toString().contains("image")) {
                checkStorage();
                type = Constant.IMAGE;
            }else if(isVideoFile(img.getPath()) || img.toString().contains("video")) {
                createVideofile();
                type= Constant.VIDEO;
            }
            PostMedia mediaFile = new PostMedia();
            mediaFile.setLink(CommonUtil.decodeSampledBitmap(img.getPath(), 200, 200));
            mediaFile.setFiletype(type);
            mediaFile.setFile_name("PostMedia/"+img.getLastPathSegment());
            mediaFile.setFilePath(img.getPath());
            mediaFile.setFileUri(img);
            postMediaArrayList.add(mediaFile);

        }
        mediaAdapter.notifyDataSetChanged();
    }


    private void uploadFileToAmazon(int i, MediaFile file) {
        String docpath = file.getPath();
        String arr[] = docpath.split("/");

        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(getActivity(), Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME, "PostImage/" + arr[arr.length - 1].replace(" ","_"), new File(file.getPath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String sucess) {

                profile_image = Constant.AWS_URL +Constant.BUCKET_NAME +"/PostImage/" + arr[arr.length - 1];


                PostMedia postMedia = new PostMedia();
                // postMedia.setLink(profile_image);

                if (!TextUtils.isEmpty(docpath) && !(docpath.contains(getString(R.string.pdf_extension)) || docpath.contains(getString(R.string.doc_extension)) || docpath.contains(getString(R.string.xls_extension))))
                    Toast.makeText(activity, R.string.file_format_error, Toast.LENGTH_SHORT).show();
                else {
                    if (docpath.contains(getString(R.string.pdf_extension))) {
                        //  postMedia.setImage(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.pdf));
                        postMedia.setFiletype(Constant.PDF);
                    } else if (docpath.contains(getString(R.string.doc_extension))) {
                        // mediaFile.setImage(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.doc));
                        postMedia.setFiletype(Constant.DOC);
                    } else if (docpath.contains(getString(R.string.xls_extension))) {
                        //  mediaFile.setImage(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.xls));
                        postMedia.setFiletype(Constant.XLS);
                    }
                    String arr[] = docpath.split("/");

                }
                postMediaArrayList.add(postMedia);
                uploadedImageList.add(profile_image);
                Log.d("PROFILE_IMAGE", ""+profile_image);
                Log.d("PROFILE_IMAGE", "file name"+arr[arr.length - 1].replace(" ","_") );
                //Toast.makeText(activity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(String errormsg) {

                Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", ""+errormsg);
            }
        });
    }


    // This is to add tagged all People at once.
    private void onAddPeopleTagList(ArrayList<People> taggedpeoplearrLists) {

        if (taggedpeoplearrList == null) taggedpeoplearrList = new ArrayList<>();
        if (taggedpeoplearrList.size() > 0) taggedpeoplearrList.clear();
        taggedpeoplearrList.addAll(taggedpeoplearrLists);
        if (taggedpeopleFL.getChildCount() > 0) taggedpeopleFL.removeAllViews();

        for (int i = 0; i < taggedpeoplearrList.size(); i++) {
            People response = taggedpeoplearrList.get(i);
            if (isPostEdit != 0) {
                if (newids == null) newids = new ArrayList<>();
                newids.add(response.getId());
            }

            View v = View.inflate(activity, R.layout.single_textview_people_tag, null);
            TextView tv = (TextView) v.findViewById(R.id.nameTV);
            ImageView delete = (ImageView) v.findViewById(R.id.deleteIV);
            tv.setText(response.getName());
            v.setTag(response);
            delete.setTag(response);
            final ArrayList<People> finalTaggedpeoplearrList = taggedpeoplearrList;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    People rep = (People) view.getTag();
                    int pos = finalTaggedpeoplearrList.indexOf(rep);
                    finalTaggedpeoplearrList.remove(rep);
                    taggedpeopleFL.removeViewAt(pos);
                    taggedpeopleList.remove(pos);
                    if (isPostEdit != 0) {
                        newids.remove(pos);
                    }
                }
            });

            taggedpeopleFL.addView(v);
            taggedpeopleList.add(v);
        }
    }

    /**
     * check path is image or not
     * @param path
     * @return
     */
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    /**
     * check path is video or not
     * @param path
     * @return
     */
    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    /**
     * create video file name and its uri
     */
    private void createVideofile() {

        imageName = "";
        state = Environment.getExternalStorageState();

        imageName = Constant.PARENT_FOLDER+"_"+ String.valueOf(System.nanoTime()) + ".mp4";

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newFile = new File(Environment.getExternalStorageDirectory(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);
        } else {
            newFile = new File(getActivity().getFilesDir(), imageName);
            newProfileImageUri = Uri.fromFile(newFile);

        }
        Log.e("count",""+count++);
        Log.e("createVideofile","imageName"+imageName);

    }

    /**
     * now upload image to s3 bucket and get its url
     * @param i
     * @param postMedia
     */
    private void uploadPicToAmazon(int i, PostMedia postMedia) {
        Log.d("PROFILE_IMAGE", ""+i);
        UploadAmazonS3 uploadAmazonS3 = UploadAmazonS3.getInstance(getActivity(), Constant.COGNITO_POOL_ID);
        uploadAmazonS3.Upload_data(Constant.BUCKET_NAME, postMedia.getFile_name(), new File(postMedia.getFilePath()), new UploadAmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String sucess) {
                profile_image = Constant.AWS_URL +Constant.BUCKET_NAME +"/" +  postMedia.getFile_name();
                Log.d("PROFILE_IMAGE", ""+profile_image);
                uploadFileCount--;
                postMediaArrayList.get(i).setFile(profile_image);

                if (uploadFileCount<=0){
                    cancelProgressDialog();
                    uploadPost();
                }


            }

            @Override
            public void error(String errormsg) {

                Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
                Log.d("AMAZON_ERROR", ""+errormsg);
            }
        });
    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        int result1 = ActivityCompat.checkSelfPermission(getActivity(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

//        checkPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, CAMERA});
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }


    private static final String TAG = "PostFragment";

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
//                        showProgressDialog();
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        CommonUtil.showAlert(getActivity(), "These Permissions required for this app.Go to settings and enable permissions.", "Permissions");
                    }
                }
            }
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void imagePath(String str) {
        Glide.with(getActivity())
                .load(str)
                //  .error(R.drawable.amritanshu)
                .into(ivUploaded);
//        ivProfile.setImageBitmap(takeImageClass.StringToBitMap(str));
        file = new File(str);
    }

    public void deleteMediaFile(int position) {
        postMediaArrayList.remove(position);
        imageArraylist.remove(position);
        mediaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onS3UploadData(ArrayList<PostMedia> images) {
        for (int i = 0; i < images.size(); i++) {
            Log.e("onS3UploadData","images "+images.get(i).getFile());
        }

    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyboard(getActivity());
        return false;
    }

}

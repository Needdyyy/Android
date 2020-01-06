package com.needyyy.app.Chat.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PageRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.needyyy.app.Base.GetTimeAgo;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Chat.Messages;
import com.needyyy.app.ImageClasses.Util;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.Login.model.register.UserDataResult;
import com.needyyy.app.R;
import com.needyyy.app.manager.BaseManager.BaseManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.needyyy.app.constants.Constants.kCurrentUser;
import static com.needyyy.app.utils.Constant.TAG;
import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * Created by KSHITIZ on 3/27/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {

    private List<Messages> mMessagesList;
    private String currentdate;
    Boolean status;
    private FirebaseAuth mAuth;
    private AsyncTask mMyTask;
    DatabaseReference mDatabaseReference ;
    Context context;
    private  int SENDMESSAGETYPE=1,RECEIVEMESSAGETYPE=2 ;
    public  DeleteMessage deleteMessageCallback;
    private UserDataResult userData ;

    //-----GETTING LIST OF ALL MESSAGES FROM CHAT ACTIVITY ----
    public MessageAdapter(Context context ,List<Messages> mMessagesList,DeleteMessage deleteMessageCallback) {
        this.context = context ;
        this.deleteMessageCallback =deleteMessageCallback ;
        this.mMessagesList = mMessagesList;
        this.currentdate = null;
        userData = (BaseManager.getDataFromPreferences(kCurrentUser, UserDataResult.class));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View sendmessageview = inflater.inflate(R.layout.list_send_message, parent, false);
                viewHolder = new SendMessageViewHolder(sendmessageview);
                break;
            case 2:
                View receivemessageview = inflater.inflate(R.layout.list_recv_message, parent, false);
                viewHolder = new ReceiveMessageViewHolder(receivemessageview);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Messages chatPojo = mMessagesList.get(i);
        switch (viewHolder.getItemViewType()) {
            case 1:
                SendMessageViewHolder sendMessageViewHolder = (SendMessageViewHolder) viewHolder;
                sendMessageViewHolder.bindContent(chatPojo);
                GetTimeAgo getTimeAgo = new GetTimeAgo();

                if(chatPojo.getType().equals("text")){
                    sendMessageViewHolder.bubbleTextView.setText(chatPojo.getMessage());
                    sendMessageViewHolder.bubbleTextView.setVisibility(View.VISIBLE);
                    sendMessageViewHolder.tvMessageTime.setVisibility(View.VISIBLE);
                    String msgTime = getTimeAgo.getTimeAgo(chatPojo.getTime(),context);
                    sendMessageViewHolder.tvMessageTime.setText(msgTime);
                    sendMessageViewHolder.tvCall.setVisibility(View.GONE);
                    sendMessageViewHolder.ivImage.setVisibility(View.GONE);
                    sendMessageViewHolder.previoudate.setVisibility(View.GONE);

                    String message_date_time = Util.getFormattedDateFromTimestamp(chatPojo.getTime());
                    if(currentdate==null || !currentdate.equals(message_date_time)) {
                        currentdate = message_date_time;
                        sendMessageViewHolder.previoudate.setText(currentdate);
                        if(currentdate!=null) {
                            sendMessageViewHolder.previoudate.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else if(chatPojo.getType().equals("image")){
                    sendMessageViewHolder.bubbleTextView.setVisibility(View.GONE);
                    sendMessageViewHolder.tvCall.setVisibility(View.GONE);
                    sendMessageViewHolder.ivImage.setVisibility(View.VISIBLE);
                    sendMessageViewHolder.tvMessageTime.setVisibility(View.VISIBLE);
                    String msgTime = getTimeAgo.getTimeAgo(chatPojo.getTime(),context);
                    sendMessageViewHolder.tvMessageTime.setText(msgTime);

                    Glide.with(context).load(chatPojo.getMessage())
                            .apply(new RequestOptions()
                                    .override(230, 280)
                                    .centerCrop())
                            .thumbnail(0.4f)
                            .into(sendMessageViewHolder.ivImage);
                    sendMessageViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(context instanceof ChatActivity)
                            {
                                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View v = li.inflate(R.layout.fragment_zoom_image, null, false);
                                final Dialog reportPost = new Dialog(context);
                                reportPost.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                reportPost.setCanceledOnTouchOutside(true);
                                reportPost.setContentView(v);
                                reportPost.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                reportPost.show();

                                ImageView imageView,cross;
                                cross=v.findViewById(R.id.cross);
                                imageView=v.findViewById(R.id.imageView);
                                cross.setVisibility(View.VISIBLE);
                                Glide.with(context)
                                        .load(chatPojo.getMessage())
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                                                .placeholder(R.drawable.needyy)
                                                .error(R.drawable.needyy))
                                        .into(imageView);
                                cross.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        reportPost.dismiss();
                                    }
                                });
                            }
                        }
                    });

                }else if(chatPojo.getType().equals("call")){
                    sendMessageViewHolder.bubbleTextView.setVisibility(View.GONE);
                    sendMessageViewHolder.tvCall.setVisibility(View.VISIBLE);
                    sendMessageViewHolder.ivImage.setVisibility(View.GONE);
                    sendMessageViewHolder.tvMessageTime.setVisibility(View.GONE);

                    long lastTime = mMessagesList.get(i).getTime();
                    String lastSeen = getTimeAgo.getTimeAgo(lastTime,context);

                    if(userData.getId().equals(mMessagesList.get(i).getFrom())){
                        sendMessageViewHolder.tvCall.setText("Outgoing Call, "+lastSeen);

                    }else{
                        sendMessageViewHolder.tvCall.setText("Incoming Call, "+lastSeen);
                    }
                }


                sendMessageViewHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new DownloadFile().execute(chatPojo.getMessage());
                        return false;
                    }
                });


                sendMessageViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        CharSequence options[] = new CharSequence[]{ "Delete","Cancel","Copy" };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete this message");
                        builder.setItems(options,new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which == 0){
                                /*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 */
                                    deleteMessageCallback.deleteMsg(mMessagesList.get(i).getKey());
                                    mMessagesList.remove(i);
                                     notifyDataSetChanged();
                                }
                                if(which == 1){
                                }
                                if(which == 2){
                                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(mMessagesList.get(i).getMessage());
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                        return true;
                    }
                });
                break;
            case 2:
                ReceiveMessageViewHolder receiveMessageViewHolder = (ReceiveMessageViewHolder) viewHolder;
                receiveMessageViewHolder.bindContent(chatPojo);

                GetTimeAgo getTimeAgo1 = new GetTimeAgo();

                if(chatPojo.getType().equals("text")){
                    receiveMessageViewHolder.bubbleTextView.setText(chatPojo.getMessage());
                    receiveMessageViewHolder.bubbleTextView.setVisibility(View.VISIBLE);
                    receiveMessageViewHolder.tvCall.setVisibility(View.GONE);
                    receiveMessageViewHolder.ivImage.setVisibility(View.GONE);
                    receiveMessageViewHolder.tvMessageTime.setVisibility(View.VISIBLE);
                    String msgTime = getTimeAgo1.getTimeAgo(chatPojo.getTime(),context);
                    receiveMessageViewHolder.tvMessageTime.setText(msgTime);

                }else if(chatPojo.getType().equals("image")){
                    receiveMessageViewHolder.bubbleTextView.setVisibility(View.GONE);
                    receiveMessageViewHolder.tvCall.setVisibility(View.GONE);
                    receiveMessageViewHolder.ivImage.setVisibility(View.VISIBLE);
                    receiveMessageViewHolder.tvMessageTime.setVisibility(View.VISIBLE);
                    String msgTime = getTimeAgo1.getTimeAgo(chatPojo.getTime(),context);
                    receiveMessageViewHolder.tvMessageTime.setText(msgTime);

                    Glide.with(context).load(chatPojo.getMessage())
                            .apply(new RequestOptions()
                                    .override(230, 280)
                                    .centerCrop())
                            .thumbnail(0.4f)
                            .into(receiveMessageViewHolder.ivImage);


                    receiveMessageViewHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            new DownloadFile().execute(chatPojo.getMessage());
                            return false;
                        }
                    });


                    receiveMessageViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                             if(context instanceof ChatActivity)
                                {

                                  //  new DownloadFile().execute(chatPojo.getMessage());

                                    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View v = li.inflate(R.layout.fragment_zoom_image, null, false);
                                    final Dialog reportPost = new Dialog(context);
                                reportPost.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                reportPost.setCanceledOnTouchOutside(true);
                                reportPost.setContentView(v);
                                reportPost.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                reportPost.show();

                                ImageView imageView,cross;
                                cross=v.findViewById(R.id.cross);
                                imageView=v.findViewById(R.id.imageView);
                                cross.setVisibility(View.VISIBLE);

                                Glide.with(context)
                                        .load(chatPojo.getMessage())
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                                                .placeholder(R.drawable.needyy)
                                                .error(R.drawable.needyy))
                                        .into(imageView);
                                cross.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        reportPost.dismiss();
                                    }
                                });
                            }
                        }
                    });


                }else if(chatPojo.getType().equals("call")){
                    receiveMessageViewHolder.bubbleTextView.setVisibility(View.GONE);
                    receiveMessageViewHolder.tvCall.setVisibility(View.VISIBLE);
                    receiveMessageViewHolder.ivImage.setVisibility(View.GONE);
                    receiveMessageViewHolder.tvMessageTime.setVisibility(View.GONE);

                    long lastTime = mMessagesList.get(i).getTime();
                    String lastSeen = getTimeAgo1.getTimeAgo(lastTime,context);

                    if(userData.getId().equals(mMessagesList.get(i).getFrom())){
                        receiveMessageViewHolder.tvCall.setText("Outgoing Call, "+lastSeen);

                    }else{
                        receiveMessageViewHolder.tvCall.setText("Incoming Call, "+lastSeen);
                    }
                }

                receiveMessageViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        CharSequence options[] = new CharSequence[]{ "Delete","Cancel","Copy" };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete this message");
                        builder.setItems(options,new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which == 0){
                                /*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 */
                                    deleteMessageCallback.deleteMsg(mMessagesList.get(i).getKey());
                                    mMessagesList.remove(i);
                                     notifyDataSetChanged();
                                }
                                if(which == 1){

                                }
                                if(which == 2){
                                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(mMessagesList.get(i).getMessage());
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();

                        return true;
                    }
                });
                break;
        }

    }


    //---NO OF ITEMS TO BE ADDED----
    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (userData.getId().equals(mMessagesList.get(position).getFrom()))
            return SENDMESSAGETYPE;
        else
            return RECEIVEMESSAGETYPE;
    }

    public class SendMessageViewHolder extends RecyclerView.ViewHolder {

        TextView bubbleTextView ;
        Messages chatPojo ;
        TextView tvCall,tvmessage, tvMessageTime;
        ImageView ivImage;
        private RelativeLayout itemview;
        private TextView previoudate;

        public SendMessageViewHolder(View itemView) {
            super(itemView);
            previoudate        =itemView.findViewById(R.id.olddate);
            itemview       = itemView.findViewById(R.id.item_view);
            bubbleTextView = itemView.findViewById(R.id.tvMessage);
            tvCall         = itemView.findViewById(R.id.tvCall);
            ivImage        = itemView.findViewById(R.id.ivImage);
            tvmessage      = itemView.findViewById(R.id.tv_chattext);
            tvMessageTime  = itemView.findViewById(R.id.tvMessageTime);
        }

        public void bindContent(Messages chatPojo) {
            this.chatPojo = chatPojo ;
        }
    }

    public class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {

        TextView bubbleTextView ;
        Messages chatPojo ;
        TextView tvCall,tvmessage, tvMessageTime;
        ImageView ivImage;
        private RelativeLayout itemview;
        public ReceiveMessageViewHolder(View itemView) {
            super(itemView);
            itemview       = itemView.findViewById(R.id.item_view);
            bubbleTextView = itemView.findViewById(R.id.tvMessage);
            tvCall = itemView.findViewById(R.id.tvCall);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvmessage      = itemView.findViewById(R.id.tv_chattext);
            tvMessageTime      = itemView.findViewById(R.id.tvMessageTime);

        }

        public void bindContent(Messages chatPojo) {
            this.chatPojo = chatPojo ;
        }
    }
    public  interface  DeleteMessage{
        public void deleteMsg(String key);
    }

    // save image


    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
           // mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            } finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
          //  mProgressDialog.dismiss();

            if(result!=null){
                // Display the downloaded image into ImageView
             //   mImageView.setImageBitmap(result);

                // Save bitmap to internal storage
                Uri imageInternalUri = saveImageToInternalStorage(result);
                // Set the ImageView image from internal storage
             //   mImageViewInternal.setImageURI(imageInternalUri);
            }else {
                // Notify user that an error occurred while downloading image
              //  Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(context);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "check12"+".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }
// save image

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
              //  folder = Environment.getExternalStorageDirectory() + "Needyyy";

                //Create androiddeft folder if it does not exist

                String folder = "Needyyy";
//                File f = new File(Environment.getExternalStorageDirectory(), folder);
//                if (!f.exists()) {
//                    f.mkdirs();
//                }


                File directory = new File(Environment.getExternalStorageDirectory(), folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(directory + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + directory + fileName;

            } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context,
                    message, Toast.LENGTH_LONG).show();
        }
    }

}

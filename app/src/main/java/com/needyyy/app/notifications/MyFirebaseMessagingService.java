package com.needyyy.app.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import static com.needyyy.app.utils.Constant.PUSH_TYPE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    String notificationImage="";
    private String CHANNEL_ID="Needyyy";
    private String _tocken = "";
    private SharedPreferences sharedPref;
    String id;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        AppController.getManager().setTocken(s);
    }

  @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("kk",remoteMessage.toString());
        if (remoteMessage == null)
            return;

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getNotification().getBody().toString());
//                Log.i("JSONDATA: ", json.toString());
//                if(AppController.getManager().getId()!=null||!AppController.getManager().getId().equals(""))
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().get("body").toString());
                  handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message,String pushtype) {
        // app is in foreground, broadcast the push message
        Log.i("Notification Mag: ", message);
        Intent notificationIntent = new Intent("android.intent.action.MAIN");
        notificationIntent.putExtra(PUSH_TYPE, pushtype);
        this.sendBroadcast(notificationIntent);
        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
    }

    private void handleDataMessage(JSONObject json) throws JSONException {
//1-normal from Users list(Admin Panel)
//2-logout
//3-user name change(pending)
//4-login(N)(pending)
//5-birthday (pending)
//
//20-global post approvel(admin_panel)(Y)(pending)
//21-share post(pending)
//22-On Like Post
//23-On Tagging in post to TaGGED User
//24-Comment On Post
//
//25-On Like Page
//26-on approve page(admin_panel)
//27-invite page(pending due to CR)
//28-follow page
//29-promote page
//
//40-wallet add payment
//
//50-On Receive Request
//51-request accept

        Intent intent = null;
        String push_type, id, scheduleType, message;

        if (json != null) {
            message = json.optString(Constant.MESSAGES);
            push_type = json.optString(PUSH_TYPE);

            if(json.has(Constant.IMAGE))
                notificationImage = json.optString(Constant.IMAGE);
            else
                notificationImage= "";

//            id = json.optString(Constant.ID);
//            scheduleType = json.optString(Constant.SCHEDULE_TYPE);
//            System.out.print("MSG: " + json + " " + message + " " + " " + push_type + " " + id + " " + scheduleType);
            handleNotification(message,push_type);
            intent = new Intent(this, HomeActivity.class);
            if (push_type.equals("1"))
            {
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if (push_type.equals("40"))
            {
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("15"))
            {
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("2")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("3")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("4")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("5")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("20")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("21")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("22")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                 id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("23")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("24")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("25")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("26")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("27")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("28")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("29")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("40")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("50")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }else if(push_type.equals("51")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("4")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("27")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("28")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("29")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("35")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("36")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("60")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else if(push_type.equals("61")){
                intent.putExtra(PUSH_TYPE, push_type);
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            else{
                intent.putExtra(PUSH_TYPE, "");
                Log.i("JSONDATA: ", json.toString());
                id=json.getJSONObject("json").getString("task_id");
                intent.putExtra("taskid",id);
            }
            showNotification(message, getString(R.string.app_name), intent);
        }
    }

    private void showNotification(String pushMessage, String pushTitle, Intent intent) {

        Bitmap notiImage = null;
        if(!TextUtils.isEmpty(notificationImage)) {
            try {
                URL url = new URL(notificationImage);
                notiImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 120, 120, false))
                .setContentTitle(pushTitle)
                .setContentText((Html.fromHtml(pushMessage)))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setContentIntent(pendingIntent);
        if(!TextUtils.isEmpty(notificationImage)&& notiImage!=null && !notiImage.equals(""))
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(notiImage).setSummaryText(pushMessage));


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(notificationManager);

        Random random = new Random();
        int notificationId = random.nextInt(10000);
        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }


    private void createNotificationChannel( NotificationManager notificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String description = "New Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}


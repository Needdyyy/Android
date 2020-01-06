package com.needyyy.app.manager.BaseManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.needyyy.AppController;
import com.needyyy.app.Base.BaseApplication;
import com.needyyy.app.constants.Constants;
import com.paytm.pgsdk.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import static com.needyyy.app.constants.Constants.kAppPreferences;
import static com.needyyy.app.constants.Constants.knotification;


public class BaseManager {
    private static String filepath;
    private static int notificationcountt;
    private static final String TAG = BaseManager.class.getSimpleName();
    /********************************Statuc Methods for SharedPreferences**********************************/
    /**
     * stores dataObject to SharedPreferences and commit the user default. This must be called in main thread
     */
    public synchronized static void saveDataIntoPreferences(Object dataObject, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(dataObject);

        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public synchronized static void savePostData(Object dataObject, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(dataObject);

        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }



    public synchronized static void saveMasterHitData(Object dataObject, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(dataObject);

        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }


    public synchronized static <T> T getMasterHitData(String key, Type typeOfT) {
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        T dataObject = null;
        try {
            Gson gson = new Gson();
            String jsonString = sharedPreferences.getString(key, Constants.kEmptyString);
            dataObject = gson.fromJson(jsonString, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataObject;
    }

    public synchronized static <T> T getPostData(String key, Type typeOfT) {
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        T dataObject = null;
        try {
            Gson gson = new Gson();
            String jsonString = sharedPreferences.getString(key, Constants.kEmptyString);
            dataObject = gson.fromJson(jsonString, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataObject;
    }

    /**
     * Return the dataObject of type typeOfT class if successfull. if fail to get data from SharedPreferences then returns null
     */
    public synchronized static <T> T getDataFromPreferences(String key, Type typeOfT) {
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        T dataObject = null;
        try {
            Gson gson = new Gson();
            String jsonString = sharedPreferences.getString(key, Constants.kEmptyString);
            dataObject = gson.fromJson(jsonString, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataObject;
    }

    public synchronized static  void  clearPreference(){
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }
    /********************************Static Methods for SharedPreferences**********************************/


    public static List<ApplicationInfo> getAppInfo() {
        PackageManager packageManager = BaseApplication.getContext().getPackageManager();
        List<ApplicationInfo> info = packageManager.getInstalledApplications(0);
        AppController.getContext().getSharedPreferences(kAppPreferences, Context.MODE_PRIVATE).edit().clear();
        return info;
    }

    /**
     * Get the app name if available under android:label tag in Manifest file. if not specified then return kDefaultAppName
     */
    public static String getAppName() {
        String appName = Constants.kDefaultAppName;

        try {
            ApplicationInfo applicationInfo = BaseApplication.getContext().getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            appName = (stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : BaseApplication.getContext().getString(stringId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return appName;
        }
    }

//    public static void savenotificationcount(int value)
//    {
//        if(value==0)
//        {
//            notificationcountt=0;
//        }
//        else
//        {
//    //        notificationcountt=getNotificationcount();
//            notificationcountt=notificationcountt+value;
//        }
//        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(knotification, Context.MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//        prefsEditor.putString("Notification", Integer.toString(notificationcountt));
//        prefsEditor.commit();
//
//    }

//    public static int getNotificationcount()
//    {
//        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(knotification, Context.MODE_PRIVATE);
//        String jsonString = sharedPreferences.getString("Notification", Constants.kEmptyString);
//        if(jsonString.equals("")||jsonString==null)
//        {
//            jsonString="0";
//        }
//        else {
//            notificationcountt = Integer.parseInt(jsonString);
//        }
//        return notificationcountt;
//    }

    public static void savenotificationstatus(Boolean status)
    {
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(knotification, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean("Notificationstatus", status);
        prefsEditor.commit();
    }

    public static Boolean getnotificationstatus()
    {
        SharedPreferences sharedPreferences = AppController.getContext().getSharedPreferences(knotification, Context.MODE_PRIVATE);
        Boolean jsonString = sharedPreferences.getBoolean("Notificationstatus", false);
        return jsonString;
    }


    public static String SaveImage(String urll)
    {

        try
        {
            URL url = new URL(urll);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            String filename="downloadedFile.png";
            Log.i("Local filename:",""+filename);
            File file = new File(SDCardRoot,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if(downloadedSize==totalSize) filepath=file.getPath();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            filepath=null;
            e.printStackTrace();
        }
//        Log.i("filepath:"," "+filepath) ;
       return filepath;

    }


}

package com.needyyy.app.manager.APIManager;

/**
 * Created by surya on 26/10/2017.
 * ReachabilityManager class provide clear interface to check the network reachability and also notify the reachability changes.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.needyyy.app.Base.BaseApplication;


public class ReachabilityManager extends BroadcastReceiver {
    /*******Netwrok Type Constant*******/
    public static final int TypeUnKnown             = -1;
    public static final int TypeNotConnected        = 0;
    public static final int Type2G                  = 1;
    public static final int Type3G                  = 2;
    public static final int Type4G                  = 3;
    public static final int TypeWiFi                = 4;
    /*******Netwrok Type Constant*******/


    private static ReachabilityManager         _ReachabilityManager;
    public static ConnectivityReceiverListener _ConnectivityReceiverListener;

    /* A private Constructor prevents any other class from instantiating.*/
    public ReachabilityManager() {
        super();
        IntentFilter filter = new IntentFilter();
        filter.addAction("CONNECTIVITY_CHANGE");
    }

    public static synchronized ReachabilityManager manager ()  {
        if(_ReachabilityManager == null)   {
            _ReachabilityManager = new ReachabilityManager();
        }
        return _ReachabilityManager;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
       // Toaster.getRangeenToast("Internet is "+isConnected);

        if (_ConnectivityReceiverListener != null) {
            _ConnectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    /**
     * Get the network info
     */
    private static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity to a Wifi network
     */
    private static boolean isConnectedWifi(Context context){
        NetworkInfo info = ReachabilityManager.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     */
    private static boolean isConnectedMobile(Context context){
        NetworkInfo info = ReachabilityManager.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnectedFast(Context context){
        NetworkInfo info = ReachabilityManager.getNetworkInfo(context);
        return (info != null && info.isConnected() && ReachabilityManager.isConnectionFast(info.getType(),info.getSubtype()));
    }

    private static boolean isConnectionFast(int type, int subtype) {

        if(type== ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type== ConnectivityManager.TYPE_MOBILE){
            switch(subtype) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    private int getNetworkType(Context context){
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = ReachabilityManager.getNetworkInfo(context);

        if(info==null || !info.isConnected())
            return TypeNotConnected; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return TypeWiFi;
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return Type2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return Type3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return Type4G;
                default:
                    return TypeUnKnown;
            }
        }
        return TypeUnKnown;
    }

    /**Get current network status*/
    public static boolean getNetworkStatus() {
        Context context = BaseApplication.getContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = ReachabilityManager.getNetworkInfo(context);
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}

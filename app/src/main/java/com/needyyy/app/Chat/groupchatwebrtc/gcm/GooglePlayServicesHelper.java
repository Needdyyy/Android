package  com.needyyy.app.Chat.groupchatwebrtc.gcm;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.needyyy.AppController;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;

public class GooglePlayServicesHelper {
    private static final String TAG = GooglePlayServicesHelper.class.getSimpleName();

    private static final int PLAY_SERVICES_REQUEST_CODE = 9000;

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     *
     * @param activity activity where you check Google Play Services availability
     */
    public boolean checkPlayServicesAvailable(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_REQUEST_CODE)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public boolean checkPlayServicesAvailable() {
        return getPlayServicesAvailabilityResultCode() == ConnectionResult.SUCCESS;
    }

    private int getPlayServicesAvailabilityResultCode() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        return apiAvailability.isGooglePlayServicesAvailable(AppController.getInstance());
    }
}
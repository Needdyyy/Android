package  com.needyyy.app.Chat.groupchatwebrtc.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.needyyy.AppController;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;


public class ResourceUtils {

    public static String getString(@StringRes int stringId) {
        return AppController.getInstance().getString(stringId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return AppController.getInstance().getResources().getDrawable(drawableId);
    }

    public static int getColor(@ColorRes int colorId) {
        return AppController.getInstance().getResources().getColor(colorId);
    }

    public static int getDimen(@DimenRes int dimenId) {
        return (int) AppController.getInstance().getResources().getDimension(dimenId);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}

package  com.needyyy.app.Chat.groupchatwebrtc.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.needyyy.AppController;
import com.needyyy.app.Chat.groupchatwebrtc.view.App;


public class KeyboardUtils {

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}

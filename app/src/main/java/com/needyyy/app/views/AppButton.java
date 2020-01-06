package com.needyyy.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by surya on 11/1/18.
 */

public class AppButton extends Button {

    public AppButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        init();
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public AppButton(Context context) {
        super(context);
//        init();
    }

//    private void init() {
//        try {
//            Typeface myFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
//
//            setTypeface(myFont);
//        } catch (Exception e) {
//            Log.e("Tag", e.toString());
//        }
//    }

}
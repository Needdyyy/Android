package com.needyyy.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by surya on 8/8/17.
 */

public class AppEditText extends android.support.v7.widget.AppCompatEditText {

    public AppEditText(Context context) {
        super(context);
        init(context);
    }


    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(Context context) {
        try {
//            Typeface myFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
//
//            setTypeface(myFont);
        } catch (Exception e) {
            Log.e("Tag", e.toString());
        }
    }
}
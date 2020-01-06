package com.needyyy.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by fourthscreen on 4/13/2016.
 */
public class AppTextView extends TextView {
    public AppTextView(Context context) {
        super(context);
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        handleStyleable(context, attrs);
    }

    public AppTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        handleStyleable(context, attrs);
    }

//    private void handleStyleable(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.fontTxt);
//        FONT_VAL font_val = FONT_VAL.NONE;
//        try {
//            for (FONT_VAL mode : FONT_VAL.values()) {
//                if (ta.getInt(R.styleable.fontTxt_txt_font, 0) == mode.getId()) {
//                    font_val = mode;
//                    break;
//                }
//            }
//            if (font_val == FONT_VAL.REGULAR) {
//                setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato_Regular.ttf"));
//            } else if (font_val == FONT_VAL.BOLD) {
//                setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato_Bold.ttf"));
//            } else if (font_val == FONT_VAL.LIGHT) {
//                setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato_Light.ttf"));
//            }else if (font_val == FONT_VAL.HEAVY) {
//                setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato_Heavy.ttf"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public enum FONT_VAL {
        REGULAR(0), BOLD(1), LIGHT(2),HEAVY(3), NONE(4);
        private final int ID;

        FONT_VAL(final int id) {
            this.ID = id;
        }

        public int getId() {
            return ID;
        }
    }
}

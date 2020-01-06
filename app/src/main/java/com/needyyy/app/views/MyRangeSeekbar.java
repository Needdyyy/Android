package com.needyyy.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.needyyy.app.R;

/**
 * Created by root on 13/6/17.
 */

public class MyRangeSeekbar extends CrystalRangeSeekbar {
    public MyRangeSeekbar(Context context) {
        super(context);
    }

    public MyRangeSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRangeSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float getCornerRadius(TypedArray typedArray) {
        return super.getCornerRadius(typedArray);
    }

    @Override
    protected float getMinValue(TypedArray typedArray) {
        return 0f;
    }

    @Override
    protected float getMaxValue(TypedArray typedArray) {
        return 90f;
    }

    @Override
    protected float getMinStartValue(TypedArray typedArray) {
        return 0f;
    }

    @Override
    protected float getMaxStartValue(TypedArray typedArray) {
        return 90f;
    }

    @Override
    protected float getSteps(TypedArray typedArray) {
        return super.getSteps(typedArray);
    }

    @Override
    protected float getGap(TypedArray typedArray) {
        return super.getGap(typedArray);
    }

    @Override
    protected float getFixedGap(TypedArray typedArray) {
        return super.getFixedGap(typedArray);
    }

    @Override
    protected int getBarColor(TypedArray typedArray) {
        return Color.parseColor("#E1E1E1");
    }

    @Override
    protected int getBarHighlightColor(TypedArray typedArray){
        return Color.parseColor("#0077B4"); }

    @Override
    protected int getLeftThumbColor(TypedArray typedArray) {
        return Color.parseColor("#058EB7");
    }

    @Override
    protected int getRightThumbColor(TypedArray typedArray) {
        return Color.parseColor("#058EB7");
    }

    @Override
    protected int getLeftThumbColorPressed(TypedArray typedArray) {
        return Color.parseColor("#0077B4");
    }

    @Override
    protected int getRightThumbColorPressed(TypedArray typedArray) {
        return Color.parseColor("#0077B4");
    }

    @Override
    protected Drawable getLeftDrawable(TypedArray typedArray) {
        return ContextCompat.getDrawable(getContext(), R.drawable.redio_button_active);
    }

    @Override
    protected Drawable getRightDrawable(TypedArray typedArray) {
            return ContextCompat.getDrawable(getContext(), R.drawable.redio_button_active);
    }

    @Override
    protected Drawable getLeftDrawablePressed(TypedArray typedArray) {
        return ContextCompat.getDrawable(getContext(), R.drawable.redio_button_active);
    }

    @Override
    protected Drawable getRightDrawablePressed(TypedArray typedArray) {
        return ContextCompat.getDrawable(getContext(), R.drawable.redio_button_active);
    }

    @Override
    protected int getDataType(TypedArray typedArray) {
        return super.getDataType(typedArray);
    }

}

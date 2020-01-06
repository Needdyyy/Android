package com.needyyy.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.needyyy.app.R;

public class Progress  extends Dialog {

    ImageView progressIV;

    public Progress(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        setContentView(R.layout.progress_layout);

        progressIV = findViewById(R.id.progressIV);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(progressIV);
//        Glide.with(context).load(R.raw.arabic).into(imageViewTarget);
        super.setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

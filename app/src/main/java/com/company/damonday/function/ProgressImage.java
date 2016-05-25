package com.company.damonday.function;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.company.damonday.R;

/**
 * Created by lamtaklung on 22/5/2016.
 */
public class ProgressImage{

    private Context mContext;
    private boolean mShowing = false;
    Dialog dialog;

    public ProgressImage(Context context) {
        mContext = context;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.progress_bar);
        ImageView image = (ImageView) dialog.findViewById(R.id.progress_image);
        Glide.with(mContext)
                .load(R.drawable.mascot_loading)
                .asGif()
                .crossFade()
                .into(image);
    }


    public void show() {
        mShowing = true;
        dialog.show();
    }

    public void dismiss() {
        mShowing = false;
        dialog.dismiss();
    }

    public boolean isShowing() {
        return mShowing;
    }

}

package com.gieasesales.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gieasesales.R;

public class OfferAlertDialog {
    private Activity mActivity;
    ProgressBar progressDialog;

    public OfferAlertDialog(Activity a) {
        this.mActivity = a;

    }

    public void showimg(String imageurl) {
        Util.Logcat.e("imageurl :" + imageurl);
        //https://medium.com/@mr.johnnyne/how-to-use-glide-v4-load-image-with-progress-update-eb02671dac18
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.offer_alert);
        // set the custom dialog components - text, image and button
        progressDialog = new ProgressBar(mActivity);
        // progressDialog.setProgress(0);
        ImageView image = dialog.findViewById(R.id.images);

        Glide.with(mActivity)
                .load(imageurl).placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(200, 200)
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        // image.setImageDrawable(mActivity.getDrawable(R.drawable.no_product));
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(image);
        // Glide.with(mActivity).load(imageurl).into(image);
        ImageView dialogButton = dialog.findViewById(R.id.cancel);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}

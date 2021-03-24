package com.gieasesales.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gieasesales.R;


public class CommonAlertDialog {
    private Activity mActivity;

    public CommonAlertDialog(Activity a) {
        this.mActivity = a;
    }

    public void build(String title, boolean value) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.cutom_alert);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.texts);
        LinearLayout success = dialog.findViewById(R.id.success);
        LinearLayout failure = dialog.findViewById(R.id.failure);
        text.setText(title);
        Util.Logcat.e("STATUS>" + value);

        if (value == true) {
            success.setVisibility(View.VISIBLE);
        } else {
            failure.setVisibility(View.VISIBLE);
        }
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
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

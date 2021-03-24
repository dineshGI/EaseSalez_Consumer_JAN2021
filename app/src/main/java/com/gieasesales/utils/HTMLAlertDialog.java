package com.gieasesales.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.gieasesales.R;


public class HTMLAlertDialog {
    private Activity mActivity;

    public HTMLAlertDialog(Activity a) {
        this.mActivity = a;
    }


    public void build(String title) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.cutom_alert);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.texts);
        text.setText(HtmlCompat.fromHtml(title, 0));
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

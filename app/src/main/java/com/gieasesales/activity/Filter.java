package com.gieasesales.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.R;

import java.util.ArrayList;
import java.util.List;

public class Filter extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog();
            }
        });

    }

    private void CustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cutom_filter_alert);
        // set the custom dialog components - text, image and button
        final ListView mainlist = dialog.findViewById(R.id.mainlist);
        final ListView sublist = dialog.findViewById(R.id.sublist);
        final ArrayList<String> listone = new ArrayList<String>();
        listone.add("Sort");
        listone.add("Shops");

        final ArrayList<String> one = new ArrayList<String>();
        //  final ArrayList<String> two = new ArrayList<String>();
        //  final ArrayList<String> three = new ArrayList<String>();

        mainlist.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listone));

        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sublist.setVisibility(View.VISIBLE);

                if (position == 0) {
                    one.clear();
                    one.add("Rating");
                    one.add("Shops");
                    one.add("Distance");
                    one.add("Door Delivery");
                }
                if (position == 1) {
                    one.clear();
                    one.add("All Shop");
                    one.add("Engaged Shop");
                }

                sublist.setAdapter(new ArrayAdapter<String>(Filter.this,
                        android.R.layout.simple_list_item_1, one));
                for (int i = 0; i < mainlist.getChildCount(); i++) {
                    if (position == i) {
                        mainlist.getChildAt(i).setBackgroundColor(Color.WHITE);
                    } else {
                        mainlist.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });

        sublist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // sublist.getChildAt(position).getTooltipText();

                if (position == 0) {
                    one.get(position);
                    Toast.makeText(Filter.this, "You have selected " + one.get(position), Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    one.get(position);
                    Toast.makeText(Filter.this, "You have selected " + one.get(position), Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
}

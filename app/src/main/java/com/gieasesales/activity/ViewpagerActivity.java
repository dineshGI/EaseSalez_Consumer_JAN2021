package com.gieasesales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.gieasesales.R;
import com.gieasesales.adapter.Pager;
import com.gieasesales.utils.Util;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ViewpagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    Pager pager;
    ArrayList<String> imageModelsArray = new ArrayList<>();
    Button prev, next;
    int Count = 0;
    DotsIndicator pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity);
        viewPager = findViewById(R.id.pager);

        pageIndicatorView = findViewById(R.id.dots_indicator);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        String array = Util.getData("sliderdata", getApplicationContext());

        try {

            JSONArray jsonarray = new JSONArray(array);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject imageobject = jsonarray.getJSONObject(i);
                imageModelsArray.add(imageobject.getString("AdvImageURL"));
                Util.Logcat.e("AdvImageURL :" + imageobject.getString("AdvImageURL"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        pager = new Pager(this, imageModelsArray);
        viewPager.setAdapter(pager);
        pageIndicatorView.setViewPager(viewPager);

        // AutoSwipeBanner();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Count--;
                viewPager.setCurrentItem(getItemofviewpager(-1), true);*/

                try {
                    if (Util.getData("MarathonFlag", getApplicationContext()).equalsIgnoreCase("0")&&Util.registration(Util.getData("MarthonEndDate", getApplicationContext()))==true) {
                        Intent event = new Intent(ViewpagerActivity.this, MarathonRegister.class);
                        startActivity(event);
                        finish();
                    } else {
                        Intent home2 = new Intent(ViewpagerActivity.this, MainActivity.class);
                        startActivity(home2);
                        finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /*Intent home2 = new Intent(ViewpagerActivity.this, MainActivity.class);
                startActivity(home2);
                finish();*/
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItemofviewpager(+1), true);
              /*  Count++;
                Util.Logcat.e("INDEX :" + viewPager.getAdapter().getCount());
                Util.Logcat.e("Count :" + Count);
                if (Count == imageModelsArray.size()) {
                    Util.Logcat.e("FINISH :" + Count);
                    Intent home2 = new Intent(ViewpagerActivity.this, MainActivity.class);
                    startActivity(home2);
                    finish();
                }*/
            }
        });
    }

    private int getItemofviewpager(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public void AutoSwipeBanner() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int currentPage = viewPager.getCurrentItem();
                if (currentPage == imageModelsArray.size() - 1) {
                    currentPage = -1;
                }
                viewPager.setCurrentItem(currentPage + 1, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);

    }
}

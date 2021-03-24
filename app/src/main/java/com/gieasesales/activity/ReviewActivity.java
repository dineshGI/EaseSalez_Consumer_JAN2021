package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gieasesales.utils.Util.REVIEW;
import static com.gieasesales.utils.Util.getData;

public class ReviewActivity extends AppCompatActivity {

    CommonAlertDialog alert;
    String POSId, POSName, IndentId, POSAddress;
    EditText EdComments;
    Button BtnSubmit;
    RatingBar BtnRating;
    ImageView back_arrow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        POSAddress = getIntent().getStringExtra("POSAddress");
        POSName = getIntent().getStringExtra("POSName");
        toolbar.setTitle(POSName);
        toolbar.setSubtitle(POSAddress);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);
        POSId = getIntent().getStringExtra("POSId");

        IndentId = getIntent().getStringExtra("IndentId");
        EdComments = findViewById(R.id.comments);
        BtnSubmit = findViewById(R.id.btn_update);
        BtnRating = findViewById(R.id.rating);
        back_arrow = findViewById(R.id.back_arrow);

        BtnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateReview();
            }
        });

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateReview();
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void UpdateReview() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("IndentId", IndentId);
            obj.put("Rating", BtnRating.getRating());
            obj.put("Feedback", EdComments.getEditableText().toString());
            Util.Logcat.e("REVIEW:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), REVIEW, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Util.Logcat.e("onResponse" + String.valueOf(response));
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            alert.build(resobject.getString("StatusDesc"), true);
                            finish();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

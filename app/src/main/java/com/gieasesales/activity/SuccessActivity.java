package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

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

import static com.gieasesales.utils.Util.CHANGE_PASSWORD;

public class SuccessActivity extends AppCompatActivity {
    CommonAlertDialog alert;
    TextView values;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order Details");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        alert = new CommonAlertDialog(this);
        values=findViewById(R.id.values);


        String sadsa=getIntent().getStringExtra("obj");
        values.setText(sadsa);
    }

    private void Changepass() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("LoginId", "");
            Util.Logcat.e("Offers:::"+ obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), CHANGE_PASSWORD, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(SuccessActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(SuccessActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError"+ message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Util.Logcat.e("onResponse"+ String.valueOf(response));
                    try {
                        Util.Logcat.e("OUTPUT:::"+ Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            alert.build(resobject.getString("StatusDesc"),true);
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"),false);
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
}

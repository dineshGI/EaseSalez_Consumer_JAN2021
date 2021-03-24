package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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

import static com.gieasesales.utils.Util.ADD_ADDRESS;
import static com.gieasesales.utils.Util.getData;

public class AddAddress extends AppCompatActivity {

    CommonAlertDialog alert;
    EditText c_name, Address1, Address2, landmark, p_city, p_pincode, p_mobile;
    Button p_save;
    CheckBox mark_default;
    ImageView back_arrow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Address");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Util.UPDATECHARGES=true;
        alert = new CommonAlertDialog(this);
        c_name = findViewById(R.id.c_name);
        Address1 = findViewById(R.id.Address1);
        Address2 = findViewById(R.id.Address2);
        landmark = findViewById(R.id.landmark);
        p_city = findViewById(R.id.p_city);
        p_pincode = findViewById(R.id.p_pincode);
        p_mobile = findViewById(R.id.p_mobile);
        p_save = findViewById(R.id.p_save);
        mark_default = findViewById(R.id.mark_default);
        back_arrow = findViewById(R.id.back_arrow);

        p_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c_name.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Name", false);
                } else if (p_mobile.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Mobile No", false);
                } else if (Address1.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Address 1", false);
                } else if (p_city.getEditableText().toString().isEmpty()) {
                    alert.build("Enter City", false);
                } else if (p_pincode.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Pincode", false);
                } else {
                    AddAddresscall();
                }
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AddAddresscall() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerAddressId", "0");
            if (mark_default.isChecked()) {
                obj.put("DefaultAddress", "1");
            } else {
                obj.put("DefaultAddress", "0");
            }

            obj.put("ContactName", c_name.getEditableText().toString());
            obj.put("ContactNumber", p_mobile.getEditableText().toString());
            obj.put("Address1", Address1.getEditableText().toString());
            obj.put("Address2", Address2.getEditableText().toString());
            obj.put("City", p_city.getEditableText().toString());
            obj.put("Pincode", p_pincode.getEditableText().toString());
            obj.put("LandMark", landmark.getEditableText().toString());
            Util.Logcat.e("ADD ADDRESS:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), ADD_ADDRESS, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {

                        alert.build(getString(R.string.timeout_error), false);

                    } else {

                        alert.build(getString(R.string.server_error), false);

                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //alert.build(resobject.getString("StatusDesc"));
                            finish();
                        } else {
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
//android:text
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

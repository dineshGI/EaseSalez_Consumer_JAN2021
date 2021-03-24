package com.gieasesales.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.gieasesales.utils.Util.FORGET_PASSWORD;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button BtnLogin;
    TextView TxtForgetPassword, new_user, app_version;
    CommonAlertDialog alert;
    EditText EdUsername, EdPassword;
    CheckBox chkbox;
    String device, osName, latitude, longitude;
    ProgressDialog loading;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Util.SHOWALERT = true;
        // getSupportActionBar().hide();

        if ((ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }

        devicedetails();
        loading = new ProgressDialog(this);
        BtnLogin = findViewById(R.id.btn_login);
        TxtForgetPassword = findViewById(R.id.forget_pin);
        new_user = findViewById(R.id.new_user);
        EdUsername = findViewById(R.id.mobile_no);
        EdPassword = findViewById(R.id.pin);
        chkbox = findViewById(R.id.rememberme);
        chkbox.setChecked(true);
        app_version = findViewById(R.id.app_version);
        app_version.setText(Util.app_version_name);
        alert = new CommonAlertDialog(this);
        BtnLogin.setOnClickListener(this);
        new_user.setOnClickListener(this);
        TxtForgetPassword.setOnClickListener(this);

        if (!Util.getData("loginuser", getApplicationContext()).isEmpty()) {
            //   Log.e("zcczxc", Util.getData("loginuser", getApplicationContext()));
            EdUsername.setText(Util.getData("loginuser", getApplicationContext()));
            EdPassword.setText(Util.getData("loginpass", getApplicationContext()));
        }
        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (!Util.getData("loginuser", getApplicationContext()).isEmpty()) {
                        EdUsername.setText(Util.getData("loginuser", getApplicationContext()));
                        EdPassword.setText(Util.getData("loginpass", getApplicationContext()));
                    }

                } else {
                    EdUsername.setText("");
                    EdPassword.setText("");
                }

            }
        });

    }

    private void devicedetails() {

        String device_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        try {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            osName = "Android " + fields[Build.VERSION.SDK_INT + 1].getName();
            Util.Logcat.e("osName" + osName);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Util.saveData("IMEINO", UUID.randomUUID().toString(), getApplicationContext());
        device = device_id + "," + "null" + "," + osName + "," + Build.VERSION.RELEASE + "," + Build.SERIAL + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + "null" + "," + "null" + "," + "null" + "," + latitude + "," + longitude + "," + "null" + ",";
        // String deviceinfo = device_id + "," + telephonyManager.getDeviceId() + "," + osName + "," + Build.VERSION.RELEASE + "," + Build.SERIAL + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + "null" + "," + telephonyManager.getNetworkOperatorName() + "," + "1.0" + "," + latitude + "," + longitude +","+"null"+ ",";
        Util.Logcat.e("device>" + device);
        Util.saveData("Deviceinfo", device, getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_user:

                if (Login.this.getPackageName().equals("com.gieasesales")) {
                    Intent home = new Intent(getApplicationContext(), Register.class);
                    startActivity(home);
                }

                //finish();
                break;
            case R.id.btn_login:
                // setLocale("ta");
                Logincall();
                break;
            case R.id.forget_pin:
                if (!EdUsername.getEditableText().toString().isEmpty()) {
                    ForgetPIN();
                } else {
                    alert.build(getString(R.string.enter_mobile_no), false);
                }
                break;
            default:
                break;
        }
    }

    private void ForgetPIN() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("LoginId", EdUsername.getEditableText().toString());
            obj.put("Password", "");
            Util.Logcat.e("FORGET PASS:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), FORGET_PASSWORD, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Login.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Login.this, getString(R.string.server_error));
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
                            alert.build(resobject.getString("StatusDesc"), true);
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

    private void Logincall() {

        if (EdUsername.getEditableText().toString().isEmpty()) {
            alert.build(getResources().getString(R.string.enter_mobile_no), false);
        } else if (EdPassword.getEditableText().toString().isEmpty()) {
            alert.build(getResources().getString(R.string.enter_pin), false);
        } else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("LoginId", EdUsername.getEditableText().toString());
                // obj.put("Password", Util.EncryptURL(EdPassword.getEditableText().toString()));
                obj.put("FCMToken", Util.getData("FCMToken", getApplicationContext()));
                obj.put("Password", EdPassword.getEditableText().toString());
                obj.put("DeviceType", "1");
                obj.put("Version", Util.app_version);
                // obj.put("DeviceInfo", Util.getData("DeviceInfo",getApplicationContext()));
                obj.put("DeviceInfo", device);
                Util.Logcat.e("LOGIN:::" + obj.toString());
                String data = Util.EncryptURL(obj.toString());
                JSONObject params = new JSONObject();
                params.put("Getrequestresponse", data);
                CallApi.postResponse(Login.this, params.toString(), Util.LOGIN, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {

                        if (message.contains("TimeoutError")) {
                            alert.build(getString(R.string.timeout_error), false);
                            //Util.ErrorLog(Login.this, getString(R.string.timeout_error));
                        } else {
                            alert.build(getString(R.string.server_error), false);
                            //Util.ErrorLog(Login.this, getString(R.string.server_error));
                        }
                        Util.Logcat.e("onError" + message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        //Util.Logcat.e("onResponse : " + response);
                        try {
                            Util.Logcat.e("LOGIN OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                            JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                            Util.Logcat.e("set UserId:::" + resobject.getString("LoginId"));

                            if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                //Util.saveData("newuser","true", getApplicationContext());
                                Util.saveData("LoginId", resobject.getString("LoginId"), getApplicationContext());
                                Util.saveData("UserName", resobject.getString("UserName"), getApplicationContext());
                                Util.saveData("IsFirstLogin", resobject.getString("IsFirstLogin"), getApplicationContext());
                                Util.saveData("ConsumerId", resobject.getString("ConsumerId"), getApplicationContext());
                                Util.saveData("KYCStatus", resobject.getString("KYCStatus"), getApplicationContext());
                                Util.saveData("IsFirstLogin", resobject.getString("IsFirstLogin"), getApplicationContext());
                                Util.saveData("IsShopAvailable", resobject.getString("IsShopAvailable"), getApplicationContext());
                              /*Intent home = new Intent(Login.this, MainActivity.class);
                                startActivity(home);
                                finish();*/
                                JSONArray jsonArray = resobject.optJSONArray("_lstAdvImages");
                                //  Util.saveData("sliderdata", jsonArray.getJSONObject(0).getString("AdvImageURL"), getApplicationContext());
                                Util.saveData("sliderdata", String.valueOf(jsonArray), getApplicationContext());

                               /* Util.Logcat.e("1 :"+jsonArray.getJSONObject(0).getString("AdvImageURL"));
                                Util.Logcat.e("2 :"+jsonArray.getJSONObject(1).getString("AdvImageURL"));
                                Util.Logcat.e("3 :"+jsonArray.getJSONObject(2).getString("AdvImageURL"));*/
                                if (resobject.getString("IsFirstLogin").equalsIgnoreCase("0")) {
                                    Intent home1 = new Intent(Login.this, Profile.class);
                                    home1.putExtra("finish", "true");
                                    startActivity(home1);
                                    finish();
                                } else {
                                    Intent home2 = new Intent(Login.this, ViewpagerActivity.class);
                                    //Intent home2 = new Intent(Login.this, MainActivity.class);
                                    //home2.putExtra("array", String.valueOf(jsonArray));
                                    startActivity(home2);
                                    finish();
                                }

                                // if (chkbox.isChecked() == true) {
                                Util.saveData("loginuser", EdUsername.getEditableText().toString(), getApplicationContext());
                                Util.saveData("loginpass", EdPassword.getEditableText().toString(), getApplicationContext());
                                // }
                            } else if (resobject.getString("Status").equalsIgnoreCase("1")) {

                                if (resobject.getString("VersionChk").equalsIgnoreCase("0")) {
                                    alert.build(resobject.getString("StatusDesc"), false);
                                } else {
                                    AlertPlaystore(resobject.getString("StatusDesc"));
                                }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        // GetUrl();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            loading.show();
            loading.setMessage("Please wait...");
            loading.setCancelable(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading.dismiss();
                }
            }, 3000);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog);
        builder.setMessage(R.string.enable_gps)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void AlertPlaystore(String data) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog);
        builder.setMessage(data)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Update), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        Uri webpage = Uri.parse("https://play.google.com/store/apps/details?id=com.gieasesales&hl=en_IN");
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);
                    }
                });
                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        Intent home2 = new Intent(Login.this, ViewpagerActivity.class);
                        startActivity(home2);
                        finish();
                    }
                });*/
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

package com.gieasesales.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.gieasesales.utils.Util.CALL_VERIFICATION;
import static com.gieasesales.utils.Util.MOBILENO_VALIDATE;
import static com.gieasesales.utils.Util.OTP_VERIFICATION;
import static com.gieasesales.utils.Util.REGISTRATION;
import static com.gieasesales.utils.Util.RESEND_OTP;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button BtnAgree, BtnMobileNo, BtnLogin, BtnRegister;
    LinearLayout LyAgree, LyMobileNo, LyExisting, LyRegister;
    EditText ValidateMobileNo;
    EditText EdExitMobileNo, EdExitOTP;
    EditText EdNewMobileNo, EdNewPIN, EdOTP;
    boolean existing = false;
    String device, osName, latitude, longitude, OTP = "";
    CommonAlertDialog alert;
    TextView terms, resend_otp, call;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //getSupportActionBar().hide();
        if ((ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);
            }
        }
        BtnAgree = findViewById(R.id.btn_agree);
        terms = findViewById(R.id.terms);
        BtnMobileNo = findViewById(R.id.btn_mobile_register);
        BtnLogin = findViewById(R.id.btn_login);
        BtnRegister = findViewById(R.id.btn_register);
        resend_otp = findViewById(R.id.resend_otp);
        call = findViewById(R.id.call);
        BtnAgree.setOnClickListener(this);
        BtnMobileNo.setOnClickListener(this);
        BtnLogin.setOnClickListener(this);
        BtnRegister.setOnClickListener(this);
        terms.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
        call.setOnClickListener(this);
        LyAgree = findViewById(R.id.layout_agree);
        LyMobileNo = findViewById(R.id.layout_mobileno);
        LyExisting = findViewById(R.id.layout_existing);
        LyRegister = findViewById(R.id.layout_register);

        alert = new CommonAlertDialog(this);
        ValidateMobileNo = findViewById(R.id.ed_validate_mobileno);
        EdExitMobileNo = findViewById(R.id.ed_existing_mobileno);

        EdExitOTP = findViewById(R.id.ed_existing_otp);
        EdNewMobileNo = findViewById(R.id.new_mobile_no);

        EdNewPIN = findViewById(R.id.new_pin);
        EdOTP = findViewById(R.id.otp);

       /* if (shouldAskPermissions()) {
            askPermissions();
        }*/

        devicedetails();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void askPermissions() {
        String[] permissions = {
                //  "android.permission.RECEIVE_SMS",
                //  "android.permission.READ_SMS",
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
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
        // TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //telephonyManager.getDeviceId();
        //Util.Logcat.e("UUID"+ UUID.randomUUID().toString());
        // Log.e("IMEI No", telephonyManager.getDeviceId());

        Util.saveData("IMEINO", UUID.randomUUID().toString(), getApplicationContext());
        // Log.e("getImei", telephonyManager.getImei().toString());
        //  Log.e("operator name", telephonyManager.getNetworkOperatorName());
        //Log.e("getDeviceSoftware", telephonyManager.getDeviceSoftwareVersion());
        //simtype,service provider
        // device = device_id + "," + telephonyManager.getDeviceId() + "," + osName + "," + Build.VERSION.RELEASE + "," + Build.SERIAL + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + "null" + "," + telephonyManager.getNetworkOperatorName() + "," + "1.0" + "," + latitude + "," + longitude + "," + "null" + ",";
        device = device_id + "," + "null" + "," + osName + "," + Build.VERSION.RELEASE + "," + Build.SERIAL + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + "null" + "," + "null" + "," + "null" + "," + latitude + "," + longitude + "," + "null" + ",";
        // String deviceinfo = device_id + "," + telephonyManager.getDeviceId() + "," + osName + "," + Build.VERSION.RELEASE + "," + Build.SERIAL + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + "null" + "," + telephonyManager.getNetworkOperatorName() + "," + "1.0" + "," + latitude + "," + longitude +","+"null"+ ",";
        Util.Logcat.e("device>" + device);
        // Util.saveData("DeviceInfo", device, getApplicationContext());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.call:

                if (EdNewMobileNo.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_mobile_no), false);
                } else {
                    CallIVR();
                }
                break;
            case R.id.resend_otp:
                MobilenoValidate("true");
                break;
            case R.id.terms:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Util.PRIVACY_POLICY));
                startActivity(browserIntent);
                break;
            case R.id.btn_agree:

                devicedetails();
                LyAgree.setVisibility(View.GONE);
                LyMobileNo.setVisibility(View.VISIBLE);
                LyExisting.setVisibility(View.GONE);
                LyRegister.setVisibility(View.GONE);

               /* Intent home = new Intent(Register.this, MainActivity.class);
                startActivity(home);
                finish();*/

                break;
            case R.id.btn_mobile_register:

                if (ValidateMobileNo.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_mobile_no), false);
                } else if (ValidateMobileNo.getEditableText().toString().length() != 10) {
                    alert.build(getString(R.string.enter_valid_mobileno), false);
                } else {
                    MobilenoValidate("false");
                }
                break;

            case R.id.btn_login:

                if (EdExitMobileNo.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_mobile_no), false);
                } else if (EdExitMobileNo.getEditableText().toString().length() != 10) {
                    alert.build(getString(R.string.enter_valid_mobileno), false);
                } else if (EdExitOTP.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.mobile_otp), false);
                } else {
                    MobilenoValidate("false");
                }
                break;

            case R.id.btn_register:

                if (EdNewMobileNo.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_mobile_no), false);
                } else if (EdNewPIN.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_pin), false);
                } else if (EdOTP.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_otp), false);
                } else {
                    register();
                }
                break;
            default:
                break;

        }
    }

    private void CallIVR() {

        Util.Logcat.e("CALL_VERIFICATION" + CALL_VERIFICATION + EdNewMobileNo.getEditableText().toString() + "_" + OTP);

        CallApi.getResponse(this, CALL_VERIFICATION + EdNewMobileNo.getEditableText().toString() + "_" + OTP, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                if (message.contains("TimeoutError")) {
                    alert.build(getString(R.string.timeout_error), false);
                    //Util.ErrorLog(Register.this, getString(R.string.timeout_error));
                } else {
                    alert.build(getString(R.string.server_error), false);
                    //Util.ErrorLog(Register.this, getString(R.string.server_error));
                }
                Util.Logcat.e("onError" + message);
            }

            @Override
            public void onResponse(JSONObject response) {
                Util.Logcat.e("CALL_VERIFICATION : " + response.toString());
                   /* try {
                        Util.Logcat.e("OUTPUT:::" + response.toString());
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            Intent home = new Intent(Register.this, Login.class);
                            startActivity(home);
                            finish();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"),false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
            }
        });

    }

    private void OTPVerification() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("MobileNo", EdExitMobileNo.getEditableText().toString());
            obj.put("OTP", EdExitOTP.getEditableText().toString());
            Util.Logcat.e("REGISTRATION:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), OTP_VERIFICATION, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.server_error));
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
                            if (Register.this.getPackageName().equals("com.gieasesales")) {
                                Intent home = new Intent(Register.this, Login.class);
                                startActivity(home);
                                finish();
                            }

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
    protected void onResume() {
        super.onResume();
        Util.Logcat.e("onResume" + "onResume");
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("OTP"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.Logcat.e("onPause" + "onPause");
        //  LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void register() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("MobileNo", EdNewMobileNo.getEditableText().toString());
            obj.put("OTP", EdOTP.getEditableText().toString());
            obj.put("PIN", EdNewPIN.getEditableText().toString());
            //obj.put("CustomerType", "1");
            obj.put("DeviceType", "1");
            obj.put("DeviceInfo", device);
            Util.saveData("DeviceInfo", device, getApplicationContext());
            Util.Logcat.e("REGISTRATION:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), REGISTRATION, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.server_error));
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

                            if (Register.this.getPackageName().equals("com.gieasesales")) {
                                Intent home = new Intent(Register.this, Login.class);
                                startActivity(home);
                                finish();
                            }

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

    private void MobilenoValidate(final String popup) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("MobileNo", ValidateMobileNo.getEditableText().toString());
            Util.Logcat.e("MOBILE OTP:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), MOBILENO_VALIDATE, new VolleyResponseListener() {
                @Override
                public void onError(String message) {

                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Register.this, MOBILENO_VALIDATE.replace(MOBILE_API,"")+":"+getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Register.this, MOBILENO_VALIDATE.replace(MOBILE_API,"")+":"+getString(R.string.server_error));

                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("MOBILE OTP:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        //  Status=2 - New User - Show OTP and PIN
                        //  Status=0 - Existing User - Show OTP
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

                            // alert.build(resobject.getString("StatusDesc"));
                            LyAgree.setVisibility(View.GONE);
                            LyMobileNo.setVisibility(View.GONE);
                            LyRegister.setVisibility(View.GONE);
                            LyExisting.setVisibility(View.VISIBLE);
                            EdExitMobileNo.setText(ValidateMobileNo.getEditableText().toString());
                            existing = true;
                            // Getotp();
                            close(resobject.getString("StatusDesc"));

                        } else if (resobject.getString("Status").equalsIgnoreCase("2")) {
                           /* Intent login = new Intent(Register.this, Login.class);
                            startActivity(login);
                            finish();*/
                            LyAgree.setVisibility(View.GONE);
                            LyMobileNo.setVisibility(View.GONE);
                            LyRegister.setVisibility(View.VISIBLE);
                            LyExisting.setVisibility(View.GONE);
                            EdNewMobileNo.setText(ValidateMobileNo.getEditableText().toString());

                            OTP = resobject.getString("OTP");

                            if (popup.equalsIgnoreCase("true")) {
                                alert.build("OTP sent Success", true);
                            }
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

    private void close(final String s) {
        final AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.alertDialog);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Ok",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Register.this.getPackageName().equals("com.gieasesales")) {
                            Intent home = new Intent(Register.this, Login.class);
                            startActivity(home);
                            finish();
                        }

                    }
                });


        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void Getotp() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("MobileNo", EdExitMobileNo.getEditableText().toString());
            Util.Logcat.e("GET OTP:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(this, params.toString(), RESEND_OTP, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Register.this, getString(R.string.server_error));
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
}

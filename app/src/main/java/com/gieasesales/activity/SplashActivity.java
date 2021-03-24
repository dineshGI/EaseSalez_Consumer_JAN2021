package com.gieasesales.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 3000;
    CommonAlertDialog alert;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        alert = new CommonAlertDialog(this);
        //getSupportActionBar().hide();
        // Util.checkTime("","","");
        //Util.Logcat.e("STAGE :"+STAGE);
        // FirebaseApp.initializeApp(this);

        if (SplashActivity.this.getPackageName().equals("com.gieasesales")) {
            init();
        }
    }

    private void init() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

               /* Intent home = new Intent(getApplicationContext(), FilterActivityCar.class);
                startActivity(home);
                finish();*/

                String hai = Util.getData("ConsumerId", getApplicationContext());

                if (hai.isEmpty() || hai == null) {
                    Intent home = new Intent(getApplicationContext(), Login.class);
                    startActivity(home);
                    finish();
                } else {

                    Logincall();

                }
            }
        }, DELAY_TIME);
    }

    private void Logincall() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("LoginId", Util.getData("loginuser", getApplicationContext()));
            // obj.put("Password", Util.EncryptURL(EdPassword.getEditableText().toString()));
            obj.put("FCMToken", Util.getData("FCMToken", getApplicationContext()));
            obj.put("Password", Util.getData("loginpass", getApplicationContext()));
            obj.put("DeviceType", "1");
            obj.put("Version", Util.app_version);
            // obj.put("DeviceInfo", Util.getData("Deviceinfo",getApplicationContext()));
            obj.put("DeviceInfo", Util.getData("Deviceinfo", getApplicationContext()));
            Util.Logcat.e("LOGIN:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(SplashActivity.this, params.toString(), Util.LOGIN, new VolleyResponseListener() {
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
                            Util.saveData("MarathonFlag", resobject.getString("MarathonFlag"), getApplicationContext());
                            Util.saveData("MarthonEndDate", resobject.getString("MarthonEndDate"), getApplicationContext());
                              /*Intent home = new Intent(Login.this, MainActivity.class);
                                startActivity(home);
                                finish();*/
                            JSONArray jsonArray = resobject.optJSONArray("_lstAdvImages");
                            //  Util.saveData("sliderdata", jsonArray.getJSONObject(0).getString("AdvImageURL"), getApplicationContext());
                            Util.saveData("sliderdata", String.valueOf(jsonArray), getApplicationContext());

                            Util.Logcat.e("MarthonEndDate :" + resobject.getString("MarthonEndDate"));
                            Util.Logcat.e("MarathonFlag  :" + resobject.getString("MarathonFlag"));
                               /* Util.Logcat.e("1 :"+jsonArray.getJSONObject(0).getString("AdvImageURL"));
                                Util.Logcat.e("2 :"+jsonArray.getJSONObject(1).getString("AdvImageURL"));
                                Util.Logcat.e("3 :"+jsonArray.getJSONObject(2).getString("AdvImageURL"));*/
                            if (resobject.getString("IsFirstLogin").equalsIgnoreCase("0")) {
                                Intent home1 = new Intent(SplashActivity.this, Profile.class);
                                home1.putExtra("finish", "true");
                                startActivity(home1);
                                finish();
                            } else {

                                try {
                                    if (resobject.getString("MarathonFlag").equalsIgnoreCase("0") && Util.registration(resobject.getString("MarthonEndDate")) == true) {
                                        Intent event = new Intent(SplashActivity.this, MarathonRegister.class);
                                        startActivity(event);
                                        finish();
                                    } else {
                                        Intent home2 = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(home2);
                                        finish();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
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

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Util.saveData("FCMToken", token, getApplicationContext());
                        // Log and toast
                        Util.Logcat.e("MSG - TOKEN: " + token);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void AlertPlaystore(String data) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog);
        builder.setMessage(data)
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
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

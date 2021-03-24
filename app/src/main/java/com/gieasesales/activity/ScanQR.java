package com.gieasesales.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gieasesales.utils.Util.SCAN_QR;
import static com.gieasesales.utils.Util.getData;

public class ScanQR extends AppCompatActivity {

    CommonAlertDialog alert;
    ImageView back_arrow;
    LinearLayout showdata;
    private CodeScanner mCodeScanner;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Scan QR");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);

        back_arrow = findViewById(R.id.back_arrow);
        showdata = findViewById(R.id.showdata);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanQR.this, "Scan Success", Toast.LENGTH_SHORT).show();
                        mCodeScanner.releaseResources();
                        mCodeScanner.stopPreview();
                        ShowAlert(result.getText());

                    }
                });
            }
        });

        mCodeScanner.startPreview();

    }

    private void ShowAlert(final String s) {
        final AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.alertDialog);
        alertDialogBuilder.setTitle("Want to Proceed?");
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Proceed",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        GetDetails(s);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //GetDetails(s);

                        finish();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void GetDetails(String value) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSUrl", value);
            Util.Logcat.e("QR REQUEST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), SCAN_QR, new VolleyResponseListener() {

                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(ScanQR.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(ScanQR.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {

                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("QR REQUEST:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //setContentView(R.layout.scan_qr);
                            //showdata.setVisibility(View.VISIBLE);
                            //alert.build(resobject.getString("StatusDesc"));
                            Util.ShowQRDATA = resobject.getString("StatusDesc");
                            //  close(resobject.getString("StatusDesc"));
                            finish();
                        } else {
                            Util.ShowQRDATA = resobject.getString("StatusDesc");
                            finish();
                            // alert.build(resobject.getString("StatusDesc"));
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

                        if (ScanQR.this.getPackageName().equals("com.gieasesales")) {
                            Intent home = new Intent(ScanQR.this, MainActivity.class);
                            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(home);
                            finish();
                        }


                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //GetDetails(s);

                        finish();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(true);
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

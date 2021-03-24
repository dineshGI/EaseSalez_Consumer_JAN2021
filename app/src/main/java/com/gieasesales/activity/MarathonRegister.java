package com.gieasesales.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.gieasesales.R;
import com.gieasesales.utils.Util;

public class MarathonRegister extends Activity {

    WebView webView;
    ImageView btn_exit;
    ProgressDialog pd;

    public static void log(String message) {
        Log.i("gi", message);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_activity);
        webView = findViewById(R.id.webview);
        btn_exit = findViewById(R.id.btn_exit);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String Weburl;

        if (Util.url_type == 0) {
            Weburl = Util.NEW_REG + Util.getData("ConsumerId", getApplicationContext());
        } else {
            Weburl = getString(R.string.reg_url) + Util.getData("ConsumerId", getApplicationContext());
        }

        Util.Logcat.e("LOGIN URL" + Weburl);
        webView.setWebViewClient(getWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.loadUrl(Weburl);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAppExitDialog();

                if (MarathonRegister.this.getPackageName().equals("com.gieasesales")) {
                    GoApp();
                }

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                pd.dismiss();
            }
        }, 3000);
    }

    private void GoApp() {
        Intent home2 = new Intent(MarathonRegister.this, MainActivity.class);
        startActivity(home2);
        finish();
    }

    private WebViewClient getWebViewClient() {

        return new WebViewClient() {

            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                Log.e("url2::::", request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.e("url::::", url);
                return true;
            }

        };
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            // If web view have back history, then go to the web view back history
            webView.goBack();
        } else {
            showAppExitDialog();
            //this.finish();
        }
    }


    protected void showAppExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Smart Salez");
        builder.setMessage("Do you want to close registration?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (MarathonRegister.this.getPackageName().equals("com.gieasesales")) {
                    GoApp();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app

            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }

        return false;

    }

    void showErrorDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MarathonRegister.this).create();
        alertDialog.setTitle("GiRetail");
        alertDialog.setMessage("Check your internet connection and try again.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });

        alertDialog.show();
    }
}

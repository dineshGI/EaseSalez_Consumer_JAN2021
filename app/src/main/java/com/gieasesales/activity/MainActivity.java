package com.gieasesales.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gieasesales.Http.CallApi;
import com.gieasesales.Module.Account;
import com.gieasesales.Module.Cart;
import com.gieasesales.Module.History;
import com.gieasesales.Module.Search;
import com.gieasesales.Module.StoreMap;
import com.gieasesales.R;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.OfferAlertDialog;
import com.gieasesales.utils.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.PAYMENT;
import static com.gieasesales.utils.Util.SHOWALERT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    BottomNavigationView navView;
    CommonAlertDialog alert;
    OfferAlertDialog offeralert;
    String image = "";
    long time = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSupportActionBar().hide();
        loadFragment(new StoreMap());
        navView = findViewById(R.id.nav_view);
        alert = new CommonAlertDialog(this);
        offeralert = new OfferAlertDialog(this);
        navView.setOnNavigationItemSelectedListener(this);
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        String array = Util.getData("_PromotionsAndOfferPOSResModel", getApplicationContext());

        try {
            JSONArray jsonarray = new JSONArray(array);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject bannerobject = jsonarray.getJSONObject(i);
                if (bannerobject.getString("BannerTypeId").equalsIgnoreCase("3")) {
                    image = bannerobject.getString("ImagePath");
                    if (!bannerobject.getString("DisplayTime").equalsIgnoreCase("0")) {
                        time = Long.parseLong(bannerobject.getString("DisplayTime"));
                    }
                    Util.Logcat.e("DisplayTime :" + bannerobject.getString("DisplayTime"));
                    break;
                }
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Write whatever to want to do after delay specified (1 sec)
                    if (SHOWALERT == true && !image.isEmpty()) {
                        offeralert.showimg(image);
                        SHOWALERT = false;
                    }
                }
            }, time);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // UI changes has been done

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_store:
                // mTextMessage.setText(R.string.title_home);
                fragment = new StoreMap();
                // fragment = new ExploreFrag();
                break;
            case R.id.navigation_search:
                // mTextMessage.setText(R.string.title_dashboard);
                fragment = new Search();
               /* Intent das=new Intent(MainActivity.this,CartActivity.class);
                startActivity(das);*/
                break;
            case R.id.navigation_cart:
                // mTextMessage.setText(R.string.title_dashboard);
                fragment = new Cart();
               /* Intent das=new Intent(MainActivity.this,CartActivity.class);
                startActivity(das);*/
                break;

            case R.id.navigation_history:
                // mTextMessage.setText(R.string.title_notifications);
                fragment = new History();
                break;
            case R.id.navigation_account:
                // mTextMessage.setText(R.string.title_notifications);
                fragment = new Account();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home != seletedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            super.onBackPressed();
            exitapp();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void exitapp() {

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.alertDialog);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        //Prints all extras. Replace with app logic.
        StringBuilder s = new StringBuilder();
        List<Map<String, String>> Payment = new ArrayList<>();

        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {

                if (!bundle.getString("txStatus").equalsIgnoreCase("CANCELLED")) {
                    for (String key : bundle.keySet()) {
                        Util.Logcat.e("IFFRAN" + key + " : " + " RES " + bundle.getString(key));
                        Util.Logcat.e("txStatus" + bundle.getString("txStatus"));
                        if (!bundle.getString(key).equalsIgnoreCase("CANCELLED"))

                            if (bundle.getString(key) != null) {
                                Util.Logcat.e("PAYMENT RESPONSE" + key + " : " + bundle.getString(key));
                                s.append(key).append(" : ").append(bundle.getString(key)).append("\n");
                                HashMap<String, String> DataHashMap = new HashMap<>();
                                if (key != null && bundle.getString(key) != null)
                                    DataHashMap.put(key, bundle.getString(key));
                                Payment.add(DataHashMap);

                            }
                    }
                    PaymentUpdate(Payment, s.toString());
                }

            }
        }

    }

    private void PaymentUpdate(final List<Map<String, String>> payment, final String value) {
        //[{paymentMode=WALLET}, {orderId=20200611151102AA275}, {txTime=2020-06-11 15:22:08}, {referenceId=359079}, {type=CashFreeResponse}, {txMsg=Transaction Successful}, {signature=H++0u2y2hpYgD2UOG3iRIZbjP1sutY/82p4lGLomjpE=}, {orderAmount=1.00}, {txStatus=SUCCESS}]
        try {
            JSONObject obj = new JSONObject();

            Util.Logcat.e("orderId" + "" + payment.get(1).get("orderId"));
            Util.Logcat.e("orderAmount" + "" + payment.get(7).get("orderAmount"));
            Util.Logcat.e("txStatus" + "" + payment.get(8).get("txStatus"));
            Util.Logcat.e("referenceId" + "" + payment.get(3).get("referenceId"));
            Util.Logcat.e("txTime" + "" + payment.get(2).get("txTime"));
            Util.Logcat.e("signature" + "" + payment.get(6).get("signature"));

            if (!payment.get(1).get("orderId").equalsIgnoreCase("false")) {
                obj.put("EaseSalezRefNo", payment.get(1).get("orderId"));
            } else {
                obj.put("EaseSalezRefNo", "");
            }
            if (!payment.get(7).get("orderAmount").equalsIgnoreCase("false")) {
                obj.put("TranAmount", payment.get(7).get("orderAmount"));
            } else {
                obj.put("TranAmount", "");
            }
            if (!payment.get(8).get("txStatus").equalsIgnoreCase("false")) {
                obj.put("PGStatus", payment.get(8).get("txStatus"));
            } else {
                obj.put("PGStatus", "");
            }
            if (!payment.get(3).get("referenceId").equalsIgnoreCase("false")) {
                obj.put("PGRefNo", payment.get(3).get("referenceId"));
            } else {
                obj.put("PGRefNo", "");
            }
            if (!payment.get(2).get("txTime").equalsIgnoreCase("false")) {
                obj.put("PGRefNo1", payment.get(2).get("txTime"));
            } else {
                obj.put("PGRefNo1", "");
            }
            if (!payment.get(6).get("signature").equalsIgnoreCase("false")) {
                obj.put("PGRefNo2", payment.get(6).get("signature"));
            } else {
                obj.put("PGRefNo2", "");
            }
            if (!payment.get(0).get("paymentMode").equalsIgnoreCase("false")) {
                obj.put("PGRefNo3", payment.get(0).get("paymentMode"));
            } else {
                obj.put("PGRefNo3", "");
            }

            if (!payment.get(5).get("txMsg").equalsIgnoreCase("false")) {
                obj.put("PGRefNo4", payment.get(5).get("txMsg"));
            } else {
                obj.put("PGRefNo4", "");
            }

            obj.put("PGRefNo5", "");
            obj.put("IndentId", "0");

            Util.Logcat.e("PAYMENT" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PAYMENT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(MainActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(MainActivity.this, getString(R.string.server_error));
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
                            if (value.contains("SUCCESS")) {
                                String datas = "Your transaction has been successfully completed pg ref:" + payment.get(3).get("referenceId") + "and order no:" + payment.get(1).get("orderId");
                                ShowAlertNew(datas, true, true);
                            } else {
                                String datas = "Your transaction has been failed pg ref:" + payment.get(3).get("referenceId") + "and order no:" + payment.get(1).get("orderId");
                                ShowAlertNew(datas, true, false);
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

    private void ShowAlert(String s, final boolean close) {
        final AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.alertDialog);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (close) {
                            Util.saveData("POSId", "", getApplicationContext());
                        } else {

                        }
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.show();
    }

    private void ShowAlertNew(String s, final boolean close, boolean value) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cutom_alert);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.texts);
        LinearLayout success = dialog.findViewById(R.id.success);
        LinearLayout failure = dialog.findViewById(R.id.failure);
        text.setText(s);
        Util.Logcat.e("STATUS>" + value);
        if (value) {
            success.setVisibility(View.VISIBLE);
        } else {
            failure.setVisibility(View.VISIBLE);
        }
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (close) {
                    Util.saveData("POSId", "", getApplicationContext());
                } else {
                }
            }
        });
        failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (close) {
                    Util.saveData("POSId", "", getApplicationContext());

                } else {
                }
            }
        });
        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (close) {
                    Util.saveData("POSId", "", getApplicationContext());

                } else {
                }

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}

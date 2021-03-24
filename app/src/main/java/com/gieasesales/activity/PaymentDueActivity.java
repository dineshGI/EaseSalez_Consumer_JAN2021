package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.PaymentDueAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.PAYMENT_DUE;
import static com.gieasesales.utils.Util.getData;

public class PaymentDueActivity extends AppCompatActivity {
    CommonAlertDialog alert;
    PaymentDueAdapter adapter;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    ListView listView;
    ImageView back_arrow;
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_due);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Dues");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = findViewById(R.id.listview);
        back_arrow = findViewById(R.id.back_arrow);
        ListCollection = new ArrayList<>();
        alert = new CommonAlertDialog(this);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetPaymentDetail();
    }

    private void GetPaymentDetail() {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerMobileNo", "");
            Util.Logcat.e("GET DETAIL:::"+ obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PAYMENT_DUE, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(PaymentDueActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(PaymentDueActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError"+ message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Util.Logcat.e("onResponse"+ String.valueOf(response));
                    try {
                        Util.Logcat.e("OUTPUT:::"+ Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstConsumerCreditBalanceDetails");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            Util.Logcat.e("EMPTY | Null:::"+ String.valueOf(jsonArray.length()));
                            alert.build("No Details",false);

                        } else {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("POSName", imageobject.getString("POSName"));
                                    DataHashMap.put("ConsumerName", imageobject.getString("ConsumerName"));
                                    DataHashMap.put("ConsumerMobileNo", imageobject.getString("ConsumerMobileNo"));
                                    DataHashMap.put("DueDate", imageobject.getString("DueDate"));
                                    DataHashMap.put("BalanceAmount", imageobject.getString("BalanceAmount"));
                                    DataHashMap.put("ShopAddress", imageobject.getString("ShopAddress"));
                                    DataHashMap.put("POSImagePath", imageobject.getString("POSImagePath"));
                                    ListCollection.add(DataHashMap);
                                }
                                adapter = new PaymentDueAdapter(PaymentDueActivity.this, ListCollection);
                                listView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

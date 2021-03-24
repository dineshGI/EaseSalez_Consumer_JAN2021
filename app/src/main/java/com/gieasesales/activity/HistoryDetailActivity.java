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
import com.gieasesales.adapter.HistoryDetailAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.HISTORY_DETAIL;

public class HistoryDetailActivity extends AppCompatActivity {

    CommonAlertDialog alert;
    HistoryDetailAdapter adapter;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    ListView listView;
    ImageView back_arrow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
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
        GetHistoryDetail(getIntent().getStringExtra("IndentId"));
    }

    private void GetHistoryDetail(String IndentId) {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("IndentId", IndentId);
            Util.Logcat.e("GET DETAIL:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), HISTORY_DETAIL, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(HistoryDetailActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(HistoryDetailActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetIndentPrdsOutputModel");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                            alert.build("No Details",false);
                        } else {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("ProductName", imageobject.getString("ProductName"));
                                    DataHashMap.put("QTY", imageobject.getString("QTY"));
                                    DataHashMap.put("PerUnitMRP", imageobject.getString("PerUnitMRP"));
                                    DataHashMap.put("TotalMRP", imageobject.getString("TotalMRP"));
                                    DataHashMap.put("CostMRP", imageobject.getString("CostMRP"));
                                    DataHashMap.put("NetPay", imageobject.getString("NetPay"));
                                    DataHashMap.put("TotalDiscountAmount", imageobject.getString("TotalDiscountAmount"));
                                    ListCollection.add(DataHashMap);
                                }
                                adapter = new HistoryDetailAdapter(HistoryDetailActivity.this, ListCollection);
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

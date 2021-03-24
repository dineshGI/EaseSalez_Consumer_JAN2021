package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.HistoryAdapterNew;
import com.gieasesales.adapter.QuoteAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.GET_QUOTE;
import static com.gieasesales.utils.Util.getData;

public class QuoteActivity extends AppCompatActivity {

    CommonAlertDialog alert;
    ImageView back_arrow;
    QuoteAdapter adapter;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    ListView listView;
    EditText search;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_activity);

        alert = new CommonAlertDialog(this);
        ListCollection = new ArrayList<>();
        back_arrow = findViewById(R.id.back_arrow);
        search = findViewById(R.id.search);
        listView = findViewById(R.id.listview);
        ListCollection = new ArrayList<>();
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GetQuote();
    }

    private void GetQuote() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("POSId", "0");
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));

            Util.Logcat.e("QUOTE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_QUOTE, new VolleyResponseListener() {
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
                    try {
                        Util.Logcat.e("QUOTE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        try {
                            JSONArray jsonArray = resobject.optJSONArray("_lstQuoteResModel");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("ConsumerMobileNo", imageobject.getString("ConsumerMobileNo"));
                                DataHashMap.put("ProductName", imageobject.getString("ProductName"));
                                DataHashMap.put("ProductId", imageobject.getString("ProductId"));
                                DataHashMap.put("ConsumerAddress", imageobject.getString("ConsumerAddress"));
                                DataHashMap.put("POSCity", imageobject.getString("POSCity"));
                                DataHashMap.put("MRP", imageobject.getString("MRP"));
                                DataHashMap.put("OfferPrice", imageobject.getString("OfferPrice"));
                                DataHashMap.put("SellingPrice", imageobject.getString("SellingPrice"));
                                DataHashMap.put("FilePath", imageobject.getString("FilePath"));
                                if (imageobject.getString("FilePath").contains(".jpg") || imageobject.getString("FilePath").contains(".png") || imageobject.getString("FilePath").contains(".PNG") || imageobject.getString("FilePath").contains(".jpeg")) {
                                    DataHashMap.put("imgavailable", "true");
                                } else {
                                    DataHashMap.put("imgavailable", "false");
                                }
                                ListCollection.add(DataHashMap);
                            }

                            if (ListCollection.size() > 0) {
                                adapter = new QuoteAdapter(QuoteActivity.this, ListCollection);
                                listView.setAdapter(adapter);
                            } else {
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                                alert.build(getString(R.string.NoRecordsAvailable), false);
                            }
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    long viewId = view.getId();
                                        /*if (viewId == R.id.views) {
                                            CancelOrder(ListCollection.get(position).get(
                                                    "IndentId"));
                                        }*/
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
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

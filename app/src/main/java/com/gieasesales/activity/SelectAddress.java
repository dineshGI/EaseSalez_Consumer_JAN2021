package com.gieasesales.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.SelectAddressAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.DELETE_ADDRESS;
import static com.gieasesales.utils.Util.GET_ADDRESS;
import static com.gieasesales.utils.Util.SET_DEFAULT;
import static com.gieasesales.utils.Util.getData;

public class SelectAddress extends AppCompatActivity {

    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    CommonAlertDialog alert;
    ListView listView;
    SelectAddressAdapter adapter;
    TextView btn_add;
    ImageView back_arrow;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Address");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Util.UPDATECHARGES=true;
        alert = new CommonAlertDialog(this);
        listView = findViewById(R.id.listview);
        btn_add = findViewById(R.id.btn_add);
        back_arrow = findViewById(R.id.back_arrow);
        btn_add.setVisibility(View.GONE);
        ListCollection = new ArrayList<>();
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (SelectAddress.this.getPackageName().equals("com.gieasesales")) {
                    Intent addaddress = new Intent(SelectAddress.this, AddAddress.class);
                    startActivity(addaddress);
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

    @Override
    protected void onResume() {
        super.onResume();
        GetAllAddress();
    }

    private void GetAllAddress() {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            //obj.put("POSId", "0");
            Util.Logcat.e("GET ADDRESS:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_ADDRESS, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        JSONArray jsonArray = resobject.optJSONArray("_lstGetConsumerAddressOutput");
                        // TxtNoData.setVisibility(View.VISIBLE);
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("ConsumerAddressId", imageobject.getString("ConsumerAddressId"));
                                DataHashMap.put("ConsumerId", imageobject.getString("ConsumerId"));
                                DataHashMap.put("Address1", imageobject.getString("Address1") + ", " + imageobject.getString("City") + ", " + imageobject.getString("Pincode"));
                                DataHashMap.put("ContactName", imageobject.getString("ContactName"));
                                DataHashMap.put("ContactNumber", imageobject.getString("ContactNumber"));
                                DataHashMap.put("LandMark", imageobject.getString("LandMark"));
                                DataHashMap.put("DefaultAddress", imageobject.getString("DefaultAddress"));
                                DataHashMap.put("Pincode", imageobject.getString("Pincode"));
                                DataHashMap.put("City", imageobject.getString("City"));
                                ListCollection.add(DataHashMap);
                            }
                            if (ListCollection.size() > 0) {
                                adapter = new SelectAddressAdapter(SelectAddress.this, ListCollection);
                                listView.setAdapter(adapter);
                            } else {
                                alert.build("No Address",false);
                                if (adapter != null)
                                    adapter.notifyDataSetChanged();
                            }
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    long viewId = view.getId();
                                    if (ListCollection.size() > 0) {
                                        if (viewId == R.id.btn_default) {
                                            SetDefault(ListCollection.get(position).get("ConsumerAddressId"));
                                        }
                                    } else {
                                        alert.build(getString(R.string.AddressEmpty), false);

                                    }
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

    private void SetDefault(String ConsumerAddressId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerAddressId", ConsumerAddressId);
            obj.put("DefaultAddress", "1");
            Util.Logcat.e("SET DEFAULT::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), SET_DEFAULT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.server_error));
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

                            finish();

                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {

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

    private void DeleteAddress(String ConsumerAddressId) {
        try {
            JSONObject obj = new JSONObject();
            // obj.put("ConsumerAddressId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerAddressId", ConsumerAddressId);

            Util.Logcat.e("DELETE ADDRESS:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), DELETE_ADDRESS, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(SelectAddress.this, getString(R.string.server_error));
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
                            GetAllAddress();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"),false);
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

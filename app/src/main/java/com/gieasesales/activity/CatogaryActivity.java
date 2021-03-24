package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.CatagoryAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.gieasesales.utils.Util.BANNER;
import static com.gieasesales.utils.Util.GET_MAINCATAGORY;
import static com.gieasesales.utils.Util.getData;

public class CatogaryActivity extends AppCompatActivity {

    CommonAlertDialog alert;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    GridView listView;
    CatagoryAdapter adapter;
    String POSId, PosName, PosAddress, ImagePath, IsSubCatAvailable, ShopPromotionMsg, TxtRating, POSTypeId;
    TextView new_shopname, new_address, AvgRating;
    ImageView new_shopimg, back_arrow;
    EditText search;
    BannerSlider bannerSlider;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.catogory_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);

        PosName = getIntent().getStringExtra("PosName");
        PosAddress = getIntent().getStringExtra("Address");
        ImagePath = getIntent().getStringExtra("ImagePath");
        POSId = getIntent().getStringExtra("POSId");
        TxtRating = getIntent().getStringExtra("AvgRating");
        IsSubCatAvailable = getIntent().getStringExtra("IsSubCatAvailable");
        ShopPromotionMsg = getIntent().getStringExtra("ShopPromotionMsg");
        POSTypeId = getIntent().getStringExtra("POSTypeId");

        toolbar.setTitle(PosName);
        toolbar.setSubtitle(PosAddress);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bannerSlider = findViewById(R.id.slider);
        alert = new CommonAlertDialog(this);
        listView = findViewById(R.id.listview);
        AvgRating = findViewById(R.id.AvgRating);

        AvgRating.setText(TxtRating);
        search = findViewById(R.id.search);
        new_shopname = findViewById(R.id.new_shopname);
        new_shopname.setText(PosName);
        new_address = findViewById(R.id.new_address);
        new_address.setText(PosAddress);
        new_shopimg = findViewById(R.id.new_shopimg);
        back_arrow = findViewById(R.id.back_arrow);
        if (ImagePath.contains(".jpg") || ImagePath.contains(".png") || ImagePath.contains(".PNG") || ImagePath.contains(".jpeg")) {
            try {
                Glide.with(this).load(new URL(ImagePath)).into(new_shopimg);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            new_shopimg.setVisibility(View.INVISIBLE);
        }

        listView.setVerticalScrollBarEnabled(false);

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

        ListCollection = new ArrayList<>();

        back_arrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });

        LoadBanner();
        GetCatogory();

    }

    private void LoadBanner() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("POSId", POSId);
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("BANNER:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(this, params.toString(), BANNER, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ExploreActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ExploreActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Util.Logcat.e("BANNER:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            JSONArray bannerarray = resobject.optJSONArray("_lstPosBannerResModel");
                            List<Banner> banners = new ArrayList<>();
                            for (int i = 0; i < bannerarray.length(); i++) {
                                JSONObject imageobject = bannerarray.getJSONObject(i);
                                //add banner using image url
                                banners.add(new RemoteBanner(imageobject.getString("ImagePath")));

                            }
                            bannerSlider.setBanners(banners);
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

    private void GetCatogory() {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            //obj.put("POSId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            Util.Logcat.e("GET MAIN CATAGORY:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_MAINCATAGORY, new VolleyResponseListener() {

                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CatogaryActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CatogaryActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("CATTTT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetLoadCategoriesOutput1");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                            alert.build(getString(R.string.cat_not_available), false);
                            //TxtNoData.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);

                                    //  if (imageobject.getString("IsActive").equalsIgnoreCase("1")) {
                                    DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("POSId", POSId);
                                    DataHashMap.put("PosName", PosName);
                                    DataHashMap.put("PosAddress", PosAddress);
                                    DataHashMap.put("AvgRating", TxtRating);
                                    DataHashMap.put("ImagePath", imageobject.getString("CategoryImagePath"));
                                    DataHashMap.put("shopimage", ImagePath);
                                    if (imageobject.getString("CategoryImagePath").contains(".jpg") || imageobject.getString("CategoryImagePath").contains(".png") || imageobject.getString("CategoryImagePath").contains(".PNG") || imageobject.getString("CategoryImagePath").contains(".jpeg") || imageobject.getString("CategoryImagePath") != null) {
                                        DataHashMap.put("imgavailable", "true");
                                    } else {
                                        DataHashMap.put("imgavailable", "false");
                                    }
                                    DataHashMap.put("CategoryId", imageobject.getString("CategoryId"));
                                    DataHashMap.put("CategoryName", imageobject.getString("CategoryName"));
                                    DataHashMap.put("CategoryDesc", imageobject.getString("CategoryDesc"));
                                    DataHashMap.put("CategoryImagePath", imageobject.getString("CategoryImagePath"));
                                    Log.e("CategoryImagePath", imageobject.getString("CategoryImagePath"));
                                    DataHashMap.put("IsSubCatAvailable", IsSubCatAvailable);
                                    DataHashMap.put("ShopPromotionMsg", ShopPromotionMsg);
                                    DataHashMap.put("POSTypeId", POSTypeId);
                                    ListCollection.add(DataHashMap);
                                    //   }
                                }
                                if (ListCollection.size() > 0) {
                                    adapter = new CatagoryAdapter(CatogaryActivity.this, ListCollection);
                                    listView.setAdapter(adapter);
                                } else {
                                    alert.build(getString(R.string.cat_not_available), false);
                                }
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

package com.gieasesales.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.gieasesales.Http.CallApi;
import com.gieasesales.Model.CarBike;
import com.gieasesales.Model.Products;
import com.gieasesales.R;
import com.gieasesales.adapter.CarBikeAdapter;
import com.gieasesales.adapter.ProductAdapter;
import com.gieasesales.adapter.ShowTitleAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.interfaces.clickInterface;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.DividerDecorator;
import com.gieasesales.utils.ExpandableHeightGridView;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.gieasesales.adapter.ProductAdapter.cart;
import static com.gieasesales.utils.Util.ADD_PRODUCT;
import static com.gieasesales.utils.Util.ADD_WISHLIST;
import static com.gieasesales.utils.Util.BANNER;
import static com.gieasesales.utils.Util.GET_CARTCOUNT;
import static com.gieasesales.utils.Util.GET_CATEGORY;
import static com.gieasesales.utils.Util.GET_MAINCATAGORY;
import static com.gieasesales.utils.Util.GET_PRODUCTLIST;
import static com.gieasesales.utils.Util.getData;

public class CarBikeActivity extends AppCompatActivity implements clickInterface, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    RecyclerView TitleRecyclerView;
    ExpandableHeightGridView listview;
    private HashMap<String, String> ButtonDataHashMap;
    private List<Map<String, String>> ButtonListCollection;
    CommonAlertDialog alert;
    private clickInterface listner;
    CarBikeAdapter adapter;
    String POSId, TxtRating = "", PosName, Town, PosAddress, ImagePath, IsSubCatAvailable, CategoryId, ShopPromotionMsg = "";
    //New Model
    ArrayList<CarBike> ORDERS;
    TextView TxtAmount, TxtNoData, MarqueText;
    //https://www.c-sharpcorner.com/article/marquee-text-in-android-using-android-studio/
    BannerSlider bannerSlider;
    EditText filterText;
    // Spinner SpinSortBy;
    ImageView back_arrow;
    RelativeLayout LyViewCart;
    TextView new_shopname, new_address, AvgRating, itemtitle;
    ImageView new_shopimg, Filter;
    // String STATUS = "1";
    List<Banner> banners = new ArrayList<>();
    Button btn_findcars;
    String POSTypeId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carbike_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);

        PosName = getIntent().getStringExtra("PosName");
        Town = getIntent().getStringExtra("Town");
        PosAddress = getIntent().getStringExtra("Address");
        IsSubCatAvailable = getIntent().getStringExtra("IsSubCatAvailable");
        CategoryId = getIntent().getStringExtra("CategoryId");
        Util.Logcat.e("CategoryId :" + CategoryId);
        ShopPromotionMsg = getIntent().getStringExtra("ShopPromotionMsg");
        TxtRating = getIntent().getStringExtra("AvgRating");
        POSTypeId = getIntent().getStringExtra("POSTypeId");

        toolbar.setTitle(PosName);
        toolbar.setSubtitle(PosAddress);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ORDERS = new ArrayList<>();
        cart = new ArrayList<>();

        POSId = getIntent().getStringExtra("POSId");
        Util.saveData("POSId", POSId, getApplicationContext());
        ImagePath = getIntent().getStringExtra("ImagePath");
        ButtonListCollection = new ArrayList<>();
        listner = this;
        //LyAddtoCart = findViewById(R.id.ly_addtocart);

        Filter = findViewById(R.id.grid);
        //list = findViewById(R.id.list);
        AvgRating = findViewById(R.id.AvgRating);
        AvgRating.setText(TxtRating);
        filterText = findViewById(R.id.search);
        itemtitle = findViewById(R.id.itemtitle);
        bannerSlider = findViewById(R.id.slider);
        TxtNoData = findViewById(R.id.no_item);
        MarqueText = findViewById(R.id.MarqueText);
        MarqueText.setSelected(true);

        /*if (ShopPromotionMsg.isEmpty()) {
            MarqueText.setVisibility(View.GONE);
        } else {
            MarqueText.setText(ShopPromotionMsg);
        }*/

        btn_findcars = findViewById(R.id.btn_findcars);
        if (POSTypeId.equalsIgnoreCase("21")) {
            btn_findcars.setText(getString(R.string.FindBikes));
        }
        TitleRecyclerView = findViewById(R.id.recyclerView);
        TxtAmount = findViewById(R.id.amt);
        back_arrow = findViewById(R.id.back_arrow);
        //TxtCount = findViewById(R.id.itemcount);
        LyViewCart = findViewById(R.id.lycart);
        LyViewCart.setVisibility(View.INVISIBLE);
        listview = findViewById(R.id.listview);
        listview.setExpanded(true);
        alert = new CommonAlertDialog(this);
        TitleRecyclerView.setHasFixedSize(true);
        TitleRecyclerView.addItemDecoration(new DividerDecorator(CarBikeActivity.this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.canScrollHorizontally();
        TitleRecyclerView.setLayoutManager(linearLayoutManager);

        new_shopname = findViewById(R.id.new_shopname);
        new_shopname.setText(PosName);
        new_address = findViewById(R.id.new_address);
        new_address.setText(PosAddress);
        new_shopimg = findViewById(R.id.new_shopimg);

        if (ImagePath.contains(".jpg") || ImagePath.contains(".png") || ImagePath.contains(".PNG") || ImagePath.contains(".jpeg")) {
            try {
                Glide.with(this).load(new URL(ImagePath)).into(new_shopimg);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            new_shopimg.setVisibility(View.INVISIBLE);
        }
        //loadbanner();

        if (IsSubCatAvailable.equalsIgnoreCase("0")) {
            GetCatogory();
        } else {
            GetSubCatogory();
            //SortByDropdown();
        }


        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= 3) {
                    filterText.setEnabled(false);
                    filterText.setClickable(false);
                    Search(String.valueOf(s));

                } else if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Filter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                filterdialog();
            }
        });

        btn_findcars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CarBikeActivity.this.getPackageName().equals("com.gieasesales")) {
                    if (POSTypeId.equalsIgnoreCase("20")) {
                        Intent filterintent = new Intent(CarBikeActivity.this, CarFilterActivity.class);
                        filterintent.putExtra("PosType", "1");
                        startActivity(filterintent);
                    } else {
                        Intent filterintent = new Intent(CarBikeActivity.this, BikeFilterActivity.class);
                        filterintent.putExtra("PosType", "2");
                        startActivity(filterintent);
                    }
                }


            }
        });
        LoadBanner();
    }

    private void LoadBanner() {

        try {
            JSONObject obj = new JSONObject();
            // obj.put("POSId", Util.getData("POSId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET BANNER:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponseNopgrss(this, params.toString(), BANNER, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Util.Logcat.e("GET BANNER:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            JSONArray jsonArray = resobject.optJSONArray("_lstPosBannerResModel");
                            banners.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
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

    @Override
    protected void onResume() {
        super.onResume();
        filterText.setText("");
        //UpdateCart();

    }

    private void GetSubCatogory() {
        ButtonListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("POSId", POSId);
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_MAINCATAGORY, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse :" + response);
                    try {
                        Util.Logcat.e("SUB CAT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetLoadCategoriesOutput2");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            alert.build("Products Not Available", false);
                        } else {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    if (CategoryId.equalsIgnoreCase(imageobject.getString("CategoryId"))) {
                                        ButtonDataHashMap = new HashMap<>();
                                        ButtonDataHashMap.put("CategoryName", imageobject.getString("SubCategoryName"));
                                        ButtonDataHashMap.put("CategoryId", imageobject.getString("SubCategoryId"));
                                        ButtonListCollection.add(ButtonDataHashMap);
                                    }
                                }
                                if (ButtonListCollection.size() > 0) {
                                    addlistitem(ButtonListCollection.get(0).get("CategoryId"), "1", "", ButtonListCollection.get(0).get("CategoryName"));
                                    TitleRecyclerView.setAdapter(new ShowTitleAdapter(CarBikeActivity.this, ButtonListCollection, listner));
                                } else {
                                    alert.build("Products Not Available", false);
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

    private void GetCatogory() {
//0 - getcatagory
//1 - getLoadcat
        ButtonListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("CategoryId", "0");
            obj.put("POSId", POSId);
            Util.Logcat.e("GET MAIN CATAGORY:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_CATEGORY, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {

                        Util.Logcat.e("GET MAIN CATAGORY:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_GetCategory");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            // Util.Logcat.e("EMPTY | Null:::"+ String.valueOf(jsonArray.length()));
                            alert.build("No Data", false);
                            // TxtNoData.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    if (imageobject.getString("IsActive").equalsIgnoreCase("1")) {
                                        ButtonDataHashMap = new HashMap<String, String>();
                                        ButtonDataHashMap.put("CategoryName", imageobject.getString("CategoryName"));
                                        ButtonDataHashMap.put("CategoryId", imageobject.getString("CategoryId"));
                                        Util.Logcat.e(imageobject.getString("CategoryName") + imageobject.getString("CategoryId"));
                                        ButtonListCollection.add(ButtonDataHashMap);
                                    }
                                }
                                if (ButtonListCollection.size() > 0) {
                                    addlistitem(ButtonListCollection.get(0).get("CategoryId"), "2", "", ButtonListCollection.get(0).get("CategoryName"));
                                    TitleRecyclerView.setAdapter(new ShowTitleAdapter(CarBikeActivity.this, ButtonListCollection, listner));
                                } else {
                                    alert.build("No Data", false);
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

    private void Search(String SearchText) {
        ORDERS.clear();
        Util.Logcat.e("SearchText :" + SearchText);
        listview.setVisibility(View.GONE);
        TxtNoData.setVisibility(View.GONE);
        try {

            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("SearchText", SearchText);
            obj.put("SubCategoryId", "0");
            obj.put("CategoryId", "0");

            Util.Logcat.e("SEARCH:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_PRODUCTLIST, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                    } else {
                        alert.build(getString(R.string.server_error), false);
                    }
                    Util.Logcat.e("onError" + message);
                    filterText.setEnabled(true);
                    filterText.setClickable(true);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    filterText.setEnabled(true);
                    filterText.setClickable(true);
                    try {
                        Util.Logcat.e("SEARCH::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            JSONArray jsonArray = resobject.optJSONArray("_lstConsumerPrdListOutputModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                                //   alert.build(getString(R.string.noshop_available),false);
                                TxtNoData.setVisibility(View.VISIBLE);
                                // Util.ShowToast(CarBikeActivity.this, "No Item for this Category");
                            } else {
                                listview.setVisibility(View.VISIBLE);
                                try {
                                    //final ArrayList<Products> orders = new ArrayList<>();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject imageobject = jsonArray.getJSONObject(i);
                                        Log.e("ATTR", ":" + imageobject.getString("IsAttributeAdded") + ":" + imageobject.getString("ProductName"));
                                        ORDERS.add(new CarBike(imageobject.getString("Productcode"), imageobject.getString("ProductId"), imageobject.getString("ProductName"), imageobject.getString("ProductTypeDesc"), imageobject.getString("UOM"), imageobject.getString("IsAllowCustomerReview"), imageobject.getDouble("SellinPrice"), imageobject.getDouble("DiscountValue"), imageobject.getDouble("MRP"), imageobject.getString("FilePath"), imageobject.getString("IsAttributeAdded"), imageobject.getInt("ReviewAvg"), imageobject.getString("DisPlayPriceText"), imageobject.getString("VehicleType"), imageobject.getString("ModelYear"), imageobject.getString("SaleType"), imageobject.getString("BodyType"), imageobject.getString("KMDriven"), imageobject.getString("VehicleModel"), imageobject.getString("FuelType"), imageobject.getString("Ownership"), imageobject.getString("Transmission"), imageobject.getString("City"), imageobject.getString("POSName"), imageobject.getString("POSId"), imageobject.getString("POSLocation"), imageobject.getString("SellingPriceWords")));
                                        // ListCollection.add(DataHashMap);
                                    }
                                    SORT("1");
                                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                            long viewId = view.getId();
                                            if (viewId == R.id.txtadd) {
                                                if (ORDERS.get(position).getIsAttributeAdded().equalsIgnoreCase("0")) {
                                                    AddItem(ORDERS.get(position).getProductid(), ORDERS.get(position).getSellinPrice());
                                                } else {
                                                    Intent product = new Intent(CarBikeActivity.this, ProdDetailActivity.class);
                                                    product.putExtra("ProductId", ORDERS.get(position).getProductid());
                                                    product.putExtra("wishlist", "false");
                                                    startActivity(product);
                                                }
                                            } else if (viewId == R.id.add_wishlist) {
                                                //   AddWishList(orders.get(position).getProductid());
                                                try {
                                                    JSONObject obj = new JSONObject();
                                                    obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
                                                    obj.put("ProductId", ORDERS.get(position).getProductid());
                                                    obj.put("POSId", POSId);
                                                    obj.put("Flag", "1");
                                                    obj.put("OfferPrice", "0");
                                                    Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
                                                    String data = Util.EncryptURL(obj.toString());
                                                    JSONObject params = new JSONObject();
                                                    params.put("Getrequestresponse", data);
                                                    CallApi.postResponse(CarBikeActivity.this, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            if (message.contains("TimeoutError")) {
                                                                alert.build(getString(R.string.timeout_error), false);
                                                                //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                                                            } else {
                                                                alert.build(getString(R.string.server_error), false);
                                                                //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                                                            }
                                                            Util.Logcat.e("onError" + message);
                                                        }

                                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            //Util.Logcat.e("onResponse : " + response);
                                                            try {
                                                                Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                                                                JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                                                                if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                                                    alert.build(resobject.getString("StatusDesc"), true);
                                                                    ImageView mText = (ImageView) view
                                                                            .findViewById(R.id.add_wishlist);
                                                                    mText.setImageDrawable(getDrawable(R.drawable.heart_red));
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

                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    public class Lowtohigh implements Comparator<CarBike> {
        public int compare(CarBike a, CarBike b) {
            return (int) (a.getSellinPrice() - b.getSellinPrice());
        }
    }

    public class Hightolow implements Comparator<CarBike> {
        public int compare(CarBike a, CarBike b) {
            return (int) (b.getSellinPrice() - a.getSellinPrice());
        }
    }

    public class Rating implements Comparator<CarBike> {
        public int compare(CarBike a, CarBike b) {
            return (int) (b.getRatingAverage() - a.getRatingAverage());
        }
    }

    private void addlistitem(String CategoryId, String val, String SearchText, String itemnname) {
        itemtitle.setText(itemnname);
        ORDERS.clear();
        listview.setVisibility(View.GONE);
        TxtNoData.setVisibility(View.GONE);
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("SearchText", SearchText);

            if (IsSubCatAvailable.equalsIgnoreCase("0")) {
                obj.put("SubCategoryId", "0");
                obj.put("CategoryId", CategoryId);
            } else {

                obj.put("SubCategoryId", CategoryId);
                obj.put("CategoryId", "0");

            }

            Util.Logcat.e("GET ITEM LIST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), GET_PRODUCTLIST, new VolleyResponseListener() {
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
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("GET ITEM LIST::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            // SortByDropdown();
                            JSONArray jsonArray = resobject.optJSONArray("_lstConsumerPrdListOutputModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                TxtNoData.setVisibility(View.VISIBLE);
                            } else {
                                listview.setVisibility(View.VISIBLE);
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject imageobject = jsonArray.getJSONObject(i);

                                        ORDERS.add(new CarBike(imageobject.getString("Productcode"), imageobject.getString("ProductId"), imageobject.getString("ProductName"), imageobject.getString("ProductTypeDesc"), imageobject.getString("UOM"), imageobject.getString("IsAllowCustomerReview"), imageobject.getDouble("SellinPrice"), imageobject.getDouble("DiscountValue"), imageobject.getDouble("MRP"), imageobject.getString("FilePath"), imageobject.getString("IsAttributeAdded"), imageobject.getInt("ReviewAvg"), imageobject.getString("DisPlayPriceText"), imageobject.getString("VehicleType"), imageobject.getString("ModelYear"), imageobject.getString("SaleType"), imageobject.getString("BodyType"), imageobject.getString("KMDriven"), imageobject.getString("VehicleModel"), imageobject.getString("FuelType"), imageobject.getString("Ownership"), imageobject.getString("Transmission"), imageobject.getString("City"), imageobject.getString("POSName"), imageobject.getString("POSId"), imageobject.getString("POSLocation"), imageobject.getString("SellingPriceWords")));
                                    }
                                    // Collections.sort(orders, new SortbyRating());
                                    SORT("1");

                                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                            long viewId = view.getId();
                                            if (viewId == R.id.txtadd) {

                                                //   AddItem(ORDERS.get(position).getProductid(), ORDERS.get(position).getSellinPrice());
                                                if (ORDERS.get(position).getIsAttributeAdded().equalsIgnoreCase("0")) {
                                                    AddItem(ORDERS.get(position).getProductid(), ORDERS.get(position).getSellinPrice());
                                                } else {
                                                    Intent product = new Intent(CarBikeActivity.this, ProdDetailActivity.class);
                                                    product.putExtra("wishlist", "false");
                                                    product.putExtra("ProductId", ORDERS.get(position).getProductid());
                                                    startActivity(product);
                                                }
                                            } else if (viewId == R.id.add_wishlist) {
                                                // AddWishList(orders.get(position).getProductid());
                                                try {
                                                    JSONObject obj = new JSONObject();
                                                    obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
                                                    obj.put("ProductId", ORDERS.get(position).getProductid());
                                                    obj.put("POSId", POSId);
                                                    obj.put("Flag", "1");
                                                    obj.put("OfferPrice", "0");
                                                    Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
                                                    String data = Util.EncryptURL(obj.toString());
                                                    JSONObject params = new JSONObject();
                                                    params.put("Getrequestresponse", data);
                                                    CallApi.postResponse(CarBikeActivity.this, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            if (message.contains("TimeoutError")) {
                                                                alert.build(getString(R.string.timeout_error), false);
                                                                //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                                                            } else {
                                                                alert.build(getString(R.string.server_error), false);
                                                                //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                                                            }
                                                            Util.Logcat.e("onError" + message);
                                                        }

                                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            //Util.Logcat.e("onResponse : " + response);
                                                            try {
                                                                Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                                                                JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                                                                if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                                                    alert.build(resobject.getString("StatusDesc"), true);
                                                                    ImageView mText = (ImageView) view
                                                                            .findViewById(R.id.add_wishlist);
                                                                    mText.setImageDrawable(getDrawable(R.drawable.heart_red));
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

                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    private void SORT(String s) {
        if (s.equalsIgnoreCase("1")) {
            Collections.sort(ORDERS, new Lowtohigh());
            // Log.e("SORT", ":" + "LOW TO HIGH");
        } else if (s.equalsIgnoreCase("3")) {
            Collections.sort(ORDERS, new Rating());
            // Log.e("SORT", ":" + "RATING");
        } else {
            // Log.e("SORT", ":" + "HIGH TO LOW");
            Collections.sort(ORDERS, new Hightolow());
        }

        // System.out.println("\nSorted by rollno");
        for (int i = 0; i < ORDERS.size(); i++) {
            System.out.println(ORDERS.get(i));
            Log.e("Selling Price", ":" + ORDERS.get(i).getSellinPrice());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ORDERS.size() > 0) {
                    TxtNoData.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter = new CarBikeAdapter(CarBikeActivity.this, ORDERS);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.registerDataSetObserver(observer);
                } else {
                    alert.build(getString(R.string.NoItemsAvailable), false);
                    TxtNoData.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            }
        });

    }

    private void filterdialog() {
        final Dialog dialog = new Dialog(CarBikeActivity.this);
        dialog.setContentView(R.layout.cutomfilter_sort);
        // set the custom dialog components - text, image and button
        final ListView mainlist = dialog.findViewById(R.id.mainlist);
        final ArrayList<String> listone = new ArrayList<>();
        if (POSTypeId.equalsIgnoreCase("20")) {
            listone.add(getString(R.string.NewCar));
            listone.add(getString(R.string.UsedCar));
        } else {
            listone.add(getString(R.string.NewBike));
            listone.add(getString(R.string.UsedBike));
        }
        listone.add(getString(R.string.CustomerRating));
        listone.add(getString(R.string.Price));
        //  listone.add(getString(R.string.ShopsOffer));
        //  listone.add(getString(R.string.DoorStep));

        mainlist.setAdapter(new ArrayAdapter<String>(CarBikeActivity.this,
                android.R.layout.simple_list_item_1, listone));

        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listone.get(position).equalsIgnoreCase(getString(R.string.NewCar))) {
                    FilterNew(ORDERS, "1");
                }
                if (listone.get(position).equalsIgnoreCase(getString(R.string.UsedCar))) {
                    FilterNew(ORDERS, "2");
                }
                if (listone.get(position).equalsIgnoreCase(getString(R.string.NewBike))) {
                    FilterNew(ORDERS, "1");
                }
                if (listone.get(position).equalsIgnoreCase(getString(R.string.UsedBike))) {
                    FilterNew(ORDERS, "2");
                }
                if (listone.get(position).equalsIgnoreCase(getString(R.string.CustomerRating))) {
                    SORT("3");
                }
                if (listone.get(position).equalsIgnoreCase(getString(R.string.Price))) {
                    SORT("1");
                }
                /*if (listone.get(position).equalsIgnoreCase(getString(R.string.DoorStep))) {
                    SORT("2");
                }*/
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        // dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void FilterNew(ArrayList<CarBike> orders, String value) {
        Log.e("before count", ":" + orders.size());
        List<CarBike> filterResultsData = new ArrayList<CarBike>();
        for (CarBike data : orders) {
            if (data.getSaleType().equalsIgnoreCase(value)) {
                filterResultsData.add(data);
            }
        }
        Log.e("after count", ":" + filterResultsData.size());
        for (int i = 0; i < filterResultsData.size(); i++) {

            System.out.println(orders.get(i));
            Log.e(value, ":" + filterResultsData.get(i).getSaleType());
        }
        if (filterResultsData.size() > 0) {
            TxtNoData.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            adapter = new CarBikeAdapter(CarBikeActivity.this, filterResultsData);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.registerDataSetObserver(observer);
        } else {
            alert.build(getString(R.string.NoItemsAvailable), false);
            TxtNoData.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }

    }

    private void AddItem(String productid, final Double amt) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("ProductId", productid);
            obj.put("Qty", "1");
            obj.put("Upflag", "1");

            obj.put("RefProductAttributeValueId", "0");
            obj.put("ProductAttributeValueId", "0");
            obj.put("ProductAttributeId2", "0");
            obj.put("ProductAttributeValueId2", "0");

            Util.Logcat.e("ADD CART:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), ADD_PRODUCT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
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
                            // UpdateCart();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
                        } else {
                            alert.build(getString(R.string.CartUpdateFailed), false);
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

    DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            // UpdateCart();
        }
    };

    public void UpdateCart() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET CART COUNT:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponseNopgrss(this, params.toString(), GET_CARTCOUNT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Util.Logcat.e("GET CART COUNT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //TxtCount.setText(resobject.getString("CartCount"));
                            TxtAmount.setText(getString(R.string.currency) + resobject.getDouble("NetPay"));
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
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void recyclerviewOnClick(Map<String, String> position) {

        addlistitem(position.get("CategoryId"), "4", "", position.get("CategoryName"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

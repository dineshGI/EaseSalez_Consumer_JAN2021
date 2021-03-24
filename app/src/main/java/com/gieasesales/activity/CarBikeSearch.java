package com.gieasesales.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.Http.CallApi;
import com.gieasesales.Model.CarBike;
import com.gieasesales.R;
import com.gieasesales.adapter.CarBikeAdapter;
import com.gieasesales.adapter.CarBikeSearchAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ExpandableHeightGridView;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ss.com.bannerslider.views.BannerSlider;

import static com.gieasesales.adapter.ProductAdapter.cart;
import static com.gieasesales.utils.Util.GET_CARTCOUNT;
import static com.gieasesales.utils.Util.getData;

public class CarBikeSearch extends AppCompatActivity {

    ExpandableHeightGridView listview;
    CommonAlertDialog alert;
    CarBikeSearchAdapter adapter;
    //String POSId;
    ArrayList<CarBike> ORDERS;
    TextView TxtAmount, TxtCount, TxtViewCart, TxtNoData;
    BannerSlider bannerSlider;
    EditText filterText;
    ImageView back_arrow, btnscan;
    RelativeLayout LyViewCart;
    ImageView Filter;
    String POSTypeId;
    Button btn_findcars;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carbike_search);

        ORDERS = new ArrayList<>();
        cart = new ArrayList<>();

        //  POSId = getIntent().getStringExtra("POSId");
        //   Util.saveData("POSId", POSId, getApplicationContext());
        btnscan = findViewById(R.id.btnscan);
        Filter = findViewById(R.id.grid);
        filterText = findViewById(R.id.search);
        bannerSlider = findViewById(R.id.slider);
        TxtNoData = findViewById(R.id.no_item);
        btn_findcars = findViewById(R.id.btn_findcars);

        TxtAmount = findViewById(R.id.amt);
        back_arrow = findViewById(R.id.back_arrow);
        TxtCount = findViewById(R.id.itemcount);
        TxtViewCart = findViewById(R.id.viewcart);
        LyViewCart = findViewById(R.id.lycart);
        LyViewCart.setVisibility(View.INVISIBLE);
        listview = findViewById(R.id.listview);
        listview.setExpanded(true);
        alert = new CommonAlertDialog(this);
        POSTypeId = getIntent().getStringExtra("POSTypeId");
        if (POSTypeId.equalsIgnoreCase("21")) {
            btn_findcars.setText(getString(R.string.FindBikes));
        }
        filterText.addTextChangedListener(new TextWatcher() {
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

                if (CarBikeSearch.this.getPackageName().equals("com.gieasesales")) {
                    if (POSTypeId.equalsIgnoreCase("20")) {
                        Intent filterintent = new Intent(CarBikeSearch.this, CarFilterActivity.class);
                        filterintent.putExtra("PosType", "1");
                        startActivity(filterintent);
                    } else {
                        Intent filterintent = new Intent(CarBikeSearch.this, BikeFilterActivity.class);
                        filterintent.putExtra("PosType", "2");
                        startActivity(filterintent);
                    }
                }

            }
        });
        LoadData(getIntent().getStringExtra("Data"));

    }

    private void LoadData(String data) {
        try {
            JSONObject jsonobject = new JSONObject(data);

            JSONArray jsonArray = jsonobject.optJSONArray("_lstConsumerPrdListOutputModel");
            if (jsonArray == null || jsonArray.length() == 0) {
                Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                //   alert.build(getString(R.string.noshop_available),false);
                TxtNoData.setVisibility(View.VISIBLE);
                // Util.ShowToast(ExploreActivity.this, "No Item for this Category");
            } else {
                listview.setVisibility(View.VISIBLE);
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject imageobject = jsonArray.getJSONObject(i);
                        Log.e("ATTR", ":" + imageobject.getString("IsAttributeAdded") + ":" + imageobject.getString("ProductName"));
                        ORDERS.add(new CarBike(imageobject.getString("Productcode"), imageobject.getString("ProductId"), imageobject.getString("ProductName"), imageobject.getString("ProductTypeDesc"), imageobject.getString("UOM"), imageobject.getString("IsAllowCustomerReview"), imageobject.getDouble("SellinPrice"), imageobject.getDouble("DiscountValue"), imageobject.getDouble("MRP"), imageobject.getString("FilePath"), imageobject.getString("IsAttributeAdded"), imageobject.getInt("ReviewAvg"), imageobject.getString("DisPlayPriceText"), imageobject.getString("VehicleType"), imageobject.getString("ModelYear"), imageobject.getString("SaleType"), imageobject.getString("BodyType"), imageobject.getString("KMDriven"), imageobject.getString("VehicleModel"), imageobject.getString("FuelType"), imageobject.getString("Ownership"), imageobject.getString("Transmission"), imageobject.getString("City"), imageobject.getString("POSName"), imageobject.getString("POSId"), imageobject.getString("POSLocation"), imageobject.getString("SellingPriceWords")));
                        // ListCollection.add(DataHashMap);
                    }
                    SORT("1");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        TxtCount.setText("");
        filterText.setText("");
        //  UpdateCart();

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
                    adapter = new CarBikeSearchAdapter(CarBikeSearch.this, ORDERS);
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
        final Dialog dialog = new Dialog(CarBikeSearch.this);
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
        // listone.add(getString(R.string.ShopsOffer));
        //  listone.add(getString(R.string.DoorStep));

        mainlist.setAdapter(new ArrayAdapter<String>(CarBikeSearch.this,
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
            adapter = new CarBikeSearchAdapter(CarBikeSearch.this, filterResultsData);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.registerDataSetObserver(observer);
        } else {
            alert.build(getString(R.string.NoItemsAvailable), false);
            TxtNoData.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
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
                        //Util.ErrorLog(CarBikeSearch.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CarBikeSearch.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Util.Logcat.e("GET CART COUNT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            TxtCount.setText(resobject.getString("CartCount"));
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

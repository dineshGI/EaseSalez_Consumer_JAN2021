package com.gieasesales.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.UniversalProdAdapter;
import com.gieasesales.adapter.UniversalShopAdapter;
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

import static com.gieasesales.utils.Util.ADD_PRODUCT;
import static com.gieasesales.utils.Util.GET_CARTCOUNT;
import static com.gieasesales.utils.Util.UNIVERSAL_SEARCH;
import static com.gieasesales.utils.Util.getData;

public class UniversalSearchActivity extends AppCompatActivity {

    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    CommonAlertDialog alert;
    ListView listView;
    UniversalShopAdapter adaptershop;
    UniversalProdAdapter adapterprod;

    TextView shop, product, TxtCount;
    ImageView back_arrow;
    String Type = "", ShopName = "";
    EditText search;
    RelativeLayout LyViewCart;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_search);

        alert = new CommonAlertDialog(this);
        listView = findViewById(R.id.listview);
        search = findViewById(R.id.search);
        shop = findViewById(R.id.shop);
        product = findViewById(R.id.product);
        back_arrow = findViewById(R.id.back_arrow);
        TxtCount = findViewById(R.id.itemcount);
        LyViewCart = findViewById(R.id.lycart);
        LyViewCart.setVisibility(View.VISIBLE);
        ListCollection = new ArrayList<>();
        Type = getIntent().getStringExtra("type");
        ShopName = getIntent().getStringExtra("ShopName");
        search.setText(ShopName);
        Util.Logcat.e("Type :" + Type);
        Util.Logcat.e("ShopName :" + ShopName);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Type.equalsIgnoreCase("1")) {
            shop.setBackground(getDrawable(R.drawable.border_btnbg_blue));
            shop.setTextColor(getColor(R.color.white));
            product.setBackground(getDrawable(R.drawable.border_gray_round));
            product.setTextColor(getColor(R.color.button_gray));
            GetDetail(Type);
        } else {
            shop.setBackground(getDrawable(R.drawable.border_gray_round));
            shop.setTextColor(getColor(R.color.button_gray));
            product.setBackground(getDrawable(R.drawable.border_btnbg_blue));
            product.setTextColor(getColor(R.color.white));
            GetDetail(Type);
        }

        shop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Type = "1";
                shop.setBackground(getDrawable(R.drawable.border_btnbg_blue));
                shop.setTextColor(getColor(R.color.white));
                product.setBackground(getDrawable(R.drawable.border_gray_round));
                product.setTextColor(getColor(R.color.button_gray));
                GetDetail(Type);
            }

        });

        product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shop.setBackground(getDrawable(R.drawable.border_gray_round));
                shop.setTextColor(getColor(R.color.button_gray));
                product.setBackground(getDrawable(R.drawable.border_btnbg_blue));
                product.setTextColor(getColor(R.color.white));
                Type = "2";
                GetDetail(Type);
            }

        });
       /* search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Type.equalsIgnoreCase("1")) {
                    if (adaptershop != null) {
                        adaptershop.getFilter().filter(s);
                    }
                } else {
                    if (adapterprod != null) {
                        adapterprod.getFilter().filter(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });*/
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TxtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AddToCart();
                Intent home = new Intent(UniversalSearchActivity.this, CartActivity.class);
               /* home.putExtra("POSId", POSId);
                home.putExtra("PosName", PosName);
                home.putExtra("PosAddress", PosAddress);
                home.putExtra("ImagePath", ImagePath);
                home.putExtra("TxtRating", TxtRating);*/
                startActivity(home);
                finish();
            }
        });

    }

    private void GetDetail(final String Type) {

        // Util.Logcat.e("Filter Type :" + Type);
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            // obj.put("ProductName", ProductName);
            obj.put("universal_search", getData("universal_search", getApplicationContext()));
            //  obj.put("Latitude", getData("latitude", getApplicationContext()));
            // obj.put("Logitude", getData("longitude", getApplicationContext()));
            obj.put("Latitude", "0");
            obj.put("Logitude", "0");
            // obj.put("CityName", getData("CityName", getApplicationContext()));
            obj.put("CityName", "");
            //obj.put("POSId", "0");
            Util.Logcat.e("SEARCH CLICK:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), UNIVERSAL_SEARCH, new VolleyResponseListener() {
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
                        Util.Logcat.e("SEARCH CLICK:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        JSONArray jsonArray = resobject.optJSONArray("response");

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);

                                if (Type.equalsIgnoreCase(imageobject.getString("type"))) {
                                    DataHashMap = new HashMap<>();
                                    DataHashMap.put("id", imageobject.getString("id"));
                                    DataHashMap.put("Productseoname", imageobject.getString("Productseoname"));
                                    DataHashMap.put("MRP", imageobject.getString("MRP"));
                                    DataHashMap.put("SellingPrice", imageobject.getString("SellingPrice"));
                                    DataHashMap.put("Address", imageobject.getString("Address"));
                                    DataHashMap.put("IsSubCatAvailable", imageobject.getString("IsSubCatAvailable"));
                                    DataHashMap.put("name", imageobject.getString("name"));
                                    DataHashMap.put("type", imageobject.getString("type"));
                                    Util.Logcat.e("type :" + imageobject.getString("type") + ">" + imageobject.getString("name"));
                                    DataHashMap.put("Town", imageobject.getString("Town"));
                                    DataHashMap.put("IsAttributeAdded", imageobject.getString("IsAttributeAdded"));
                                    DataHashMap.put("POSRefId", imageobject.getString("POSRefId"));
                                    DataHashMap.put("FromShopPOSTypeId", imageobject.getString("FromShopPOSTypeId"));
                                    DataHashMap.put("FromShopPosName", imageobject.getString("FromShopPosName"));
                                    DataHashMap.put("FromShopTown", imageobject.getString("FromShopTown"));
                                    DataHashMap.put("image", imageobject.getString("image"));

                                    if (imageobject.getString("image").contains(".jpg") || imageobject.getString("image").contains(".png") || imageobject.getString("image").contains(".PNG") || imageobject.getString("image").contains(".jpeg")) {
                                        DataHashMap.put("imgavailable", "true");
                                    } else {
                                        DataHashMap.put("imgavailable", "false");
                                    }

                                    if (ShopName.equalsIgnoreCase(imageobject.getString("name"))) {
                                        ListCollection.add(0, DataHashMap);
                                        //   Util.Logcat.e("ListCollection :" + DataHashMap);
                                    } else {
                                        ListCollection.add(DataHashMap);
                                    }
                                }
                            }

                            if (ListCollection.size() > 0) {
                                // listView.setAdapter(new UniversalProdAdapter(UniversalSearchActivity.this, ListCollection, Type));
                                if (Type.equalsIgnoreCase("1")) {
                                    adaptershop = new UniversalShopAdapter(UniversalSearchActivity.this, ListCollection);
                                    listView.setAdapter(adaptershop);
                                } else {
                                    adapterprod = new UniversalProdAdapter(UniversalSearchActivity.this, ListCollection);
                                    listView.setAdapter(adapterprod);
                                }

                            } else {
                                if (Type.equalsIgnoreCase("1")) {
                                    alert.build(getString(R.string.NoShops), false);
                                } else {
                                    alert.build(getString(R.string.ProductsNotAvailable), false);
                                }
                                if (adaptershop != null)
                                    adaptershop.notifyDataSetChanged();

                                if (adapterprod != null)
                                    adapterprod.notifyDataSetChanged();
                            }

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    long viewId = view.getId();
                                    if (ListCollection.size() > 0) {
                                        if (viewId == R.id.txtadd) {
                                           /* if (ListCollection.get(position).get("").equalsIgnoreCase("0")) {
                                                AddItem(ORDERS.get(position).getProductid(), ORDERS.get(position).getSellinPrice());
                                            } else {
                                                Intent product = new Intent(ExploreActivity.this, ProdDetailActivity.class);
                                                product.putExtra("ProductId", ORDERS.get(position).getProductid());
                                                product.putExtra("wishlist", "false");
                                                startActivity(product);
                                            }*/
                                            AddItem(ListCollection.get(position).get("id"), ListCollection.get(position).get("POSRefId"));
                                        }
                                    }
                                }
                            });
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

    private void AddItem(final String productid, String POSId) {

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
                        //Util.ErrorLog(ExploreActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ExploreActivity.this, getString(R.string.server_error));
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
                            UpdateCart();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
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
        //  GetDetail(Type);
        TxtCount.setText("");
        //filterText.setText("");
        UpdateCart();

    }

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

                        Util.Logcat.e("GET CART COUNT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            TxtCount.setText(resobject.getString("CartCount"));
                            // TxtAmount.setText(getString(R.string.currency) + resobject.getDouble("NetPay"));
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

package com.gieasesales.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.WishlistAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.ADD_PRODUCT;
import static com.gieasesales.utils.Util.DELETE_WISHLIST;
import static com.gieasesales.utils.Util.GET_CARTCOUNT;
import static com.gieasesales.utils.Util.GET_WISHLIST;
import static com.gieasesales.utils.Util.REMOVE_PRODUCT;
import static com.gieasesales.utils.Util.getData;

public class Wishlist extends AppCompatActivity {

    CommonAlertDialog alert;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    GridView gridview;
    WishlistAdapter adapter;
    ImageView back_arrow;
    RelativeLayout LyViewCart;
    TextView TxtCount, nodata;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Wishlist");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);
        LyViewCart = findViewById(R.id.lycart);
        nodata = findViewById(R.id.nodata);
        LyViewCart.setVisibility(View.VISIBLE);
        gridview = findViewById(R.id.listview);
        back_arrow = findViewById(R.id.back_arrow);
        TxtCount = findViewById(R.id.itemcount);
        ListCollection = new ArrayList<>();
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
                if (Wishlist.this.getPackageName().equals("com.gieasesales")) {
                    Intent home = new Intent(Wishlist.this, CartActivity.class);
                    startActivity(home);
                }

            }
        });
        getwishlist();
        //  GetPosDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCart();
    }

    private void getwishlist() {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", Util.getData("ConsumerId", getApplicationContext()));
            obj.put("ProductId", "0");

            Util.Logcat.e("GET WISHLIST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_WISHLIST, new VolleyResponseListener() {

                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Wishlist.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Wishlist.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("WISHLIST:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetWishListOutputModel");

                        if (jsonArray.length() > 0) {

                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    DataHashMap = new HashMap<>();
                                    DataHashMap.put("POSId", imageobject.getString("POSId"));
                                    DataHashMap.put("PosTypeId", imageobject.getString("PosTypeId"));
                                    DataHashMap.put("ProductId", imageobject.getString("ProductId"));
                                    DataHashMap.put("ProductName", imageobject.getString("ProductName"));
                                    DataHashMap.put("PrdImagePath", imageobject.getString("PrdImagePath"));
                                    DataHashMap.put("MRP", imageobject.getString("MRP"));
                                    DataHashMap.put("DisPlayPriceText", imageobject.getString("DisPlayPriceText"));
                                    DataHashMap.put("IsAttributeAdded", imageobject.getString("IsAttributeAdded"));
                                    DataHashMap.put("SellinPrice", imageobject.getString("SellinPrice"));
                                    DataHashMap.put("DiscountPercentage", imageobject.getString("DiscountPercentage"));
                                    DataHashMap.put("ReviewCnt", imageobject.getString("RatingAverage"));
                                    DataHashMap.put("WishListId", imageobject.getString("WishListId"));
                                    DataHashMap.put("DiscountValue", imageobject.getString("DiscountValue"));
                                    DataHashMap.put("FilePath", imageobject.getString("FilePath"));
                                    DataHashMap.put("SellingPriceWords", imageobject.getString("SellingPriceWords"));
                                    Util.Logcat.e("SellingPriceWords  :" + imageobject.getString("SellingPriceWords"));
                                    ListCollection.add(DataHashMap);

                                }
                                if (ListCollection.size() > 0) {
                                    adapter = new WishlistAdapter(Wishlist.this, ListCollection);
                                    gridview.setAdapter(adapter);
                                } else {
                                    alert.build(getString(R.string.wishlist_empty), false);

                                }
                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        long viewId = view.getId();
                                        if (viewId == R.id.txtadd) {

                                            //AddItem(ListCollection.get(position).get("ProductId"), ListCollection.get(position).get("POSId"));

                                            if (ListCollection.get(position).get("IsAttributeAdded").equalsIgnoreCase("0")) {
                                                AddItem(ListCollection.get(position).get("ProductId"), ListCollection.get(position).get("POSId"));
                                            } else {
                                                if (Wishlist.this.getPackageName().equals("com.gieasesales")) {
                                                    Intent product = new Intent(Wishlist.this, ProdDetailActivity.class);
                                                    product.putExtra("ProductId", ListCollection.get(position).get("ProductId"));
                                                    product.putExtra("POSId", ListCollection.get(position).get("POSId"));
                                                    product.putExtra("wishlist", "true");
                                                    startActivity(product);
                                                }

                                            }

                                        } else if (viewId == R.id.delete) {
                                            DeleteWishList(ListCollection.get(position).get("WishListId"));
                                        }

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //TxtNoData.setVisibility(View.VISIBLE);
                        } else {
                            alert.build(getString(R.string.wishlist_empty), false);
                            gridview.setVisibility(View.GONE);
                            nodata.setVisibility(View.VISIBLE);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
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

    private void AddItem(final String productid, final String POSId) {
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
                        //Util.ErrorLog(Wishlist.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Wishlist.this, getString(R.string.server_error));
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
                        } else if (resobject.getString("Status").equalsIgnoreCase("2")) {
                            DeleteAlert(resobject.getString("StatusDesc") + ". " + getString(R.string.removecart), productid, false);
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

    private void DeleteAlert(String s, final String productid, boolean value) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.deletecart_alert);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.texts);
        LinearLayout success = dialog.findViewById(R.id.success);
        LinearLayout failure = dialog.findViewById(R.id.failure);
        text.setText(s);
        Util.Logcat.e("STATUS>" + value);

        if (value == true) {
            success.setVisibility(View.VISIBLE);
        } else {
            failure.setVisibility(View.VISIBLE);
        }

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Button dialogButton = dialog.findViewById(R.id.yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteItem(productid);
                dialog.dismiss();

            }
        });

        Button NoButton = dialog.findViewById(R.id.no);
        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void DeleteItem(String productid) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", "0");
            obj.put("ProductId", "0");
            obj.put("Qty", "0");
            obj.put("ProductAttributeId", "0");
            obj.put("ProductAttributeValueId", "0");
            obj.put("ProductAttributeId2", "0");
            obj.put("ProductAttributeValueId2", "0");

            Util.Logcat.e("DELETE ITEM:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), REMOVE_PRODUCT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(getActivity(), getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(getActivity(), getString(R.string.server_error));
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
                            alert.build(resobject.getString("StatusDesc"), true);
                            UpdateCart();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
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

    public void UpdateCart() {

        try {
            JSONObject obj = new JSONObject();
            // obj.put("POSId", Util.getData("POSId", getApplicationContext()));
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET CART COUNT::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponseNopgrss(this, params.toString(), GET_CARTCOUNT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Wishlist.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Wishlist.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Util.Logcat.e("GET CART COUNT:::::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            TxtCount.setText(resobject.getString("CartCount"));
                            //TxtAmount.setText(getString(R.string.currency) + resobject.getDouble("NetPay"));
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

    private void DeleteWishList(String WishListId) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("WishListId", WishListId);
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("DELETE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), DELETE_WISHLIST, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(getActivity(), GET_CART.replace(MOBILE_API,"")+":"+getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(getActivity(), GET_CART.replace(MOBILE_API,"")+":"+getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Util.Logcat.e("DELETE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            // JSONArray jsonArray = resobject.optJSONArray("_lstCartGetModel");
                            // POSId = jsonArray.getJSONObject(0).getString("PosID");
                            getwishlist();
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

package com.gieasesales.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ExpandableHeightGridView;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.ReviewAdaptor;
import com.gieasesales.adapter.SpecAdaptor;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.ADD_PRODUCT;
import static com.gieasesales.utils.Util.ADD_WISHLIST;
import static com.gieasesales.utils.Util.GET_ATTR_PRICE;
import static com.gieasesales.utils.Util.GET_CARTCOUNT;
import static com.gieasesales.utils.Util.GET_PROD_DETAIL;
import static com.gieasesales.utils.Util.PROD_REVIEW;
import static com.gieasesales.utils.Util.REMOVE_PRODUCT;
import static com.gieasesales.utils.Util.getData;

public class ProdDetailActivity extends AppCompatActivity implements View.OnClickListener {

    CommonAlertDialog alert;
    ExpandableTextView TxtDescription;
    ExpandableHeightGridView listview, reviewlist;
    //ListView listview;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection, ReviewListCollection;
    TextView des_text, review_text;
    TextView spinnertxt1, spinnertxt2;
    Spinner spinner1, spinner2;
    TextView ProductName, SellingPrice, RatingAverage, ProductDesc, updatedprice;
    TextView des, spec, rev, minus, plus, qty, txtadd, TxtCount, category, tags;
    String POSId, ProductId, Price;
    ImageView add_wishlist;
    LinearLayout des_ly, spec_ly, review_ly, spinnerly1, spinnerly2;
    int count = 1;
    ImageView back_arrow, Imgproduct;
    RatingBar rating;
    Button btn_reviewupdate;
    RelativeLayout LyViewCart;
    EditText ReviewText;
    JSONArray _lstProductParticularsOutput1, _lstProductParticularsOutput4, _lstProductParticularsOutput3, _lstProductParticularsOutput2, _lstProductParticularsOutput5, _lstProductParticularsOutput6;
    String Attribute1Id = "0", ProductAttribute1ValueId = "0", Attribute2Id = "0", ProductAttribute2ValueId = "0";

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proddetail_activity_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Offers");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);
        ListCollection = new ArrayList<>();
        ReviewListCollection = new ArrayList<>();

        rev = findViewById(R.id.rev);
        rev.setOnClickListener(this);
        spec = findViewById(R.id.spec);
        spec.setOnClickListener(this);
        des = findViewById(R.id.des);
        des.setOnClickListener(this);

        back_arrow = findViewById(R.id.back_arrow);
        Imgproduct = findViewById(R.id.img);
        category = findViewById(R.id.category);
        tags = findViewById(R.id.tags);
        LyViewCart = findViewById(R.id.lycart);
        LyViewCart.setVisibility(View.VISIBLE);
        txtadd = findViewById(R.id.txtadd);
        TxtCount = findViewById(R.id.itemcount);
        //review_title = findViewById(R.id.review_title);
        ReviewText = findViewById(R.id.ReviewText);
        ProductName = findViewById(R.id.ProductName);
        SellingPrice = findViewById(R.id.SellingPrice);
        RatingAverage = findViewById(R.id.RatingAverage);
        ProductDesc = findViewById(R.id.ProductDesc);
        add_wishlist = findViewById(R.id.add_wishlist);
        updatedprice = findViewById(R.id.updatedprice);
        rating = findViewById(R.id.rating);
        qty = findViewById(R.id.qty);
        btn_reviewupdate = findViewById(R.id.btn_reviewupdate);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        add_wishlist.setOnClickListener(this);
        btn_reviewupdate.setOnClickListener(this);
        txtadd.setOnClickListener(this);

        spinnertxt1 = findViewById(R.id.spinnertxt1);
        spinnertxt2 = findViewById(R.id.spinnertxt2);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        spinnerly1 = findViewById(R.id.spinnerly1);
        spinnerly2 = findViewById(R.id.spinnerly2);

        _lstProductParticularsOutput1 = new JSONArray();
        _lstProductParticularsOutput2 = new JSONArray();
        _lstProductParticularsOutput3 = new JSONArray();
        _lstProductParticularsOutput4 = new JSONArray();
        _lstProductParticularsOutput5 = new JSONArray();
        _lstProductParticularsOutput6 = new JSONArray();


        review_ly = findViewById(R.id.review_ly);
        des_ly = findViewById(R.id.des_ly);
        spec_ly = findViewById(R.id.spec_ly);

        des_text = findViewById(R.id.des_text);
        review_text = findViewById(R.id.review_text);
        listview = findViewById(R.id.listview);
        reviewlist = findViewById(R.id.reviewlist);
        listview.setExpanded(true);
        reviewlist.setExpanded(true);

        // dummdata();
        ProductId = getIntent().getStringExtra("ProductId");

        // POSId = getData("POSId", getApplicationContext());

        if (getIntent().getStringExtra("wishlist").equalsIgnoreCase("true")) {
            POSId = getIntent().getStringExtra("POSId");
            Util.Logcat.e("wishlist :" + "true");
        } else {
            Util.Logcat.e("wishlist :" + "false");
            POSId = getData("POSId", getApplicationContext());
        }

        TxtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AddToCart();
                Intent home = new Intent(ProdDetailActivity.this, CartActivity.class);
                home.putExtra("POSId", POSId);
                startActivity(home);
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                // SpinShopType = String.valueOf(SpinPaymentMode.getSelectedItemPosition() + 1);
                Util.Logcat.e("spinner1 :" + spinner1.getSelectedItem().toString());
                try {
                    for (int j = 0; j < _lstProductParticularsOutput4.length(); j++) {
                        JSONObject object = _lstProductParticularsOutput4.getJSONObject(j);
                        if (spinner1.getSelectedItem().toString().equalsIgnoreCase(object.getString("AttributeValue"))) {
                            Util.Logcat.e("AttributeId :" + object.getString("AttributeId"));
                            Util.Logcat.e("AttributeValueId :" + object.getString("AttributeValueId"));
                            Attribute1Id = object.getString("AttributeId");
                            ProductAttribute1ValueId = object.getString("AttributeValueId");
                            GetPriceDetails(Attribute1Id, ProductAttribute1ValueId, Attribute2Id, ProductAttribute2ValueId);
                            break;
                        }
                    }
                } catch (JSONException e) {
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                // SpinShopType = String.valueOf(SpinPaymentMode.getSelectedItemPosition() + 1);
                Util.Logcat.e("spinner1 :" + spinner2.getSelectedItem().toString());
                try {
                    for (int j = 0; j < _lstProductParticularsOutput4.length(); j++) {
                        JSONObject object = _lstProductParticularsOutput4.getJSONObject(j);
                        if (spinner2.getSelectedItem().toString().equalsIgnoreCase(object.getString("AttributeValue"))) {
                            Util.Logcat.e("AttributeId :" + object.getString("AttributeId"));
                            Util.Logcat.e("AttributeValueId :" + object.getString("AttributeValueId"));
                            Attribute2Id = object.getString("AttributeId");
                            ProductAttribute2ValueId = object.getString("AttributeValueId");
                            GetPriceDetails(Attribute1Id, ProductAttribute1ValueId, Attribute2Id, ProductAttribute2ValueId);

                            break;
                        }
                    }
                } catch (JSONException e) {
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetDetails(ProductId);
        //  GetPriceDetails();
        UpdateCart();

    }

    private void GetDetails(String ProductId) {

        try {
            JSONObject obj = new JSONObject();
            //obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ProductId", ProductId);
            obj.put("POSId", POSId);
            obj.put("ProductSeoName", "");

            Util.Logcat.e("DETAIL:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_PROD_DETAIL, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("DETAIL:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

                            //Add default details
                            _lstProductParticularsOutput1 = resobject.optJSONArray("_lstProductParticularsOutput1");

                            if (_lstProductParticularsOutput1.length() > 0) {

                                ProductName.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("ProductName"));
                                SellingPrice.setText(getString(R.string.currency) + _lstProductParticularsOutput1.getJSONObject(0).getString("DisPlayPriceText"));
                                RatingAverage.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("RatingAverage") + " (" + _lstProductParticularsOutput1.getJSONObject(0).getString("ReviewCnt") + " Customer Reviews)");
                                Price = _lstProductParticularsOutput1.getJSONObject(0).getString("SellingPrice");

                                category.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("CategoryName"));
                                tags.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("MetaTags"));

                                String img = _lstProductParticularsOutput1.getJSONObject(0).getString("FilePath");
                                if (img.contains(".jpg") || img.contains(".png") || img.contains(".PNG") || img.contains(".jpeg")) {
                                    Glide.with(ProdDetailActivity.this).load(img).into(Imgproduct);
                                } else {
                                    Imgproduct.setVisibility(View.GONE);
                                    // Imgproduct.setImageDrawable(getResources().getDrawable(R.drawable.no_product));
                                }

                                //spinner title text
                                _lstProductParticularsOutput6 = resobject.optJSONArray("_lstProductParticularsOutput6");
                                _lstProductParticularsOutput4 = resobject.optJSONArray("_lstProductParticularsOutput4");

                                Util.Logcat.e("Lenght:" + _lstProductParticularsOutput6.length());
                                if (_lstProductParticularsOutput6.length() > 0) {
                                    spinnertxt1.setText(_lstProductParticularsOutput6.getJSONObject(0).getString("AttributeName"));
                                    Util.Logcat.e("AttributeName 1:" + _lstProductParticularsOutput6.getJSONObject(0).getString("AttributeName"));
                                    spinnerly1.setVisibility(View.VISIBLE);

                                    final List<String> spinnerlistSub = new ArrayList<>();
                                    // spinnerlistSub.add("Select " + spinnertxt1.getText().toString());
                                    for (int j = 0; j < _lstProductParticularsOutput4.length(); j++) {
                                        JSONObject object = _lstProductParticularsOutput4.getJSONObject(j);
                                        if (spinnertxt1.getText().toString().equalsIgnoreCase(object.getString("AttributeName"))) {
                                            spinnerlistSub.add(object.getString("AttributeValue"));
                                        }
                                    }
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                                            (ProdDetailActivity.this, R.layout.spinner_filtertv,
                                                    spinnerlistSub);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                            .simple_spinner_dropdown_item);
                                    spinner1.setAdapter(spinnerArrayAdapter);

                                }
                                if (_lstProductParticularsOutput6.length() > 1) {
                                    spinnertxt2.setText(_lstProductParticularsOutput6.getJSONObject(1).getString("AttributeName"));
                                    spinnerly2.setVisibility(View.VISIBLE);

                                    final List<String> spinnerlistSub = new ArrayList<>();
                                    // spinnerlistSub.add("Select " + spinnertxt1.getText().toString());
                                    for (int j = 0; j < _lstProductParticularsOutput4.length(); j++) {
                                        JSONObject object = _lstProductParticularsOutput4.getJSONObject(j);
                                        if (spinnertxt2.getText().toString().equalsIgnoreCase(object.getString("AttributeName"))) {
                                            spinnerlistSub.add(object.getString("AttributeValue"));
                                        }
                                    }
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                                            (ProdDetailActivity.this, R.layout.spinner_filtertv,
                                                    spinnerlistSub);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                            .simple_spinner_dropdown_item);
                                    spinner2.setAdapter(spinnerArrayAdapter);
                                    if (spinnerlistSub.size() == 0) {
                                        spinnerly2.setVisibility(View.GONE);
                                    }

                                }

                                //Description
                                _lstProductParticularsOutput3 = resobject.optJSONArray("_lstProductParticularsOutput3");
                                String text = _lstProductParticularsOutput3.getJSONObject(0).getString("ProductDesc");
                                ProductDesc.setText(HtmlCompat.fromHtml(text, 0));

                                //Specification
                                _lstProductParticularsOutput2 = resobject.optJSONArray("_lstProductParticularsOutput2");
                                if (_lstProductParticularsOutput2.length() > 0) {
                                    for (int j = 0; j < _lstProductParticularsOutput2.length(); j++) {
                                        JSONObject object = _lstProductParticularsOutput2.getJSONObject(j);
                                        HashMap<String, String> DataHashMap = new HashMap<>();
                                        DataHashMap.put("AttributeName", object.getString("AttributeName"));
                                        DataHashMap.put("AttributeValue", object.getString("AttributeValue"));
                                        ListCollection.add(DataHashMap);
                                    }

                                    listview.setAdapter(new SpecAdaptor(ProdDetailActivity.this, ListCollection));
                                }
                                _lstProductParticularsOutput5 = resobject.optJSONArray("_lstProductParticularsOutput5");
                                if (_lstProductParticularsOutput5.length() > 0) {
                                    for (int j = 0; j < _lstProductParticularsOutput5.length(); j++) {
                                        JSONObject object = _lstProductParticularsOutput5.getJSONObject(j);
                                        HashMap<String, String> DataHashMap = new HashMap<>();
                                        DataHashMap.put("ConsumerName", object.getString("ConsumerName"));
                                        DataHashMap.put("ReviewText", object.getString("ReviewText"));
                                        DataHashMap.put("ReplyText", object.getString("ReplyText"));
                                        DataHashMap.put("ReviewDT", object.getString("ReviewDT"));
                                        DataHashMap.put("Title", object.getString("Title"));
                                        DataHashMap.put("Rating", object.getString("Rating"));
                                        ReviewListCollection.add(DataHashMap);
                                    }

                                    reviewlist.setAdapter(new ReviewAdaptor(ProdDetailActivity.this, ReviewListCollection));
                                }
                                // alert.build(resobject.getString("StatusDesc"));
                                // finish();
                            } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                                alert.build(resobject.getString("StatusDesc"), false);
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

    private void GetPriceDetails(String Attribute1Id, String ProductAttribute1ValueId, String Attribute2Id, String ProductAttribute2ValueId) {

        try {
            JSONObject obj = new JSONObject();
            //obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ProductId", ProductId);
            obj.put("POSId", POSId);
            obj.put("Attribute1Id", Attribute1Id);
            obj.put("ProductAttribute1ValueId", ProductAttribute1ValueId);
            obj.put("Attribute2Id", Attribute2Id);
            obj.put("ProductAttribute2ValueId", ProductAttribute2ValueId);

            Util.Logcat.e("PRICE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_ATTR_PRICE, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("PRICE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //  alert.build(resobject.getString("StatusDesc"));
                            updatedprice.setText(getString(R.string.currency) + resobject.getString("SellingPrice"));
                            Price = resobject.getString("SellingPrice");

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

    private void AddReview() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ProductId", ProductId);
            obj.put("POSId", POSId);
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("Title", "");
            obj.put("ReviewText", ReviewText.getEditableText().toString());
            obj.put("Rating", rating.getRating());
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));

            Util.Logcat.e("ADD REVIEW:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), PROD_REVIEW, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ReviewActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("ADD REVIEW:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //review_title.setText("");
                            ReviewText.setText("");
                            rating.setRating(0);
                            alert.build(resobject.getString("StatusDesc"), true);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_wishlist:
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
                    obj.put("ProductId", ProductId);
                    obj.put("POSId", POSId);
                    obj.put("Flag", "1");
                    obj.put("OfferPrice", "0");
                    Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
                    String data = Util.EncryptURL(obj.toString());
                    JSONObject params = new JSONObject();
                    params.put("Getrequestresponse", data);
                    CallApi.postResponse(ProdDetailActivity.this, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
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

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(JSONObject response) {
                            //Util.Logcat.e("onResponse : " + response);
                            try {
                                Util.Logcat.e("ADD WISHLIST:::" + Util.Decrypt(response.getString("Postresponse")));
                                JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                                if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                    alert.build(resobject.getString("StatusDesc"), true);
                                    add_wishlist.setImageDrawable(getDrawable(R.drawable.heart_red));
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


                break;
            case R.id.minus:
                count--;
                if (count > 0) {
                    qty.setText("" + count);
                    //  AddItem(String.valueOf(count));
                } else {
                    count = 1;
                    //  AddItem(String.valueOf(count));
                }
                break;
            case R.id.plus:
                count++;
                qty.setText("" + count);
                //AddItem(String.valueOf(count));
                break;
            case R.id.des:

                des.setBackgroundColor(getResources().getColor(R.color.white));
                des.setTextColor(getResources().getColor(R.color.button_gray));

                spec.setBackgroundColor(getResources().getColor(R.color.white));
                spec.setTextColor(getResources().getColor(R.color.button_gray));

                rev.setBackgroundColor(getResources().getColor(R.color.white));
                rev.setTextColor(getResources().getColor(R.color.button_gray));

                des_ly.setVisibility(View.GONE);
                spec_ly.setVisibility(View.GONE);
                review_ly.setVisibility(View.GONE);

                des.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                des.setTextColor(getResources().getColor(R.color.white));
                des_ly.setVisibility(View.VISIBLE);

                break;
            case R.id.spec:
                des.setBackgroundColor(getResources().getColor(R.color.white));
                des.setTextColor(getResources().getColor(R.color.button_gray));

                spec.setBackgroundColor(getResources().getColor(R.color.white));
                spec.setTextColor(getResources().getColor(R.color.button_gray));

                rev.setBackgroundColor(getResources().getColor(R.color.white));
                rev.setTextColor(getResources().getColor(R.color.button_gray));

                des_ly.setVisibility(View.GONE);
                spec_ly.setVisibility(View.GONE);
                review_ly.setVisibility(View.GONE);

                spec.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                spec.setTextColor(getResources().getColor(R.color.white));
                spec_ly.setVisibility(View.VISIBLE);
                break;
            case R.id.rev:
                des.setBackgroundColor(getResources().getColor(R.color.white));
                des.setTextColor(getResources().getColor(R.color.button_gray));

                spec.setBackgroundColor(getResources().getColor(R.color.white));
                spec.setTextColor(getResources().getColor(R.color.button_gray));

                rev.setBackgroundColor(getResources().getColor(R.color.white));
                rev.setTextColor(getResources().getColor(R.color.button_gray));

                des_ly.setVisibility(View.GONE);
                spec_ly.setVisibility(View.GONE);
                review_ly.setVisibility(View.GONE);

                rev.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                rev.setTextColor(getResources().getColor(R.color.white));
                review_ly.setVisibility(View.VISIBLE);
                break;
            case R.id.txtadd:
                AddItem(String.valueOf(count));
                break;
            case R.id.btn_reviewupdate:

                if (ReviewText.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.EnterReview), false);
                } else {
                    AddReview();
                }
                break;
            default:
                break;
        }
    }

    private void AddItem(String Quantity) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("PosID", POSId);
            obj.put("ProductId", ProductId);
            obj.put("Qty", Quantity);
            obj.put("Upflag", "2");
            obj.put("RefProductAttributeValueId", Attribute1Id);
            obj.put("ProductAttributeValueId", ProductAttribute1ValueId);
            obj.put("ProductAttributeId2", Attribute2Id);
            obj.put("ProductAttributeValueId2", ProductAttribute2ValueId);

            Util.Logcat.e("ADD CART:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), ADD_PRODUCT, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(CartActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(CartActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("ADD ITEM:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            // alert.build(resobject.getString("StatusDesc"));
                            //GetCartitems();
                            UpdateCart();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(getString(R.string.CartUpdateFailed), false);
                        }else if (resobject.getString("Status").equalsIgnoreCase("2")) {
                            DeleteAlert(resobject.getString("StatusDesc") + ". " + getString(R.string.removecart), ProductId, false);
                        }else {
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
                    listview.setEnabled(true);
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
            //obj.put("POSId", Util.getData("POSId", getApplicationContext()));
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET CART COUNT:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            // CallApi.postResponseNopgrss(this, params.toString(), GET_CART, new VolleyResponseListener() {
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

                            //TxtAmount.setText(getString(R.string.currency) + resobject.getDouble("NetPay"));
                           /* JSONArray jsonArray = resobject.optJSONArray("_lstCartGetModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                                LyAddtoCart.setVisibility(View.GONE);
                            } else {
                                LyAddtoCart.setVisibility(View.GONE);
                                // TxtCount.setText(resobject.getString("TotalQty") + " Items");
                                TxtCount.setText(resobject.getString("CartCount"));
                                TxtAmount.setText(getString(R.string.currency) + resobject.getDouble("NetPay"));
                            }*/

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

package com.gieasesales.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.GalleryAdaptor;
import com.gieasesales.adapter.Pager;
import com.gieasesales.adapter.SpecAdaptor;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ContactSellerAlertDialog;
import com.gieasesales.utils.ExpandableHeightGridView;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.ADD_WISHLIST;
import static com.gieasesales.utils.Util.GET_PROD_DETAIL;
import static com.gieasesales.utils.Util.GET_STORES;
import static com.gieasesales.utils.Util.getData;

public class CarBikeDetail extends AppCompatActivity implements View.OnClickListener {

    CommonAlertDialog alert;
    ContactSellerAlertDialog Contactalert;

    //ListView listview;
    //  private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection, GalleryListCollection;
    String ProductId, POSId, ProductSEOURL;
    ExpandableHeightGridView listview;
    GridView gallerylist;
    ImageView back_arrow, prev, next, add_wishlist, share;
    TextView TxtCount, ProductName,SellingPriceWords, SellingPrice, MRP, ProductDesc, KMDriven, FuelType, OwnerShip, ModelYear, Transmission;
    String Price;
    RelativeLayout LyViewCart;
    JSONArray _lstProductParticularsOutput1, _lstProductParticularsOutput4, _lstProductParticularsOutput3, _lstProductParticularsOutput2, _lstProductParticularsOutput5, _lstProductParticularsOutput6, _lstProductParticularsOutput7;
    TextView des, spec, gal, City, EMI;
    LinearLayout des_ly, spec_ly, review_ly, QuotePrice, ContactSeller, Im_Interested;
    EditText ReviewText;
    ViewPager viewPager;
    Pager pager;
    ArrayList<String> imageModelsArray = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carbike_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);
        Contactalert = new ContactSellerAlertDialog(this);
        viewPager = findViewById(R.id.pager);
        City = findViewById(R.id.City);
        EMI = findViewById(R.id.emi);
        LyViewCart = findViewById(R.id.lycart);
        LyViewCart.setVisibility(View.INVISIBLE);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        ListCollection = new ArrayList<>();
        GalleryListCollection = new ArrayList<>();
        QuotePrice = findViewById(R.id.QuotePrice);
        ContactSeller = findViewById(R.id.ContactSeller);
        Im_Interested = findViewById(R.id.Im_Interested);
        add_wishlist = findViewById(R.id.add_wishlist);
        share = findViewById(R.id.share);
        back_arrow = findViewById(R.id.back_arrow);
        ReviewText = findViewById(R.id.ReviewText);
        TxtCount = findViewById(R.id.itemcount);
        ProductName = findViewById(R.id.ProductName);
        SellingPriceWords = findViewById(R.id.SellingPriceWords);
        SellingPrice = findViewById(R.id.SellingPrice);
        MRP = findViewById(R.id.MRP);
        KMDriven = findViewById(R.id.KMDriven);
        Transmission = findViewById(R.id.Transmission);
        ModelYear = findViewById(R.id.ModelYear);
        FuelType = findViewById(R.id.FuelType);
        OwnerShip = findViewById(R.id.OwnerShip);
        ProductDesc = findViewById(R.id.ProductDesc);
        listview = findViewById(R.id.listview);
        gallerylist = findViewById(R.id.gallery);

        _lstProductParticularsOutput1 = new JSONArray();
        _lstProductParticularsOutput2 = new JSONArray();
        _lstProductParticularsOutput3 = new JSONArray();
        _lstProductParticularsOutput4 = new JSONArray();
        _lstProductParticularsOutput5 = new JSONArray();
        _lstProductParticularsOutput6 = new JSONArray();
        _lstProductParticularsOutput7 = new JSONArray();

        gal = findViewById(R.id.rev);
        gal.setOnClickListener(this);
        spec = findViewById(R.id.spec);
        spec.setOnClickListener(this);
        des = findViewById(R.id.des);
        des.setOnClickListener(this);
        add_wishlist.setOnClickListener(this);
        Im_Interested.setOnClickListener(this);
        share.setOnClickListener(this);
        QuotePrice.setOnClickListener(this);
        ContactSeller.setOnClickListener(this);

        review_ly = findViewById(R.id.review_ly);
        des_ly = findViewById(R.id.des_ly);
        spec_ly = findViewById(R.id.spec_ly);

        // dummdata();

        ProductId = getIntent().getStringExtra("ProductId");
        //POSId = getData("POSId", getApplicationContext());
        if (getIntent().getStringExtra("wishlist").equalsIgnoreCase("true")) {
            POSId = getIntent().getStringExtra("POSId");
        } else {
            POSId = getData("POSId", getApplicationContext());
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetDetails(ProductId);
        //  GetPriceDetails();
        //  UpdateCart();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(getItemofviewpager(-1), true);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(getItemofviewpager(+1), true);

            }
        });

    }

    private int getItemofviewpager(int i) {
        return viewPager.getCurrentItem() + i;
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
                    } else {
                        alert.build(getString(R.string.server_error), false);
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Util.Logcat.e("DETAIL:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

                            //Add default details
                            _lstProductParticularsOutput1 = resobject.optJSONArray("_lstProductParticularsOutput1");

                            if (_lstProductParticularsOutput1.length() > 0) {

                                ProductName.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("ProductName"));
                                SellingPriceWords.setText(getString(R.string.currency) +_lstProductParticularsOutput1.getJSONObject(0).getString("SellingPriceWords"));
                                KMDriven.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("KMDriven"));
                                EMI.setText(getString(R.string.emi_start) + " " + getString(R.string.currency) + _lstProductParticularsOutput1.getJSONObject(0).getString("EMI"));
                                City.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("POSName") + ", " + _lstProductParticularsOutput1.getJSONObject(0).getString("POSLocation"));
                                FuelType.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("FuelType"));
                                OwnerShip.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("OwnerShip"));
                                ModelYear.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("ModelYear"));
                                Transmission.setText(_lstProductParticularsOutput1.getJSONObject(0).getString("Transmission"));
                                SellingPrice.setText(getString(R.string.currency) + _lstProductParticularsOutput1.getJSONObject(0).getString("SellingPrice"));
                                MRP.setText(getString(R.string.currency) + _lstProductParticularsOutput1.getJSONObject(0).getString("MRP"));
                                MRP.setPaintFlags(MRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                Price = _lstProductParticularsOutput1.getJSONObject(0).getString("SellingPrice");
                                ProductSEOURL = _lstProductParticularsOutput1.getJSONObject(0).getString("ProductSEOURL");

                            }
                            //spinner title text
                            _lstProductParticularsOutput6 = resobject.optJSONArray("_lstProductParticularsOutput6");

                            //Gallery Image
                            _lstProductParticularsOutput7 = resobject.optJSONArray("_lstProductParticularsOutput7");

                            //PrdImage,ImageName
                            for (int i = 0; i < _lstProductParticularsOutput7.length(); i++) {
                                JSONObject imageobject = _lstProductParticularsOutput7.getJSONObject(i);
                                imageModelsArray.add(imageobject.getString("PrdImage"));
                                Util.Logcat.e("PrdImage :" + imageobject.getString("PrdImage"));
                                HashMap<String, String> DataHashMap = new HashMap<>();
                                DataHashMap.put("PrdImage", imageobject.getString("PrdImage"));
                                GalleryListCollection.add(DataHashMap);
                            }
                            if (GalleryListCollection.size() > 0) {
                                Util.Logcat.e("GalleryListCollection :" + GalleryListCollection.size());
                                gallerylist.setAdapter(new GalleryAdaptor(CarBikeDetail.this, GalleryListCollection));
                            }
                            pager = new Pager(CarBikeDetail.this, imageModelsArray);
                            viewPager.setAdapter(pager);

                            _lstProductParticularsOutput4 = resobject.optJSONArray("_lstProductParticularsOutput4");

                            // Util.Logcat.e("Lenght:" + _lstProductParticularsOutput6.length());

                            //Description
                            _lstProductParticularsOutput3 = resobject.optJSONArray("_lstProductParticularsOutput3");
                            if (_lstProductParticularsOutput3.length() > 0) {
                                String text = _lstProductParticularsOutput3.getJSONObject(0).getString("ProductDesc");
                                ProductDesc.setText(HtmlCompat.fromHtml(text, 0));
                            }
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

                                listview.setAdapter(new SpecAdaptor(CarBikeDetail.this, ListCollection));
                            }
                            _lstProductParticularsOutput5 = resobject.optJSONArray("_lstProductParticularsOutput5");


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

            case R.id.ContactSeller:
                GetStoreDetails(POSId);
                break;

            case R.id.QuotePrice:
                build();
                break;

            case R.id.Im_Interested:
                AddWishList("2", "0");
                break;

            case R.id.share:

                PackageManager pm = getPackageManager();

                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, ProductSEOURL);
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(CarBikeDetail.this, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.add_wishlist:
                AddWishList("1", "0");
                break;

            case R.id.des:

                des.setBackgroundColor(getResources().getColor(R.color.white));
                des.setTextColor(getResources().getColor(R.color.button_gray));

                spec.setBackgroundColor(getResources().getColor(R.color.white));
                spec.setTextColor(getResources().getColor(R.color.button_gray));

                gal.setBackgroundColor(getResources().getColor(R.color.white));
                gal.setTextColor(getResources().getColor(R.color.button_gray));

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

                gal.setBackgroundColor(getResources().getColor(R.color.white));
                gal.setTextColor(getResources().getColor(R.color.button_gray));

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

                gal.setBackgroundColor(getResources().getColor(R.color.white));
                gal.setTextColor(getResources().getColor(R.color.button_gray));

                des_ly.setVisibility(View.GONE);
                spec_ly.setVisibility(View.GONE);
                review_ly.setVisibility(View.GONE);


                gal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gal.setTextColor(getResources().getColor(R.color.white));
                review_ly.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    public void build() {
        final Dialog dialog = new Dialog(CarBikeDetail.this);
        dialog.setContentView(R.layout.quoteprice);
        // set the custom dialog components - text, image and button
        final EditText Amount = dialog.findViewById(R.id.Amount);
        final ImageView close = dialog.findViewById(R.id.close);

        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Amount.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_amount), false);
                } else {
                    AddWishList("3", Amount.getEditableText().toString());
                    dialog.dismiss();
                }

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void GetStoreDetails(String posId) {

        try {
            JSONObject obj = new JSONObject();
            // obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerId", "0");
            obj.put("POSId", posId);
            Util.Logcat.e("GET STORE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_STORES, new VolleyResponseListener() {
                @Override
                public void onError(String message) {

                    Util.Logcat.e("onError" + message);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("GET STORE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobjects = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        JSONArray jsonArray = resobjects.optJSONArray("_lstPOSDetailsOutputModels");
                        JSONObject resobject = jsonArray.getJSONObject(0);
                        String Hours = resobject.getString("DeliveryFromTime") + "-" + resobject.getString("DeliveryToTime");
                        Contactalert.build(resobject.getString("PosName"), resobject.getString("PhoneNo"), resobject.getString("Address"), Hours, ProductId, POSId, resobject.getString("Pincode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AddWishList(String Flag, String OfferPrice) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("ProductId", ProductId);
            obj.put("POSId", POSId);
            obj.put("Flag", Flag);
            obj.put("OfferPrice", OfferPrice);
            Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(CarBikeDetail.this, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
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

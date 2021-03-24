package com.gieasesales.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.CartAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.HTMLAlertDialog;
import com.gieasesales.utils.Util;
import com.gocashfree.cashfreesdk.CFPaymentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.ADD_PRODUCT;
import static com.gieasesales.utils.Util.APPLY_COUPON;
import static com.gieasesales.utils.Util.APP_ID;
import static com.gieasesales.utils.Util.CALCULATE_DELIVERY;
import static com.gieasesales.utils.Util.GET_ADDRESS;
import static com.gieasesales.utils.Util.GET_CART;
import static com.gieasesales.utils.Util.GET_PAYMENTMODE;
import static com.gieasesales.utils.Util.GET_STORES;
import static com.gieasesales.utils.Util.PAYMENT;
import static com.gieasesales.utils.Util.PLACE_ORDER;
import static com.gieasesales.utils.Util.REMOVE_PRODUCT;
import static com.gieasesales.utils.Util.STAGE;
import static com.gieasesales.utils.Util.UPDATECHARGES;
import static com.gieasesales.utils.Util.getData;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_VENDOR_SPLIT;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    CommonAlertDialog alert;
    HTMLAlertDialog htmlalert;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection, Payment;
    CartAdapter adapter;
    ListView listview;
    String PosName, POSId = "", PosAddress, StrPaymentMode = "", ImagePath, StringTotalAmt = "", DeliveryTypeId = "";
    String cus_name = "", cus_address = "", cus_address2 = "", landmark = "", cus_city = "", cus_pincode = "", cus_mobileno = "", ConsumerAddressId = "", DefaultAddress = "";
    TextView TxtQuantity, TxtTotalAmt, TxtAmt, TxtDate, to_txt;
    //TxtDate;
    ImageView ImgCalender, cal_to, ShopImg, btn_img_edit;
    Button BtnPlaceOrder, BtnAddMore, shopnow, btnapply;
    Spinner SpinPaymentMode, SpinDelivery_mode;
    TextView ed_address, inclusive_gst, exclusive_gst, delivery_amt;
    LinearLayout ly_pickup, ly_delivery, ly_option, ly_shopdetails, LyTop, LyBottom, ly_address, lyscheduled;
    LinearLayout ly_inclgst, ly_excgst, lyshownow, ly_deliverycharge, ly_coupon, ly_deliverynow;
    RadioGroup radioGroup, Radiodelivery;
    RadioButton pickup, delivery, deliver_now, scheduled;
    TextView mobileno, working_hours, termstxt, btn_addaddress, btn_changeaddress, new_shopname, AvgRating;
    //shop_address, contact_person
    CheckBox termscheckbox;
    Toolbar toolbar;
    ImageView back_arrow;
    double DeliveryCharge = 0.0;
    String DeliveryDistance = "0", POSLatitude = "", POSLongitude = "", Latitude = "", Longitude = "";
    String OrderAcceptanceFromTime = "", OrderAcceptanceToTime = "", DeliveryFromTime = "", DeliveryToTime = "";
    private long mLastClickTimeListViewItem = 0;
    EditText coupon, deliveryinstruction;
    String CouponAmount = "0.0";
    TextView TxtDiscount, TxtYouSave;
    String FromTime = "", ToTime = "", DeliveryPolicyId = "", DistanceNetPay = "", TotalWeight = "";
    boolean finish = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        alert = new CommonAlertDialog(this);
        htmlalert = new HTMLAlertDialog(this);
        listview = findViewById(R.id.listview);
        back_arrow = findViewById(R.id.back_arrow);
        ly_inclgst = findViewById(R.id.ly_inclgst);
        ly_excgst = findViewById(R.id.ly_excgst);
        lyshownow = findViewById(R.id.lyshownow);
        ly_deliverycharge = findViewById(R.id.ly_deliverycharge);
        termscheckbox = findViewById(R.id.termscheckbox);
        btn_img_edit = findViewById(R.id.btn_img_edit);
        SpinDelivery_mode = findViewById(R.id.delivery_mode);
        inclusive_gst = findViewById(R.id.inclusive_gst);
        exclusive_gst = findViewById(R.id.exclusive_gst);
        delivery_amt = findViewById(R.id.delivery_amt);
        new_shopname = findViewById(R.id.new_shopname);
        AvgRating = findViewById(R.id.AvgRating);
        ly_pickup = findViewById(R.id.ly_pickup);
        ly_delivery = findViewById(R.id.ly_delivery);
        ly_option = findViewById(R.id.ly_option);
        radioGroup = findViewById(R.id.radiogroup);
        pickup = findViewById(R.id.pickup);
        delivery = findViewById(R.id.delivery);
        TxtDate = findViewById(R.id.date);
        TxtDate.setText(Util.getdatetime());
        ly_deliverynow = findViewById(R.id.ly_deliverynow);
        deliver_now = findViewById(R.id.deliver_now);
        scheduled = findViewById(R.id.scheduled);
        cal_to = findViewById(R.id.cal_to);

        to_txt = findViewById(R.id.to_txt);

        to_txt.setText(Util.getonlydate());

        lyscheduled = findViewById(R.id.lyscheduled);
        Radiodelivery = findViewById(R.id.Radiodelivery);

        LyTop = findViewById(R.id.top);
        LyBottom = findViewById(R.id.bottom);
        ly_address = findViewById(R.id.ly_address);
        btn_addaddress = findViewById(R.id.btn_addaddress);
        btn_changeaddress = findViewById(R.id.btn_changeaddress);
        shopnow = findViewById(R.id.shopnow);
        btnapply = findViewById(R.id.btnapply);
        coupon = findViewById(R.id.coupon);
        deliveryinstruction = findViewById(R.id.deliveryinstruction);
        ly_coupon = findViewById(R.id.ly_coupon);
        //shop_address,contact_person,mobileno,working_hours

        // shop_address = findViewById(R.id.shop_address);
        // contact_person = findViewById(R.id.contact_person);
        mobileno = findViewById(R.id.mobileno);
        working_hours = findViewById(R.id.working_hours);
        ShopImg = findViewById(R.id.img);
        termstxt = findViewById(R.id.termstxt);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.pickup) {
                    ly_pickup.setVisibility(View.VISIBLE);
                    TxtDate.setText(Util.getdatetime());
                    ly_deliverynow.setVisibility(View.GONE);
                    // to_txt.setText("");
                    ly_delivery.setVisibility(View.GONE);
                    updatetotal(StringTotalAmt);
                    ly_deliverycharge.setVisibility(View.GONE);
                    DeliveryTypeId = "1";
                    //   DeliveryPolicyId = "0";
                } else if (checkedId == R.id.delivery) {
                    ly_deliverycharge.setVisibility(View.VISIBLE);
                    // double total = Double.parseDouble(StringTotalAmt) + DeliveryCharge;
                    double total = Double.parseDouble(StringTotalAmt);

                    CalculateDelivery(String.valueOf(total));
                    /*if (DeliveryCharge == 0.0) {
                        //double total = Double.parseDouble(StringTotalAmt) - DeliveryCharge;
                        updatetotal(StringTotalAmt);
                        ly_deliverycharge.setVisibility(View.GONE);
                    } else {
                        ly_deliverycharge.setVisibility(View.VISIBLE);
                        double total = Double.parseDouble(StringTotalAmt) + DeliveryCharge;
                        updatetotal(String.valueOf(total));
                    }*/
                    ly_deliverynow.setVisibility(View.VISIBLE);
                    ly_delivery.setVisibility(View.VISIBLE);
                    ly_pickup.setVisibility(View.GONE);
                    TxtDate.setText("");
                    DeliveryTypeId = "2";
                    // DeliveryPolicyId = "1";
                }
            }

        });

        Radiodelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.deliver_now) {
                    lyscheduled.setVisibility(View.GONE);
                    DeliveryPolicyId = "1";
                } else if (checkedId == R.id.scheduled) {
                    lyscheduled.setVisibility(View.VISIBLE);
                    DeliveryPolicyId = "2";
                }
            }

        });

        ly_shopdetails = findViewById(R.id.ly_shopdetails);
        TxtQuantity = findViewById(R.id.qty);
        TxtDiscount = findViewById(R.id.coupon_discount);
        TxtYouSave = findViewById(R.id.you_save);
        TxtTotalAmt = findViewById(R.id.sum_amount);
        TxtAmt = findViewById(R.id.amt);
        SpinPaymentMode = findViewById(R.id.payment_mode);
        ed_address = findViewById(R.id.ed_address);

        //TxtDate = findViewById(R.id.date);
        ImgCalender = findViewById(R.id.btn_calender);
        BtnPlaceOrder = findViewById(R.id.btn_order);
        BtnAddMore = findViewById(R.id.btn_addmore);

        ImgCalender.setOnClickListener(this);

        cal_to.setOnClickListener(this);
        BtnPlaceOrder.setOnClickListener(this);
        BtnAddMore.setOnClickListener(this);
        termstxt.setOnClickListener(this);
        btn_img_edit.setOnClickListener(this);
        btn_addaddress.setOnClickListener(this);
        btn_changeaddress.setOnClickListener(this);
        shopnow.setOnClickListener(this);
        btnapply.setOnClickListener(this);

        ListCollection = new ArrayList<>();


        SpinPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {

                StrPaymentMode = String.valueOf(SpinPaymentMode.getSelectedItemPosition() + 1);
                if (StrPaymentMode.equalsIgnoreCase("3")) {
                    StrPaymentMode = "99";
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
        // LoadPaymentMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProfile();
        GetCartitems();

    }

    private void updatetotal(String updatedamt) {
        //TxtTotalAmt.setText(getString(R.string.currency) + updatedamt);
        TxtTotalAmt.setText(getString(R.string.currency) + String.format("%.2f", Double.parseDouble(updatedamt)));
    }

    private void GetProfile() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET PROFILE::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_ADDRESS, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        //    alert.build(getString(R.string.timeout_error),false);
                    } else {
                        //   alert.build(getString(R.string.server_error),false);
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("PROFILE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                JSONArray jsonArray = resobject.optJSONArray("_lstGetConsumerAddressOutput");
                                if (jsonArray == null || jsonArray.length() == 0) {
                                    Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                                    //  Util.ShowToast(Profile.this, "No Item in Cart");
                                    ly_address.setVisibility(View.GONE);
                                } else {
                                    ly_address.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject imageobject = jsonArray.getJSONObject(i);
                                        if (imageobject.getString("DefaultAddress").equalsIgnoreCase("1")) {
                                            String data = imageobject.getString("ContactName") + "\n" +
                                                    imageobject.getString("Address1") + "\n" +
                                                    //imageobject.getString("Address2") + "\n" +
                                                    imageobject.getString("Address2") +
                                                    imageobject.getString("City") + "\n" +
                                                    imageobject.getString("Pincode") + "\n" +
                                                    imageobject.getString("ContactNumber");

                                            if (!imageobject.getString("LandMark").isEmpty()) {
                                                data = data + "\n" +
                                                        "Landmark : " + imageobject.getString("LandMark");
                                            }

                                            ed_address.setText(data);
                                            cus_name = imageobject.getString("ContactName");
                                            cus_address = imageobject.getString("Address1");
                                            // EdAddress2.setText(imageobject.getString("Address2"));
                                            cus_city = imageobject.getString("City");
                                            cus_pincode = imageobject.getString("Pincode");

                                            cus_mobileno = imageobject.getString("ContactNumber");
                                            cus_address2 = imageobject.getString("Address2");
                                            landmark = imageobject.getString("LandMark");
                                            ConsumerAddressId = imageobject.getString("ConsumerAddressId");
                                            DefaultAddress = imageobject.getString("DefaultAddress");
                                            Latitude = imageobject.getString("Latitude");
                                            Longitude = imageobject.getString("Longitude");
                                            Util.Logcat.e("Latitude >>" + imageobject.getString("Latitude"));
                                            Util.Logcat.e("Longitude >>" + imageobject.getString("Longitude"));
                                            if (UPDATECHARGES && DeliveryTypeId.equalsIgnoreCase("2") || DeliveryTypeId.equalsIgnoreCase("3")) {
                                                double total = Double.parseDouble(StringTotalAmt);
                                                CalculateDelivery(String.valueOf(total));
                                            }
                                            break;

                                        } else {

                                            String data = jsonArray.getJSONObject(0).getString("ContactName") + "\n" +
                                                    jsonArray.getJSONObject(0).getString("Address1") + "\n" +
                                                    //imageobject.getString("Address2") + "\n" +
                                                    jsonArray.getJSONObject(0).getString("Address2") +
                                                    jsonArray.getJSONObject(0).getString("City") + "\n" +
                                                    jsonArray.getJSONObject(0).getString("Pincode") + "\n" +
                                                    jsonArray.getJSONObject(0).getString("ContactNumber");
                                            if (!jsonArray.getJSONObject(0).getString("LandMark").isEmpty()) {
                                                data = data + "\n" +
                                                        "Landmark : " + imageobject.getString("LandMark");
                                            }

                                            ed_address.setText(data);
                                            cus_name = jsonArray.getJSONObject(0).getString("ContactName");
                                            cus_address = jsonArray.getJSONObject(0).getString("Address1");
                                            // EdAddress2.setText(imageobject.getString("Address2"));
                                            cus_city = jsonArray.getJSONObject(0).getString("City");
                                            cus_pincode = jsonArray.getJSONObject(0).getString("Pincode");
                                            cus_mobileno = jsonArray.getJSONObject(0).getString("ContactNumber");
                                            cus_address2 = jsonArray.getJSONObject(0).getString("Address2");
                                            landmark = jsonArray.getJSONObject(0).getString("LandMark");
                                            ConsumerAddressId = jsonArray.getJSONObject(0).getString("ConsumerAddressId");
                                            DefaultAddress = jsonArray.getJSONObject(0).getString("DefaultAddress");

                                        }
                                        //StrEmail = imageobject.getString("EmailId");
                                    }
                                    //JSONObject imageobject = jsonArray.getJSONObject(0);

                                }
                            }
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            //   alert.build(resobject.getString("StatusDesc"));
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

    private void LoadPaymentMode(String POSId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            Util.Logcat.e("PAYMENT MODE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_PAYMENTMODE, new VolleyResponseListener() {
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
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        // JSONObject obj = response.getJSONObject("subdispo");
                        final List<String> spinnerlistSub = new ArrayList<>();

                        JSONArray dispo = resobject.getJSONArray("_lstPaymentMode");
                        for (int j = 0; j < dispo.length(); j++) {
                            JSONObject object = dispo.getJSONObject(j);
                            Util.Logcat.e("subname1" + "" + object.getString("PaymentMode"));
                            spinnerlistSub.add(object.getString("PaymentMode"));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                                        (CartActivity.this, R.layout.spinner_textview,
                                                spinnerlistSub);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                                SpinPaymentMode.setAdapter(spinnerArrayAdapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetCartitems() {
        ResetCoupon();
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("POSId", "0");
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET CART:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_CART, new VolleyResponseListener() {
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
                    try {
                        Util.Logcat.e("GET CART:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            JSONArray jsonArray = resobject.optJSONArray("_lstCartGetModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                Util.Logcat.e("EMPTY | Null:::" + jsonArray.length());
                                // alert.build(getString(R.string.CartEmpty), false);
                                LyTop.setVisibility(View.GONE);
                                LyBottom.setVisibility(View.VISIBLE);
                                ly_shopdetails.setVisibility(View.GONE);
                                // lyshownow.setVisibility(View.VISIBLE);
                                toolbar.setTitle("");
                                toolbar.setSubtitle("");
                            } else {
                                TxtQuantity.setText("Total Price " + "(" + resobject.getString("TotalQty") + " Items" + ")");
                                TxtYouSave.setText(getString(R.string.currency) + String.format("%.2f", resobject.getDouble("TotalDiscount")));
                                TxtTotalAmt.setText(getString(R.string.currency) + String.format("%.2f", resobject.getDouble("TotalSum")));
                                TxtAmt.setText(getString(R.string.currency) + String.format("%.2f", resobject.getDouble("TotalPrice")));

                                Log.e("TotalPrice", resobject.getString("TotalPrice"));
                                Log.e("TotalDiscount", resobject.getString("TotalDiscount"));
                                //StringTotalAmt = Util.ChangeNumber(resobject.getString("TotalSum"));
                                StringTotalAmt = resobject.getString("TotalSum");
                                DistanceNetPay = resobject.getString("DistanceNetPay");
                                TotalWeight = resobject.getString("TotalWeight");
                                if (DeliveryTypeId.equalsIgnoreCase("1") || pickup.isChecked()) {
                                    updatetotal(StringTotalAmt);
                                } else if (DeliveryTypeId.equalsIgnoreCase("3") && pickup.isChecked()) {
                                    updatetotal(StringTotalAmt);
                                } else if (DeliveryTypeId.equalsIgnoreCase("2")) {
                                    double total = Double.parseDouble(StringTotalAmt);

                                    CalculateDelivery(String.valueOf(total));
                                /*if (DeliveryCharge == 0.0) {
                                    //double total = Double.parseDouble(StringTotalAmt) - DeliveryCharge;
                                    updatetotal(StringTotalAmt);
                                } else {
                                    double total = Double.parseDouble(StringTotalAmt) + DeliveryCharge;
                                    updatetotal(String.valueOf(total));
                                }*/
                                } else if (DeliveryTypeId.equalsIgnoreCase("3") && delivery.isChecked()) {
                                    double total = Double.parseDouble(StringTotalAmt);

                                    CalculateDelivery(String.valueOf(total));
                                /*if (DeliveryCharge == 0.0) {
                                    //double total = Double.parseDouble(StringTotalAmt) - DeliveryCharge;
                                    updatetotal(StringTotalAmt);
                                } else {
                                    double total = Double.parseDouble(StringTotalAmt) + DeliveryCharge;
                                    updatetotal(String.valueOf(total));
                                }*/
                                }
                                if (POSId.isEmpty()) {
                                    POSId = jsonArray.getJSONObject(0).getString("PosID");
                                    Log.e("POSId", POSId);
                                    GetStoreDetails(POSId);
                                    LoadPaymentMode(POSId);
                                }
                                double inc = Double.parseDouble(resobject.getString("TotalInclusiveGST"));
                                double exc = Double.parseDouble(resobject.getString("TotalExclusiveGST"));

                                if (inc > 0) {
                                    ly_inclgst.setVisibility(View.VISIBLE);

                                    inclusive_gst.setText(getString(R.string.currency) + resobject.getString("TotalInclusiveGST"));
                                } else {
                                    ly_inclgst.setVisibility(View.GONE);
                                }

                                if (exc > 0) {
                                    ly_excgst.setVisibility(View.VISIBLE);
                                    exclusive_gst.setText(getString(R.string.currency) + resobject.getString("TotalExclusiveGST"));
                                } else {
                                    ly_excgst.setVisibility(View.GONE);
                                }

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject imageobject = jsonArray.getJSONObject(i);
                                        DataHashMap = new HashMap<>();
                                        DataHashMap.put("ProductName", imageobject.getString("ProductName"));
                                        DataHashMap.put("Totalprice", imageobject.getString("Totalprice"));
                                        DataHashMap.put("SellingPrice", imageobject.getString("SellingPrice"));
                                        DataHashMap.put("Qty", imageobject.getString("Qty"));
                                        DataHashMap.put("ProductId", imageobject.getString("ProductId"));
                                        DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                                        DataHashMap.put("NetPrice", imageobject.getString("NetPrice"));
                                        //GST Details
                                        DataHashMap.put("ExclusiveGST", imageobject.getString("ExclusiveGST"));
                                        DataHashMap.put("InclusiveGST", imageobject.getString("InclusiveGST"));
                                        DataHashMap.put("GSTTypeId", imageobject.getString("GSTTypeId"));
                                        DataHashMap.put("GSTpercentage", imageobject.getString("GSTpercentage"));
                                        DataHashMap.put("ProductAttributeId", imageobject.getString("ProductAttributeId"));
                                        DataHashMap.put("ProductAttributeValueId", imageobject.getString("ProductAttributeValueId"));
                                        DataHashMap.put("ProductAttributeId2", imageobject.getString("ProductAttributeId2"));
                                        DataHashMap.put("ProductAttributeValueId2", imageobject.getString("ProductAttributeValueId2"));
                                        if (imageobject.getString("ImagePath").contains(".jpg") || imageobject.getString("ImagePath").contains(".png") || imageobject.getString("ImagePath").contains(".PNG") || imageobject.getString("ImagePath").contains(".jpeg")) {
                                            DataHashMap.put("imgavailable", "true");
                                        } else {
                                            DataHashMap.put("imgavailable", "false");
                                        }

                                        ListCollection.add(DataHashMap);
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ListCollection.size() > 0) {
                                                adapter = new CartAdapter(CartActivity.this, ListCollection);
                                                listview.setAdapter(adapter);
                                                setListViewHeightBasedOnItems(listview);
                                            } else {
                                                // ListCollection.clear();
                                                LyTop.setVisibility(View.GONE);
                                                LyBottom.setVisibility(View.VISIBLE);
                                                // ShowAlertNew("Cart Empty", true, false);
                                            }

                                        }
                                    });


                                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (SystemClock.elapsedRealtime() - mLastClickTimeListViewItem < 1000) {
                                                return;
                                            }
                                            mLastClickTimeListViewItem = SystemClock.elapsedRealtime();
                                            long viewId = view.getId();
                                            if (ListCollection.size() > 0) {
                                                if (viewId == R.id.plus) {
                                                    int qty = Integer.parseInt(ListCollection.get(position).get("Qty")) + 1;
                                                    //AddItem(ListCollection.get(position).get("ProductId"), String.valueOf(qty));
                                                    AddItem(ListCollection.get(position).get("ProductId"), String.valueOf(qty), ListCollection.get(position).get("ProductAttributeId"), ListCollection.get(position).get("ProductAttributeValueId"), ListCollection.get(position).get("ProductAttributeId2"), ListCollection.get(position).get("ProductAttributeValueId2"));
                                                } else if (viewId == R.id.minus) {
                                                    int qty = Integer.parseInt(ListCollection.get(position).get("Qty")) - 1;
                                                    if (Integer.parseInt(ListCollection.get(position).get("Qty")) != 1)
                                                        // ProductAttributeId ProductAttributeValueId ProductAttributeId2 ProductAttributeValueId2
                                                        AddItem(ListCollection.get(position).get("ProductId"), String.valueOf(qty), ListCollection.get(position).get("ProductAttributeId"), ListCollection.get(position).get("ProductAttributeValueId"), ListCollection.get(position).get("ProductAttributeId2"), ListCollection.get(position).get("ProductAttributeValueId2"));
                                                } else if (viewId == R.id.btn_delete) {
                                                    DeleteItem(ListCollection.get(position).get("ProductId"), ListCollection.get(position).get("ProductAttributeId"), ListCollection.get(position).get("ProductAttributeValueId"), ListCollection.get(position).get("ProductAttributeId2"), ListCollection.get(position).get("ProductAttributeValueId2"));
                                                }
                                            } else {
                                                alert.build("Cart Empty", false);

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

    private void GetStoreDetails(String POSId) {

        try {
            JSONObject obj = new JSONObject();
            // obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerId", "0");
            obj.put("POSId", POSId);
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

                        JSONArray _lstDeliveryPolicyModel = resobject.optJSONArray("_lstDeliveryPolicyModel");
                        JSONArray _lstDeliverySlotResModel = resobject.optJSONArray("_lstDeliverySlotResModel");
                        LoadDeliverySpinner(_lstDeliverySlotResModel);
                        Util.Logcat.e("_lstDeliveryPolicyModel" + "" + _lstDeliveryPolicyModel);
                        if (_lstDeliveryPolicyModel.length() > 0) {
                            for (int j = 0; j < _lstDeliveryPolicyModel.length(); j++) {
                                JSONObject object = _lstDeliveryPolicyModel.getJSONObject(j);
                                Util.Logcat.e("DeliveryPolicy" + "" + object.getString("DeliveryPolicy"));
                                if (object.getString("DeliveryPolicyId").equalsIgnoreCase("1")) {
                                    deliver_now.setText(object.getString("DeliveryPolicy"));
                                    deliver_now.setVisibility(View.VISIBLE);
                                    deliver_now.setChecked(true);
                                    DeliveryPolicyId = "1";
                                }
                                if (object.getString("DeliveryPolicyId").equalsIgnoreCase("2")) {
                                    scheduled.setText(object.getString("DeliveryPolicy"));
                                    scheduled.setVisibility(View.VISIBLE);
                                    if (deliver_now.getVisibility() == View.GONE) {
                                        scheduled.setChecked(true);
                                        DeliveryPolicyId = "2";
                                    }
                                }
                            }
                        }

                        PosName = resobject.getString("PosName");
                        new_shopname.setText(resobject.getString("PosName"));
                        AvgRating.setText(resobject.getString("AvgRating") + " (" + resobject.getString("TotalReviews") + " Reviews)");
                        PosAddress = resobject.getString("Address");
                        ImagePath = resobject.getString("ImagePath");

                        DeliveryFromTime = resobject.getString("DeliveryFromTime");
                        DeliveryToTime = resobject.getString("DeliveryToTime");
                        OrderAcceptanceFromTime = resobject.getString("OrderAcceptanceFromTime");
                        OrderAcceptanceToTime = resobject.getString("OrderAcceptanceToTime");

                        DeliveryTypeId = resobject.getString("DeliveryTypeId");
                        //get delivery charge

                        // DeliveryCharge = Double.parseDouble(resobject.getString("DeliveryCharge"));
                        //   delivery_amt.setText(getString(R.string.currency) + String.format("%.2f", resobject.getDouble("DeliveryCharge")));

                        POSLatitude = resobject.getString("Latitude");
                        POSLongitude = resobject.getString("Longitude");

                        if (DeliveryTypeId.equalsIgnoreCase("1")) {
                            ly_option.setVisibility(View.GONE);
                            ly_pickup.setVisibility(View.VISIBLE);
                            ly_deliverynow.setVisibility(View.GONE);
                            pickup.setChecked(true);
                        } else if (DeliveryTypeId.equalsIgnoreCase("2")) {
                            ly_option.setVisibility(View.GONE);
                            ly_delivery.setVisibility(View.VISIBLE);
                            ly_deliverynow.setVisibility(View.VISIBLE);
                            delivery.setChecked(true);
                        } else {
                            ly_option.setVisibility(View.VISIBLE);
                        }

                        if (!PosName.isEmpty() && PosName != null) {
                            toolbar.setTitle(PosName);
                        } else {
                            toolbar.setTitle("N/A");
                        }

                        if (!PosAddress.isEmpty() && PosAddress != null) {
                            toolbar.setSubtitle(PosAddress);
                        } else {
                            toolbar.setSubtitle("N/A");
                        }

                        if (ImagePath.contains(".png") || ImagePath.contains(".PNG") || ImagePath.contains(".jpg") || ImagePath.contains(".jpeg")) {
                            Glide.with(CartActivity.this).load(ImagePath).into(ShopImg);
                        } else {
                            ShopImg.setVisibility(View.INVISIBLE);
                        }

                        if (!resobject.getString("PhoneNo").isEmpty() && resobject.getString("PhoneNo") != null) {
                            // mobileno.setText("Mobile No " + ": " + resobject.getString("PhoneNo"));
                            mobileno.setText(resobject.getString("PhoneNo"));
                        } else {
                            mobileno.setText("Mobile No " + ": " + "N/A");
                        }

                        if (resobject.getString("DeliveryFromTime") != null && resobject.getString("DeliveryToTime") != null) {
                            //working_hours.setText("Working Hour " + ": " + resobject.getString("DeliveryFromTime") + "-" + resobject.getString("DeliveryToTime"));
                            working_hours.setText(resobject.getString("DeliveryFromTime") + "-" + resobject.getString("DeliveryToTime"));
                        } else {
                            working_hours.setText("Working Hour " + ": " + "N/A");
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

    private void LoadDeliverySpinner(final JSONArray _lstDeliverySlotResModel) {
        Util.Logcat.e("_lstDeliverySlotResModel" + "" + _lstDeliverySlotResModel);
        try {

            final List<String> spinnerlistSub = new ArrayList<>();
            spinnerlistSub.add("Select Time Slot");
            for (int j = 0; j < _lstDeliverySlotResModel.length(); j++) {
                JSONObject object = _lstDeliverySlotResModel.getJSONObject(j);
                Util.Logcat.e("Schedule" + "" + object.getString("Schedule"));
                spinnerlistSub.add(object.getString("Schedule"));
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                    (this, R.layout.spinner_textview,
                            spinnerlistSub);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            SpinDelivery_mode.setAdapter(spinnerArrayAdapter);
        } catch (JSONException e) {
            Util.Logcat.e("SpinDelivery_mode:" + e);
        }

        SpinDelivery_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                String hai = SpinDelivery_mode.getSelectedItem().toString();
                Util.Logcat.e("SpinDelivery_mode :" + hai);

                if (!"Select Time Slot".equalsIgnoreCase(hai)) {
                    try {
                        boolean validation = false;
                        for (int j = 0; j < _lstDeliverySlotResModel.length(); j++) {
                            JSONObject object = _lstDeliverySlotResModel.getJSONObject(j);
                            if (hai.equalsIgnoreCase(object.getString("Schedule"))) {
                                String SelectedDate = to_txt.getText().toString() + " " + object.getString("FromTime") + ":00";
                                Util.Logcat.e("Selected Date :" + SelectedDate);
                                Util.Logcat.e("Current Date :" + Util.getdatetime());
                                if (Util.DateTimecompare(SelectedDate, Util.getdatetime())) {
                                    FromTime = to_txt.getText().toString() + " " + object.getString("FromTime") + ":00";
                                    ToTime = to_txt.getText().toString() + " " + object.getString("ToTime") + ":00";
                                    Util.Logcat.e("FromTime :" + FromTime);
                                    Util.Logcat.e("ToTime :" + ToTime);
                                    validation = true;
                                    // break;
                                }
                                break;
                            }
                        }
                        if (validation == false) {
                            FromTime = "";
                            ToTime = "";
                            SpinDelivery_mode.setSelection(0);
                            alert.build(getString(R.string.SelectFutureTimeSlot), false);
                        }
                    } catch (JSONException | ParseException e) {

                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void AddItem(String productid, String Quantity, String ProductAttributeId, String ProductAttributeValueId, String ProductAttributeId2, String ProductAttributeValueId2) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("ProductId", productid);
            obj.put("Qty", Quantity);
            obj.put("Upflag", "2");

            obj.put("RefProductAttributeValueId", ProductAttributeId);
            obj.put("ProductAttributeValueId", ProductAttributeValueId);
            obj.put("ProductAttributeId2", ProductAttributeId2);
            obj.put("ProductAttributeValueId2", ProductAttributeValueId2);

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
                            GetCartitems();
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

    private void PlaceOrder(String ProductInfo) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            // obj.put("PickupTime", TxtDate.getText().toString());
            obj.put("PickupTime", TxtDate.getText().toString());
            // obj.put("StateCode", "0");
            obj.put("ProductInfo", ProductInfo);
            obj.put("CustomerName", cus_name);
            obj.put("LandMark", landmark);
            obj.put("DeliveryAddress", cus_address);
            obj.put("City", cus_city);
            obj.put("Pincode", cus_pincode);
            obj.put("CouponCode", coupon.getEditableText().toString());
            obj.put("CouponAmount", CouponAmount);
            if (!inclusive_gst.getText().toString().isEmpty()) {
                obj.put("TotalInclusiveGST", inclusive_gst.getText().toString().replaceAll("₹", ""));
            } else {
                obj.put("TotalInclusiveGST", "0");
            }

            if (!exclusive_gst.getText().toString().isEmpty()) {
                obj.put("TotalExclusiveGST", exclusive_gst.getText().toString().replaceAll("₹", ""));
            } else {
                obj.put("TotalExclusiveGST", "0");
            }
            obj.put("MobileNo", cus_mobileno);
            if (StrPaymentMode.isEmpty()) {
                obj.put("PaymentModeId", "0");
            } else {
                obj.put("PaymentModeId", StrPaymentMode);
            }
            //obj.put("PaymentModeId", "0");

            if (DeliveryTypeId.equalsIgnoreCase("1")) {
                obj.put("DeliveryPolicyId", "0");
                obj.put("DeliveryCharges", "0");
            } else {
                obj.put("DeliveryPolicyId", DeliveryPolicyId);
                obj.put("DeliveryCharges", DeliveryCharge);
            }

            if (DeliveryPolicyId.equalsIgnoreCase("1")) {
                obj.put("DeliveryFromTime", Util.getdatetime());
                obj.put("DeliveryToTime", Util.getdatetime());
            } else if (DeliveryPolicyId.equalsIgnoreCase("2")) {
                obj.put("DeliveryFromTime", FromTime);
                obj.put("DeliveryToTime", ToTime);
            } else {
                obj.put("DeliveryFromTime", "");
                obj.put("DeliveryToTime", "");
            }

            //  obj.put("DeliveryFromTime", "");
            //  obj.put("DeliveryToTime", to_txt.getText().toString());
            obj.put("DeliveryTypeId", DeliveryTypeId);
            obj.put("BookingType", "3");
            obj.put("DeliveryDistance", DeliveryDistance);
            obj.put("BookingSite", "SMARTSALEZ");
            obj.put("OrderDesc", deliveryinstruction.getEditableText().toString());
            // obj.put("orderAmount", "1");
            // obj.put("orderAmount", StringTotalAmt);
            obj.put("orderAmount", TxtTotalAmt.getText().toString().replaceAll("₹", ""));
            Util.Logcat.e("PLACE ORDER:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PLACE_ORDER, new VolleyResponseListener() {
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
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            // alert.build(resobject.getString("StatusDesc"));
                            // GetCartitems(POSId);
                            // if (StrPaymentMode.equalsIgnoreCase("1") && Util.DeliveryTypeId.equalsIgnoreCase("2")) {
                            if (StrPaymentMode.equalsIgnoreCase("2")) {
                                Map<String, String> params = new HashMap<>();
                                params.put(PARAM_APP_ID, APP_ID);
                                params.put(PARAM_ORDER_ID, resobject.getString("IndentNo"));
                                //params.put(PARAM_ORDER_AMOUNT, "1");
                                //params.put(PARAM_ORDER_AMOUNT, StringTotalAmt);
                                params.put(PARAM_ORDER_AMOUNT, TxtTotalAmt.getText().toString().replaceAll("₹", ""));
                                params.put(PARAM_ORDER_NOTE, "Shopname :" + new_shopname);
                                params.put(PARAM_CUSTOMER_NAME, cus_name);
                                params.put(PARAM_CUSTOMER_PHONE, cus_mobileno);
                                params.put(PARAM_CUSTOMER_EMAIL, resobject.getString("EmailId"));
                                params.put(PARAM_VENDOR_SPLIT, resobject.getString("vendorSplit"));
                                DoPayment(params, resobject.getString("Token"));
                            } else {

                                ShowAlertNew(resobject.getString("StatusDesc"), true, true);

                            }

                        } else if (resobject.getString("Status").equalsIgnoreCase("1205")) {
                            alert.build(resobject.getString("StatusDesc"), false);
                        } else if (resobject.getString("Status") == null) {
                            alert.build("Server Error. Please contact customer care", false);
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

    private void ResetCoupon() {
        CouponAmount = "0.00";
        TxtDiscount.setText("0.00");
        coupon.setText("");
    }

    private void ShowAlertNew(String s, final boolean close, boolean value) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cutom_alert);
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

        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (close) {
                    Util.saveData("POSId", "", getApplicationContext());
                    if (CartActivity.this.getPackageName().equals("com.gieasesales")) {
                        Intent history = new Intent(CartActivity.this, HistoryActivity.class);
                        startActivity(history);
                        finish();
                    }
                    // GetCartitems();
                } else {
                }

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void DoPayment(Map<String, String> params, String token) {
        finish = true;
        Util.Logcat.e("params" + String.valueOf(params));
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(CartActivity.this, params, token, STAGE, "#ed1c24", "#FFFFFF", false);
        //cfPaymentService.doPayment(CartActivity.this, params, token, STAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Util.Logcat.e("IFFRAN" + "API Response : ");
        //Prints all extras. Replace with app logic.
        StringBuilder s = new StringBuilder();
        Payment = new ArrayList<>();
        Payment.clear();
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {

                if (!bundle.getString("txStatus").equalsIgnoreCase("CANCELLED")) {
                    for (String key : bundle.keySet()) {
                        Util.Logcat.e("IFFRAN" + key + " : " + " RES " + bundle.getString(key));
                        Util.Logcat.e("txStatus" + bundle.getString("txStatus"));
                        if (!bundle.getString(key).equalsIgnoreCase("CANCELLED"))

                            if (bundle.getString(key) != null) {
                                Util.Logcat.e("PAYMENT RESPONSE" + key + " : " + bundle.getString(key));
                                s.append(key).append(" : ").append(bundle.getString(key)).append("\n");
                                HashMap<String, String> DataHashMap = new HashMap<>();
                                if (key != null && bundle.getString(key) != null)
                                    DataHashMap.put(key, bundle.getString(key));
                                Payment.add(DataHashMap);

                            }
                    }
                    PaymentUpdate(Payment, s.toString());
                }
                /*if (Payment.size() > 0) {

                }*/
            }
        }
    }

    private void PaymentUpdate(final List<Map<String, String>> payment, final String value) {
        //[{paymentMode=WALLET}, {orderId=20200611151102AA275}, {txTime=2020-06-11 15:22:08}, {referenceId=359079}, {type=CashFreeResponse}, {txMsg=Transaction Successful}, {signature=H++0u2y2hpYgD2UOG3iRIZbjP1sutY/82p4lGLomjpE=}, {orderAmount=1.00}, {txStatus=SUCCESS}]

        try {
            final JSONObject obj = new JSONObject();
            Util.Logcat.e("orderId" + "" + payment.get(1).get("orderId"));
            Util.Logcat.e("orderAmount" + "" + payment.get(7).get("orderAmount"));
            Util.Logcat.e("txStatus" + "" + payment.get(8).get("txStatus"));
            Util.Logcat.e("referenceId" + "" + payment.get(3).get("referenceId"));
            Util.Logcat.e("txTime" + "" + payment.get(2).get("txTime"));
            Util.Logcat.e("signature" + "" + payment.get(6).get("signature"));

            if (!payment.get(1).get("orderId").equalsIgnoreCase("false")) {
                obj.put("EaseSalezRefNo", payment.get(1).get("orderId"));
            } else {
                obj.put("EaseSalezRefNo", "");
            }
            if (!payment.get(7).get("orderAmount").equalsIgnoreCase("false")) {
                obj.put("TranAmount", payment.get(7).get("orderAmount"));
            } else {
                obj.put("TranAmount", "");
            }
            if (!payment.get(8).get("txStatus").equalsIgnoreCase("false")) {
                obj.put("PGStatus", payment.get(8).get("txStatus"));
            } else {
                obj.put("PGStatus", "");
            }
            if (!payment.get(3).get("referenceId").equalsIgnoreCase("false")) {
                obj.put("PGRefNo", payment.get(3).get("referenceId"));
            } else {
                obj.put("PGRefNo", "");
            }
            if (!payment.get(2).get("txTime").equalsIgnoreCase("false")) {
                obj.put("PGRefNo1", payment.get(2).get("txTime"));
            } else {
                obj.put("PGRefNo1", "");
            }
            if (!payment.get(6).get("signature").equalsIgnoreCase("false")) {
                obj.put("PGRefNo2", payment.get(6).get("signature"));
            } else {
                obj.put("PGRefNo2", "");
            }

            if (!payment.get(0).get("paymentMode").equalsIgnoreCase("false")) {
                obj.put("PGRefNo3", payment.get(0).get("paymentMode"));
            } else {
                obj.put("PGRefNo3", "");
            }

            if (!payment.get(5).get("txMsg").equalsIgnoreCase("false")) {
                obj.put("PGRefNo4", payment.get(5).get("txMsg"));
            } else {
                obj.put("PGRefNo4", "");
            }

            obj.put("PGRefNo5", "");//empty string
            obj.put("IndentId", "0");
            Util.Logcat.e("PAYMENT" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PAYMENT, new VolleyResponseListener() {
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
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            if (value.contains("SUCCESS")) {
                                String datas = "Your transaction has been successfully completed pg ref:" + payment.get(3).get("referenceId") + "and order no:" + payment.get(1).get("orderId");
                                ShowAlertNew(datas, true, true);

                            } else {
                                String datas = "Your transaction has been failed pg ref:" + payment.get(3).get("referenceId") + "and order no:" + payment.get(1).get("orderId");
                                ShowAlertNew(datas, true, false);
                            }

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

    private void DeleteItem(String productid, String ProductAttributeId, String ProductAttributeValueId, String ProductAttributeId2, String ProductAttributeValueId2) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("ProductId", productid);
            obj.put("Qty", "0");

            obj.put("ProductAttributeId", ProductAttributeId);
            obj.put("ProductAttributeValueId", ProductAttributeValueId);
            obj.put("ProductAttributeId2", ProductAttributeId2);
            obj.put("ProductAttributeValueId2", ProductAttributeValueId2);

            Util.Logcat.e("DELETE ITEM:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(this, params.toString(), REMOVE_PRODUCT, new VolleyResponseListener() {
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
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            //alert.build(resobject.getString("StatusDesc"));
                            GetCartitems();
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

    private boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.

            int totalItemsHeight = 0;

            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        final Calendar c = Calendar.getInstance();
        Integer mYear = c.get(Calendar.YEAR);
        Integer mMonth = c.get(Calendar.MONTH);
        Integer mDay = c.get(Calendar.DAY_OF_MONTH);

        switch (v.getId()) {
            case R.id.btnapply:
                if (coupon.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Coupon Code", false);
                } else {
                    StringBuffer value = new StringBuffer();
                    for (Map<String, String> sdas : ListCollection) {
                        //value.append(sdas.get("ProductId") + "~" + "0" + "~" + sdas.get("Qty") + "~" + sdas.get("SellingPrice") + "|");
                        value.append(sdas.get("ProductId")
                                + "~" + "0" + "~" + sdas.get("Qty")
                                + "~" + sdas.get("SellingPrice")
                                + "~" + sdas.get("GSTTypeId")
                                + "~" + sdas.get("GSTpercentage")
                                + "~" + sdas.get("InclusiveGST")
                                + "~" + sdas.get("ExclusiveGST")
                                + "~" + sdas.get("ProductAttributeId")
                                + "~" + sdas.get("ProductAttributeValueId")
                                + "~" + sdas.get("ProductAttributeId2")
                                + "~" + sdas.get("ProductAttributeValueId2") + "|");
                        //ProductId~ProductAttributeValueId~QTY~SellingPrice|
                               /* ProductId~ProductAttributeValueId~Qty~SellingPrice~GSTTypeId~
                                    GSTpercentage~InclusiveGST~ExclusiveGST
                            ~ProductAttributeId~ProductAttributeValueId~ProductAttributeId2~ProductAttributeValueId2*/
                    }
                    ApplyCoupon(value.toString());
                }
                break;

            case R.id.shopnow:
                finish();
                break;

            case R.id.btn_changeaddress:

                if (CartActivity.this.getPackageName().equals("com.gieasesales")) {
                    Intent changeaddres = new Intent(CartActivity.this, SelectAddress.class);
                    changeaddres.putExtra("showselect", "true");
                    startActivity(changeaddres);
                }

                break;

            case R.id.btn_addaddress:
                if (CartActivity.this.getPackageName().equals("com.gieasesales")) {
                    Intent addaddres = new Intent(CartActivity.this, AddAddress.class);
                    startActivity(addaddres);
                }

                break;

            case R.id.btn_img_edit:
                if (CartActivity.this.getPackageName().equals("com.gieasesales")) {
                    Intent editaddress = new Intent(CartActivity.this, EditAddress.class);
                    editaddress.putExtra("cus_name", cus_name);
                    editaddress.putExtra("cus_address", cus_address);
                    editaddress.putExtra("cus_address2", cus_address2);
                    editaddress.putExtra("landmark", landmark);
                    editaddress.putExtra("cus_city", cus_city);
                    editaddress.putExtra("cus_pincode", cus_pincode);
                    editaddress.putExtra("cus_mobileno", cus_mobileno);
                    editaddress.putExtra("ConsumerAddressId", ConsumerAddressId);
                    editaddress.putExtra("DefaultAddress", DefaultAddress);
                    startActivity(editaddress);
                }

                break;

            case R.id.termstxt:
                if (getData("Terms", getApplicationContext()).equalsIgnoreCase("null")) {
                    alert.build("No Data", false);
                } else {
                    htmlalert.build(getData("Terms", getApplicationContext()));
                }
                break;

            case R.id.btn_calender:
                DatePickerDialog datePickerDialog = new DatePickerDialog(CartActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear <= 8 && dayOfMonth > 9) {
                            String _data = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            TxtDate.setText(_data);
                        } else if (monthOfYear <= 8 && dayOfMonth <= 9) {
                            String _data = "0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            TxtDate.setText(_data);
                        } else {
                            if (dayOfMonth <= 9) {
                                String _data = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                TxtDate.setText(_data);
                            } else {
                                String _data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                TxtDate.setText(_data);
                            }

                        }

                        /*String _data = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
                        TxtDate.setText(_data);*/

                        TimePickerDialog timePickerDialog = new TimePickerDialog(CartActivity.this, R.style.SpinnerTimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //String hai=String.format("%02d", hourOfDay);
                                // hourOfDay= Integer.parseInt(hai);
                                String _dataHora = TxtDate.getText().toString() + " " + String.format("%02d:%02d", hourOfDay, minute) + ":" + "00";
                                TxtDate.setText(_dataHora);
                                Util.Logcat.e("LOG TIME" + hourOfDay + ":" + minute);
                                try {
                                    if (Integer.parseInt(Util.comparedate(_dataHora, Util.getdatetime())) > 0) {
                                        //it's after current
                                        //String _dataHora = TxtDate.getText().toString() + " " + String.format("%02d:%02d", hourOfDay, minute) + ":" + "00";
                                        //  TxtDate.setText(_dataHora);
                                        if (Util.checkTimepickup(DeliveryFromTime.substring(0, 2), DeliveryToTime.substring(0, 2), _dataHora) == true) {
                                            TxtDate.setText(_dataHora);
                                        } else {
                                            alert.build("Select Pickup time between " + DeliveryFromTime + " - " + DeliveryFromTime, false);
                                            TxtDate.setText("");
                                        }
                                    } else {
                                        try {
                                            if (Util.validtime(TxtDate.getText().toString())) {
                                                TxtDate.setText("");
                                                alert.build(getString(R.string.SelectFutureTime), false);
                                            } else {
                                                //it's before current'
                                                TxtDate.setText("");
                                                alert.build(getString(R.string.InvalidTime), false);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, Util.get2value(), c.get(Calendar.MINUTE), true);
                        timePickerDialog.setCancelable(false);
                        timePickerDialog.show();
                        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                break;

            case R.id.btn_order:
                if (DeliveryTypeId.equalsIgnoreCase("3") && pickup.isChecked() == false && delivery.isChecked() == false) {
                    alert.build("Select Delivery Type", false);
                } else if (pickup.isChecked() == true && TxtDate.getText().toString().isEmpty()) {
                    alert.build("Select Pickup Date", false);
                } else if (scheduled.isChecked() && SpinDelivery_mode.getSelectedItem().toString().equalsIgnoreCase("Select Time Slot") & !DeliveryTypeId.equalsIgnoreCase("1")) {
                    alert.build("Select Delivery Time Slot", false);
                } else if (scheduled.isChecked() && to_txt.getText().toString().isEmpty()) {
                    alert.build("Select Scheduled Date", false);
                } else {
                    try {
                        if (pickup.isChecked() && Util.validdatetime(TxtDate.getText().toString())) {
                            TxtDate.setText("");
                            alert.build(getString(R.string.SelectFutureTime), false);
                        } else if (DeliveryTypeId.equalsIgnoreCase("3") && pickup.isChecked() && Util.validdatetime(TxtDate.getText().toString())) {
                            TxtDate.setText("");
                            alert.build(getString(R.string.SelectFutureTime), false);
                        } else if (delivery.isChecked() == true && to_txt.getText().toString().isEmpty()) {
                            alert.build("Select Delivery To Time", false);
                        } else if (ed_address.getText().toString().isEmpty()) {
                            alert.build("Add Address", false);
                        } else if (pickup.isChecked() == true && Util.checkTimepickup(DeliveryFromTime.substring(0, 2), DeliveryToTime.substring(0, 2), TxtDate.getText().toString()) == false) {
                            alert.build("Select Pickup time between " + DeliveryFromTime + "-" + DeliveryToTime, false);
                        } else if (termscheckbox.isChecked() == false) {
                            alert.build("Agree Terms & Conditions", false);
                        } else {

                            StringBuffer value = new StringBuffer();
                            for (Map<String, String> sdas : ListCollection) {

                                // value.append(sdas.get("ProductId") + "~" + "0" + "~" + sdas.get("Qty") + "~" + sdas.get("SellingPrice") + "|");
                                // value.append(sdas.get("ProductId") + "~" + "0" + "~" + sdas.get("Qty") + "~" + sdas.get("SellingPrice") + "~" + sdas.get("GSTTypeId") + "~" + sdas.get("GSTpercentage") + "~" + sdas.get("InclusiveGST") + "~" + sdas.get("ExclusiveGST") + "|");
                                //  ProductId~ProductAttributeValueId~QTY~SellingPrice|
                                value.append(sdas.get("ProductId") + "~" + "0" + "~" + sdas.get("Qty")
                                        + "~" + sdas.get("SellingPrice")
                                        + "~" + sdas.get("GSTTypeId")
                                        + "~" + sdas.get("GSTpercentage")
                                        + "~" + sdas.get("InclusiveGST")
                                        + "~" + sdas.get("ExclusiveGST")
                                        + "~" + sdas.get("ProductAttributeId")
                                        + "~" + sdas.get("ProductAttributeValueId")
                                        + "~" + sdas.get("ProductAttributeId2")
                                        + "~" + sdas.get("ProductAttributeValueId2") + "|");
                            }
                            PlaceOrder(value.toString());
                            Util.Logcat.e("PlaceOrder" + value.toString());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.cal_to:
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(CartActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear <= 8 && dayOfMonth > 9) {
                            String _data = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            to_txt.setText(_data);
                        } else if (monthOfYear <= 8 && dayOfMonth <= 9) {
                            String _data = "0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            to_txt.setText(_data);
                        } else {
                            if (dayOfMonth <= 9) {
                                String _data = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                to_txt.setText(_data);
                            } else {
                                String _data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                to_txt.setText(_data);
                            }

                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog2.setCancelable(false);
                datePickerDialog2.show();
                datePickerDialog2.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                // datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis());
                break;
            case R.id.btn_addmore:
                finish();
                break;
            default:
                break;
        }
    }

    private void ApplyCoupon(String ProductInfo) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("ProductInfo", ProductInfo);
            obj.put("CouponCode", coupon.getEditableText().toString());
            Util.Logcat.e("COUPON:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), APPLY_COUPON, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("COUPON:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            alert.build(resobject.getString("StatusDesc"), true);
                            Util.Logcat.e("DiscountTypeId: " + resobject.getString("DiscountTypeId"));
                            if (resobject.getString("DiscountTypeId").equalsIgnoreCase("2")) {
                                CouponAmount = resobject.getString("DiscountValue");
                            } else {
                                double value = Double.parseDouble(StringTotalAmt) * Double.parseDouble(resobject.getString("DiscountValue")) / 100;
                                CouponAmount = String.valueOf(value);
                            }

                            if (Double.parseDouble(CouponAmount) > 0) {
                                //  ly_coupon.setVisibility(View.VISIBLE);
                                TxtDiscount.setText(getString(R.string.currency) + String.format("%.2f", Double.parseDouble(CouponAmount)));
                                double total = Double.parseDouble(StringTotalAmt) - Double.parseDouble(CouponAmount);
                                StringTotalAmt = String.valueOf(total);
                                updatetotal(String.valueOf(total));
                                // coupon.setEnabled(false);
                                // coupon.setClickable(false);
                            } else {
                                //ly_coupon.setVisibility(View.INVISIBLE);
                                TxtDiscount.setText(getString(R.string.currency) + "0.00");
                            }
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

    private void CalculateDelivery(String NetPay) {
        try {
            JSONObject obj = new JSONObject();
            // obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("Distance", "0");
            obj.put("Weight", TotalWeight);
            obj.put("NetPay", DistanceNetPay);
            if (Latitude.isEmpty()) {
                obj.put("Latitude", "0");
            } else {
                obj.put("Latitude", Latitude);
            }

            if (Longitude.isEmpty()) {
                obj.put("Longitude", "0");
            } else {
                obj.put("Longitude", Longitude);
            }

            if (POSLatitude.isEmpty()) {
                obj.put("POSLatitude", "0");
            } else {
                obj.put("POSLatitude", POSLatitude);
            }

            if (POSLongitude.isEmpty()) {
                obj.put("POSLongitude", "0");
            } else {
                obj.put("POSLongitude", POSLongitude);
            }

            Util.Logcat.e("DELIVERY CHARGRES:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), CALCULATE_DELIVERY, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("DELIVERY CHARGRES:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            DeliveryCharge = resobject.getDouble("DeliveryCharges");
                            DeliveryDistance = resobject.getString("Distance");
                            delivery_amt.setText(getString(R.string.currency) + String.format("%.2f", resobject.getDouble("DeliveryCharges")));
                            double total = Double.parseDouble(StringTotalAmt) + DeliveryCharge;
                            updatetotal(String.valueOf(total));
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
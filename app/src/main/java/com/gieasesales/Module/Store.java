package com.gieasesales.Module;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.StoreAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.GET_STORES;
import static com.gieasesales.utils.Util.SHOP_FILTER;
import static com.gieasesales.utils.Util.getData;


public class Store extends Fragment implements View.OnClickListener {

    //unable to push
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    List<Map<String, String>> MasterData = new ArrayList<>();
    JSONArray MasterARRAY = new JSONArray();
    ListView listView;
    StoreAdapter adapter;
    LinearLayout ListViewly, maply;
    CommonAlertDialog alert;
    TextView nodata;

    String latitude, longitude;

    EditText filterText;
    static final int REQUEST_CODE = 0;
    //GpsTracker gpsTracker;
    RadioGroup radioGroup;
    RadioButton pickup, delivery, online;
    Spinner SpinShopType, SpinSortBy, SpinShop;
    ImageView back_arrow, change;

    public Store() {
        // Required empty public constructor
    }

    public static Store newInstance(String param1, String param2) {
        Store fragment = new Store();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.store, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        listView = rootView.findViewById(R.id.listview);
        ListViewly = rootView.findViewById(R.id.listviewly);
        maply = rootView.findViewById(R.id.maply);
        filterText = rootView.findViewById(R.id.search);
        nodata = rootView.findViewById(R.id.nodata);
        radioGroup = rootView.findViewById(R.id.radiogroup);
        pickup = rootView.findViewById(R.id.pickup);
        delivery = rootView.findViewById(R.id.delivery);
        online = rootView.findViewById(R.id.online);
        SpinShopType = rootView.findViewById(R.id.spin_shoptype);
        SpinShop = rootView.findViewById(R.id.shop);
        SpinSortBy = rootView.findViewById(R.id.SpinSortBy);
        change = rootView.findViewById(R.id.change);
        back_arrow = rootView.findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);
        ListCollection = new ArrayList<>();
        alert = new CommonAlertDialog(getActivity());
        ListViewly.setOnClickListener(this);
        maply.setOnClickListener(this);
        change.setOnClickListener(this);

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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
               /* delivertye= 1 or 3 = pickup
                delivertye= 2 or 3 = delivery
                CashFreeVendorCode!="" = online*/
                if (checkedId == R.id.pickup) {
                    SortShop("DeliveryTypeId", "1", "3");
                } else if (checkedId == R.id.delivery) {
                    SortShop("DeliveryTypeId", "2", "3");
                } else if (checkedId == R.id.online) {
                    SortOnline("CashFreeVendorCode");
                }
            }

        });
        SpinShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                // SpinShopType = String.valueOf(SpinPaymentMode.getSelectedItemPosition() + 1);
                if (SpinShopType.getSelectedItem().toString().equalsIgnoreCase("ALL")) {
                    GetStore();
                } else {
                    Log.e("POSTypeId", String.valueOf(SpinShopType.getSelectedItemPosition()));
                    SortbyCatagory("POSTypeId", String.valueOf(SpinShopType.getSelectedItemPosition() + 1));
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        GetFilterDropdown();
        SortByDropdown();
        ShopDropdown();
        return rootView;
    }

    private void ShopDropdown() {

        final List<String> spinnerlist = new ArrayList<>();
        spinnerlist.add("Engaged Shop");
        spinnerlist.add("All Shop");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                        (getActivity(), R.layout.spinner_filtertv,
                                spinnerlist);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                SpinShop.setAdapter(spinnerArrayAdapter);
            }
        });

        SpinShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                if (SpinShop.getSelectedItem().toString().equalsIgnoreCase("All Shop")) {
                    GetStore();
                } else {

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void SortByDropdown() {

        final List<String> spinnerlist = new ArrayList<>();
        //spinnerlist.add("Custom Rating");
        spinnerlist.add("Rating");
        spinnerlist.add("Shops");
        spinnerlist.add("Distance");
        spinnerlist.add("Door Delivery");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                        (getActivity(), R.layout.spinner_filtertv,
                                spinnerlist);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                SpinSortBy.setAdapter(spinnerArrayAdapter);
            }
        });

        SpinSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {

                if (SpinSortBy.getSelectedItem().toString().equalsIgnoreCase("Distance")) {
                    SortbyDistance("Distance", false);
                } else if (SpinSortBy.getSelectedItem().toString().equalsIgnoreCase("Rating")) {
                    SortbyDistance("AvgRating", false);
                } else if (SpinSortBy.getSelectedItem().toString().equalsIgnoreCase("Shops")) {
                    SortbyDistance("PosName", true);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void SortbyDistance(String data, boolean shop) {
        // Log.e("SORT VALUE", "" + Util.sortbydistance(jsonArray, "DeliveryTypeId"));
        ListCollection = new ArrayList<>();
        JSONArray array = null;
        if (shop) {
            array = Util.sortbyshop(MasterARRAY, data);
        } else {
            array = Util.sortbydistance(MasterARRAY, data);
        }
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject imageobject = array.getJSONObject(i);
                DataHashMap = new HashMap<String, String>();
                DataHashMap.put("POSId", imageobject.getString("POSId"));
                DataHashMap.put("POSTypeId", imageobject.getString("POSTypeId"));
                DataHashMap.put("Address", imageobject.getString("Address"));
                DataHashMap.put("EmailId", imageobject.getString("EmailId"));
                DataHashMap.put("PhoneNo", imageobject.getString("PhoneNo"));
                DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                DataHashMap.put("ShopTypeImagePath", imageobject.getString("ShopTypeImagePath"));

                if (imageobject.getString("ShopTypeImagePath").contains(".jpg") || imageobject.getString("ShopTypeImagePath").contains(".png")|| imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("PrdImagePath").contains(".PNG")|| imageobject.getString("ShopTypeImagePath").contains(".jpeg")) {
                    DataHashMap.put("imgavailable", "true");
                } else {
                    DataHashMap.put("imgavailable", "false");
                }

                DataHashMap.put("PosType", imageobject.getString("PosType"));
                DataHashMap.put("AvgRating", imageobject.getString("AvgRating"));
                DataHashMap.put("TotalReviews", imageobject.getString("TotalReviews"));
                DataHashMap.put("MDName", imageobject.getString("MDName"));
                DataHashMap.put("DeliveryFromTime", imageobject.getString("DeliveryFromTime"));
                DataHashMap.put("DeliveryToTime", imageobject.getString("DeliveryToTime"));
                DataHashMap.put("PosName", imageobject.getString("PosName"));
                DataHashMap.put("Latitude", imageobject.getString("Latitude"));
                DataHashMap.put("Longitude", imageobject.getString("Longitude"));
                DataHashMap.put("IsSubCatAvailable", imageobject.getString("IsSubCatAvailable"));
                DataHashMap.put("DeliveryTypeId", imageobject.getString("DeliveryTypeId"));
                DataHashMap.put("Terms", imageobject.getString("Terms"));
                DataHashMap.put("City", imageobject.getString("City"));
                DataHashMap.put("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                DataHashMap.put("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));
                DataHashMap.put("Distance", imageobject.getString("Distance"));
                DataHashMap.put("CashFreeVendorCode", imageobject.getString("CashFreeVendorCode"));
                Log.e("data", imageobject.getString(data));
                ListCollection.add(DataHashMap);

            }
            if (ListCollection.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                // adapter = new StoreAdapter(getActivity(), ListCollection,bannerarray);
                listView.setAdapter(adapter);
            } else {
                alert.build(getString(R.string.noshop_available),false);
                listView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void GetFilterDropdown() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("PosTypeId", "0");
            Util.Logcat.e("SHOP TYPE MODE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(getActivity(), params.toString(), SHOP_FILTER, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        // alert.build(getString(R.string.timeout_error),false);
                    } else {
                        // alert.build(getString(R.string.server_error),false);
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
                        spinnerlistSub.add("ALL");

                        JSONArray dispo = resobject.getJSONArray("_lstGetPOSTypeOutputModel");
                        for (int j = 0; j < dispo.length(); j++) {
                            JSONObject object = dispo.getJSONObject(j);
                            Util.Logcat.e("PosTypeId" + "" + object.getString("PosTypeId"));
                            spinnerlistSub.add(object.getString("POSTypeDesc"));
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                                        (getActivity(), R.layout.spinner_filtertv,
                                                spinnerlistSub);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                                SpinShopType.setAdapter(spinnerArrayAdapter);
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

    private void SortbyCatagory(final String filterdata, final String value1) {
        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {
            DataHashMap = new HashMap<String, String>();
            if (imageobject.get(filterdata).equalsIgnoreCase(value1)) {
                DataHashMap.put("POSId", imageobject.get("POSId"));
                DataHashMap.put("POSTypeId", imageobject.get("POSTypeId"));
                DataHashMap.put("Address", imageobject.get("Address"));
                DataHashMap.put("EmailId", imageobject.get("EmailId"));
                DataHashMap.put("PhoneNo", imageobject.get("PhoneNo"));
                DataHashMap.put("ImagePath", imageobject.get("ImagePath"));
                DataHashMap.put("ShopTypeImagePath", imageobject.get("ShopTypeImagePath"));

                if (imageobject.get("ShopTypeImagePath").contains(".jpg") || imageobject.get("ShopTypeImagePath").contains(".png") || imageobject.get("ShopTypeImagePath").contains(".PNG") || imageobject.get("ShopTypeImagePath").contains(".jpeg")) {
                    DataHashMap.put("imgavailable", "true");
                } else {
                    DataHashMap.put("imgavailable", "false");
                }

                DataHashMap.put("PosType", imageobject.get("PosType"));
                DataHashMap.put("AvgRating", imageobject.get("AvgRating"));
                DataHashMap.put("TotalReviews", imageobject.get("TotalReviews"));
                DataHashMap.put("MDName", imageobject.get("MDName"));
                DataHashMap.put("DeliveryFromTime", imageobject.get("DeliveryFromTime"));
                DataHashMap.put("DeliveryToTime", imageobject.get("DeliveryToTime"));
                DataHashMap.put("PosName", imageobject.get("PosName"));
                DataHashMap.put("Latitude", imageobject.get("Latitude"));
                DataHashMap.put("Longitude", imageobject.get("Longitude"));
                DataHashMap.put("IsSubCatAvailable", imageobject.get("IsSubCatAvailable"));
                DataHashMap.put("DeliveryTypeId", imageobject.get("DeliveryTypeId"));
                DataHashMap.put("Terms", imageobject.get("Terms"));
                DataHashMap.put("City", imageobject.get("City"));
                DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                DataHashMap.put("Distance", imageobject.get("Distance"));
                DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                Log.e("DeliveryTypeId", "PICKUP/DELIVERY: " + imageobject.get("DeliveryTypeId"));
                ListCollection.add(DataHashMap);
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            //  adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
        } else {
            alert.build(getString(R.string.noshop_available),false);
            listView.setVisibility(View.GONE);
        }

    }

    private void SortShop(final String filterdata, final String value1, final String value2) {
        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {
            DataHashMap = new HashMap<String, String>();
            if (imageobject.get(filterdata).contains(value1) || imageobject.get(filterdata).contains(value2)) {
                DataHashMap.put("POSId", imageobject.get("POSId"));
                DataHashMap.put("POSTypeId", imageobject.get("POSTypeId"));
                DataHashMap.put("Address", imageobject.get("Address"));
                DataHashMap.put("EmailId", imageobject.get("EmailId"));
                DataHashMap.put("PhoneNo", imageobject.get("PhoneNo"));
                DataHashMap.put("ImagePath", imageobject.get("ImagePath"));
                DataHashMap.put("ShopTypeImagePath", imageobject.get("ShopTypeImagePath"));

                if (imageobject.get("ShopTypeImagePath").contains(".jpg") || imageobject.get("ShopTypeImagePath").contains(".png")|| imageobject.get("ShopTypeImagePath").contains(".PNG") || imageobject.get("ShopTypeImagePath").contains(".jpeg")) {
                    DataHashMap.put("imgavailable", "true");
                } else {
                    DataHashMap.put("imgavailable", "false");
                }

                DataHashMap.put("PosType", imageobject.get("PosType"));
                DataHashMap.put("AvgRating", imageobject.get("AvgRating"));
                DataHashMap.put("TotalReviews", imageobject.get("TotalReviews"));
                DataHashMap.put("MDName", imageobject.get("MDName"));
                DataHashMap.put("DeliveryFromTime", imageobject.get("DeliveryFromTime"));
                DataHashMap.put("DeliveryToTime", imageobject.get("DeliveryToTime"));
                DataHashMap.put("PosName", imageobject.get("PosName"));
                DataHashMap.put("Latitude", imageobject.get("Latitude"));
                DataHashMap.put("Longitude", imageobject.get("Longitude"));
                DataHashMap.put("IsSubCatAvailable", imageobject.get("IsSubCatAvailable"));
                DataHashMap.put("DeliveryTypeId", imageobject.get("DeliveryTypeId"));
                DataHashMap.put("Terms", imageobject.get("Terms"));
                DataHashMap.put("City", imageobject.get("City"));
                DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                DataHashMap.put("Distance", imageobject.get("Distance"));
                DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                Log.e("DeliveryTypeId", "PICKUP/DELIVERY: " + imageobject.get("DeliveryTypeId"));
                ListCollection.add(DataHashMap);
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            //  adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
        } else {
            alert.build(getString(R.string.noshop_available),false);
            listView.setVisibility(View.GONE);
        }

    }

    private void SortOnline(final String filterdata) {
        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {
            DataHashMap = new HashMap<String, String>();
            if (!imageobject.get(filterdata).isEmpty()) {
                DataHashMap.put("POSId", imageobject.get("POSId"));
                DataHashMap.put("POSTypeId", imageobject.get("POSTypeId"));
                DataHashMap.put("Address", imageobject.get("Address"));
                DataHashMap.put("EmailId", imageobject.get("EmailId"));
                DataHashMap.put("PhoneNo", imageobject.get("PhoneNo"));
                DataHashMap.put("ImagePath", imageobject.get("ImagePath"));
                DataHashMap.put("ShopTypeImagePath", imageobject.get("ShopTypeImagePath"));

                if (imageobject.get("ShopTypeImagePath").contains(".jpg") || imageobject.get("ShopTypeImagePath").contains(".png")|| imageobject.get("ShopTypeImagePath").contains(".PNG") || imageobject.get("ShopTypeImagePath").contains(".jpeg")) {
                    DataHashMap.put("imgavailable", "true");
                } else {
                    DataHashMap.put("imgavailable", "false");
                }

                DataHashMap.put("PosType", imageobject.get("PosType"));
                DataHashMap.put("AvgRating", imageobject.get("AvgRating"));
                DataHashMap.put("TotalReviews", imageobject.get("TotalReviews"));
                DataHashMap.put("MDName", imageobject.get("MDName"));
                DataHashMap.put("DeliveryFromTime", imageobject.get("DeliveryFromTime"));
                DataHashMap.put("DeliveryToTime", imageobject.get("DeliveryToTime"));
                DataHashMap.put("PosName", imageobject.get("PosName"));
                DataHashMap.put("Latitude", imageobject.get("Latitude"));
                DataHashMap.put("Longitude", imageobject.get("Longitude"));
                DataHashMap.put("IsSubCatAvailable", imageobject.get("IsSubCatAvailable"));
                DataHashMap.put("DeliveryTypeId", imageobject.get("DeliveryTypeId"));
                DataHashMap.put("Terms", imageobject.get("Terms"));
                DataHashMap.put("City", imageobject.get("City"));
                DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                DataHashMap.put("Distance", imageobject.get("Distance"));
                DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                Log.e("DeliveryTypeId", "ONLINE: " + imageobject.get("DeliveryTypeId"));
                ListCollection.add(DataHashMap);
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            //     adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
        } else {
            alert.build(getString(R.string.noshop_available),false);
            listView.setVisibility(View.GONE);
        }
    }

    private void GetStore() {
        ListCollection.clear();
        MasterARRAY = new JSONArray();
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            //  obj.put("Latitude", "0");
            //  obj.put("Longitude", "0");
            Util.Logcat.e("GET STORE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(getActivity(), params.toString(), GET_STORES, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(getActivity(), getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(getActivity(), getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("GET STORE:::" + Util.Decrypt(response.getString("Postresponse")));

                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        JSONArray jsonArray = resobject.optJSONArray("_lstPOSDetailsOutputModels");
                        MasterARRAY = resobject.optJSONArray("_lstPOSDetailsOutputModels");
                        // Log.e("SORT VALUE", "" + Util.sortbydistance(jsonArray, "DeliveryTypeId"));
                        // JSONArray jsonArrays = Util.sortbydistance(jsonArray, "DeliveryTypeId");
                        // TxtNoData.setVisibility(View.VISIBLE);
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("POSId", imageobject.getString("POSId"));
                                DataHashMap.put("POSTypeId", imageobject.getString("POSTypeId"));
//                              DataHashMap.put("Address", imageobject.getString("Town") + ", " + imageobject.getString("City") + ", " + imageobject.getString("State") + ", " + imageobject.getString("Pincode"));
                                DataHashMap.put("Address", imageobject.getString("Address"));
                                DataHashMap.put("EmailId", imageobject.getString("EmailId"));
                                DataHashMap.put("PhoneNo", imageobject.getString("PhoneNo"));
                                DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                                DataHashMap.put("ShopTypeImagePath", imageobject.getString("ShopTypeImagePath"));

                                if (imageobject.getString("ShopTypeImagePath").contains(".jpg") || imageobject.getString("ShopTypeImagePath").contains(".png")|| imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("ShopTypeImagePath").contains(".jpeg")) {
                                    DataHashMap.put("imgavailable", "true");
                                } else {
                                    DataHashMap.put("imgavailable", "false");
                                }

                                DataHashMap.put("PosType", imageobject.getString("PosType"));
                                DataHashMap.put("AvgRating", imageobject.getString("AvgRating"));
                                DataHashMap.put("TotalReviews", imageobject.getString("TotalReviews"));
                                DataHashMap.put("MDName", imageobject.getString("MDName"));
                                DataHashMap.put("DeliveryFromTime", imageobject.getString("DeliveryFromTime"));
                                DataHashMap.put("DeliveryToTime", imageobject.getString("DeliveryToTime"));
                                DataHashMap.put("PosName", imageobject.getString("PosName"));
                                DataHashMap.put("Latitude", imageobject.getString("Latitude"));
                                DataHashMap.put("Longitude", imageobject.getString("Longitude"));
                                DataHashMap.put("IsSubCatAvailable", imageobject.getString("IsSubCatAvailable"));
                                DataHashMap.put("DeliveryTypeId", imageobject.getString("DeliveryTypeId"));
                                DataHashMap.put("Terms", imageobject.getString("Terms"));
                                DataHashMap.put("City", imageobject.getString("City"));
                                DataHashMap.put("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                                DataHashMap.put("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));
                                DataHashMap.put("Distance", imageobject.getString("Distance"));
                                DataHashMap.put("CashFreeVendorCode", imageobject.getString("CashFreeVendorCode"));

                                //Log.e("DeliveryTypeId", "HAI: " + imageobject.getString("DeliveryTypeId"));
                                ListCollection.add(DataHashMap);
                                MasterData = ListCollection;
                            }
                            if (ListCollection.size() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                //    adapter = new StoreAdapter(getActivity(), ListCollection);
                                listView.setAdapter(adapter);
                            } else {
                                alert.build(getString(R.string.noshop_available),false);
                                listView.setVisibility(View.GONE);
                            }
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                if (ListViewly.getVisibility() == View.VISIBLE) {
                    change.setImageDrawable(getResources().getDrawable(R.drawable.list));
                    maply.setVisibility(View.VISIBLE);
                    ListViewly.setVisibility(View.GONE);

                } else {
                    change.setImageDrawable(getResources().getDrawable(R.drawable.marker));
                    ListViewly.setVisibility(View.VISIBLE);
                    maply.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

}
package com.gieasesales.Module;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.activity.BikeFilterActivity;
import com.gieasesales.activity.CarFilterActivity;
import com.gieasesales.activity.CatogaryActivity;
import com.gieasesales.activity.ExploreActivity;
import com.gieasesales.activity.PickLocation;
import com.gieasesales.activity.ScanActivity;
import com.gieasesales.adapter.ShowCatagoryAdapter;
import com.gieasesales.adapter.StoreAdapter;
import com.gieasesales.interfaces.RecyclerTouchListener;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.interfaces.click;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.DividerDecorator;
import com.gieasesales.utils.GpsTracker;
import com.gieasesales.utils.Util;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.gieasesales.utils.Util.GET_STORES;
import static com.gieasesales.utils.Util.SHOP_FILTER;
import static com.gieasesales.utils.Util.SortJSONArray;
import static com.gieasesales.utils.Util.getData;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class StoreMap extends Fragment implements View.OnClickListener, OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    private HashMap<String, String> DataHashMap, CatagoryDataHashMap;
    private List<Map<String, String>> ListCollection, CatagoryCollection;

    List<Map<String, String>> MasterData = new ArrayList<>();
    JSONArray MasterARRAY = new JSONArray();
    ListView listView;
    StoreAdapter adapter;
    LinearLayout ListViewly, maply, store_details, locationchangesnew;
    CommonAlertDialog alert;
    TextView nodata, title, address, explore, scan, citynew;
    //current_address;

    EditText filterText;
    RadioGroup radioGroup, CarbikeRadioGroup;
    RadioButton pickup, delivery, online, New, Used, OFFERS;
    RelativeLayout lycart;

    ImageView back_arrow, change, carbikesort;
    RecyclerView CatagoryRecyclerView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private static final String LAYER_ID = "LAYER_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    // private static final String ICON_ID = "ICON_ID";
    private static final String ICON_ID = "IFFRAN";
    List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
    JSONObject DATA = null;
    GpsTracker trackerlocation;
    JSONArray CatagoryArray;
    LinearLayout imgfilter, showcarfilter, otherfitler;
    BannerSlider bannerSlider;
    List<Banner> banners = new ArrayList<>();
    String PosType = "";
    Button btn_findcars;

    boolean bannerloop = true;

    public StoreMap() {
        // Required empty public constructor
    }

    public static StoreMap newInstance(String param1, String param2) {
        StoreMap fragment = new StoreMap();
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
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));
        View rootView = inflater.inflate(R.layout.store_map, container, false);

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
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        //  listner =new StoreMap();

        bannerSlider = rootView.findViewById(R.id.slider);
        btn_findcars = rootView.findViewById(R.id.btn_findcars);
        trackerlocation = new GpsTracker(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        listView = rootView.findViewById(R.id.listview);
        //  Locationchange = rootView.findViewById(R.id.locationchanges);
        locationchangesnew = rootView.findViewById(R.id.locationchangesnew);
        lycart = rootView.findViewById(R.id.lycart);
        //  Locationchange.setVisibility(View.INVISIBLE);
        lycart.setVisibility(View.INVISIBLE);
        // listView.setExpanded(true);
        title = rootView.findViewById(R.id.title);
        address = rootView.findViewById(R.id.address);
        showcarfilter = rootView.findViewById(R.id.showcarfilter);
        otherfitler = rootView.findViewById(R.id.otherfitler);
        //city = rootView.findViewById(R.id.city);
        citynew = rootView.findViewById(R.id.citynew);
        imgfilter = rootView.findViewById(R.id.imgfilter);
        CatagoryRecyclerView = rootView.findViewById(R.id.recyclerView);
        CatagoryRecyclerView.setHasFixedSize(true);
        CatagoryRecyclerView.addItemDecoration(new DividerDecorator(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.canScrollHorizontally();
        CatagoryRecyclerView.setLayoutManager(linearLayoutManager);

        ListViewly = rootView.findViewById(R.id.listviewly);
        maply = rootView.findViewById(R.id.maply);
        filterText = rootView.findViewById(R.id.search);
        nodata = rootView.findViewById(R.id.nodata);
        radioGroup = rootView.findViewById(R.id.radiogroup);
        CarbikeRadioGroup = rootView.findViewById(R.id.CarbikeRadioGroup);
        pickup = rootView.findViewById(R.id.pickup);
        delivery = rootView.findViewById(R.id.delivery);
        explore = rootView.findViewById(R.id.explore);
        scan = rootView.findViewById(R.id.scan);

        //current_address = rootView.findViewById(R.id.current_address);

        Util.saveData("latitude", String.valueOf(trackerlocation.getLatitude()), getActivity().getApplicationContext());
        Util.saveData("longitude", String.valueOf(trackerlocation.getLongitude()), getActivity().getApplicationContext());

        upaddress(trackerlocation.getLatitude(), trackerlocation.getLongitude());

        online = rootView.findViewById(R.id.online);
        New = rootView.findViewById(R.id.New);
        Used = rootView.findViewById(R.id.Used);
        OFFERS = rootView.findViewById(R.id.OFFERS);

        change = rootView.findViewById(R.id.change);
        carbikesort = rootView.findViewById(R.id.carbikesort);
        back_arrow = rootView.findViewById(R.id.back_arrow);
        store_details = rootView.findViewById(R.id.store_details);
        back_arrow.setVisibility(View.INVISIBLE);
        ListCollection = new ArrayList<>();
        CatagoryCollection = new ArrayList<>();
        alert = new CommonAlertDialog(getActivity());
        ListViewly.setOnClickListener(this);
        maply.setOnClickListener(this);
        imgfilter.setOnClickListener(this);
        //Locationchange.setOnClickListener(this);
        locationchangesnew.setOnClickListener(this);
        //change.setOnClickListener(this);
        explore.setOnClickListener(this);
        scan.setOnClickListener(this);
        change.setOnClickListener(this);
        carbikesort.setOnClickListener(this);
        // current_address.setOnClickListener(this);
        btn_findcars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    if (PosType.equalsIgnoreCase("20")) {
                        Intent filterintent = new Intent(getActivity(), CarFilterActivity.class);
                        filterintent.putExtra("PosType", "1");
                        startActivity(filterintent);
                    } else {
                        Intent filterintent = new Intent(getActivity(), BikeFilterActivity.class);
                        filterintent.putExtra("PosType", "2");
                        startActivity(filterintent);
                    }
                }

            }
        });

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

        CarbikeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
               /* delivertye= 1 or 3 = pickup
                delivertye= 2 or 3 = delivery
                CashFreeVendorCode!="" = online*/
                if (checkedId == R.id.New) {
                    // SortShop("DeliveryTypeId", "1", "3");
                    alert.build("Filter Not applied", false);
                } else if (checkedId == R.id.Used) {
                    //  SortShop("DeliveryTypeId", "2", "3");
                    alert.build("Filter Not applied", false);
                } else if (checkedId == R.id.OFFERS) {
                    //  SortOnline("CashFreeVendorCode");
                    alert.build("Filter Not applied", false);
                }
            }

        });

        mapView.getMapAsync(this);
        // LoadBanner();


        // GetStore("0");

        return rootView;
    }


    private void upaddress(double lat, double lng) {
        String address = Util.getAddress(lat, lng, getActivity());
        //city.setText(address);
        Util.saveData("CityName",address, getActivity().getApplicationContext());
        citynew.setText(address);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        builder.setMessage(R.string.enable_gps)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void SortbyDistance(String data, boolean shop) {
        Log.e(data, String.valueOf(shop));
        ListCollection = new ArrayList<>();
        ListCollection.clear();

        JSONArray array = null;

        if (shop) {
            array = Util.sortbyshop(MasterARRAY, data);
            Log.e("SHOP BY SHOP", String.valueOf(shop));
        } else {
            // array = Util.sortbydistance(MasterARRAY, data);
            array = SortJSONArray(MasterARRAY, data);

         //   Log.e("SHOP BY DISTANCE", String.valueOf(shop));
        }

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject imageobject = array.getJSONObject(i);
              //  Log.e("DATA" + PosType, imageobject.getString("POSTypeId"));
                if (PosType.equalsIgnoreCase(imageobject.getString("POSTypeId"))) {
                    DataHashMap = new HashMap<String, String>();
                    DataHashMap.put("POSId", imageobject.getString("POSId"));
                    DataHashMap.put("ShopOffers", imageobject.getString("ShopOffers"));
                    DataHashMap.put("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));
                    DataHashMap.put("OrderAcceptanceFromTime", imageobject.getString("OrderAcceptanceFromTime"));
                    DataHashMap.put("OrderAcceptanceToTime", imageobject.getString("OrderAcceptanceToTime"));
                    DataHashMap.put("POSTypeId", imageobject.getString("POSTypeId"));
                    DataHashMap.put("Address", imageobject.getString("Address"));
                    DataHashMap.put("EmailId", imageobject.getString("EmailId"));
                    DataHashMap.put("PhoneNo", imageobject.getString("PhoneNo"));
                    DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                    DataHashMap.put("ShopTypeImagePath", imageobject.getString("ShopTypeImagePath"));

                    if (imageobject.getString("ShopTypeImagePath").contains(".jpg") || imageobject.getString("ShopTypeImagePath").contains(".png") || imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("ShopTypeImagePath").contains(".jpeg")) {
                        DataHashMap.put("imgavailable", "true");
                    } else {
                        DataHashMap.put("imgavailable", "false");
                    }

                    if (imageobject.getString("ImagePath").contains(".jpg") || imageobject.getString("ImagePath").contains(".png") || imageobject.getString("ImagePath").contains(".PNG") || imageobject.getString("ImagePath").contains(".jpeg")) {
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
                    DataHashMap.put("Town", imageobject.getString("Town"));
                    DataHashMap.put("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                    DataHashMap.put("Distance", imageobject.getString("Distance"));
                    DataHashMap.put("CashFreeVendorCode", imageobject.getString("CashFreeVendorCode"));
                    ListCollection.add(DataHashMap);
                }

            }

            if (ListCollection.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                adapter = new StoreAdapter(getActivity(), ListCollection);
                listView.setAdapter(adapter);
                setListViewHeightBasedOnItems(listView);
            } else {
                alert.build(getString(R.string.noshop_available), false);
                listView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SortbyRating(String data) {

        ListCollection = new ArrayList<>();
        ListCollection.clear();

        JSONArray array = null;

        array = Util.sortbyrating(MasterARRAY, data);

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject imageobject = array.getJSONObject(i);
              //  Log.e("SortbyRating :" + PosType, imageobject.getString("POSTypeId"));
                if (PosType.equalsIgnoreCase(imageobject.getString("POSTypeId"))) {
                    DataHashMap = new HashMap<String, String>();
                    DataHashMap.put("POSId", imageobject.getString("POSId"));
                    DataHashMap.put("ShopOffers", imageobject.getString("ShopOffers"));
                    DataHashMap.put("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));
                    DataHashMap.put("OrderAcceptanceFromTime", imageobject.getString("OrderAcceptanceFromTime"));
                    DataHashMap.put("OrderAcceptanceToTime", imageobject.getString("OrderAcceptanceToTime"));
                    DataHashMap.put("POSTypeId", imageobject.getString("POSTypeId"));
                    DataHashMap.put("Address", imageobject.getString("Address"));
                    DataHashMap.put("EmailId", imageobject.getString("EmailId"));
                    DataHashMap.put("PhoneNo", imageobject.getString("PhoneNo"));
                    DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                    DataHashMap.put("ShopTypeImagePath", imageobject.getString("ShopTypeImagePath"));

                    if (imageobject.getString("ShopTypeImagePath").contains(".jpg") || imageobject.getString("ShopTypeImagePath").contains(".png") || imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("ShopTypeImagePath").contains(".jpeg")) {
                        DataHashMap.put("imgavailable", "true");
                    } else {
                        DataHashMap.put("imgavailable", "false");
                    }

                    if (imageobject.getString("ImagePath").contains(".jpg") || imageobject.getString("ImagePath").contains(".png") || imageobject.getString("ImagePath").contains(".PNG") || imageobject.getString("ImagePath").contains(".jpeg")) {
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
                    DataHashMap.put("Town", imageobject.getString("Town"));
                    DataHashMap.put("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                    DataHashMap.put("Distance", imageobject.getString("Distance"));
                    DataHashMap.put("CashFreeVendorCode", imageobject.getString("CashFreeVendorCode"));
              //      Log.e("AvgRating", imageobject.getString(data));
                    ListCollection.add(DataHashMap);
                }
            }
            if (ListCollection.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                adapter = new StoreAdapter(getActivity(), ListCollection);
                listView.setAdapter(adapter);
                setListViewHeightBasedOnItems(listView);
            } else {
                alert.build(getString(R.string.noshop_available), false);
                listView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetCatogory() {
        CatagoryCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getActivity().getApplicationContext()));
            //obj.put("PosTypeId", "0");
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
                        Util.Logcat.e("SHOP TYPE MODE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        // JSONObject obj = response.getJSONObject("subdispo");

                        //  JSONArray dispo = resobject.getJSONArray("_lstGetPOSTypeOutputModel");
                        JSONArray dispo = resobject.getJSONArray("_lstProductTypeResModel");
                        CatagoryArray = resobject.getJSONArray("_lstProductTypeResModel");
                        for (int j = 0; j < dispo.length(); j++) {
                            JSONObject object = dispo.getJSONObject(j);
                            //Util.Logcat.e("PosTypeId" + "" + object.getString("PosTypeId"));
                            if (object.getString("IsActive").equalsIgnoreCase("1")) {
                                CatagoryDataHashMap = new HashMap<>();
                                CatagoryDataHashMap.put("ProductTypeId", object.getString("ProductTypeId"));
                                CatagoryDataHashMap.put("ProductTypeDesc", object.getString("ProductTypeDesc"));
                                CatagoryDataHashMap.put("ImagePath", object.getString("ImagePath"));
                                CatagoryCollection.add(CatagoryDataHashMap);
                            }
                        }
                        if (CatagoryCollection.size() > 0) {
                            CatagoryRecyclerView.setAdapter(new ShowCatagoryAdapter(getActivity(), CatagoryCollection));
                            //  SortbyCatagory("POSTypeId", CatagoryCollection.get(0).get("ProductTypeId"));
                        } else {
                            //  alert.build("Products Not Available",false);
                        }
                        CatagoryRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                                CatagoryRecyclerView, new click() {
                            @Override
                            public void onClick(View view, final int position) {
                                //Values are passing to activity & to fragment as well
                                filterText.setText("");
                                PosType = CatagoryCollection.get(position).get("ProductTypeId");
                          //      Log.e("ProductTypeId", CatagoryCollection.get(position).get("ProductTypeId"));
                                if (CatagoryCollection.get(position).get("ProductTypeId").equalsIgnoreCase("20") || CatagoryCollection.get(position).get("ProductTypeId").equalsIgnoreCase("21")) {
                                    showcarfilter.setVisibility(View.GONE);
                                    btn_findcars.setVisibility(View.VISIBLE);

                                    if (PosType.equalsIgnoreCase("20")) {
                                        btn_findcars.setText(getString(R.string.FindCars));
                                    }
                                    if (PosType.equalsIgnoreCase("21")) {
                                        btn_findcars.setText(getString(R.string.FindBikes));
                                    }
                                    otherfitler.setVisibility(View.GONE);
                                } else {
                                    btn_findcars.setVisibility(View.GONE);
                                    showcarfilter.setVisibility(View.GONE);
                                    otherfitler.setVisibility(View.VISIBLE);
                                }
                                SortbyCatagory("POSTypeId", CatagoryCollection.get(position).get("ProductTypeId"));
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

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
        Util.Logcat.e(filterdata + "==" + value1);

        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {
            DataHashMap = new HashMap<String, String>();

            if (imageobject.get(filterdata).equalsIgnoreCase(value1)) {
                Util.Logcat.e(PosType + ">" + imageobject.get("POSTypeId"));
                if (PosType.equalsIgnoreCase(imageobject.get("POSTypeId"))) {
                    DataHashMap.put("POSId", imageobject.get("POSId"));
                    DataHashMap.put("ShopOffers", imageobject.get("ShopOffers"));
                    DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                    DataHashMap.put("OrderAcceptanceFromTime", imageobject.get("OrderAcceptanceFromTime"));
                    DataHashMap.put("OrderAcceptanceToTime", imageobject.get("OrderAcceptanceToTime"));
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
                    if (imageobject.get("ImagePath").contains(".jpg") || imageobject.get("ImagePath").contains(".png") || imageobject.get("ImagePath").contains(".PNG") || imageobject.get("ImagePath").contains(".jpeg")) {
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
                    DataHashMap.put("Town", imageobject.get("Town"));
                 //   Log.e("Town:", imageobject.get("Town"));
                    DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                    DataHashMap.put("Distance", imageobject.get("Distance"));
                    DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                    // Log.e(imageobject.get("PosName" + "::"), imageobject.get("OrderAcceptanceFromTime") + " - " + imageobject.get("OrderAcceptanceToTime"));
                    ListCollection.add(DataHashMap);
                }
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnItems(listView);
        } else {
            alert.build(getString(R.string.noshop_available), false);
            listView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void SortShop(final String filterdata, final String value1, final String value2) {
        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {

            if (imageobject.get(filterdata).contains(value1) || imageobject.get(filterdata).contains(value2)) {
                Log.e("PosType", PosType + ">" + imageobject.get("POSTypeId"));
                if (PosType.equalsIgnoreCase(imageobject.get("POSTypeId"))) {
                    DataHashMap = new HashMap<String, String>();
                    DataHashMap.put("POSId", imageobject.get("POSId"));
                    DataHashMap.put("ShopOffers", imageobject.get("ShopOffers"));
                    DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                    DataHashMap.put("OrderAcceptanceFromTime", imageobject.get("OrderAcceptanceFromTime"));
                    DataHashMap.put("OrderAcceptanceToTime", imageobject.get("OrderAcceptanceToTime"));
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
                    if (imageobject.get("ImagePath").contains(".jpg") || imageobject.get("ImagePath").contains(".png") || imageobject.get("ImagePath").contains(".PNG") || imageobject.get("ImagePath").contains(".jpeg")) {
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
                    DataHashMap.put("Town", imageobject.get("Town"));
                    DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                    DataHashMap.put("Distance", imageobject.get("Distance"));
                    DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                    //  Log.e("DeliveryTypeId", "PICKUP/DELIVERY: " + imageobject.get("DeliveryTypeId"));
                    ListCollection.add(DataHashMap);
                }
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnItems(listView);
        } else {
            alert.build(getString(R.string.noshop_available), false);
            listView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void SortOnline(final String filterdata) {
        ListCollection = new ArrayList<>();
        for (Map<String, String> imageobject : MasterData) {

            if (!imageobject.get(filterdata).isEmpty() && PosType.equalsIgnoreCase(imageobject.get("POSTypeId"))) {
                DataHashMap = new HashMap<String, String>();
                DataHashMap.put("POSId", imageobject.get("POSId"));
                DataHashMap.put("ShopOffers", imageobject.get("ShopOffers"));
                DataHashMap.put("OrderAcceptanceFlag", imageobject.get("OrderAcceptanceFlag"));
                DataHashMap.put("OrderAcceptanceFromTime", imageobject.get("OrderAcceptanceFromTime"));
                DataHashMap.put("OrderAcceptanceToTime", imageobject.get("OrderAcceptanceToTime"));
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
                if (imageobject.get("ImagePath").contains(".jpg") || imageobject.get("ImagePath").contains(".png") || imageobject.get("ImagePath").contains(".PNG") || imageobject.get("ImagePath").contains(".jpeg")) {
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
                DataHashMap.put("Town", imageobject.get("Town"));
                DataHashMap.put("ShopPromotionMsg", imageobject.get("ShopPromotionMsg"));
                DataHashMap.put("Distance", imageobject.get("Distance"));
                DataHashMap.put("CashFreeVendorCode", imageobject.get("CashFreeVendorCode"));
                // Log.e("DeliveryTypeId", "ONLINE: " + imageobject.get("DeliveryTypeId"));
                ListCollection.add(DataHashMap);
            }
        }
        if (ListCollection.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            adapter = new StoreAdapter(getActivity(), ListCollection);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnItems(listView);
        } else {
            alert.build(getString(R.string.noshop_available), false);
            listView.setVisibility(View.GONE);
        }
    }

    private void GetStore(String consumerid) {

        if (consumerid.equalsIgnoreCase("0")) {
            Util.Logcat.e("ALL shops");
        } else {
            Util.Logcat.e("Engaged shops :" + consumerid);
        }

        ListCollection = new ArrayList<>();
        MasterARRAY = new JSONArray();


        try {
            JSONObject obj = new JSONObject();
            // obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("ConsumerId", consumerid);
            obj.put("City", citynew.getText().toString());
            obj.put("Latitude", getData("latitude", getActivity().getApplicationContext()));
            obj.put("Longitude", getData("longitude", getActivity().getApplicationContext()));

            Util.Logcat.e("GET STORE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(getActivity(), params.toString(), GET_STORES, new VolleyResponseListener() {
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
                        Util.Logcat.e("GET STORE:::" + Util.Decrypt(response.getString("Postresponse")));

                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstPOSDetailsOutputModels");

                        MasterARRAY = resobject.optJSONArray("_lstPOSDetailsOutputModels");
                        JSONArray _PromotionsAndOfferPOSResModel = resobject.optJSONArray("_PromotionsAndOfferPOSResModel");

                        Util.saveData("_PromotionsAndOfferPOSResModel", String.valueOf(_PromotionsAndOfferPOSResModel), getActivity().getApplicationContext());
                        Util.Logcat.e("_PromotionsAndOfferPOSResModel :" + resobject.optJSONArray("_PromotionsAndOfferPOSResModel"));

                        Util.Logcat.e("_PromotionsAndOfferPOSResModel:" + _PromotionsAndOfferPOSResModel.length());


                        if (bannerloop) {
                            for (int i = 0; i < _PromotionsAndOfferPOSResModel.length(); i++) {
                                JSONObject bannerobject = _PromotionsAndOfferPOSResModel.getJSONObject(i);
                                if (bannerobject.getString("BannerTypeId").equalsIgnoreCase("1")) {
                                    banners.add(new RemoteBanner(bannerobject.getString("ImagePath")));
                                    Util.Logcat.e(bannerobject.getString("BannerTypeId") + ">" + bannerobject.getString("ImagePath"));
                                }
                            }
                            bannerSlider.setBanners(banners);
                        }

                        bannerloop = false;

                        for (int i = 0; i < _PromotionsAndOfferPOSResModel.length(); i++) {
                            JSONObject bannerobject = _PromotionsAndOfferPOSResModel.getJSONObject(i);
                            if (bannerobject.getString("BannerTypeId").equalsIgnoreCase("2")) {
                                Util.saveData("listimage", bannerobject.getString("ImagePath"), getActivity().getApplicationContext());
                                Util.saveData("ProdcutOrderDisplay", bannerobject.getString("ProdcutOrderDisplay"), getActivity().getApplicationContext());
                                Util.Logcat.e("ProdcutOrderDisplay :" + bannerobject.getString("ProdcutOrderDisplay"));
                                break;
                            }
                        }

                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                       //         Util.Logcat.e("PosType :" + imageobject.getString("PosType"));
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("POSId", imageobject.getString("POSId"));
                                DataHashMap.put("ShopOffers", imageobject.getString("ShopOffers"));
                                DataHashMap.put("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));
                                DataHashMap.put("OrderAcceptanceFromTime", imageobject.getString("OrderAcceptanceFromTime"));
                                DataHashMap.put("OrderAcceptanceToTime", imageobject.getString("OrderAcceptanceToTime"));
                                DataHashMap.put("POSTypeId", imageobject.getString("POSTypeId"));
//                              DataHashMap.put("Address", imageobject.getString("Town") + ", " + imageobject.getString("Town") + ", " + imageobject.getString("State") + ", " + imageobject.getString("Pincode"));
                                DataHashMap.put("Address", imageobject.getString("Address"));
                                DataHashMap.put("EmailId", imageobject.getString("EmailId"));
                                DataHashMap.put("PhoneNo", imageobject.getString("PhoneNo"));
                                DataHashMap.put("ImagePath", imageobject.getString("ImagePath"));
                                DataHashMap.put("ShopTypeImagePath", imageobject.getString("ShopTypeImagePath"));

                                if (imageobject.getString("ShopTypeImagePath").contains(".jpg") || imageobject.getString("ShopTypeImagePath").contains(".png") || imageobject.getString("ShopTypeImagePath").contains(".PNG") || imageobject.getString("ShopTypeImagePath").contains(".jpeg")) {
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
                                DataHashMap.put("Town", imageobject.getString("Town"));
                                DataHashMap.put("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                                DataHashMap.put("Distance", imageobject.getString("Distance"));
                                DataHashMap.put("CashFreeVendorCode", imageobject.getString("CashFreeVendorCode"));
                                // Log.e(imageobject.getString("PosName") + "::", imageobject.getString("OrderAcceptanceFlag"));
                                //  Log.e("DeliveryTypeId", "HAI: " + imageobject.getString("DeliveryTypeId"));
                                //Log.e("LAT", imageobject.getString("Latitude"));
                                //Log.e("LNG", imageobject.getString("Longitude"));

                               /* Feature singleFeatureOne = Feature.fromGeometry(
                                        Point.fromLngLat(Double.parseDouble(imageobject.getString("Longitude")), Double.parseDouble(imageobject.getString("Latitude"))));
                                singleFeatureOne.addStringProperty("PosName", imageobject.getString("PosName"));
                                singleFeatureOne.addStringProperty("POSId", imageobject.getString("POSId"));
                                singleFeatureOne.addStringProperty("Address", imageobject.getString("Address"));
                                singleFeatureOne.addStringProperty("ImagePath", imageobject.getString("ImagePath"));
                                //singleFeatureOne.addStringProperty("CategoryId", imageobject.getString("CategoryId"));
                                singleFeatureOne.addStringProperty("ShopPromotionMsg", imageobject.getString("ShopPromotionMsg"));
                                singleFeatureOne.addStringProperty("IsSubCatAvailable", imageobject.getString("IsSubCatAvailable"));
                                singleFeatureOne.addStringProperty("DeliveryTypeId", imageobject.getString("DeliveryTypeId"));
                                singleFeatureOne.addStringProperty("Terms", imageobject.getString("Terms"));
                                symbolLayerIconFeatureList.add(singleFeatureOne);
                                */

                                //    Log.e("OrderAcceptanceFlag", imageobject.getString("OrderAcceptanceFlag"));

                                ListCollection.add(DataHashMap);
                                MasterData = ListCollection;
                            }

                            if (ListCollection.size() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                adapter = new StoreAdapter(getActivity(), ListCollection);
                                listView.setAdapter(adapter);
                                setListViewHeightBasedOnItems(listView);
                                //setListViewHeightBasedOnItems(listView);
                                if (CatagoryCollection.size() > 0) {
                                    PosType = CatagoryCollection.get(0).get("ProductTypeId");
                                    SortbyCatagory("POSTypeId", CatagoryCollection.get(0).get("ProductTypeId"));
                                }
                            } else {
                                alert.build(getString(R.string.noshop_available), false);
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
        //listner =getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carbikesort:
                break;
            case R.id.imgfilter:
                CustomDialog();
                break;
            case R.id.change:
                /*Intent location = new Intent(getActivity(), PickLocation.class);
                getActivity().startActivity(location);*/
                break;
            /*case R.id.locationchanges:
                Intent location = new Intent(getActivity(), PickLocation.class);
                getActivity().startActivity(location);
                break;*/
            case R.id.locationchangesnew:



                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent locationnew = new Intent(getActivity(), PickLocation.class);
                    getActivity().startActivity(locationnew);
                }


                break;

            case R.id.scan:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    try {
                        Intent scan = new Intent(getActivity(), ScanActivity.class);
                        scan.putExtra("PosId", DATA.getJSONObject("properties").getString("POSId"));
                        getActivity().startActivity(scan);
                        Log.e("CLICKED", DATA.getJSONObject("properties").getString("PosName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.explore:

                try {
                    Util.saveData("DeliveryTypeId", DATA.getJSONObject("properties").getString("DeliveryTypeId"), getActivity().getApplicationContext());
                    Util.saveData("Terms", DATA.getJSONObject("properties").getString("Terms"), getActivity().getApplicationContext());

                    if (DATA.getJSONObject("properties").getString("IsSubCatAvailable").equalsIgnoreCase("0")) {
                        //Show sub catagory

                        Intent explore = new Intent(getActivity(), ExploreActivity.class);
                        explore.putExtra("POSId", DATA.getJSONObject("properties").getString("POSId"));
                        explore.putExtra("Address", DATA.getJSONObject("properties").getString("Address"));
                        explore.putExtra("PosName", DATA.getJSONObject("properties").getString("PosName"));
                        explore.putExtra("IsSubCatAvailable", DATA.getJSONObject("properties").getString("IsSubCatAvailable"));
                        explore.putExtra("ImagePath", DATA.getJSONObject("properties").getString("ImagePath"));
                        explore.putExtra("CategoryId", "0");
                        explore.putExtra("ShopPromotionMsg", DATA.getJSONObject("properties").getString("ShopPromotionMsg"));
                        explore.putExtra("IsSubCatAvailable", DATA.getJSONObject("properties").getString("IsSubCatAvailable"));
                        getActivity().startActivity(explore);

                    } else {

                        //Show  catagory
                        Intent explore = new Intent(getActivity(), CatogaryActivity.class);
                        explore.putExtra("POSId", DATA.getJSONObject("properties").getString("POSId"));
                        explore.putExtra("Address", DATA.getJSONObject("properties").getString("Address"));
                        explore.putExtra("PosName", DATA.getJSONObject("properties").getString("PosName"));
                        explore.putExtra("IsSubCatAvailable", DATA.getJSONObject("properties").getString("IsSubCatAvailable"));
                        explore.putExtra("ImagePath", DATA.getJSONObject("properties").getString("ImagePath"));
                        explore.putExtra("ShopPromotionMsg", DATA.getJSONObject("properties").getString("ShopPromotionMsg"));
                        explore.putExtra("IsSubCatAvailable", DATA.getJSONObject("properties").getString("IsSubCatAvailable"));
                        getActivity().startActivity(explore);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
           /* case R.id.locationchanges:
                if (ListViewly.getVisibility() == View.VISIBLE) {
                    change.setImageDrawable(getResources().getDrawable(R.drawable.list));
                    maply.setVisibility(View.VISIBLE);
                    ListViewly.setVisibility(View.GONE);
                } else {
                    change.setImageDrawable(getResources().getDrawable(R.drawable.marker));
                    ListViewly.setVisibility(View.VISIBLE);
                    maply.setVisibility(View.GONE);
                }
                break;*/

            default:
                break;
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        StoreMap.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                enableLocationComponent(style);
                Log.e("HAI", "CALL API");

                upaddress(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

// Add the SymbolLayer icon image to the map style
                        .withImage(ICON_ID, BitmapFactory.decodeResource(
                                StoreMap.this.getResources(), R.drawable.mapbox_marker_icon_default))

// Adding a GeoJson source for the SymbolLayer icons.
                        .withSource(new GeoJsonSource(SOURCE_ID,
                                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

// Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
// marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
// the coordinate point. This is offset is not always needed and is dependent on the image
// that you use for the SymbolLayer icon.
                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(PropertyFactory.iconImage(ICON_ID),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})
                                )
                        ));

            }
        });
        mapboxMap.addOnMapClickListener(this);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getActivity())
                    .pulseEnabled(true)
                    .build();

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);

//            Log.e("getLatitude", "" + locationComponent.getLastKnownLocation().getLatitude());
            //        Log.e("getLongitude", "" + locationComponent.getLastKnownLocation().getLongitude());
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            //finish();
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

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    private boolean handleClickIcon(PointF screenPoint) {
        store_details.setVisibility(View.VISIBLE);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
        if (!features.isEmpty()) {
            // Show the Feature in the TextView to show that the icon is based on the ICON_PROPERTY key/value
            // TextView featureInfoTextView = findViewById(R.id.);
            // title.setText(features.get(0).toJson());
            Log.e("DATA", features.get(0).toJson());

            //    JSONObject OBJECT= new  JSONObject(features.get(0).toJson());

            try {

                DATA = new JSONObject(features.get(0).toJson());
                Log.e("SHOP NAME", DATA.getJSONObject("properties").getString("PosName"));
                title.setText(DATA.getJSONObject("properties").getString("PosName"));
                address.setText(DATA.getJSONObject("properties").getString("Address"));

            } catch (JSONException e) {

            }

            return true;
        } else {
            return false;
        }
    }

    // SortbyCatagory("POSTypeId", position.get("ProductTypeId"));
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        btn_findcars.setVisibility(View.GONE);

        //GetStore("0");
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            progressDialog.show();
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    upaddress(Double.parseDouble(Util.getData("latitude", getActivity().getApplicationContext())), Double.parseDouble(Util.getData("longitude", getActivity().getApplicationContext())));
                }
            }, 3000);
        }

    }

    private void CustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.cutom_filter_alert);
        // set the custom dialog components - text, image and button
        final ListView mainlist = dialog.findViewById(R.id.mainlist);
        final ListView sublist = dialog.findViewById(R.id.sublist);
        final ArrayList<String> listone = new ArrayList<>();
        listone.add("Sort");
        listone.add("Shops");

        final ArrayList<String> one = new ArrayList<>();

        mainlist.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, listone));

        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sublist.setVisibility(View.VISIBLE);

                if (position == 0) {
                    one.clear();
                    one.add("Rating");
                    one.add("Shops");
                    one.add("Distance");
                    //  one.add("Door Delivery");
                    one.add("Pickup");
                    one.add("Delivery");
                    one.add("PG");
                    //   one.add("Offers");
                }
                if (position == 1) {
                    one.clear();

                    if (getData("IsShopAvailable", getActivity().getApplicationContext()).equalsIgnoreCase("0")) {
                        one.add("All Shop");
                        one.add("Engaged Shop");
                        Log.e("IsShopAvailable", getData("IsShopAvailable", getActivity().getApplicationContext()));
                    } else {
                        one.add("Engaged Shop");
                        one.add("All Shop");
                        Log.e("IsShopAvailable", getData("IsShopAvailable", getActivity().getApplicationContext()));
                    }

                }

                sublist.setAdapter(new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, one));
                for (int i = 0; i < mainlist.getChildCount(); i++) {
                    if (position == i) {
                        mainlist.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                    } else {
                        mainlist.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });

        sublist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // sublist.getChildAt(position).getTooltipText();
                Util.Logcat.e("FILTER :" + one.get(position));
                if (one.get(position).equalsIgnoreCase("Distance")) {
                    SortbyDistance("Distance", false);
                } else if (one.get(position).equalsIgnoreCase("Rating")) {
                    SortbyRating("AvgRating");
                } else if (one.get(position).equalsIgnoreCase("Shops")) {
                    SortbyDistance("PosName", true);
                } else if (one.get(position).equalsIgnoreCase("Door Delivery")) {
                    SortShop("DeliveryTypeId", "2", "3");
                } else if (one.get(position).equalsIgnoreCase("All Shop")) {
                    GetStore("0");
                    if (CatagoryCollection.size() > 0) {
                        CatagoryRecyclerView.setAdapter(new ShowCatagoryAdapter(getActivity(), CatagoryCollection));
                        //  SortbyCatagory("POSTypeId", CatagoryCollection.get(0).get("ProductTypeId"));
                    }
                } else if (one.get(position).equalsIgnoreCase("Engaged Shop")) {
                    GetStore(getData("ConsumerId", getActivity().getApplicationContext()));
                } else if (one.get(position).equalsIgnoreCase("Pickup")) {
                    SortShop("DeliveryTypeId", "1", "3");
                } else if (one.get(position).equalsIgnoreCase("Delivery")) {
                    SortShop("DeliveryTypeId", "2", "3");
                } else if (one.get(position).equalsIgnoreCase("PG")) {
                    SortOnline("CashFreeVendorCode");
                } else if (one.get(position).equalsIgnoreCase("Offers")) {

                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        GetCatogory();
        if (getData("IsShopAvailable", getActivity().getApplicationContext()).equalsIgnoreCase("0")) {
            GetStore("0");
            Util.Logcat.e("ALL SHOP :" + getData("IsShopAvailable", getActivity().getApplicationContext()));
        } else {
            GetStore(getData("ConsumerId", getActivity().getApplicationContext()));
            Util.Logcat.e("ENGAGED SHOP :" + getData("IsShopAvailable", getActivity().getApplicationContext()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
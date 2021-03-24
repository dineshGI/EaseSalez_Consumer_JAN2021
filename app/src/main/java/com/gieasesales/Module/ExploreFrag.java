package com.gieasesales.Module;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.gieasesales.Http.CallApi;
import com.gieasesales.Model.Products;
import com.gieasesales.R;
import com.gieasesales.activity.ExploreActivity;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.DividerDecorator;
import com.gieasesales.utils.Util;
import com.gieasesales.activity.CartActivity;
import com.gieasesales.adapter.ProductAdapter;
import com.gieasesales.adapter.ShowTitleAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.interfaces.clickInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.GET_CART;
import static com.gieasesales.utils.Util.GET_CATEGORY;
import static com.gieasesales.utils.Util.GET_PRODUCTLIST;
import static com.gieasesales.utils.Util.getData;
import static com.gieasesales.adapter.ProductAdapter.cart;


public class ExploreFrag extends Fragment implements clickInterface, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    //unable to push
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //private Store.OnFragmentInteractionListener mListener;
    private OnFragmentInteractionListener mListener;
    //GpsTracker gpsTracker;
    RecyclerView TitleRecyclerView;
    ListView listview;
    private HashMap<String, String> ButtonDataHashMap, DataHashMap;
    private List<Map<String, String>> ButtonListCollection, ListCollection;
    CommonAlertDialog alert;
    private clickInterface listner;
    // ItemAdapter adapter;
    ProductAdapter adapter;
    String POSId = "1", PosName = "Alpha", PosAddress = "", ImagePath = "";
    //New Model
    ArrayList<Products> orders;
    TextView TxtAmount, TxtCount, TxtViewCart, TxtNoData;
    LinearLayout LyAddtoCart;
    Double Amount = 0.0;
    ImageView ImgView;
    Toolbar toolbar;


    public ExploreFrag() {
        // Required empty public constructor
    }

    public static ExploreFrag newInstance(String param1, String param2) {
        ExploreFrag fragment = new ExploreFrag();
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

        View rootView = inflater.inflate(R.layout.explore_fragment, container, false);
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

        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setPadding(10, 0, 0, 0);
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        orders = new ArrayList<>();
        cart = new ArrayList<>();
        //ImagePath = getIntent().getStringExtra("ImagePath");

        Util.Logcat.e("ImagePath" + ImagePath);

        Util.saveData("POSId", POSId, getActivity().getApplicationContext());
        Util.saveData("PosName", PosName, getActivity().getApplicationContext());
        Util.saveData("PosAddress", PosAddress, getActivity().getApplicationContext());
        Util.saveData("ImagePath", ImagePath, getActivity().getApplicationContext());

        toolbar.setTitle(Util.getData("PosName", getActivity().getApplicationContext()));
        toolbar.setSubtitle(Util.getData("PosAddress", getActivity().getApplicationContext()));

        ButtonListCollection = new ArrayList<>();
        ListCollection = new ArrayList<>();
        listner = this;
        LyAddtoCart = rootView.findViewById(R.id.ly_addtocart);
        ImgView = rootView.findViewById(R.id.img);
        TxtNoData = rootView.findViewById(R.id.no_item);
        TitleRecyclerView = rootView.findViewById(R.id.recyclerView);
        TxtAmount = rootView.findViewById(R.id.amt);
        TxtCount = rootView.findViewById(R.id.itemcount);
        TxtViewCart = rootView.findViewById(R.id.viewcart);
        listview = rootView.findViewById(R.id.listview);
        alert = new CommonAlertDialog(getActivity());
        TitleRecyclerView.setHasFixedSize(true);
        TitleRecyclerView.addItemDecoration(new DividerDecorator(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.canScrollHorizontally();
        TitleRecyclerView.setLayoutManager(linearLayoutManager);

       /* if (ImagePath != null) {
            Glide.with(this).load(ImagePath).into(ImgView);
        }*/

        addbuttons();
        TxtViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.Logcat.e("Add to CART" + cart.toString());
                //  AddToCart();
                    Intent home = new Intent(getActivity(), CartActivity.class);
                    home.putExtra("POSId", POSId);
                    home.putExtra("PosName", PosName);
                    home.putExtra("PosAddress", PosAddress);
                    startActivity(home);

            }
        });
        return rootView;
    }

    private void addbuttons() {

        ButtonListCollection.clear();

        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("CategoryId", "0");
            Util.Logcat.e("GET CATAGORY:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(getActivity(), params.toString(), GET_CATEGORY, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                    } else {
                        alert.build(getString(R.string.server_error),false);
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_GetCategory");
                        if (jsonArray == null || jsonArray.length() == 0) {
                            Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                            //   alert.build(getString(R.string.noshop_available),false);
                            // TxtNoData.setVisibility(View.VISIBLE);
                        } else
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    ButtonDataHashMap = new HashMap<String, String>();
                                    ButtonDataHashMap.put("CategoryName", imageobject.getString("CategoryName"));
                                    ButtonDataHashMap.put("CategoryId", imageobject.getString("CategoryId"));
                                    ButtonListCollection.add(ButtonDataHashMap);
                                }
                                addlistitem(jsonArray.getJSONObject(0).getString("CategoryId"));
                                TitleRecyclerView.setAdapter(new ShowTitleAdapter(getActivity(), ButtonListCollection, listner));

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

    private void addlistitem(String CategoryId) {
        listview.setVisibility(View.GONE);
        TxtNoData.setVisibility(View.GONE);
        orders = new ArrayList<>();
        //ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("UserId", Util.getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("POSId", POSId);
            obj.put("CategoryId", CategoryId);
            Util.Logcat.e("GET ITEM LIST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(getActivity(), params.toString(), GET_PRODUCTLIST, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                    } else {
                        alert.build(getString(R.string.server_error),false);
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
                            JSONArray jsonArray = resobject.optJSONArray("_lstConsumerPrdListOutputModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                                //   alert.build(getString(R.string.noshop_available),false);
                                TxtNoData.setVisibility(View.VISIBLE);
                                // Util.ShowToast(ExploreActivity.this, "No Item for this Catagory");
                            } else {
                                listview.setVisibility(View.VISIBLE);
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject imageobject = jsonArray.getJSONObject(i);
                                       // orders.add(new Products(imageobject.getString("Productcode"), imageobject.getString("ProductId"), imageobject.getString("ProductName"), imageobject.getString("ProductTypeDesc"), imageobject.getString("UOM"), imageobject.getString("IsAllowCustomerReview"), imageobject.getDouble("SellinPrice"), imageobject.getDouble("DiscountValue"), imageobject.getDouble("MRP"), imageobject.getString("FilePath"),imageobject.getString("IsAttributeAdded")));
                                        // ListCollection.add(DataHashMap);
                                        //  Log.e("IMG",imageobject.getString("FilePath"));
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (orders.size() > 0)
                                                adapter = new ProductAdapter(getActivity(), orders,"0");
                                            listview.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            adapter.registerDataSetObserver(observer);
                                        }
                                    });
                                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            long viewId = view.getId();
                                            if (viewId == R.id.txtadd) {
                                                /*if (Util.getData("KYCStatus", getApplicationContext()).equalsIgnoreCase("1")) {
                                                    AddItem(orders.get(position).getProductid(), orders.get(position).getSellinPrice());
                                                } else {
                                                    alert.build("Update the KYC details");
                                                }*/
                                                AddItem(orders.get(position).getProductid(), orders.get(position).getSellinPrice());
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            alert.build(resobject.getString("StatusDesc"),false);
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

    private void AddItem(String productid, final Double amt) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("POSId", Util.getData("POSId", getActivity().getApplicationContext()));
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
            CallApi.postResponse(getActivity(), params.toString(), "", new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                    } else {
                        alert.build(getString(R.string.server_error),false);
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
                            // TxtCount.setText(resobject.getString("TotalPrds") + " Items");
                            // Amount = Amount + amt;
                            // TxtAmount.setText("Rs "+String.valueOf(Amount));
                            UpdateCart();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(getString(R.string.CartUpdateFailed),false);
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
            obj.put("POSId", Util.getData("POSId", getActivity().getApplicationContext()));
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            Util.Logcat.e("GET CART:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(getActivity(), params.toString(), GET_CART, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                    } else {
                        alert.build(getString(R.string.server_error),false);
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            JSONArray jsonArray = resobject.optJSONArray("_lstCartGetModel");
                            if (jsonArray == null || jsonArray.length() == 0) {
                                Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                            } else {
                                LyAddtoCart.setVisibility(View.VISIBLE);
                                TxtCount.setText(resobject.getString("TotalQty") + " Items");
                                TxtAmount.setText(getString(R.string.currency) +resobject.getString("TotalSum"));
                            }

                        } else {
                            alert.build(resobject.getString("StatusDesc"),false);
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

    public int calculateMealTotal() {
        int mealTotal = 0;
        for (Products order : orders) {
            mealTotal += order.getSellinPrice() * order.getmQuantity();
            Util.Logcat.e("order Added" + order.getProductcode());
        }
        return mealTotal;
    }

    public int calculateCount() {
        int mealTotal = 0;
        for (Products order : orders) {
            mealTotal += order.getmQuantity();
        }
        return mealTotal;
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
        addlistitem(position.get("CategoryId"));
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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}
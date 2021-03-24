package com.gieasesales.Module;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.adapter.HistoryAdapterNew;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.CANCEL_ORDER;
import static com.gieasesales.utils.Util.ORDER_HISTORY;
import static com.gieasesales.utils.Util.getData;


public class History extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;
    HistoryAdapterNew adapter;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    ListView listView;
    CommonAlertDialog alert;
    TextView FromDT, ToDT;
    Button btn_search, btn_lasttrans;
    ImageView cal_from, cal_to, back_arrow;
    EditText Search;

    public History() {
        // Required empty public constructor
    }

    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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

        View rootView = inflater.inflate(R.layout.history, container, false);
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
        FromDT = rootView.findViewById(R.id.FromDT);
        FromDT.setText(Util.getonlydate());
        ToDT = rootView.findViewById(R.id.ToDT);
        ToDT.setText(Util.getonlydate());
        btn_search = rootView.findViewById(R.id.btn_search);
        btn_lasttrans = rootView.findViewById(R.id.btn_lasttrans);
        cal_from = rootView.findViewById(R.id.cal_from);
        cal_to = rootView.findViewById(R.id.cal_to);
        ListCollection = new ArrayList<>();
        alert = new CommonAlertDialog(getActivity());
        back_arrow = rootView.findViewById(R.id.back_arrow);
        Search = rootView.findViewById(R.id.search);
        back_arrow.setVisibility(View.INVISIBLE);
        btn_search.setOnClickListener(this);
        btn_lasttrans.setOnClickListener(this);
        cal_from.setOnClickListener(this);
        cal_to.setOnClickListener(this);
        Search.addTextChangedListener(new TextWatcher() {
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
        GetRecent();
        return rootView;
    }

    private void GetHistory() {

        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("FromDT", FromDT.getText().toString());
            obj.put("ToDT", ToDT.getText().toString());
            Util.Logcat.e("GET History:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(getActivity(), params.toString(), ORDER_HISTORY, new VolleyResponseListener() {
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

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("HIS:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetIndentOutputModel1");

                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("POSName", imageobject.getString("POSName"));
                                DataHashMap.put("POSAddress", imageobject.getString("POSAddress"));
                                DataHashMap.put("POSId", imageobject.getString("POSId"));
                                DataHashMap.put("TotalQTY", imageobject.getString("TotalQTY"));
                                //DataHashMap.put("TotalAmount", Util.ChangeNumber(imageobject.getString("TotalAmount")));
                                DataHashMap.put("TotalAmount", String.format("%.2f", Double.parseDouble(imageobject.getString("TotalAmount"))));
                                DataHashMap.put("NetPayable", String.format("%.2f", Double.parseDouble(imageobject.getString("NetPayable"))));
                                //DataHashMap.put("NetPayable", Util.ChangeNumber(imageobject.getString("NetPayable")));
                                DataHashMap.put("IndentDate", imageobject.getString("IndentDate"));
                                DataHashMap.put("IndentId", imageobject.getString("IndentId"));
                                DataHashMap.put("IndentNo", imageobject.getString("IndentNo"));
                                DataHashMap.put("IndentStatus", imageobject.getString("IndentStatus"));
                                DataHashMap.put("IndentStatusDesc", imageobject.getString("IndentStatusDesc"));
                                DataHashMap.put("TotalReviews", imageobject.getString("TotalReviews"));
                                DataHashMap.put("Rating", imageobject.getString("Rating"));
                                DataHashMap.put("DeliveryTypeId", imageobject.getString("DeliveryTypeId"));
                                DataHashMap.put("PickUpDate", imageobject.getString("PickUpDate"));
                                DataHashMap.put("DeliveryFromTime", imageobject.getString("DeliveryFromTime"));
                                DataHashMap.put("DeliveryToTime", imageobject.getString("DeliveryToTime"));
                                DataHashMap.put("Feedback", imageobject.getString("Feedback"));
                                DataHashMap.put("PaymentMode", imageobject.getString("PaymentMode"));
                                DataHashMap.put("ShopImagePath", imageobject.getString("ShopImagePath"));
                                DataHashMap.put("DeliveryCharge", imageobject.getString("DeliveryCharge"));
                                DataHashMap.put("DeliveryType", imageobject.getString("DeliveryType"));
                                Util.Logcat.e("DeliveryType: "+imageobject.getString("DeliveryType"));
                                Util.Logcat.e("Address: "+imageobject.getString("Address"));
                               // DataHashMap.put("Address", imageobject.getString("Address") + " - " + imageobject.getString("Pincode"));
                                DataHashMap.put("Address", imageobject.getString("Address"));
                                if (imageobject.getString("ShopImagePath").contains(".jpg") || imageobject.getString("ShopImagePath").contains(".png") || imageobject.getString("ShopImagePath").contains(".PNG") || imageobject.getString("ShopImagePath").contains(".jpeg")) {
                                    DataHashMap.put("imgavailable", "true");
                                } else {
                                    DataHashMap.put("imgavailable", "false");
                                }
                                Util.Logcat.e(imageobject.getString("DeliveryTypeId") + "::" + imageobject.getString("POSName"));
                                // Util.Logcat.e(imageobject.getString("IndentNo") + "::" + imageobject.getString("IndentStatus") + "::" + imageobject.getString("TotalReviews"));
                                ListCollection.add(DataHashMap);
                            }

                            if (ListCollection.size() > 0) {
                                adapter = new HistoryAdapterNew(getActivity(), ListCollection);
                                listView.setAdapter(adapter);
                            } else {
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                                alert.build(getString(R.string.NoRecordsAvailable), false);
                            }
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    long viewId = view.getId();
                                    if (viewId == R.id.views) {
                                        CancelOrder(ListCollection.get(position).get(
                                                "IndentId"));
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetRecent() {

        ListCollection.clear();

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("FromDT", "");
            obj.put("ToDT", "");
            Util.Logcat.e("GET History:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(getActivity(), params.toString(), ORDER_HISTORY, new VolleyResponseListener() {
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

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        JSONArray jsonArray = resobject.optJSONArray("_lstGetIndentOutputModel1");

                        int size = 0;
                        if (jsonArray.length() >= 5) {
                            size = 5;
                        } else {
                            size = jsonArray.length();
                        }

                        try {
                            for (int i = 0; i < size; i++) {
                                JSONObject imageobject = jsonArray.getJSONObject(i);
                                DataHashMap = new HashMap<String, String>();
                                DataHashMap.put("POSName", imageobject.getString("POSName"));
                                DataHashMap.put("POSAddress", imageobject.getString("POSAddress"));
                                DataHashMap.put("POSId", imageobject.getString("POSId"));
                                DataHashMap.put("TotalQTY", imageobject.getString("TotalQTY"));
                                //DataHashMap.put("TotalAmount", Util.ChangeNumber(imageobject.getString("TotalAmount")));
                                DataHashMap.put("TotalAmount", String.format("%.2f", Double.parseDouble(imageobject.getString("TotalAmount"))));
                                DataHashMap.put("NetPayable", String.format("%.2f", Double.parseDouble(imageobject.getString("NetPayable"))));
                                //DataHashMap.put("NetPayable", Util.ChangeNumber(imageobject.getString("NetPayable")));
                                DataHashMap.put("IndentDate", imageobject.getString("IndentDate"));
                                DataHashMap.put("IndentId", imageobject.getString("IndentId"));
                                DataHashMap.put("IndentNo", imageobject.getString("IndentNo"));
                                DataHashMap.put("IndentStatus", imageobject.getString("IndentStatus"));
                                DataHashMap.put("IndentStatusDesc", imageobject.getString("IndentStatusDesc"));
                                DataHashMap.put("TotalReviews", imageobject.getString("TotalReviews"));
                                DataHashMap.put("Rating", imageobject.getString("Rating"));
                                DataHashMap.put("DeliveryTypeId", imageobject.getString("DeliveryTypeId"));
                                DataHashMap.put("PickUpDate", imageobject.getString("PickUpDate"));
                                DataHashMap.put("DeliveryFromTime", imageobject.getString("DeliveryFromTime"));
                                DataHashMap.put("DeliveryToTime", imageobject.getString("DeliveryToTime"));
                                DataHashMap.put("Feedback", imageobject.getString("Feedback"));
                                DataHashMap.put("PaymentMode", imageobject.getString("PaymentMode"));
                                DataHashMap.put("ShopImagePath", imageobject.getString("ShopImagePath"));
                                DataHashMap.put("DeliveryCharge", imageobject.getString("DeliveryCharge"));
                                DataHashMap.put("DeliveryType", imageobject.getString("DeliveryType"));
                               // DataHashMap.put("Address", imageobject.getString("Address") + " - " + imageobject.getString("Pincode"));
                                DataHashMap.put("Address", imageobject.getString("Address"));

                                if (imageobject.getString("ShopImagePath").contains(".jpg") || imageobject.getString("ShopImagePath").contains(".png") || imageobject.getString("ShopImagePath").contains(".PNG") || imageobject.getString("ShopImagePath").contains(".jpeg")) {
                                    DataHashMap.put("imgavailable", "true");
                                } else {
                                    DataHashMap.put("imgavailable", "false");
                                }
                                Util.Logcat.e(imageobject.getString("DeliveryTypeId") + "::" + imageobject.getString("POSName"));
                                // Util.Logcat.e(imageobject.getString("IndentNo") + "::" + imageobject.getString("IndentStatus") + "::" + imageobject.getString("TotalReviews"));
                                ListCollection.add(DataHashMap);
                            }

                            if (ListCollection.size() > 0) {
                                adapter = new HistoryAdapterNew(getActivity(), ListCollection);
                                listView.setAdapter(adapter);
                            } else {
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                                // adapter.notifyDataSetChanged();
                                alert.build("No Records Available", false);
                            }

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    long viewId = view.getId();
                                    if (viewId == R.id.views) {
                                        CancelOrder(ListCollection.get(position).get(
                                                "IndentId"));
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CancelOrder(String IndentId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getActivity().getApplicationContext()));
            obj.put("IndentId", IndentId);
            obj.put("Status", "6");
            obj.put("Remarks", "");
            Util.Logcat.e("CANCEL ITEM:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(getActivity(), params.toString(), CANCEL_ORDER, new VolleyResponseListener() {
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
                            //alert.build(resobject.getString("StatusDesc"));
                            GetHistory();
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

        final Calendar c = Calendar.getInstance();
        Integer mYear = c.get(Calendar.YEAR);
        Integer mMonth = c.get(Calendar.MONTH);
        Integer mDay = c.get(Calendar.DAY_OF_MONTH);

        switch (v.getId()) {

            case R.id.cal_from:
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear <= 8 && dayOfMonth > 9) {
                            String _data = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            FromDT.setText(_data);
                        } else if (monthOfYear <= 8 && dayOfMonth <= 9) {
                            String _data = "0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            FromDT.setText(_data);
                        } else {
                            if (dayOfMonth <= 9) {
                                String _data = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                FromDT.setText(_data);
                            } else {
                                String _data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                FromDT.setText(_data);
                            }

                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog1.setCancelable(false);
                datePickerDialog1.show();
                datePickerDialog1.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                break;

            case R.id.cal_to:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear <= 8 && dayOfMonth > 9) {
                            String _data = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            ToDT.setText(_data);
                        } else if (monthOfYear <= 8 && dayOfMonth <= 9) {
                            String _data = "0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            ToDT.setText(_data);
                        } else {
                            if (dayOfMonth <= 9) {
                                String _data = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                ToDT.setText(_data);
                            } else {
                                String _data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                ToDT.setText(_data);
                            }

                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                break;

            case R.id.btn_lasttrans:
                GetRecent();
                break;
            case R.id.btn_search:

                if (FromDT.getText().toString().isEmpty()) {
                    alert.build("Select From Date", false);
                } else if (ToDT.getText().toString().isEmpty()) {
                    alert.build("Select To Date", false);
                } else {
                    try {
                        if (FromDT.getText().toString().equalsIgnoreCase(ToDT.getText().toString())) {
                            GetHistory();
                        } else if (Util.Datecompare(FromDT.getText().toString(), ToDT.getText().toString())) {
                            GetHistory();
                        } else {
                            alert.build("To Date Should be greater than From Date", false);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
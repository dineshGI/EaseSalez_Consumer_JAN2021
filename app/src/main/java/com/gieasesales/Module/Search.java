package com.gieasesales.Module;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.CommonSearchAdapter;
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

import static com.gieasesales.utils.Util.UNIVERSAL_SEARCH;
import static com.gieasesales.utils.Util.getData;


public class Search extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;
    ListView listView;
    ImageView back_arrow;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    CommonAlertDialog alert;
    EditText Search;

    public Search() {
        // Required empty public constructor
    }

    //register_now
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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

        View rootView = inflater.inflate(R.layout.search, container, false);
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

        // Programmatically initialize the scanner view

        // Inflate the layout for this fragment
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        alert = new CommonAlertDialog(getActivity());
        ListCollection = new ArrayList<>();
        listView = rootView.findViewById(R.id.listView);
        Search = rootView.findViewById(R.id.search);
        back_arrow = rootView.findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= 3) {
                    Search.setEnabled(false);
                    Search.setClickable(false);
                    SearchData(String.valueOf(s));

                } /*else if (adapter != null) {
                    adapter.getFilter().filter(s);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;

    }

    private void SearchData(final String valueOf) {
        ListCollection.clear();
        try {
            JSONObject obj = new JSONObject();
            obj.put("universal_search", valueOf);
            Util.saveData("universal_search", valueOf, getActivity().getApplicationContext());
           // obj.put("CityName", getData("CityName", getActivity().getApplicationContext()));
            obj.put("CityName", "");
            //   obj.put("Latitude", getData("latitude", getActivity().getApplicationContext()));
            //   obj.put("Logtitude", getData("longitude", getActivity().getApplicationContext()));
            obj.put("Latitude", "0");
            obj.put("Logtitude", "0");
            Util.Logcat.e("GET History:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);

            CallApi.postResponse(getActivity(), params.toString(), UNIVERSAL_SEARCH, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Search.setEnabled(true);
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
                    Search.setEnabled(true);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        JSONArray jsonArray = resobject.optJSONArray("response");
                        //  JSONArray jsonArray = new JSONArray("_lstGetIndentOutputModel1");
                        try {
                            if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject imageobject = jsonArray.getJSONObject(i);
                                    DataHashMap = new HashMap<>();
                                    DataHashMap.put("id", imageobject.getString("id"));
                                    DataHashMap.put("name", imageobject.getString("name"));
                                    DataHashMap.put("type", imageobject.getString("type"));
                                    DataHashMap.put("image", imageobject.getString("image"));
                                    //  DataHashMap.put("type", valueOf);
                                    DataHashMap.put("FromShopPOSTypeId", imageobject.getString("FromShopPOSTypeId"));
                                    DataHashMap.put("FromShopPosName", imageobject.getString("FromShopPosName"));
                                    DataHashMap.put("FromShopTown", imageobject.getString("FromShopTown"));

                                    if (imageobject.getString("image").contains(".jpg") || imageobject.getString("image").contains(".png") || imageobject.getString("image").contains(".PNG") || imageobject.getString("image").contains(".jpeg")) {
                                        DataHashMap.put("imgavailable", "true");
                                    } else {
                                        DataHashMap.put("imgavailable", "false");
                                    }
                                    ListCollection.add(DataHashMap);
                                }
                                Util.Logcat.e("ListCollection :" + ListCollection);
                                if (ListCollection.size() > 0) {
                                    //adapter = new HistoryAdapterNew(getActivity(), ListCollection);
                                    listView.setAdapter(new CommonSearchAdapter(getActivity(), ListCollection));
                                } else {
                                    alert.build(getString(R.string.NoRecordsAvailable), false);
                                }
                            } else {
                                alert.build(resobject.getString("StatusDesc"), false);
                            }

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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Search.setText("");
      /* if( listView.getAdapter()!=null){

       }*/
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
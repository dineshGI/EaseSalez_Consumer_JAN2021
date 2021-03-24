package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.FuelTypeAdapter;
import com.gieasesales.adapter.MakeAdapter;
import com.gieasesales.adapter.OwnerShipAdapter;
import com.gieasesales.adapter.TransmissionAdapter;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ExpandableHeightGridView;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gieasesales.utils.Util.APPLY_FILTER;
import static com.gieasesales.utils.Util.VEHICLE_FILTER;
import static com.gieasesales.utils.Util.getData;

public class FilterActivityCar extends AppCompatActivity implements View.OnClickListener {

    CommonAlertDialog alert;
    ExpandableHeightGridView makelistView, fueltypelistView, ownershiplistView, transmissionlistView;
    MakeAdapter makeadapter;
    FuelTypeAdapter fueltypeadapter;
    OwnerShipAdapter ownershipadapter;
    TransmissionAdapter transadapter;
    private HashMap<String, String> MakeDataHashMap, FuelTypeDataHashMap;
    private List<Map<String, String>> MakeListCollection, FuelTypeListCollection;
    TextView NewCar, UsedCar;
    ImageView SUV, Hatchback, Sedan, Micro, Convertable, Couple, Truck, Vintage, close;
    LinearLayout fuelcheckbox, ownershipcheckbox, transmisioncheckbox;
    ImageView btn_fueltype, btn_ownership, btn_transmission;
    JSONArray _lstManufacturer, _lstFuelType, _lstOwnership, _lstBodyType, _lstTransmission, _lstPriceRange, _lstManufactureYear, _lstKMRange;

    List<String> Make = new ArrayList<>();

    CrystalRangeSeekbar BudgetSeekbar, YearSeekbar, KMSeekbar;
    TextView BudgetMin, BudgetMax, YearMin, YearMax, KMMin, KMMax;
    EditText search;
    Button btn_submit;
    String Bodytype = "", FuelType = "", OwnerShip = "", TransMission = "", SalesType = "1";

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity_car);

        btn_submit = findViewById(R.id.btn_submit);
        search = findViewById(R.id.search);
        makelistView = findViewById(R.id.makelistView);
        fueltypelistView = findViewById(R.id.fueltypelistView);
        ownershiplistView = findViewById(R.id.ownershiplistView);
        transmissionlistView = findViewById(R.id.transmissionlistView);
        makelistView.setExpanded(true);
        fueltypelistView.setExpanded(true);
        ownershiplistView.setExpanded(true);
        transmissionlistView.setExpanded(true);
        BudgetSeekbar = findViewById(R.id.BudgetSeekbar);
        YearSeekbar = findViewById(R.id.YearSeekbar);
        KMSeekbar = findViewById(R.id.KMSeekbar);

        BudgetMin = findViewById(R.id.BudgetMin);
        BudgetMax = findViewById(R.id.BudgetMax);

        YearMin = findViewById(R.id.YearMin);
        YearMax = findViewById(R.id.YearMax);

        KMMin = findViewById(R.id.KMMin);
        KMMax = findViewById(R.id.KMMax);

        alert = new CommonAlertDialog(this);
        close = findViewById(R.id.close);
        NewCar = findViewById(R.id.NewCar);
        UsedCar = findViewById(R.id.UsedCar);
        SUV = findViewById(R.id.SUV);
        Micro = findViewById(R.id.Micro);
        Hatchback = findViewById(R.id.Hatchback);
        Sedan = findViewById(R.id.Sedan);
        Convertable = findViewById(R.id.Convertable);
        Couple = findViewById(R.id.Couple);
        Truck = findViewById(R.id.Truck);
        Vintage = findViewById(R.id.Vintage);
        MakeListCollection = new ArrayList<>();
        FuelTypeListCollection = new ArrayList<>();

        fuelcheckbox = findViewById(R.id.fuelcheckbox);
        ownershipcheckbox = findViewById(R.id.ownershipcheckbox);
        transmisioncheckbox = findViewById(R.id.transmisioncheckbox);

        btn_fueltype = findViewById(R.id.btn_fueltype);
        btn_ownership = findViewById(R.id.btn_ownership);
        btn_transmission = findViewById(R.id.btn_transmission);

        SUV.setOnClickListener(this);
        Hatchback.setOnClickListener(this);
        Sedan.setOnClickListener(this);
        Micro.setOnClickListener(this);
        Convertable.setOnClickListener(this);
        Couple.setOnClickListener(this);
        Truck.setOnClickListener(this);
        Vintage.setOnClickListener(this);

        //  CheckBox geekBox =new CheckBox(this) ;

        ButtonClick();

        GetDetails();

    }

    private void GetDetails() {

        try {
            JSONObject obj = new JSONObject();
            //obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("VehicleTypeId", "1");
            obj.put("POSId", "0");
            obj.put("ProductSeoName", "");

            Util.Logcat.e("DETAIL:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), VEHICLE_FILTER, new VolleyResponseListener() {
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
                            _lstManufacturer = new JSONArray();
                            _lstFuelType = new JSONArray();
                            _lstOwnership = new JSONArray();
                            _lstBodyType = new JSONArray();
                            _lstTransmission = new JSONArray();
                            _lstPriceRange = new JSONArray();
                            _lstManufactureYear = new JSONArray();
                            _lstKMRange = new JSONArray();

                            _lstManufacturer = resobject.optJSONArray("_lstManufacturer");

                            if (_lstManufacturer.length() > 0) {
                                for (int i = 0; i < _lstManufacturer.length(); i++) {
                                    JSONObject imageobject = _lstManufacturer.getJSONObject(i);
                                    MakeDataHashMap = new HashMap<String, String>();
                                    MakeDataHashMap.put("Manufacturer", imageobject.getString("Manufacturer"));
                                    MakeDataHashMap.put("ManufacturerId", imageobject.getString("ManufacturerId"));
                                    MakeListCollection.add(MakeDataHashMap);

                                }
                                if (MakeListCollection.size() > 0) {
                                    makeadapter = new MakeAdapter(FilterActivityCar.this, MakeListCollection);
                                    makelistView.setAdapter(makeadapter);
                                }

                            }

                            makelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Util.Logcat.e("CheckBox :" + position);
                                    if (!Make.contains(MakeListCollection.get(position).get("Manufacturer"))) {
                                        Make.add(MakeListCollection.get(position).get("Manufacturer"));
                                        Toast.makeText(FilterActivityCar.this, "Added :" + MakeListCollection.get(position).get("Manufacturer"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Make.remove(MakeListCollection.get(position).get("Manufacturer"));
                                        Toast.makeText(FilterActivityCar.this, "Removed :" + MakeListCollection.get(position).get("Manufacturer"), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            _lstFuelType = resobject.optJSONArray("_lstFuelType");
                            if (_lstFuelType.length() > 0) {
                                for (int i = 0; i < _lstFuelType.length(); i++) {
                                    JSONObject imageobject = _lstFuelType.getJSONObject(i);

                                    FuelTypeDataHashMap = new HashMap<String, String>();
                                    FuelTypeDataHashMap.put("FuelType", imageobject.getString("FuelType"));
                                    FuelTypeDataHashMap.put("FuelTypeId", imageobject.getString("FuelTypeId"));
                                    FuelTypeListCollection.add(FuelTypeDataHashMap);
                                }
                                if (FuelTypeListCollection.size() > 0) {
                                    fueltypeadapter = new FuelTypeAdapter(FilterActivityCar.this, FuelTypeListCollection);
                                    fueltypelistView.setAdapter(fueltypeadapter);
                                }
                            }
                            fueltypelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Util.Logcat.e("FuelType :" + FuelTypeListCollection.get(position).get("FuelType"));
                                    FuelType = FuelTypeListCollection.get(position).get("FuelType");

                                    CheckBox cb;

                                    for (int i = 0; i < fueltypelistView.getChildCount(); i++) {
                                        cb = (CheckBox) fueltypelistView.getChildAt(i).findViewById(R.id.checkbox);
                                        cb.setChecked(false);
                                    }
                                    fueltypeadapter.notifyDataSetChanged();

                                }
                            });
                            _lstOwnership = resobject.optJSONArray("_lstOwnership");
                            final List<Map<String, String>> OwnershipListCollection = new ArrayList<>();
                            if (_lstOwnership.length() > 0) {
                                for (int i = 0; i < _lstOwnership.length(); i++) {
                                    JSONObject imageobject = _lstOwnership.getJSONObject(i);
                                    HashMap<String, String> DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("Ownership", imageobject.getString("Ownership"));
                                    DataHashMap.put("OwnershipId", imageobject.getString("OwnershipId"));
                                    OwnershipListCollection.add(DataHashMap);

                                }
                                if (OwnershipListCollection.size() > 0) {
                                    ownershipadapter = new OwnerShipAdapter(FilterActivityCar.this, OwnershipListCollection);
                                    ownershiplistView.setAdapter(ownershipadapter);
                                }

                                ownershiplistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Util.Logcat.e("Ownership :" + OwnershipListCollection.get(position).get("Ownership"));
                                        OwnerShip = OwnershipListCollection.get(position).get("Ownership");
                                        CheckBox cb;
                                        for (int i = 0; i < ownershiplistView.getChildCount(); i++) {
                                            cb = (CheckBox) ownershiplistView.getChildAt(i).findViewById(R.id.checkbox);
                                            cb.setChecked(false);
                                        }
                                        ownershipadapter.notifyDataSetChanged();

                                    }
                                });
                            }


                            _lstBodyType = resobject.optJSONArray("_lstBodyType");


                            _lstTransmission = resobject.optJSONArray("_lstTransmission");

                            final List<Map<String, String>> TransListCollection = new ArrayList<>();
                            if (_lstTransmission.length() > 0) {
                                for (int i = 0; i < _lstTransmission.length(); i++) {
                                    JSONObject imageobject = _lstTransmission.getJSONObject(i);
                                    HashMap<String, String> DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("Transmission", imageobject.getString("Transmission"));
                                    DataHashMap.put("TransmissionId", imageobject.getString("TransmissionId"));
                                    TransListCollection.add(DataHashMap);

                                }
                                if (TransListCollection.size() > 0) {
                                    transadapter = new TransmissionAdapter(FilterActivityCar.this, TransListCollection);
                                    transmissionlistView.setAdapter(transadapter);
                                }

                                transmissionlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Util.Logcat.e("Transmission :" + TransListCollection.get(position).get("Transmission"));
                                        TransMission = TransListCollection.get(position).get("Transmission");
                                        CheckBox cb;
                                        for (int i = 0; i < transmissionlistView.getChildCount(); i++) {
                                            cb = (CheckBox) transmissionlistView.getChildAt(i).findViewById(R.id.checkbox);
                                            cb.setChecked(false);
                                        }
                                        transadapter.notifyDataSetChanged();

                                    }
                                });
                            }

                            _lstPriceRange = resobject.optJSONArray("_lstPriceRange");
                            if (_lstPriceRange.length() > 0) {
                                for (int i = 0; i < _lstTransmission.length(); i++) {
                                    Float min = Float.valueOf(_lstPriceRange.getJSONObject(0).getString("LowestPrice"));
                                    Float max = Float.valueOf(_lstPriceRange.getJSONObject(0).getString("HighestPrice"));
                                    BudgetSeekbar.setMinValue(min);
                                    BudgetMin.setText(getString(R.string.currency) + " " + min);
                                    BudgetSeekbar.setMaxValue(max);
                                    BudgetMax.setText(getString(R.string.currency) + " " + max);
                                }

                            }

                            _lstManufactureYear = resobject.optJSONArray("_lstManufactureYear");

                            if (_lstManufactureYear.length() > 0) {
                                for (int i = 0; i < _lstManufactureYear.length(); i++) {
                                    Float min = Float.valueOf(_lstManufactureYear.getJSONObject(0).getString("ModelYear"));
                                    Float max = Float.valueOf(_lstManufactureYear.getJSONObject(_lstManufactureYear.length() - 1).getString("ModelYear"));
                                    YearSeekbar.setMinValue(min);
                                    String mins = String.valueOf(min).replace(".0", "");
                                    YearMin.setText("" + mins);
                                    YearSeekbar.setMaxValue(max);
                                    String maxs = String.valueOf(max).replace(".0", "");
                                    YearMax.setText("" + maxs);
                                }

                            }
                            _lstKMRange = resobject.optJSONArray("_lstKMRange");
                            if (_lstKMRange.length() > 0) {
                                for (int i = 0; i < _lstKMRange.length(); i++) {
                                    Float min = Float.valueOf(_lstKMRange.getJSONObject(0).getString("KMStart"));
                                    Float max = Float.valueOf(_lstKMRange.getJSONObject(0).getString("KMEnd"));
                                    KMSeekbar.setMinValue(min);
                                    KMMin.setText("" + min);
                                    KMSeekbar.setMaxValue(max);
                                    KMMax.setText("" + max);
                                }
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

    private void ButtonClick() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyFilter();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (makeadapter != null) {
                    makeadapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesType = "1";
                NewCar.setTextColor(getResources().getColor(R.color.white));
                NewCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                UsedCar.setTextColor(getResources().getColor(R.color.button_gray));
                UsedCar.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        UsedCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesType = "2";
                UsedCar.setTextColor(getResources().getColor(R.color.white));
                UsedCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                NewCar.setTextColor(getResources().getColor(R.color.button_gray));
                NewCar.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_fueltype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fuelcheckbox.getVisibility() == View.VISIBLE) {
                    fuelcheckbox.setVisibility(View.GONE);
                    btn_fueltype.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                } else {
                    btn_fueltype.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    fuelcheckbox.setVisibility(View.VISIBLE);
                }

            }
        });
        btn_ownership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ownershipcheckbox.getVisibility() == View.VISIBLE) {
                    ownershipcheckbox.setVisibility(View.GONE);
                    btn_ownership.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                } else {
                    btn_ownership.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    ownershipcheckbox.setVisibility(View.VISIBLE);
                }

            }
        });


        btn_transmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transmisioncheckbox.getVisibility() == View.VISIBLE) {
                    transmisioncheckbox.setVisibility(View.GONE);
                    btn_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                } else {
                    btn_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    transmisioncheckbox.setVisibility(View.VISIBLE);
                }

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BudgetSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                BudgetMin.setText(getString(R.string.currency) + " " + minValue);
                BudgetMax.setText(getString(R.string.currency) + " " + maxValue);
            }
        });
        YearSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                YearMin.setText(String.valueOf(minValue));
                YearMax.setText(String.valueOf(maxValue));
            }
        });

        KMSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                KMMin.setText(String.valueOf(minValue));
                KMMax.setText(String.valueOf(maxValue));
            }
        });

    }

    private void ApplyFilter() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("POSTypeId", "20");
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("SaleType", SalesType);
            obj.put("BudgetFrom", BudgetMin.getText().toString().replaceAll(getString(R.string.currency), "").trim());
            obj.put("BudgetTo", BudgetMax.getText().toString().replaceAll(getString(R.string.currency), "").trim());

            obj.put("KMDrivenFrom", KMMin.getText().toString());
            obj.put("KMDrivenTo", KMMax.getText().toString());
            obj.put("ModelYear", YearMin.getText().toString() + "-" + YearMax.getText().toString());

            StringBuilder Manufacturer = new StringBuilder();
            for (String Manufac : Make) {

                Manufacturer.append(Manufac + "~");
            }
            // obj.put("VehicleModel", Manufacturer.substring(0, Manufacturer.length() - 1));
            obj.put("Model", Manufacturer.substring(0, Manufacturer.length() - 1));

            obj.put("BodyType", Bodytype);
            obj.put("FuelType", FuelType);
            obj.put("OwnerShip", OwnerShip);
            obj.put("Transmission", TransMission);

            Util.Logcat.e("APPLY FILTER:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), APPLY_FILTER, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(EditAddress.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(EditAddress.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("APPLY FILTER:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

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

    @Override
    public void onClick(View v) {

        SUV.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Hatchback.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Sedan.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Micro.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Convertable.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Couple.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Truck.setImageDrawable(getResources().getDrawable(R.drawable.suv));
        Vintage.setImageDrawable(getResources().getDrawable(R.drawable.suv));

        switch (v.getId()) {

            case R.id.SUV:
                SUV.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "SUV";
                break;
            case R.id.Hatchback:
                Hatchback.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Hatchback";
                break;
            case R.id.Sedan:
                Sedan.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Sedan";
                break;
            case R.id.Micro:
                Micro.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Micro";
                break;
            case R.id.Convertable:
                Convertable.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Convertable";
                break;
            case R.id.Couple:
                Couple.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Coupe";
                break;
            case R.id.Truck:
                Truck.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Truck and Mini van";
                break;
            case R.id.Vintage:
                Vintage.setImageDrawable(getResources().getDrawable(R.drawable.suv_red));
                Bodytype = "Vintage";
                break;

            default:
                break;
        }
    }
}

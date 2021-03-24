package com.gieasesales.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.adapter.MakeAdapter;
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

public class BikeFilterActivity extends AppCompatActivity implements View.OnClickListener {

    CommonAlertDialog alert;
    ExpandableHeightGridView makelistView;
    MakeAdapter adapter;
    private HashMap<String, String> DataHashMap;
    private List<Map<String, String>> ListCollection;
    TextView NewCar, UsedCar;
    // ImageView SUV, Hatchback, Sedan, Micro, Convertable, Couple, Truck, Vintage, close;
    ImageView Scooter, Bike, Sports, Cruiser, OffRoad, Vintage, close;
    LinearLayout fuelcheckbox, ownershipcheckbox, transmisioncheckbox;
    ImageView btn_fueltype, btn_ownership, btn_transmission;
    JSONArray _lstManufacturer, _lstFuelType, _lstOwnership, _lstBodyType, _lstTransmission, _lstPriceRange, _lstManufactureYear, _lstKMRange;

    List<String> Make = new ArrayList<>();
    List<String> FuelType = new ArrayList<>();
    List<String> OwnerShip = new ArrayList<>();
    List<String> TransMission = new ArrayList<>();

    CrystalRangeSeekbar BudgetSeekbar, YearSeekbar, KMSeekbar;
    TextView BudgetMin, BudgetMax, YearMin, YearMax, KMMin, KMMax, city;
    EditText search;
    Button btn_submit;
    LinearLayout Changelocation;
    String Bodytype = "", SaleType = "1";
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikefilter_activity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        btn_submit = findViewById(R.id.btn_submit);
        city = findViewById(R.id.city);
        Changelocation = findViewById(R.id.Changelocation);
        search = findViewById(R.id.search);
        makelistView = findViewById(R.id.makelistView);
        makelistView.setExpanded(true);
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
        Scooter = findViewById(R.id.Scooter);
        Cruiser = findViewById(R.id.Cruiser);
        Bike = findViewById(R.id.Bike);
        Sports = findViewById(R.id.Sports);
        OffRoad = findViewById(R.id.OffRoad);
        Vintage = findViewById(R.id.Vintage);
        Vintage = findViewById(R.id.Vintage);
        ListCollection = new ArrayList<>();

        fuelcheckbox = findViewById(R.id.fuelcheckbox);
        ownershipcheckbox = findViewById(R.id.ownershipcheckbox);
        transmisioncheckbox = findViewById(R.id.transmisioncheckbox);

        btn_fueltype = findViewById(R.id.btn_fueltype);
        btn_ownership = findViewById(R.id.btn_ownership);
        btn_transmission = findViewById(R.id.btn_transmission);

        Scooter.setOnClickListener(this);
        Bike.setOnClickListener(this);
        Sports.setOnClickListener(this);
        Cruiser.setOnClickListener(this);
        OffRoad.setOnClickListener(this);
        Vintage.setOnClickListener(this);
        Vintage.setOnClickListener(this);

        //  CheckBox geekBox =new CheckBox(this) ;
        ButtonClick();

        GetDetails();

    }

    @Override
    public void onResume() {
        super.onResume();

        //GetStore("0");
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                    upaddress(Double.parseDouble(Util.getData("latitude", getApplicationContext())), Double.parseDouble(Util.getData("longitude", getApplicationContext())));
                }
            }, 3000);
        }

    }

    private void upaddress(double lat, double lng) {
        String address = Util.getAddress(lat, lng, BikeFilterActivity.this);
        //city.setText(address);
        city.setText(address);
    }

    private void GetDetails() {

        try {
            JSONObject obj = new JSONObject();
            //obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("VehicleTypeId", "2");
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
                                    DataHashMap = new HashMap<String, String>();
                                    DataHashMap.put("Manufacturer", imageobject.getString("Manufacturer"));
                                    DataHashMap.put("ManufacturerId", imageobject.getString("ManufacturerId"));
                                    ListCollection.add(DataHashMap);

                                }
                                if (ListCollection.size() > 0) {
                                    adapter = new MakeAdapter(BikeFilterActivity.this, ListCollection);
                                    makelistView.setAdapter(adapter);
                                }
                            }

                            makelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Util.Logcat.e("CheckBox :" + position);
                                    if (!Make.contains(ListCollection.get(position).get("Manufacturer"))) {
                                        Make.add(ListCollection.get(position).get("Manufacturer"));
                                        //  Toast.makeText(BikeFilterActivity.this, "Added :" + ListCollection.get(position).get("Manufacturer"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Make.remove(ListCollection.get(position).get("Manufacturer"));
                                        //   Toast.makeText(BikeFilterActivity.this, "Removed :" + ListCollection.get(position).get("Manufacturer"), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            _lstFuelType = resobject.optJSONArray("_lstFuelType");
                            if (_lstFuelType.length() > 0) {
                                final int[] numChecked = {0};
                                for (int i = 0; i < _lstFuelType.length(); i++) {
                                    JSONObject imageobject = _lstFuelType.getJSONObject(i);

                                    final CheckBox geekBox = new CheckBox(BikeFilterActivity.this);
                                    geekBox.setText(imageobject.getString("FuelType"));
                                    geekBox.setId(imageobject.getInt("FuelTypeId"));
                                    fuelcheckbox.addView(geekBox);
                                    geekBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            String data = (isChecked ? "Checked" : "Unchecked");
                                            if (data.equalsIgnoreCase("Checked")) {
                                                FuelType.add(geekBox.getText().toString());
                                            } else {
                                                if (FuelType.contains(geekBox.getText().toString())) {
                                                    FuelType.remove(geekBox.getText().toString());
                                                }
                                            }

                                        }
                                    });
                                }

                            }
                            _lstOwnership = resobject.optJSONArray("_lstOwnership");
                            if (_lstOwnership.length() > 0) {
                                for (int i = 0; i < _lstOwnership.length(); i++) {
                                    JSONObject imageobject = _lstOwnership.getJSONObject(i);

                                    final CheckBox geekBox = new CheckBox(BikeFilterActivity.this);
                                    geekBox.setText(imageobject.getString("Ownership"));
                                    geekBox.setId(imageobject.getInt("OwnershipId"));
                                    ownershipcheckbox.addView(geekBox);
                                    geekBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            String data = (isChecked ? "Checked" : "Unchecked");
                                            if (data.equalsIgnoreCase("Checked")) {
                                                OwnerShip.add(geekBox.getText().toString());
                                            } else {
                                                if (OwnerShip.contains(geekBox.getText().toString())) {
                                                    OwnerShip.remove(geekBox.getText().toString());
                                                }
                                            }

                                        }
                                    });
                                }

                            }

                            _lstBodyType = resobject.optJSONArray("_lstBodyType");

                            _lstTransmission = resobject.optJSONArray("_lstTransmission");

                            if (_lstTransmission.length() > 0) {
                                for (int i = 0; i < _lstTransmission.length(); i++) {
                                    JSONObject imageobject = _lstTransmission.getJSONObject(i);

                                    final CheckBox geekBox = new CheckBox(BikeFilterActivity.this);
                                    geekBox.setText(imageobject.getString("Transmission"));
                                    geekBox.setId(imageobject.getInt("TransmissionId"));
                                    transmisioncheckbox.addView(geekBox);
                                    geekBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            String data = (isChecked ? "Checked" : "Unchecked");
                                            if (data.equalsIgnoreCase("Checked")) {
                                                TransMission.add(geekBox.getText().toString());
                                            } else {
                                                if (TransMission.contains(geekBox.getText().toString())) {
                                                    TransMission.remove(geekBox.getText().toString());
                                                }
                                            }

                                        }
                                    });
                                }

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
        Changelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BikeFilterActivity.this.getPackageName().equals("com.gieasesales")) {
                    Intent locationnew = new Intent(BikeFilterActivity.this, PickLocation.class);
                    startActivity(locationnew);
                }

            }
        });


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

                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleType = "1";
                NewCar.setTextColor(getResources().getColor(R.color.white));
                NewCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                UsedCar.setTextColor(getResources().getColor(R.color.button_gray));
                UsedCar.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        UsedCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleType = "2";
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
            obj.put("POSTypeId", "21");
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("SaleType", SaleType);
            obj.put("BudgetFrom", BudgetMin.getText().toString().replaceAll(getString(R.string.currency), "").trim());
            obj.put("BudgetTo", BudgetMax.getText().toString().replaceAll(getString(R.string.currency), "").trim());
            obj.put("KMDrivenFrom", KMMin.getText().toString());
            obj.put("KMDrivenTo", KMMax.getText().toString());
            obj.put("City", city.getText().toString());
            StringBuilder Manufacturer = new StringBuilder();

            for (String Manufac : Make) {

                Manufacturer.append(Manufac + "~");
            }

            obj.put("VehicleModel", Manufacturer.toString());
            obj.put("ModelFromYear", YearMin.getText().toString());
            obj.put("ModelToYear", YearMax.getText().toString());
            //obj.put("ModelYear", "2000");

            obj.put("BodyType", Bodytype);

            StringBuilder fueltype = new StringBuilder();
            for (String type : FuelType) {

                fueltype.append(type + "~");
            }

            // obj.put("FuelType", fueltype.substring(0, fueltype.length() - 1));
            obj.put("FuelType", fueltype.toString());

            StringBuilder Ownership = new StringBuilder();
            for (String ownership : OwnerShip) {

                Ownership.append(ownership + "~");
            }
            obj.put("OwnerShip", Ownership.toString());


            StringBuilder Transmission = new StringBuilder();
            for (String trans : TransMission) {

                Transmission.append(trans + "~");
            }
            obj.put("Transmission", Transmission.toString());

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


                            if (BikeFilterActivity.this.getPackageName().equals("com.gieasesales")) {
                                Intent search = new Intent(BikeFilterActivity.this, CarBikeSearch.class);
                                search.putExtra("Data", resobject.toString());
                                search.putExtra("POSTypeId", "21");
                                startActivity(search);
                                finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BikeFilterActivity.this, R.style.alertDialog);
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

    @Override
    public void onClick(View v) {

        Scooter.setImageDrawable(getResources().getDrawable(R.drawable.scooter_gray));
        Bike.setImageDrawable(getResources().getDrawable(R.drawable.bikes_gray));
        Sports.setImageDrawable(getResources().getDrawable(R.drawable.sports_gray));
        Cruiser.setImageDrawable(getResources().getDrawable(R.drawable.cruiser_gray));
        OffRoad.setImageDrawable(getResources().getDrawable(R.drawable.offroad_gray));
        Vintage.setImageDrawable(getResources().getDrawable(R.drawable.vintagebike_gray));

        switch (v.getId()) {

            case R.id.Scooter:
                Scooter.setImageDrawable(getResources().getDrawable(R.drawable.scooter_red));
                Bodytype = "Scooter";
                break;
            case R.id.Bike:
                Bike.setImageDrawable(getResources().getDrawable(R.drawable.bikes_red));
                Bodytype = "Bike";
                break;
            case R.id.Sports:
                Sports.setImageDrawable(getResources().getDrawable(R.drawable.sports_red));
                Bodytype = "Sports";
                break;
            case R.id.Cruiser:
                Cruiser.setImageDrawable(getResources().getDrawable(R.drawable.cruiser_red));
                Bodytype = "Cruiser";
                break;
            case R.id.OffRoad:
                OffRoad.setImageDrawable(getResources().getDrawable(R.drawable.offroad_red));
                Bodytype = "OffRoad";
                break;
            case R.id.Vintage:
                Vintage.setImageDrawable(getResources().getDrawable(R.drawable.vintagebike_red));
                Bodytype = "Vintage";
                break;

            default:
                break;
        }
    }
}

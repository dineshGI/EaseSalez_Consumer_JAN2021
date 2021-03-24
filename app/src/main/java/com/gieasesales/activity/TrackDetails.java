package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gieasesales.utils.Util.DELIVERY_DETAILS;

public class TrackDetails extends AppCompatActivity {

    CommonAlertDialog alert;
    ImageView back_arrow;
    TextView OrderNo, OrderDate, PaymentMode, DeliveryCharges, OrderAmount, DeliveryFEName, Temperature, TemperatureDT;
    TextView OrderPicked,FEDeliverySlotDT,NetPayable,IndentStatusDesc,TotalAmount;
    CircleImageView ProfileImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_details);

        alert = new CommonAlertDialog(this);
        back_arrow = findViewById(R.id.back_arrow);
        OrderNo = findViewById(R.id.OrderNo);
        NetPayable = findViewById(R.id.NetPayable);
        IndentStatusDesc = findViewById(R.id.IndentStatusDesc);
        OrderDate = findViewById(R.id.OrderDate);
        PaymentMode = findViewById(R.id.PaymentMode);
        DeliveryCharges = findViewById(R.id.DeliveryCharges);
        OrderAmount = findViewById(R.id.OrderAmount);
        TotalAmount = findViewById(R.id.TotalAmount);
        DeliveryFEName = findViewById(R.id.DeliveryFEName);
        Temperature = findViewById(R.id.Temperature);
        TemperatureDT = findViewById(R.id.TemperatureDT);
        ProfileImage = findViewById(R.id.ProfileImage);
        OrderPicked = findViewById(R.id.PickupDT);
        FEDeliverySlotDT = findViewById(R.id.FEDeliverySlotDT);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String IndentNo = getIntent().getStringExtra("IndentNo");
        Details(IndentNo);
    }

    private void Details(String IndentNo) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("IndentNo", IndentNo);
            Util.Logcat.e("DETAILS:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), DELIVERY_DETAILS, new VolleyResponseListener() {
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
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("DETAILS:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobjects = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobjects.getString("Status").equalsIgnoreCase("0")) {

                            JSONArray jsonArray = resobjects.optJSONArray("_lstOrderDeliveryDetailsResModel");
                            JSONObject resobject = jsonArray.getJSONObject(0);
                            OrderNo.setText(resobject.getString("OrderNo"));
                            OrderDate.setText(resobject.getString("OrderDate"));
                            PaymentMode.setText(resobject.getString("PaymentMode"));
                            DeliveryCharges.setText(getString(R.string.currency) + resobject.getString("DeliveryCharges"));
                            TotalAmount.setText(getString(R.string.currency) + resobject.getString("TotalAmount"));
                            NetPayable.setText(getString(R.string.currency) + resobject.getString("NetPayable"));
                            OrderAmount.setText(getString(R.string.currency) + resobject.getString("NetPayable"));
                            DeliveryFEName.setText(resobject.getString("DeliveryFEName"));
                            Temperature.setText(resobject.getString("Temperature"));
                            TemperatureDT.setText(resobject.getString("TemperatureDT"));
                            OrderPicked.setText(resobject.getString("PickupDT"));
                            FEDeliverySlotDT.setText(resobject.getString("FEDeliverySlotDT"));
                            IndentStatusDesc.setText(resobject.getString("IndentStatusDesc"));
                            String ImagePath = resobject.getString("ProfileImage");

                            if (ImagePath.contains(".png") || ImagePath.contains(".PNG") || ImagePath.contains(".jpg") || ImagePath.contains(".jpeg") || !ImagePath.isEmpty()) {
                                Glide.with(TrackDetails.this).load(resobject.getString("ProfileImagePath")).into(ProfileImage);
                            } else {
                                ProfileImage.setVisibility(View.VISIBLE);
                            }

                        } else {
                            alert.build(resobjects.getString("StatusDesc"), false);
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
//android:text
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

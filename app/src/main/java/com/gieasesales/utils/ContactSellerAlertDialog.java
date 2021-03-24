package com.gieasesales.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gieasesales.utils.Util.ADD_WISHLIST;
import static com.gieasesales.utils.Util.getData;


public class ContactSellerAlertDialog {
    private Activity mActivity;

    public ContactSellerAlertDialog(Activity a) {
        this.mActivity = a;
    }

    public void build(String name, String contactno, String address, String hours, final String ProductId, final String POSId,String pincode) {
        AddWishList(ProductId,POSId);
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.contactseller_alert);
        // set the custom dialog components - text, image and button
        final TextView ContactNo = dialog.findViewById(R.id.ContactNo);
        TextView Name = dialog.findViewById(R.id.Name);
        TextView Address = dialog.findViewById(R.id.Address);
        TextView Hours = dialog.findViewById(R.id.Hours);
        TextView Pincode = dialog.findViewById(R.id.Pincode);

        Name.setText(name);
        ContactNo.setText(contactno);
        Address.setText(address);
        Hours.setText(hours);
        Pincode.setText(pincode);

        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
        ImageView close = dialog.findViewById(R.id.close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

        ContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ContactNo.getText().toString()));
                mActivity.startActivity(intent);
            }
        });
    }
    private void AddWishList(String ProductId, String POSId) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", mActivity.getApplicationContext()));
            obj.put("ProductId", ProductId);
            obj.put("POSId", POSId);
            obj.put("Flag", "4");
            obj.put("OfferPrice", "0");
            Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(mActivity, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                    } else {
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
                          //  alert.build(resobject.getString("StatusDesc"), true);
                        } else {
                          //  alert.build(resobject.getString("StatusDesc"), false);
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

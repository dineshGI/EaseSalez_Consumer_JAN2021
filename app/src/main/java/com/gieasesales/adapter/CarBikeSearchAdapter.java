package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.Model.CarBike;
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeDetail;
import com.gieasesales.interfaces.VolleyResponseListener;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.gieasesales.utils.Util.ADD_WISHLIST;
import static com.gieasesales.utils.Util.getData;

public class CarBikeSearchAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private ProgressDialog progressDialog;
    private List<CarBike> filterData, originaldata;
    public static List<String> cart = new ArrayList<>();
    CommonAlertDialog alert;

    public CarBikeSearchAdapter(Activity context, List<CarBike> listCollectionone) {

        activity = context;
        filterData = listCollectionone;
        originaldata = listCollectionone;
        alert = new CommonAlertDialog(activity);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

    }


    @Override
    public int getCount() {
        return this.filterData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.filterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void pos(int position) {
        filterData.remove(filterData.get(position));
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {

            vi = inflater.inflate(R.layout.carbikesearch_adapter, null);

            holder = new ViewHolder();
            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.shopname = vi.findViewById(R.id.shopname);
            holder.TxtMrp = vi.findViewById(R.id.mrp);
            holder.TxtSellingPrice = vi.findViewById(R.id.selling_price);
            holder.Imgproduct = vi.findViewById(R.id.pimg);
            holder.KMDriven = vi.findViewById(R.id.KMDriven);
            holder.FuelType = vi.findViewById(R.id.FuelType);
            holder.Ownership = vi.findViewById(R.id.Ownership);
            holder.detail_click = vi.findViewById(R.id.detail_click);
            holder.add_wishlist = vi.findViewById(R.id.add_wishlist);
            holder.City = vi.findViewById(R.id.City);
            holder.SellingPriceWords = vi.findViewById(R.id.SellingPriceWords);
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.shopname.setText(filterData.get(position).getPOSName() + ", " + filterData.get(position).getPOSLocation());
        holder.TxtItemName.setText(filterData.get(position).getProductName());
        holder.KMDriven.setText(filterData.get(position).getKMDriven() + " " + activity.getString(R.string.KM));
        holder.FuelType.setText(filterData.get(position).getFuelType());
        holder.Ownership.setText(filterData.get(position).getOwnership());
        holder.City.setText(filterData.get(position).getModelYear());
        holder.SellingPriceWords.setText(activity.getString(R.string.currency) +filterData.get(position).getSellingPriceWords());
        holder.TxtMrp.setText(activity.getString(R.string.currency) + String.format("%.2f", filterData.get(position).getSellinPrice()));
        holder.TxtSellingPrice.setText(activity.getString(R.string.currency) + String.format("%.2f", filterData.get(position).getMrp()));

        holder.TxtSellingPrice.setPaintFlags(holder.TxtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (filterData.get(position).getProductImg().contains(".jpg") || filterData.get(position).getProductImg().contains(".png") || filterData.get(position).getProductImg().contains(".PNG") || filterData.get(position).getProductImg().contains(".jpeg")) {
            try {
                Glide.with(activity).load(new URL(filterData.get(position).getProductImg())).into(holder.Imgproduct);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            holder.Imgproduct.setVisibility(View.VISIBLE);
            // holder.Imgproduct.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_product));
        }

        holder.detail_click.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Util.saveData("POSId", filterData.get(position).getPOSId(), activity.getApplicationContext());
                Util.Logcat.e("POSId CLICK" + filterData.get(position).getPOSId());
                Intent product = new Intent(activity, CarBikeDetail.class);
                product.putExtra("ProductId", filterData.get(position).getProductid());
                product.putExtra("wishlist", "false");
                activity.startActivity(product);
            }
        });

        holder.add_wishlist.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("ConsumerId", getData("ConsumerId", activity.getApplicationContext()));
                    obj.put("ProductId", filterData.get(position).getProductid());
                    obj.put("POSId", filterData.get(position).getPOSId());
                    obj.put("Flag", "1");
                    obj.put("OfferPrice", "0");
                    Util.Logcat.e("ADD WISHLIST:::" + obj.toString());
                    String data = Util.EncryptURL(obj.toString());
                    JSONObject params = new JSONObject();
                    params.put("Getrequestresponse", data);
                    CallApi.postResponse(activity, params.toString(), ADD_WISHLIST, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            if (message.contains("TimeoutError")) {
                                alert.build(activity.getString(R.string.timeout_error), false);
                                //Util.ErrorLog(CarBikeActivity.this, getString(R.string.timeout_error));
                            } else {
                                alert.build(activity.getString(R.string.server_error), false);
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
                                    alert.build(resobject.getString("StatusDesc"), true);

                                    holder.add_wishlist.setImageDrawable(activity.getDrawable(R.drawable.heart_red));
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
                // holder.add_wishlist.setImageDrawable(activity.getDrawable(R.drawable.heart_red));
            }
        });

        return vi;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originaldata;
                    results.count = originaldata.size();
                } else {
                    List<CarBike> filterResultsData = new ArrayList<CarBike>();
                    for (CarBike data : originaldata) {
                        if (data.getProductName().toLowerCase().contains(constraint)) {
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterData.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterData = (List<CarBike>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder {

        TextView TxtItemName, TxtMrp, TxtSellingPrice, KMDriven, FuelType, Ownership, City, shopname,SellingPriceWords;
        ImageView Imgproduct, add_wishlist;
        LinearLayout detail_click;

    }
}


package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.Model.CarBike;
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeDetail;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CarBikeAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private ProgressDialog progressDialog;
    private List<CarBike> filterData, originaldata;
    public static List<String> cart = new ArrayList<>();
    CommonAlertDialog alert;

    public CarBikeAdapter(Activity context, List<CarBike> listCollectionone) {

        activity = context;

        filterData = listCollectionone;
        originaldata = listCollectionone;
        alert = new CommonAlertDialog(activity);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
            vi = inflater.inflate(R.layout.carbike_adapter, null);

            holder = new ViewHolder();
            holder.TxtItemName = vi.findViewById(R.id.itemname);
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
                ((GridView) parent).performItemClick(v, position, 0);
                //holder.add_wishlist.setImageDrawable(activity.getDrawable(R.drawable.heart_red));
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
                    List<CarBike> filterResultsData = new ArrayList<>();
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

        TextView TxtItemName, TxtMrp, TxtSellingPrice, KMDriven, FuelType, Ownership, City,SellingPriceWords;
        ImageView Imgproduct, add_wishlist;
        LinearLayout detail_click;

    }
}


package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuoteAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;

    public QuoteAdapter(Activity context, List<Map<String, String>> listCollectionone) {

        activity = context;
        filterData = listCollectionone;
        originaldata = listCollectionone;
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
            vi = inflater.inflate(R.layout.quote_adapter, null);
            holder = new ViewHolder();
            holder.ProductName = vi.findViewById(R.id.ProductName);
            holder.img = vi.findViewById(R.id.img);
            holder.ConsumerAddress = vi.findViewById(R.id.ConsumerAddress);
            holder.ConsumerMobileNo = vi.findViewById(R.id.ConsumerMobileNo);
            holder.POSCity = vi.findViewById(R.id.POSCity);
            holder.SellingPrice = vi.findViewById(R.id.SellingPrice);
            holder.MRP = vi.findViewById(R.id.MRP);
            holder.City = vi.findViewById(R.id.City);
            holder.OfferPrice = vi.findViewById(R.id.OfferPrice);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }


        holder.ProductName.setText(filterData.get(position).get(
                "ProductName"));
        holder.City.setText(filterData.get(position).get(
                "POSCity"));
        holder.OfferPrice.setText(filterData.get(position).get(
                "OfferPrice"));

        if (filterData.get(position).get(
                "imgavailable").equalsIgnoreCase("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "FilePath")).into(holder.img);
        } else {
            Util.Logcat.e("FALSE" + "NO IMG");
            holder.img.setVisibility(View.INVISIBLE);
        }
        holder.ConsumerAddress.setText(filterData.get(position).get(
                "ConsumerAddress"));
        holder.ConsumerMobileNo.setText(filterData.get(position).get(
                "ConsumerMobileNo"));
        holder.POSCity.setText(filterData.get(position).get(
                "POSCity"));
        holder.SellingPrice.setText(filterData.get(position).get(
                "SellingPrice"));
        holder.MRP.setText(filterData.get(position).get(
                "MRP"));

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
                    List<Map<String, String>> filterResultsData = new ArrayList<Map<String, String>>();
                    for (Map<String, String> data : originaldata) {
                        if (data.get("ProductName").toLowerCase().contains(constraint)) {
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
                filterData = (List<Map<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder {
        TextView ProductName, ConsumerAddress, ConsumerMobileNo, POSCity, SellingPrice, MRP,City,OfferPrice;
        ImageView img;

    }
}


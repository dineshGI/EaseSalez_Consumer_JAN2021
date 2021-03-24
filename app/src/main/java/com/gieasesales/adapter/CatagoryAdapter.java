package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeActivity;
import com.gieasesales.utils.Util;
import com.gieasesales.activity.ExploreActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatagoryAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;

    public CatagoryAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {

            vi = inflater.inflate(R.layout.catagory_adapter_new, null);
            holder = new ViewHolder();
            holder.TxtName = vi.findViewById(R.id.name);
            holder.TxtAddress = vi.findViewById(R.id.address);
            holder.TxtExplore = vi.findViewById(R.id.explore);
            holder.Imageview = vi.findViewById(R.id.img);
            holder.TxtDirection = vi.findViewById(R.id.direction);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtName.setText(filterData.get(position).get(
                "CategoryName"));
        holder.TxtAddress.setText(filterData.get(position).get(
                "CategoryDesc"));

       /* Glide.with(activity).load(filterData.get(position).get(
                "ImagePath")).into(holder.Imageview);*/

        if (filterData.get(position).get(
                "imgavailable").contains("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "ImagePath")).into(holder.Imageview);
        } else {
            Util.Logcat.e("FALSE" + "NO IMG");
            holder.Imageview.setImageDrawable(activity.getDrawable(R.drawable.no_product));
        }

        holder.TxtExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show sub catagory
                if (filterData.get(position).get(
                        "POSTypeId").equalsIgnoreCase("20") || filterData.get(position).get(
                        "POSTypeId").equalsIgnoreCase("21")) {
                    Intent explore = new Intent(activity, CarBikeActivity.class);
                    explore.putExtra("POSId", filterData.get(position).get(
                            "POSId"));
                    explore.putExtra("PosName", filterData.get(position).get(
                            "PosName"));
                    explore.putExtra("POSTypeId", filterData.get(position).get(
                            "POSTypeId"));
                    explore.putExtra("ImagePath", filterData.get(position).get(
                            "shopimage"));
                    explore.putExtra("Address", filterData.get(position).get(
                            "PosAddress"));
                    explore.putExtra("CategoryId", filterData.get(position).get(
                            "CategoryId"));
                    explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                            "IsSubCatAvailable"));
                    explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                            "ShopPromotionMsg"));
                    explore.putExtra("AvgRating", filterData.get(position).get(
                            "AvgRating"));
                    activity.startActivity(explore);
                } else {
                    Intent explore = new Intent(activity, ExploreActivity.class);
                    explore.putExtra("POSId", filterData.get(position).get(
                            "POSId"));
                    explore.putExtra("PosName", filterData.get(position).get(
                            "PosName"));
                    explore.putExtra("ImagePath", filterData.get(position).get(
                            "shopimage"));
                    explore.putExtra("Address", filterData.get(position).get(
                            "PosAddress"));
                    explore.putExtra("CategoryId", filterData.get(position).get(
                            "CategoryId"));
                    explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                            "IsSubCatAvailable"));
                    explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                            "ShopPromotionMsg"));
                    explore.putExtra("AvgRating", filterData.get(position).get(
                            "AvgRating"));

                    activity.startActivity(explore);
                }
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
                    List<Map<String, String>> filterResultsData = new ArrayList<Map<String, String>>();
                    for (Map<String, String> data : originaldata) {
                        if (data.get("CategoryName").toLowerCase().contains(constraint) || data.get("CategoryDesc").toLowerCase().contains(constraint)) {
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
        TextView TxtName, TxtAddress, TxtDirection;
        ImageView Imageview;
        LinearLayout TxtExplore;
    }
}


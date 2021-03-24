package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeActivity;
import com.gieasesales.activity.CatogaryActivity;
import com.gieasesales.activity.ExploreActivity;
import com.gieasesales.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UniversalShopAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;

    public UniversalShopAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.universalshop_adapter, null);
            holder = new ViewHolder();
            holder.name = vi.findViewById(R.id.name);
            holder.img = vi.findViewById(R.id.img);
            //holder.type = vi.findViewById(R.id.type);
            holder.Town = vi.findViewById(R.id.Town);
            holder.Address = vi.findViewById(R.id.Address);
            holder.explore = vi.findViewById(R.id.explore);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.name.setText(filterData.get(position).get(
                "name"));
        Util.Logcat.e("FromShopPosName :"+filterData.get(position).get(
                "name"));
        holder.Town.setText(filterData.get(position).get(
                "Town"));
        holder.Address.setText(filterData.get(position).get(
                "Address"));

        if (filterData.get(position).get(
                "imgavailable").equalsIgnoreCase("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "image")).into(holder.img);
        } else {
            //Util.Logcat.e("FALSE" + "NO IMG");
            holder.img.setVisibility(View.INVISIBLE);
        }

       /* holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterData.get(position).get(
                        "IsSubCatAvailable").equalsIgnoreCase("0")) {
                    //Show sub catagory
                    Intent explore = new Intent(activity, ExploreActivity.class);
                    explore.putExtra("POSId", filterData.get(position).get(
                            "id"));
                    explore.putExtra("PosName", filterData.get(position).get(
                            "name"));
                    explore.putExtra("ImagePath", filterData.get(position).get(
                            "image"));
                    explore.putExtra("Address", filterData.get(position).get(
                            "Address"));
                    explore.putExtra("CategoryId", "0");
                    explore.putExtra("ShopPromotionMsg", "");
                    explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                            "IsSubCatAvailable"));
                    explore.putExtra("AvgRating", "");
                    activity.startActivity(explore);

                } else {
                    //Show  catagory
                    Intent explore = new Intent(activity, CatogaryActivity.class);
                    explore.putExtra("POSId", filterData.get(position).get(
                            "id"));
                    explore.putExtra("PosName", filterData.get(position).get(
                            "name"));
                    explore.putExtra("ImagePath", filterData.get(position).get(
                            "image"));
                    explore.putExtra("Address", filterData.get(position).get(
                            "Address"));
                    explore.putExtra("CategoryId", "");
                    explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                            "IsSubCatAvailable"));

                    explore.putExtra("ShopPromotionMsg", "");
                    explore.putExtra("AvgRating", "");
                    explore.putExtra("POSTypeId", "");

                    activity.startActivity(explore);
                }
            }
        });
*/
        holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterData.get(position).get(
                        "IsSubCatAvailable").equalsIgnoreCase("0")) {

                    if (filterData.get(position).get(
                            "FromShopPOSTypeId").equalsIgnoreCase("20") || filterData.get(position).get(
                            "FromShopPOSTypeId").equalsIgnoreCase("21")) {
                        //Show sub catagory

                        Intent explore = new Intent(activity, CarBikeActivity.class);
                        explore.putExtra("POSId", filterData.get(position).get(
                                "id"));
                        explore.putExtra("POSTypeId", filterData.get(position).get(
                                "FromShopPOSTypeId"));
                        explore.putExtra("PosName", filterData.get(position).get(
                                "name"));
                        explore.putExtra("ImagePath", filterData.get(position).get(
                                "image"));
                        explore.putExtra("Address", filterData.get(position).get(
                                "Address"));
                        explore.putExtra("CategoryId", "0");
                       /* explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                                "ShopPromotionMsg"));*/
                        explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                                "IsSubCatAvailable"));
                       /* explore.putExtra("AvgRating", filterData.get(position).get(
                                "AvgRating") + " (" + filterData.get(position).get(
                                "TotalReviews") + " Reviews)");*/
                        explore.putExtra("Town", filterData.get(position).get(
                                "Town"));

                        activity.startActivity(explore);

                    } else {

                        //Show sub catagory
                        Intent explore = new Intent(activity, ExploreActivity.class);
                        explore.putExtra("POSId", filterData.get(position).get(
                                "id"));
                        explore.putExtra("PosName", filterData.get(position).get(
                                "name"));
                        explore.putExtra("ImagePath", filterData.get(position).get(
                                "image"));
                        explore.putExtra("Address", filterData.get(position).get(
                                "Address"));
                        explore.putExtra("CategoryId", "0");
                        /*explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                                "ShopPromotionMsg"));*/
                        explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                                "IsSubCatAvailable"));
                        /*explore.putExtra("AvgRating", filterData.get(position).get(
                                "AvgRating") + " (" + filterData.get(position).get(
                                "TotalReviews") + " Reviews)");*/
                        activity.startActivity(explore);
                    }
                } else {
                    //Show  catagory
                    Intent explore = new Intent(activity, CatogaryActivity.class);
                    explore.putExtra("POSId", filterData.get(position).get(
                            "id"));
                    explore.putExtra("PosName", filterData.get(position).get(
                            "name"));
                    explore.putExtra("ImagePath", filterData.get(position).get(
                            "image"));
                    explore.putExtra("Address", filterData.get(position).get(
                            "Address"));
                        /*explore.putExtra("CategoryId", filterData.get(position).get(
                            "CategoryId"));*/
                    explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                            "IsSubCatAvailable"));

                    /*explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                            "ShopPromotionMsg"));

                    explore.putExtra("AvgRating", filterData.get(position).get(
                            "AvgRating") + " (" + filterData.get(position).get(
                            "TotalReviews") + " Reviews)");
                            */

                    explore.putExtra("POSTypeId",  filterData.get(position).get(
                            "FromShopPOSTypeId"));

                    activity.startActivity(explore);
                }
            }
        });


        return vi;
    }

   /* public Filter getFilter() {
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
                        if (data.get("name").toLowerCase().contains(constraint)) {
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
    }*/

    public static class ViewHolder {
        TextView name, type, Town, Address;
        ImageView img;
        LinearLayout explore;

    }
}


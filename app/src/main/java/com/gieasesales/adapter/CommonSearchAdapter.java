package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.UniversalSearchActivity;

import java.util.List;
import java.util.Map;

public class CommonSearchAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;

    public CommonSearchAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.common_search_adapter, null);
            holder = new ViewHolder();
            holder.name = vi.findViewById(R.id.name);
            holder.img = vi.findViewById(R.id.img);
            holder.type = vi.findViewById(R.id.type);
            holder.click = vi.findViewById(R.id.click);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.name.setText(filterData.get(position).get(
                "name"));
        if (filterData.get(position).get(
                "type").equalsIgnoreCase("1")) {
            holder.type.setText("Shop");
        } else {
            holder.type.setText("Product");
        }

        if (filterData.get(position).get(
                "imgavailable").equalsIgnoreCase("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "image")).into(holder.img);
        } else {
            holder.img.setVisibility(View.INVISIBLE);
        }

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent detail = new Intent(activity, UniversalSearchActivity.class);
                    detail.putExtra("type", filterData.get(position).get(
                            "type"));
                    detail.putExtra("ShopName", filterData.get(position).get(
                            "name"));
                    activity.startActivity(detail);
                }

                // activity.finish();
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
    }*/

    public static class ViewHolder {
        TextView name, type;
        ImageView img;
        LinearLayout click;

    }
}


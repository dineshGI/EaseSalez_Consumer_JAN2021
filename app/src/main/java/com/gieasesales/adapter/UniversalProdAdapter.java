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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Filter;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.ProdDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UniversalProdAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;


    public UniversalProdAdapter(Activity context, List<Map<String, String>> listCollectionone) {
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
            vi = inflater.inflate(R.layout.universalprod_adapter, null);

            holder = new ViewHolder();
            holder.name = vi.findViewById(R.id.name);
            holder.img = vi.findViewById(R.id.img);
            //  holder.ProductName = vi.findViewById(R.id.ProductName);

            holder.Town = vi.findViewById(R.id.Town);
            holder.txtadd = vi.findViewById(R.id.txtadd);
            holder.product_view = vi.findViewById(R.id.product_view);
            holder.ProductName = vi.findViewById(R.id.ProductName);
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.name.setText("From, " + filterData.get(position).get(
                "FromShopPosName"));
        holder.MRP = vi.findViewById(R.id.MRP);
        holder.SellingPrice = vi.findViewById(R.id.SellingPrice);
        holder.ProductName.setText(filterData.get(position).get(
                "name"));
        holder.Town.setText(filterData.get(position).get(
                "FromShopTown"));
        holder.MRP.setText(activity.getString(R.string.currency) + filterData.get(position).get(
                "MRP"));
        holder.MRP.setPaintFlags(holder.MRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.SellingPrice.setText(activity.getString(R.string.currency) + filterData.get(position).get(
                "SellingPrice"));

        if (filterData.get(position).get(
                "imgavailable").equalsIgnoreCase("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "image")).into(holder.img);
        } else {
            holder.img.setVisibility(View.INVISIBLE);
        }

        if (filterData.get(position).get("IsAttributeAdded").equalsIgnoreCase("1")) {
            holder.product_view.setVisibility(View.VISIBLE);
            // holder.TxtMrp.setText(activity.getString(R.string.currency) + filterData.get(position).getDisPlayPriceText());
        } else {
           /* if (filterData.get(position).getDiscountValue() != 0) {
                holder.TxtSellingPrice.setVisibility(View.VISIBLE);
            }*/
            holder.product_view.setVisibility(View.GONE);
        }
        holder.product_view.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent product = new Intent(activity, ProdDetailActivity.class);
                    product.putExtra("ProductId", filterData.get(position).get("id"));
                    product.putExtra("wishlist", "false");
                    activity.startActivity(product);
                }


            }
        });
        holder.txtadd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
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
                    List<Map<String, String>> filterResultsData = new ArrayList<>();
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
        TextView name, MRP, SellingPrice, Town, txtadd, ProductName;
        ImageView img, product_view;
    }
}


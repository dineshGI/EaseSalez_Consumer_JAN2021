package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.gieasesales.R;

import java.util.List;
import java.util.Map;

public class OpenOrderAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public OpenOrderAdapter(Activity context, List<Map<String, String>> listCollectionone) {

        activity = context;
        filterData = listCollectionone;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {

            vi = inflater.inflate(R.layout.open_order_adapter, null);
            holder = new ViewHolder();
            holder.TxtOrderNo = vi.findViewById(R.id.order_no);
            holder.TxtTotalItems = vi.findViewById(R.id.total_items);
            holder.TxtName = vi.findViewById(R.id.name);
            holder.TxtMobileNo = vi.findViewById(R.id.mobileno);
            holder.TxtTime = vi.findViewById(R.id.time);
            holder.TxtTotal = vi.findViewById(R.id.total);
            holder.TxtDiscount = vi.findViewById(R.id.discount);
            holder.TxtNet = vi.findViewById(R.id.net);
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.TxtOrderNo.setText(filterData.get(position).get(
                "orderno"));
        holder.TxtTotalItems.setText(filterData.get(position).get(
                "totalitems"));
        holder.TxtName.setText(filterData.get(position).get(
                "name"));
        holder.TxtMobileNo.setText(filterData.get(position).get(
                "mobileno"));
        holder.TxtTime.setText(filterData.get(position).get(
                ""));
        holder.TxtTotal.setText(filterData.get(position).get(
                ""));
        holder.TxtDiscount.setText(filterData.get(position).get(
                ""));
        holder.TxtNet.setText(filterData.get(position).get(
                ""));

        return vi;
    }


    public static class ViewHolder {
        TextView TxtOrderNo, TxtTotalItems, TxtName, TxtMobileNo,TxtTime,TxtTotal,TxtDiscount,TxtNet;

    }
}


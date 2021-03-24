package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gieasesales.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class PaymentDueAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public PaymentDueAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {
            vi = inflater.inflate(R.layout.paymentdue_adapter, null);
            holder = new ViewHolder();
            holder.POSName = vi.findViewById(R.id.POSName);
            holder.BalanceAmount = vi.findViewById(R.id.BalanceAmount);
            holder.DueDate = vi.findViewById(R.id.DueDate);
            holder.ConsumerMobileNo = vi.findViewById(R.id.ConsumerMobileNo);
            holder.ConsumerName = vi.findViewById(R.id.ConsumerName);
            holder.POSImagePath = vi.findViewById(R.id.POSImagePath);
            holder.ShopAddress = vi.findViewById(R.id.ShopAddress);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.POSName.setText(filterData.get(position).get(
                "POSName"));
        holder.ShopAddress.setText(filterData.get(position).get(
                "ShopAddress"));


        holder.BalanceAmount.setText("Due Amount :  " + activity.getString(R.string.currency) + " " + filterData.get(position).get(
                "BalanceAmount"));
        if (filterData.get(position).get(
                "DueDate").equalsIgnoreCase("null")) {
            holder.DueDate.setVisibility(View.GONE);
        } else {
            holder.DueDate.setText("Due Date :  " + filterData.get(position).get(
                    "DueDate"));
        }
        holder.ConsumerMobileNo.setText("MobileNo :  " + filterData.get(position).get(
                "ConsumerMobileNo"));
        holder.ConsumerName.setText("Consumer Name :  " + filterData.get(position).get(
                "ConsumerName"));

        if (filterData.get(position).get("POSImagePath").contains(".jpg") || filterData.get(position).get("POSImagePath").contains(".png")) {
            try {
                Glide.with(activity).load(new URL(filterData.get(position).get("POSImagePath"))).into(holder.POSImagePath);
            } catch (MalformedURLException e) {
                e.printStackTrace();

            }
        } else {
            holder.POSImagePath.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_product));
        }

        return vi;
    }

    public static class ViewHolder {
        TextView POSName, BalanceAmount, DueDate, ConsumerMobileNo, ConsumerName,ShopAddress;
        ImageView POSImagePath;

    }
}


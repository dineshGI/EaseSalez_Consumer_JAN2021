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

public class HistoryDetailAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public HistoryDetailAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.historydetail_adapter, null);
            holder = new ViewHolder();
            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.TxtQty = vi.findViewById(R.id.quantity);
            holder.TxtPerUnit = vi.findViewById(R.id.peritem);
            holder.TxtTotal = vi.findViewById(R.id.amount);
            holder.TxtOffer = vi.findViewById(R.id.offeramount);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtItemName.setText(filterData.get(position).get(
                "ProductName"));
        holder.TxtPerUnit.setText(activity.getString(R.string.currency) + String.format("%.2f", Double.parseDouble(filterData.get(position).get(
                "CostMRP"))) + " x " + filterData.get(position).get(
                "QTY"));
        holder.TxtQty.setText("Qty : " + filterData.get(position).get(
                "QTY"));
        //holder.TxtTotal.setText(activity.getString(R.string.currency) + Util.ChangeNumber(filterData.get(position).get("TotalMRP")));
        holder.TxtTotal.setText(activity.getString(R.string.currency) + String.format("%.2f", Double.parseDouble(filterData.get(position).get(
                "NetPay"))));
        holder.TxtOffer.setText(activity.getString(R.string.currency) + String.format("%.2f", Double.parseDouble(filterData.get(position).get(
                "TotalDiscountAmount"))) + " off");



        return vi;
    }

    public static class ViewHolder {
        TextView TxtItemName, TxtPerUnit, TxtQty, TxtTotal, TxtOffer;

    }
}


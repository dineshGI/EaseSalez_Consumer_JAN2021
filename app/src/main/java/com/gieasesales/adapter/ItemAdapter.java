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

public class ItemAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public ItemAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.TxtMrp = vi.findViewById(R.id.mrp);
            holder.TxtSellingPrice = vi.findViewById(R.id.selling_price);
            holder.TxtDiscount = vi.findViewById(R.id.discount);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtItemName.setText(filterData.get(position).get(
                "ProductName"));
      /*  holder.TxtOriginalPrice.setText(filterData.get(position).get(
                "DiscountValue"));*/
        holder.TxtSellingPrice.setText(filterData.get(position).get(
                "SellinPrice"));
        holder.TxtDiscount.setText("");

        return vi;
    }

    public static class ViewHolder {
        TextView TxtItemName, TxtSellingPrice, TxtDiscount,TxtMrp;

    }
}


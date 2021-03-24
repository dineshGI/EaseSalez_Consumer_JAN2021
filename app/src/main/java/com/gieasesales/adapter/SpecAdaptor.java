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

public class SpecAdaptor extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public SpecAdaptor(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.spec_adaptor, null);
            holder = new ViewHolder();
            holder.left = vi.findViewById(R.id.left);
            holder.right = vi.findViewById(R.id.right);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.left.setText(filterData.get(position).get(
                "AttributeName"));
        holder.right.setText(filterData.get(position).get(
                "AttributeValue"));

        return vi;
    }

    public static class ViewHolder {
        TextView left, right;

    }
}


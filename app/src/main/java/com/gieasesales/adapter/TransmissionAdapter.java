package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.GridView;

import com.gieasesales.Model.Products;
import com.gieasesales.R;
import com.gieasesales.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransmissionAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;

    public TransmissionAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.make_adapter, null);
            holder = new ViewHolder();
            holder.checkbox = vi.findViewById(R.id.checkbox);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);
                holder.checkbox.setChecked(true);
            }

        });

        holder.checkbox.setText(filterData.get(position).get(
                "Transmission"));

        return vi;
    }

    public static class ViewHolder {
        CheckBox checkbox;

    }
}


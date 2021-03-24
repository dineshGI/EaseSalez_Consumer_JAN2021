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

public class GalleryAdaptor extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public GalleryAdaptor(Activity context, List<Map<String, String>> listCollectionone) {
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

            vi = inflater.inflate(R.layout.gallery_adapter, null);

            holder = new ViewHolder();
            holder.img = vi.findViewById(R.id.img);
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        try {
            Glide.with(activity).load(new URL(filterData.get(position).get("PrdImage"))).into(holder.img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return vi;

    }

    public static class ViewHolder {
        ImageView img;

    }
}


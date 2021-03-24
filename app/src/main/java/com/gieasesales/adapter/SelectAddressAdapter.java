package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gieasesales.R;

import java.util.List;
import java.util.Map;

public class SelectAddressAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public SelectAddressAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.select_address_adapter, null);
            holder = new ViewHolder();
            holder.address = vi.findViewById(R.id.address);
            holder.ContactName = vi.findViewById(R.id.ContactName);
            holder.btn_delete = vi.findViewById(R.id.btn_delete);
            holder.btn_edit = vi.findViewById(R.id.btn_edit);
            holder.btn_default = vi.findViewById(R.id.btn_default);
            holder.btn_img_edit = vi.findViewById(R.id.btn_img_edit);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        String data = filterData.get(position).get(
                "Address1") + "\n" + "Landmark : " + filterData.get(position).get(
                "LandMark") + "\n" + "Mobile No : " + filterData.get(position).get(
                "ContactNumber") + "\n" + "City : " + filterData.get(position).get(
                "City") + "\n" + "Pincode : " + filterData.get(position).get(
                "Pincode");

        holder.address.setText(data);

        holder.ContactName.setText(filterData.get(position).get(
                "ContactName"));

        holder.btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);

            }
        });

        return vi;
    }

    public static class ViewHolder {
        TextView address, ContactName;
        TextView btn_delete, btn_edit, btn_default;
        ImageView btn_img_edit;

    }
}


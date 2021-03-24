package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.utils.ImageAlertDialog;
import com.gieasesales.utils.Util;

import java.util.List;
import java.util.Map;

public class CartAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public CartAdapter(Activity context, List<Map<String, String>> listCollectionone) {

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {
            vi = inflater.inflate(R.layout.sample, null);
            holder = new ViewHolder();

            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.ImgDelete = vi.findViewById(R.id.btn_delete);
            holder.TxtMinus = vi.findViewById(R.id.minus);
            holder.TxtPlus = vi.findViewById(R.id.plus);
            holder.TxtTotal = vi.findViewById(R.id.selling_price);
            holder.TxtQty = vi.findViewById(R.id.qtys);
            holder.ImgProduct = vi.findViewById(R.id.img_prod);
            holder.hideimage = vi.findViewById(R.id.hideimage);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtItemName.setText(filterData.get(position).get(
                "ProductName"));
        holder.TxtQty.setText(filterData.get(position).get(
                "Qty"));


        holder.TxtTotal.setText(activity.getString(R.string.currency) + String.format("%.2f", Double.parseDouble(filterData.get(position).get(
                "NetPrice"))));


        if (filterData.get(position).get(
                "imgavailable").contains("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "ImagePath")).into(holder.ImgProduct);
        } else {
            Util.Logcat.e("FALSE" + "NO IMG");
            holder.ImgProduct.setImageDrawable(activity.getDrawable(R.drawable.no_product));
            holder.hideimage.setVisibility(View.INVISIBLE);
        }

        holder.ImgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAlertDialog alert = new ImageAlertDialog(activity);
                alert.showimg(filterData.get(position).get(
                        "ImagePath"));
            }
        });

        holder.TxtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        holder.TxtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);

            }
        });

        holder.ImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return vi;
    }

    public static class ViewHolder {
        TextView TxtItemName, TxtMinus, TxtPlus, TxtTotal, TxtQty;
        ImageView ImgDelete, ImgProduct;
        LinearLayout hideimage;

    }
}


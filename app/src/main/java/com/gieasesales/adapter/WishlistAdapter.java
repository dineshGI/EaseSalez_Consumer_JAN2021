package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeDetail;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ImageAlertDialog;
import com.gieasesales.activity.ProdDetailActivity;
import com.gieasesales.utils.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishlistAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;
    //private List<Products> filterData, originaldata;
    CommonAlertDialog alert;
    public static List<String> cart = new ArrayList<>();

    public WishlistAdapter(Activity context, List<Map<String, String>> listCollectionone) {
        activity = context;
        filterData = listCollectionone;
        originaldata = listCollectionone;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alert = new CommonAlertDialog(activity);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
            vi = inflater.inflate(R.layout.wishlist_adapter_new, null);
            holder = new ViewHolder();

            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.TxtMrp = vi.findViewById(R.id.mrp);
            holder.lynormalprice = vi.findViewById(R.id.lynormalprice);
            holder.TxtSellingPrice = vi.findViewById(R.id.selling_price);
            holder.TextOffer = vi.findViewById(R.id.discount);
            holder.LyAdd = vi.findViewById(R.id.lyadd);
            holder.TxtAdd = vi.findViewById(R.id.txtadd);
            holder.Imgproduct = vi.findViewById(R.id.pimg);
            holder.product_view = vi.findViewById(R.id.product_view);
            holder.rating_bar = vi.findViewById(R.id.rating_bar);
            holder.delete = vi.findViewById(R.id.delete);
            holder.SellingPriceWords = vi.findViewById(R.id.SellingPriceWords);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtItemName.setText(filterData.get(position).get("ProductName"));
        holder.SellingPriceWords.setText(activity.getString(R.string.currency) +filterData.get(position).get("SellingPriceWords"));
        holder.TxtMrp.setText(activity.getString(R.string.currency) + filterData.get(position).get("SellinPrice"));
        holder.TxtSellingPrice.setText(activity.getString(R.string.currency) + filterData.get(position).get("MRP"));
        holder.TxtSellingPrice.setPaintFlags(holder.TxtSellingPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (filterData.get(position).get("DiscountValue").equalsIgnoreCase("0.0")) {
            holder.TextOffer.setVisibility(View.INVISIBLE);
        } else {
            holder.TextOffer.setText(activity.getString(R.string.currency) + filterData.get(position).get("DiscountValue") + " off");
            Util.Logcat.e("DiscountPercentage :" + filterData.get(position).get("DiscountPercentage"));
        }

        if (filterData.get(position).get("FilePath").contains(".jpg") || filterData.get(position).get("FilePath").contains(".png") || filterData.get(position).get("FilePath").contains(".PNG") || filterData.get(position).get("FilePath").contains(".jpeg")) {
            try {
                Glide.with(activity).load(new URL(filterData.get(position).get("FilePath"))).into(holder.Imgproduct);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            holder.Imgproduct.setVisibility(View.GONE);
        }

        if (filterData.get(position).get("IsAttributeAdded").equalsIgnoreCase("1")) {
            holder.product_view.setVisibility(View.VISIBLE);
            holder.TxtMrp.setText(activity.getString(R.string.currency) + filterData.get(position).get("DisPlayPriceText"));
        } else {
            //   Util.Logcat.e("DiscountValue :"+filterData.get(position).get("DiscountValue"));
            if (filterData.get(position).get("DiscountValue").equalsIgnoreCase("0.0")) {
                holder.TxtSellingPrice.setVisibility(View.GONE);
            } else {
                holder.TxtSellingPrice.setVisibility(View.VISIBLE);
            }
            holder.product_view.setVisibility(View.INVISIBLE);
        }

        if (Float.valueOf(filterData.get(position).get("ReviewCnt")) > 0) {
            holder.rating_bar.setVisibility(View.VISIBLE);
            //  Integer i = Integer.parseInt(filterData.get(position).get("ReviewCnt"));
            //  Float b = i.floatValue();
            holder.rating_bar.setRating(Float.valueOf(filterData.get(position).get("ReviewCnt")));
        } else {
            holder.rating_bar.setVisibility(View.INVISIBLE);
        }

        holder.Imgproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAlertDialog alert = new ImageAlertDialog(activity);
                alert.showimg(filterData.get(position).get("FilePath"));
            }
        });

        holder.TxtAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);
            }
        });
        holder.product_view.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    if (filterData.get(position).get("PosTypeId").equalsIgnoreCase("20") || filterData.get(position).get("PosTypeId").equalsIgnoreCase("21")) {
                        Intent product = new Intent(activity, CarBikeDetail.class);
                        product.putExtra("ProductId", filterData.get(position).get("ProductId"));
                        product.putExtra("POSId", filterData.get(position).get("POSId"));
                        product.putExtra("wishlist", "true");
                        activity.startActivity(product);
                    } else {
                        Intent product = new Intent(activity, ProdDetailActivity.class);
                        product.putExtra("ProductId", filterData.get(position).get("ProductId"));
                        product.putExtra("POSId", filterData.get(position).get("POSId"));
                        product.putExtra("wishlist", "true");
                        activity.startActivity(product);
                    }
                }

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);

            }
        });

        if (filterData.get(position).get("PosTypeId").equalsIgnoreCase("20") || filterData.get(position).get("PosTypeId").equalsIgnoreCase("21")) {
            holder.TxtAdd.setVisibility(View.INVISIBLE);
            holder.product_view.setVisibility(View.VISIBLE);
            holder.SellingPriceWords.setVisibility(View.VISIBLE);
            holder.lynormalprice.setVisibility(View.GONE);
        } else {
            holder.lynormalprice.setVisibility(View.VISIBLE);
            holder.SellingPriceWords.setVisibility(View.GONE);
        }
        return vi;
    }

    public static class ViewHolder {
        TextView TxtItemName, TxtMrp, TxtSellingPrice, TextOffer, TxtAdd, SellingPriceWords;
        LinearLayout LyAdd, lynormalprice;
        ImageView Imgproduct, product_view, delete;
        RatingBar rating_bar;
    }
}


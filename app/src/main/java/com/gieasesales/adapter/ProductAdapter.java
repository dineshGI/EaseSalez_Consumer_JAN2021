package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.Model.Products;
import com.gieasesales.R;
import com.gieasesales.activity.ProdDetailActivity;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.ImageAlertDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private ProgressDialog progressDialog;
    //List<Map<String, String>> filterData;
    private List<Products> filterData, originaldata;
    public static List<String> cart = new ArrayList<>();
    CommonAlertDialog alert;
    String UI;

    public ProductAdapter(Activity context, List<Products> listCollectionone, String design) {

        activity = context;
        UI = design;
        filterData = listCollectionone;
        originaldata = listCollectionone;
        alert = new CommonAlertDialog(activity);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

   /* public ProductAdapter(Context context, List<Products> myOrders) {
        super(activity, 0, myOrders);
        this.list = myOrders;
        this.context = context;
    }*/

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
            if (UI.equalsIgnoreCase("0")) {
                vi = inflater.inflate(R.layout.product_adapter, null);
            } else {
                vi = inflater.inflate(R.layout.product_adapter_grid, null);
            }

            holder = new ViewHolder();

            holder.TxtItemName = vi.findViewById(R.id.itemname);
            holder.TxtMrp = vi.findViewById(R.id.mrp);
            holder.TxtSellingPrice = vi.findViewById(R.id.selling_price);
            holder.TextOffer = vi.findViewById(R.id.discount);
            holder.LyAdd = vi.findViewById(R.id.lyadd);
            holder.TxtAdd = vi.findViewById(R.id.txtadd);
            holder.Imgproduct = vi.findViewById(R.id.pimg);
            holder.add_wishlist = vi.findViewById(R.id.add_wishlist);
            holder.product_view = vi.findViewById(R.id.product_view);
            holder.rating_bar = vi.findViewById(R.id.rating_bar);
            holder.hideimage = vi.findViewById(R.id.hideimage);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtItemName.setText(filterData.get(position).getProductName());
        holder.TxtMrp.setText(activity.getString(R.string.currency) + String.format("%.2f", filterData.get(position).getSellinPrice()));
        holder.TxtSellingPrice.setText(activity.getString(R.string.currency) + String.format("%.2f", filterData.get(position).getMrp()));
        holder.TxtSellingPrice.setPaintFlags(holder.TxtSellingPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (filterData.get(position).getDiscountValue() == 0) {
            holder.TextOffer.setVisibility(View.INVISIBLE);
        } else {
            holder.TextOffer.setText(activity.getString(R.string.currency) + String.format("%.2f", filterData.get(position).getDiscountValue()) + " off");
        }

        if (filterData.get(position).getProductImg().contains(".jpg") || filterData.get(position).getProductImg().contains(".png") || filterData.get(position).getProductImg().contains(".PNG") || filterData.get(position).getProductImg().contains(".jpeg")) {
            try {
                Glide.with(activity).load(new URL(filterData.get(position).getProductImg())).into(holder.Imgproduct);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            if (UI.equalsIgnoreCase("1")) {
                holder.hideimage.setVisibility(View.GONE);
            } else {
                holder.Imgproduct.setVisibility(View.GONE);
            }
            // holder.Imgproduct.setVisibility(View.GONE);
            holder.Imgproduct.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_product));
            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 500);
                }
            }, 1000);*/
        }

        holder.Imgproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAlertDialog alert = new ImageAlertDialog(activity);
                alert.showimg(filterData.get(position).getProductImg());
            }
        });

        holder.TxtAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);
            }
        });

        holder.add_wishlist.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);
                // holder.add_wishlist.setImageDrawable(activity.getDrawable(R.drawable.heart_red));
            }
        });
        if (filterData.get(position).getIsAttributeAdded().equalsIgnoreCase("1")) {
            holder.product_view.setVisibility(View.VISIBLE);
            holder.TxtMrp.setText(activity.getString(R.string.currency) + filterData.get(position).getDisPlayPriceText());
        } else {
            if (filterData.get(position).getDiscountValue() != 0) {
                holder.TxtSellingPrice.setVisibility(View.VISIBLE);
            }
            holder.product_view.setVisibility(View.INVISIBLE);
        }

        if (filterData.get(position).getRatingAverage() > 0) {
            holder.rating_bar.setVisibility(View.VISIBLE);
            Integer i = filterData.get(position).getRatingAverage();
            Float b = i.floatValue();
            holder.rating_bar.setRating(b);
            // Util.Logcat.e("getRatingAverage 1:" + filterData.get(position).getRatingAverage());
        } else {
            holder.rating_bar.setVisibility(View.INVISIBLE);
            //  Util.Logcat.e("getRatingAverage 2:" + filterData.get(position).getRatingAverage());
        }

        holder.product_view.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent product = new Intent(activity, ProdDetailActivity.class);
                    product.putExtra("ProductId", filterData.get(position).getProductid());
                    product.putExtra("wishlist", "false");
                    activity.startActivity(product);
                }

            }
        });

        return vi;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originaldata;
                    results.count = originaldata.size();
                } else {
                    List<Products> filterResultsData = new ArrayList<Products>();
                    for (Products data : originaldata) {
                        if (data.getProductName().toLowerCase().contains(constraint)) {
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterData.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterData = (List<Products>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder {

        TextView TxtItemName, TxtMrp, TxtSellingPrice, TextOffer, TxtAdd;
        LinearLayout LyAdd, hideimage;
        ImageView Imgproduct, add_wishlist, product_view;
        RatingBar rating_bar;

    }
}


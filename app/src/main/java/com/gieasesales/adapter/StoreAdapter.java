package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.CarBikeActivity;
import com.gieasesales.activity.CatogaryActivity;
import com.gieasesales.activity.ExploreActivity;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;
    CommonAlertDialog alert;

    public StoreAdapter(Activity context, List<Map<String, String>> listCollectionone) {

        activity = context;
        filterData = listCollectionone;
        originaldata = listCollectionone;
        alert = new CommonAlertDialog(activity);

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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {

            vi = inflater.inflate(R.layout.store_adapter, null);
            holder = new ViewHolder();
            holder.TxtName = vi.findViewById(R.id.name);
            holder.TxtAddress = vi.findViewById(R.id.address);

            holder.TxtExplore = vi.findViewById(R.id.explore);
            holder.Imageview = vi.findViewById(R.id.img);
            holder.addimage = vi.findViewById(R.id.addimage);
            //holder.TxtScan = vi.findViewById(R.id.direction);
            holder.City = vi.findViewById(R.id.city);

            holder.AvgRating = vi.findViewById(R.id.AvgRating);
            //holder.store_time = vi.findViewById(R.id.store_time);
            holder.Distance = vi.findViewById(R.id.Distance);
            holder.pickimg = vi.findViewById(R.id.pickimg);
            holder.delivery = vi.findViewById(R.id.delivery);
            holder.review_count = vi.findViewById(R.id.review_count);
            holder.ShopPromotionMsg = vi.findViewById(R.id.ShopPromotionMsg);
            holder.ShopOffers = vi.findViewById(R.id.ShopOffers);
            holder.carcount = vi.findViewById(R.id.carcount);
            holder.loan = vi.findViewById(R.id.loan);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtName.setText(filterData.get(position).get(
                "PosName"));
        holder.TxtAddress.setText(filterData.get(position).get(
                "Town"));
        holder.City.setText(filterData.get(position).get(
                "Town"));

        if (filterData.get(position).get(
                "ShopPromotionMsg").isEmpty()) {
            holder.ShopPromotionMsg.setVisibility(View.INVISIBLE);
        } else {
            holder.ShopPromotionMsg.setVisibility(View.VISIBLE);
            holder.ShopPromotionMsg.setText(filterData.get(position).get(
                    "ShopPromotionMsg"));
        }

        if (filterData.get(position).get(
                "ShopOffers").isEmpty()) {
            holder.ShopOffers.setVisibility(View.GONE);
        } else {
            holder.ShopOffers.setVisibility(View.VISIBLE);
            holder.ShopOffers.setText(filterData.get(position).get(
                    "ShopOffers"));
        }

        holder.Distance.setText("Kms \n" + filterData.get(position).get(
                "Distance") + " KM");

        /*holder.pos_type.setText(filterData.get(position).get(
                "PosType"));
                holder.Distance.setText("Total Reviews : " + filterData.get(position).get(
                "Distance"));
                */
       /* holder.store_time.setText("Hours\n" + filterData.get(position).get(
                "DeliveryFromTime") + "-" + filterData.get(position).get(
                "DeliveryToTime"));*/

        holder.AvgRating.setText(filterData.get(position).get(
                "AvgRating"));

        holder.review_count.setText(" (" + filterData.get(position).get(
                "TotalReviews") + " Reviews" + ")");

        if (filterData.get(position).get(
                "imgavailable").contains("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "ImagePath")).into(holder.Imageview);
            //ImagePath
        } else {
            Util.Logcat.e("FALSE" + "NO IMG");
            //holder.Imageview.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_product));
            holder.Imageview.setVisibility(View.INVISIBLE);
        }

       /* if (filterData.get(position).get(
                "OrderAcceptanceFlag").equalsIgnoreCase("1")) {
            holder.TxtExplore.setClickable(false);
            holder.TxtExplore.setEnabled(false);
            holder.shopclosed.setText("Shop Closed");
            holder.TxtScan.setVisibility(View.GONE);
            holder.shopclosed.setVisibility(View.VISIBLE);
            // holder.TxtExplore.setBackgroundColor(activity.getResources().getColor(R.color.red));
        }*/

      /*  if (!filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("1") || !filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("3")) {
            holder.pickimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.hand_grey));
        }*/

        if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("1")) {
            holder.pickimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.hand));
            holder.delivery.setImageDrawable(activity.getResources().getDrawable(R.drawable.bike_grey));
        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("2")) {
            holder.pickimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.hand_grey));
            holder.delivery.setImageDrawable(activity.getResources().getDrawable(R.drawable.bike_blue));
        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("3")) {
            holder.pickimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.hand));
            holder.delivery.setImageDrawable(activity.getResources().getDrawable(R.drawable.bike_blue));
        }

        holder.carcount = vi.findViewById(R.id.carcount);
        holder.loan = vi.findViewById(R.id.loan);
        if (filterData.get(position).get(
                "POSTypeId").equalsIgnoreCase("20") || filterData.get(position).get(
                "POSTypeId").equalsIgnoreCase("21")) {
            holder.carcount.setVisibility(View.VISIBLE);
            holder.loan.setVisibility(View.VISIBLE);
            holder.pickimg.setVisibility(View.GONE);
            holder.delivery.setVisibility(View.GONE);
        } else {
            holder.carcount.setVisibility(View.GONE);
            holder.loan.setVisibility(View.GONE);
            holder.pickimg.setVisibility(View.VISIBLE);
            holder.delivery.setVisibility(View.VISIBLE);
        }


        if (filterData.get(position).get(
                "POSTypeId").equalsIgnoreCase("20")) {
            holder.carcount.setText("50+ Cars");
        }else {
            holder.carcount.setText("50+ Bikes");
        }

        holder.TxtExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterData.get(position).get(
                        "OrderAcceptanceFlag").equalsIgnoreCase("1")) {
                    alert.build("Shop Closed", false);

                } else {

                    Util.saveData("DeliveryTypeId", filterData.get(position).get(
                            "DeliveryTypeId"), activity.getApplicationContext());

                    Util.saveData("Terms", filterData.get(position).get(
                            "Terms"), activity.getApplicationContext());

                    Util.shop_address = filterData.get(position).get(
                            "Address");
                    Util.contact_person = filterData.get(position).get(
                            "MDName");
                    Util.mobileno = filterData.get(position).get(
                            "PhoneNo");
                    Util.shopimg = filterData.get(position).get(
                            "ImagePath");

                    Util.working_hours = filterData.get(position).get(
                            "DeliveryFromTime") + " - " + filterData.get(position).get(
                            "DeliveryToTime");
                    Util.Logcat.e("POSTypeId :" + filterData.get(position).get(
                            "POSTypeId"));

                    if (filterData.get(position).get(
                            "IsSubCatAvailable").equalsIgnoreCase("0")) {

                        if (filterData.get(position).get(
                                "POSTypeId").equalsIgnoreCase("20") || filterData.get(position).get(
                                "POSTypeId").equalsIgnoreCase("21")) {
                            //Show sub catagory

                            Intent explore = new Intent(activity, CarBikeActivity.class);
                            explore.putExtra("POSId", filterData.get(position).get(
                                    "POSId"));
                            explore.putExtra("POSTypeId", filterData.get(position).get(
                                    "POSTypeId"));
                            explore.putExtra("PosName", filterData.get(position).get(
                                    "PosName"));
                            explore.putExtra("ImagePath", filterData.get(position).get(
                                    "ImagePath"));
                            explore.putExtra("Address", filterData.get(position).get(
                                    "Address"));
                            explore.putExtra("CategoryId", "0");
                            explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                                    "ShopPromotionMsg"));
                            explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                                    "IsSubCatAvailable"));
                            explore.putExtra("AvgRating", filterData.get(position).get(
                                    "AvgRating") + " (" + filterData.get(position).get(
                                    "TotalReviews") + " Reviews)");
                            explore.putExtra("Town", filterData.get(position).get(
                                    "Town"));

                            activity.startActivity(explore);

                        } else {

                            //Show sub catagory
                            Intent explore = new Intent(activity, ExploreActivity.class);
                            explore.putExtra("POSId", filterData.get(position).get(
                                    "POSId"));
                            explore.putExtra("PosName", filterData.get(position).get(
                                    "PosName"));
                            explore.putExtra("ImagePath", filterData.get(position).get(
                                    "ImagePath"));
                            explore.putExtra("Address", filterData.get(position).get(
                                    "Address"));
                            explore.putExtra("CategoryId", "0");
                            explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                                    "ShopPromotionMsg"));
                            explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                                    "IsSubCatAvailable"));
                            explore.putExtra("AvgRating", filterData.get(position).get(
                                    "AvgRating") + " (" + filterData.get(position).get(
                                    "TotalReviews") + " Reviews)");
                            activity.startActivity(explore);
                        }
                    } else {
                        //Show  catagory
                        Intent explore = new Intent(activity, CatogaryActivity.class);
                        explore.putExtra("POSId", filterData.get(position).get(
                                "POSId"));
                        explore.putExtra("PosName", filterData.get(position).get(
                                "PosName"));
                        explore.putExtra("ImagePath", filterData.get(position).get(
                                "ImagePath"));
                        explore.putExtra("Address", filterData.get(position).get(
                                "Address"));
                        /*explore.putExtra("CategoryId", filterData.get(position).get(
                            "CategoryId"));*/
                        explore.putExtra("IsSubCatAvailable", filterData.get(position).get(
                                "IsSubCatAvailable"));
                        explore.putExtra("ImagePath", filterData.get(position).get(
                                "ImagePath"));
                        explore.putExtra("ShopPromotionMsg", filterData.get(position).get(
                                "ShopPromotionMsg"));

                        explore.putExtra("AvgRating", filterData.get(position).get(
                                "AvgRating") + " (" + filterData.get(position).get(
                                "TotalReviews") + " Reviews)");
                        explore.putExtra("POSTypeId", filterData.get(position).get(
                                "POSTypeId"));

                        activity.startActivity(explore);
                    }

                }
            }
        });


        if (Util.getData("ProdcutOrderDisplay", activity.getApplicationContext()).equalsIgnoreCase("0")) {

            Glide.with(activity).load(Util.getData("listimage", activity.getApplicationContext())).into(holder.addimage);
            holder.addimage.setVisibility(View.GONE);
        } else {
            int value = position % Integer.parseInt(Util.getData("ProdcutOrderDisplay", activity.getApplicationContext()));
            //  Util.Logcat.e("POSSS" + value);
            if (value == 1) {

                if (Util.getData("listimage", activity.getApplicationContext()).isEmpty()) {
                    holder.addimage.setVisibility(View.GONE);
                } else {
                    Glide.with(activity).load(Util.getData("listimage", activity.getApplicationContext())).into(holder.addimage);
                    holder.addimage.setVisibility(View.VISIBLE);
                }

            } else {
                holder.addimage.setVisibility(View.GONE);
            }
        }
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
                    List<Map<String, String>> filterResultsData = new ArrayList<Map<String, String>>();
                    for (Map<String, String> data : originaldata) {
                        if (data.get("PosName").toLowerCase().contains(constraint) || data.get("Address").toLowerCase().contains(constraint)) {
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
                filterData = (List<Map<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder {

        TextView TxtName, TxtAddress, AvgRating, City, Distance, review_count, ShopPromotionMsg, ShopOffers, shopclosed;
        //TextView pos_type,TotalReviews,store_time;
        ImageView Imageview, pickimg, delivery, addimage;
        LinearLayout TxtExplore;
        TextView carcount, loan;
        //ViewPager addimage;
    }

}


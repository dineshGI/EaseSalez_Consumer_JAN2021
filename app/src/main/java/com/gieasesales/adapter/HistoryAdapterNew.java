package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.activity.HistoryDetailActivity;
import com.gieasesales.activity.ReviewActivity;
import com.gieasesales.activity.TrackDetails;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryAdapterNew extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData, originaldata;
    CommonAlertDialog alert;

    public HistoryAdapterNew(Activity context, List<Map<String, String>> listCollectionone) {
        activity = context;
        filterData = listCollectionone;
        originaldata = listCollectionone;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        alert = new CommonAlertDialog(activity);
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

            vi = inflater.inflate(R.layout.history_adapter_new, null);
            holder = new ViewHolder();
            holder.TxtPosName = vi.findViewById(R.id.shop_name);
            holder.TxtDate = vi.findViewById(R.id.date);
            holder.TxtAmount = vi.findViewById(R.id.amount);
            holder.TxtQty = vi.findViewById(R.id.quantity);
            holder.TxtOrderId = vi.findViewById(R.id.order_id);
            holder.TxtStatus = vi.findViewById(R.id.status);
            holder.DeliveryCharge = vi.findViewById(R.id.DeliveryCharge);

            holder.LyButton = vi.findViewById(R.id.ly_button);
            holder.Lyorder_details = vi.findViewById(R.id.order_details);

            holder.BtnCancel = vi.findViewById(R.id.views);
            holder.BtnFeedback = vi.findViewById(R.id.feedback);
            holder.BtnView = vi.findViewById(R.id.shop_name);

            holder.PaymentMode = vi.findViewById(R.id.PaymentMode);
            holder.Imageview = vi.findViewById(R.id.img);

            holder.pickuply = vi.findViewById(R.id.pickuply);
            holder.deliveryly = vi.findViewById(R.id.deliveryly);
            holder.pickup = vi.findViewById(R.id.pickup);
            holder.delivery = vi.findViewById(R.id.delivery);
            holder.DeliveryChargely = vi.findViewById(R.id.DeliveryChargely);
            holder.DeliveryType = vi.findViewById(R.id.DeliveryType);
            holder.Address = vi.findViewById(R.id.Address);
            holder.Addressly = vi.findViewById(R.id.Addressly);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.TxtPosName.setText(filterData.get(position).get(
                "POSName"));

        holder.DeliveryType.setText(" - " + filterData.get(position).get(
                "DeliveryType"));
        holder.Address.setText(" - " + filterData.get(position).get(
                "Address"));

        holder.TxtDate.setText(filterData.get(position).get(
                "IndentDate"));

        holder.TxtQty.setText("- " + filterData.get(position).get(
                "TotalQTY") + " Items");

        holder.TxtAmount.setText("- " + "₹ " + filterData.get(position).get(
                "NetPayable"));

        holder.TxtOrderId.setText(filterData.get(position).get(
                "IndentNo"));

        holder.TxtOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent viewdetail = new Intent(activity, HistoryDetailActivity.class);
                    viewdetail.putExtra("IndentId", filterData.get(position).get(
                            "IndentId"));
                    activity.startActivity(viewdetail);
                }

            }
        });

        holder.BtnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent review = new Intent(activity, ReviewActivity.class);
                    review.putExtra("POSName", filterData.get(position).get(
                            "POSName"));
                    review.putExtra("IndentId", filterData.get(position).get(
                            "IndentId"));
                    review.putExtra("POSId", filterData.get(position).get(
                            "POSId"));
                    review.putExtra("POSAddress", filterData.get(position).get(
                            "POSAddress"));
                    activity.startActivity(review);
                }


            }
        });

        /*holder.TxtStatus.setText(filterData.get(position).get(
                "IndentStatusDesc"));

        if (filterData.get(position).get(
                "IndentStatusDesc").equalsIgnoreCase("Order Cancelled")) {
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.red));
        }*/
        holder.pickup.setText(" - " + filterData.get(position).get(
                "PickUpDate"));

        holder.delivery.setText(" - " + filterData.get(position).get(
                "DeliveryFromTime") + " To " + filterData.get(position).get(
                "DeliveryToTime"));
        if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("1")) {
            Log.e("PickUpDate", filterData.get(position).get(
                    "PickUpDate"));
            if (!filterData.get(position).get(
                    "PickUpDate").isEmpty() && filterData.get(position).get(
                    "PickUpDate") != null && !filterData.get(position).get(
                    "PickUpDate").equalsIgnoreCase("00/00/0000 00:00:00")) {
                holder.pickuply.setVisibility(View.VISIBLE);
            }

        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("2")) {
            if (!filterData.get(position).get(
                    "DeliveryFromTime").isEmpty() && !filterData.get(position).get(
                    "DeliveryToTime").isEmpty()) {

                holder.deliveryly.setVisibility(View.VISIBLE);
            }
        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("3")) {

            if (!filterData.get(position).get(
                    "PickUpDate").isEmpty() && filterData.get(position).get(
                    "PickUpDate") != null && !filterData.get(position).get(
                    "PickUpDate").equalsIgnoreCase("00/00/0000 00:00:00")) {

                holder.pickuply.setVisibility(View.VISIBLE);
            }
            if (!filterData.get(position).get(
                    "DeliveryFromTime").isEmpty() && !filterData.get(position).get(
                    "DeliveryToTime").isEmpty()) {

                holder.deliveryly.setVisibility(View.VISIBLE);
            }
            // holder.pickuply.setVisibility(View.VISIBLE);
            // holder.deliveryly.setVisibility(View.VISIBLE);
        }

        holder.PaymentMode.setText("- " + filterData.get(position).get(
                "PaymentMode"));

        if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("1")) {
            holder.DeliveryChargely.setVisibility(View.GONE);
            holder.Addressly.setVisibility(View.GONE);
        } else {
            holder.DeliveryCharge.setText("- " + "₹ " + filterData.get(position).get(
                    "DeliveryCharge"));
        }

        if (filterData.get(position).get(
                "imgavailable").equalsIgnoreCase("true")) {
            Glide.with(activity).load(filterData.get(position).get(
                    "ShopImagePath")).into(holder.Imageview);
        } else {
            Util.Logcat.e("FALSE" + "NO IMG");
            holder.Imageview.setVisibility(View.INVISIBLE);
        }

       /* TotalReviews == 0 Show Review button
          TotalReviews > 0 Show Rating (Star)
          IndentStatus - 0 - Show Cancel button*/

        if (filterData.get(position).get(
                "TotalReviews").equalsIgnoreCase("0")) {
            holder.BtnFeedback.setVisibility(View.VISIBLE);
        } else {
            // holder.LyButton.setVisibility(View.GONE);
            holder.BtnCancel.setVisibility(View.GONE);
            holder.BtnFeedback.setVisibility(View.GONE);
        }

        /*if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("0")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
        } else {
            holder.BtnCancel.setVisibility(View.GONE);
        }*/
        /*IndentStatus - 0 - Order Placed - Orange
        IndentStatus - 1 - Order Accepted - Green
        IndentStatus - 2 - Order Rejected - Red
        IndentStatus - 5 - Order Delivered - Green
        IndentStatus - 6 - Order Cancelled - Red*/
        if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("0")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
            holder.TxtStatus.setText("Order Placed");
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.order_yellow));
        } else if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("1")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
            holder.TxtStatus.setText("Order Accepted");
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.green));
        } else if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("2")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
            holder.TxtStatus.setText("Order Rejected");
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.red));
        } else if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("5")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
            holder.TxtStatus.setText("Order Delivered");
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.green));
        } else if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("6")) {
            holder.BtnCancel.setVisibility(View.GONE);
            holder.TxtStatus.setText("Order Cancelled");
            holder.TxtStatus.setBackgroundColor(activity.getResources().getColor(R.color.red));
        } else {
            holder.BtnCancel.setVisibility(View.GONE);
        }

        holder.BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        holder.TxtPosName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.getPackageName().equals("com.gieasesales")) {
                    Intent trackdetails = new Intent(activity, TrackDetails.class);
                    trackdetails.putExtra("IndentNo", filterData.get(position).get(
                            "IndentNo"));
                    activity.startActivity(trackdetails);
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
                    List<Map<String, String>> filterResultsData = new ArrayList<Map<String, String>>();
                    for (Map<String, String> data : originaldata) {
                        if (data.get("POSName").toLowerCase().contains(constraint) || data.get("POSName").contains(constraint) || data.get("IndentNo").toLowerCase().contains(constraint) || data.get("IndentNo").contains(constraint)) {
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
        TextView TxtPosName, TxtAmount, TxtDate, TxtQty, TxtStatus, TxtOrderId, DeliveryType, Address;
        TextView BtnView, BtnCancel, BtnFeedback, PaymentMode, pickup, delivery, DeliveryCharge;
        LinearLayout LyButton, Lyorder_details, pickuply, deliveryly, DeliveryChargely, Addressly;
        ImageView Imageview;
        //RatingBar RatingBar;
    }
}


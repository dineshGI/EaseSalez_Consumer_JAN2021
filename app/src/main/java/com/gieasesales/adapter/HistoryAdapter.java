package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.activity.HistoryDetailActivity;
import com.gieasesales.activity.ReviewActivity;

import java.util.List;
import java.util.Map;

public class HistoryAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;
    CommonAlertDialog alert;

    public HistoryAdapter(Activity context, List<Map<String, String>> listCollectionone) {
        activity = context;
        filterData = listCollectionone;
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

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (vi == null) {

            vi = inflater.inflate(R.layout.history_adapter, null);
            holder = new ViewHolder();
            holder.TxtPosName = vi.findViewById(R.id.shop_name);
            holder.TxtPosAddress = vi.findViewById(R.id.address);
            holder.TxtDate = vi.findViewById(R.id.date);
            holder.TxtAmount = vi.findViewById(R.id.amount);
            holder.TxtQty = vi.findViewById(R.id.quantity);
            holder.TxtOrderId = vi.findViewById(R.id.order_id);
            holder.TxtStatus = vi.findViewById(R.id.status);

            holder.LyRating = vi.findViewById(R.id.lyrating);
            holder.LyButton = vi.findViewById(R.id.ly_button);
            holder.Lyorder_details = vi.findViewById(R.id.order_details);

            holder.BtnCancel = vi.findViewById(R.id.views);
            holder.BtnFeedback = vi.findViewById(R.id.feedback);
            holder.BtnView = vi.findViewById(R.id.shop_name);
            holder.RatingBar = vi.findViewById(R.id.rating_bar);
            holder.showcomments = vi.findViewById(R.id.showcomments);

            holder.pickup_date = vi.findViewById(R.id.pickup_date);
            holder.delivery_time = vi.findViewById(R.id.delivery_time);
            holder.PaymentMode = vi.findViewById(R.id.PaymentMode);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.pickup_date.setText("Pickup Date : " + filterData.get(position).get(
                "PickUpDate"));

        holder.delivery_time.setText("Delivery Time : " + filterData.get(position).get(
                "DeliveryFromTime") + "-" + filterData.get(position).get(
                "DeliveryToTime"));

        if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("1")) {
            holder.pickup_date.setVisibility(View.VISIBLE);
            holder.delivery_time.setVisibility(View.GONE);
        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("2")) {
            holder.pickup_date.setVisibility(View.GONE);
            holder.delivery_time.setVisibility(View.VISIBLE);

        } else if (filterData.get(position).get(
                "DeliveryTypeId").equalsIgnoreCase("3")) {
            holder.pickup_date.setVisibility(View.VISIBLE);
            holder.delivery_time.setVisibility(View.VISIBLE);
        } else {
            holder.pickup_date.setVisibility(View.GONE);
            holder.delivery_time.setVisibility(View.GONE);
        }
        if (filterData.get(position).get(
                "DeliveryFromTime").isEmpty() || filterData.get(position).get(
                "DeliveryToTime").isEmpty()) {
            holder.delivery_time.setVisibility(View.GONE);
        }

        if (filterData.get(position).get(
                "PickUpDate").equalsIgnoreCase("00/00/0000 00:00:00")) {
            holder.pickup_date.setVisibility(View.GONE);
        }

        holder.TxtPosName.setText(filterData.get(position).get(
                "POSName"));
        holder.showcomments.setText("Your feedback : " + filterData.get(position).get(
                "Feedback"));

        holder.TxtPosAddress.setText(filterData.get(position).get(
                "POSAddress"));
        holder.TxtDate.setText("Order Date : " + filterData.get(position).get(
                "IndentDate"));

        holder.TxtQty.setText("Total Qty : " + filterData.get(position).get(
                "TotalQTY") + " Items");

        holder.TxtAmount.setText("₹ " + filterData.get(position).get(
                "NetPayable"));

        holder.TxtOrderId.setText("Order Id : " + filterData.get(position).get(
                "IndentNo"));

        holder.RatingBar.setRating(Float.parseFloat(filterData.get(position).get(
                "Rating")));
        holder.Lyorder_details.setOnClickListener(new View.OnClickListener() {
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

        holder.TxtStatus.setText("Order Status : " + filterData.get(position).get(
                "IndentStatusDesc"));

        holder.PaymentMode.setText("Payment Mode : " + filterData.get(position).get(
                "PaymentMode"));

       /* TotalReviews == 0 Show Review button
          TotalReviews > 0 Show Rating (Star)
          IndentStatus - 0 - Show Cancel button*/

        if (filterData.get(position).get(
                "TotalReviews").equalsIgnoreCase("0")) {
            holder.BtnFeedback.setVisibility(View.VISIBLE);
        } else {
            holder.LyRating.setVisibility(View.VISIBLE);
            // holder.LyButton.setVisibility(View.GONE);
            holder.BtnCancel.setVisibility(View.INVISIBLE);
            holder.BtnFeedback.setVisibility(View.INVISIBLE);
            holder.LyRating.setVisibility(View.VISIBLE);
        }

        if (filterData.get(position).get(
                "IndentStatus").equalsIgnoreCase("0")) {
            holder.BtnCancel.setVisibility(View.VISIBLE);
        } else {
            holder.BtnCancel.setVisibility(View.GONE);
        }

        holder.BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return vi;
    }

    public static class ViewHolder {
        TextView TxtPosName, TxtPosAddress, TxtAmount, TxtDate, TxtQty, TxtStatus, TxtOrderId;
        TextView BtnView, BtnCancel, BtnFeedback, pickup_date, delivery_time, showcomments, PaymentMode;
        LinearLayout LyRating, LyButton, Lyorder_details;
        RatingBar RatingBar;
    }
}


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

public class ReviewAdaptor extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    public ReviewAdaptor(Activity context, List<Map<String, String>> listCollectionone) {

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
            vi = inflater.inflate(R.layout.review_adapter, null);
            holder = new ViewHolder();
            holder.name = vi.findViewById(R.id.name);
            holder.rating = vi.findViewById(R.id.rating);
            holder.reviewdate = vi.findViewById(R.id.reviewdate);
            holder.comment = vi.findViewById(R.id.comment);
            holder.title = vi.findViewById(R.id.title);
            holder.ReviewText = vi.findViewById(R.id.ReviewText);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.name.setText(filterData.get(position).get(
                "ConsumerName"));
        holder.rating.setText(filterData.get(position).get(
                "Rating"));
        holder.reviewdate.setText(filterData.get(position).get(
                "ReviewDT"));
        holder.ReviewText.setText(filterData.get(position).get(
                "ReviewText"));
        holder.comment.setText("Replied :" + filterData.get(position).get(
                "ReplyText"));
        holder.title.setText(filterData.get(position).get(
                "Title"));

        return vi;

    }

    public static class ViewHolder {
        TextView name, rating, reviewdate, comment, title, ReviewText;

    }
}


package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.gieasesales.R;
import com.gieasesales.interfaces.clickInterface;

import java.util.List;
import java.util.Map;


public class ShowTitleAdapter extends RecyclerView.Adapter<ShowTitleAdapter.ViewHolder> {

    static LayoutInflater inflater = null;
    Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;
    clickInterface listeners;
    int row_index=0;

    public ShowTitleAdapter(Activity context, List<Map<String, String>> listCollectionone, clickInterface listener) {
        activity = context;
        filterData = listCollectionone;
        listeners = listener;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setFadeAnimation(holder.itemView);
        holder.BtnTitle.setText(filterData.get(position).get("CategoryName"));
        holder.BtnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeners.recyclerviewOnClick(filterData.get(position));
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if (row_index==position) {
            holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.white));
           // holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.dark_gray));
          //  holder.BtnTitle.setBackgroundResource(R.drawable.tab_btn);
            holder.BtnTitle.setBackgroundResource(R.drawable.tab_btn_orange);

        } else {
            holder.BtnTitle.setBackgroundResource(R.drawable.tab_btn);
            holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.hint_gray));
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView BtnTitle;

        private ViewHolder(View vi) {
            super(vi);
            BtnTitle = vi.findViewById(R.id.title);

        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}

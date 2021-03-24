package com.gieasesales.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gieasesales.R;
import com.gieasesales.interfaces.clickInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class ShowCatagoryAdapter extends RecyclerView.Adapter<ShowCatagoryAdapter.ViewHolder> {

    static LayoutInflater inflater = null;
    Activity activity;
    ProgressDialog progressDialog;
    List<Map<String, String>> filterData;

    int row_index = 0;

    public ShowCatagoryAdapter(Activity context, List<Map<String, String>> listCollectionone) {
        activity = context;
        filterData = listCollectionone;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.roundtitle_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setFadeAnimation(holder.itemView);
        holder.BtnTitle.setText(filterData.get(position).get("ProductTypeDesc"));

        //holder.img.setText(filterData.get(position).get("ProductTypeDesc"));
        try {
            Glide.with(activity).load(new URL(filterData.get(position).get("ImagePath"))).into(holder.img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.RL_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listeners.recyclerviewOnClick(filterData.get(position));
                progressDialog.show();
                row_index = position;
                notifyDataSetChanged();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        progressDialog.dismiss();
                    }
                }, 200);
            }
        });

        if (row_index == position) {
            //    holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.white));
            //    holder.BtnTitle.setBackgroundResource(R.drawable.tab_btn_orange);
            holder.RL_layout.setBackgroundResource(R.drawable.circle_border_red);
            holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        } else {
            holder.RL_layout.setBackgroundResource(R.drawable.circle_border_grey);
            holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.button_gray));
            //     holder.BtnTitle.setBackgroundResource(R.drawable.tab_btn);
            //   holder.BtnTitle.setTextColor(activity.getResources().getColor(R.color.hint_gray));
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
        RelativeLayout RL_layout;
        ImageView img;

        private ViewHolder(View vi) {
            super(vi);
            BtnTitle = vi.findViewById(R.id.title);
            RL_layout = vi.findViewById(R.id.RL_layout);
            img = vi.findViewById(R.id.img);

        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}

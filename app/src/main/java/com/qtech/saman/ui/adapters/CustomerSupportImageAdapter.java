package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.apis.CustomerSupportListApi;
import com.qtech.saman.ui.activities.CustomerSupport.SupportDetailsActivity;
import com.qtech.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerSupportImageAdapter extends RecyclerView.Adapter<CustomerSupportImageAdapter.ViewHolder> {

    private Context mContext;
    LayoutInflater inflater;
    ArrayList<String> imagelist;

    public CustomerSupportImageAdapter(Context mContext, ArrayList<String> imagelist) {
        this.mContext = mContext;
        this.imagelist = imagelist;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.imageView_delete.setVisibility(View.GONE);
        Picasso.get().load(Constants.URLS.BaseURLImages + imagelist.get(position))
                .fit()
                .into(holder.iv_photos);
    }

    @Override
    public int getItemCount() {
        return imagelist == null ? 0 : imagelist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView iv_photos;
        @BindView(R.id.imageView_delete)
        ImageView imageView_delete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

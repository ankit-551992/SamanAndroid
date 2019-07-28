package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.OrderTrack;
import com.qtech.saman.utils.SamanApp;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By apple on 2019-05-29
 */
public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.RowViewHolder> {

    HashMap<Integer, List<OrderTrack>> hashMap;
    private Context mContext;
    List<Integer> keys;

    public TrackOrderAdapter(Context mContext, HashMap<Integer, List<OrderTrack>> hashMap) {
        this.hashMap = hashMap;
        this.mContext = mContext;
    }

    public void setKeys(List<Integer> keys) {
        this.keys = keys;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_track_row_new, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        List<OrderTrack> orderTrackList = hashMap.get(keys.get(position));
        if (orderTrackList != null) {
            if (SamanApp.isEnglishVersion) {
                holder.productNameTextView.setText(orderTrackList.get(0).getProductName());
            } else {
                holder.productNameTextView.setText(orderTrackList.get(0).getProductNameAR());
            }




            String status = "";
            Collections.sort(orderTrackList, new Comparator<OrderTrack>() {
                @Override
                public int compare(OrderTrack lhs, OrderTrack rhs) {
                    return getDate(rhs.getDate()).compareTo(getDate(lhs.getDate()));
                }
            });

            RecyclerView.LayoutManager layoutManager;
            TrackingAdapter trackingAdapter;
            layoutManager = new LinearLayoutManager(mContext);
            holder.trackRecyclerView.setLayoutManager(layoutManager);
            holder.trackRecyclerView.setNestedScrollingEnabled(false);
            trackingAdapter = new TrackingAdapter(mContext, orderTrackList);
            holder.trackRecyclerView.setAdapter(trackingAdapter);

//            for (int i = 0; i < orderTrackList.size(); i++) {
//
//                Long dateTimeStamp = Long.parseLong(orderTrackList.get(i).getDate().replaceAll("\\D", ""));
//                Date date = new Date(dateTimeStamp);
//                DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a", Locale.ENGLISH);
//                String dateFormatted = formatter.format(date);
//
//                if (status.isEmpty()) {
//                    status = getStatus(orderTrackList.get(i).getStatus()) + " - " + dateFormatted.toString() + "\n\n";
//                } else {
//                    status = status + getStatus(orderTrackList.get(i).getStatus()) + " - " + dateFormatted.toString() + "\n\n";
//                }
//            }
//            holder.statusTextView.setText(status);

        }
    }

    private Date getDate(String date) {
        return new Date(Long.parseLong(date.replaceAll("\\D", "")));
    }

    private String getStatus(int s) {
        switch (s) {
            case 1:
                return mContext.getString(R.string.pending);
            case 2:
                return mContext.getString(R.string.processing);
            case 3:
                return mContext.getString(R.string.shipped);
            case 4:
                return mContext.getString(R.string.complete);
            case 5:
                return mContext.getString(R.string.canceled);
            case 6:
                return mContext.getString(R.string.refunded);
            case 7:
                return mContext.getString(R.string.reversed);
            case 8:
                return mContext.getString(R.string.cancelled_by_customer);
            default:
                return mContext.getString(R.string.pending);
        }
    }

    @Override
    public int getItemCount() {
        return hashMap == null ? 0 : hashMap.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView productNameTextView;
        @BindView(R.id.tv_status)
        TextView statusTextView;
        @BindView(R.id.recyclerView)
        RecyclerView trackRecyclerView;
        RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

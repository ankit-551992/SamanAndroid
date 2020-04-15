package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.OrderTrack;
import com.qtech.saman.utils.SamanApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.RowViewHolder> {

    private List<OrderTrack> orderTrackArrayList;
    private Context mContext;

    public TrackingAdapter(Context mContext, List<OrderTrack> orderTrackArrayList) {
        this.orderTrackArrayList = orderTrackArrayList;
        this.mContext = mContext;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_track_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        OrderTrack orderTrack = orderTrackArrayList.get(position);
        Log.e("ORDERLIST00", "-orderTrackArrayList---" + orderTrackArrayList.toString());

        if (SamanApp.isEnglishVersion) {
            if (orderTrack.getComment() != null && orderTrack.getComment() != null && !orderTrack.getComment().isEmpty()) {
                holder.messageTextView.setText(orderTrack.getComment());
            } else {
                holder.messageTextView.setVisibility(View.GONE);
            }
            holder.statusTextView.setText(orderTrack.getProductName() + " - " + getStatus(orderTrack.getStatus()));

        } else {
            if (orderTrack.getComment() != null && orderTrack.getCommentAR() != null && !orderTrack.getCommentAR().isEmpty()) {
                holder.messageTextView.setText(orderTrack.getCommentAR());
            } else {
                holder.messageTextView.setVisibility(View.GONE);
            }
            holder.statusTextView.setText(orderTrack.getProductNameAR() + " - " + getStatus(orderTrack.getStatus()));
        }

        Long dateTimeStamp = Long.parseLong(orderTrack.getDate().replaceAll("\\D", ""));
//        Long dateTimeStamp = Long.parseLong("1548406244312");
        Date date = new Date(dateTimeStamp);
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a");
        String dateFormatted = formatter.format(date);
        holder.dateTextView.setText(dateFormatted.toString());
        holder.statusTextView.setText(getStatus(orderTrack.getStatus()) + " - " + dateFormatted.toString());

        if (position == orderTrackArrayList.size() - 1) {
            holder.view.setVisibility(View.INVISIBLE);
        }

//        if (position == 0) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.matched_circle));
//            } else {
//                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.matched_circle));
//            }
//        }
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
            default:
                return mContext.getString(R.string.pending);
        }
    }


    @Override
    public int getItemCount() {
        return orderTrackArrayList == null ? 0 : orderTrackArrayList.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_textView)
        TextView messageTextView;
        @BindView(R.id.status_textView)
        TextView statusTextView;
        @BindView(R.id.date_textView)
        TextView dateTextView;
        @BindView(R.id.iv_status)
        ImageView imageView;
        @BindView(R.id.view)
        View view;

        RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

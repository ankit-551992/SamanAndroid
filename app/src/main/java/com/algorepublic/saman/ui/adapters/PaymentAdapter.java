package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.ui.fragments.store.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Payment> paymentList = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;



    public PaymentAdapter(Context mContext,List<Payment> paymentList){
        this.paymentList=paymentList;
        this.mContext=mContext;
    }


    public void removeItem(int position) {
        paymentList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, paymentList.size());
    }
    public void restoreItem(Payment payment, int position) {
        paymentList.add(position, payment);
        notifyItemInserted(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return paymentList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_payment_row, parent, false);

            return new PaymentViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PaymentViewHolder) {
            PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return paymentList == null ? 0 : paymentList.size();
    }



    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName;
        public PaymentViewHolder(View v) {
            super(v);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }
}

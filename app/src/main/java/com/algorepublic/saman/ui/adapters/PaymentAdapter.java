package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.ui.fragments.store.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<CardDs> cardDsList = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;



    public PaymentAdapter(Context mContext,List<CardDs> cardDsList){
        this.cardDsList=cardDsList;
        this.mContext=mContext;
    }


    public void removeItem(int position) {
        cardDsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardDsList.size());
    }
    public void restoreItem(CardDs cardDs, int position) {
        cardDsList.add(position, cardDs);
        notifyItemInserted(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return cardDsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
            paymentViewHolder.cardHolderName.setText(cardDsList.get(position).getCardHolder());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return cardDsList == null ? 0 : cardDsList.size();
    }



    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_holder_name)
        TextView cardHolderName;
        public PaymentViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
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

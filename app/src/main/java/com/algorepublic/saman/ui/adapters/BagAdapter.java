package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Product;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Product> productArrayList;
    private Context mContext;

    public BagAdapter(Context mContext,List<Product> productArrayList){
        this.productArrayList=productArrayList;
        this.mContext=mContext;
    }

//    public void removeItem(int position) {
//        productArrayList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, productArrayList.size());
//    }
//    public void restoreItem(Product product, int position) {
//        productArrayList.add(position, product);
//        notifyItemInserted(position);
//    }

    @Override
    public int getItemViewType(int position) {
        return productArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bag_row, parent, false);

            return new BagViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BagViewHolder) {
            BagViewHolder bagViewHolder = (BagViewHolder) holder;
            bagViewHolder.getPosition = holder.getAdapterPosition();
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList == null ? 0 : productArrayList.size();
    }


    class BagViewHolder extends RecyclerView.ViewHolder {

        int getPosition;
        @BindView(R.id.tv_quantity) TextView quantity;

        BagViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.iv_add_quantity)
        void addItem() {
            int current=Integer.parseInt(quantity.getText().toString());
            current++;
            quantity.setText(String.valueOf(current));
        }

        @OnClick(R.id.iv_remove_quantity)
        void removeItem() {
            int current=Integer.parseInt(quantity.getText().toString());
            if(current>1) {
                current--;
            }
            quantity.setText(String.valueOf(current));
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.native_progress_bar) ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
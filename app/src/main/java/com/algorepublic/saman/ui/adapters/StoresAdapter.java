package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.ui.activities.search.SearchActivity;
import com.algorepublic.saman.ui.fragments.store.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class StoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Store> storeArrayList = new ArrayList<>();
    private Context mContext;

    public StoresAdapter(Context mContext,List<Store> storeArrayList){
        this.storeArrayList=storeArrayList;
        this.mContext=mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return storeArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_row, parent, false);

            return new StoreViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StoreViewHolder) {
            StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
            storeViewHolder.storeName.setText("Store Name"+position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent intent=new Intent(mContext, SearchActivity.class);
                        intent.putExtra("Function",2); //2 for Store Products
                        intent.putExtra("StoreName","Store Name"+position);
                        intent.putExtra("StoreNameAr","Store Name"+position);
                        intent.putExtra("StoreID",storeArrayList.get(position).getId());
                        mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return storeArrayList == null ? 0 : storeArrayList.size();
    }



    static class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName;
        private ImageView storeImage;

        public StoreViewHolder(View v) {
            super(v);

            storeImage = (ImageView) v.findViewById(R.id.iv_store_image);
            storeName = (TextView) v.findViewById(R.id.tv_store_name);
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

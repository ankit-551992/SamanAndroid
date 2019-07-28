package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.Store;
import com.qtech.saman.ui.activities.store.StoreDetailActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

            if(SamanApp.isEnglishVersion) {
                storeViewHolder.storeName.setText(storeArrayList.get(position).getStoreName());
            }else {
                storeViewHolder.storeName.setText(storeArrayList.get(position).getStoreNameAR());
            }

            String url=Constants.URLS.BaseURLImages +storeArrayList.get(position).getLogoURL();
            Picasso.get().load(url)
                    .into(((StoreViewHolder) holder).storeImage);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent intent=new Intent(mContext, StoreDetailActivity.class);
                        intent.putExtra("Function",2); //2 for Store Products
                        intent.putExtra("StoreName",storeArrayList.get(position).getStoreName());
                        intent.putExtra("StoreNameAr",storeArrayList.get(position).getStoreNameAR());
                        intent.putExtra("StoreID",storeArrayList.get(position).getID());
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

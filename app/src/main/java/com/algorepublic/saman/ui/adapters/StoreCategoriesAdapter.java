package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.StoreCategory;
import com.algorepublic.saman.ui.activities.search.ProductListingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<StoreCategory> categories = new ArrayList<>();
    private Context mContext;

    int storeID=0;
    String storeName="";
    String storeNameAr="";

    public StoreCategoriesAdapter(Context mContext, List<StoreCategory> categories,int storeID,String storeName,String storeNameAr) {
        this.categories = categories;
        this.mContext = mContext;
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeNameAr = storeNameAr;
    }

    @Override
    public int getItemViewType(int position) {
        return categories.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_category_row, parent, false);

            return new CategoryViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CategoryViewHolder) {
            CategoryViewHolder messageViewHolder = (CategoryViewHolder) holder;

            messageViewHolder.categoryName.setText(categories.get(position).getTitle());

            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, ProductListingActivity.class);
                    intent.putExtra("Function",2); //2 for Store Products
                    intent.putExtra("StoreName",storeName);
                    intent.putExtra("categoryID",categories.get(position).getID());
                    intent.putExtra("categoryName",categories.get(position).getTitle());
                    intent.putExtra("categoryNameAr",categories.get(position).getTitleAR());
                    intent.putExtra("StoreNameAr",storeNameAr);
                    intent.putExtra("StoreID",storeID);
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
        return categories == null ? 0 : categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_name)
        TextView categoryName;

        public CategoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }

    public void setNames(String storeName,String storeNameAr){
        this.storeName=storeName;
        this.storeNameAr=storeNameAr;
    }
}

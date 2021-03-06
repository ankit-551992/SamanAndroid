package com.qtech.saman.ui.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckOutProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> productArrayList = new ArrayList<>();
    private Context mContext;


    public CheckOutProductAdapter(Context mContext, List<Product> productArrayList) {
        this.productArrayList = productArrayList;
        this.mContext = mContext;
    }


    public void removeItem(int position) {
        productArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productArrayList.size());
    }

    public void restoreItem(Product product, int position) {
        productArrayList.add(position, product);
        notifyItemInserted(position);
    }

    @Override
    public int getItemViewType(int position) {
        return productArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_fav_row, parent, false);

            return new FavoritesViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FavoritesViewHolder) {
            FavoritesViewHolder favoritesViewHolder = (FavoritesViewHolder) holder;
            if (SamanApp.isEnglishVersion) {
//                favoritesViewHolder.name.setText(productArrayList.get(position).getProductName());
                favoritesViewHolder.name.setText(productArrayList.get(position).getProductName() + " x " + productArrayList.get(position).getQuantity());
                favoritesViewHolder.storeName.setText(productArrayList.get(position).getStoreName());
                favoritesViewHolder.price.setText(productArrayList.get(position).getOptions());
            } else {
//                favoritesViewHolder.name.setText(productArrayList.get(position).getProductNameAR());
                favoritesViewHolder.name.setText(productArrayList.get(position).getProductNameAR() + " x " + productArrayList.get(position).getQuantity());
                favoritesViewHolder.storeName.setText(productArrayList.get(position).getStoreNameAR());
                favoritesViewHolder.price.setText(productArrayList.get(position).getOptionsAR());
            }
//            favoritesViewHolder.price.setText(productArrayList.get(position).getPrice()+" OMR");


            if (productArrayList.get(position).getLogoURL() != null && !productArrayList.get(position).getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + productArrayList.get(position).getLogoURL())
                        .into(favoritesViewHolder.productImageView);
            }

//            favoritesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
//                    intent.putExtra("ProductID",productArrayList.get(position).getID());
//                    mContext.startActivity(intent);
//                }
//            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList == null ? 0 : productArrayList.size();
    }


    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView name;
        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.iv_product)
        ImageView productImageView;

        public FavoritesViewHolder(View v) {
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
}

package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.qtech.saman.R;
import com.qtech.saman.data.model.OptionValue;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BagCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> products = new ArrayList<>();
    private Context mContext;


    public BagCartAdapter(Context mContext, List<Product> products) {
        this.products = products;
        this.mContext = mContext;
    }


    @Override
    public int getItemViewType(int position) {
        return products.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_bag_row, parent, false);
            return new BagViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BagViewHolder) {
            BagViewHolder bagViewHolder = (BagViewHolder) holder;
            Product product = products.get(position);
            if (product.getLogoURL() != null && !product.getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + product.getLogoURL())
                        .into(bagViewHolder.storeImage);
            }

            bagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", products.get(position).getID());
                    intent.putExtra("Options", products.get(position).getOptionValues());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private String getOptionsData(Product product1) {
        View v = null;
        OptionValue optionValue = null;
        String ids = "";
        if (product1.getOptionValues() != null) {
            for (int i = 0; i < product1.getProductOptions().size(); i++) {
                optionValue = product1.getProductOptions().get(i).getOptionValues().get(0);
                if (ids.equals("")) {
                    ids = "" + optionValue.getID();
                } else {
                    ids = ids + "," + optionValue.getID();
                }
            }
        }
        return ids;
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }


    static class BagViewHolder extends RecyclerView.ViewHolder {
        private ImageView storeImage;

        public BagViewHolder(View v) {
            super(v);
            storeImage = (ImageView) v.findViewById(R.id.iv_image);
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

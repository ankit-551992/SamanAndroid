package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> brandArrayList;
    private int userID;
    private Context mContext;

    public BrandsAdapter(Context mContext,List<Product> brandArrayList,int userID){
        this.brandArrayList=brandArrayList;
        this.mContext=mContext;
        this.userID=userID;
    }

    @Override
    public int getItemViewType(int position) {
        return brandArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_brand_row, parent, false);

            return new BrandViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BrandViewHolder) {
            final BrandViewHolder brandViewHolder = (BrandViewHolder) holder;

            Product product=brandArrayList.get(position);
            brandViewHolder.productDescription.setText(product.getProductName());
            brandViewHolder.productPrice.setText(product.getPrice()+" OMR");

            if(product.getLogoURL()!=null && !product.getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + product.getLogoURL())
                        .placeholder(R.drawable.dummy_mobile)
                        .error(R.drawable.dummy_mobile)
                        .into(brandViewHolder.productImageView);
            }


            brandViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID",brandArrayList.get(position).getID());
                    mContext.startActivity(intent);
                }
            });

            if(brandArrayList.get(position).getFavorite()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    brandViewHolder.favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.fav));
                }else {
                    brandViewHolder.favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav));
                }
            }

            brandViewHolder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(brandArrayList.get(position).getFavorite()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            brandViewHolder.favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_b));
                        }else {
                            brandViewHolder.favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_b));
                        }
                        GlobalValues.markUnFavourite(userID, brandArrayList.get(position).getID());
                        brandArrayList.get(position).setFavorite(false);
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            brandViewHolder.favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.fav));
                        }else {
                            brandViewHolder.favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav));
                        }
                        GlobalValues.markFavourite(userID, brandArrayList.get(position).getID());
                        brandArrayList.get(position).setFavorite(true);
                    }
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return brandArrayList == null ? 0 : brandArrayList.size();
    }



    static class BrandViewHolder extends RecyclerView.ViewHolder {
        private TextView productDescription;
        private TextView productPrice;
        private ImageView productImageView;
        private ImageView favoriteImageView;
        private ImageView cartImageView;

        public BrandViewHolder(View v) {
            super(v);
            productImageView = (ImageView) v.findViewById(R.id.iv_product);
            productDescription = (TextView) v.findViewById(R.id.tv_product_description);
            productPrice = (TextView) v.findViewById(R.id.tv_product_price);
            favoriteImageView = (ImageView) v.findViewById(R.id.iv_favorite);
            cartImageView = (ImageView) v.findViewById(R.id.iv_add_to_cart);
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

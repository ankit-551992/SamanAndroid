package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.ui.fragments.bag.BagFragment;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Product> productArrayList;
    private Context mContext;
    private BagFragment bagFragment;
    float grandTotal = 0;

    public BagAdapter(Context mContext, List<Product> productArrayList, BagFragment bagFragment) {
        this.productArrayList = productArrayList;
        this.mContext = mContext;
        this.bagFragment = bagFragment;

        for (int i=0;i<productArrayList.size();i++){
            this.productArrayList.get(i).setAvailableQuantity(productArrayList.get(i).getQuantity());
        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof BagViewHolder) {
            BagViewHolder bagViewHolder = (BagViewHolder) holder;
            bagViewHolder.getPosition = holder.getAdapterPosition();
            Product product = productArrayList.get(bagViewHolder.getPosition);
            bagViewHolder.name.setText(product.getProductName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bagViewHolder.description.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                bagViewHolder.description.setText(Html.fromHtml(product.getDescription()));
            }

            if(product.getLogoURL()!=null && !product.getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + product.getLogoURL())
                        .into(bagViewHolder.productImageView);
            }

            bagViewHolder.price.setText(product.getPrice() + " OMR");
            float total = product.getPrice() * product.getQuantity();
            grandTotal = grandTotal + total;
            bagViewHolder.total.setText(total + " OMR");
            bagViewHolder.quantity.setText(String.valueOf(product.getQuantity()));

            bagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID",productArrayList.get(position).getID());
                    mContext.startActivity(intent);
                }
            });
            bagFragment.updateTotal(grandTotal, 0);

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
        @BindView(R.id.tv_quantity)
        TextView quantity;
        @BindView(R.id.tv_product_name)
        TextView name;
        @BindView(R.id.tv_product_description)
        TextView description;
        @BindView(R.id.tv_product_price)
        TextView price;
        @BindView(R.id.tv_product_total)
        TextView total;
        @BindView(R.id.iv_product)
        ImageView productImageView;

        BagViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.iv_add_quantity)
        void addItem() {
            SamanApp.localDB.addToCart(
                    productArrayList.get(getPosition),
                    productArrayList.get(getPosition).getOptionValues(),
                    productArrayList.get(getPosition).getOptions(),
                    productArrayList.get(getPosition).getOptions(),
                    1);

            productArrayList.get(getPosition).setQuantity(productArrayList.get(getPosition).getQuantity() + 1);
            updateNotify();

            int current = Integer.parseInt(quantity.getText().toString());

//            if (productArrayList.get(getPosition).getAvailableQuantity() > current) {
//                SamanApp.localDB.addToCart(
//                        productArrayList.get(getPosition),
//                        productArrayList.get(getPosition).getOptionValues(),
//                        productArrayList.get(getPosition).getOptions(),
//                        productArrayList.get(getPosition).getOptions(),
//                        1);
//
//
//                productArrayList.get(getPosition).setQuantity(productArrayList.get(getPosition).getQuantity() + 1);
//                updateNotify();
//            } else {
//                String text = String.format(mContext.getString(R.string.items_available_count), productArrayList.get(getPosition).getAvailableQuantity());
//                Constants.showAlert(mContext.getString(R.string.title_my_bag),
//                        text,
//                        mContext.getString(R.string.cancel),
//                        mContext);
//            }
        }

        @OnClick(R.id.iv_remove_quantity)
        void removeItem() {
            int current = Integer.parseInt(quantity.getText().toString());
            if (current > 1) {
                current--;

                SamanApp.localDB.addToCart(
                        productArrayList.get(getPosition),
                        productArrayList.get(getPosition).getOptionValues(),
                        productArrayList.get(getPosition).getOptions(),
                        productArrayList.get(getPosition).getOptions(),
                        -1);
                productArrayList.get(getPosition).setQuantity(productArrayList.get(getPosition).getQuantity() - 1);
            }
            updateNotify();
        }
    }


    public void updateNotify() {
        grandTotal = 0;
        notifyDataSetChanged();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.native_progress_bar)
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
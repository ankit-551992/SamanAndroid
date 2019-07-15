package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.OrderItem;
import com.algorepublic.saman.data.model.ProductOption;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.RowViewHolder> {

    private List<OrderItem> orderItems;
    private Context mContext;

    public InvoiceAdapter(Context mContext, List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.mContext = mContext;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, final int position) {
        OrderItem orderItem = orderItems.get(position);

        if(orderItem.getProduct()!=null) {
            if(SamanApp.isEnglishVersion) {
                holder.productName.setText(orderItem.getProduct().getProductName()+" x "+orderItem.getProductQuantity());
                holder.storeName.setText(orderItem.getProduct().getStoreName());
            }else {
                holder.productName.setText(orderItem.getProduct().getProductNameAR()+" x "+orderItem.getProductQuantity());
                holder.storeName.setText(orderItem.getProduct().getStoreNameAR());
            }
            String options="";
            if(orderItem.getProductOptionList()!=null) {
                for (int i = 0; i < orderItem.getProductOptionList().size(); i++) {
                    ProductOption productOption = orderItem.getProductOptionList().get(i);
                    if (SamanApp.isEnglishVersion) {
                        if (options.equals("")) {
                            options = productOption.getOptionValue().getTitle();
                        } else {
                            options = options + "," + productOption.getOptionValue().getTitle();
                        }
                    } else {
                        if (options.equals("")) {
                            options = productOption.getOptionValue().getTitleAR();
                        } else {
                            options = options + "," + productOption.getOptionValue().getTitleAR();
                        }
                    }
                }
            }
            holder.productPrice.setText(options);
            if(orderItem.getStatus()!=null) {
                holder.productStatus.setText(orderItem.getStatus());
            }
//            holder.productPrice.setText(orderItem.getProduct().getPrice()+ " OMR x "+orderItem.getProduct().getQuantity());
            if(orderItem.getProduct().getLogoURL()!=null && !orderItem.getProduct().getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + orderItem.getProduct().getLogoURL())
                        .into(holder.productImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderItems == null ? 0 : orderItems.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView productName;
        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_product_price)
        TextView productPrice;
        @BindView(R.id.tv_product_status)
        TextView productStatus;
        @BindView(R.id.iv_product)
        ImageView productImageView;
        RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

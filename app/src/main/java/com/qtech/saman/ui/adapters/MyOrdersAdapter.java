package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qtech.saman.R;
import com.qtech.saman.data.model.OrderHistory;
import com.qtech.saman.ui.activities.invoice.InvoiceActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private List<OrderHistory> orderHistoryList;
    private Context mContext;
    LayoutInflater inflater;

    public MyOrdersAdapter(Context mContext, List<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
        this.mContext = mContext;
        inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_order_header, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        Long datetimestamp = Long.parseLong(orderHistory.getCreatedAt().replaceAll("\\D", ""));
        Date date = new Date(datetimestamp);
//        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy G 'at' HH:mm:ss z");
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a",Locale.ENGLISH);
        String dateFormatted = formatter.format(date);
        holder.dateTextView.setText(dateFormatted.toString());
        holder.orderNUmberTextView.setText(orderHistory.getOrderNumber());
//        for (int i =0; i<orderHistory.getOrderItems().size(); i++) {
//            OrderItem orderItem = orderHistory.getOrderItems().get(i);
//            View child = inflater.inflate(R.layout.item_my_order_row, null);
//            TextView productName = (TextView) child.findViewById(R.id.tv_product_name);
//            TextView productPrice = (TextView) child.findViewById(R.id.tv_product_price);
//            ImageView productImageView = (ImageView) child.findViewById(R.id.iv_product);
//
//            if(orderItem.getProduct()!=null) {
//                productName.setText(orderItem.getProduct().getProductName());
//                productPrice.setText(orderItem.getProduct().getPrice()+ " OMR");
//
//                if(orderItem.getProduct().getLogoURL()!=null && !orderItem.getProduct().getLogoURL().isEmpty()) {
//                    Picasso.get().load(Constants.URLS.BaseURLImages + orderItem.getProduct().getLogoURL())
//                            .into(productImageView);
//                }
//                holder.orderItemsLayout.addView(child);
//            }
//        }

        holder.orderItemsLayout.setVisibility(View.GONE);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(holder.orderItemsLayout.getVisibility()==View.VISIBLE){
//                    holder.orderItemsLayout.setVisibility(View.GONE);
//                }else {
//                    holder.orderItemsLayout.setVisibility(View.VISIBLE);
//                }
                Intent intent=new Intent(mContext,InvoiceActivity.class);
                intent.putExtra("Obj",orderHistoryList.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryList == null ? 0 : orderHistoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_date)
        TextView dateTextView;
        @BindView(R.id.tv_order_number)
        TextView orderNUmberTextView;
        @BindView(R.id.order_items_layout)
        LinearLayout orderItemsLayout;
        @BindView(R.id.relative_layout)
        RelativeLayout relativeLayout;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

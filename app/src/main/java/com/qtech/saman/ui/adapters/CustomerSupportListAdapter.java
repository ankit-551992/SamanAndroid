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
import com.qtech.saman.data.model.apis.CustomerSupportListApi;
import com.qtech.saman.ui.activities.CustomerSupport.SupportDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerSupportListAdapter extends RecyclerView.Adapter<CustomerSupportListAdapter.ViewHolder> {

    private List<CustomerSupportListApi.Result> customerSupportList;
    private Context mContext;
    LayoutInflater inflater;

    public CustomerSupportListAdapter(Context mContext, List<CustomerSupportListApi.Result> customerSupportList) {
        this.mContext = mContext;
        this.customerSupportList = customerSupportList;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        CustomerSupportListApi.Result customerHistory = customerSupportList.get(position);

        holder.tv_order.setText(mContext.getResources().getString(R.string.customer_id));
        holder.tv_order_d.setText(mContext.getResources().getString(R.string.customer_subject));

        holder.orderNUmberTextView.setText(customerHistory.getID());
        holder.dateTextView.setText(customerHistory.getSubject());

//        holder.orderItemsLayout.setVisibility(View.GONE);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, SupportDetailsActivity.class);
                intent.putExtra("UserSupportDetail", customerSupportList.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerSupportList == null ? 0 : customerSupportList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order)
        TextView tv_order;
        @BindView(R.id.tv_order_d)
        TextView tv_order_d;
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

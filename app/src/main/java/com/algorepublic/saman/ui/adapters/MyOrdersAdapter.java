package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.ui.activities.myaccount.myorders.DateItem;
import com.algorepublic.saman.ui.activities.myaccount.myorders.GeneralItem;
import com.algorepublic.saman.ui.activities.myaccount.myorders.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<ListItem> consolidatedList = new ArrayList<>();

    public MyOrdersAdapter(Context context, List<ListItem> consolidatedList) {
        this.consolidatedList = consolidatedList;
        this.mContext = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.item_my_order_row, parent,
                        false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case ListItem.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.item_my_order_header, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case ListItem.TYPE_GENERAL:

                GeneralItem generalItem   = (GeneralItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) viewHolder;
                generalViewHolder.txtTitle.setText(generalItem.getOrder().getName());
                generalViewHolder.textViewSubDescription.setText(generalItem.getOrder().getDate());

                break;

            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;

                dateViewHolder.txtTitle.setText(dateItem.getDate());

                break;
        }
    }



    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle;

        public DateViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.tv_order_date);

        }
    }


    class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle;
        protected TextView textViewSubDescription;

        public GeneralViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.tv_product_description);
            textViewSubDescription = (TextView) v.findViewById(R.id.tv_sub_description);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }
}

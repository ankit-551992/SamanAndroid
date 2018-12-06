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
import com.algorepublic.saman.data.model.Conversation;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.ui.activities.myaccount.messages.MessagingActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.fragments.store.OnLoadMoreListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Conversation> messages = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;

    public ConversationsAdapter(Context mContext, List<Conversation> messages){
        this.messages=messages;
        this.mContext=mContext;
    }


    @Override
    public int getItemViewType(int position) {
        return messages.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_messages_row, parent, false);
            return new ConversationViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConversationViewHolder) {
            ConversationViewHolder conversationViewHolder = (ConversationViewHolder) holder;
            final Conversation conversation=messages.get(position);
            conversationViewHolder.storeName.setText(conversation.getStoreName());
            conversationViewHolder.productName.setText(conversation.getProductName());

            Long datetimestamp = Long.parseLong(conversation.getCreatedAt().replaceAll("\\D", ""));
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("EEEE, d MMM, yyyy 'at' HH:mm a");
            String dateFormatted = formatter.format(date);
            conversationViewHolder.dateTime.setText(dateFormatted.toString());

            conversationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, MessagingActivity.class);
                    intent.putExtra("ConversationID",conversation.getID());
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
        return messages == null ? 0 : messages.size();
    }



    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_product_name)
        TextView productName;
        @BindView(R.id.tv_date_time)
        TextView dateTime;

        public ConversationViewHolder(View v) {
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

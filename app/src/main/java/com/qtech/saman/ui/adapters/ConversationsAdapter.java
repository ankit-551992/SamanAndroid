package com.qtech.saman.ui.adapters;

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

import com.qtech.saman.R;
import com.qtech.saman.data.model.Conversation;
import com.qtech.saman.data.model.Message;
import com.qtech.saman.data.model.User;
import com.qtech.saman.ui.activities.myaccount.messages.MessagingActivity;
import com.qtech.saman.ui.fragments.store.OnLoadMoreListener;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Conversation> messages = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;
    User authenticatedUser;

    public ConversationsAdapter(Context mContext, List<Conversation> messages) {
        this.messages = messages;
        this.mContext = mContext;
        authenticatedUser = GlobalValues.getUser(this.mContext);
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
            final Conversation conversation = messages.get(position);

            int unreadCount = 0;

            for (int i = 0; i < conversation.getMessages().size(); i++) {
                Message message = conversation.getMessages().get(i);
                if (!message.getIsRead() && message.getSender().getId() != authenticatedUser.getId()) {
                    unreadCount++;
                }
            }

            if (unreadCount == 0) {
                conversationViewHolder.unreadMessagesCount.setVisibility(View.GONE);
            } else {
                conversationViewHolder.unreadMessagesCount.setVisibility(View.VISIBLE);
                conversationViewHolder.unreadMessagesCount.setText("" + unreadCount);
            }

            if (SamanApp.isEnglishVersion) {
                conversationViewHolder.storeName.setText(conversation.getTitle());
                if (conversation.getProductName() != null) {
                    if (!conversation.getProductName().equals("")) {
                        conversationViewHolder.productName.setText(conversation.getProductName());
                    } else {
                        conversationViewHolder.productName.setVisibility(View.GONE);
                    }
                } else {
                    conversationViewHolder.productName.setVisibility(View.GONE);
                }
            } else {
                conversationViewHolder.storeName.setText(conversation.getTitleAr());
                if (conversation.getProductName() != null) {
                    if (!conversation.getProductName().equals("")) {
                        conversationViewHolder.productName.setText(conversation.getProductNameAR());
                    } else {
                        conversationViewHolder.productName.setVisibility(View.GONE);
                    }
                } else {
                    conversationViewHolder.productName.setVisibility(View.GONE);
                }
            }

            Long datetimestamp = Long.parseLong(conversation.getUpdatedAt().replaceAll("\\D", ""));
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            conversationViewHolder.dateTime.setText(dateFormatted.toString());


            if (!conversation.getImage().equals("")) {
                Picasso.get().load(Constants.URLS.BaseURLImages + conversation.getImage())
                        .into(conversationViewHolder.imageView);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    conversationViewHolder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.app_icon));
                } else {
                    conversationViewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.app_icon));
                }
            }

            int finalUnreadCount = unreadCount;
            conversationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conversationViewHolder.unreadMessagesCount.setVisibility(View.GONE);
                    Intent intent = new Intent(mContext, MessagingActivity.class);
                    intent.putExtra("ConversationID", conversation.getID());
                    mContext.startActivity(intent);
                    int newCount = GlobalValues.getBadgeCount(mContext) - finalUnreadCount;
                    GlobalValues.setBadgeCount(mContext, newCount);
                    ShortcutBadger.applyCount(mContext, newCount);
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

        @BindView(R.id.tv_unread_message_count)
        TextView unreadMessagesCount;
        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_product_name)
        TextView productName;
        @BindView(R.id.tv_date_time)
        TextView dateTime;
        @BindView(R.id.imageView)
        ImageView imageView;

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

package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_OTHER = 0;
    private final int VIEW_TYPE_SELF = 1;
    List<Message> messages = new ArrayList<>();
    private Context mContext;
    User authenticatedUser;

    public ChatAdapter(Context mContext, List<Message> messages) {
        this.messages = messages;
        this.mContext = mContext;
        authenticatedUser = GlobalValues.getUser(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("SENDER_ID", "----senderId---" + messages.get(position).getSender().getId());
        Log.e("SENDER_ID", "--authenticatedUser--senderId---" + authenticatedUser.getId());

        // if (messages.get(position).getSender().getId() == authenticatedUser.getId()) {
        if (!messages.get(position).getSender().getId().equals(authenticatedUser.getId())) {
            return VIEW_TYPE_OTHER;
        } else {
            return VIEW_TYPE_SELF;
        }
       /*  if (messages.get(position).getSender().getId() == authenticatedUser.getId()) {
            return VIEW_TYPE_SELF;
        } else {
            return VIEW_TYPE_OTHER;
        }*/

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OTHER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_other_row, parent, false);
            return new OtherUserViewHolder(view);
        } else if (viewType == VIEW_TYPE_SELF) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_self_row, parent, false);
            return new SelfUserViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OtherUserViewHolder) {

            OtherUserViewHolder otherUserViewHolder = (OtherUserViewHolder) holder;

            Message message = messages.get(position);

            otherUserViewHolder.messageTextView.setText(message.getMessageBody());

            Long datetimestamp = Long.parseLong(message.getCreatedAt().replaceAll("\\D", ""));
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            otherUserViewHolder.dateTime.setText(dateFormatted.toString());


            if (message.getSender() != null) {

                User sender = message.getSender();

                otherUserViewHolder.userName.setText(sender.getFirstName() + " " + sender.getLastName());

                if (sender.getProfileImagePath() != null
                        && !sender.getProfileImagePath().isEmpty()
                        && !sender.getProfileImagePath().equalsIgnoreCase("path")) {

                    Picasso.get()
                            .load(Constants.URLS.BaseURLImages + sender.getProfileImagePath())
                            .placeholder(R.drawable.ic_profile)
                            .into(otherUserViewHolder.storeImage);

                } else {
                    Picasso.get()
                            .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                            .placeholder(R.drawable.ic_profile)
                            .into(otherUserViewHolder.storeImage);
                }
            }
        } else if (holder instanceof SelfUserViewHolder) {

            SelfUserViewHolder selfUserViewHolder = (SelfUserViewHolder) holder;

            Message message = messages.get(position);

            selfUserViewHolder.messageTextView.setText(message.getMessageBody());

            Long datetimestamp = Long.parseLong(message.getCreatedAt().replaceAll("\\D", ""));
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy 'at' hh:mm a", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            selfUserViewHolder.dateTime.setText(dateFormatted.toString());

            //  selfUserViewHolder.userName.setText(authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName());
            selfUserViewHolder.userName.setText(authenticatedUser.getFirstName());
            if (authenticatedUser.getProfileImagePath() != null && !authenticatedUser.getProfileImagePath().isEmpty() && !authenticatedUser.getProfileImagePath().equalsIgnoreCase("path")) {
                if (authenticatedUser.getSocialID() != 0) {
                    if (!authenticatedUser.getProfileImagePath().isEmpty()) {
                        Picasso.get()
                                .load(authenticatedUser.getProfileImagePath())
                                .placeholder(R.drawable.ic_profile)
                                .into(selfUserViewHolder.storeImage);
                    } else {
                        Picasso.get()
                                .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                                .placeholder(R.drawable.ic_profile)
                                .into(selfUserViewHolder.storeImage);
                    }
                } else {
                    Picasso.get()
                            .load(Constants.URLS.BaseURLImages + authenticatedUser.getProfileImagePath())
                            .placeholder(R.drawable.ic_profile)
                            .into(selfUserViewHolder.storeImage);
                }
            } else {
                Picasso.get()
                        .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                        .placeholder(R.drawable.ic_profile)
                        .into(selfUserViewHolder.storeImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }


    static class OtherUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.iv_store_image)
        ImageView storeImage;
        @BindView(R.id.tv_date_time)
        TextView dateTime;
        @BindView(R.id.tv_userName)
        TextView userName;

        public OtherUserViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class SelfUserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.iv_store_image)
        ImageView storeImage;
        @BindView(R.id.tv_date_time)
        TextView dateTime;
        @BindView(R.id.tv_userName)
        TextView userName;

        public SelfUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

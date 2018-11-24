package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_OTHER = 0;
    private final int VIEW_TYPE_SELF = 1;
    List<Message> messages = new ArrayList<>();
    private Context mContext;

    public ChatAdapter(Context mContext,List<Message> messages){
        this.messages=messages;
        this.mContext=mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2==0){
            return VIEW_TYPE_OTHER;
        }else {
            return VIEW_TYPE_SELF;
        }
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
        } else if (holder instanceof SelfUserViewHolder) {
            SelfUserViewHolder selfUserViewHolder = (SelfUserViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }



    static class OtherUserViewHolder extends RecyclerView.ViewHolder {
        public OtherUserViewHolder(View v) {
            super(v);
        }
    }

    static class SelfUserViewHolder extends RecyclerView.ViewHolder {
        public SelfUserViewHolder(View itemView) {
            super(itemView);
        }
    }
}

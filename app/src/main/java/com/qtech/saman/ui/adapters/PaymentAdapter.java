package com.qtech.saman.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.CardDs;
import com.qtech.saman.ui.activities.myaccount.payment.AddCardActivity;
import com.qtech.saman.ui.fragments.store.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<CardDs> cardDsList = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;

    public PaymentAdapter(Context mContext, List<CardDs> cardDsList) {
        this.cardDsList = cardDsList;
        this.mContext = mContext;
    }

    public void removeItem(int position) {
        cardDsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardDsList.size());
    }

    public void restoreItem(CardDs cardDs, int position) {
        cardDsList.add(position, cardDs);
        notifyItemInserted(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return cardDsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_payment_row, parent, false);
            return new PaymentViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PaymentViewHolder) {
            PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;
            if (position == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getDrawable(R.drawable.cash_delivery));
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                } else {
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash_delivery));
                }
                paymentViewHolder.cardHolderName.setText(mContext.getString(R.string.card_delivery));
                paymentViewHolder.cardExpiry.setVisibility(View.GONE);
                paymentViewHolder.cardNumber.setVisibility(View.GONE);
                paymentViewHolder.edit.setVisibility(View.GONE);
            } else if (position == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_oman_net));
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                } else {
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_oman_net));
                }
                paymentViewHolder.cardHolderName.setText(mContext.getString(R.string.omannet));
                paymentViewHolder.cardExpiry.setVisibility(View.GONE);
                paymentViewHolder.cardNumber.setVisibility(View.GONE);
                paymentViewHolder.edit.setVisibility(View.GONE);
            } else {
                paymentViewHolder.cardHolderName.setText(cardDsList.get(position).getCardHolder());
                paymentViewHolder.cardExpiry.setText(cardDsList.get(position).getExpireDate());
                String cardNumber = String.valueOf(cardDsList.get(position).getCardNumber());
                String cardNumberShow = cardNumber.substring(Math.max(cardNumber.length() - 4, 0));
                paymentViewHolder.cardNumber.setText(mContext.getString(R.string.card_end_number) + " (" + cardNumberShow + ")");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_visa_));
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                } else {
                    paymentViewHolder.cardImageView.getLayoutParams().height = 200;
                    paymentViewHolder.cardImageView.getLayoutParams().width = 200;
                    paymentViewHolder.cardImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_visa_));
                }

                paymentViewHolder.cardExpiry.setVisibility(View.VISIBLE);
                paymentViewHolder.cardNumber.setVisibility(View.VISIBLE);
                paymentViewHolder.edit.setVisibility(View.VISIBLE);

                paymentViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, AddCardActivity.class);
                        intent.putExtra("Obj", cardDsList.get(position));
                        ((Activity) mContext).startActivityForResult(intent, 1010);
                    }
                });
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        Intent data = new Intent();
                        String text = "CASH";
                        data.putExtra("DATA", text);
                        ((Activity) mContext).setResult(RESULT_OK, data);
                        ((Activity) mContext).finish();
                    } else if (position == 1) {
                        Intent data = new Intent();
                        String text = "OMANNET";
                        data.putExtra("DATA", text);
                        ((Activity) mContext).setResult(RESULT_OK, data);
                        ((Activity) mContext).finish();
                    } else {
                        Intent data = new Intent();
                        String text = cardDsList.get(position).getCardNumber();
                        data.putExtra("DATA", text);
                        ((Activity) mContext).setResult(RESULT_OK, data);
                        ((Activity) mContext).finish();
                    }
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    String maskedCardNumber(String cardNumber) {
        int maskLen = cardNumber.length() - 4;
        char[] mask = new char[maskLen];
        Arrays.fill(mask, '*');
        return new String(mask) + cardNumber.substring(maskLen);
    }

    @Override
    public int getItemCount() {
        return cardDsList == null ? 0 : cardDsList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_holder_name)
        TextView cardHolderName;
        @BindView(R.id.tv_card_number)
        TextView cardNumber;
        @BindView(R.id.tv_expiry)
        TextView cardExpiry;
        @BindView(R.id.iv_card)
        ImageView cardImageView;
        @BindView(R.id.iv_edit)
        ImageView edit;

        public PaymentViewHolder(View v) {
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

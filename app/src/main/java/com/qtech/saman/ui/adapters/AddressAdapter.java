package com.qtech.saman.ui.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.qtech.saman.R;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.myaccount.addresses.AddShippingAddressActivity;
import com.qtech.saman.ui.activities.myaccount.addresses.ShippingAddressActivity;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddressAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<ShippingAddress> shippingAddresses = new ArrayList<>();
    private Context mContext;
    Dialog dialog;

    public AddressAdapter(Context mContext, List<ShippingAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return shippingAddresses.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_shipping_address_row, parent, false);

            return new MessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MessageViewHolder) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) holder;

            ShippingAddress address = shippingAddresses.get(position);

            String userAddress = address.getAddressLine1();
            if (address.getFloor() != null) {
                userAddress = userAddress + ", " + address.getFloor();
            }
            if (address.getApt() != null) {
                userAddress = userAddress + ", " + address.getApt();
            }
            if (address.getAddressLine2() != null) {
                userAddress = userAddress + ", " + address.getAddressLine2();
            }
            if (address.getCity() != null) {
                userAddress = userAddress + ", " + address.getCity();
            }
            if (address.getCountry() != null) {
                userAddress = userAddress + ", " + address.getCountry();
            }
            if (address.getRegion() != null) {
                userAddress = userAddress + ", " + address.getRegion();
            }
            messageViewHolder.address.setText(userAddress);

            // Drag From Right
            messageViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, messageViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));

            messageViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    int id = shippingAddresses.get(position).getiD();
//                    String text = shippingAddresses.get(position).getAddressLine1()+","+shippingAddresses.get(position).getCity()+","+shippingAddresses.get(position).getCountry();

                    String text = messageViewHolder.address.getText().toString();
                    data.putExtra("ID", id);
                    data.putExtra("DATA", shippingAddresses.get(position));
//                  data.putExtra("DATA", text);
                    ((Activity) mContext).setResult(RESULT_OK, data);
                    ((Activity) mContext).finish();
                }
            });

            messageViewHolder.editIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_edit_ic));
            messageViewHolder.textView1.setText(mContext.getString(R.string.edit));
            messageViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            messageViewHolder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddShippingAddressActivity.class);
                    intent.putExtra("ShippingAddress", shippingAddresses.get(position));
                    intent.putExtra("Type", 1);
                    mContext.startActivity(intent);
                    mItemManger.closeAllItems();
                }
            });

            messageViewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SamanApp.isEnglishVersion) {
                        showPopUp(mContext.getString(R.string.out_of_stock_title),
                                mContext.getString(R.string.address_msg),
                                mContext.getString(R.string.no),
                                mContext.getString(R.string.yes),
                                1, position);
                    } else {
                        showPopUp("",
                                mContext.getString(R.string.address_msg),
                                mContext.getString(R.string.no),
                                mContext.getString(R.string.yes),
                                1, position);
                    }
                    mItemManger.closeAllItems();
                }
            });
            // mItemManger is member in RecyclerSwipeAdapter Class
            mItemManger.bindView(messageViewHolder.itemView, position);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return shippingAddresses == null ? 0 : shippingAddresses.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;

        @BindView(R.id.layout1)
        LinearLayout layout1;

        @BindView(R.id.layout2)
        LinearLayout layout2;

        @BindView(R.id.iv_icon1)
        ImageView editIcon;

        @BindView(R.id.tv_1)
        TextView textView1;

        @BindView(R.id.tv_address)
        TextView address;

        public MessageViewHolder(View v) {
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


    private void deleteAddress(int Id) {

        WebServicesHandler.instance.deleteAddress(Id, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                SimpleSuccess simpleSuccess = response.body();
                ((ShippingAddressActivity) mContext).call();
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                ((ShippingAddressActivity) mContext).getAddresses();
            }
        });
    }

    private void showPopUp(String title, String message, String closeButtonText, String nextButtonText, final int type, int position) {
        dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_information_pop_up);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_pop_up_close);
        Button closePopUp = (Button) dialog.findViewById(R.id.button_close_pop_up);
        Button nextButton = (Button) dialog.findViewById(R.id.button_pop_next);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_message);
        if (title.isEmpty()) {
            titleTextView.setVisibility(View.GONE);
        }
        titleTextView.setText(title);
        messageTextView.setText(message);
        closePopUp.setText(closeButtonText);
        nextButton.setText(nextButtonText);

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(shippingAddresses.get(position).getiD());
                shippingAddresses.remove(position);
                notifyDataSetChanged();
//              mItemManger.closeAllItems();
                dialog.dismiss();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }
}

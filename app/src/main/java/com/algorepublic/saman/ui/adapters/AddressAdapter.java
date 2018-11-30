package com.algorepublic.saman.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.ShippingAddress;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.myaccount.addresses.AddShippingAddressActivity;
import com.algorepublic.saman.ui.activities.myaccount.addresses.ShippingAddressActivity;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

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

            messageViewHolder.address.setText(shippingAddresses.get(position).getAddressLine1());

            // Drag From Right
            messageViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, messageViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));

            messageViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    int id = shippingAddresses.get(position).getID();
                    String text = shippingAddresses.get(position).getAddressLine1();
                    data.putExtra("ID",id);
                    data.putExtra("DATA",text);
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
                }
            });

            messageViewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAddress(shippingAddresses.get(position).getID());
                    shippingAddresses.remove(position);
                    notifyDataSetChanged();
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
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                ((ShippingAddressActivity)mContext).getAddresses();
            }
        });
    }
}

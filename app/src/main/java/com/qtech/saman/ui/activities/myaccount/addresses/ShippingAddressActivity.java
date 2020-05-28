package com.qtech.saman.ui.activities.myaccount.addresses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetAddressApi;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.AddressAdapter;
import com.qtech.saman.utils.GlobalValues;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ShippingAddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_settings)
    ImageView settings;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<ShippingAddress> shippingAddresses;
    AddressAdapter addressAdapter;

    User authenticatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.shipping_address));
        toolbarBack.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setImageDrawable(getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        } else {
            settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        }

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        shippingAddresses = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, shippingAddresses);
        mRecyclerView.setAdapter(addressAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddresses();
    }

    public void call() {
        onResume();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_settings)
    public void addAddress() {
        Intent intent = new Intent(ShippingAddressActivity.this, AddShippingAddressActivity.class);
        startActivity(intent);
    }

    public void getAddresses() {

        shippingAddresses.clear();
        addressAdapter.notifyDataSetChanged();

        WebServicesHandler.instance.getAddressList(authenticatedUser.getId(), new retrofit2.Callback<GetAddressApi>() {
            @Override
            public void onResponse(Call<GetAddressApi> call, Response<GetAddressApi> response) {
                GetAddressApi addressApi = response.body();
                if (addressApi != null) {
                    if (addressApi.getSuccess() == 1) {
                        if (addressApi.getResult() != null) {
                            shippingAddresses.addAll(addressApi.getResult());
                        }
                        if (shippingAddresses.size() > 2) {
                            settings.setVisibility(View.VISIBLE);
                        } else {
                            settings.setVisibility(View.VISIBLE);
                        }
                        addressAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAddressApi> call, Throwable t) {
                addressAdapter.notifyDataSetChanged();
            }
        });
    }
}

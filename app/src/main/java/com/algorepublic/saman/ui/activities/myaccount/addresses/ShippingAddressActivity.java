package com.algorepublic.saman.ui.activities.myaccount.addresses;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.ShippingAddress;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.adapters.AddressAdapter;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
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


        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(ShippingAddressActivity.this,R.drawable.ic_ddelete),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                shippingAddresses.remove(pos);
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.edit),
                        ResourceUtil.getBitmap(ShippingAddressActivity.this,R.drawable.ic_edit),
                        Color.parseColor("#FEC831"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                Intent intent=new Intent(ShippingAddressActivity.this,AddShippingAddressActivity.class);
//                                intent.putExtra("Address",shippingAddresses.get(pos).getAddressLine1());
                                intent.putExtra("Address","Testing ok okay okay");
                                intent.putExtra("Type",1);
                                startActivity(intent);
                            }
                        }
                ));
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddresses();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_settings)
    public void addAddress() {
        Intent intent=new Intent(ShippingAddressActivity.this,AddShippingAddressActivity.class);
        startActivity(intent);
    }

    private void getAddresses(){

        shippingAddresses.clear();

        for (int i=0;i<5;i++){
            ShippingAddress shippingAddress=new ShippingAddress();
            shippingAddresses.add(shippingAddress);
        }
        addressAdapter.notifyDataSetChanged();

    }

}

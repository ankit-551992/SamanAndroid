package com.algorepublic.saman.ui.activities.order.checkout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.ui.activities.PoliciesActivity;
import com.algorepublic.saman.ui.adapters.BagCartAdapter;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SamanApp;
import com.algorepublic.saman.utils.SwipeHelper;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutOrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView cross;


    @BindView(R.id.tv_order_total)
    TextView orderTotalTextView;
    @BindView(R.id.tv_order_number)
    TextView orderNumberTextView;
    @BindView(R.id.tv_order_status)
    TextView orderStatusTextView;

    //Bag
    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.cart_item_recyclerView)
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    FavoritesAdapter favoritesAdapter;
    //Bag

    PlaceOrderResponse placeOrderResponse;
    int orderTotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
//        toolbarBack.setVisibility(View.VISIBLE);
        cross.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            placeOrderResponse = (PlaceOrderResponse)getIntent().getSerializableExtra("Response");
            orderTotal = getIntent().getIntExtra("OrderTotal",0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cross.setImageDrawable(getDrawable(R.drawable.ic_cross));
        }else {
            cross.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross));
        }

        orderTotalTextView.setText(String.valueOf(orderTotal));
        if(placeOrderResponse.getResult().getOrderNumber()!=null) {
            orderTotalTextView.setText(placeOrderResponse.getResult().getOrderNumber());
        }

        if(placeOrderResponse.getResult().getOrderStatus()!=null) {
            if (placeOrderResponse.getResult().getOrderStatus() == 0) {
                orderStatusTextView.setText(getString(R.string.pending));
            }
        }
        setBag();

        new SwipeHelper(this, cartRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.cancel),
                        ResourceUtil.getBitmap(CheckoutOrderActivity.this,R.drawable.ic_cross),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                            }
                        }
                ));
            }
        };

    }

    @Override
    protected void onDestroy() {
        GlobalValues.orderPlaced=true;
        if(SamanApp.localDB!=null){
            SamanApp.localDB.clearCart();
        }
        super.onDestroy();
    }

    @OnClick(R.id.toolbar_search)
    void search(){

        GlobalValues.orderPlaced=true;
        if(SamanApp.localDB!=null){
            SamanApp.localDB.clearCart();
        }
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
//        if(SamanApp.localDB!=null){
//            SamanApp.localDB.clearCart();
//        }
//        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        if(SamanApp.localDB!=null){
            SamanApp.localDB.clearCart();
        }
        super.onBackPressed();
    }

    @OnClick(R.id.tv_return_policy)
    void policy(){
        Intent intent=new Intent(CheckoutOrderActivity.this,PoliciesActivity.class);
        intent.putExtra("type",2);
        startActivity(intent);
//        new FinestWebView.Builder(CheckoutOrderActivity.this).show(Constants.URLS.returnPolicy);
    }


    private void setBag() {
        layoutManager = new LinearLayoutManager(CheckoutOrderActivity.this);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(CheckoutOrderActivity.this, productArrayList);
        cartRecyclerView.setAdapter(favoritesAdapter);

        getDataFromDB();
    }


    private void getDataFromDB(){

        if(SamanApp.localDB!=null){
            productArrayList.addAll(SamanApp.localDB.getCartProducts());
            favoritesAdapter.notifyDataSetChanged();
        }

        quantity.setText(productArrayList.size()+ " " +getResources().getQuantityString(R.plurals.items, productArrayList.size()));
    }

}

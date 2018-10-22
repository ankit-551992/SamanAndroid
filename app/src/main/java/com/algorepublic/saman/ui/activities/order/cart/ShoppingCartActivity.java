package com.algorepublic.saman.ui.activities.order.cart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.adapters.BagCartAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    //Bag
    @BindView(R.id.bag_recyclerView)
    RecyclerView bagRecyclerView;
    @BindView(R.id.tv_bag_see_all)
    TextView bagSeeAllTextView;
    RecyclerView.LayoutManager layoutManager;
    List<Store> bagArrayList = new ArrayList<>();
    BagCartAdapter bagCartAdapter;
    //Bag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.shopping_cart));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        setBag();

    }



    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_place_order)
    void placeOrder(){
        Intent intent=new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
        startActivity(intent);
    }

    private void setBag() {
        layoutManager = new GridLayoutManager(this, 3);
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        bagArrayList = new ArrayList<>();
        bagCartAdapter = new BagCartAdapter(this, bagArrayList);
        bagRecyclerView.setAdapter(bagCartAdapter);
        bagRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));

        for (int i = 0; i < 3; i++) {
            Store store = new Store();
            bagArrayList.add(store);
            bagCartAdapter.notifyDataSetChanged();
        }
    }


}

package com.algorepublic.saman.ui.activities.order.checkout;

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
import com.algorepublic.saman.ui.adapters.BagCartAdapter;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
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

    //Bag
    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.cart_item_recyclerView)
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    FavoritesAdapter favoritesAdapter;
    //Bag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }


        setBag();

    }

    @OnClick(R.id.tv_return_policy)
    void policy(){
        new FinestWebView.Builder(CheckoutOrderActivity.this).show(Constants.URLS.returnPolicy);
    }


    private void setBag() {
        layoutManager = new LinearLayoutManager(CheckoutOrderActivity.this);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(CheckoutOrderActivity.this, productArrayList);
        cartRecyclerView.setAdapter(favoritesAdapter);

        getfavorites();
    }


    private void getfavorites(){
        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            productArrayList.add(product);
            favoritesAdapter.notifyDataSetChanged();
        }

        quantity.setText(productArrayList.size()+ " " +getResources().getQuantityString(R.plurals.items, productArrayList.size()));
    }

}

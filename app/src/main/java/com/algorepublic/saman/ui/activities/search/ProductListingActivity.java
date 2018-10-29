package com.algorepublic.saman.ui.activities.search;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProductListingActivity  extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.search_bar)
    RelativeLayout searchBar;
    @BindView(R.id.recyclerView)
    RecyclerView searchRecyclerView;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;

    int function=0;
    int storeID=0;
    String storeName="";
    String storeNameAr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            function=bundle.getInt("Function");
            if(function==2){
                storeID=bundle.getInt("StoreID");
                storeName=bundle.getString("StoreName");
                storeNameAr=bundle.getString("StoreNameAr");
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (function==1){
            toolbarTitle.setText(getString(R.string.latest_product));
        }else if (function==2){
            toolbarTitle.setText(storeName);
        }

        toolbarTitle.setAllCaps(true);
        toolbarBack.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        setProductsAdapter();

    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void setProductsAdapter() {
        productLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(productLayoutManager);
        searchRecyclerView.setNestedScrollingEnabled(false);
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(this, displayData);
        searchRecyclerView.setAdapter(productAdapter);
        searchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        if(function==2) {
            getProductsByStoreID(storeID);
        }else {
            getLatestProducts(storeID);
        }
    }


    private void getProductsByStoreID(int storeID) {

        WebServicesHandler.instance.getProductsByStore(""+storeID,new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        displayData.addAll(getProducts.getProduct());
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }

    private void getLatestProducts(int storeID) {

        WebServicesHandler.instance.getProductsByStore(""+storeID,new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {

                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        displayData.addAll(getProducts.getProduct());
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }
}

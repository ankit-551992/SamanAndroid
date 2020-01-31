package com.qtech.saman.ui.activities.search;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetProducts;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.ProductAdapter;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProductListingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_empty)
    TextView empty;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView search;
    @BindView(R.id.search_bar)
    RelativeLayout searchBar;
    @BindView(R.id.recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;

    int function = 0;
    int storeID = 0;
    int bannerID = 0;
    int categoryID = 0;
    String storeName = "";
    String storeNameAr = "";
    String categoryName = "";
    String categoryNameAr = "";

    User authenticatedUser;

    int currentPage = 0;
    int pageSize = 20;
    boolean isGetAll = false;
    private boolean isLoading;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int visibleThreshold = 5;
            int lastVisibleItem, totalItemCount;
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

            if (!isGetAll && !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                displayData.add(null);
                productAdapter.notifyItemInserted(displayData.size() - 1);
                isLoading = true;
                currentPage++;
                if (function == 2) {
                    getProductsByStoreID(storeID, categoryID, currentPage, pageSize);
                } else {
                    getLatestProducts(currentPage, pageSize);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        authenticatedUser = GlobalValues.getUser(this);
        if (bundle != null) {
            function = bundle.getInt("Function");
            if (function == 2) {
                storeID = bundle.getInt("StoreID");
                bannerID = bundle.getInt("BannerID");
                categoryID = bundle.getInt("categoryID");
                storeName = bundle.getString("StoreName");
                storeNameAr = bundle.getString("StoreNameAr");
                categoryName = bundle.getString("categoryName");
                categoryNameAr = bundle.getString("categoryNameAr");
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        search.setVisibility(View.GONE);
        if (function == 1) {
            toolbarTitle.setText(getString(R.string.new_in));
        } else if (function == 2) {
            if (SamanApp.isEnglishVersion) {
                toolbarTitle.setText(storeName);
            } else {
                toolbarTitle.setText(storeNameAr);
            }
        }

        toolbarTitle.setAllCaps(true);
        toolbarBack.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        setProductsAdapter();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_search)
    void search() {
        Intent intent = new Intent(ProductListingActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void setProductsAdapter() {
        productLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(productLayoutManager);
        searchRecyclerView.setNestedScrollingEnabled(false);
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(this, displayData, authenticatedUser.getId(), false);
        searchRecyclerView.setAdapter(productAdapter);
        searchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false, this));
        searchRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        progressBar.setVisibility(View.VISIBLE);

        if (function == 2) {
            if (bannerID != 0) {
                getProductsByStoreIDForBanner(bannerID, storeID, categoryID, currentPage, pageSize);
            } else {
                getProductsByStoreID(storeID, categoryID, currentPage, pageSize);
            }

        } else {
            getLatestProducts(currentPage, pageSize);
        }
    }

    private void getProductsByStoreID(int storeID, int categoryId, int pageIndex, int pageSize) {

        WebServicesHandler.instance.getProductsByStoreAndCategory(storeID, categoryId, authenticatedUser.getId(), pageIndex, pageSize, new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                GetProducts getProducts = response.body();
                progressBar.setVisibility(View.GONE);
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1) == null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if (getProducts.getProduct() != null && getProducts.getProduct().size() > 0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        } else {
                            isGetAll = true;
                        }
                        isLoading = false;
                    }
                }
                if (displayData.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }

    private void getProductsByStoreIDForBanner(int bannerID, int storeID, int categoryId, int pageIndex, int pageSize) {

        WebServicesHandler.instance.getProductsByStoreAndCategoryForBanner(bannerID, storeID, categoryId, authenticatedUser.getId(), pageIndex, pageSize, new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                GetProducts getProducts = response.body();
                progressBar.setVisibility(View.GONE);
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1) == null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if (getProducts.getProduct() != null && getProducts.getProduct().size() > 0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        } else {
                            isGetAll = true;
                        }
                        isLoading = false;
                    }
                }
                if (displayData.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }

    private void getLatestProducts(int pageIndex, int pageSize) {

        WebServicesHandler.instance.getLatestProducts(authenticatedUser.getId(), pageIndex, pageSize, new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                progressBar.setVisibility(View.GONE);
                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1) == null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if (getProducts.getProduct() != null && getProducts.getProduct().size() > 0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        } else {
                            isGetAll = true;
                        }
                        isLoading = false;
                    }
                }
                if (displayData.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }
}

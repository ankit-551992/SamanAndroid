package com.qtech.saman.ui.fragments.product;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetProducts;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.ProductAdapter;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.qtech.saman.utils.GlobalValues.CATEGORYID;

public class ProductsCategoryFragment extends BaseFragment {

    @BindView(R.id.tv_empty)
    TextView empty;
    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    int currentPage = 0;
    boolean isGetAll = false;
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;
    int pageSize = 20;
    User authenticatedUser;
    private int categoryID;
    List<Product> newdisplayData = new ArrayList<>();
    String search_category_product;
    boolean isBannerProduct = false;

    public static ProductsCategoryFragment newInstance(int categoryID, String search, boolean b) {
        Bundle bundle = new Bundle();
        bundle.putInt("CategoryID", categoryID);
        bundle.putString("SEARCH", search);
        bundle.putBoolean("IsBannerProduct", b);
        ProductsCategoryFragment fragment = new ProductsCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            categoryID = bundle.getInt("CategoryID");
            search_category_product = bundle.getString("SEARCH");
            isBannerProduct = bundle.getBoolean("IsBannerProduct", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        currentPage = 0;
        authenticatedUser = GlobalValues.getUser(getContext());
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), displayData, authenticatedUser.getId(), false);
        recyclerView.setAdapter(productAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false, getContext()));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        if (isBannerProduct) {
//            isBannerProduct = false;
            getBannerProductList(categoryID, authenticatedUser.getId(), currentPage, pageSize);
        } else {
            getProducts(categoryID, currentPage, pageSize);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayData = new ArrayList<>();
                productAdapter = new ProductAdapter(getContext(), displayData, authenticatedUser.getId(), false);
                recyclerView.setAdapter(productAdapter);
                currentPage = 0;
                if (isBannerProduct) {
                    getBannerProductList(categoryID, authenticatedUser.getId(), currentPage, pageSize);
                } else {
                    getProducts(categoryID, currentPage, pageSize);
                }

            }
        });
        return view;
    }

    private void getBannerProductList(int categoryID, Integer id, int currentPage, int pageSize) {

        String userId = String.valueOf(authenticatedUser.getId());
        WebServicesHandler.instance.getBannerProductList(categoryID, userId, currentPage, pageSize, new retrofit2.Callback<GetProducts>() {
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
                        } else {
                            isGetAll = true;
                        }
                        isLoading = false;
                        if (!search_category_product.equals("")) {
                            getSearchCategory(search_category_product, displayData);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                }

                if (displayData.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                    empty.setText(getActivity().getResources().getString(R.string.no_product_found));
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }

    private void getProducts(int categoryID, int pageIndex, int pageSize) {
        CATEGORYID = categoryID;
        if (categoryID == 0) {
            //for All
            getLatestProducts(pageIndex, pageSize, 0);
        } else if (categoryID == 1) {
            //for new in
            getLatestProducts(pageIndex, pageSize, 1);
        } else {
            WebServicesHandler.instance.getProductsByCategory(categoryID, authenticatedUser.getId(), pageIndex, pageSize, new retrofit2.Callback<GetProducts>() {
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
                            } else {
                                isGetAll = true;
                            }
                            isLoading = false;
                            if (!search_category_product.equals("")) {
                                getSearchCategory(search_category_product, displayData);
                            }
//                            if (category_flag) {
//                                if (!search_category_product.equals("")) {
//                                    getSearchCategory(search_category_product, displayData);
//                                }
//                            }
                            productAdapter.notifyDataSetChanged();
                        }
                    }else {
                        if (displayData.size() > 0) {
                            empty.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            empty.setText(getResources().getString(R.string.no_product_found));
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<GetProducts> call, Throwable t) {
                }
            });
        }
    }

    private void getSearchCategory(String search_category_product, List<Product> productList) {
        List<Product> seachproductlist = new ArrayList<>();
//        seachstorelist.addAll(storeList);
        for (Product storename : productList) {
//            if (storename.getStoreName() != null && storename.getStoreName().contains(search)) {
            if (storename.getProductName().toLowerCase().contains(search_category_product.toLowerCase())) {
                seachproductlist.add(storename);
            }
        }
        if (displayData != null) {
            displayData.clear();
        }
        displayData.addAll(seachproductlist);
        productAdapter.notifyDataSetChanged();
    }

    private void getLatestProducts(int pageIndex, int pageSize, int categoryID) {

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

                        if (newdisplayData.size() > 0) {
                            newdisplayData.clear();
                        }
                        newdisplayData.addAll(getProducts.getProduct());
                        if (getProducts.getProduct() != null && getProducts.getProduct().size() > 0) {
                            if (categoryID == 0) {
                                displayData.addAll(getProducts.getProduct());
                            } else {
                                for (Product product : newdisplayData) {
                                    if (product.getIsNewIn().equals("true")) {
                                        displayData.add(product);
                                    }
                                }
                            }
                            if (!search_category_product.equals("")) {
                                getSearchCategory(search_category_product, displayData);
                            }
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
                    empty.setText(getResources().getString(R.string.no_product_found));
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }

    @Override
    public String getName() {
        return null;
    }

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
//              productAdapter.notifyItemInserted(displayData.size() + 1);
                isLoading = true;
                currentPage++;
                getProducts(categoryID, currentPage, pageSize);
            }
        }
    };
}

package com.algorepublic.saman.ui.fragments.product;

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

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class AllProductsFragment extends BaseFragment {

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
    private boolean isNewIn;

    public static AllProductsFragment newInstance(boolean isNewIn) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewIn", isNewIn);

        AllProductsFragment fragment = new AllProductsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            isNewIn = bundle.getBoolean("isNewIn");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        currentPage = 0;
        authenticatedUser = GlobalValues.getUser(getContext());
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), displayData,authenticatedUser.getId(),false);
        recyclerView.setAdapter(productAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false,getContext()));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        if(isNewIn) {
            getLatestProducts(currentPage, pageSize);
        }else {
            getAllProducts(currentPage, pageSize);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayData = new ArrayList<>();
                productAdapter = new ProductAdapter(getContext(), displayData,authenticatedUser.getId(),false);
                recyclerView.setAdapter(productAdapter);
                currentPage = 0;
                if(isNewIn) {
                    getLatestProducts(currentPage, pageSize);
                }else {
                    getAllProducts(currentPage, pageSize);
                }
            }
        });

        return view;
    }


    private void getLatestProducts(int pageIndex,int pageSize) {

        WebServicesHandler.instance.getLatestProducts(authenticatedUser.getId(),pageIndex,pageSize,new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                progressBar.setVisibility(View.GONE);
                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1)==null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if(getProducts.getProduct()!=null && getProducts.getProduct().size()>0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        }else {
                            isGetAll=true;
                        }
                        isLoading = false;
                    }
                }
                if(displayData.size()>0){
                    empty.setVisibility(View.GONE);
                }else {
                    empty.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
            }
        });
    }


    private void getAllProducts(int pageIndex,int pageSize) {

        WebServicesHandler.instance.getAllProducts(authenticatedUser.getId(),pageIndex,pageSize,new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                progressBar.setVisibility(View.GONE);
                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1) {
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1)==null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if(getProducts.getProduct()!=null && getProducts.getProduct().size()>0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        }else {
                            isGetAll=true;
                        }
                        isLoading = false;
                    }
                }
                if(displayData.size()>0){
                    empty.setVisibility(View.GONE);
                }else {
                    empty.setVisibility(View.VISIBLE);
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
                productAdapter.notifyItemInserted(displayData.size() - 1);
                isLoading = true;
                currentPage++;
                if(isNewIn) {
                    getLatestProducts(currentPage, pageSize);
                }else {
                    getAllProducts(currentPage, pageSize);
                }
            }
        }
    };

}

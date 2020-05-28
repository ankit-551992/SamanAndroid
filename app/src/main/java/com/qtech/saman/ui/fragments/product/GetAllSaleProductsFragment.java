package com.qtech.saman.ui.fragments.product;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class GetAllSaleProductsFragment extends BaseFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
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

        getProducts(currentPage,pageSize);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayData = new ArrayList<>();
                productAdapter = new ProductAdapter(getContext(), displayData,authenticatedUser.getId(),false);
                recyclerView.setAdapter(productAdapter);
                currentPage = 0;
                getProducts(currentPage,pageSize);
            }
        });
        return view;
    }

    private void getProducts(int pageIndex,int pageSize) {

//        WebServicesHandler.instance.getSaleProducts(authenticatedUser.getId(),pageIndex,pageSize,new retrofit2.Callback<GetProducts>() {
        WebServicesHandler.instance.getSaleProducts(authenticatedUser.getId(),20,30,new retrofit2.Callback<GetProducts>() {
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
                getProducts(currentPage,pageSize);
            }
        }
    };
}

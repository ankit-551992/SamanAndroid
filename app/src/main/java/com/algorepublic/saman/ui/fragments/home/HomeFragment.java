package com.algorepublic.saman.ui.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    //stores
    @BindView(R.id.stores_recyclerView)
    RecyclerView storesRecyclerView;
    @BindView(R.id.tv_stores_see_all)
    TextView storeSeeAllTextView;
    RecyclerView.LayoutManager layoutManager;
    List<Store> storeArrayList = new ArrayList<>();
    StoresAdapter storesAdapter;
    //stores

    //product
    @BindView(R.id.latest_products_recyclerView)
    RecyclerView productRecyclerView;
    @BindView(R.id.tv_latest_products_see_all)
    TextView productSeeAllTextView;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> productArrayList = new ArrayList<>();
    ProductAdapter productAdapter;
    //product


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        setStore();
        setProduct();
        return view;
    }

    private void setStore() {
        layoutManager = new GridLayoutManager(getActivity(), 3);
        storesRecyclerView.setLayoutManager(layoutManager);
        storesRecyclerView.setNestedScrollingEnabled(false);
        storeArrayList = new ArrayList<>();
        storesAdapter = new StoresAdapter(getContext(), storeArrayList);
        storesRecyclerView.setAdapter(storesAdapter);
        storesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));

        for (int i = 0; i < 3; i++) {
            Store store = new Store();
            storeArrayList.add(store);
            storesAdapter.notifyDataSetChanged();
        }
    }

    private void setProduct() {
        productLayoutManager = new GridLayoutManager(getActivity(), 2);
        productRecyclerView.setLayoutManager(productLayoutManager);
        productRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productArrayList);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        for (int i = 0; i < 2; i++) {
            Product product = new Product();
            productArrayList.add(product);
            productAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getName() {
        return "Home Fragment";
    }
}

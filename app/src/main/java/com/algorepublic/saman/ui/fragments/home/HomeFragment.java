package com.algorepublic.saman.ui.fragments.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Brand;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.ui.activities.productdetail.CustomPagerAdapter;
import com.algorepublic.saman.ui.adapters.BestSellersAdapter;
import com.algorepublic.saman.ui.adapters.BrandsAdapter;
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

    //Brand
    @BindView(R.id.brands_recyclerView)
    RecyclerView brandsRecyclerView;
    RecyclerView.LayoutManager brandsLayoutManager;
    List<Brand> brandsArrayList = new ArrayList<>();
    BrandsAdapter brandsAdapter;
    //Brand


    //BestSellers
    @BindView(R.id.bestSellersPager)
    ViewPager  bestSellersPager;
    ArrayList<String> bestSellersURLs;
    CustomPagerAdapter bestSellersAdapter;
    //BestSellers

    //Header
    @BindView(R.id.viewpager)
    ViewPager mPager;
    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;
    //Header

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
        setBrand();
        setProduct();
        header();
        setBestSellers();
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


    private void header(){
        urls=new ArrayList<>();
        customPagerAdapter= new CustomPagerAdapter(getContext(),urls);
        mPager.setAdapter(customPagerAdapter);

        setPagerData();
    }

    private void setPagerData(){
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        customPagerAdapter.notifyDataSetChanged();
    }

    private void setBestSellers() {
        bestSellersURLs=new ArrayList<>();
        bestSellersAdapter= new CustomPagerAdapter(getContext(),bestSellersURLs);
        bestSellersPager.setAdapter(bestSellersAdapter);

        bestSellersPager.setPageMargin(10);
        bestSellersPager.setClipToPadding(false);
        bestSellersPager.setPadding(100,0,100,0);

        bestSellersURLs.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        bestSellersURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        bestSellersURLs.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        bestSellersURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        bestSellersAdapter.notifyDataSetChanged();
    }

    private void setBrand() {
        brandsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        brandsRecyclerView.setLayoutManager(brandsLayoutManager);
        brandsRecyclerView.setNestedScrollingEnabled(false);
        brandsArrayList = new ArrayList<>();
        brandsAdapter = new BrandsAdapter(getContext(), brandsArrayList);
        brandsRecyclerView.setAdapter(brandsAdapter);

        for (int i = 0; i < 3; i++) {
            Brand brand = new Brand();
            brandsArrayList.add(brand);
            brandsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getName() {
        return "Home Fragment";
    }
}

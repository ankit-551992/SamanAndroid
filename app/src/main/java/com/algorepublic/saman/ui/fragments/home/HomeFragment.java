package com.algorepublic.saman.ui.fragments.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Brand;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Slider;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.order.cart.ShoppingCartActivity;
import com.algorepublic.saman.ui.activities.product.ProductsActivity;
import com.algorepublic.saman.ui.activities.product.SalesProductActivity;
import com.algorepublic.saman.ui.activities.productdetail.CustomPagerAdapter;

import com.algorepublic.saman.ui.adapters.BestSellerPagerAdapter;
import com.algorepublic.saman.ui.adapters.BestSellersAdapter;
import com.algorepublic.saman.ui.adapters.BrandsAdapter;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeContractor.View {

    HomeContractor.Presenter presenter;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.iv_header_below_banner)
    ImageView headerBelowBanner;

    //stores
    @BindView(R.id.stores_recyclerView)
    RecyclerView storesRecyclerView;
    @BindView(R.id.tv_stores_see_all)
    TextView storeSeeAllTextView;
    RecyclerView.LayoutManager layoutManager;
    StoresAdapter storesAdapter;
    //stores

    //Brand
    @BindView(R.id.brands_recyclerView)
    RecyclerView brandsRecyclerView;
    RecyclerView.LayoutManager brandsLayoutManager;
    BrandsAdapter brandsAdapter;
    //Brand

    //BestSellers
    @BindView(R.id.bestSellersPager)
    ViewPager bestSellersPager;
    @BindView(R.id.indicators)
    CirclePageIndicator pageIndicator;
    BestSellerPagerAdapter bestSellersAdapter;
    //BestSellers

    //Header
    @BindView(R.id.viewpager)
    ViewPager mPager;
    CustomPagerAdapter customPagerAdapter;
    @BindView(R.id.indicator)
    CirclePageIndicator circlePageIndicator;
    //Header

    //product
    @BindView(R.id.latest_products_recyclerView)
    RecyclerView productRecyclerView;
    @BindView(R.id.tv_latest_products_see_all)
    TextView productSeeAllTextView;
    RecyclerView.LayoutManager productLayoutManager;
    ProductAdapter productAdapter;
    //product

    User authenticatedUser;
    int bannerType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        authenticatedUser = GlobalValues.getUser(getContext());
        presenter = new HomePresenter(this);
        presenter.getHomeData(authenticatedUser.getId());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @OnClick(R.id.tv_latest_products_see_all)
    void products() {
//        Intent intent=new Intent(getContext(), ProductListingActivity.class);
//        intent.putExtra("Function",1); //1 for Latest Products
//        startActivity(intent);
        Intent intent = new Intent(getContext(), ProductsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_stores_see_all)
    void stores() {
//        Intent intent=new Intent(getContext(), StoreActivity.class);
//        startActivity(intent);
        ((DashboardActivity) getContext()).callStoreNav();
    }

    @OnClick(R.id.iv_header_below_banner)
    void banner() {
//        Log.e("BannerType",""+bannerType);
        if (bannerType == 5) {
            Intent intent = new Intent(getContext(), SalesProductActivity.class);
            getContext().startActivity(intent);
        }
    }


    private void setStore(List<Store> storeArrayList) {
        layoutManager = new GridLayoutManager(getActivity(), 3);
        storesRecyclerView.setLayoutManager(layoutManager);
        storesRecyclerView.setNestedScrollingEnabled(false);
        storesAdapter = new StoresAdapter(getContext(), storeArrayList);
        storesRecyclerView.setAdapter(storesAdapter);
        storesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false, getContext()));
    }

    private void setProduct(List<Product> productArrayList) {
        productLayoutManager = new GridLayoutManager(getActivity(), 2);
        productRecyclerView.setLayoutManager(productLayoutManager);
        productRecyclerView.setNestedScrollingEnabled(false);
        productAdapter = new ProductAdapter(getContext(), productArrayList, authenticatedUser.getId(), true);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false, getContext()));
    }

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5 * 1000; // time in milliseconds between successive task executions.

    private void header(final List<String> urls) {
        customPagerAdapter = new CustomPagerAdapter(getContext(), urls);
        mPager.setAdapter(customPagerAdapter);
        circlePageIndicator.setViewPager(mPager);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                mPager.setCurrentItem(currentPage, true);
                currentPage++;
                if (currentPage == urls.size()) {
                    currentPage = 0;
                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void setBestSellers(List<Slider> sliderList) {

        bestSellersAdapter = new BestSellerPagerAdapter(getContext(), sliderList);
        bestSellersPager.setAdapter(bestSellersAdapter);

//        bestSellersPager.setPageMargin(10);
//        bestSellersPager.setClipToPadding(false);
//        bestSellersPager.setPadding(200,0,200,0);

        int median;
        if (sliderList.size() % 2 == 0)
            median = (sliderList.size() / 2 + sliderList.size() / 2 - 1) / 2;
        else
            median = sliderList.size() / 2;

        bestSellersPager.setCurrentItem(median);
        pageIndicator.setViewPager(bestSellersPager);
        pageIndicator.setCurrentItem(median);
    }

    private void hotPicks(List<Product> hotPicks) {
        brandsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        brandsRecyclerView.setLayoutManager(brandsLayoutManager);
        brandsRecyclerView.setNestedScrollingEnabled(false);
        brandsAdapter = new BrandsAdapter(getContext(), hotPicks, authenticatedUser.getId());
        brandsRecyclerView.setAdapter(brandsAdapter);
    }

    @Override
    public String getName() {
        return "Home Fragment";
    }

    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public void response(HomeScreenData screenApi) {

        if (screenApi.getStores() != null) {
            setStore(screenApi.getStores());
        } else {
            storesRecyclerView.setVisibility(View.GONE);
            storeSeeAllTextView.setVisibility(View.GONE);
        }

        if (screenApi.getBannerURL() != null && !screenApi.getBannerURL().equals("")) {
            Picasso.get().load(Constants.URLS.BaseURLImages + screenApi.getBannerURL()).fit().centerCrop()
                    .placeholder(R.drawable.home_banner)
                    .error(R.drawable.home_banner)
                    .into(headerBelowBanner);

            bannerType = screenApi.getBannerType();
        }

        if (screenApi.getLatestProducts() != null) {
            setProduct(screenApi.getLatestProducts());
        } else {
            productRecyclerView.setVisibility(View.GONE);
            productSeeAllTextView.setVisibility(View.GONE);
        }

        if (screenApi.getHeaderURLs() != null) {
            header(screenApi.getHeaderURLs());
        } else {
            List<String> dummyHeaders = new ArrayList<>();
            dummyHeaders.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
            header(dummyHeaders);
        }

        if (screenApi.getHotPicks() != null) {
            hotPicks(screenApi.getHotPicks());
        }

        if (screenApi.getBannerSliderURLs() != null) {
            setBestSellers(screenApi.getBannerSliderURLs());
        } else {
            bestSellersPager.setVisibility(View.GONE);
            pageIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void error(String message) {
        if (message.equalsIgnoreCase("null")) {
            showAlert("Server Error", "Please check you internet connection and try again", getString(R.string.try_again), getContext());
        }
    }

    public void showAlert(String title, String message, String buttonText, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.getHomeData(authenticatedUser.getId());
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

package com.algorepublic.saman.ui.fragments.favourite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.ui.adapters.SwipeFavoritesAdapter;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends BaseFragment implements FavoritesContractor.View {

    @BindView(R.id.loading)
    RelativeLayout loading;

    @BindView(R.id.tv_empty_bag)
    TextView tv_empty_bag;
    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.recycler)
    RecyclerView favoritesRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    SwipeFavoritesAdapter favoritesAdapter;

    FavoritesPresenter presenter;
    User authenticatedUser;


    int currentPage = 0;
    int pageSize = 20;
    boolean isGetAll = false;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);
        authenticatedUser = GlobalValues.getUser(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        favoritesAdapter = new SwipeFavoritesAdapter(getContext(), productArrayList,this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        presenter = new FavoritesPresenter(this);
        presenter.getFavoritesData(authenticatedUser.getId(),currentPage,pageSize,true);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
    }


    @Override
    public void response(List<Product> product) {

        if (productArrayList.size() > 0 && productArrayList.get(productArrayList.size() - 1)==null) {
            productArrayList.remove(productArrayList.size() - 1);
            favoritesAdapter.notifyItemRemoved(productArrayList.size());
        }

        if(product.size()==0){
            isGetAll=true;
        }

        isLoading=false;
        productArrayList.addAll(product);
        favoritesAdapter.notifyDataSetChanged();
        quantity.setText(productArrayList.size() + " " + getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));
        ((DashboardActivity) getActivity()).updateFavCount(productArrayList.size());
        if (productArrayList.size() > 0) {
            tv_empty_bag.setVisibility(View.GONE);
        } else {
            tv_empty_bag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void error(String message) {

    }

    public void updateCount(int size){
        if(size>0){
            tv_empty_bag.setVisibility(View.GONE);
        }else{
            tv_empty_bag.setVisibility(View.VISIBLE);
        }
        quantity.setText(size+ " " +getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));

    }

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
                productArrayList.add(null);
                favoritesAdapter.notifyItemInserted(productArrayList.size() - 1);
                isLoading = true;
                currentPage++;
                presenter.getFavoritesData(authenticatedUser.getId(),currentPage,pageSize,false);
            }
        }
    };
}

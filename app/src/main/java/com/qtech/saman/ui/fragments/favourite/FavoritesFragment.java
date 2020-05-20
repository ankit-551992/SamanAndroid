package com.qtech.saman.ui.fragments.favourite;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.adapters.SwipeFavoritesAdapter;
import com.qtech.saman.utils.GlobalValues;

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
        GlobalValues.isFromHome = false;
        authenticatedUser = GlobalValues.getUser(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        favoritesAdapter = new SwipeFavoritesAdapter(getContext(), productArrayList, this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        presenter = new FavoritesPresenter(this);
        presenter.getFavoritesData(authenticatedUser.getId(), currentPage, pageSize, true);
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

        if (productArrayList.size() > 0 && productArrayList.get(productArrayList.size() - 1) == null) {
            productArrayList.remove(productArrayList.size() - 1);
            favoritesAdapter.notifyItemRemoved(productArrayList.size());
        }

        if (product.size() == 0) {
            isGetAll = true;
        }

        isLoading = false;
        productArrayList.addAll(product);
        favoritesAdapter.notifyDataSetChanged();
        int quan1 = 0;
        quan1 = quan1 + productArrayList.size();
        quantity.setText(quan1 + getActivity().getResources().getQuantityString(R.plurals.bag_wish_items, quan1));
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

    public void updateCount(int size) {
        if (size > 0) {
            tv_empty_bag.setVisibility(View.GONE);
        } else {
            tv_empty_bag.setVisibility(View.VISIBLE);
        }
        int quan = 0;
        for (int i = 0; i < productArrayList.size(); i++) {
            quan = quan + productArrayList.get(i).getQuantity();
        }
        quantity.setText(quan + " " + getActivity().getResources().getQuantityString(R.plurals.bag_wish_items, quan));
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
                presenter.getFavoritesData(authenticatedUser.getId(), currentPage, pageSize, false);
            }
        }
    };
}

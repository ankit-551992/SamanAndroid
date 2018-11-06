package com.algorepublic.saman.ui.fragments.store.Tab;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class Tabs extends BaseFragment {

    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    List<Store> storeArrayList = new ArrayList<>();
    StoresAdapter adapter;
    int currentPage = 1;
    boolean isGetAll = false;
    private int categoryID;

    public static Tabs newInstance(int categoryID) {
        Bundle bundle = new Bundle();
        bundle.putInt("CategoryID", categoryID);

        Tabs fragment = new Tabs();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            categoryID = bundle.getInt("CategoryID");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        storeArrayList = new ArrayList<>();
        adapter = new StoresAdapter(getContext(), storeArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        getStores();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeArrayList = new ArrayList<>();
                adapter = new StoresAdapter(getContext(), storeArrayList);
                recyclerView.setAdapter(adapter);
                currentPage = 1;
                getStores();
            }
        });

        return view;
    }


    private void getStores() {

        WebServicesHandler.instance.getStoresByCategory(String.valueOf(categoryID), new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {

                if (storeArrayList.size() > 0) {
                    storeArrayList.remove(storeArrayList.size() - 1);
                    adapter.notifyItemRemoved(storeArrayList.size());
                }
                GetStores getStores = response.body();
                if (getStores != null) {
                    if (getStores.getSuccess() == 1) {
                        isGetAll = true;
//                if(getStores.getLastPage()==currentPage){
//                    isGetAll=true;
//                }else {
//                    currentPage++;
//                }
                        progressBar.setVisibility(View.GONE);
                        storeArrayList.addAll(getStores.getStores());
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetStores> call, Throwable t) {
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
                storeArrayList.add(null);
                adapter.notifyItemInserted(storeArrayList.size() - 1);
                isLoading = true;
                currentPage++;
                getStores();
            }
        }
    };
}

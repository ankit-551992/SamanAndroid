package com.qtech.saman.ui.fragments.store.Tab;

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
import com.qtech.saman.data.model.Store;
import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.StoresAdapter;
import com.qtech.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @BindView(R.id.tv_empty)
    TextView tv_empty;
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
        adapter = new StoresAdapter(getContext(), storeArrayList, 0);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false, getContext()));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        getStores();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeArrayList = new ArrayList<>();
                adapter = new StoresAdapter(getContext(), storeArrayList, 0);
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

                progressBar.setVisibility(View.GONE);

                if (storeArrayList.size() > 0 && storeArrayList.get(storeArrayList.size() - 1) == null) {
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
                        storeArrayList.addAll(getStores.getStores());
                        progressBar.setVisibility(View.GONE);

                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;

                        Collections.sort(storeArrayList, new Comparator<Store>() {
                            @Override
                            public int compare(Store s1, Store s2) {
                                return s1.getStoreName().compareToIgnoreCase(s2.getStoreName());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }
                if (storeArrayList.size() < 1) {
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
                    tv_empty.setVisibility(View.GONE);
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

package com.qtech.saman.ui.fragments.store.Tab;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.data.model.Store;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.StoresAdapter;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.qtech.saman.utils.GlobalValues.FLAG_SEARCH;

public class AllStores extends BaseFragment {

    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tv_empty)
    TextView txt_empty;
    RecyclerView.LayoutManager layoutManager;
    List<Store> storeArrayList = new ArrayList<>();
    StoresAdapter adapter;
    int currentPage = 1;
    boolean isGetAll = false;
    String search = "";
    int storeID;
    boolean isBannerStore = false;
    String userID;
    int pageIndex = 0;
    int pageSize = 30;

    public static AllStores newInstance(String search_query, int storeID, boolean b) {
        Bundle bundle = new Bundle();
        bundle.putString("SEARCH", search_query);
        bundle.putInt("StoreID", storeID);
        bundle.putBoolean("BannerStore", b);

//      setAdapterOnSearch(bundle);
        AllStores fragment = new AllStores();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            search = bundle.getString("SEARCH", "");
            storeID = bundle.getInt("StoreID", 0);
            isBannerStore = bundle.getBoolean("BannerStore", false);
            Log.e("SEARCH000", "--search--00--readBundle---store---" + search);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());

        User authenticatedUser = GlobalValues.getUser(getActivity());
        userID = String.valueOf(authenticatedUser.getId());
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        storeArrayList = new ArrayList<>();
        adapter = new StoresAdapter(getContext(), storeArrayList, storeID);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false, getContext()));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        Log.e("SEARCH000", "--search--all---store---");
        if (isBannerStore) {
            getBannerStore();
        } else {
            getStores();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeArrayList = new ArrayList<>();
                adapter = new StoresAdapter(getContext(), storeArrayList, storeID);
                recyclerView.setAdapter(adapter);
                currentPage = 1;
                if (isBannerStore) {
                    getBannerStore();
                } else {
                    getStores();
                }
            }
        });
        return view;
    }

    private void getBannerStore() {

        WebServicesHandler.instance.getBannerProduct(storeID, userID, pageIndex, pageSize, new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {
                Log.e("PRODUCT888", "--response--" + new Gson().toJson(response));

                if (storeArrayList.size() > 0 && storeArrayList.get(storeArrayList.size() - 1) == null) {
                    storeArrayList.remove(storeArrayList.size() - 1);
                    adapter.notifyItemRemoved(storeArrayList.size());
                }
                GetStores getStores = response.body();
                if (getStores != null) {
                    if (getStores.getSuccess() == 1) {
                        txt_empty.setVisibility(View.GONE);
                        isGetAll = true;
//                if(getStores.getLastPage()==currentPage){
//                    isGetAll=true;
//                }else {
//                    currentPage++;
//                }
                        progressBar.setVisibility(View.GONE);
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

                        if (FLAG_SEARCH) {
                            getSearchStore(search, storeArrayList);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        txt_empty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetStores> call, Throwable t) {

            }
        });
    }

    private void getStores() {

        WebServicesHandler.instance.getAllStores(new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {

                if (storeArrayList.size() > 0 && storeArrayList.get(storeArrayList.size() - 1) == null) {
                    storeArrayList.remove(storeArrayList.size() - 1);
                    adapter.notifyItemRemoved(storeArrayList.size());
                }
                GetStores getStores = response.body();
                if (getStores != null) {
                    if (getStores.getSuccess() == 1) {
                        txt_empty.setVisibility(View.GONE);
                        isGetAll = true;
//                if(getStores.getLastPage()==currentPage){
//                    isGetAll=true;
//                }else {
//                    currentPage++;
//                }
                        progressBar.setVisibility(View.GONE);
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

                        if (FLAG_SEARCH) {
                            getSearchStore(search, storeArrayList);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        txt_empty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetStores> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                txt_empty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getSearchStore(String search, List<Store> storeList) {
        List<Store> seachstorelist = new ArrayList<>();
        for (Store storename : storeList) {
            if (SamanApp.isEnglishVersion) {
//            if (storename.getStoreName() != null && storename.getStoreName().contains(search)) {
                if (storename.getStoreName().toLowerCase().contains(search.toLowerCase())) {
                    seachstorelist.add(storename);
                }
            } else {
                if (storename.getStoreNameAR().contains(search.toLowerCase())) {
                    seachstorelist.add(storename);
                }
            }
        }
        if (storeArrayList != null) {
            storeArrayList.clear();
        }
        if (storeArrayList != null) {
            storeArrayList.addAll(seachstorelist);
        }
        Log.e("SEARCH000", "--search--22--storeArrayList----" + new Gson().toJson(storeArrayList));
        adapter.notifyDataSetChanged();
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
                if (isBannerStore) {
                    getBannerStore();
                } else {
                    getStores();
                }
            }
        }
    };
}

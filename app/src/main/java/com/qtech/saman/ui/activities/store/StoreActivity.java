package com.qtech.saman.ui.activities.store;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Store;
import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.StoresAdapter;
import com.qtech.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class StoreActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_settings)
    ImageView filter;
    @BindView(R.id.editText_search)
    EditText searchEditText;
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
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.title_store));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        storeArrayList = new ArrayList<>();
        adapter = new StoresAdapter(this, storeArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false,this));
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);

        getStores();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeArrayList = new ArrayList<>();
                adapter = new StoresAdapter(StoreActivity.this, storeArrayList);
                recyclerView.setAdapter(adapter);
                currentPage = 1;
                getStores();
            }
        });
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void getStores() {

        WebServicesHandler.instance.getAllStores(new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {

                if (storeArrayList.size() > 0 && storeArrayList.get(storeArrayList.size()-1)==null) {
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

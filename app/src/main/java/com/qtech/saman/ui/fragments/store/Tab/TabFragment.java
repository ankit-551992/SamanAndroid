package com.qtech.saman.ui.fragments.store.Tab;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.data.model.Store;
import com.qtech.saman.ui.fragments.store.OnLoadMoreListener;
import com.qtech.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFragment extends BaseFragment {

    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    List<Store> storeArrayList = new ArrayList<>();
    CustomAdapter adapter;
    int currentPage = 1;
    boolean isGetAll = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        storeArrayList = new ArrayList<>();
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);


        //Both Solution Working Use any
//        1
        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge,getContext()));
//        2
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_margin);
//        recyclerView.addItemDecoration(itemDecoration);

        //Both Solution Working Use any

        progressBar.setVisibility(View.VISIBLE);

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeArrayList = new ArrayList<>();
                adapter = new CustomAdapter();
                recyclerView.setAdapter(adapter);
                currentPage = 1;
                getData();
            }
        });


        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isGetAll) {
                    storeArrayList.add(null);
                    adapter.notifyItemInserted(storeArrayList.size() - 1);
                    getData();
                }
            }
        });

        return view;
    }


    private void getData() {

        if (storeArrayList.size() > 0) {
            storeArrayList.remove(storeArrayList.size() - 1);
            adapter.notifyItemRemoved(storeArrayList.size());
        }
        for (int i = 0; i < 9; i++) {
            Store store = new Store();
            storeArrayList.add(store);
            isGetAll = true;
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            adapter.setLoaded();
            swipeRefreshLayout.setRefreshing(false);
        }

//        WebServicesHandler.instance.getNews(Constants.student.getId(),currentPage, new retrofit2.Callback<NewsListApi>() {
//            @Override
//            public void onResponse(Call<NewsListApi> call, Response<NewsListApi> response) {
//
//                if(newsArrayList.size()>0) {
//                    newsArrayList.remove(newsArrayList.size() - 1);
//                    adapter.notifyItemRemoved(newsArrayList.size());
//                }
//                NewsListApi newsListApi=response.body();
//
//                if(newsListApi.getLastPage()==currentPage){
//                    isGetAll=true;
//                }else {
//                    currentPage++;
//                }
//                progressBar.setVisibility(View.GONE);
//                newsArrayList.addAll(newsListApi.getNews());
//                adapter.notifyDataSetChanged();
//                adapter.setLoaded();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<NewsListApi> call, Throwable t) {
//            }
//        });
    }

    @Override
    public String getName() {
        return null;
    }


    static class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName;
        private ImageView storeImage;

        public StoreViewHolder(View v) {
            super(v);

            storeImage = (ImageView) v.findViewById(R.id.iv_store_image);
            storeName = (TextView) v.findViewById(R.id.tv_store_name);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;

        public CustomAdapter() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (totalItemCount == 1) {
                        adapter.setLoaded();
                    }

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        isLoading = true;
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }

                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return storeArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_store_row, parent, false);
                return new StoreViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_progress_bar, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof StoreViewHolder) {
                StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return storeArrayList == null ? 0 : storeArrayList.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
    }
}

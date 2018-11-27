package com.algorepublic.saman.ui.activities.store;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.StoreCategory;
import com.algorepublic.saman.data.model.apis.GetAddressApi;
import com.algorepublic.saman.data.model.apis.GetStore;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.StoreCategoriesAdapter;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class StoreDetailActivity extends AppCompatActivity {

    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.iv_store_background)
    ImageView bg;
    @BindView(R.id.iv_store_logo)
    ImageView logo;
    @BindView(R.id.tv_store_name)
    TextView storeNameTextView;
    @BindView(R.id.recyclerView_categories)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<StoreCategory> storeCategoryList = new ArrayList<>();
    StoreCategoriesAdapter storeCategoriesAdapter;

    Store store;


    int function = 0;
    int storeID = 0;
    String storeName = "";
    String storeNameAr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        loading.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.title_store));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            function = bundle.getInt("Function");
            storeID = bundle.getInt("StoreID");
            storeName = bundle.getString("StoreName");
            storeNameAr = bundle.getString("StoreNameAr");

            storeNameTextView.setText(storeName);
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        storeCategoryList = new ArrayList<>();
        storeCategoriesAdapter = new StoreCategoriesAdapter(this, storeCategoryList,storeID,storeName,storeNameAr);
        recyclerView.setAdapter(storeCategoriesAdapter);

        getStore();
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void getStore() {
        WebServicesHandler.instance.getStore(storeID, new retrofit2.Callback<GetStore>() {
            @Override
            public void onResponse(Call<GetStore> call, Response<GetStore> response) {
                GetStore getStore = response.body();
                if (getStore != null) {
                    if (getStore.getSuccess() == 1) {
                        if(getStore.getStore()!=null){
                            store = getStore.getStore();

                            String url=Constants.URLS.BaseURLImages + store.getLogoURL();
                            Picasso.get().load(url)
                                      .transform(new CircleTransform())
                                      .into(logo);

                            Picasso.get().load(Constants.URLS.BaseURLImages + store.getBannerURL()).fit().centerCrop()
                                    .into(bg);

                            storeCategoryList.addAll(store.getStoreCategoryList());
                            storeCategoriesAdapter.notifyDataSetChanged();
                        }
                    }
                }
                storeCategoriesAdapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                    }
                }, 1500);

            }

            @Override
            public void onFailure(Call<GetStore> call, Throwable t) {
            }
        });
    }

}
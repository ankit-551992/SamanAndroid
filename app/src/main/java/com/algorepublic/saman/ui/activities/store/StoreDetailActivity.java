package com.algorepublic.saman.ui.activities.store;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
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
import retrofit2.Call;
import retrofit2.Response;

public class StoreDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_store_background)
    ImageView bg;
    @BindView(R.id.iv_logo)
    ImageView logo;
    @BindView(R.id.tv_store_name)
    TextView storeNameTextView;
    @BindView(R.id.recyclerView_categories)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<StoreCategory> storeCategoryList = new ArrayList<>();
    StoreCategoriesAdapter storeCategoriesAdapter;



    int function = 0;
    int storeID = 0;
    String storeName = "";
    String storeNameAr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);

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

    private void getStore() {
        WebServicesHandler.instance.getStore(storeID, new retrofit2.Callback<GetStore>() {
            @Override
            public void onResponse(Call<GetStore> call, Response<GetStore> response) {
                GetStore getStore = response.body();
                if (getStore != null) {
                    if (getStore.getSuccess() == 1) {
                        if(getStore.getStore()!=null){
                            Store store=getStore.getStore();
                            Picasso.get().load(Constants.URLS.BaseURLImages + store.getLogoURL())
                                    .placeholder(R.drawable.earth_top)
                                    .transform(new CircleTransform())
                                    .into(logo);

                            Picasso.get().load(Constants.URLS.BaseURLImages + store.getBannerURL()).fit().centerCrop()
                                    .placeholder(R.drawable.ic_account_img)
                                    .into(bg);

                            storeCategoryList.addAll(store.getStoreCategoryList());
                            storeCategoriesAdapter.notifyDataSetChanged();
                        }
                    }
                }
                storeCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetStore> call, Throwable t) {
            }
        });
    }

}

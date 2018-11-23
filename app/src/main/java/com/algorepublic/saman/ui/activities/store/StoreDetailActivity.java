package com.algorepublic.saman.ui.activities.store;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.StoreCategory;
import com.algorepublic.saman.ui.adapters.StoreCategoriesAdapter;
import com.algorepublic.saman.utils.GlobalValues;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreDetailActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView_categories)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<StoreCategory> productCategories = new ArrayList<>();
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
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        productCategories = new ArrayList<>();
        productCategories.addAll(GlobalValues.storeCategories);
        storeCategoriesAdapter = new StoreCategoriesAdapter(this, productCategories,storeID,storeName,storeNameAr);
        recyclerView.setAdapter(storeCategoriesAdapter);


    }
}

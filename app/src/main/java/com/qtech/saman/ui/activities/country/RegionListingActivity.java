package com.qtech.saman.ui.activities.country;

import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.ui.adapters.RegionsAdapter;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegionListingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    RelativeLayout loading;
    RecyclerView.LayoutManager layoutManager;
    RegionsAdapter regionsAdapter;

    List<Country> regions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.select_region));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        loading.setVisibility(View.GONE);
        setData();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    private void setData() {
        String[] reg;
        if (SamanApp.isEnglishVersion) {
            reg = getResources().getStringArray(R.array.regions);
        } else {
            reg = getResources().getStringArray(R.array.regions);
        }

        regions = new ArrayList<>();
        for (int i = 0; i < reg.length; i++) {
            Country country = new Country();
            country.setName(reg[i]);
            regions.add(country);
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        regionsAdapter = new RegionsAdapter(this, regions);
        recyclerView.setAdapter(regionsAdapter);
    }
}

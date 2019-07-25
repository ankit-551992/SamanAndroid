package com.algorepublic.saman.ui.activities.country;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.CountriesAdapter;
import com.algorepublic.saman.ui.adapters.RegionsAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

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

        String[] reg = getResources().getStringArray(R.array.regions);
        regions = new ArrayList<>();
        for (int i=0;i<reg.length;i++){
            Country country = new Country();
            country.setName(reg[i]);
            regions.add(country);
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL));
        regionsAdapter = new RegionsAdapter(this, regions);
        recyclerView.setAdapter(regionsAdapter);

    }

    private void getRegions() {
        WebServicesHandler.instance.getCountries(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject JObject = new JSONObject(response.body().string());
                    int status = 0;
                    status = JObject.getInt("success");
                    if (status == 0) {
                        getRegions();
                    } else if (status == 1) {
                        if (JObject.has("result")) {
                            JSONArray jsonArray = JObject.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Country country = new Country();

                                country.setId(jsonObject.getInt("ID"));
                                String flagURL = jsonObject.getString("FlagURL");

                                String[] array = flagURL.split("/");
                                String[] array2 = array[array.length - 1].split("\\.");
                                String[] array3 = array2[0].split("_");
                                String shortNameCode = array3[array3.length - 1];

                                country.setSortname(shortNameCode);
                                country.setName(jsonObject.getString("CountryName"));
                                country.setFlag(Constants.URLS.BaseURLImages + flagURL);
                                country.setPhoneCode(jsonObject.getString("CountryCode"));

                                regions.add(country);
                            }
                        }
                    }

                    regionsAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}

package com.qtech.saman.ui.activities.country;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.CountriesAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CountriesActivity extends BaseActivity {

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
    CountriesAdapter countriesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.select_country));
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
        if (GlobalValues.countries == null) {
            GlobalValues.countries = new ArrayList<>();
//            getCountries();
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        countriesAdapter = new CountriesAdapter(this, GlobalValues.countries,false);
        recyclerView.setAdapter(countriesAdapter);
    }

    private void getCountryList() {

        WebServicesHandler.instance.getAllStores(new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {

            }
            @Override
            public void onFailure(Call<GetStores> call, Throwable t) {

            }
        });
    }

    private void getCountries() {
        try {
            JSONObject obj = new JSONObject(Constants.loadJSONFromAsset(getApplicationContext()));
            JSONArray jsonArray = obj.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country = new Country();

                country.setId(jsonObject.getInt("id"));
                country.setSortname(jsonObject.getString("sortname"));
                country.setName(jsonObject.getString("name"));
                country.setFlag("https://www.saman.om/Flags/flag_" + jsonObject.getString("sortname").toLowerCase() + ".png");
                country.setPhoneCode(jsonObject.getString("phoneCode"));

                GlobalValues.countries.add(country);
            }

            countriesAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

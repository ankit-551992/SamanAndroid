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
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.CountriesAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CountriesListingActivity extends BaseActivity {

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

    int cartlist_code;
    boolean getCode = false;
    int select_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getCode = getIntent().getBooleanExtra("GetCode", false);

        if (getIntent() != null) {
            cartlist_code = getIntent().getIntExtra("cartlist", 0);
        }
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
        if (cartlist_code == 1) {
            GlobalValues.countries = new ArrayList<>();
            getCountriesAPI();
            setAdapter();
        } else {
            if (GlobalValues.countries == null || GlobalValues.countries.size() == 0) {
                GlobalValues.countries = new ArrayList<>();
                getCountries();
                setAdapter();
            } else {
                GlobalValues.countries = new ArrayList<>();
                getCountries();
                setAdapter();
            }
        }
    }

    private void setAdapter() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        countriesAdapter = new CountriesAdapter(this, GlobalValues.countries, getCode);
        recyclerView.setAdapter(countriesAdapter);
        recyclerView.scrollToPosition(select_pos);
    }

    private void getCountries() {
        GlobalValues.countries = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Constants.loadJSONFromAsset(getApplicationContext()));
            JSONArray jsonArray = obj.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country = new Country();
                country.setId(jsonObject.getInt("id"));
                country.setSortname(jsonObject.getString("sortname"));
                country.setName(jsonObject.getString("name"));
                country.setName_AR(jsonObject.getString("name_AR"));
                country.setFlag("https://www.saman.om/Flags/flag_" + jsonObject.getString("sortname").toLowerCase() + ".png");
                country.setPhoneCode("" + jsonObject.getInt("phoneCode"));

                if (GlobalValues.getSelectedCountry(this).equalsIgnoreCase(country.getSortname())) {
                    select_pos = i;
                }
                GlobalValues.countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCountriesAPI() {
        WebServicesHandler.instance.getCountries(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject JObject = new JSONObject(response.body().string());
                    int status = 0;
                    status = JObject.getInt("success");
                    if (status == 0) {
                        getCountriesAPI();
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
                                country.setName_AR(jsonObject.getString("CountryNameAR"));

                                GlobalValues.countries.add(country);
                            }
                        }
                    }
                    countriesAdapter.notifyDataSetChanged();
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

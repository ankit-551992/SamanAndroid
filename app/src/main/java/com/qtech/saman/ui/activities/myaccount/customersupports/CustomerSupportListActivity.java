package com.qtech.saman.ui.activities.myaccount.customersupports;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.CustomerSupportListApi;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.CustomerSupportListAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSupportListActivity extends BaseActivity {

    @BindView(R.id.tv_empty)
    TextView empty;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_customersupport)
    ImageView toolbar_customersupport;

    User authenticatedUser;
    @BindView(R.id.recycler)
    RecyclerView customerRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomerSupportListAdapter customerSupportListAdapter;
    List<CustomerSupportListApi.Result> customerSupportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
        toolbarTitle.setText(getString(R.string.customer_service));
        toolbarBack.setVisibility(View.VISIBLE);
        toolbar_customersupport.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        setHistoryListLayout();
    }

    private void getCustomerListHistory() {
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.getSupportListByUser(authenticatedUser.getId(), new Callback<CustomerSupportListApi>() {
            @Override
            public void onResponse(Call<CustomerSupportListApi> call, Response<CustomerSupportListApi> response) {
                CustomerSupportListApi customerSupportListApi = response.body();
                loading.setVisibility(View.GONE);
                if (customerSupportListApi != null) {
                    if (customerSupportListApi.getResult() != null) {
                        customerSupportList.addAll(customerSupportListApi.getResult());
                    }
                }
                if (customerSupportList.size() < 1) {
                    empty.setVisibility(View.VISIBLE);
                    empty.setText(getResources().getString(R.string.no_items_customer_service));
                }
                customerSupportListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CustomerSupportListApi> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_customersupport)
    public void customerSupportClick() {
        Intent intent = new Intent(this, CustomerSupportActivity.class);
        startActivityForResult(intent, Constants.USERSUPPORT_REQUEST_CODE);
    }

    private void setHistoryListLayout() {
        layoutManager = new LinearLayoutManager(this);
        customerRecyclerView.setLayoutManager(layoutManager);
        customerRecyclerView.setNestedScrollingEnabled(false);
        customerSupportList = new ArrayList<>();
        customerSupportListAdapter = new CustomerSupportListAdapter(this, customerSupportList);
        customerRecyclerView.setAdapter(customerSupportListAdapter);
        getCustomerListHistory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.USERSUPPORT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getCustomerListHistory();
            }
        }
    }
}

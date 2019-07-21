package com.algorepublic.saman.ui.activities.myaccount.myorders;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.OrderHistory;
import com.algorepublic.saman.data.model.OrderTrack;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.OrderHistoryAPI;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.MyOrdersAdapter;
import com.algorepublic.saman.utils.GlobalValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends BaseActivity {


    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.tv_empty)
    TextView empty;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.recycler)
    RecyclerView ordersRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyOrdersAdapter ordersAdapter;

    List<OrderHistory> orderHistoryArrayList = new ArrayList<>();
    User authenticatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
        toolbarTitle.setText(getString(R.string.my_orders));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        setHistoryLayout();
    }

    private void getOrderHistory() {

        WebServicesHandler apiClient = WebServicesHandler.instance;
        Log.e("USERID", "---authenticatedUser--id--" + authenticatedUser.getId());
        apiClient.getOrderHistory(authenticatedUser.getId(), new Callback<OrderHistoryAPI>() {
            @Override
            public void onResponse(Call<OrderHistoryAPI> call, Response<OrderHistoryAPI> response) {

                OrderHistoryAPI orderHistoryAPI = response.body();
                loading.setVisibility(View.GONE);
                if (orderHistoryAPI != null) {
                    if (orderHistoryAPI.getResult() != null) {
                        orderHistoryArrayList.addAll(orderHistoryAPI.getResult());
                    }
                }

                if (orderHistoryArrayList.size() < 1) {
                    empty.setVisibility(View.VISIBLE);
                }

                Collections.sort(orderHistoryArrayList, new Comparator<OrderHistory>() {
                    @Override
                    public int compare(OrderHistory lhs, OrderHistory rhs) {
                        return getDate(rhs.getCreatedAt()).compareTo(getDate(lhs.getCreatedAt()));
                    }
                });
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderHistoryAPI> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }


    private Date getDate(String date) {
        return new Date(Long.parseLong(date.replaceAll("\\D", "")));
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void setHistoryLayout() {

        layoutManager = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(layoutManager);
        ordersRecyclerView.setNestedScrollingEnabled(false);
        orderHistoryArrayList = new ArrayList<>();
        ordersAdapter = new MyOrdersAdapter(this, orderHistoryArrayList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        getOrderHistory();
    }

}

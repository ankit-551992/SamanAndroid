package com.qtech.saman.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.OrderHistory;
import com.qtech.saman.data.model.OrderTrack;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.OrderTrackResponse;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.TrackOrderAdapter;
import com.qtech.saman.utils.GlobalValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TrackingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.recyclerView)
    RecyclerView trackRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    TrackOrderAdapter trackingAdapter;


    @BindView(R.id.tv_order_total)
    TextView orderTotal;
    @BindView(R.id.tv_order_number)
    TextView orderNumberTextView;

    //    List<OrderTrack> orderTrackArrayList = new ArrayList<>();
    HashMap<Integer, List<OrderTrack>> orderTrackArrayList = new HashMap<Integer, List<OrderTrack>>();

    List<Integer> keys;

    OrderHistory orderHistory;
    User authenticatedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
        orderHistory = (OrderHistory) getIntent().getSerializableExtra("Obj");
        toolbarTitle.setText(getString(R.string.track));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        layoutManager = new LinearLayoutManager(this);
        trackRecyclerView.setLayoutManager(layoutManager);
        trackRecyclerView.setNestedScrollingEnabled(false);
        orderTrackArrayList = new HashMap<Integer, List<OrderTrack>>();
        trackingAdapter = new TrackOrderAdapter(this, orderTrackArrayList);
        trackRecyclerView.setAdapter(trackingAdapter);

//        getTrackingInfo();
//        getTrackingData(orderHistory.getID());

        setData();
        orderNumberTextView.setText(orderHistory.getOrderNumber());
        orderTotal.setText(orderHistory.getTotalPrice() + " " + getString(R.string.currency_omr));
    }


    private void setDataOld() {
        for (int i = 0; i < orderHistory.getOrderItems().size(); i++) {
            for (int j = 0; j < orderHistory.getOrderItems().get(i).getOrderTrackList().size(); j++) {
                OrderTrack orderTrack = orderHistory.getOrderItems().get(i).getOrderTrackList().get(j);
                orderTrack.setProductName(orderHistory.getOrderItems().get(i).getProduct().getProductName());
                orderTrack.setProductNameAR(orderHistory.getOrderItems().get(i).getProduct().getProductNameAR());

//                orderTrackArrayList.add(orderTrack);
            }
        }

//        Collections.sort(orderTrackArrayList, new Comparator<OrderTrack>() {
//            @Override
//            public int compare(OrderTrack lhs, OrderTrack rhs) {
//                return getDate(rhs.getDate()).compareTo(getDate(lhs.getDate()));
//            }
//        });

        trackingAdapter.notifyDataSetChanged();
    }

    private void setData() {
        keys = new ArrayList<>();

        for (int i = 0; i < orderHistory.getOrderItems().size(); i++) {

            int key = orderHistory.getOrderItems().get(i).getProduct().getID();

            for (int j = 0; j < orderHistory.getOrderItems().get(i).getOrderTrackList().size(); j++) {

                if (orderTrackArrayList.containsKey(key)) {

                    List<OrderTrack> list = orderTrackArrayList.get(key);
                    OrderTrack orderTrack = orderHistory.getOrderItems().get(i).getOrderTrackList().get(j);
                    orderTrack.setProductName(orderHistory.getOrderItems().get(i).getProduct().getProductName());
                    orderTrack.setProductNameAR(orderHistory.getOrderItems().get(i).getProduct().getProductNameAR());

                    list.add(orderTrack);

                } else {
                    List<OrderTrack> list = new ArrayList<OrderTrack>();
                    OrderTrack orderTrack = orderHistory.getOrderItems().get(i).getOrderTrackList().get(j);
                    orderTrack.setProductName(orderHistory.getOrderItems().get(i).getProduct().getProductName());
                    orderTrack.setProductNameAR(orderHistory.getOrderItems().get(i).getProduct().getProductNameAR());
                    list.add(orderTrack);

                    orderTrackArrayList.put(key, list);

                    keys.add(key);

                }

            }
        }
//        Collections.sort(orderTrackArrayList, new Comparator<OrderTrack>() {
//            @Override
//            public int compare(OrderTrack lhs, OrderTrack rhs) {
//                return getDate(rhs.getDate()).compareTo(getDate(lhs.getDate()));
//            }
//        });

        trackingAdapter.setKeys(keys);
        trackingAdapter.notifyDataSetChanged();
    }

    private Date getDate(String date) {
        return new Date(Long.parseLong(date.replaceAll("\\D", "")));
//        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
//        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
//        Date convertedDate = new Date();
//        try {
//            convertedDate = dateFormat.parse(date);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return convertedDate;
    }


    private void getTrackingData(int orderID) {

        WebServicesHandler.instance.getOrderStatus(orderID, new retrofit2.Callback<OrderTrackResponse>() {
            @Override
            public void onResponse(Call<OrderTrackResponse> call, Response<OrderTrackResponse> response) {
                OrderTrackResponse orderTrackResponse = response.body();

                if (orderTrackResponse != null) {
                    if (orderTrackResponse.getSuccess() == 1) {
                        if (orderTrackResponse.getResult() != null) {
//                            orderTrackArrayList.addAll(orderTrackResponse.getResult());
//                            Collections.reverse(orderTrackArrayList);
//                            trackingAdapter.notifyDataSetChanged();
                        }
                    }
                }

                trackingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderTrackResponse> call, Throwable t) {
            }
        });

    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


}

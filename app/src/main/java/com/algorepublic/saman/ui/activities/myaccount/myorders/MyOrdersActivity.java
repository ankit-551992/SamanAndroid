package com.algorepublic.saman.ui.activities.myaccount.myorders;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Order;
import com.algorepublic.saman.ui.adapters.MyOrdersAdapter;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersActivity extends BaseActivity{


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

    List<Order> orderArrayList = new ArrayList<>();
    List<ListItem> consolidatedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.my_orders));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }


        new SwipeHelper(this, ordersRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.cancel),
                        ResourceUtil.getBitmap(MyOrdersActivity.this,R.drawable.ic_cross),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
            }
        };

        setData();
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void setData(){

        layoutManager = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(layoutManager);
        ordersRecyclerView.setNestedScrollingEnabled(false);
        orderArrayList = new ArrayList<>();
        consolidatedList = new ArrayList<>();
        ordersAdapter = new MyOrdersAdapter(this,consolidatedList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        orderArrayList.add(new Order("name 1", "2016-06-21"));
        orderArrayList.add(new Order("name 2", "2016-06-05"));
        orderArrayList.add(new Order("name 2", "2016-06-05"));
        orderArrayList.add(new Order("name 3", "2016-05-17"));
        orderArrayList.add(new Order("name 3", "2016-05-17"));
        orderArrayList.add(new Order("name 3", "2016-05-17"));
        orderArrayList.add(new Order("name 3", "2016-05-17"));
        orderArrayList.add(new Order("name 2", "2016-06-05"));
        orderArrayList.add(new Order("name 3", "2016-05-17"));

        HashMap<String, List<Order>> groupedHashMap = groupDataIntoHashMap(orderArrayList);


        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);

            for (Order order : groupedHashMap.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setOrder(order);//setBookingDataTabs(bookingDataTabs);
                consolidatedList.add(generalItem);
            }
        }

    }



    private HashMap<String, List<Order>> groupDataIntoHashMap(List<Order> listOfPojosOfJsonArray) {

        HashMap<String, List<Order>> groupedHashMap = new HashMap<>();

        for (Order pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<Order> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        return groupedHashMap;
    }


}

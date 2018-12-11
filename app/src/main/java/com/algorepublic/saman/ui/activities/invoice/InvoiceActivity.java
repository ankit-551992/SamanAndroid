package com.algorepublic.saman.ui.activities.invoice;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.OrderHistory;
import com.algorepublic.saman.data.model.OrderItem;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.adapters.CountriesAdapter;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.ui.adapters.InvoiceAdapter;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceActivity  extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    @BindView(R.id.tv_order_total)
    TextView orderTotalTextView;
    @BindView(R.id.tv_order_number)
    TextView orderNumberTextView;
    @BindView(R.id.tv_order_status)
    TextView orderStatusTextView;
    @BindView(R.id.tv_delivery_date)
    TextView deliveryDateTextView;
    @BindView(R.id.tv_shipment_address)
    TextView shippingAddress;

    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.cart_item_recyclerView)
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<OrderItem> orderItems = new ArrayList<>();
    InvoiceAdapter invoiceAdapter;


    OrderHistory orderHistory;
    User authenticatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_invoice);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        authenticatedUser = GlobalValues.getUser(this);
        orderHistory=(OrderHistory) getIntent().getSerializableExtra("Obj");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.invoice));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }


        if(orderHistory!=null) {
            orderTotalTextView.setText(String.valueOf(orderHistory.getTotalPrice()) + " " + getString(R.string.OMR));
            if (orderHistory.getOrderNumber() != null) {
                orderNumberTextView.setText(orderHistory.getOrderNumber());
            }

            if (orderHistory.getStatus() != null) {
                if (orderHistory.getStatus().equals("0")) {
                    orderStatusTextView.setText(getString(R.string.pending));
                } else if (orderHistory.getStatus().equals("1")) {
                    orderStatusTextView.setText(getString(R.string.received));
                } else {
                    orderStatusTextView.setText(getString(R.string.pending));
                }
            }

            Long dateTimeStamp = Long.parseLong(orderHistory.getDeliveryDate().replaceAll("\\D", ""));
            Date date = new Date(dateTimeStamp);
            DateFormat formatter = new SimpleDateFormat("EEEE, d MMM, yyyy");
            String dateFormatted = formatter.format(date);
            deliveryDateTextView.setText(dateFormatted.toString());

            if (orderHistory.getShippingAddress() != null) {
                String address=orderHistory.getShippingAddress().getAddressLine1().replace(",", "\n\n");
                address=address+"\n\n"+orderHistory.getShippingAddress().getCity();
                address=address+"\n\n"+orderHistory.getShippingAddress().getCountry();
                shippingAddress.setText(address);
            }else{
                shippingAddress.setText("Muscat\n\nOman");
            }
        }

        setBag();
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private void setBag() {
        layoutManager = new LinearLayoutManager(InvoiceActivity.this);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setNestedScrollingEnabled(false);
        orderItems = new ArrayList<>();
        orderItems.addAll(orderHistory.getOrderItems());
        invoiceAdapter = new InvoiceAdapter(InvoiceActivity.this, orderItems);
        cartRecyclerView.setAdapter(invoiceAdapter);

        quantity.setText(orderItems.size()+ " " +getResources().getQuantityString(R.plurals.items, orderItems.size()));
    }


}

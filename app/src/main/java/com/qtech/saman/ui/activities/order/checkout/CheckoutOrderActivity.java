package com.qtech.saman.ui.activities.order.checkout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.apis.PlaceOrderResponse;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.PoliciesActivity;
import com.qtech.saman.ui.adapters.CheckOutProductAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.ResourceUtil;
import com.qtech.saman.utils.SamanApp;
import com.qtech.saman.utils.SwipeHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CheckoutOrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView cross;

    @BindView(R.id.tv_order_total)
    TextView orderTotalTextView;
    @BindView(R.id.tv_order_number)
    TextView orderNumberTextView;
    @BindView(R.id.tv_order_status)
    TextView orderStatusTextView;
    @BindView(R.id.tv_delivery_date)
    TextView deliveryDateTextView;

    //Bag
    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.cart_item_recyclerView)
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    CheckOutProductAdapter checkOutProductAdapter;
    //Bag

    PlaceOrderResponse placeOrderResponse;
    float orderTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out_success));
//        toolbarBack.setVisibility(View.VISIBLE);
        cross.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            placeOrderResponse = (PlaceOrderResponse) getIntent().getSerializableExtra("Response");
            orderTotal = getIntent().getFloatExtra("OrderTotal", 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cross.setImageDrawable(getDrawable(R.drawable.ic_cross));
        } else {
            cross.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross));
        }

        orderTotalTextView.setText(String.valueOf(orderTotal) + " " + getString(R.string.OMR));
        if (placeOrderResponse.getResult().getOrderNumber() != null) {
            orderNumberTextView.setText(placeOrderResponse.getResult().getOrderNumber() + placeOrderResponse.getResult().getId());
        }

        if (placeOrderResponse.getResult().getOrderStatus() != null) {
            if (!placeOrderResponse.getResult().getOrderStatus().equals("")) {
                orderStatusTextView.setText(placeOrderResponse.getResult().getOrderStatus());
            }
        }

        if (placeOrderResponse.getResult().getDeliveryDate() != null) {
            Long dateTimeStamp = Long.parseLong(placeOrderResponse.getResult().getDeliveryDate().replaceAll("\\D", ""));
            Date date = new Date(dateTimeStamp);
            DateFormat formatter = new SimpleDateFormat("EEEE, d MMM, yyyy", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            deliveryDateTextView.setText(dateFormatted.toString());
        }
        setBag();

        new SwipeHelper(this, cartRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.cancel),
                        ResourceUtil.getBitmap(CheckoutOrderActivity.this, R.drawable.ic_cross),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Constants.showAlert(getString(R.string.sorry), getString(R.string.order_cancel_msg), getString(R.string.okay), CheckoutOrderActivity.this);
                            }
                        }
                ));
            }
        };
    }

    Dialog dialog;
    EditText editText;
    RatingBar ratingBar;
    Button sendButton;
    String orderID;

    @OnClick(R.id.iv_survey)
    public void survey() {
        dialog = new Dialog(CheckoutOrderActivity.this);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.dialog_feedback);

        editText = dialog.findViewById(R.id.editText_review);
        ratingBar = dialog.findViewById(R.id.ratting);
        sendButton = dialog.findViewById(R.id.button_feedback);

        if (placeOrderResponse.getResult().getOrderNumber() != null) {
            orderID = placeOrderResponse.getResult().getOrderNumber();
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingBar.getRating() > 0) {
                    updateOrderFeedback(Integer.parseInt(orderID), ratingBar.getRating(), editText.getText().toString());
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(CheckoutOrderActivity.this, getString(R.string.rating_required), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateOrderFeedback(int orderID, float rating, String feedback) {
        WebServicesHandler.instance.updateOrderFeedback(orderID, rating, feedback, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
//                Log.e("RES",""+response.body().getSuccess());
                //    Toast.makeText(CheckoutOrderActivity.this, "Thank you for give me Feedback", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                Log.e("RES", "Failed Feedback Response" + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        GlobalValues.orderPlaced = true;
        if (SamanApp.localDB != null) {
            SamanApp.localDB.clearCart();
        }
        super.onDestroy();
    }

    @OnClick(R.id.toolbar_search)
    void search() {

        GlobalValues.orderPlaced = true;
        if (SamanApp.localDB != null) {
            SamanApp.localDB.clearCart();
        }
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
//        if(SamanApp.localDB!=null){
//            SamanApp.localDB.clearCart();
//        }
//        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        if (SamanApp.localDB != null) {
            SamanApp.localDB.clearCart();
        }
        super.onBackPressed();
    }

    @OnClick(R.id.tv_return_policy)
    void policy() {
        Intent intent = new Intent(CheckoutOrderActivity.this, PoliciesActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
//        new FinestWebView.Builder(CheckoutOrderActivity.this).show(Constants.URLS.returnPolicy);
    }

    @OnClick(R.id.button_cancel_order)
    void cancelOrder() {
        Constants.showAlert(getString(R.string.sorry), getString(R.string.order_cancel_msg), getString(R.string.okay), CheckoutOrderActivity.this);
    }

    private void setBag() {
        layoutManager = new LinearLayoutManager(CheckoutOrderActivity.this);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        checkOutProductAdapter = new CheckOutProductAdapter(CheckoutOrderActivity.this, productArrayList);
        cartRecyclerView.setAdapter(checkOutProductAdapter);

        getDataFromDB();
    }


    private void getDataFromDB() {

        if (SamanApp.localDB != null) {
            productArrayList.addAll(SamanApp.localDB.getCartProducts());
            checkOutProductAdapter.notifyDataSetChanged();
        }

        quantity.setText(productArrayList.size() + " " + getResources().getQuantityString(R.plurals.items, productArrayList.size()));
        if (SamanApp.localDB != null) {
            SamanApp.localDB.clearCart();
        }
    }
}